package ecse489.group18.experiment3;

import java.net.Socket;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class Client {
	/**
	 * The server's port number on which it will be listening.
	 */
	private static final int SERVER_PORT = 5000;
	/**
	 * The server's address.
	 */
	private static final String SERVER_ADDRESS = "dsp2014.ece.mcgill.ca";

	public static void main(String[] args) {
		Socket serverSocket;
		App myApp;
		
		try {
			serverSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			myApp = new App(serverSocket);
		} catch (Exception e) {
			System.err.println("An error occured while trying to connect to the server.");
			e.printStackTrace();
			return;
		}
		
		Thread processingThread = new Thread(myApp);
		processingThread.start();
	}
}
