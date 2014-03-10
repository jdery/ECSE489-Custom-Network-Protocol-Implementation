package ecse489.group18.experiment3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public abstract class AppState {
	
	private final String COMMA_REGEX = ".*[,].*";
	private final int READ_RESPONSE_DELAY = 500;

	protected App backPointerApp;
	protected OutputStream socketOutputStream;
	protected BufferedInputStream bufferedInputStream;
	protected BufferedReader bufferedReader;

	/**
	 * 
	 */
	public AppState(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		this.backPointerApp = backPointerApp;
		this.socketOutputStream = socketOutputStream;
		this.bufferedReader = bufferedReader;
		this.bufferedInputStream = new BufferedInputStream(socketInputStream);
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
		this.socketOutputStream.write(messageToSend.toByteArray());
	}
	
	/**
	 * Reads a message from the InputStreamReader.
	 * 
	 * @throws IOException
	 */
	protected Message readMessage() throws Exception {
		byte[] tempInformation = new byte[4];
		// This delay will leave time for the input buffer to receive the information
		// since this method is always called after a message was sent to the server.
		Thread.sleep(READ_RESPONSE_DELAY);
		
		// If the buffer is empty it means that we have read all the responses.
		if (this.bufferedInputStream.available() == 0) {
			return (null);
		}
		
		// Reads the MessageType.
		this.bufferedInputStream.read(tempInformation);
		int messageTypeInt = ByteBuffer.wrap(tempInformation).getInt();
		MessageType messageType = MessageType.values()[messageTypeInt];
		
		// Reads the SubMessageType.
		this.bufferedInputStream.read(tempInformation);
		int subMessageType = ByteBuffer.wrap(tempInformation).getInt();
		
		// Reads the size of the message.
		this.bufferedInputStream.read(tempInformation);
		int size = ByteBuffer.wrap(tempInformation).getInt();
		
		// Reads the Message Data.
		byte[] messageDataChars = new byte[size];
		this.bufferedInputStream.read(messageDataChars);
		String messageData = new String(messageDataChars);
		
		return (new Message(messageType, subMessageType, messageData));
	}

	/**
	 * Reads multiple messages that are stored in the input stream.
	 * 
	 * @return A Vector<Message> containing all the messages.
	 * @throws Exception
	 */
	protected Vector<Message> readMessages() throws Exception {
		Vector<Message> messages = new Vector<Message>(5);
		Message currentMessage;
		do {
		do {
			currentMessage = this.readMessage();
			if (currentMessage != null) {
				messages.add(currentMessage);
			}
		} while(currentMessage != null);
		} while(messages.size() == 0);
		
		return (messages);
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
	
	/**
	 * Will validate that the credential are in the right format.
	 * 
	 * @param credential
	 *            The credential (either password or username) to be validated.
	 * @return True if valid and false otherwise.
	 */
	protected boolean validateCredentials(String username, String password) {
		if (!validateCredential(username) || !validateCredential(password)) {
			return (false);
		}
		return (true);
	}

	/**
	 * Validate the credentials.
	 * 
	 * @param credential
	 *            The credentials to be validated.
	 * @return True if valid and false otherwise.
	 */
	protected boolean validateCredential(String credential) {
		//TODO: need to complete this method.
		if (credential == null || credential.length() == 0) {
			return (false);
		}
		if (Pattern.matches(COMMA_REGEX, credential)) {
			return (false);
		}
		return (true);
	}
	
	/**
	 * Displays "Press enter to continue" and waits for a carriage return.
	 * @throws IOException 
	 */
	protected void pressEnterToContinue() throws IOException {
		System.out.println("\nPress enter to continue...");
		bufferedReader.readLine();
	}
	
	/**
	 * Sends the login request and handle the response.
	 * 
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	protected boolean loginUser(String username, String password) throws Exception {
		
		Message responseFromServer;
		// This will ensure that only one thread at a time can send requests and retrieve the associated responses.
		synchronized(App.LOCK) {
			this.sendMessage(new Message(MessageType.LOGIN, 0, username + "," + password));
			do {
				responseFromServer = this.readMessage();
			} while(responseFromServer ==  null);
		}

		if (responseFromServer.getMessageType() == MessageType.LOGIN) {
			switch(responseFromServer.getSubMessageType()) {
			case 0:
				System.out.println("You were successfully authenticated!");
				this.backPointerApp.setIsUserLoggedIn(true);
				this.backPointerApp.startPollingMessages();
				return (true);
			case 1:
				System.out.println("You are already logged in!");
				break;
			case 2:
				System.out.println("Bad credentials!");
				break;
			case 3:
				System.out.println("Badly formatted message!");
				break;
			}
		} else {
			System.out.println("An unexpected response was received: " + responseFromServer.toString());
		}
		
		return (false);
	}
}
