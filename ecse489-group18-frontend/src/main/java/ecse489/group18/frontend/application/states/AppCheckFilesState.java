package ecse489.group18.frontend.application.states;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ecse489.group18.frontend.application.App;
import ecse489.group18.frontend.application.Helpers;

public class AppCheckFilesState extends AppState {

	/**
	 * @param backPointerApp
	 * @param socketInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppCheckFilesState(App backPointerApp,
			BufferedInputStream bufferedInputStream,
			OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream,
				bufferedReader);
	}

	@Override
	public void execute() {
		try {
			Helpers.printHeader("Files received!!!");
			
			// if no user is logged in
			if (!this.backPointerApp.isUserLoggedIn()) {
				System.out.println("You are not logged in!");
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				this.pressEnterToContinue();
				return;
			}
			
			String messages = this.backPointerApp.getFilenameMessagesFromPollingThread();
			if (messages == null) {
				System.out.println("The pollForFiles thread is not running yet! You need to be logged in!");
			} else {
				System.out.println(messages);
				
				int iUserInput = 0;
				do {
					try {
						System.out.print("Select which file you would like to download (0 to return to main menu): ");
						String sUserInput = bufferedReader.readLine();
						iUserInput = Integer.parseInt(sUserInput);
						
						// break if 0
						if (iUserInput == 0) break;
						
						int _iUserInput = iUserInput;
						
						// subtract 1 here because arraylist indexing starts at 0, and this one starts at 1
						_iUserInput--;
						
						String theFilename = this.backPointerApp.getFilenameFromPollingThread(_iUserInput);
						byte[] theFile = this.backPointerApp.getFilenameFileFromPollingThread(_iUserInput);
						
						FileOutputStream out = new FileOutputStream(theFilename);
						out.write(theFile);
						out.flush();
						out.close();
						
						// debug
						System.out.println("We have a file!");
						
						
					} catch (NumberFormatException e) {
						System.out.println("You must enter an integer value");
					}
				} while (true);
								
			}
			this.pressEnterToContinue();
			this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Displays "Press enter to continue" and waits for a carriage return.
	 * @throws IOException 
	 */
	protected void pressEnterToContinue() throws IOException {
		System.out.println("\nPress enter to continue...");
		bufferedReader.readLine();
	}

}
