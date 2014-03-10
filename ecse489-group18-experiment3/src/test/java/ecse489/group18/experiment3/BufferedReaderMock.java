package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.Reader;

public class BufferedReaderMock extends BufferedReader {

	String readerLine = "";
	
	public BufferedReaderMock(Reader arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public BufferedReaderMock(Reader arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	public void setReadLine(String s) {
		readerLine = s;
	}
	
	@Override
	public String readLine() {
		return readerLine;
	}

}
