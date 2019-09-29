package chat;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.*;


public class Client {
	

	Socket socket = null;
	BufferedInputStream input = null;
	DataOutputStream out = null;
	
	
	public Client(String ipAddress, int portNumber) {
		try {
			socket = new Socket(ipAddress, portNumber);
			System.out.println("Connected");
						
			
			input = new BufferedInputStream(System.in);
			
			out = new DataOutputStream(socket.getOutputStream());
		}
		catch (UnknownHostException u) {
			System.out.println(u);
		}
		catch(IOException e) {
			System.out.println(e);
		}
		
		String line = "";
		
		while(!line.equals("Over")) {
			try {
				line = Integer.toString(input.read());
				out.writeUTF(line);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		
		try {
			input.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) throws UnknownHostException{
		
		//gets local host ip address
		InetAddress inetAddress = InetAddress.getLocalHost();
		Scanner input = new Scanner(System.in);
		ArrayList<connection> connectionList = new ArrayList<connection>();
		int id = 0;
		
		int port = 0;
		
		if(args.length == 0) {
			
			System.out.println("Please enter a port number: ");
			if(input.hasNextInt()) {
				port = input.nextInt();
			}else {
				System.out.println("Only integers are allowed");
				System.out.println("Program terminated...bye");
				System.exit(1); 
			}
			
		}else {
			port = Integer.parseInt(args[0]);
		}
		
		
		System.out.println("Hello! Welcome to myChat");
		System.out.println("Type a command to get started or type 'help' for options");
		
		String userInput = input.nextLine();
		String command[] = userInput.split(" ");
		
		//Menu for chat
		while(!command[0].toLowerCase().equals("exit")) {
			if(command[0].toLowerCase().equals("help")) {
				
				System.out.println("Available commands:");
				System.out.println("==> myip : Displays the IP address of this process.");
				System.out.println("==> myport : Display the port on which this process is listening for incoming connections.");
				System.out.println("==> connect <destination> <port> : Establishes a new TCP connection to the specified destination and port number.");
				System.out.println("==> list : Display a numbered list of all the connections.");
				System.out.println("==> terminate <connection id> : Terminates the connection listed under the specified number");
				System.out.println("==> send <connection id> <message> : Send a message to the host on the connection.");
				System.out.println("==> exit : Terminates the program.");
				
			}else if(command[0].toLowerCase().equals("myip")) {
				
				System.out.println("Your IP address is: " + inetAddress.getHostAddress());
				
			}else if(command[0].toLowerCase().equals("myport")) {
				
				System.out.println("Your port number is: " + port);
				
			}else if(command[0].toLowerCase().equals("connect")) {
				
//				if(command.length == 3) {
//					
//					connectionList.add(new connection (++id, Integer.parseInt(command[1]) , command[2]));
//					
//				}else {
//					System.out.println("Wrong command!!!");
//					System.out.println("Follow this structure: connect <IP destination> <Port>");
//				}
//				
				Client client = new Client(inetAddress.getHostAddress(), port);
//				Client client = new Client(connectionList.get(0).getIpAddress(),connectionList.get(0).getPort());
				
			}else if(command[0].toLowerCase().equals("list")){
				
				if(connectionList.size() != 0) {
					System.out.println("	ID		PORT		IP ADDRESS");
					for(int i =0; i < connectionList.size(); i++) {
						System.out.println("	"+ connectionList.get(i).getId() + "		"+connectionList.get(i).getPort()+"		"+connectionList.get(i).getIpAddress());
					}
				}else {
					System.out.println("Your list is empty. Please add new connections!!!");
				}
				
			}else if(command[0].toLowerCase().equals("terminate")){
				System.out.println("Terminate the connection");
			}else if(command[0].toLowerCase().equals("send")) {
				System.out.println("Send message to the specified host");
			}else {
				System.out.println("Command not found. Please try 'help'");
			}
			
			System.out.println("Enter a command: ");
			userInput = input.nextLine();
			String temp[] = userInput.split(" ");
			command = temp.clone();
		}
		
		input.close();
		System.out.println("Program terminated...bye");
		System.exit(1); 
		
	}


}
