/**
 * 
 */
package ecse489.group18.frontend;

/**
 * @author Jean-Sebastien Dery
 * @author Matthew McAllister
 * 
 */
public class User {
	
	private String username;
	private String password;

	/**
	 * 
	 */
	public User(String username, String password) {
		// Need to validate this information here.
		this.username = username;
		this.password = password;
	}
}
