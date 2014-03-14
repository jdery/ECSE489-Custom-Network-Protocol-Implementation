/**
 * 
 */
package ecse489.group18.frontend;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppCheckMessagesState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppCheckMessagesState(App backPointerApp, BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			Helpers.printHeader("Messages!!!");
			
			if (!this.backPointerApp.isUserLoggedIn()) {
				System.out.println("You are not logged in!");
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				this.pressEnterToContinue();
				return;
			}
			
			String messages = this.backPointerApp.getMessagesFromPollingThread();
			if (messages == null) {
				System.out.println("The polling thread is not running yet! You need to be logged in!");
			} else {
				System.out.println(messages);
			}
			this.pressEnterToContinue();
			this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
