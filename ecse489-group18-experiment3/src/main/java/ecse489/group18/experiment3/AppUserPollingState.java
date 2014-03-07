package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

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
		System.out.println("The polling thread has been started.");
		try {
			while(true) {
//				System.out.println("Polling the server for new messages.");
				this.execute();
				Thread.sleep(SLEEPING_PERIOD);
			}
		} catch (InterruptedException e) {
			System.out.println("The thread was interrupted.");
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
			Vector<Message> responses;
			// This will ensure that only one thread at a time can send requests and retrieve the associated responses.
			synchronized(App.LOCK) {
				this.sendMessage(Message.MessageFactory(DefaultMessages.QUERY_MESSAGES));
				responses = this.readMessages();
			}
			
			if (responses.get(0) != null && responses.get(0).getSubMessageType() == 1) {
				System.out.println("A new message was received!");
				
				for (int i = 0 ; i < responses.size() ; i++) {
					String message = responses.get(i).getMessageData();
					
//					System.out.println(message);
					
					int fromIndex = message.indexOf(',');
					int dateIndex = message.indexOf(',', fromIndex+1);
					
					String from = message.substring(0, fromIndex);
					String date = message.substring(fromIndex+1, dateIndex);
					String messageData = message.substring(dateIndex+1, message.length());
					String formatedMessage = "From: " + from + ". Date: " + date + ". Message: " + messageData;

					this.addMessage(formatedMessage);
					System.out.println("A message was added: " + formatedMessage);
				}
			} else {
//				System.out.println("No new messages.");
			}
			
		} catch (InterruptedException e) {
			System.err.println("The polling thread has been shutdown.");
		} catch (Exception e) {
			System.err.println("A problem occured while polling the server for new messages.");
			e.printStackTrace();
		}
	}
}
