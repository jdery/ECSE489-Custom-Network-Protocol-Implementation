/**
 * 
 */
package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppCreateState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppCreateState(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			this.printHeader("Creating a new user!");
			
			if (this.backPointerApp.isUserLoggedIn()) {
				System.out.println("In order to create an account you need to be logged out!");
				this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
				this.pressEnterToContinue();
				return;
			}	

			String username, password;

			do {
				System.out.println("Enter valid credentials in order to create the user.");
				System.out.print("Username: ");
				username = bufferedReader.readLine();
				System.out.print("Password: ");
				password = bufferedReader.readLine();
			} while (!validateCredentials(username, username));

			if (this.createUserAccount(username, password)) {
				this.createUserStore();
			}
			
			this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
			this.pressEnterToContinue();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the account of a user and handles the response.
	 * 
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	private boolean createUserAccount(String username, String password) throws Exception {
		
		Message responseFromServer;
		// This will ensure that only one thread at a time can send requests and retrieve the associated responses.
		synchronized(App.LOCK) {
			// Sends the message to create the user in the database.
			this.sendMessage(new Message(MessageType.CREATE_USER, 0, username + "," + password));
			do {
				responseFromServer = this.readMessage();
			} while (responseFromServer == null);
		}

		// Verify the response from the user.
		if (responseFromServer.getMessageType() == MessageType.CREATE_USER) {
			switch (responseFromServer.getSubMessageType()) {
			case 0:
				System.out.println("The user was successfully created!");
				this.loginUser(username, password);
				return (true);
			case 1:
				System.out.println("The user already exists!");
				break;
			case 2:
				System.out.println("The user is already logged in!");
				break;
			case 3:
				System.out.println("Badly formatted message!");
				break;
			}
		} else {
			System.out.println("An unexpected response was received: " + responseFromServer.toString());
		}
		
		return (false);
	}

	/**
	 * Creates the Store of the user.
	 * 
	 * @throws Exception
	 */
	private void createUserStore() throws Exception {
		Message responseFromServer;
		synchronized(App.LOCK) {
			// Sends the message to create the user in the database.
			this.sendMessage(Message.MessageFactory(DefaultMessages.CREATE_STORE));
			do {
				responseFromServer = this.readMessage();
			} while (responseFromServer == null);
		}

		// Verify the response from the user.
		if (responseFromServer.getMessageType() == MessageType.CREATE_STORE) {
			switch (responseFromServer.getSubMessageType()) {
			case 0:
				System.out.println("The user store was created successfully!");
				break;
			case 1:
				System.out.println("The user store already exists!");
				break;
			case 2:
				System.out.println("The user is not logged in!");
				break;
			}
		} else {
			System.out.println("An unexpected response was received: " + responseFromServer.toString());
		}
	}
}
