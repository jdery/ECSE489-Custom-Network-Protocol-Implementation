package networking;

import java.net.*;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import networking.auth.AuthenticationManager;
import networking.auth.IAuthenticator;
import database.IResource;
import logging.LogLevel;
import logging.Logfile;

public class ServerSocketListener extends Thread implements IAuthenticator {

	private boolean alive;
	private SSLServerSocketFactory serverSocketFactory;
	private ServerSocket serverSocket;
	private IResource resource;
	
	private AuthenticationManager manager;
	
	public ServerSocketListener(int port, IResource resource) {
		// Resource regarding the System properties: http://stackoverflow.com/questions/5871279/java-ssl-and-cert-keystore
		// How I generated the keystore: http://docs.oracle.com/javase/tutorial/security/toolsign/step3.html
		try {
			// Sets the location of the application's Keystore where the certificate and private key will be stored.
			System.setProperty("javax.net.ssl.keyStore", "telecom_server_keystore");
			// Sets the password required to access the private key in hte Keystore file.
			System.setProperty("javax.net.ssl.keyStorePassword", "P455w0!D");
			
			this.serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			this.serverSocket = serverSocketFactory.createServerSocket(port);
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
				
				SSLSession session = ((SSLSocket) client).getSession();
			    Certificate[] cchain2 = session.getLocalCertificates();
			    for (int i = 0; i < cchain2.length; i++) {
			      System.out.println(((X509Certificate) cchain2[i]).getSubjectDN());
			    }
				
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
