package client;

import generated.CardType;
import generated.PositionType;

public class BestCardPosition {

	private PositionType shiftPosition;
	private PositionType newMyPosition;
	private CardType shiftCard;
	private MoveRating mr;
	
	public BestCardPosition(PositionType shiftPosition, PositionType newMyPosition,CardType shiftCard, MoveRating mr){
		this.shiftPosition = shiftPosition;
		this.newMyPosition = newMyPosition;
		this.shiftCard = shiftCard;
		this.mr = mr;
		
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

	public MoveRating getMr() {
		return mr;
	}
	
	
	
	
}
