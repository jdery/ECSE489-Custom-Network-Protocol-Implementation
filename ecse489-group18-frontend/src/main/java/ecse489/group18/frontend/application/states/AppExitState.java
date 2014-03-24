package ecse489.group18.frontend.application.states;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

import ecse489.group18.frontend.application.App;
import ecse489.group18.frontend.application.Helpers;
import ecse489.group18.frontend.messages.DefaultMessages;
import ecse489.group18.frontend.messages.Message;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppExitState extends AppState {

	private final int SLEEPYTIME = 500;
	
	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppExitState(App backPointerApp, BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		Helpers.printHeader("Exiting the application!");
		try {
			
			this.backPointerApp.setUserToLoggedOut();
			
			try { // give the polling threads time to stop
				Thread.sleep(SLEEPYTIME);
			} catch (Exception e) {
				//do nothing
			}
			// This will ensure that only one thread at a time can send requests and retrieve the associated responses.
			synchronized(App.LOCK) {
				this.sendMessage(Message.MessageFactory(DefaultMessages.EXIT));
			}
			
			System.out.println("The connection with the server was closed.");
			this.pressEnterToContinue();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
