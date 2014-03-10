package ecse489.group18.experiment3;

/** Class is a public accessor to AppState methods **/

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

public class AppStateMock extends AppState {

	public AppStateMock(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {}
	
	

}
