package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AppCheckMessagesStateMock extends AppCheckMessagesState {

	public AppCheckMessagesStateMock(App backPointerApp,
			InputStream socketInputStream, OutputStream socketOutputStream,
			BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * We don't want to experience IOExceptions every time
	 * we run a test that calls this method...
	 */
	@Override
	protected void pressEnterToContinue() throws IOException {}

}
