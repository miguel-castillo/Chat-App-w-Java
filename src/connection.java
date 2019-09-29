package chat;


public class connection {
	
	private int id;
	private int port;
	private String ipAddress;
	
	public connection(int id, int port, String ipAddress) {
		super();
		this.id = id;
		this.port = port;
		this.ipAddress = ipAddress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



}
