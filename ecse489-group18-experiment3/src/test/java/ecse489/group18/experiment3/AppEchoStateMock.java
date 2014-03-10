package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

public class AppEchoStateMock extends AppEchoState {

	private Message reader;
	public boolean wasReadCalled;
	
	public AppEchoStateMock(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
		// TODO Auto-generated constructor stub
	}
	
	public void setReadMessage(Message m) {
		reader = m;
	}
	
	public void resetWasReadCalled() {
		wasReadCalled = false;
	}
	
	@Override
	public Message readMessage() {
		wasReadCalled = true;
		return reader;
	}
	
	@Override
	public void pressEnterToContinue() {}
	
	

}
