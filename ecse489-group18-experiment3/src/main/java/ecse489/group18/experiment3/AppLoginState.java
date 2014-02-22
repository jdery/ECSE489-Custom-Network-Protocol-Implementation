/**
 * 
 */
package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppLoginState extends AppState {
	
	private final String COMMA_REGEX = ".*[,].*";

	public AppLoginState(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			this.printHeader("Login Menu");
			
			String username, password;

			do {
				System.out.println("Enter valid credentials in order to login.");
				System.out.print("Username: ");
				username = bufferedReader.readLine();
				System.out.print("Password: ");
				password = bufferedReader.readLine();
			} while (!validateCredentials(username, username));

			this.sendMessage(new Message(MessageType.LOGIN, 0, username + "," + password));
			Message responseFromServer = this.readMessage();

			if (responseFromServer.getMessageType() == MessageType.LOGIN) {
				switch(responseFromServer.getSubMessageType()) {
				case 0:
					System.out.println("You were successfully authenticated!");
					break;
				case 1:
					System.out.println("You are already logged in!");
					break;
				case 2:
					System.out.println("Bad credentials!");
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

	/**
	 * Will validate that the credential are in the right format.
	 * 
	 * @param credential
	 *            The credential (either password or username) to be validated.
	 * @return True if valid and false otherwise.
	 */
	private boolean validateCredentials(String username, String password) {
		if (!validateCredential(username) || !validateCredential(password)) {
			return (false);
		}
		return (true);
	}

	/**
	 * Validate the credentials.
	 * 
	 * @param credential
	 *            The credentials to be validated.
	 * @return True if valid and false otherwise.
	 */
	private boolean validateCredential(String credential) {
		if (credential == null || credential.length() == 0) {
			return (false);
		}
		if (Pattern.matches(COMMA_REGEX, credential)) {
			return (false);
		}
		return (true);
	}
}
