package ecse489.group18.frontend;

import java.util.regex.Pattern;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class Helpers {

	private static final String COMMA_REGEX = ".*[,].*";
	private static final String JPEG_REGEX = ".*[.][jJ][pP][eE][gG]";
	private static final String PNG_REGEX = ".*[.][pP][nN][gG]";
	private static final String ZIP_REGEX = ".*[.][zZ][iI][pP]";
	
	/**
	 * Prints a header on the terminal.
	 * 
	 * @param header
	 *            The header to be printed.
	 */
	public static void printHeader(String header) {
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
	public static boolean validateCredentials(String username, String password) {
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
	public static boolean validateCredential(String credential) {
		if (credential == null || credential.length() == 0) {
			return (false);
		}
		if (Pattern.matches(COMMA_REGEX, credential)) {
			return (false);
		}
		return (true);
	}
	
	/**
	 * Validates the supported file extension.
	 * 
	 * @param extension The file path to verify.
	 * @return True if valid and false otherwise.
	 */
	public static boolean validateFileExtention(String extension) {
		if (Pattern.matches(JPEG_REGEX, extension) || Pattern.matches(PNG_REGEX, extension) || Pattern.matches(ZIP_REGEX, extension)) {
			return (true);
		} else {
			return (false);
		}
	}
}
