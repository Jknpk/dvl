package client;

import generated.ErrorType;
import generated.MazeCom;
import generated.MazeComType;


public class Login {

	private MazeComMessageFactory mazeComMessageFactory;
	private Client client;
	private int id;
	
	public Login(Client client) {
		this.client = client;
		mazeComMessageFactory = new MazeComMessageFactory();
		sendLogin();
	}
	private void sendLogin() {
		MazeCom mc = mazeComMessageFactory.createLoginMessage("Team-PurpleMaze");
		try {
			client.writeToServer(mc);
			handleLoginReply(client.receiveFromServer());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Der Login schlug fehl!");
		}
		
	}
	public void handleLoginReply(MazeCom message) {
	
		if (message.getMcType() == MazeComType.LOGINREPLY){
			this.id = message.getLoginReplyMessage().getNewID();
			System.out.println("Erfolgreich am Server angemeldet! Zugewiesene ID: " + getId());
		}
		else if(message.getMcType() == MazeComType.ACCEPT){
			if(!message.getAcceptMessage().isAccept() && message.getAcceptMessage().getErrorCode()==ErrorType.AWAIT_LOGIN){
				System.err.println("Falsche Nachricht wurde gesendet!");
				System.exit(1);
			}		
		}
		else if(message.getMcType() == MazeComType.DISCONNECT){
			if(message.getDisconnectMessage().getErrorCode() == ErrorType.TOO_MANY_TRIES){
				System.err.println("Zu viele Login-Versuche! Verbindung zum Serevr wird beendet.");
				System.exit(1);
			}
			
		}
		
		
		
	}

	public int getId(){
		return this.id;
	}
	
	
	
}
