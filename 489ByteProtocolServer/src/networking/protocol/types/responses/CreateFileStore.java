package networking.protocol.types.responses;

public enum CreateFileStore {
	STORE_CREATED(0),
	STORE_ALREADY_EXISTS(1),
	NOT_LOGGED_IN(2);
	
	private int i;
	private CreateFileStore(int i) {
		this.i = i;
	}
	
	public int getInt() {return i;}
	public static CreateFileStore ofInt(int i) {
		for (CreateFileStore cs : CreateFileStore.values())
			if (cs.i == i)
				return cs;
		return STORE_ALREADY_EXISTS;
	}
}
