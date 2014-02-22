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
public class AppMainState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppMainState(App backPointerApp,
			InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	/**
	 * Prints the main menu lol.
	 */
	private void printMainMenu() {
		this.printHeader("Welcome to the main menu!");
		System.out.println("1 - Log into your account");
		System.out.println("2 - Create a new account");
		System.out.println("3 - Delete an account");
		System.out.println("4 - Send echo message to server");
		System.out.println("5 - Exit");
	}

	@Override
	public void execute() {
		try {
			String userInput;
			do {
				printMainMenu();
				userInput = bufferedReader.readLine();
			} while (!switchState(userInput));
		} catch (Exception e) {
			System.err.println("An error occured while reading your input.");
			e.printStackTrace();
		}
	}

	/**
	 * Switch the current state based on the user's input.
	 * 
	 * @param nextState
	 *            The input to be considered.
	 * @return True if it is a valid input and false otherwise.
	 */
	private boolean switchState(String nextState) {
		if (nextState.equals("1")) {
			this.backPointerApp.changeCurrentState(this.backPointerApp.loginState);
		} else if (nextState.equals("2")) {

		} else if (nextState.equals("3")) {

		} else if (nextState.equals("4")) {
			this.backPointerApp.changeCurrentState(this.backPointerApp.echoState);
		} else if (nextState.equals("5")) {

		} else {
			return (false);
		}

		return (true);
	}
}
