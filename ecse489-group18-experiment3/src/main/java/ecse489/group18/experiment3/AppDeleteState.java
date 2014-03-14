package ecse489.group18.experiment3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.util.Vector;

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
	public AppDeleteState(App backPointerApp, BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			Helpers.printHeader("Deleting a user!");
			
			if (!this.backPointerApp.isUserLoggedIn()) {
				System.out.println("You are not logged in!");
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				this.pressEnterToContinue();
				return;
			}

			Vector<Message> responsesFromServer;
			// This will ensure that only one thread at a time can send requests and retrieve the associated responses.
			synchronized(App.LOCK) {
				this.sendMessage(Message.MessageFactory(DefaultMessages.DELETE_USER));
				responsesFromServer = this.readMultipleResponsesFromServer();
			}

			for (Message responseFromServer : responsesFromServer) {
				if (responseFromServer != null && responseFromServer.getMessageType() == MessageType.DELETE_USER) {
					switch(responseFromServer.getSubMessageType()) {
					case 0:
						System.out.println("The user was successfully deleted!");
						this.backPointerApp.setIsUserLoggedIn(false);
						this.backPointerApp.stopPollingMessages();
						break;
					case 1:
						System.out.println("The user is not logged in!");
						break;
					default:
						System.out.println("Something went wrong in the process...");
					}
					this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				} else if (responseFromServer != null) {
					System.err.println("An unexpected response was received: " + responseFromServer.toString());
				} else {
					System.err.println("No response from the server was received...");
				}
			}
			
			this.pressEnterToContinue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
