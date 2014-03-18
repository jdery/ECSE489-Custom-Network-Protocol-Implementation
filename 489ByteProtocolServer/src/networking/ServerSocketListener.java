package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocketFactory;

import logging.LogLevel;
import logging.Logfile;
import networking.auth.AuthenticationManager;
import networking.auth.IAuthenticator;
import database.IResource;

public class ServerSocketListener extends Thread implements IAuthenticator {

	private boolean alive;
	private SSLServerSocketFactory serverSocketFactory;
	private ServerSocket serverSocket;
	private IResource resource;
	
	private AuthenticationManager manager;
	
	public ServerSocketListener(int port, IResource resource) {
		// Resource regarding the System properties: http://stackoverflow.com/questions/5871279/java-ssl-and-cert-keystore
		// How I generated the keystore: http://docs.oracle.com/javase/tutorial/security/toolsign/step3.html
		// Generate the truststore: http://publib.boulder.ibm.com/infocenter/tivihelp/v8r1/index.jsp?topic=%2Fcom.ibm.netcoolimpact.doc5.1.1%2Fadmin%2Fimag_gui_server_generating_the_ssl_keystore_server_certificate_t.html
		
		try {
			// Sets the location of the application's Keystore where the certificate and private key will be stored.
			System.setProperty("javax.net.ssl.keyStore", "keystore_server.jks");
			// Sets the password required to access the private key in hte Keystore file.
			System.setProperty("javax.net.ssl.keyStorePassword", "ThisIsOurPass123");
			System.setProperty("javax.net.ssl.trustStore", "cacerts.jks");
			System.setProperty("javax.net.ssl.trustStorePassword", "ThisIsOurPass123");
			
			this.serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			this.serverSocket = serverSocketFactory.createServerSocket(port);
//			this.serverSocket = new ServerSocket(port);
			this.resource = resource; 
			alive = true;
			
			this.manager = new AuthenticationManager(this);
			
			Logfile.writeToFile("Listening for incoming connections on port: " + port, LogLevel.INFO);
		} catch (IOException e) {
			Logfile.writeToFile("Failed to open server socket due to error: " + e.getMessage(), LogLevel.CRITICAL);
			e.printStackTrace();
			alive = false;
		}
	}
	
	public void run() {
		while(alive) {
			try {
				Logfile.writeToFile("Waiting for a new client connection", LogLevel.DEBUG);
				Socket client = serverSocket.accept();
				
				ClientProcessor cp = new ClientProcessor(client, resource, manager);
				cp.start();
				Logfile.writeToFile("Accepted connection from: " + client.getInetAddress().getHostAddress(), LogLevel.DEBUG);
			} catch (IOException e) {
				Logfile.writeToFile("Failed to create new client connection or socket closed", LogLevel.ERROR);
			}
		}
	}
	
	public void kill() {
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			Logfile.writeToFile("Failed to close server socket", LogLevel.CRITICAL);
		}
		this.alive = false;
	}

	@Override
	public boolean Authenticate(String username, String password) {
		return resource.login(username, password);
	}
}
