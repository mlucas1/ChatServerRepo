import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author mlucas1
 * @version 9.12.13
 */
public class ChatServer {
	
	private static final int PORT = 9001;
    public static int totalRooms = 0;
	public static int totalGuests = 0;
    public static HashMap<String,ChatRoom> rooms = new HashMap<String,ChatRoom>();
	public static ArrayList<ChatServerThread> clients = new ArrayList<ChatServerThread>();
    public static ChatRoom antechamber;
	
	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) { 
        antechamber = new ChatRoom();
        
		try {
			ServerSocket server = new ServerSocket(PORT);
			
			while (true) {
				totalGuests++;
				ChatServerThread newUser = new ChatServerThread(server.accept());
                antechamber.addClient(newUser);
				clients.add(newUser);
				newUser.start();
			}
		} 
        catch (IOException e) {
			e.printStackTrace();
		}
	}
}
