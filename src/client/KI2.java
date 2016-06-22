package client;

import java.util.ArrayList;
import java.util.List;

import generated.BoardType;
import generated.CardType;
import generated.PositionType;
import generated.CardType.Openings;

public class KI2 implements Runnable {

	private CardType shiftCard;
	private BoardType actualBoard;
	private PositionType treasurePosition;
	private PositionType myPosition;
	private BoardGenerator generator;
	private BoardType newBoard = null;
	
	public KI2(CardType shiftCard, BoardType actualBoard, PositionType treasurePosition, PositionType myPosition) {
		this.shiftCard = shiftCard;
		this.actualBoard = actualBoard;
		this.treasurePosition = treasurePosition;
		this.myPosition = myPosition;

		generator = new BoardGenerator();
	}

	private boolean isSave(PositionType position) {
		if (position.getCol() % 2 == 0 && position.getRow() % 2 == 0) {
			return true;
		}

		return false;

	}

	private MoveRating rateMove(PositionType toRate, PositionType treasurePosition) {
		// if toRate is safe

		if (toRate.getCol() == treasurePosition.getCol() && toRate.getRow() == treasurePosition.getRow()) {
			return MoveRating.HIT;
		}
		if (isSave(toRate)) {
			// check if trasurePosition is on a save place
			if (isSave(treasurePosition)) {
				// max 8 positions with SAVE1
				// check left and rigth
				if (checkSave1(toRate, treasurePosition)) {
					return MoveRating.SAVE_1;
				} else if (checkSave2(toRate, treasurePosition)) {
					return MoveRating.SAVE_2;
				} else if (checkSave3(toRate, treasurePosition)) {
					return MoveRating.SAVE_3;
				} else if (checkSave4(toRate, treasurePosition)) {
					return MoveRating.SAVE_4;
				} else if (checkSave5(toRate, treasurePosition)) {
					return MoveRating.SAVE_5;
				}
			} else {
				// TODO
			}
		}
		return MoveRating.STAY;
	}

	private boolean checkSave5(PositionType toRate, PositionType treasurePosition2) {
		if ((treasurePosition.getRow() == toRate.getRow() - 4 && treasurePosition.getCol() == toRate.getCol() + 4)
				|| (treasurePosition.getRow() == toRate.getRow() - 4
						&& treasurePosition.getCol() == toRate.getCol() - 4)
				|| (treasurePosition.getRow() == toRate.getRow() + 4
						&& treasurePosition.getCol() == toRate.getCol() + 4)
				|| (treasurePosition.getRow() == toRate.getRow() + 4
						&& treasurePosition.getCol() == toRate.getCol() - 4)) {
			return true;
		}
		return false;
	}

	private boolean checkSave4(PositionType toRate, PositionType treasurePosition2) {
		if ((treasurePosition.getRow() == toRate.getRow() - 4 && treasurePosition.getCol() == toRate.getCol() - 2)
				|| (treasurePosition.getRow() == toRate.getRow() - 4
						&& treasurePosition.getCol() == toRate.getCol() + 2)
				|| (treasurePosition.getRow() == toRate.getRow() + 4
						&& treasurePosition.getCol() == toRate.getCol() - 2)
				|| (treasurePosition.getRow() == toRate.getRow() + 4
						&& treasurePosition.getCol() == toRate.getCol() + 2)) {
			return true;
		}
		return false;
	}

	private boolean checkSave3(PositionType toRate, PositionType treasurePosition2) {
		if (treasurePosition.getRow() == toRate.getRow() - 4 || treasurePosition.getRow() == toRate.getRow() + 4
				|| treasurePosition.getCol() == toRate.getCol() - 4
				|| treasurePosition.getCol() == toRate.getCol() + 4) {
			return true;
		}
		return false;
	}

	private boolean checkSave2(PositionType toRate, PositionType treasurePosition2) {
		if ((treasurePosition.getRow() == toRate.getRow() - 2 && treasurePosition.getCol() == toRate.getCol() - 2)
				|| (treasurePosition.getRow() == toRate.getRow() - 2
						&& treasurePosition.getCol() == toRate.getCol() + 2)
				|| (treasurePosition.getRow() == toRate.getRow() + 2
						&& treasurePosition.getCol() == toRate.getCol() - 2)
				|| (treasurePosition.getRow() == toRate.getRow() + 2
						&& treasurePosition.getCol() == toRate.getCol() + 2)) {
			return true;
		}
		return false;

		// max 7
		// if (treasurePosition.getRow() == toRate.getRow() - 4 ||
		// treasurePosition.getRow() == toRate.getRow() + 4
		// || treasurePosition.getCol() == toRate.getCol() - 4
		// || treasurePosition.getCol() == toRate.getCol() + 4) {
		// return true;
		// // check right left corners
		// } // TODO

	}

	private boolean checkSave1(PositionType toRate, PositionType treasurePosition2) {
		if (treasurePosition.getRow() == toRate.getRow() - 2 || treasurePosition.getRow() == toRate.getRow() + 2
				|| treasurePosition.getCol() == toRate.getCol() - 2
				|| treasurePosition.getCol() == toRate.getCol() + 2) {
			return true;
			// check right left corners
		}
		// check2
		// else if ((treasurePosition.getRow() == toRate.getRow() - 2
		// && treasurePosition.getCol() == toRate.getCol() - 2)
		// || (treasurePosition.getRow() == toRate.getRow() - 2
		// && treasurePosition.getCol() == toRate.getCol() + 2)
		// || (treasurePosition.getRow() == toRate.getRow() + 2
		// && treasurePosition.getCol() == toRate.getCol() - 2)
		// || (treasurePosition.getRow() == toRate.getRow() + 2
		// && treasurePosition.getCol() == toRate.getCol() + 2)) {
		// return true;
		// }
		return false;
	}

	@Override
	public void run() {
		int[] aussen = {0,6};
		int[] innen = {1,3,5};
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				for(int k = 0; k < 3; k++){
					
					PositionType shiftPosition = new PositionType();
					if(i == 0){
						shiftPosition.setRow(aussen[j]);
						shiftPosition.setCol(innen[k]);
					}else{
						shiftPosition.setRow(innen[k]);
						shiftPosition.setCol(aussen[j]);
						
					}

					if(!actualBoard.getForbidden().equals(shiftPosition)){
						// Jetzt gehts ab!
						newBoard = generator.proceedShift(actualBoard, shiftPosition, shiftCard);
						
						// list with all possible moves
						List<PositionType> positionsToGo = possibleMoves();
						for(PositionType singlePositiontoRate:positionsToGo){
							rateMove(singlePositiontoRate, treasurePosition);
						}
						
						
						
						
					}
					
				}
			}
		}

	}
	
	private List<PositionType> possibleMoves() {
		List<PositionType> ret = new ArrayList<>();
		// ret.add(myPosition);
		PositionType tmp = new PositionType();
		tmp.setCol(myPosition.getCol());
		tmp.setRow(myPosition.getRow());
		// TODO go throuhg all possible cards and add the position to ret
		findPositions(tmp, ret);
		return ret;
	}
	
	private void findPositions(PositionType tmp, List<PositionType> ret) {
		CardType cardType = newBoard.getRow().get(tmp.getRow()).getCol().get(tmp.getCol());
		Openings openingsMyPosition = cardType.getOpenings();
		ret.add(tmp);
		System.out.println("can go to " + tmp.getRow() + "\t" + tmp.getCol());
		// TODO check ob die andere karte für uns offen ist!
		if (openingsMyPosition.isBottom()) {
			bottem(tmp, ret);
		}
		if (openingsMyPosition.isLeft()) {
			left(tmp, ret);
		}
		if (openingsMyPosition.isRight()) {
			rigth(tmp, ret);
		}
		if (openingsMyPosition.isTop()) {
			top(tmp, ret);

		}
	}
	
	private void top(PositionType tmp, List<PositionType> ret) {
		if (tmp.getRow() == 0) {
			return;
		} else {
			if (newBoard.getRow().get(tmp.getRow() - 1).getCol().get(tmp.getCol()).getOpenings().isBottom()) {
				PositionType newPos = new PositionType();
				newPos.setCol(tmp.getCol());
				newPos.setRow(tmp.getRow() - 1);
				if (!positionInRet(newPos, ret)) {
					findPositions(newPos, ret);
				}
			}
		}
	}

	private void rigth(PositionType tmp, List<PositionType> ret) {
		if (tmp.getCol() == 6) {
			return;
		} else {
			if (newBoard.getRow().get(tmp.getRow()).getCol().get(tmp.getCol() + 1).getOpenings().isLeft()) {
				PositionType newPos = new PositionType();
				newPos.setCol(tmp.getCol() + 1);
				newPos.setRow(tmp.getRow());
				if (!positionInRet(newPos, ret)) {
					findPositions(newPos, ret);
				}
			}
		}
	}

	private void left(PositionType tmp, List<PositionType> ret) {
		if (tmp.getCol() == 0) {
			return;
		} else {
			if (newBoard.getRow().get(tmp.getRow()).getCol().get(tmp.getCol() - 1).getOpenings().isRight()) {
				PositionType newPos = new PositionType();
				newPos.setCol(tmp.getCol() - 1);
				newPos.setRow(tmp.getRow());
				if (!positionInRet(newPos, ret)) {
					findPositions(newPos, ret);
				}
			}
		}
	}

	private boolean positionInRet(PositionType newPos, List<PositionType> ret) {
		for (PositionType positionType : ret) {
			if (newPos.getRow() == positionType.getRow() && newPos.getCol() == positionType.getCol()) {
				return true;
			}
		}
		return false;
	}

	private void bottem(PositionType tmp, List<PositionType> ret) {
		if (tmp.getRow() == 6) {
			return;
		} else if (newBoard.getRow().get(tmp.getRow() + 1).getCol().get(tmp.getCol()).getOpenings().isTop()) {
			ret.add(tmp);
			PositionType newPos = new PositionType();
			newPos.setCol(tmp.getCol());
			newPos.setRow(tmp.getRow() + 1);
			if (!positionInRet(newPos, ret)) {
				findPositions(newPos, ret);
			}
		}
	}

}
