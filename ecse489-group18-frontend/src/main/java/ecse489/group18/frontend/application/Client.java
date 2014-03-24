package ecse489.group18.frontend.application;


/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class Client {
	/**
	 * The server's port number on which it will be listening.
	 */
	public static int SERVER_PORT = 5000;
	/**
	 * The server's address.
	 */
	public static String SERVER_ADDRESS = "localhost";
//	public static String SERVER_ADDRESS = "dsp2014.ece.mcgill.ca";

	public static void main(String[] args) {
		App myApp;
		
		try {
			// Verifies that the number of arguments are correct and if not displays a message.
			int numberOfArguments = args.length;
			if (numberOfArguments != 0 && numberOfArguments != 2) {
				System.err.println("You did not entered the proper amount of arguments!");
				System.out.println("How to use this :");
				System.out.println("If you enter no arguments, the IP='" + SERVER_ADDRESS + "' and port='" + SERVER_PORT + "'");
				System.out.println("If you enter two arguments, the IP first and then the port number.");
				return;
			} else if (numberOfArguments == 2) {
				SERVER_ADDRESS = args[0];
				SERVER_PORT = Integer.parseInt(args[1]);
			}
			
			System.out.println("Enabling the connection with the server...");
			myApp = new App(SERVER_ADDRESS, SERVER_PORT);
		} catch (Exception e) {
			System.err.println("An error occured while trying to connect to the server.");
			e.printStackTrace();
			System.err.println("Terminating the application...");
			return;
		}
		
		Thread processingThread = new Thread(myApp);
		processingThread.start();
	}
}
