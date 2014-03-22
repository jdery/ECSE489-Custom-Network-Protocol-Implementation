package database;

import networking.protocol.IncomingPacketHandler;

public class UserFile {

	public String username;
	public String filename;
	public byte[] file;
	public String time;
	
	public UserFile() {
		username = "";
		file = null;
		time = "";
	}
	
	public UserFile(String u, String fn, byte[] f, String t) {
		username = u;
		filename = fn;
		file = f;
		time = t;
	}
	
	public String format() {
		StringBuilder sb = new StringBuilder();
		sb.append(username);
		sb.append(IncomingPacketHandler.FIELD_TERMINATOR);
		sb.append(filename);
		sb.append(IncomingPacketHandler.FIELD_TERMINATOR);
		sb.append(time);
		return sb.toString();
	}
	
	public byte[] getFileByteArray() {
		return file;
	}

}
