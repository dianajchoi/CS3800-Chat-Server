import java.awt.*;
import javax.swing.*;
/*
 * File: ChatServer.java
 * Author: Diana Choi, Sara Joshi
 * Class: CS 3800-02 Computer Networks
 * Professor: J. Korah
 * Assignment: Final
 * 
 * 
 * Purpose: This class encapsulates the GUI side of the
 *          chat application. It will receive messages
 *          from the server and clients and display them
 *          accordingly in separate sections of the window.
 */
public class ChatWindow {

	private JFrame window;
	private JTextField typeYourMessageHere;
	private JTextArea seeAllMessagesHere;
	private boolean onUsernameSelection;
	
	public ChatWindow()
	{
		window = new JFrame("USS Smirnubs Chatter");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initializeMessageArea();
		window.setVisible(true);
		onUsernameSelection = true;
	}
	
	public void initializeMessageArea()
	{
		typeYourMessageHere = new JTextField(50);
		seeAllMessagesHere = new JTextArea(16, 50);
		typeYourMessageHere.setEditable(true);
		seeAllMessagesHere.setEditable(false);
		window.getContentPane().add(new JScrollPane(seeAllMessagesHere), BorderLayout.CENTER);
		window.getContentPane().add(typeYourMessageHere, BorderLayout.SOUTH);
		window.pack();
	}
	
	public JTextField getTextField()
	{
		return typeYourMessageHere;
	}
	
	public void printMessageToScreen(String message)
	{
		seeAllMessagesHere.append(message + "\n");
	}
	
	public void exit()
	{
		window.setVisible(false);
		window.dispose();
	}

}
