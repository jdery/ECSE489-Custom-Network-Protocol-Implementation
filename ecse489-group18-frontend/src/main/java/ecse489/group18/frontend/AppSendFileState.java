package ecse489.group18.frontend;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.OutputStream;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class AppSendFileState extends AppState {

	/**
	 * @param backPointerApp
	 * @param bufferedInputStream
	 * @param socketOutputStream
	 * @param bufferedReader
	 */
	public AppSendFileState(App backPointerApp, BufferedInputStream bufferedInputStream, OutputStream socketOutputStream, BufferedReader bufferedReader) {
		super(backPointerApp, bufferedInputStream, socketOutputStream, bufferedReader);
		
	}

	@Override
	public void execute() {
		try {
			Helpers.printHeader("Sending a file to another user!");
			
			if (!this.backPointerApp.isUserLoggedIn()) {
				System.out.println("You are not logged in!");
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				this.pressEnterToContinue();
				return;
			}	
			
			String destination, from, filePath;
			System.out.print("From user: ");
			from = bufferedReader.readLine();
			System.out.print("Destination user: ");
			destination = bufferedReader.readLine();
			System.out.println("The file you want to transfer must be of the following extention: '.jpeg', '.png', '.zip'.");
			System.out.println("The path you will enter will be from the place the application is being executed.");
			System.out.print("File to transfer: ");
			filePath = bufferedReader.readLine();
			
			if (!Helpers.validateFileExtention(filePath)) {
				System.out.println("This file extension is not supported!");
				this.pressEnterToContinue();
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				return;
			}
			
			this.pressEnterToContinue();
			this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
