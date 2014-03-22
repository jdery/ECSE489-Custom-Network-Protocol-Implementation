package ecse489.group18.frontend.messages;

/**
 * The different message types that the custom message can have.
 * 
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 *
 */
public enum MessageType {
	EXIT,
	BADLY_FORMATTED_MESSAGE,
	ECHO,
	LOGIN,
	LOGOFF,
	CREATE_USER,
	DELETE_USER,
	CREATE_STORE,
	SEND_MESSAGE_TO_USER,
	QUERY_MESSAGES,
	SEND_FILE,
	CREATE_FILE_STORE,
	QUERY_FILES;
	
	/**
	 * 
	 * @return The value of the enumerator.
	 */
	public int getValue() {
		return (this.ordinal());
	}
}
