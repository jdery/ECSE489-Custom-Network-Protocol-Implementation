package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

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

	private AppState currentState;
	public AppState loginState, loggedState, mainState, echoState, exitState, createState;

	public App(String serverAddress, int serverPort) throws Exception {
		serverSocket = new Socket(serverAddress, serverPort);
		socketInputStream = serverSocket.getInputStream();
		socketOutputStream = serverSocket.getOutputStream();
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		loginState = new AppLoginState(this, socketInputStream, socketOutputStream, bufferedReader);
		loggedState = new AppLoggedState(this, socketInputStream, socketOutputStream, bufferedReader);
		mainState = new AppMainState(this, socketInputStream, socketOutputStream, bufferedReader);
		echoState = new AppEchoState(this, socketInputStream, socketOutputStream, bufferedReader);
		exitState = new AppExitState(this, socketInputStream, socketOutputStream, bufferedReader);
		createState = new AppCreateState(this, socketInputStream, socketOutputStream, bufferedReader);
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
}
