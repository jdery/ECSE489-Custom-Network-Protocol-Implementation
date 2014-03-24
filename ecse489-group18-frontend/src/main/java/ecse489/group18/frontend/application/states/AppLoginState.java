package ecse489.group18.frontend.application.states;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.OutputStream;

import ecse489.group18.frontend.application.App;
import ecse489.group18.frontend.application.Helpers;

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
			Helpers.printHeader("Login Menu");
			
			if (this.backPointerApp.isUserLoggedIn()) {
				System.out.println("The user is already logged in!");
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
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
			} while (!Helpers.validateCredentials(username, username));
			
			this.loginUser(username, password);
			
			this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
			this.pressEnterToContinue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
