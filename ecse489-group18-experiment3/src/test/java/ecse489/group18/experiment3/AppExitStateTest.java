package ecse489.group18.experiment3;

import junit.framework.TestCase;

public class AppExitStateTest extends TestCase {

	AppMock a;
	AppExitStateMock aes;
	OutputStreamMock osm;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		a = new AppMock(Client.SERVER_ADDRESS, Client.SERVER_PORT);
		osm = new OutputStreamMock();
		aes = new AppExitStateMock(a, null, osm, null);
	}
	
	/**
	 * Tests execute method
	 * because of call to System.exit, this test will not pass.
	 * However, it is possible to debug up until that point.
	 */
	public void testExecute() {
		aes.execute();
		
		assertTrue(a.wasStopPollingMessagesCalled);
		assertTrue(osm.wasWriteCalled);
	}
	
	

}
