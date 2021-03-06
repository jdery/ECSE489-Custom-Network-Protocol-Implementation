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
	
	public static String s_exit = "EXIT";
	public static String s_echo = "ECHO_TEAM_18";
	public static String s_logoff = "LOGOFF";
	public static String s_delete = "DELETE_USER";
	public static String s_create = "CREATE_STORE";
	public static String s_query = "QUERY_MESSAGES";
	
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
			return (new Message(MessageType.EXIT, 0, s_exit));
		case ECHO:
			return (new Message(MessageType.ECHO, 0, s_echo));
		case LOGOFF:
			//FIXME: this does not seem to be working without the LOGOFF text.
			return (new Message(MessageType.LOGOFF, 0, s_logoff));
		case DELETE_USER:
			return (new Message(MessageType.DELETE_USER, 0, s_delete));
		case CREATE_STORE:
			return (new Message(MessageType.CREATE_STORE, 0, s_create));
		case QUERY_MESSAGES:
			return (new Message(MessageType.QUERY_MESSAGES, 0, s_query));
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
		byte[] messageType = ByteBuffer.allocate(4)
				.putInt(this.messageType.getValue()).array();
		byte[] subMessageType = ByteBuffer.allocate(4)
				.putInt(this.subMessageType).array();
		byte[] size = ByteBuffer.allocate(4).putInt(this.size).array();
		byte[] messageData = this.messageData.getBytes();

		byte[] arrayToBeReturned = new byte[messageType.length
				+ subMessageType.length + size.length + messageData.length];
		System.arraycopy(messageType, 0, arrayToBeReturned, 0,
				messageType.length);
		System.arraycopy(subMessageType, 0, arrayToBeReturned,
				messageType.length, subMessageType.length);
		System.arraycopy(size, 0, arrayToBeReturned, messageType.length
				+ subMessageType.length, size.length);
		System.arraycopy(messageData, 0, arrayToBeReturned, messageType.length
				+ subMessageType.length + size.length, messageData.length);
		
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
