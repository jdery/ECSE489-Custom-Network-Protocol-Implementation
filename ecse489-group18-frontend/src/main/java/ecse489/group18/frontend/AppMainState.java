/**
 * 
 */
package ecse489.group18.frontend;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.util.NoSuchElementException;

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
	public AppMainState(App backPointerApp, BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
	}

	/**
	 * Prints the main menu.
	 */
	private void printMainMenu() {
		Helpers.printHeader("Welcome to the main menu!");
		System.out.println("1 - Log into your account");
		System.out.println("2 - Logout of account");
		System.out.println("3 - Create a new account");
		System.out.println("4 - Delete an account");
		System.out.println("5 - Send echo message to server");
		System.out.println("6 - Send a message to a user");
		System.out.println("7 - Check current messages");
		System.out.println("8 - Exit");
		System.out.print("Enter an option: ");
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
		try {
			int nextStateValue = Integer.parseInt(nextState);
			AppStates nexState = AppStates.getEnum(nextStateValue);
			this.backPointerApp.changeCurrentState(nexState);
		} catch(NumberFormatException e) {
			System.out.println("You must enter an integer value!");
			return (false);
		} catch(NoSuchElementException e) {
			System.out.println("You must enter a valid value!");
			return (false);
		}
		
		return (true);
	}
}
