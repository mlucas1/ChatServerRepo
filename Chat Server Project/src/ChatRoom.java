import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author aanderson1
 * @version 9.18.13
 */
public class ChatRoom {
	
	protected String roomName;
    protected ArrayList<ChatServerThread> clients;
    
    public ChatRoom(String s){
        roomName = s;
    	clients = new ArrayList<ChatServerThread>();
    }
    
    public void addClient(ChatServerThread client){
        clients.add(client);
    }
    
    
}
