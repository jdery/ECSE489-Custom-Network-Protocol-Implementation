package ecse489.group18.experiment3;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class App implements Runnable {
	
	InputStreamReader socketInputStream;
	OutputStream socketOutputStream;

	public App(Socket serverSocket) throws Exception {
		socketInputStream = new InputStreamReader(serverSocket.getInputStream());
		socketOutputStream = serverSocket.getOutputStream();
	}

	public void run() {
		
	}
}
