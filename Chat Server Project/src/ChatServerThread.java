import java.io.*;
import java.net.*;

/**
 * Class ChatServerThread defines the way each new ChatServer client will interact with the server
 * 
 * @author mlucas1
 * @version 9.12.13
 */
public class ChatServerThread extends Thread {
	protected Socket         	socket;
	protected PrintWriter    	out;
	protected BufferedReader	in;
	protected String			username;
    protected ChatRoom          room;
    protected boolean           canMessage;
    
    public ChatServerThread(Socket socket) {
        super("ChatServerThread");
        
    	this.socket = socket;
        
        try {
        	/* Define i/o for this thread (through the socket) */
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            
            /* DEBUG */
            System.out.println("New User Connected!");
            
            
            
            setNickname("Guest " + ChatServer.totalGuests);
            room=ChatServer.antechamber;
            canMessage=false;
            
            
            /* Welcome the user */
            this.out.println("<System> Welcome to the chatroom! You are " + "\"" + username + "\"" + ". There are " + (room.clients.size()+1) + " total users in the room.");
            
            
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
    
    public void run() {
        /*	This thread will constantly listen for client input, and send it to the server.
         	It will also output messages from other connected clients to this client.
         */
    	
        while (true) {
            try {
                /* Get string from client */
                String fromClient = this.in.readLine();
                
                int messageType = parse(fromClient);
                
                if (messageType == 3) {
                	this.out.println("<System> Command unrecognizable. Valid commands are: /nick \"username\" and /disconnect");
                }
                else if (messageType == 2) {
                	disconnect();
                }
                else if (messageType == 1) { // /nick Matthew
                	String name = fromClient.substring(6);
                	if (!setNickname(name)) {
                		this.out.println("<System> Username unavailable. Try again.");
                	}
                	else
                		setNickname(name);
                }
                else if (messageType == 4){
                    String roomName = fromClient.substring(6);
                    joinRoom(roomName);
                }
                else if (messageType != -1)
                	broadcastMessage(fromClient);
                
                
            } catch (IOException e) {
                /* On exception, stop the thread */
                System.out.println("IOException: " + e);
                return;
            }
        }
    }
    
    public boolean joinRoom(String roomName){
        if (ChatServer.rooms.containsKey(roomName)){
            return false;
        }
        room=ChatServer.rooms.get(roomName);
        synchronized(room.clients){
            for (ChatServerThread user : room.clients){
                user.out.println("<System> New user joined: " + username);
            }
        }
        
        canMessage = true;
        return true;
    }
    
    private synchronized boolean setNickname(String s) {
    	synchronized(ChatServer.clients){
            for (ChatServerThread user: ChatServer.clients){
                if (s.equalsIgnoreCase(user.getNickname())){
                    return false;
                }
            }
        }
    	if(username != null) {
    		if(canMessage) {
    			synchronized(room.clients){
    				for (ChatServerThread c : room.clients)
    					c.out.println("<System> " + username + "'s nickname is now " + "\"" + s + "\"" + ".");
    			}
    		}
    	}
        username=s;
        return true;
    }
    
    private synchronized void broadcastMessage(String s) {
    	if (canMessage){
            synchronized(room.clients) {
                for (ChatServerThread c : room.clients) {
                    c.getWriter().println(timeStamp() + " <" + username + ">"+ " " + s);
                }
            }
        }
    }
    
    @SuppressWarnings("deprecation")
	private synchronized boolean disconnect() {
    	synchronized(ChatServer.clients) {
    		ChatServer.clients.remove(this);
    		for (ChatServerThread c : ChatServer.clients) {
    			c.out.println("<System> " + "\"" + username + "\"" + "has disconnected.");
    		}
    		this.stop();
    		
    	}
    	
    	return false;
    }
    
    private int parse(String s) //returns an integer associated with an interpretation of the intended effect of a message sent to the server
    {
    	if(s == null || s.length() == 0) {//checks to see if input string is empty or null[
    		return -1;
    	}
    	else if(s.charAt(0) != '/') {//checks to see if the input string is a command
        	return 0;
        }
        else if (s.indexOf("/nick ") == 0){//checks to see if user is attempting to rename themselves
        	return 1;
        }
        else if (s.equals("/disconnect")){//checks to see if user is attempting to disconnect
        	return 2;
        }
        else if (s.indexOf("/join ") == 0){//checks to see if user is attempting to join an existing chatroom
            return 4;
        }
        else if (s.indexOf("/newRoom ") == 0){//checks to see if user is attempting to create a new chatroom
            return 5;
        }
        else
        	return 3;
    }
    
    public String getNickname() {
    	return username;
    }
    
    public PrintWriter getWriter() {
    	return out;
    }
    
    private String timeStamp(){
    	int time = (int) System.currentTimeMillis();
    	time /= 1000;
	int hours = time / 3600;
    	int minutes = (time - 3600 * hours)/60;
	int seconds = time - 3600  * hours - 60 * minutes;
	return ("<"+hours+ ":"+minutes+":"+seconds+">");
    }

}
