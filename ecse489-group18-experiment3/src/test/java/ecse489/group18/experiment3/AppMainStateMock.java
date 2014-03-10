package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

public class AppMainStateMock extends AppMainState {

	public boolean wasPrintMainMenuCalled = false;
	
	public AppMainStateMock(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void printMainMenu() {
		wasPrintMainMenuCalled = true;
	}
	
//	@Override
//	public boolean switchState(String nextState) {
//		super()
//		
//		return true;
//	}

}
