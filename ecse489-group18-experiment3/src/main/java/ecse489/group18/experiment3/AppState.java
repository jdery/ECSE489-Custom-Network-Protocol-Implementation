package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.IOException;
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
	 * @throws IOException
	 */
	protected void sendMessage(Message messageToSend) throws IOException {
		socketOutputStream.write(messageToSend.toByteArray());
	}

	/**
	 * Reads a message from the InputStreamReader.
	 * 
	 * @throws IOException
	 */
	protected Message readMessage() throws Exception {
		int messageTypeInt = socketInputStream.read();
		MessageType messageType = MessageType.values()[messageTypeInt];
		int subMessageType = socketInputStream.read();
		int size = socketInputStream.read();
		char[] messageDataChars = new char[size];
		socketInputStream.read(messageDataChars, 0, size);
		String messageData = new String(messageDataChars);
		
		return (new Message(messageType, subMessageType, messageData));
	}

	/**
	 * Prints a header on the terminal.
	 * 
	 * @param header
	 *            The header to be printed.
	 */
	protected void printHeader(String header) {
		int lengthOfHeader = header.length() + 4;

		System.out.print("+");
		for (int i = 0; i < (lengthOfHeader - 2); i++) {
			System.out.print("-");
		}
		System.out.println("+");

		System.out.println("| " + header + " |");

		System.out.print("+");
		for (int i = 0; i < (lengthOfHeader - 2); i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}
}
