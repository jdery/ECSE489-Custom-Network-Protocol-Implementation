package ecse489.group18.experiment3;

import java.io.InputStreamReader;

import junit.framework.TestCase;

public class AppMainStateTest extends TestCase {

	AppMock a;
	BufferedReaderMock brm;
	AppMainStateMock ams;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		a = new AppMock(Client.SERVER_ADDRESS, Client.SERVER_PORT);
		brm = new BufferedReaderMock(new InputStreamReader(System.in));
		ams = new AppMainStateMock(a, null, null, brm);
	}

	/**
	 * Tests execute method
	 */
	public void testExecute() {
		brm.setReadLine("1");
		ams.execute();
		assertTrue(a.getCurrentState() == a.loginState);
		
		brm.setReadLine("2");
		ams.execute();
		assertTrue(a.getCurrentState() == a.logoutState);
		
		brm.setReadLine("3");
		ams.execute();
		assertTrue(a.getCurrentState() == a.createState);
		
		brm.setReadLine("4");
		ams.execute();
		assertTrue(a.getCurrentState() == a.deleteState);
		
		brm.setReadLine("5");
		ams.execute();
		assertTrue(a.getCurrentState() == a.echoState);
		
		brm.setReadLine("6");
		ams.execute();
		assertTrue(a.getCurrentState() == a.appSendMessageState);
		
		brm.setReadLine("7");
		ams.execute();
		assertTrue(a.getCurrentState() == a.appCheckMessagesState);
		
		brm.setReadLine("8");
		ams.execute();
		assertTrue(a.getCurrentState() == a.exitState);
		
		// we will not test error case since we would need a stack.
		// manual test needed. however, testSwitchState below tests
		// with an error
		
		assertTrue(ams.wasPrintMainMenuCalled);
	}
	
	/**
	 * Tests switchState method
	 */
	public void testSwitchStateError() {
		boolean b = ams.switchState("errorString");
		assertFalse(b);
	}
}
