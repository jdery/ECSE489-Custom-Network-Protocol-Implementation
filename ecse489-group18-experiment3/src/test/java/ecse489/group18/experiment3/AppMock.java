package ecse489.group18.experiment3;

public class AppMock extends App {

	public AppMock(String serverAddress, int serverPort) throws Exception {
		super(serverAddress, serverPort);
		// TODO Auto-generated constructor stub
	}
	
	public AppState getCurrentState() {
		return currentState;
	}
	
	public AppState getUserPolling() {
		return userPolling;
	}
	
	public Thread getPollingThread() {
		return pollingThread;
	}
	
	public void setUserPolling(AppUserPollingState aups) {
		userPolling = aups;
	}
}
