package ecse489.group18.frontend;

import java.util.regex.Pattern;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class Helpers {

	private static final String COMMA_REGEX = ".*[,].*";
	
	/**
	 * Prints a header on the terminal.
	 * 
	 * @param header
	 *            The header to be printed.
	 */
	protected static void printHeader(String header) {
		int lengthOfHeader = header.length() + 4;

		System.out.print("+");
		for (int i = 0; i < (lengthOfHeader - 2); i++) {
			System.out.print("-");
		}
		System.out.println("+");

		System.out.println("| " + header + " |");

		System.out.print("+");
		for (int i = 0; i < (lengthOfHeader - 2); i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}
	
	/**
	 * Will validate that the credential are in the right format.
	 * 
	 * @param credential
	 *            The credential (either password or username) to be validated.
	 * @return True if valid and false otherwise.
	 */
	protected static boolean validateCredentials(String username, String password) {
		if (!validateCredential(username) || !validateCredential(password)) {
			return (false);
		}
		return (true);
	}

	/**
	 * Validate the credentials.
	 * 
	 * @param credential
	 *            The credentials to be validated.
	 * @return True if valid and false otherwise.
	 */
	protected static boolean validateCredential(String credential) {
		if (credential == null || credential.length() == 0) {
			return (false);
		}
		if (Pattern.matches(COMMA_REGEX, credential)) {
			return (false);
		}
		return (true);
	}
}
