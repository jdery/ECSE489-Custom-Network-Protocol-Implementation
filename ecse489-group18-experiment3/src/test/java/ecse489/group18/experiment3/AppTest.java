package ecse489.group18.experiment3;

import junit.framework.*;
import org.junit.*;
import java.net.*;



/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
	AppMock a;

    @Before
    protected void setUp() throws Exception
    {
        super.setUp();
        
        a = new AppMock("dsp2014.ece.mcgill.ca", 5000);
    }

    /**
     * Test App Constructor
     */
    public void testAppConstructor() throws Exception
    {
    	// valid test
    	AppMock app;
    	try{
    		app = new AppMock("dsp2014.ece.mcgill.ca", 5000);
    		
    		assertNotNull(app.createState);
    		assertNotNull(app.deleteState);
    		assertNotNull(app.echoState);
    		assertNotNull(app.exitState);
    		assertNotNull(app.loginState);
    		assertNotNull(app.logoutState);
    		assertNotNull(app.mainState);
    		assertNotNull(app.appCheckMessagesState);
    		assertNotNull(app.appSendMessageState);
    		
    		assertEquals(app.getCurrentState(), app.mainState);
    		
    	} catch (Exception e) {
    		fail("exceptionThrown");
    	}
    }
    
    /**
     * Test App Constructor failure
     */
    public void testAppConstructorException() throws Exception
    {
    	// invalid port number
    	try{
    		AppMock e_app1 = new AppMock("dsp2014.ece.mcgill.ca", -1);
    	} catch (Exception e) {
    		assertTrue(e.getClass().equals(IllegalArgumentException.class));
    	}
    	
    	// null address
    	try{
    		AppMock e_app2 = new AppMock("", 5000);
    	} catch (Exception e) {
    		assertTrue(e.getClass().equals(ConnectException.class));
    	}
    	
    	// null address
    	try{
    		AppMock e_app3 = new AppMock("blahblahbalh", 5000);
    	} catch (Exception e) {
    		assertTrue(e.getClass().equals(UnknownHostException.class));
    	}
    }
    
    /**
     * Test App.changeCurrentState method
     */
    public void testChangeCurrentState() {
    	assertTrue(a.getCurrentState() == a.mainState);
    	
    	AppState toChange = a.echoState;
    	a.changeCurrentState(toChange);
    	assertTrue(a.getCurrentState() == toChange);
    }
    
    /**
     * Test App.startPollingMessages and App.stopPollingMessages methods.
     */
    public void testPollingThread() throws Exception {
    	
    	// start polling thread
    	a.startPollingMessages();
    	assertNotNull(a.getUserPolling());

    	assertTrue(a.getPollingThread().isAlive());
    	
    	// stop polling thread
    	a.stopPollingMessages();
    	assertTrue(a.getPollingThread().isInterrupted());
    	assertNull(a.getUserPolling());
    }
    
    /**
     * Test App.getMessagesFromPollingThread method.
     */
    public void testGetMessagesFromPollingThread() {
    	// here, userPolling member has not been initialized yet
    	String ret = a.getMessagesFromPollingThread();
    	assertNull(ret);
    	
    	// create and assign it
    	AppUserPollingStateMock aupsm = new AppUserPollingStateMock(null, null, null, null);
    	a.setUserPolling(aupsm);
    	
    	ret = a.getMessagesFromPollingThread();
    	assertNotNull(ret);
    	assertEquals(ret, AppUserPollingStateMock.MESSAGE_PLACEHOLDER);
    }

    
}
