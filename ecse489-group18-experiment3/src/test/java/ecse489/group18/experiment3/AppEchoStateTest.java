package ecse489.group18.experiment3;

import junit.framework.TestCase;

public class AppEchoStateTest extends TestCase {

	AppEchoStateMock aes;
	OutputStreamMock osm;
	AppMock a;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		a = new AppMock(Client.SERVER_ADDRESS, Client.SERVER_PORT);
		osm = new OutputStreamMock();
		aes = new AppEchoStateMock(a, null, osm, null);
	}
	
	public void testExecute() {
		aes.setReadMessage(Message.MessageFactory(DefaultMessages.ECHO));
		aes.execute();
		
		assertTrue(osm.wasWriteCalled);
		assertTrue(a.getCurrentState() == a.mainState);
	}
}
