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
	
	private final int SLEEPING_PERIOD = 200;
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
		try {
			while(true) {
				this.execute();
				Thread.sleep(SLEEPING_PERIOD);
			}
		} catch (InterruptedException e) {
			System.out.println("The polling thread was interrupted.");
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
//				System.out.println("Polling the server.");
				this.sendMessage(Message.MessageFactory(DefaultMessages.QUERY_MESSAGES));
				responses = this.readMultipleResponsesFromServer();
			}
			
			if (responses != null && responses.get(0) != null) {
				if (responses.get(0).getSubMessageType() == 1) {
					System.out.println();
					this.printHeader("You received a new message!");
					
					for (int i = 0 ; i < responses.size() ; i++) {
						String message = responses.get(i).getMessageData();
						
						int fromIndex = message.indexOf(',');
						int dateIndex = message.indexOf(',', fromIndex+1);
						
						String from = message.substring(0, fromIndex);
						String date = message.substring(fromIndex+1, dateIndex);
						String messageData = message.substring(dateIndex+1, message.length());
						String formatedMessage = "From: " + from + ". Date: " + date + ". Message: " + messageData;

						this.addMessage(formatedMessage);
					}
				}
			}
		} catch (InterruptedException e) {
			System.err.println("The polling thread has been shutdown.");
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("The array exception thing happent");
		} catch (Exception e) {
			System.err.println("A problem occured while polling the server for new messages.");
			e.printStackTrace();
		}
	}
}
