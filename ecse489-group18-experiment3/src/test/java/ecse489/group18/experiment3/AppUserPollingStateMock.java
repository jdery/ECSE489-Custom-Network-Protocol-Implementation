package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

public class AppUserPollingStateMock extends AppUserPollingState {

	public static String MESSAGE_PLACEHOLDER = "message placeholder";
	
	public AppUserPollingStateMock(App backPointerApp,
			InputStream socketInputStream, OutputStream socketOutputStream,
			BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
		// TODO Auto-generated constructor stub
	}
	
	public String getMessages() {
		return MESSAGE_PLACEHOLDER;
	}

}
