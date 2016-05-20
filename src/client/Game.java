package client;

import generated.MazeCom;
import generated.MazeComType;

public class Game {
	
	Client client;
	Taktik taktik = new testTaktik();
	
	public Game(Client client){
		this.client = client;
	}

	public void start() {
		
		while(true){
			MazeCom mc = client.receiveFromServer();
			if(mc.getMcType() == MazeComType.AWAITMOVE){
				// Client ist dran und darf einen Zug schicken 
				taktik.getMove(mc.getAwaitMoveMessage());
			}
			else if(mc.getMcType() == MazeComType.WIN){
				// Spiel vorbei, jemand hat gewonnen
				System.out.println("Und der Gewinner ist.. : " + mc.getWinMessage().getWinner());
				break;
			}
			
		}
		
	}
	
	
	
	
	
}
