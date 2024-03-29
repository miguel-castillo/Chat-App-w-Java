// Socket Chat App By Misael and Miguel CS4770

import java.io.*; 
import java.util.*; 
import java.net.*; 

 
public class Test2 { 

	// List that will store the clients or connections
	private static ArrayList<ClientHandler> clientsList = new ArrayList<ClientHandler>();
	
	// counter for clients 
	static int id = 1; 
	static int port_number;
	static Scanner scanner = new Scanner(System.in); 
	
	public static void main(String[] args) throws IOException { 
		//Validates the port number when app starts; otherwise, it stops the app.
		if(args.length < 1) {
			System.out.println("Enter a port number to start the App: ");
			String userInput = scanner.nextLine(); 
			try {
				port_number = Integer.parseInt(userInput);
			}catch(Exception e){
				invalidPort();
			}
			
		}else {
			try {
				port_number = Integer.parseInt(args[0]);
			}catch(Exception e){
				invalidPort();
			}
		}
		
		InetAddress ip = InetAddress.getLocalHost();
		System.out.println("Welcome to myChat App");
		System.out.println("***Your IP is: "+ip.getHostAddress()+" and port "+port_number+" ***");
		System.out.println("Try 'help' to display command options.");
		
		
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
		//Close all connections when windows gets closed.
		Runtime.getRuntime().addShutdownHook(new Thread() {

		    @Override
		    public void run() {
		    	for(int i = 0; i< clientsList.size(); i++) {
					sendMessage(i+1,"logout");
				}
		    }

		});
		
	} 
	
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
					ClientHandler newConnection = new ClientHandler(s, dis, dos, id); 
					
					// Create a new Thread with this object. 
					Thread newClientThread = new Thread(newConnection); 
					
					// add this client to active clients list 
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
		InetAddress ip;
		
		
		
		@Override
		public void run() { 
			while (true) { 

				// read string and gets command. 
				String userInput = scn.nextLine(); 
				String command[] = userInput.split(" ");
				try {
					ip = InetAddress.getLocalHost();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				if(command[0].toLowerCase().equals("help")) {
					System.out.println("myip: Display the IP address of this process");
					System.out.println("myport: Display the port on which this process is listening for incoming connections");
					System.out.println("connect <destination> <port no>: This command establishes a new TCP connection to the specified<destination> at the specified <port no>.");
					System.out.println("list: Display a numbered list of all the connections this process is part of.");
					System.out.println("terminate <connection id>: This command will terminate the connection listed under the specified number when LIST is used to display all connections.");
					System.out.println("send <connection id.> <message>: This will send the message to the host on the connection that is designated by the number 3 when command \"list\" is used.");
					System.out.println("exit: Close all connections and terminate this process");
				}else if(command[0].toLowerCase().equals("myip")) {
					System.out.println("Your IP Adress is: " + ip.getHostAddress());
				}else if(command[0].toLowerCase().equals("myport")) {
					System.out.println("Your Port Number is: " + port_number);	
				}else if(command[0].toLowerCase().equals("connect")) {
					
					// establish the connection 
					try {
						Socket s = new Socket(command[1], Integer.parseInt(command[2]));
						
						// obtaining input and out streams 
						DataInputStream dis = new DataInputStream(s.getInputStream()); 
						DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
						
						System.out.println("Adding client to your list with index: "+ id); 
						
						// Create a new handler object for handling this request. 
						ClientHandler newConnection = new ClientHandler(s, dis, dos, id); 
						
						// Create a new Thread with this object. 
						Thread newClientThread = new Thread(newConnection); 
						
						// add this client to active clients list 
						clientsList.add(newConnection);
						

						// start the thread. 
						newClientThread.start(); 
						
						// increment id for new client. 
						id++; 
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
				}else if(command[0].toLowerCase().equals("list")) {
					if(clientsList.isEmpty()) {
						System.out.println("There are no connections.");
					} else {
						System.out.println("id: IP address				Port No.");
						for (int i = 0; i < clientsList.size(); i++) {
							System.out.println((i+1) + ": " + clientsList.get(i).s.getLocalAddress() + "			" + clientsList.get(i).s.getPort());
						}
					}
				}else if(command[0].toLowerCase().equals("terminate")) {
					//TODO
					int connectionID = Integer.parseInt(command[1]) - 1;
					if(clientsList.isEmpty()) {
						System.out.println("There are no connections.");
					} else if ((connectionID) > clientsList.size() || (connectionID) < 0) {
						System.out.println("Please enter a valid connection ID.");
					} else {
						ClientHandler connection = clientsList.get(connectionID);
						terminateConnection(connection, connectionID);
					}
					
				}else if(command[0].toLowerCase().equals("send")) {
					
					String message = "";
					int destination;
					
					if(command.length > 2) {
						//puts messages together
						for(int i = 2; i < command.length; i++) {
							message += command[i] + " ";
						}
						
						try {
							destination = Integer.parseInt(command[1]);
							sendMessage(destination, message);
						}catch(Exception e){
							System.out.println("Please enter a valid connection id and try again!!!!");
						}
						
						
						
					}else {
						System.out.println("Invalid format.");
						System.out.println("Try 'send <connection id> <message>'");
					}
					
					
				}else if(command[0].toLowerCase().equals("exit")) {
					terminateApp();
				}else {
					System.out.println("Command not found. Try 'help' for a list of valid commands.");
				}
				 
			} 
		} 
		
	}
	
	//Function to send messages
	public static void sendMessage(int connection_Id, String message) {
		int client = connection_Id - 1;
		if( client < clientsList.size()){
			// write on the output stream for the specify client
			try {
				clientsList.get(client).dos.writeUTF(message);
				System.out.println("Message sent");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("Connection ID was not found.");
		}
		
	}
	
	//validate port number
	public static void invalidPort() {
		System.out.println("Please make sure you input a valid port number.");
		System.out.println("Try running the program again with a valid port number.");
		terminateApp();
	}
	
	//terminate any connection and removes it from the list
	public static void terminateConnection(ClientHandler client, int id) {
		ClientHandler connection = client;
		try {
			
			connection.s.close();
			connection.dis.close();
			connection.dos.close();
			connection.stopRunning();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientsList.remove(id);
	}
	
	//close the app safely
	public static void terminateApp() {
		for(int i = 0; i< clientsList.size(); i++) {
			sendMessage(i+1,"logout");
		}
		System.out.println("Program Terminated. Bye...");
		System.exit(0);
	}
	
	// remove socket connections that are closed from the list.
	public static void removeCloseConnection() {
		for(int i = 0; i<clientsList.size(); i++) {
			if(clientsList.get(i).s.isClosed()) {
				clientsList.remove(i);
			}
		}
	}

} 



