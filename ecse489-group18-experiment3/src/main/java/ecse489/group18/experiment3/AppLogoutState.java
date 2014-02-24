package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

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
	public AppLogoutState(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			this.printHeader("Logging out the user!");

			this.sendMessage(Message.MessageFactory(DefaultMessages.LOGOFF));
			Message responseFromServer = this.readMessage();

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
				this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
			} else {
				System.out.println("An unexpected response was received: " + responseFromServer.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.backPointerApp.changeCurrentState(this.backPointerApp.mainState);
	}
}
