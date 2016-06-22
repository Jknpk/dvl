package client;

import generated.CardType;
import generated.PositionType;

public class BestCardPosition {

	private PositionType shiftPosition;
	private PositionType newMyPosition;
	private CardType shiftCard;
	
	public BestCardPosition(PositionType shiftPosition, PositionType newMyPosition,CardType shiftCard){
		this.shiftPosition = shiftPosition;
		this.newMyPosition = newMyPosition;
		this.shiftCard = shiftCard;
		
	}

	public PositionType getShiftPosition() {
		return shiftPosition;
	}

	public PositionType getNewMyPosition() {
		return newMyPosition;
	}

	public CardType getShiftCard() {
		return shiftCard;
	}
	
	
	
	
}
