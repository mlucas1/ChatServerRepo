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
    
    public ChatServerThread(Socket socket) {
        super("ChatServerThread");
        
    	this.socket = socket;
        
        try {
        	/* Define i/o for this thread (through the socket) */
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            
            /* DEBUG */
            System.out.println("New User Connected!");
            
            /* Welcome the user */
            this.out.println("<System> Welcome!");
            
            setNickname("Guest " + ChatServer.totalGuests);
            
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
                else
                	broadcastMessage(fromClient);
                
                
            } catch (IOException e) {
                /* On exception, stop the thread */
                System.out.println("IOException: " + e);
                return;
            }
        }
    }
    
    private synchronized boolean setNickname(String s) {
    	synchronized(ChatServer.clients){
            for (ChatServerThread user: ChatServer.clients){
                if (s.equalsIgnoreCase(user.getNickname())){
                    return false;
                }
            }
        }
    	if(username != null)
    		broadcastMessage("I'm changing my name to " + s);
    	this.out.println("<System> Your nickname is now " + s);
        username=s;
        return true;
    }
    
    private synchronized void broadcastMessage(String s) {
    	synchronized(ChatServer.clients) {
    		for (ChatServerThread c : ChatServer.clients) {
    			c.getWriter().println("<" + username + ">"+ " " + s);
    		}
    	}
    }
    
    private synchronized boolean disconnect() {
    	synchronized(ChatServer.clients) {
    		ChatServer.clients.remove(this);
    	}
    	
    	return false;
    }
    
    private int parse(String s) //returns an integer associated with an interpretation of the intended effect of a message sent to the server
    {
        if(s.charAt(0) != '/') {
        	return 0;
        }
        else if (s.substring(0,6).equals("/nick ")) {
        	return 1;
        }
        else if (s.substring(0,11).equals("/disconnect")) {
        	return 2;
        }
        else
        	return 3;
        
    	
    	/*String[] stringArray = s.split("\\w+");
        if (s.charAt(0) != '/') //checks to see if incoming text is a command. If not, it will check to see what type of command it is.
        {
            return 0;
        }
        else if (stringArray[0].equals( "/nick"))//checks to see if incoming text is intended to rename the user
        {
            return 1;
        }
        else if (stringArray[1].equals("/disconnect"))//checks to see if incoming text ins intended to disconnect the user
        {
            return 2;
        }
        else//checks to see if command is nonsensical, and thus can be disregarded or responded to privately with an error message.
        {
            return 3;
        }*/
    }
    
    public String getNickname() {
    	return username;
    }
    
    public PrintWriter getWriter() {
    	return out;
    }
    
    

}
