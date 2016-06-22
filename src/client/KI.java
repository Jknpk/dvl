package client;

import generated.BoardType;
import generated.CardType;
import generated.PositionType;

public class KI implements Runnable {

	private CardType shiftCard;
	private BoardType actualBoard;
	private PositionType treasurePosition;
	private PositionType myPosition;
	private BoardGenerator generator;

	public KI(CardType shiftCard, BoardType actualBoard, PositionType treasurePosition, PositionType myPosition) {
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

	private MoveRating checkLeftRight(PositionType toRate, PositionType treasurePosition2) {
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
		// TODO Auto-generated method stub

	}

}
