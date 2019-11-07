import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
/*
 * File: ChatServer.java
 * Author: Diana Choi, Sara Joshi
 * Class: CS 3800-02 Computer Networks
 * Professor: J. Korah
 * Assignment: Final
 * 
 * 
 * Purpose: This class is a server that listens for
 *          connections from chat users. It keeps track
 *          of all the users currently connected using a
 *          concurrent HashMap.
 */
public class ChatServer {

	private int portNumber;
	private Set<String> usernames = new HashSet<>();
	private Set<UserThread> clients = new HashSet<>();
	
	public ChatServer(int port)
	{
		portNumber = port;
	}
	
	public static void main(String[] args) throws IOException
	{
		//Scanner stdin = new Scanner(System.in);
		//System.out.println("Server: Enter port number: ");
		int port = 5000; //stdin.nextInt();
		//stdin.close();
		ChatServer chatServer = new ChatServer(port);
		chatServer.beginLoop();
	}
	
	public void beginLoop()
	{
		try (ServerSocket server = new ServerSocket(portNumber))
		{
			System.out.println("Server: Listening on port " + portNumber);
			
			while (true)
			{
				Socket connection = server.accept();
				System.out.println("Server: Accepted new connection");
				
				UserThread newClient = new UserThread(connection, this);
				clients.add(newClient);
				newClient.start();
			}
		}
		catch (IOException e)
		{
			System.out.println("Server: I/O Exception: " + e.getMessage());
		}
		
	}
	
	public void writeMessage(String message)
	{
		for (UserThread client : clients)
		{
			client.writeMessage(message);
		}
	}
	
	public void writeMessage(String message, UserThread notMe)
	{
		for (UserThread client : clients)
		{
			if (client != notMe)
			{
				client.writeMessage(message);
			}
		}
	}
	
	public void addUsername(String newUsername)
	{
		usernames.add(newUsername);
	}
	
	public Set<String> getUsernames()
	{
		return usernames;
	}
	
	public void removeUser(String username, UserThread client)
	{
		if (usernames.remove(username))
		{
			clients.remove(client);
			System.out.println("Server: " + username + " has left the chatroom.");
		}
	}
	
	private class UserThread extends Thread
	{
		private String username;
		private Socket socket;
		private ChatServer server;
		private PrintWriter out;
		private BufferedReader in;
		
		public UserThread(Socket connection, ChatServer chatServer)
		{
			this.socket = connection;
			this.server = chatServer;
		}
		
		public void run()
		{
			try
			{
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				registerNewUser();
				server.writeMessage(username + " has joined the chatroom!", this);
				
				String message = "";
				while (!message.equals("."))
				{
					message = in.readLine();
					server.writeMessage(username + ": " + message);
				}
				server.removeUser(username, this);
				server.writeMessage(username + " has left the chatroom", this);
			}
			catch (IOException e)
			{
				System.out.println("UserThread: I/O Exception: " + e.getMessage());
			}
		}
		
		public void registerNewUser() throws IOException
		{
			out.println("What is your name?");
			username = in.readLine();
			while (username == null || server.getUsernames().contains(username))
			{
				out.println("Invalid username or username already in use. Try another.");
				out.println("What is your name?");
				username = in.readLine();
			}
			server.addUsername(username);
			out.println("Welcome " + username);
		}
		
		public void writeMessage(String message)
		{
			out.println(message);
		}
	}
}

