package ecse489.group18.experiment3;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamMock extends OutputStream {

	public boolean wasWriteCalled = false;
	
	public OutputStreamMock() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void write(int arg0) throws IOException {
		// TODO Auto-generated method stub
		wasWriteCalled = true;
	}
	
	public void resetWrite() {
		wasWriteCalled = false;
	}

}
