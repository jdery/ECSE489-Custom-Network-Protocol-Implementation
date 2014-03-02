package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppLoginState extends AppState {

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

			this.loginUser(username, password);
			
			this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
			this.pressEnterToContinue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
