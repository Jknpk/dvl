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
import generated.MazeComType;
import generated.ObjectFactory;
import networking.MazeComMessageFactory;
import networking.UTFInputStream;
import networking.UTFOutputStream;

public class Client {
	
	private String ip;
	private String port;
	private Socket clientSocket;
	
	private UTFOutputStream outToServer;
	private UTFInputStream inFromServer;
	
	private ObjectFactory of;
	private JAXBContext jc;
	
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	
	private ByteArrayOutputStream byteArrayOutputStream;
	private ByteArrayInputStream byteArrayInputStream;
	
	private Thread communicationThread;
	
	private boolean isConnected = false;
	
	private int id = -1;
	
	private MazeComMessageFactory mazeComMessageFactory;
	
	public Client(String ip, String port){
		init(ip, port);
		
		try {
			outToServer = new UTFOutputStream(clientSocket.getOutputStream());
			inFromServer = new UTFInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	
	try {	
		of = new ObjectFactory();
		jc = JAXBContext.newInstance(MazeCom.class);
		marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		unmarshaller = jc.createUnmarshaller();
		byteArrayOutputStream = new ByteArrayOutputStream();
		mazeComMessageFactory = new MazeComMessageFactory();
   	} catch (JAXBException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	}
	
	private void init(String ip, String port) {
		this.ip = ip;
		this.port = port;
		try {
			this.clientSocket = new Socket(ip, Integer.parseInt(port));
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}

	public void connect(){
		if(isConnected) throw new RuntimeException("Client ist bereits connected");
		isConnected = true;
		//start CommunicationThread
		communicationThread = new Thread(new CommunicationThread());
		communicationThread.start();
		System.out.println("HUHU");
	}
	
	
	public void writeToServer(MazeCom mc) throws SocketException{
		try {
			marshaller.marshal(mc, byteArrayOutputStream);
			String string = new String(byteArrayOutputStream.toByteArray());
			outToServer.writeUTF8(string);
			outToServer.flush();
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
	} 

	public void handleServerMessage(MazeCom message) {
		if (message.getMcType() == MazeComType.LOGINREPLY){
			id = message.getLoginReplyMessage().getNewID();
		}
	}

//Kommunikation mit Server über diese private Klasse
	private class CommunicationThread implements Runnable{
		public void run() {
			MazeCom mc = mazeComMessageFactory.createLoginMessage("kajo");
			try {
				writeToServer(mc);
			} catch (SocketException e) {
				e.printStackTrace();
				System.out.println("Konnte mich nicht anmelden :/");
			}
			while(!Thread.currentThread().isInterrupted()){
				try{
					// Wartet auf Nachrichten vom Server
					String string = inFromServer.readUTF8();
//					System.out.println("Bekomme String der Länge: " + string.length());
					//Unmarshalling und Nachrichtenausgabe
					byteArrayInputStream = new ByteArrayInputStream(string.getBytes());
					unmarshaller = jc.createUnmarshaller();
					MazeCom message = (MazeCom) unmarshaller.unmarshal(byteArrayInputStream);
					handleServerMessage(message);
				}catch(JAXBException | IOException e){
					e.printStackTrace();
				}
			}	
		}	
	}
	
	public static void main(String[] args) {
		
		// Port: 5123

		Client client = null;
		try{
			client = new Client(args[0], args[1]);
		} catch (ArrayIndexOutOfBoundsException e){
			client = new Client("127.0.0.1", "5123");	//Defaultwerte
		}	
		client.connect();
	}
}
