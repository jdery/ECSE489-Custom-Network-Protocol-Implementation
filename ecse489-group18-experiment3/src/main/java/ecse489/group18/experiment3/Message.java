package ecse489.group18.experiment3;

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
	 * The size of the message data (payload) of the current message.
	 */
	private int size;
	/**
	 * The payload of the message.
	 */
	private String messageData;
	
	
	public Message(MessageType messageType, int size, String messageData) {
		this.messageType = messageType;
		this.size = size;
		this.messageData = messageData;
	}
}
