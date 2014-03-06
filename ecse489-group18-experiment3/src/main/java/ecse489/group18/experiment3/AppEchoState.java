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
public class AppEchoState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppEchoState(App backPointerApp,
			InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		this.printHeader("Echo message sent!");
		
		try {
			this.sendMessage(Message.MessageFactory(DefaultMessages.ECHO));
			Message responseFromServer = this.readMessage();
			System.out.println("Response from server: " + responseFromServer.toString());
			
			this.pressEnterToContinue();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
	}
}
