import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author mlucas1
 * @version 9.11.13
 */
public class ChatServer {
	
	public static ArrayList<Thread> activeUsers = new ArrayList<Thread>();
	
	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) { 
        
		/* Check port exists */
        if (args.length < 1) {
            System.out.println("Usage: ChatServer <port>");
            System.exit(1);
        }
        
        /* This is the server socket to accept connections */
        ServerSocket serverSocket = null;
        
        /* Create the server socket */
        try {
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        } 
        catch (IOException e) {
            System.out.println("IOException: " + e);
            System.exit(1);
        }
        
        /* Continuously listen for new clients and spin off threads for them */
        while (true) {
            
        	ArrayList<Thread> activeThreads = new ArrayList<Thread>();
        	
        	try {
                /* Get a new client */
                Socket clientSocket = serverSocket.accept();
                
                /* Create a thread for the client and start */
                Thread clientThread = new Thread(new ChatServerThread(clientSocket));
                activeThreads.add(clientThread);
                
            } catch (IOException e) {
                System.out.println("Accept failed: " + e);
                System.exit(1);
            }
        }
	}
}
