package ecse489.group18.experiment3;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.Socket;

public class AppStateTest extends TestCase {

	AppStateMock as;
	AppMock back;
	BufferedInputStreamMock bism;
	OutputStreamMock osm;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		back = new AppMock(Client.SERVER_ADDRESS, Client.SERVER_PORT);
		bism = new BufferedInputStreamMock(new Socket(Client.SERVER_ADDRESS, Client.SERVER_PORT).getInputStream());
		osm = new OutputStreamMock();
		as = new AppStateMock(back, bism, osm, null);
	}
	
	/** 
	 * Tests ValidateCredential method
	 */
	public void testValidateCredential() {
		
		// the credential cannot contain a comma
		String strNull = null;
		String strEmpty = "";
		String containsComma = "iama,comma";
		String containsNone = "blahb.13[]clahblah";
		
		// valid string
		boolean retb = as.validateCredential(containsNone);
		assertTrue(retb);
		
		// invalid string
		retb = as.validateCredential(containsComma);
		assertFalse(retb);
		
		// null string
		retb = as.validateCredential(strNull);
		assertFalse(retb);
		
		// empty string
		retb = as.validateCredential(strEmpty);
		assertFalse(retb);
	}
	
	/** 
	 * Tests ValidateCredentials method
	 */
	public void testValidateCredentials() {
		String valid = "hello";
		String invalid = "hel,lo";
		
		boolean retb = as.validateCredentials(valid, valid);
		assertTrue(retb);
		
		retb = as.validateCredentials(invalid, valid);
		assertFalse(retb);
		
		retb = as.validateCredentials(valid, invalid);
		assertFalse(retb);
		
		retb = as.validateCredentials(invalid, invalid);
		assertFalse(retb);
	}
	
	/**
	 * Tests readMessage method
	 */
	public void testReadMessage() {
		// readMessage with no available bytes
		bism.setAvailable(0);
		try {
			Message m = as.readMessage();
			assertNull(m);
		} catch (Exception e) {
			fail();
		}
		
//		// readMessage with available bytes
//		bism.setAvailable(100);
//		
//		byte setter[] = {1,2,3,4};
//		bism.setRead(setter);
//		try {
//			Message m = as.readMessage();
//			assertNotNull(m);
//			
//			assertEquals(m.getMessageData(), "1234");
//		} catch (Exception e) {
//			fail();
//		}
		
	}
	
	/** 
	 * Tests sendMessage method
	 */
	public void testSendMessage() {
		try {
			as.sendMessage(Message.MessageFactory(DefaultMessages.EXIT));
			assertTrue(osm.wasWriteCalled);
		} catch (IOException e) {
			fail();
		}
	}
	
	
	
	
	
	
	

}
