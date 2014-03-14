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
public class AppLogoutState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppLogoutState(App backPointerApp, BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			Helpers.printHeader("Logging out the user!");
			
			if (!this.backPointerApp.isUserLoggedIn()) {
				System.out.println("You are not logged in!");
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				this.pressEnterToContinue();
				return;
			}

			this.backPointerApp.stopPollingMessages();
			this.backPointerApp.setIsUserLoggedIn(false);
			
			Vector<Message> messages;
			// This will ensure that only one thread at a time can send requests and retrieve the associated responses.
			synchronized(App.LOCK) {
				this.sendMessage(Message.MessageFactory(DefaultMessages.LOGOFF));
				messages = this.readMultipleResponsesFromServer();
			}
			
			// Goes through the potential messages received from the server.
			for (Message responseFromServer : messages) {
				if (responseFromServer.getMessageType() == MessageType.LOGOFF) {
					switch(responseFromServer.getSubMessageType()) {
					case 0:
						System.out.println("You are not logged in anymore!");
						break;
					case 1:
						System.out.println("You tried to log out without first logging in!");
						break;
					case 2:
						System.out.println("You are already logged out! The session has expired!");
						break;
					}
				} else {
					System.out.println("An unexpected response was received: " + responseFromServer.toString());
				}
			}
			
			this.pressEnterToContinue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
	}
}
