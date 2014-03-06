/**
 * 
 */
package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
	public AppCheckMessagesState(App backPointerApp,
			InputStream socketInputStream, OutputStream socketOutputStream,
			BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			this.printHeader("Messages!!!");
			String messages = this.backPointerApp.getMessagesFromPollingThread();
			if (messages == null) {
				System.out.println("The polling thread is not running yet! You need to be logged in!");
			} else {
				System.out.println(messages);
			}
			this.pressEnterToContinue();
			this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
