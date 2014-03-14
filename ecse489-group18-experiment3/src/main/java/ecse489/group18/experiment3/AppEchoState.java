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
public class AppEchoState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppEchoState(App backPointerApp, BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		Helpers.printHeader("Echo message sent!");
		
		try {
			Message responseFromServer;
			// This will ensure that only one thread at a time can send requests and retrieve the associated responses.
			synchronized(App.LOCK) {
				this.sendMessage(Message.MessageFactory(DefaultMessages.ECHO));
				responseFromServer = this.readResponseFromServer();
			}
			System.out.println("Response from server: " + responseFromServer.toString());
			
			this.pressEnterToContinue();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
	}
}
