package client;

import generated.BoardType;
import generated.CardType;
import generated.PositionType;

public class KI implements Runnable{

	

	private CardType shiftCard;
	private BoardType actualBoard;
	private PositionType treasurePosition;
	private PositionType myPosition;
	private BoardGenerator generator;
	
	
	public KI(CardType shiftCard, BoardType actualBoard, PositionType treasurePosition, PositionType myPosition){
		this.shiftCard = shiftCard;
		this.actualBoard = actualBoard;
		this.treasurePosition = treasurePosition;
		this.myPosition = myPosition;
		
		generator = new BoardGenerator();
	}
	
	
	
	private boolean isSave(PositionType position){
		if(position.getCol() % 2 == 0 && position.getRow() % 2 == 0){
			return true;
		}
		
		return false;
		
		
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
