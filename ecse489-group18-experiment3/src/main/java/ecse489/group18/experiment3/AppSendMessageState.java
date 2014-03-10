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
public class AppSendMessageState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppSendMessageState(App backPointerApp,
			InputStream socketInputStream, OutputStream socketOutputStream,
			BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		this.printHeader("Send a message to a friend!");
		
		try {
			if (!this.backPointerApp.isUserLoggedIn()) {
				System.out.println("You are not logged in!");
				this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
				this.pressEnterToContinue();
				return;
			}			
			
			String destination, from;
			System.out.print("From user: ");
			from = bufferedReader.readLine();
			System.out.print("Destination user: ");
			destination = bufferedReader.readLine();
			
			if (this.validateCredentials(destination, from)) {
				System.out.print("Message to be sent: ");
				String message = bufferedReader.readLine();
				
				Message responseFromServer;
				synchronized(App.LOCK) {
					this.sendMessage(new Message(MessageType.SEND_MESSAGE_TO_USER, 0, destination + "," + from + "@" + message));
					do {
						responseFromServer = this.readMessage();
					} while(responseFromServer == null);
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
			
			this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
			this.pressEnterToContinue();
		} catch (Exception e) {
			System.err.println("An error occured while reading your input.");
			e.printStackTrace();
		}
	}
}
