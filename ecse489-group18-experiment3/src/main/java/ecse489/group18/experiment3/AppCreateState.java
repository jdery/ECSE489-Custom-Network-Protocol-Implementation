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
	public AppCreateState(App backPointerApp,
			InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			this.printHeader("Creating a new user!");
			
			String username, password;

			do {
				System.out.println("Enter valid credentials in order to create the user.");
				System.out.print("Username: ");
				username = bufferedReader.readLine();
				System.out.print("Password: ");
				password = bufferedReader.readLine();
			} while (!validateCredentials(username, username));

			this.sendMessage(new Message(MessageType.CREATE_USER, 0, username + "," + password));
			Message responseFromServer = this.readMessage();

			if (responseFromServer.getMessageType() == MessageType.CREATE_USER) {
				switch(responseFromServer.getSubMessageType()) {
				case 0:
					System.out.println("The user was successfully created!");
					break;
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
				this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
			} else {
				System.out.println("An unexpected response was received: " + responseFromServer.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
