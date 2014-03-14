package ecse489.group18.frontend;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
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
	
	public static final Object LOCK = new Object();

	private BufferedInputStream bufferedInputStream;
	private OutputStream socketOutputStream;
	private BufferedReader bufferedReader;
	private Socket serverSocket;
	private Thread pollingThread;
	private boolean isUserLoggedIn = false;

	private AppState currentState;
	private AppUserPollingState userPolling;
	private AppState loginState, mainState, echoState, exitState, createState, logoutState, deleteState;
	private AppState appCheckMessagesState, appSendMessageState;

	public App(String serverAddress, int serverPort) throws Exception {
		serverSocket = new Socket(serverAddress, serverPort);
		bufferedInputStream = new BufferedInputStream(serverSocket.getInputStream());
		socketOutputStream = serverSocket.getOutputStream();
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		this.createAllStates();
		currentState = mainState;
	}
	
	public boolean isUserLoggedIn() {
		return (this.isUserLoggedIn);
	}
	
	public void setIsUserLoggedIn(boolean isUserLoggedIn) {
		this.isUserLoggedIn = isUserLoggedIn;
	}
	
	/**
	 * Creates all the states that will be used for the FSM.
	 */
	private void createAllStates() {
		loginState = new AppLoginState(this, bufferedInputStream, socketOutputStream, bufferedReader);
		mainState = new AppMainState(this, bufferedInputStream, socketOutputStream, bufferedReader);
		echoState = new AppEchoState(this, bufferedInputStream, socketOutputStream, bufferedReader);
		exitState = new AppExitState(this, bufferedInputStream, socketOutputStream, bufferedReader);
		createState = new AppCreateState(this, bufferedInputStream, socketOutputStream, bufferedReader);
		logoutState = new AppLogoutState(this, bufferedInputStream, socketOutputStream, bufferedReader);
		deleteState = new AppDeleteState(this, bufferedInputStream, socketOutputStream, bufferedReader);
		appCheckMessagesState = new AppCheckMessagesState(this, bufferedInputStream, socketOutputStream, bufferedReader);
		appSendMessageState = new AppSendMessageState(this, bufferedInputStream, socketOutputStream, bufferedReader);
	}

	/**
	 * Will execute the current state of the FSM.
	 */
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
	public void changeCurrentState(AppStates nextState) {
		switch(nextState) {
		case LOGIN:
			this.currentState = this.loginState;
			break;
		case MAIN_MENU:
			this.currentState = this.mainState;
			break;
		case SEND_ECHO:
			this.currentState = this.echoState;
			break;
		case EXIT_APPLICATION:
			this.currentState = this.exitState;
			break;
		case CREATE_USER:
			this.currentState = this.createState;
			break;
		case LOGOUT_USER:
			this.currentState = this.logoutState;
			break;
		case DELETE_USER:
			this.currentState = this.deleteState;
			break;
		case CHECK_RECEIVED_MESSAGES:
			this.currentState = this.appCheckMessagesState;
			break;
		case SEND_MESSAGE:
			this.currentState = this.appSendMessageState;
			break;
		default:
			this.currentState = this.mainState;
		}
	}
	
	/**
	 * Will start polling messages for the currently logged in user.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public void startPollingMessages() throws UnknownHostException, IOException {
		this.userPolling = new AppUserPollingState(this, this.bufferedInputStream, this.socketOutputStream, bufferedReader);
		pollingThread = new Thread(this.userPolling);
		pollingThread.start();
	}
		
	/**
	 * Will stop polling messages for the current user.
	 */
	public void stopPollingMessages() {
		// This lock will let the polling thread complete a full cycle (which includes reading the BufferedInputReader for a response).
		synchronized(App.LOCK) {
			if (this.userPolling != null) {
				this.pollingThread.interrupt();
				this.userPolling = null;
			}
		}
	}
	
	/**
	 * Will get the messages from the polling thread.
	 * 
	 * @return The messages ready to be printed on the console.
	 */
	public String getMessagesFromPollingThread() {
		if (this.userPolling == null) {
			return (null);
		} else {
			return (this.userPolling.getMessages());
		}
	}
}
