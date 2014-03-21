package ecse489.group18.frontend.application;

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
	private static final String TXT_REGEX = ".*[.][tT][xX][tT]";
	public static final int USERNAME_MAXIMUM_SIZE = 15;
	
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
		if (credential == null
				|| credential.length() == 0
				|| Pattern.matches(COMMA_REGEX, credential)
				|| credential.length() > USERNAME_MAXIMUM_SIZE) {
			return (false);
		}
		return (true);
	}
	
	public static boolean isJPEG(String filePath) {
		return (Pattern.matches(JPEG_REGEX, filePath));
	}
	
	public static boolean isPNG(String filePath) {
		return (Pattern.matches(PNG_REGEX, filePath));
	}
	
	public static boolean isZIP(String filePath) {
		return (Pattern.matches(ZIP_REGEX, filePath));
	}
	
	public static boolean isTXT(String filePath) {
		return (Pattern.matches(TXT_REGEX, filePath));
	}
	
	/**
	 * Validates if the file path has a supported file extension.
	 * 
	 * @param extension The file path to verify.
	 * @return True if valid and false otherwise.
	 */
	public static boolean isSupportedFileExtention(String filePath) {
		if (isJPEG(filePath) || isPNG(filePath) || isTXT(filePath) || isZIP(filePath)) {
			return (true);
		} else {
			return (false);
		}
	}
}
