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
public class AppExitState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppExitState(App backPointerApp, InputStream socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		this.printHeader("Exiting the application!");
		try {
			this.sendMessage(Message.MessageFactory(DefaultMessages.EXIT));
			System.out.println("The connection with the server was closed.\nPress enter to close the program...");
			bufferedReader.readLine();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
