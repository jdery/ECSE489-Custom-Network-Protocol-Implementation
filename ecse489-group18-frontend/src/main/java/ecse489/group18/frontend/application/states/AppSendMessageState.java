/**
 * 
 */
package ecse489.group18.frontend.application.states;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.OutputStream;

import ecse489.group18.frontend.application.App;
import ecse489.group18.frontend.application.Helpers;
import ecse489.group18.frontend.messages.Message;
import ecse489.group18.frontend.messages.MessageType;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppSendMessageState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppSendMessageState(App backPointerApp, BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		Helpers.printHeader("Send a message to a friend!");
		
		try {
			if (!this.backPointerApp.isUserLoggedIn()) {
				System.out.println("You are not logged in!");
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				this.pressEnterToContinue();
				return;
			}			
			
			String destination, from;
			from = this.backPointerApp.getLoggedInUsername();
			System.out.print("Destination user: ");
			destination = bufferedReader.readLine();
			
			if (Helpers.validateCredentials(destination, from)) {
				System.out.print("Message to be sent: ");
				String message = bufferedReader.readLine();
				
				Message responseFromServer;
				synchronized(App.LOCK) {
					this.sendMessage(new Message(MessageType.SEND_MESSAGE_TO_USER, 0, destination + "," + from + "@" + message));
					responseFromServer = this.readResponseFromServer();
				}
				
				// Verify the response from the user.
				if (responseFromServer.getMessageType() == MessageType.SEND_MESSAGE_TO_USER) {
					switch (responseFromServer.getSubMessageType()) {
					case 0:
						System.out.println("The message was sent successfully!");
						break;
					case 1:
						System.out.println("Failed to write in the user data store!");
						break;
					case 2:
						System.out.println("The user does not exit!");
						break;
					case 3:
						System.out.println("You are not logged in!");
						break;
					case 4:
						System.out.println("Badly formatted message!");
						break;
					}
				} else {
					System.out.println("An unexpected response was received: " + responseFromServer.toString());
				}
			} else {
				System.err.println("The destination is not of a valid format!");
			}
			
			this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
			this.pressEnterToContinue();
		} catch (Exception e) {
			System.err.println("An error occured while reading your input.");
			e.printStackTrace();
		}
	}
}
