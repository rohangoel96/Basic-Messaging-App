import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Server extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output ;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public Server(){
		
		super("Rohan's Messenger");
		userText = new JTextField();
		userText.setEditable(false); 	//can't edit if not connected to anyone
		
		userText.addActionListener(
				
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent event) {
						//sendMessage(event.getActionCommand());
						userText.setText("");	//after the message is sent, clear the text field
					}
				}
				
				);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(1024,768);
		setVisible(true);
			
			}

}
