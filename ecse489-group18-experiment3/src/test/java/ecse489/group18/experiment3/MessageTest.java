package ecse489.group18.experiment3;

import junit.framework.TestCase;

public class MessageTest extends TestCase {

	Message sampleMessage;
	MessageType messageType;
	String messageData;
	int subMessage;
	
	DefaultMessages dm_exit = DefaultMessages.EXIT;
	DefaultMessages dm_echo = DefaultMessages.ECHO;
	DefaultMessages dm_logoff = DefaultMessages.LOGOFF;
	DefaultMessages dm_delete = DefaultMessages.DELETE_USER;
	DefaultMessages dm_create = DefaultMessages.CREATE_STORE;
	DefaultMessages dm_query = DefaultMessages.QUERY_MESSAGES;
	
	MessageType mt_exit = MessageType.EXIT;
	MessageType mt_badlyFormatted = MessageType.BADLY_FORMATTED_MESSAGE;
	MessageType mt_echo = MessageType.ECHO;
	MessageType mt_login = MessageType.LOGIN;
	MessageType mt_logoff = MessageType.LOGOFF;
	MessageType mt_createUser = MessageType.CREATE_USER;
	MessageType mt_delete = MessageType.DELETE_USER;
	MessageType mt_createStore = MessageType.CREATE_STORE;
	MessageType mt_send = MessageType.SEND_MESSAGE_TO_USER;
	MessageType mt_query = MessageType.QUERY_MESSAGES;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		messageType = mt_exit;
		messageData = "messageData";
		subMessage = 1234;
		sampleMessage = new Message(messageType, subMessage, messageData);
	}
	
	/**
	 * Tests types of Messages created from Message.MessageFactory method
	 */
	public void testMessageFactory() {
		
		// exit message
		Message m = Message.MessageFactory(dm_exit);
		assertEquals(m.getMessageType(), mt_exit);
		assertEquals(m.getSubMessageType(), 0);
		assertEquals(m.getMessageData(), Message.s_exit);
		
		m = Message.MessageFactory(dm_echo);
		assertEquals(m.getMessageType(), mt_echo);
		assertEquals(m.getSubMessageType(), 0);
		assertEquals(m.getMessageData(), Message.s_echo);
		
		m = Message.MessageFactory(dm_logoff);
		assertEquals(m.getMessageType(), mt_logoff);
		assertEquals(m.getSubMessageType(), 0);
		assertEquals(m.getMessageData(), Message.s_logoff);
		
		m = Message.MessageFactory(dm_delete);
		assertEquals(m.getMessageType(), mt_delete);
		assertEquals(m.getSubMessageType(), 0);
		assertEquals(m.getMessageData(), Message.s_delete);
		
		m = Message.MessageFactory(dm_create);
		assertEquals(m.getMessageType(), mt_createStore);
		assertEquals(m.getSubMessageType(), 0);
		assertEquals(m.getMessageData(), Message.s_create);
		
		m = Message.MessageFactory(dm_query);
		assertEquals(m.getMessageType(), mt_query);
		assertEquals(m.getSubMessageType(), 0);
		assertEquals(m.getMessageData(), Message.s_query);
	}
	
//	public void testToString() {
//		
//	}
//	
//	public void testToByteArray() {
//		
//	}

}
