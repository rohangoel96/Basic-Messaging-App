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
						sendMessage(event.getActionCommand());
						userText.setText("");	//after the message is sent, clear the text field
					}
				}
				
				);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(400,600);
		setVisible(true);
			
			}
	
	//set up and run the server
	public void startRunning(){
	
		try {
			
			server = new ServerSocket(6789 ,100);	//port_number (same to used for the client app),backlog( i.e how many people can wait to access this messenger)
			while(true){
				try {
					//start and wait for someone to connect
					waitForConnection();
					
					//once connected, setup input and output streams
					setupStreams();
					
					//send and receive messages
					whileChatting();
					
				} catch (EOFException e) { //EOF : End of File (here connection)
					showMessage("\n Server ended the connection");
				} finally{
					closeConnections();
				}
				
			}
							
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	
	}
	
	//wait for connection and then display connection info
	private void waitForConnection() throws IOException{
		
		showMessage("Waiting for someone to connect ... \n");
		
		//setup the socket (connection)
		connection = server.accept(); //accepts the connection to socket when someone tries to connect
		
		showMessage("Now connected to "+connection.getInetAddress().getHostName());
		
	}
	
	//get stream to send and receive data
	private void setupStreams() throws IOException{
		
		output = new ObjectOutputStream(connection.getOutputStream()); //creating the pathway to connect to the computer the connection socket created
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream()); //no flush in input as can flush only on your input and not their input
		
		showMessage("\nStreams are no setup\n");
		
	}
	
	//during the chat conversation
	private void whileChatting() throws IOException{
		
		String message = "You are now connected !";
		sendMessage(message);
		ableToType(true);
		
		do {
			
			try {
				
				message = (String) input.readObject(); //whenever something sent, it is treated as an object and parse it into int
				showMessage("\n"+message);
				
			} catch (ClassNotFoundException e) {
				showMessage("!! Only string inputs allowed !!");
			}
									
		} while (!message.equals("CLIENT-END"));
		
	}	
	
	//close streams and sockets after you are done chatting
	private void closeConnections(){
		
		showMessage("\nClosing connections ... \n");
		ableToType(false);
		
		try {
			
			output.close();
			input.close();
			connection.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//send message to the client DIFFERENT from showMessage 
	private void sendMessage(String message){
		
		try {
			
			output.writeObject("SERVER : "+message);
			output.flush();
			showMessage("\n SERVER : "+message); //should also show in the chatbox the message sent
			
		} catch (IOException e) {
			chatWindow.append("ERROR : UNABLE TO SEND MESSAGE !");
		}
		
	}
	
	//writes to the chat window
	private void showMessage(final String text){
		
		//makes a new thread that updates a part of the GUI
		SwingUtilities.invokeLater(				
				new Runnable(){					
					public void run(){						
						chatWindow.append(text);						
					}					
				}							
				);
		}
	
	//let's user type in the text box
	private void ableToType(final boolean bool){
		
		//makes a new thread that updates a part of the GUI
		SwingUtilities.invokeLater(				
				new Runnable(){					
					public void run(){						
						userText.setEditable(bool);					
					}					
				}							
				);
		
	}
	
	public static void main(String[] args) {
		
		Server rohan = new Server();
		rohan.setDefaultCloseOperation(EXIT_ON_CLOSE);
		rohan.startRunning();
	}

}
