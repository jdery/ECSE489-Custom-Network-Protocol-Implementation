package ecse489.group18.frontend.application.states;

import java.util.NoSuchElementException;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public enum AppStates {
	LOGIN(1),
	LOGOUT_USER(2),
	CREATE_USER(3),
	DELETE_USER(4),
	SEND_ECHO(5),
	SEND_MESSAGE(6),
	SEND_FILE(7),
	CHECK_RECEIVED_MESSAGES(8),
	EXIT_APPLICATION(9),
	MAIN_MENU(10);
	
	private int value;
	
	private AppStates(int value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return The value of the enumerator.
	 */
	public int getValue() {
		return (this.value);
	}
	
	/**
	 * Returns the AppStates enum instantiation based on the integer value.
	 * 
	 * @param value The integer value to consider.
	 * @return The instantiated AppStates enum.
	 */
	public static AppStates getEnum(int value) {
		for (AppStates currentEnum : AppStates.values()) {
			if (currentEnum.getValue() == value) {
				return (currentEnum);
			}
		}
		
		throw new NoSuchElementException("The value entered is does not correspond to any value in the enum.");
	}
}