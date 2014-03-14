/**
 * 
 */
package ecse489.group18.experiment3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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
	public AppMainState(App backPointerApp, BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
	}

	/**
	 * Prints the main menu lol.
	 */
	private void printMainMenu() {
		this.printHeader("Welcome to the main menu!");
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
			
			switch(nextStateValue) {
			case 1:
				this.backPointerApp.changeCurrentState(this.backPointerApp.loginState);
				break;
			case 2:
				this.backPointerApp.changeCurrentState(this.backPointerApp.logoutState);
				break;
			case 3:
				this.backPointerApp.changeCurrentState(this.backPointerApp.createState);
				break;
			case 4:
				this.backPointerApp.changeCurrentState(this.backPointerApp.deleteState);
				break;
			case 5:
				this.backPointerApp.changeCurrentState(this.backPointerApp.echoState);
				break;
			case 6:
				this.backPointerApp.changeCurrentState(this.backPointerApp.appSendMessageState);
				break;
			case 7:
				this.backPointerApp.changeCurrentState(this.backPointerApp.appCheckMessagesState);
				break;
			case 8:
				this.backPointerApp.changeCurrentState(this.backPointerApp.exitState);
				break;
			default:
				System.out.println("Enter a valid option!");
				return (false);
			}
		} catch(NumberFormatException e) {
			System.out.println("Enter an integer value!");
			return (false);
		}
		
		return (true);
	}
}
