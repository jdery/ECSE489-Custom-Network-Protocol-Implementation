package ecse489.group18.frontend.application.states;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

import ecse489.group18.frontend.application.App;
import ecse489.group18.frontend.application.Helpers;
import ecse489.group18.frontend.messages.DefaultMessages;
import ecse489.group18.frontend.messages.Message;
import ecse489.group18.frontend.messages.MessageType;

public class AppUserPollForFilesState extends AppState implements Runnable {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppUserPollForFilesState(App backPointerApp,
			BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
		// TODO Auto-generated constructor stub
	}

	private final int SLEEPING_PERIOD = 200;
	private ArrayList<String> listOfFilenames = new ArrayList<String>();
	private ArrayList<String> listOfFiles = new ArrayList<String>();
	private Hashtable<String, byte[]> hsFiles = new Hashtable<String, byte[]>();

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
			System.out.println("The pollForFiles thread was interrupted.");
		}
	}
	
	/**
	 * Adds a file to the hashtable of files.
	 * 
	 * @param message The message to be added.
	 */
	private void addFile(String filename_message, byte[] rawFile) {
		synchronized(this.hsFiles) {
			this.hsFiles.put(filename_message, rawFile);
		}
	}
	
	//TODO: remove this one
	/**
	 * Adds a filename to the list of filenames.
	 * 
	 * @param message The message to be added.
	 */
	private void addFilenameMessage(String filename_message) {
		synchronized(this.listOfFilenames) {
			this.listOfFilenames.add(filename_message);
		}
	}
	
	private void addFilename(String filename) {
		synchronized(this.listOfFiles) {
			this.listOfFiles.add(filename);
		}
	}
	
	public String getFilenameForIndex(int index) throws IndexOutOfBoundsException {
		return listOfFiles.get(index);
	}
	
	/**
	 * Uses Index of Arraylist to get string, and finds corresponding key (string), value (byte[]) pair in hashtable
	 * @param index
	 * @return
	 */
	public byte[] getFileForIndex(int index) {
		String temp = listOfFilenames.get(index);
		return hsFiles.get(temp);
	}
	
	/**
	 * 
	 * @return The list of messages received.
	 */
	public String getFilenameMessages() {
		StringBuilder messagesToReturn = new StringBuilder(500);
		
		int i = 0;
		synchronized(this.listOfFilenames) {
			for (String filename : this.listOfFilenames) {
				messagesToReturn.append(++i + ". " + filename + "\n");
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
				this.sendMessage(Message.MessageFactory(DefaultMessages.QUERY_FILES));
				responses = this.readMultipleResponsesFromServer();
			}
			
			if (responses != null && responses.get(0) != null) {
				
				// submessage type 1 means that there is a new file
				if (responses.get(0).getSubMessageType() == 1) {
					System.out.println();
					Helpers.printHeader("You received a new file!");
					
					for (int i = 0 ; i < responses.size() ; i = i + 2) {
						String message = responses.get(i).getMessageData();
						
						int fromIndex = message.indexOf(',');
						int filenameIndex = message.indexOf(',', fromIndex+1);
						
						String from = message.substring(0, fromIndex);
						String filename = message.substring(fromIndex+1, filenameIndex);
						String date = message.substring(filenameIndex+1, message.length());
						String formattedMessage = "From: " + from + ".\tDate: " + date + ".\tFilename: " + filename;
						
						// because 2 messages are received... first sending string message above, and second
						// sending raw file data, we use i + 1 here
						byte[] rawFile = responses.get(i + 1).getRawData();
						
						//debug
						System.out.println("The size of rawFile is " + rawFile.length);

						this.addFilename(filename);
						this.addFilenameMessage(formattedMessage);
						this.addFile(formattedMessage, rawFile);
					}
				}
			}
		} catch (InterruptedException e) {
			System.err.println("The pollForFiles thread has been shutdown.");
		} catch (Exception e) {
			System.err.println("A problem occured while polling the server for new files.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads multiple messages that are stored in the input stream.
	 * 
	 * @return A Vector<Message> containing all the messages.
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws Exception
	 */
	@Override
	protected Vector<Message> readMultipleResponsesFromServer() throws InterruptedException, IOException {
		Vector<Message> messages = new Vector<Message>(5);
		Message currentMessage;
		do {
			do {
				currentMessage = this.readMessage();
				if (currentMessage != null) {
					messages.add(currentMessage);
					
					// also here get the second one
					currentMessage = this.readMessageBytes();
					if (currentMessage != null) {
						messages.add(currentMessage);
					}
				}
			} while(currentMessage != null);
		} while(messages.size() == 0);
		
		return (messages);
	}
	
	// almost copy from up... raw data instead of string 
	private Message readMessageBytes() throws InterruptedException, IOException {
		try {
			byte[] tempInformation = new byte[4];
			
			// Reads the first 3 parameters of the message.
			byte[] allHeaders = new byte[12];
			int bytesRead = 0;
			while(bytesRead < 12) {
				// this try catch gets rid of the IndexOutOfBounds exception due to stopping the 2 polling threads at the same time
//				try {
					bytesRead += bufferedInputStream.read(allHeaders, bytesRead, 12 - bytesRead);
//				} catch (IndexOutOfBoundsException e) {
//					return null;
//				}
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
			
			// Reads the Message Data.
			byte[] messageDataChars = new byte[size];
			this.bufferedInputStream.read(messageDataChars, 0, size);
			
			return (new Message(messageType, subMessageType, messageDataChars));
		} catch(java.net.SocketTimeoutException e) {
			// If we reach here it means that the timer on the socket for the read operation
			// has timedout. This is normal since we don't want it to stall forever if there
			// is no response from the server.
			return (null);
		}
	}

}
