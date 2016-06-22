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
		// TODO Auto-generated method stub

	}

}
