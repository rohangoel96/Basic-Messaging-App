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
		
		super("Rohan's Messenger - Client");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
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
	
	//connect to server
	public void startRunning(){
		
		try {
			
			connectToServer();
			setupStreams();
			whileChatting();
			
		} catch (EOFException e) {
			showMessage("\nClient terminated the connection\n");
		}catch(IOException e2){
			e2.printStackTrace();
		}finally{
			closeConnection();
		}
			
	}

	//connect to server
	private void connectToServer() throws IOException{
		
		showMessage("Attempting connection....");	//trying to make the socket
		connection = new Socket(InetAddress.getByName(serverIP), 6789);	//ServerIP, port(same as Server)
		showMessage("\nConnected to : "+connection.getInetAddress().getHostName());
	
	}
	
	//set up stream to send and recieve messages
	private void setupStreams() throws IOException{
		
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\nSteams are now setuped\n");
		
	}
	
	//while chatting with server
	private void whileChatting() throws IOException{
		
		ableToType(true);
		do {
			try {
				
				message = (String) input.readObject();
				showMessage("\n"+message);
					
			} catch (ClassNotFoundException e) {
				showMessage("Type only strings please");
			}
			
		} while (!message.equals("SERVER : END"));
		
	}
	
	//closes the streams and sockets
	private void closeConnection(){
		
		showMessage("\nClosing connections ...");
		ableToType(false);
		
		try {
			
			output.close();
			input.close();
			connection.close();
						
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	
	//send message to server
	private void sendMessage(String message){
		
		try {
			
			output.writeObject("CLIENT : "+message);
			output.flush();
			showMessage("\nCLIENT : "+message);
			
		} catch (IOException e) {
			chatWindow.append("\nERROR : Can't send message");
		}
		
	}
	
	//update the chatWindow
	private void showMessage(final String message){
		
		SwingUtilities.invokeLater(
				
				new Runnable(){
					public void run(){
						chatWindow.append(message);
					}
				}	
				);
	
	}
	private void ableToType(boolean bool){

		SwingUtilities.invokeLater(
				
				new Runnable(){
					public void run(){
						userText.setEditable(bool);
					}
				}	
				);	
	}

	public static void main(String[] args) {
		
		Client rohan_client = new Client("127.0.0.1"); //The constructor needs an IP for the server
		rohan_client.setDefaultCloseOperation(EXIT_ON_CLOSE);
		rohan_client.startRunning();
	}
	
}
