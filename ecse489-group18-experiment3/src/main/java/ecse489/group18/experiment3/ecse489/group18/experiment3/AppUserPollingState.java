package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppUserPollingState extends AppState implements Runnable {
	
	private final int SLEEPING_PERIOD = 1000;

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppUserPollingState(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	/**
	 * 
	 */
	public void run() {
		while(true) {
			try {
				this.execute();
				Thread.sleep(SLEEPING_PERIOD);
			} catch (InterruptedException e) {
				System.err.println("A problem occured while trying to sleep the polling thread.");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void execute() {
		try {
			this.sendMessage(Message.MessageFactory(DefaultMessages.QUERY_MESSAGES));
			Message response = this.readMessage();
			
			
			
		} catch (Exception e) {
			System.err.println("A problem occured while polling the server for new messages.");
			e.printStackTrace();
		}
	}
}
