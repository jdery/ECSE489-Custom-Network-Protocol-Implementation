package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public abstract class AppState {

	protected App backPointerApp;
	protected InputStreamReader socketInputStream;
	protected OutputStream socketOutputStream;
	protected BufferedReader bufferedReader;

	/**
	 * 
	 */
	public AppState(App backPointerApp, InputStreamReader socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		this.backPointerApp = backPointerApp;
		this.socketInputStream = socketInputStream;
		this.socketOutputStream = socketOutputStream;
		this.bufferedReader = bufferedReader;
	}

	/**
	 * Will execute the state.
	 */
	public abstract void execute();

	/**
	 * Sends a message to the server.
	 * 
	 * @param messageToSend
	 *            The message that will be sent.
	 */
	protected void sendMessage(Message messageToSend) {
		try {
			socketOutputStream.write(messageToSend.toByteArray());
		} catch (Exception e) {
			System.err
					.println("Was not able to send the message to the server.");
			e.printStackTrace();
		}
	}
}
