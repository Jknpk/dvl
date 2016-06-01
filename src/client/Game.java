package client;

import java.net.SocketException;

import generated.ErrorType;
import generated.MazeCom;
import generated.MazeComType;


public class Game {
	
	private Client client;
	private Taktik taktik = new testTaktik();				// Hier die Taktik festlegen
//	private Taktik taktik = new OliTaktik();				// Hier die Taktik festlegen
	private MazeComMessageFactory mazeComMessageFactory;
	
	public Game(Client client){
		this.client = client;
		this.mazeComMessageFactory = new MazeComMessageFactory();
	}

	public void start() {
		
		while(true){
			MazeCom mc = client.receiveFromServer();
			if(mc.getMcType() == MazeComType.AWAITMOVE){
				// Client ist dran und darf einen Zug schicken 
				MazeCom mcMM = mazeComMessageFactory.createMoveMessage(client.getId(), taktik.getMove(mc.getAwaitMoveMessage()));
				MazeCom mcAnswer = null;
				try {
					client.writeToServer(mcMM);
					mcAnswer = client.receiveFromServer();
				} catch (SocketException e) {
					e.printStackTrace();
				}
				
				if(mcAnswer.getMcType() == MazeComType.ACCEPT){
					if(mcAnswer.getAcceptMessage().isAccept()){
						System.err.println("G�ltiger Zug!");
					}
					else if(!mcAnswer.getAcceptMessage().isAccept() && mcAnswer.getAcceptMessage().getErrorCode() == ErrorType.AWAIT_MOVE){
						System.err.println("Falsche Nachricht!");
					}
					else if(!mcAnswer.getAcceptMessage().isAccept() && mcAnswer.getAcceptMessage().getErrorCode() == ErrorType.ILLEGAL_MOVE){
						System.err.println("Inkorrekter Zug!");
					}
				}
				else if(mcAnswer.getMcType() == MazeComType.DISCONNECT){
					if(mcAnswer.getDisconnectMessage().getErrorCode() == ErrorType.TOO_MANY_TRIES){
						System.err.println("Zu viele Versuche!");
					}
				}
				
			}
			else if(mc.getMcType() == MazeComType.WIN){
				// Spiel vorbei, jemand hat gewonnen
				System.out.println("Und der Gewinner ist.. : " + mc.getWinMessage().getWinner());
				break;
			}
			
		}
		
	}
	
	
	
	
	
}
