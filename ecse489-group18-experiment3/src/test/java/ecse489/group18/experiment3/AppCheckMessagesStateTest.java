package ecse489.group18.experiment3;

import junit.framework.TestCase;

public class AppCheckMessagesStateTest extends TestCase {

	AppCheckMessagesStateMock acms;
	AppMock back;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		back = new AppMock(Client.SERVER_ADDRESS, Client.SERVER_PORT);
		back.currentState = back.appCheckMessagesState;
		acms = new AppCheckMessagesStateMock(back, null, null, null);
	}
	
	/**
	 * Tests execute method 
	 */
	public void testExecute() {
		
		assertTrue(back.currentState == back.appCheckMessagesState);
		
		// Since testing with messages or no messages simply changes the print statement,
		// we won't bother testing both codepaths
		acms.execute();
		
		assertTrue(back.wasGetMessagesFromPollingThreadCalled);
		assertTrue(back.currentState == back.mainState);
	}
	
}
