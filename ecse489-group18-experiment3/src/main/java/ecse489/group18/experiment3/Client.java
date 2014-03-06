package ecse489.group18.experiment3;


/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class Client {
	/**
	 * The server's port number on which it will be listening.
	 */
	public static final int SERVER_PORT = 5000;
	/**
	 * The server's address.
	 */
	public static final String SERVER_ADDRESS = "dsp2014.ece.mcgill.ca";

	public static void main(String[] args) {
		App myApp;
		
		try {
			System.out.println("Enabling the connection with the server...");
			myApp = new App(SERVER_ADDRESS, SERVER_PORT);
			System.out.println("The connection was established successfully.");
		} catch (Exception e) {
			System.err.println("An error occured while trying to connect to the server.");
			e.printStackTrace();
			return;
		}
		
		Thread processingThread = new Thread(myApp);
		processingThread.start();
	}
}
