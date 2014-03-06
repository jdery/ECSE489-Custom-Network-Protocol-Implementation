package ecse489.group18.experiment3.ecse489;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class App implements Runnable {

	private InputStream socketInputStream;
	private OutputStream socketOutputStream;
	private BufferedReader bufferedReader;
	private Socket serverSocket;
	private Thread pollingThread;
	private String serverAddress;
	private int serverPort;

	private AppState currentState;
	public AppState loginState, mainState, echoState, exitState, createState, logoutState, deleteState;

	public App(String serverAddress, int serverPort) throws Exception {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		serverSocket = new Socket(serverAddress, serverPort);
		socketInputStream = serverSocket.getInputStream();
		socketOutputStream = serverSocket.getOutputStream();
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		loginState = new AppLoginState(this, socketInputStream, socketOutputStream, bufferedReader);
		mainState = new AppMainState(this, socketInputStream, socketOutputStream, bufferedReader);
		echoState = new AppEchoState(this, socketInputStream, socketOutputStream, bufferedReader);
		exitState = new AppExitState(this, socketInputStream, socketOutputStream, bufferedReader);
		createState = new AppCreateState(this, socketInputStream, socketOutputStream, bufferedReader);
		logoutState = new AppLogoutState(this, socketInputStream, socketOutputStream, bufferedReader);
		deleteState = new AppDeleteState(this, socketInputStream, socketOutputStream, bufferedReader);
		currentState = mainState;
	}

	public void run() {
		while (true) {
			currentState.execute();
		}
	}

	/**
	 * Changes the current state of the FSM.
	 * 
	 * @param nextState
	 *            The next state that the FSM will be in.
	 */
	public void changeCurrentState(AppState nextState) {
		this.currentState = nextState;
	}
	
	/**
	 * Will start polling messages for the currently logged in user.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public void startPollingMessages() throws UnknownHostException, IOException {
		Socket serverSocket = new Socket(this.serverAddress, this.serverPort);
		InputStream socketInputStream = serverSocket.getInputStream();
		OutputStream socketOutputStream = serverSocket.getOutputStream();
		
		AppUserPollingState userPolling = new AppUserPollingState(this, socketInputStream, socketOutputStream, bufferedReader);
		pollingThread = new Thread(userPolling);
		pollingThread.start();
	}
	
	/**
	 * Will stop polling messages for the current user.
	 */
	public void stopPollingMessages() {
		pollingThread.interrupt();
	}
}
