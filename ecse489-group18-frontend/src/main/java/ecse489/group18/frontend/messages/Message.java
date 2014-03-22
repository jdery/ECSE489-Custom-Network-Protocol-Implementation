package ecse489.group18.frontend.messages;

import java.nio.ByteBuffer;
import java.util.NoSuchElementException;

import ecse489.group18.frontend.application.Helpers;

/**
 * The custom protocol message.
 * 
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class Message {
	/**
	 * The message type of the current message.
	 */
	private MessageType messageType;
	/**
	 * The Sub-Message type of the current message.
	 */
	private int subMessageType;
	/**
	 * The size of the message data (payload) of the current message.
	 */
	private int size;
	/**
	 * The payload of the message.
	 */
	private String messageData;
	
	/**
	 * The payload raw data used when we send files to the server.
	 */
	private byte[] rawData;

	/**
	 * Factory for the messages.
	 * 
	 * @param defaultMessage
	 *            The type of message to be instantiated.
	 * @return The instantiated message.
	 */
	public static Message MessageFactory(DefaultMessages defaultMessage) {
		switch (defaultMessage) {
		case EXIT:
			return (new Message(MessageType.EXIT, 0, "EXIT"));
		case ECHO:
			return (new Message(MessageType.ECHO, 0, "ECHO_TEAM_18"));
		case LOGOFF:
			return (new Message(MessageType.LOGOFF, 0, "LOGOFF"));
		case DELETE_USER:
			return (new Message(MessageType.DELETE_USER, 0, "DELETE_USER"));
		case CREATE_STORE:
			return (new Message(MessageType.CREATE_STORE, 0, "CREATE_STORE"));
		case CREATE_FILE_STORE:
			return (new Message(MessageType.CREATE_FILE_STORE, 0, "CREATE_FILE_STORE"));
		case QUERY_MESSAGES:
			return (new Message(MessageType.QUERY_MESSAGES, 0, "QUERY_MESSAGES"));
		case QUERY_FILES:
			return (new Message(MessageType.QUERY_FILES, 0, "QUERY_FILES"));
		}

		return (null);
	}
	
	/**
	 * Determines what is the Message Sub-Type based on the extension of the file.
	 * 
	 * @param filePath The file path to analyze.
	 * @return The corresponding Message Sub-Type value.
	 * @throws NoSuchElementException
	 */
	public static int giveSubTypesBasedOnFileExtension(String filePath) throws NoSuchElementException {
		if (Helpers.isJPEG(filePath)) {
			return (0);
		} else if (Helpers.isPNG(filePath)) {
			return (1);
		} else if (Helpers.isTXT(filePath)) {
			return (2);
		} else if (Helpers.isZIP(filePath)) {
			return (3);
		} else {
			throw new NoSuchElementException("The extension you entered is not supported.");
		}
	}

	public Message(MessageType messageType, int subMessageType, String messageData) {
		this.messageType = messageType;
		this.subMessageType = subMessageType;
		this.messageData = messageData;
		this.rawData = null;
		this.size = messageData.length();
	}
	
	public Message(MessageType messageType, int subMessageType, byte[] rawData) {
		this.messageType = messageType;
		this.subMessageType = subMessageType;
		this.messageData = null;
		this.rawData = rawData;
		this.size = rawData.length;
	}

	/**
	 * Converts the message to a byte[].
	 * 
	 * @return The converted byte[].
	 */
	public byte[] toByteArray() {
		byte[] messageType = ByteBuffer.allocate(4).putInt(this.messageType.getValue()).array();
		byte[] subMessageType = ByteBuffer.allocate(4).putInt(this.subMessageType).array();
		byte[] size = ByteBuffer.allocate(4).putInt(this.size).array();
		byte[] messageData;
		if (this.messageData != null) {
			messageData = this.messageData.getBytes();
		} else {
			messageData = this.rawData;
			
			//debug
			System.out.println(messageData);
		}

		byte[] arrayToBeReturned = new byte[messageType.length + subMessageType.length + size.length + messageData.length];
		System.arraycopy(messageType, 0, arrayToBeReturned, 0, messageType.length);
		System.arraycopy(subMessageType, 0, arrayToBeReturned, messageType.length, subMessageType.length);
		System.arraycopy(size, 0, arrayToBeReturned, messageType.length + subMessageType.length, size.length);
		System.arraycopy(messageData, 0, arrayToBeReturned, messageType.length + subMessageType.length + size.length, messageData.length);
		
		return (arrayToBeReturned);
	}
	
	/**
	 * 
	 * @return The Message Type.
	 */
	public MessageType getMessageType() {
		return (this.messageType);
	}
	
	/**
	 * 
	 * @return The Sub Message Type.
	 */
	public int getSubMessageType() {
		return (this.subMessageType);
	}
	
	/**
	 * 
	 * @return The Message Data.
	 */
	public String getMessageData() {
		return (this.messageData);
	}
	
	public byte[] getRawData() {
		return (this.rawData);
	}
	
	@Override
	public String toString() {
		StringBuilder content = new StringBuilder(100);
		content.append("{MessageType=" + messageType.getValue() + "}");
		content.append("{SubMessageType=" + subMessageType + "}");
		content.append("{Size=" + size + "}");
		content.append("{MessageData=" + messageData + "}");
		
		return (content.toString());
	}
}
