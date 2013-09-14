import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author mlucas1
 * @version 9.12.13
 */
public class ChatServer {
	
	private static final int PORT = 9001;
	public static int totalGuests = 0;
	public static ArrayList<ChatServerThread> clients = new ArrayList<ChatServerThread>();
	
	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) { 
        
		try {
			ServerSocket server = new ServerSocket(PORT);
			
			while (true) {
				totalGuests++;
				ChatServerThread newUser = new ChatServerThread(server.accept());
				clients.add(newUser);
				newUser.start();
			}
		} 
        catch (IOException e) {
			e.printStackTrace();
		}
	}
}
