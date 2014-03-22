package networking.protocol.types.responses;

public enum SendFileToUser {
	FILE_SENT(0),
	FAILED_TO_WRITE_TO_USER_FILE_STORE(1),
	USER_DOESNT_EXIST(2),
	NOT_LOGGED_IN(3),
	BADLY_FORMATTED(4),
	INTERMEDIATE(5);
	
	private int i;
	private SendFileToUser(int i) {
		this.i = i;
	}
	
	public int getInt() {return i;}
	
	public static SendFileToUser ofInt(int i) {
		for (SendFileToUser stu : SendFileToUser.values())
			if (stu.i == i)
				return stu;
		return BADLY_FORMATTED;
	}
}

