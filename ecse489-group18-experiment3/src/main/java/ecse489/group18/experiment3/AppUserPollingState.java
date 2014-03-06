package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppUserPollingState extends AppState implements Runnable {
	
	private final int SLEEPING_PERIOD = 1000;
	private ArrayList<String> listOfMessages = new ArrayList<String>();

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppUserPollingState(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	/**
	 * 
	 */
	public void run() {
		while(true) {
			try {
				this.execute();
				Thread.sleep(SLEEPING_PERIOD);
			} catch (InterruptedException e) {
				System.err.println("A problem occured while trying to sleep the polling thread.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds a message to the list of messages.
	 * 
	 * @param message The message to be added.
	 */
	private void addMessage(String message) {
		synchronized(this.listOfMessages) {
			this.listOfMessages.add(message);
			System.out.println("A message was added!");
		}
	}
	
	/**
	 * 
	 * @return The list of messages received.
	 */
	public String getMessages() {
		StringBuilder messagesToReturn = new StringBuilder(500);
		
		synchronized(this.listOfMessages) {
			for (String message : this.listOfMessages) {
				messagesToReturn.append(message + "\n");
			}
		}
		
		return (messagesToReturn.toString());
	}

	@Override
	public void execute() {
		try {
			this.sendMessage(Message.MessageFactory(DefaultMessages.QUERY_MESSAGES));
			Message response = this.readMessage();
			
			if (response.getSubMessageType() == 1) {
				response = this.readMessage();
				String message = response.getMessageData();
				
				// Finds the form part and the message part of the response.
				int division = message.indexOf('@');
				String from = message.substring(0, division-1);
				String data = message.substring(division+1, message.length());
				
				// Creates the formatted message that will be added to the list of message.
				String formatedMessage = "From: " + from + ". Message: " + data;
				this.addMessage(formatedMessage);
			} else {
				System.out.println("No new messages.");
			}
			
		} catch (Exception e) {
			System.err.println("A problem occured while polling the server for new messages.");
			e.printStackTrace();
		}
	}
}
