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
            this.out.println("Welcome!");
            
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
                
                
                //TODO Perform required functions if input is a command; otherwise send to all users
                
                
            } catch (IOException e) {
                /* On exception, stop the thread */
                System.out.println("IOException: " + e);
                return;
            }
        }
    }
    
    private synchronized boolean setNickname(String s) {
    	return false;
    }
    
    private synchronized void broadCastMessage(String s) {
    	
    }
    
    private synchronized boolean disconnect() {
    	return false;
    }
    
    private int parse(String s) {
    	return 0;
    }
    
    public String getNickname() {
    	return username;
    }
    
    public PrintWriter getWriter() {
    	return out;
    }
    
    

}