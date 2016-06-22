package client;

import java.util.ArrayList;
import java.util.List;

import generated.BoardType;
import generated.CardType;
import generated.PositionType;
import generated.TreasureType;
import generated.CardType.Openings;

public class KI implements Runnable {

	private CardType shiftCard;
	private BoardType actualBoard;
	private PositionType treasurePosition;
	private PositionType myPosition;
	private BoardGenerator generator;
	private BoardType newBoard = null;
	private TreasureType actualTreasure;

	public KI(CardType shiftCard, BoardType actualBoard, PositionType treasurePosition, PositionType myPosition, TreasureType actualTreasure) {
		this.shiftCard = shiftCard;
		this.actualBoard = actualBoard;
		this.treasurePosition = treasurePosition;
		this.myPosition = myPosition;
		this.actualTreasure = actualTreasure;
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
		// verändert nur die position wenn man auf eine feste Position kann
		if (isSave(toRate)) {
			// check if trasurePosition is on a save place
			if (isSave(treasurePosition)) {
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
				// in der mitte von 2 festen
				if (treasurePosition.getRow() % 2 != 0 && treasurePosition.getCol() % 2 != 0) {
					if (checkMiddle(toRate, treasurePosition) != null) {
						return checkMiddle(toRate, treasurePosition);
					}
				}
				// links und rechts sind fest
				if (treasurePosition.getRow() % 2 != 0 && treasurePosition.getCol() % 2 == 0) {
					// TODO
					if (checkLeftRight(toRate, treasurePosition) != null) {
						return checkLeftRight(toRate, treasurePosition);
					}
				}
				// oben und unten sind fest
				if (treasurePosition.getRow() % 2 == 0 && treasurePosition.getCol() % 2 != 0) {
					if (checkTopBottem(toRate, treasurePosition) != null) {
						checkTopBottem(toRate, treasurePosition);
					}
				}
			}
		}
		return MoveRating.STAY;
	}

	private MoveRating checkTopBottem(PositionType toRate, PositionType treasurePosition) {
		int trasureCol = treasurePosition.getCol();
		int treasureRow = treasurePosition.getRow();
		int toRateCol = toRate.getCol();
		int toRateRow = toRate.getRow();
		if (treasureRow == toRateRow && trasureCol - 1 == toRateCol
				|| treasureRow == toRateRow && trasureCol + 1 == toRateCol) {
			return MoveRating.SAVE_1;
		}
		if (treasureRow - 2 == toRateRow && trasureCol - 1 == toRateCol
				|| treasureRow - 2 == toRateRow && trasureCol + 1 == toRateCol
				|| treasureRow + 2 == toRateRow && trasureCol - 1 == toRateCol
				|| treasureRow + 2 == toRateRow && trasureCol + 1 == toRateCol
				|| treasureRow == toRateRow && trasureCol + 3 == toRateCol
				|| treasureRow == toRateRow && trasureCol + 3 == toRateCol) {
			return MoveRating.SAVE_2;
		}
		if (treasureRow - 2 == toRateRow && trasureCol - 3 == toRateCol
				|| treasureRow - 2 == toRateRow && trasureCol + 3 == toRateCol
				|| treasureRow + 2 == toRateRow && trasureCol - 3 == toRateCol
				|| treasureRow + 2 == toRateRow && trasureCol + 3 == toRateCol) {
			return MoveRating.SAVE_3;
		}
		return null;
	}

	private MoveRating checkLeftRight(PositionType toRate, PositionType treasurePosition) {
		int trasureCol = treasurePosition.getCol();
		int treasureRow = treasurePosition.getRow();
		int toRateCol = toRate.getCol();
		int toRateRow = toRate.getRow();
		if ((treasureRow - 1 == toRateRow || treasureRow + 1 == toRateRow) && trasureCol == toRateCol) {
			return MoveRating.SAVE_1;
		}
		if ((trasureCol + 3 == toRateCol || trasureCol - 3 == toRateCol) && trasureCol == toRateCol
				|| trasureCol + 2 == toRateCol && treasureRow - 1 == toRateRow
				|| trasureCol + 2 == toRateCol && treasureRow + 1 == toRateRow
				|| trasureCol - 2 == toRateCol && treasureRow - 1 == toRateRow
				|| trasureCol - 2 == toRateCol && treasureRow + 1 == toRateRow) {
			return MoveRating.SAVE_2;
		}
		if (treasureRow + 3 == toRateRow && trasureCol + 2 == toRateCol
				|| treasureRow - 3 == toRateRow && trasureCol + 2 == toRateCol
				|| treasureRow + 3 == toRateRow && trasureCol - 2 == toRateCol
				|| treasureRow - 3 == toRateRow && trasureCol - 2 == toRateCol) {
			return MoveRating.SAVE_3;
		}
		return null;

	}

	private MoveRating checkMiddle(PositionType toRate, PositionType treasurePosition) {
		int trasureCol = treasurePosition.getCol();
		int treasureRow = treasurePosition.getRow();
		int toRateCol = toRate.getCol();
		int toRateRow = toRate.getRow();
		if (trasureCol - 1 == toRateCol && treasureRow - 1 == toRateRow
				|| trasureCol + 1 == toRateCol && treasureRow - 1 == toRateRow
				|| trasureCol - 1 == toRateCol && treasureRow + 1 == toRateRow
				|| trasureCol + 1 == toRateCol && treasureRow + 1 == toRateRow) {
			return MoveRating.SAVE_1;
		}
		if (trasureCol - 3 == toRateCol && treasureRow - 1 == toRateRow
				|| trasureCol - 3 == toRateCol && treasureRow + 1 == toRateRow
				|| trasureCol + 3 == toRateCol && treasureRow - 1 == toRateRow
				|| trasureCol + 3 == toRateCol && treasureRow + 1 == toRateRow

				|| trasureCol - 1 == toRateCol && treasureRow - 3 == toRateRow
				|| trasureCol - 1 == toRateCol && treasureRow + 3 == toRateRow
				|| trasureCol + 1 == toRateCol && treasureRow - 3 == toRateRow
				|| trasureCol + 1 == toRateCol && treasureRow + 3 == toRateRow) {
			return MoveRating.SAVE_2;
		}
		if (trasureCol - 2 == toRateCol && treasureRow - 2 == toRateRow
				|| trasureCol + 2 == toRateCol && treasureRow - 2 == toRateRow
				|| trasureCol - 2 == toRateCol && treasureRow + 2 == toRateRow
				|| trasureCol + 2 == toRateCol && treasureRow + 2 == toRateRow) {
			return MoveRating.SAVE_3;
		}
		return null;
	}

	private boolean checkSave5(PositionType toRate, PositionType treasurePosition) {
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

	private boolean checkSave4(PositionType toRate, PositionType treasurePosition) {
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

	private boolean checkSave1(PositionType toRate, PositionType treasurePosition) {
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
							rateMove(singlePositiontoRate, generator.findTreasure(actualTreasure, newBoard));
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
		// TODO check ob die andere karte f��r uns offen ist!
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
