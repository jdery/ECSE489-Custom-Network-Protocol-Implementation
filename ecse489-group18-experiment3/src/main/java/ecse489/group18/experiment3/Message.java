package ecse489.group18.experiment3;

import java.nio.ByteBuffer;

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
	 * Factory for the messages.
	 * 
	 * @param defaultMessage
	 *            The type of message to be instantiated.
	 * @return The instantiated message.
	 */
	static public Message MessageFactory(DefaultMessages defaultMessage) {
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
		case QUERY_MESSAGES:
			return (new Message(MessageType.QUERY_MESSAGES, 0, "QUERY_MESSAGES"));
		}

		return (null);
	}

	public Message(MessageType messageType, int subMessageType,
			String messageData) {
		this.messageType = messageType;
		this.subMessageType = subMessageType;
		this.messageData = messageData;
		this.size = messageData.length();
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
		byte[] messageData = this.messageData.getBytes();

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
