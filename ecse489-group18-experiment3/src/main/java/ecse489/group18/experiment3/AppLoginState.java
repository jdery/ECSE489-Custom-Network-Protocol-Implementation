package ecse489.group18.experiment3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.OutputStream;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppLoginState extends AppState {

	public AppLoginState(App backPointerApp, BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			this.printHeader("Login Menu");
			
			if (this.backPointerApp.isUserLoggedIn()) {
				System.out.println("The user is already logged in!");
				this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
				this.pressEnterToContinue();
				return;
			}
			
			String username, password;

			do {
				System.out.println("Enter valid credentials in order to login.");
				System.out.print("Username: ");
				username = bufferedReader.readLine();
				System.out.print("Password: ");
				password = bufferedReader.readLine();
			} while (!validateCredentials(username, username));

			if (this.loginUser(username, password)) {
//				this.backPointerApp.setIsUserLoggedIn(true);
//				this.backPointerApp.startPollingMessages();
			}
			
			this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
			this.pressEnterToContinue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
