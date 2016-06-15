package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import generated.MazeCom;
import generated.ObjectFactory;
import networking.UTFInputStream;
import networking.UTFOutputStream;

public class Client {

	private static final String PORT = "5123";
	private static final String HOST = "127.0.0.1";
	private String ip;
	private String port;
	private Socket clientSocket;

	private UTFOutputStream outToServer;
	private UTFInputStream inFromServer;

	private ObjectFactory of = new ObjectFactory();;
	private JAXBContext jc;

	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	private ByteArrayInputStream byteArrayInputStream;

	private Thread communicationThread;

	private int id;

	public Client(String ip, String port) {
		this.ip = ip;
		this.port = port;
		try {
			this.clientSocket = new Socket(ip, Integer.parseInt(port));
			outToServer = new UTFOutputStream(clientSocket.getOutputStream());
			inFromServer = new UTFInputStream(clientSocket.getInputStream());
			jc = JAXBContext.newInstance(MazeCom.class);
			marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			unmarshaller = jc.createUnmarshaller();
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
	}
	
	

	public void connect() {
		// Login
		Login login = new Login(this);
		this.id = login.getId();
		// Start the Game
		Game game = new Game(this);
		game.start();
	}

	public void writeToServer(MazeCom mc) throws SocketException {
		byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			marshaller.marshal(mc, byteArrayOutputStream);
			String string = new String(byteArrayOutputStream.toByteArray());
			outToServer.writeUTF8(string);
			outToServer.flush();
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
	}

	public MazeCom receiveFromServer() {
		MazeCom mc = null;
		try {
			String string = inFromServer.readUTF8();
			// Unmarshalling und Nachrichtenausgabe
			byteArrayInputStream = new ByteArrayInputStream(string.getBytes());
			unmarshaller = jc.createUnmarshaller();
			mc = (MazeCom) unmarshaller.unmarshal(byteArrayInputStream);
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
		return mc;
	}

	public static void main(String[] args) {

		// Port: 5123

		Client client = null;
		try {
			client = new Client(args[0], args[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			client = new Client(HOST, PORT); // Defaultwerte
		}
		client.connect();
	}

	public int getId() {
		return this.id;
	}
}
