/**
 * 
 */
package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppLoginState extends AppState {

	public AppLoginState(App backPointerApp,
			InputStreamReader socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			this.printHeader("Login Menu");
			System.out.println("Enter your credentials in order to login.\nEnter an empty string to go back to the main menu.");
			//TODO: make sure that a user can quit this menu...
			String username, password;
			
			do {
				System.out.print("Username: ");
				username = bufferedReader.readLine();
			} while(!validCredential(username));
			
			do {
				System.out.print("Password: ");
				password = bufferedReader.readLine();
			} while(!validCredential(password));
			
			this.sendMessage(new Message(MessageType.LOGIN, 0, username + "," + password));
			Message responseFromServer = this.readMessage();
			System.out.println("Response from server: " + responseFromServer.toString());
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		this.backPointerApp.changeCurrentState(backPointerApp.loggedState);
	}
	
	/**
	 * Will validate that the credential are in the right format.
	 * 
	 * @param credential The credential (either password or username) to be validated.
	 * @return True if valid and false otherwise.
	 */
	private boolean validCredential(String credential) {
		if (credential == null || credential.length() == 0) {
			return (false);
		}
		return (true);
	}
}
