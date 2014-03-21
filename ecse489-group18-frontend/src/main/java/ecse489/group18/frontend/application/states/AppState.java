package ecse489.group18.frontend.application.states;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Vector;

import ecse489.group18.frontend.application.App;
import ecse489.group18.frontend.messages.Message;
import ecse489.group18.frontend.messages.MessageType;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public abstract class AppState {

	protected App backPointerApp;
	private OutputStream socketOutputStream;
	private BufferedInputStream bufferedInputStream;
	protected BufferedReader bufferedReader;

	/**
	 * 
	 */
	public AppState(App backPointerApp, BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		this.backPointerApp = backPointerApp;
		this.socketOutputStream = socketOutputStream;
		this.bufferedReader = bufferedReader;
		this.bufferedInputStream = bufferedInputStream;
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
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private Message readMessage() throws InterruptedException, IOException {
		try {
			byte[] tempInformation = new byte[4];
			
			// Reads the first 3 parameters of the message.
			byte[] allHeaders = new byte[12];
			int bytesRead = 0;
			while(bytesRead < 12) {
			  bytesRead += bufferedInputStream.read(allHeaders, bytesRead, 12 - bytesRead);
			}
			
			// Reads the MessageType.
			tempInformation = Arrays.copyOfRange(allHeaders,0,4);
			int messageTypeInt = ByteBuffer.wrap(tempInformation).getInt();
			MessageType messageType = MessageType.values()[messageTypeInt];
			
			// Reads the SubMessageType.
			tempInformation = Arrays.copyOfRange(allHeaders,4,8);
			int subMessageType = ByteBuffer.wrap(tempInformation).getInt();
			
			// Reads the size of the message.
			tempInformation = Arrays.copyOfRange(allHeaders,8,12);
			int size = ByteBuffer.wrap(tempInformation).getInt();
			
			// TODO: need to handle when the message is a file.
			// Reads the Message Data.
			byte[] messageDataChars = new byte[size];
			this.bufferedInputStream.read(messageDataChars, 0, size);
			String messageData = new String(messageDataChars);
			
			return (new Message(messageType, subMessageType, messageData));
		} catch(java.net.SocketTimeoutException e) {
			// If we reach here it means that the timer on the socket for the read operation
			// has timedout. This is normal since we don't want it to stall forever if there
			// is no response from the server.
			return (null);
		}
	}
	
	/**
	 * Reads a single response from the server and handles network latency.
	 * 
	 * @return The Message object that corresponds to the server's response.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	protected Message readResponseFromServer() throws InterruptedException, IOException {
		Message responseFromServer;
		do {
			responseFromServer = this.readMessage();
		} while (responseFromServer == null);
		
		return (responseFromServer);
	}

	/**
	 * Reads multiple messages that are stored in the input stream.
	 * 
	 * @return A Vector<Message> containing all the messages.
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws Exception
	 */
	protected Vector<Message> readMultipleResponsesFromServer() throws InterruptedException, IOException {
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
	 * Displays "Press enter to continue" and waits for a carriage return.
	 * @throws IOException 
	 */
	protected void pressEnterToContinue() throws IOException {
		System.out.println("\nPress enter to continue...");
		bufferedReader.readLine();
	}
	
	/**
	 * Will wait for user input and set the next state to be the main menu.
	 * @throws IOException 
	 */
	protected void switchToMainMenu() throws IOException {
		this.pressEnterToContinue();
		this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
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
				this.backPointerApp.setUserToLoggedIn(username);
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
