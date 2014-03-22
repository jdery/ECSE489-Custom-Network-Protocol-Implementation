package database;

public interface IResource {
	public boolean login(String username, String password);
	public boolean createUser(String username, String password);
	public boolean deleteUser(String username);
	
	public boolean createUserDataTable(String username);
	public boolean userDataTableExists(String username);
	
	public boolean userFileTableExists(String username);
	public boolean createUserFileTable(String username);
	
	public boolean sendMessageToUser(String dUsername, String sUsername, String message);
	public boolean sendFileToUser(String dUsername, String sFilename, String sUsername, byte[] file);
	
	public UserMessage[] getNewUserMessages(String username);
	public UserFile[] getNewUserFiles(String username);
}
