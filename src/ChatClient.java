import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
/*
 * File: ChatClient.java
 * Author: Diana Choi, Sara Joshi
 * Class: CS 3800-02 Computer Networks
 * Professor: J. Korah
 * Assignment: Final
 * 
 * 
 * Purpose: This class is a chat client that makes a connection
 *          to the chat server with a unique username and its
 *          own reader and writer.
 */
public class ChatClient {
	private InetAddress host;
	private int portNumber;
	private String username;
	private BufferedReader in;
	private PrintWriter out;
	
	public ChatClient(InetAddress hostname, int port)
	{
		host = hostname;
		portNumber = port;
	}
	
	public static void main(String[] args) throws UnknownHostException
	{
		//Scanner stdin = new Scanner(System.in);
		//System.out.println("Client: Enter host address: ");
		InetAddress hostname = InetAddress.getLocalHost(); //stdin.nextLine();
		//System.out.println("Client: Enter port number: ");
		int port = 5000; //stdin.nextInt();
		//stdin.close();
		ChatClient chatClient = new ChatClient(hostname, port);
		chatClient.beginLoop();
	}
	
	public void beginLoop()
	{
		try
		{
			Socket connection = new Socket(host, portNumber);
			System.out.println("Client: Connected to server");
			Scanner stdin = new Scanner(System.in);
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			out = new PrintWriter(connection.getOutputStream(), true);
			
			Thread write = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					String message = stdin.nextLine();
					while (!message.equals("."))
					{
						out.println(message);
						message = stdin.nextLine();
					}
					stdin.close();
				}
			});
			
			Thread read = new Thread(new Runnable()
			{
				@Override
				public void run()
				{					
					try
					{
						String message = in.readLine();
						while (!message.endsWith(": ."))
						{
							System.out.println(message);
							message = in.readLine();
						}
						connection.close();
					}
					catch (IOException e)
					{
						System.out.println("Client-Read: I/O Exception: " + e.getMessage());
					}
				}
			});
			write.start();
			read.start();
		}
		catch (UnknownHostException e)
		{
			System.out.println("Client: Unknown Host Exception: " + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("Client: I/O Exception: " + e.getMessage());
		}
	}
	
	public void setUsername(String name)
	{
		username = name;
	}
	
	
	public String getUsername()
	{
		return username;
	}
}
