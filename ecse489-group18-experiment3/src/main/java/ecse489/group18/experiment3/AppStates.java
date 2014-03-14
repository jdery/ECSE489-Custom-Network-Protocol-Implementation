/**
 * 
 */
package ecse489.group18.experiment3;

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
	CHECK_RECEIVED_MESSAGES(7),
	EXIT_APPLICATION(8),
	MAIN_MENU(9);
	
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
	
	public static AppStates getEnum(int value) {
		for (AppStates currentEnum : AppStates.values()) {
			if (currentEnum.getValue() == value) {
				return (currentEnum);
			}
		}
		
		throw new NoSuchElementException("The value entered is does not correspond to any value in the enum.");
	}
}