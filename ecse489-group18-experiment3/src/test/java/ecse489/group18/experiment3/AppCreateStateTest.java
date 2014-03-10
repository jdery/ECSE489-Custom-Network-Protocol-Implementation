package ecse489.group18.experiment3;

import junit.framework.TestCase;

public class AppCreateStateTest extends TestCase {

	AppCreateState acs;
	AppMock back;
	
	protected void setUp() throws Exception {
		super.setUp();
		back = new AppMock(Client.SERVER_ADDRESS, Client.SERVER_PORT);
		acs = new AppCreateState(back, null, null, null);
	}
	
	

}
