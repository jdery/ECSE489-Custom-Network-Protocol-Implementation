package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppDeleteState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppDeleteState(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			this.printHeader("Deleting a user!");

			Message responseFromServer;
			// This will ensure that only one thread at a time can send requests and retrieve the associated responses.
			synchronized(App.LOCK) {
				this.sendMessage(Message.MessageFactory(DefaultMessages.DELETE_USER));
				responseFromServer = this.readMessage();
			}

			if (responseFromServer != null && responseFromServer.getMessageType() == MessageType.DELETE_USER) {
				switch(responseFromServer.getSubMessageType()) {
				case 0:
					System.out.println("The user was successfully deleted!");
					break;
				case 1:
					System.out.println("The user is not logged in!");
					break;
				default:
					System.out.println("Something went wrong in the process...");
				}
				this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
			} else if (responseFromServer != null) {
				System.err.println("An unexpected response was received: " + responseFromServer.toString());
			} else {
				System.err.println("No response from the server was received...");
			}
			
			this.pressEnterToContinue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
