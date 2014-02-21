/**
 * 
 */
package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author dery
 *
 */
public class AppLoggedState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppLoggedState(App backPointerApp,
			InputStreamReader socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
		
	}

	@Override
	public void execute() {
		this.printHeader("You are now logged in!");
	}
}
