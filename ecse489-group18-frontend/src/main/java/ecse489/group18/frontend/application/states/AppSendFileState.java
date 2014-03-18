package ecse489.group18.frontend.application.states;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import ecse489.group18.frontend.application.App;
import ecse489.group18.frontend.application.Helpers;
import ecse489.group18.frontend.messages.*;

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
			System.out.println("The file you want to transfer must be of the following extention: '.jpeg', '.png', '.zip', '.txt'.");
			System.out.println("The path you will enter will be from the place the application is being executed.");
			System.out.print("File to transfer: ");
			filePath = bufferedReader.readLine();
			
			if (!Helpers.validateFileExtention(filePath)) {
				System.out.println("This file extension is not supported!");
				this.pressEnterToContinue();
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				return;
			}
			
			File fileToSend = new File(filePath);
			int sizeOfFile = (int) fileToSend.length();
			
			if (sizeOfFile > 50000) {
				System.out.println("The file you selected is too big! It must be less than 50,000 bytes!");
				this.pressEnterToContinue();
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				return;
			} else if (sizeOfFile == 0) {
				System.out.println("The file you selected contains nothing! It must be more than 0 byte!");
				this.pressEnterToContinue();
				this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
				return;
			}
			
			String[] pathDivisions = filePath.split(File.separator);
			String fileName = pathDivisions[pathDivisions.length-1];
			
			System.out.println("\nSize of file: " + sizeOfFile + "byte(s)");
			System.out.println("\nName of file: " + fileName);
			
			FileInputStream br = new FileInputStream(filePath);
			byte[] fileContent = new byte[sizeOfFile];
			br.read(fileContent, 0, sizeOfFile);
			System.out.println("The file has been loaded in memory.");
			
			// TODO: the extension will be included in the message Sub-Type.
			int messageSubType = Message.giveSubTypesBasedOnFileExtension(fileName);
			
			Message messageToSend = new Message(MessageType.SEND_FILE, messageSubType, fileContent);
			
			this.sendMessage(messageToSend);
			
			this.pressEnterToContinue();
			this.backPointerApp.changeCurrentState(AppStates.MAIN_MENU);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
