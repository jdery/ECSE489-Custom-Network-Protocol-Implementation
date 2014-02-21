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
			System.out.println("Enter your credentials in order to login.");
			
			System.out.print("Username: ");
			String username = bufferedReader.readLine();
			if (!validCredential(username)) {
				System.err.println("You must enter a valid username!");
			}
			
			System.out.print("Password: ");
			String password = bufferedReader.readLine();
			if (!validCredential(password)) {
				System.err.println("You must enter a valid password!");
			}
			
			this.sendMessage(new Message(MessageType.LOGIN, 0, username + "," + password));
			Message responseFromServer = this.readMessage();
			System.out.println(responseFromServer.toString());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//this.backPointerApp.changeCurrentState(backPointerApp.loggedState);
	}
	
	/**
	 * Will validate that the credential are in the right format.
	 * 
	 * @param credential The credential (either password or username) to be validated.
	 * @return True if valid and false otherwise.
	 */
	private boolean validCredential(String credential) {
		// TODO: to be implemented.
		return (true);
	}
}
