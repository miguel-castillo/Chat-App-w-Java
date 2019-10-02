// Java implementation of Server side 

import java.io.*; 
import java.util.*; 
import java.net.*; 

 
public class Test1
{ 

	// List that will store the clients or connections
	private static ArrayList<ClientHandler> clientsList = new ArrayList<ClientHandler>();
	
	// counter for clients 
	static int id = 1; 
	
	//server class
	static class myServer implements Runnable {
		
		ServerSocket ss; 
		Socket s; 
		
		//Server constructor..it will get a port on which it will be listening.
		public myServer(int serverPort) throws IOException { 
			this.ss = new ServerSocket(serverPort);
		} 

		@Override
		public void run() {
			// running infinite loop for getting 
			// client request 
			while (true) 
			{ 
				// Accept the incoming request 
				try {
					s = ss.accept();
					System.out.println("New connection request received"); 
					
					// obtain input and output streams 
					DataInputStream dis = new DataInputStream(s.getInputStream()); 
					DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
					
					System.out.println("Adding client to your list with index: "+ id); 
					
					// Create a new handler object for handling this request. 
					ClientHandler newConnection = new ClientHandler(s,"client " + id, dis, dos); 
					
					// Create a new Thread with this object. 
					Thread newClientThread = new Thread(newConnection); 
					
					// add this client to active clients list 
//					ar.add(newConnection); 
					clientsList.add(newConnection);
					

					// start the thread. 
					newClientThread.start(); 
					
					// increment i for new client. 
					id++; 
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 


			} 
			
		}
		
	
		
	}
	
	public static class menu implements Runnable{
		Scanner scn = new Scanner(System.in); 
		
		@Override
		public void run() { 
			while (true) { 

				// read string and gets command. 
				String userInput = scn.nextLine(); 
				String command[] = userInput.split(" ");	
				
				if(command[0].toLowerCase().equals("connect")) {
					
					// establish the connection 
					try {
						Socket s = new Socket(command[1], Integer.parseInt(command[2]));
						
						// obtaining input and out streams 
						DataInputStream dis = new DataInputStream(s.getInputStream()); 
						DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
						
						System.out.println("Adding client to your list with index: "+ id); 
						
						// Create a new handler object for handling this request. 
						ClientHandler newConnection = new ClientHandler(s,"client " + id, dis, dos); 
						
						// Create a new Thread with this object. 
						Thread newClientThread = new Thread(newConnection); 
						
						// add this client to active clients list 
//						ar.add(newConnection); 
						clientsList.add(newConnection);
						

						// start the thread. 
						newClientThread.start(); 
						
						// increment id for new client. 
						id++; 
						
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}else if(command[0].toLowerCase().equals("send")) {
					sendMessage(Integer.parseInt(command[1]), command[2]);
				}
				 
			} 
		} 
		
	}
	
	public static void sendMessage(int connection_Id, String message) {
		int client = connection_Id - 1;
		if( client < clientsList.size()){
			// write on the output stream for the specify client
			try {
				clientsList.get(client).dos.writeUTF(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("Connection ID was not found.");
		}
		
	}

	
	public static void main(String[] args) throws IOException
	{ 
		System.out.println("myChat App started on port 6000");
		System.out.println("There are not connections yet.");
		System.out.println("Try connect <locat ip> <port>");
		
		int port_number = 6000;
		
		//Initialize variable
		menu appMenu = new menu();
		
		//Appserver will listen for new connections and add them to the list automatically.
		//It will also listen for new upcoming messages and display them.
		myServer appServer = new myServer(port_number);
		
		//Create threads
		Thread server = new Thread(appServer);
		Thread menuThread = new Thread(appMenu);
		
		//Starts threads
		server.start();
		menuThread.start();
		
	} 
} 


