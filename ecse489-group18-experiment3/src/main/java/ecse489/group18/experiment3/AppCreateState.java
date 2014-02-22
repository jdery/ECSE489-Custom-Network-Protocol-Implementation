/**
 * 
 */
package ecse489.group18.experiment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppCreateState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppCreateState(App backPointerApp,
			InputStreamReader socketInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, socketInputStream, socketOutputStream,
				bufferedReader);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ecse489.group18.experiment3.AppState#execute()
	 */
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
