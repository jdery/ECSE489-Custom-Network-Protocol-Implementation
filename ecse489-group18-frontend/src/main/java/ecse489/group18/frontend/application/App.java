package ecse489.group18.frontend.application;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

import ecse489.group18.frontend.application.states.AppCheckFilesState;
import ecse489.group18.frontend.application.states.AppCheckMessagesState;
import ecse489.group18.frontend.application.states.AppCreateState;
import ecse489.group18.frontend.application.states.AppDeleteState;
import ecse489.group18.frontend.application.states.AppEchoState;
import ecse489.group18.frontend.application.states.AppExitState;
import ecse489.group18.frontend.application.states.AppLoginState;
import ecse489.group18.frontend.application.states.AppLogoutState;
import ecse489.group18.frontend.application.states.AppMainState;
import ecse489.group18.frontend.application.states.AppSendFileState;
import ecse489.group18.frontend.application.states.AppSendMessageState;
import ecse489.group18.frontend.application.states.AppState;
import ecse489.group18.frontend.application.states.AppStates;
import ecse489.group18.frontend.application.states.AppUserPollForFilesState;
import ecse489.group18.frontend.application.states.AppUserPollingState;

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
	private Thread pollForFilesThread;
	private boolean isUserLoggedIn = false;

	private AppState currentState;
	private AppUserPollingState userPolling;
	private AppUserPollForFilesState userPollForFiles;
	private AppState loginState, mainState, echoState, exitState, createState, logoutState, deleteState;
	private AppState appCheckMessagesState, appCheckFilesState, appSendMessageState, appSendFileState;

	public App(String serverAddress, int serverPort) throws Exception {
//		// Sets the system variable required to handle the certificate.
		System.setProperty("javax.net.ssl.trustStore", "cacerts.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "ThisIsOurPass123");
		
		// Creates the SSL connection with the known certificate.
	    SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
	    this.serverSocket = ssf.createSocket(serverAddress, serverPort);
	    // The timeout is used here so that we don't block on read calls.
	    // it is critical since the application normally stalls on these BufferedInputStream.read().
	    this.serverSocket.setSoTimeout(200);
//	    this.serverSocket = new Socket(serverAddress, serverPort);
		
		bufferedInputStream = new BufferedInputStream(serverSocket.getInputStream());
		socketOutputStream = serverSocket.getOutputStream();
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		this.createAllStates();
		currentState = mainState;
	}
	
	/**
	 * 
	 * @return True if a user is logged in and false otherwise.
	 */
	public boolean isUserLoggedIn() {
		return (this.isUserLoggedIn);
	}
	
	/**
	 * Will change the state of the App to when the user is logged in.
	 * 
	 * @param username The username of the user.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public void setUserToLoggedIn() throws UnknownHostException, IOException {
		this.isUserLoggedIn = true;
		this.startPollingFiles();
		this.startPollingMessages();
	}
	
	/**
	 * Will change the state of the App to when the user is logged out.
	 */
	public void setUserToLoggedOut() {
		this.isUserLoggedIn = false;
		this.stopPollingFiles();
		this.stopPollingMessages();
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
		appCheckFilesState = new AppCheckFilesState(this, bufferedInputStream, socketOutputStream, bufferedReader);
		appSendMessageState = new AppSendMessageState(this, bufferedInputStream, socketOutputStream, bufferedReader);
		appSendFileState = new AppSendFileState(this, bufferedInputStream, socketOutputStream, bufferedReader);
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
		case CHECK_FILES:
			this.currentState = this.appCheckFilesState;
			break;
		case SEND_MESSAGE:
			this.currentState = this.appSendMessageState;
			break;
		case SEND_FILE:
			this.currentState = this.appSendFileState;
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
	private void startPollingMessages() throws UnknownHostException, IOException {
		this.userPolling = new AppUserPollingState(this, this.bufferedInputStream, this.socketOutputStream, bufferedReader);
		pollingThread = new Thread(this.userPolling);
		pollingThread.start();
	}
		
	/**
	 * Will stop polling messages for the current user.
	 */
	private void stopPollingMessages() {
		// This lock will let the polling thread complete a full cycle (which includes reading the BufferedInputReader for a response).
		synchronized(App.LOCK) {
			if (this.userPolling != null) {
				this.pollingThread.interrupt();
				this.userPolling = null;
			}
		}
	}
	
	/**
	 * Will start polling messages for the currently logged in user.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	private void startPollingFiles() throws UnknownHostException, IOException {
		this.userPollForFiles = new AppUserPollForFilesState(this, this.bufferedInputStream, this.socketOutputStream, bufferedReader);
		pollForFilesThread = new Thread(this.userPollForFiles);
		pollForFilesThread.start();
	}
		
	/**
	 * Will stop polling messages for the current user.
	 */
	private void stopPollingFiles() {
		// This lock will let the polling thread complete a full cycle (which includes reading the BufferedInputReader for a response).
		synchronized(App.LOCK) {
			if (this.userPollForFiles != null) {
				this.pollForFilesThread.interrupt();
				this.userPollForFiles = null;
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
	
	/**
	 * Will get the messages from the polling thread.
	 * 
	 * @return The messages ready to be printed on the console.
	 */
	public String getFilenameMessagesFromPollingThread() {
		if (this.userPollForFiles == null) {
			return (null);
		} else {
			return (this.userPollForFiles.getFilenameMessages());
		}
	}
	
	public String getFilenameFromPollingThread(int index) throws IndexOutOfBoundsException {
		if (this.userPollForFiles == null) {
			return null;
		}
		return this.userPollForFiles.getFilenameForIndex(index);
	}
	
	public byte[] getFilenameFileFromPollingThread(int index) {
		if (this.userPollForFiles == null) {
			return null;
		}
		return this.userPollForFiles.getFileForIndex(index);
	}
}
