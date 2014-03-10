package ecse489.group18.experiment3;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class BufferedInputStreamMock extends BufferedInputStream {

	public int available = 0;
	public int read = 0;
	public byte[] b = null;
	
	public BufferedInputStreamMock(InputStream arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public BufferedInputStreamMock(InputStream arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	public void setAvailable(int a) {
		available = a;
	}
	
	@Override
	public int available() {
		return available;
	}
	
	public void setRead(int r) {
		read = r;
	}
	
	public void setRead(byte[] setter) {
		b = setter;
	}
	
	/**
	 * Sets parameter arg to predefined arg variable for dependency injection
	 * @param arg
	 * @return read variable
	 */
	@Override
	public int read(byte[] arg) {
		for (int i = 0; i < arg.length; i++) {
			arg[i] = b[i];
		}
		return read;
	}

}
