import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	/**
	 * Server sits on a public domain and can be accessed by anyone
	 * Here the client can connect to only the server
	 */
	
	public Client(String host){ //IP of the server
		
		super("Rohan's Client");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent event) {
						sendData(event.getActionCommand());
						userText.setText("");
						
					}
				}				
				);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow),BorderLayout.CENTER);
		setSize(400,600);
		setVisible(true);
		
		
	}
	
	
	
	

}
