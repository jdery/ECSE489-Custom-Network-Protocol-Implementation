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
			if (messages == null) { // Ensures that the polling thread for files is running.
				System.out.println("The pollForFiles thread is not running yet! You need to be logged in!");
			} else {
				System.out.println(messages);

				this.takeInputsFromUser();
			}
		} catch (IOException e) {
			System.out.println("A problem uccured either while writting the file to your file system or while taking inputs on the command line.");
			System.out.println("Make sure this process has the user rights to operate in the folder where it is running.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes the input from the user and either save the file locally or switch to the main state.
	 * 
	 * @throws IOException
	 */
	private void takeInputsFromUser() throws IOException {
		do {
			try {
				System.out.print("Select which file you would like to download (0 to return to main menu): ");
				String sUserInput = bufferedReader.readLine();
				int iUserInput = Integer.parseInt(sUserInput);
				
				// Leaves the menu if the user inputs 0 or an invalid number.
				if (iUserInput == 0) {
					this.pressEnterToContinue();
					this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
					break;
				} else if (iUserInput < 0) {
					throw new IndexOutOfBoundsException();
				}

				// The subtraction is present since the index of the ArrayList will start at 0.
				iUserInput--;
				
				this.writeFileToFileSystem(iUserInput);
				
				System.out.println("The selected file was saved on your local file system.");
			} catch (NumberFormatException e) {
				System.out.println("You must enter a valid integer value!");
			} catch (IndexOutOfBoundsException e) {
				System.out.println("You must enter a valid integer value!");
			}
		} while (true);
	}
	
	/**
	 * Writes the file on the file system based on its position in the array list in which it is stored.
	 * 
	 * @param fileIndexInArrayList The position of the file in the array list.
	 * @throws IOException
	 */
	private void writeFileToFileSystem(int fileIndexInArrayList) throws IOException, IndexOutOfBoundsException {
		String theFilename = this.backPointerApp.getFilenameFromPollingThread(fileIndexInArrayList);
		byte[] theFile = this.backPointerApp.getFilenameFileFromPollingThread(fileIndexInArrayList);
		
		// Writes the file to the local file system.
		FileOutputStream out = new FileOutputStream(theFilename);
		out.write(theFile);
		out.flush();
		out.close();
	}
}
