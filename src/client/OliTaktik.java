/**
 * 
 */
package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import generated.AwaitMoveMessageType;
import generated.BoardType;
import generated.CardType;
import generated.CardType.Openings;
import generated.MoveMessageType;
import generated.PositionType;
import generated.TreasureType;
import generated.TreasuresToGoType;

/**
 * @author oliver
 *
 */
public class OliTaktik implements Taktik {

	private BoardType board;
	private List<TreasureType> foundTreasures;
	private TreasureType treasure;
	private List<TreasuresToGoType> treasuresToGo;
	private PositionType myPosition;
	private int ownPlayerId;
	private CardType shiftCard;
	private PositionType treasurePosition;

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.Taktik#getMove(generated.AwaitMoveMessageType)
	 */
	@Override
	public MoveMessageType getMove(AwaitMoveMessageType awaitMoveMessage, int ownPlayerId) {
		board = awaitMoveMessage.getBoard();
		foundTreasures = awaitMoveMessage.getFoundTreasures();
		treasure = awaitMoveMessage.getTreasure();
		treasuresToGo = awaitMoveMessage.getTreasuresToGo();
		shiftCard = awaitMoveMessage.getBoard().getShiftCard();
		this.ownPlayerId = ownPlayerId;
		myPosition = new PositionType();
		treasurePosition = new PositionType();

		// final ExecutorService service;
		// final Future<BestCardPosition> task1;
		// final Future<BestCardPosition> task2;
		// final Future<BestCardPosition> task3;
		// final Future<BestCardPosition> task4;
		//
		// service = Executors.newFixedThreadPool(4);
		//
		// Card cardShiftCard = new Card(shiftCard);
		// List<Card> shiftCards = cardShiftCard.getPossibleRotations();
		// System.out.println("Laenge von shiftCards " + shiftCards.size());
		// task1 = service.submit(new KI(shiftCards.get(0), board,
		// treasurePosition, myPosition, treasure));
		// task2 = service.submit(new KI(shiftCards.get(1), board,
		// treasurePosition, myPosition, treasure));
		// task3 = service.submit(new KI(shiftCards.get(2), board,
		// treasurePosition, myPosition, treasure));
		// task4 = service.submit(new KI(shiftCards.get(3), board,
		// treasurePosition, myPosition, treasure));
		// BestCardPosition[] bcps = new BestCardPosition[4];
		// try {
		// bcps[0] = task1.get();
		// bcps[1] = task2.get();
		// bcps[2] = task3.get();
		// bcps[3] = task4.get();
		// } catch (InterruptedException | ExecutionException e) {
		// e.printStackTrace();
		// System.out.println("Dein Schei� klappt nicht!");
		// }
		// service.shutdown();
		// for (int i = 0; i < 4; i++) {
		// System.out.println(bcps[i] + " " + bcps[i].getMr().getValue());
		// }
		//
		// int best = 0;
		// for (int i = 0; i < 3; i++) {
		// if (bcps[i].getMr().getValue() < bcps[i + 1].getMr().getValue()) {
		// best = i + 1;
		// }
		// }

		BoardGenerator generator = new BoardGenerator();

		// shift board to get acutely board after shifting
		PositionType input = createRandomPositionForShiftedCard();

		board = generator.proceedShift(board, input, shiftCard);
		myPosition = generator.findPlayer(ownPlayerId, board);
		treasurePosition = generator.findTreasure(treasure, board);

		// list with all possible moves
		MoveMessageType moveMessage = new MoveMessageType();
		List<PositionType> positionsToGo = possibleMoves();
		moveMessage.setShiftCard(shiftCard);
		moveMessage.setShiftPosition(input);
		moveMessage.setNewPinPos(myPosition);
		MoveRating tmp = MoveRating.STAY;
		for (PositionType positionType : positionsToGo) {
			MoveRating rateMove = rateMove(positionType, treasurePosition);
			if (rateMove.getValue() > tmp.getValue()) {
				moveMessage.setNewPinPos(positionType);
				tmp = rateMove;
			}
		}

		return moveMessage;
	}

	private ArrayList<PositionType> listeAllShiftposition() {
		ArrayList<PositionType> ret = new ArrayList<>();
		PositionType a = new PositionType();
		a.setRow(1);
		a.setCol(0);
		PositionType b = new PositionType();
		b.setRow(3);
		b.setCol(0);
		PositionType c = new PositionType();
		c.setRow(5);
		c.setCol(0);
		PositionType d = new PositionType();
		d.setRow(1);
		d.setCol(6);
		PositionType e = new PositionType();
		e.setRow(3);
		e.setCol(6);
		PositionType f = new PositionType();
		f.setRow(5);
		f.setCol(6);

		PositionType g = new PositionType();
		g.setRow(0);
		g.setCol(1);
		PositionType h = new PositionType();
		h.setRow(0);
		h.setCol(3);
		PositionType i = new PositionType();
		i.setRow(0);
		i.setCol(5);
		PositionType j = new PositionType();
		j.setRow(0);
		j.setCol(1);
		PositionType k = new PositionType();
		k.setRow(0);
		k.setCol(3);
		PositionType l = new PositionType();
		l.setRow(0);
		l.setCol(5);
		ret.add(a);
		ret.add(b);
		ret.add(c);
		ret.add(d);
		ret.add(e);
		ret.add(f);
		ret.add(g);
		ret.add(h);
		ret.add(i);
		ret.add(j);
		ret.add(k);
		ret.add(l);
		return ret;
	}

	private PositionType createRandomPositionForShiftedCard() {
		PositionType ret = new PositionType();
		int OBEN = 0;
		int LINKS = 1;
		int RECHTS = 2;
		int UNTEN = 3;
		Random randomStelle = new Random();
		int[] possilble = { 1, 3, 5 };
		Random randomPosition = new Random();
		int position = randomPosition.nextInt(3);
		int stelle = randomStelle.nextInt(4);
		if (stelle == OBEN) {
			ret.setRow(0);
			ret.setCol(possilble[position]);
		} else if (stelle == UNTEN) {
			ret.setRow(6);
			ret.setCol(possilble[position]);
		} else if (stelle == LINKS) {
			ret.setCol(0);
			ret.setRow(possilble[position]);
		} else if (stelle == RECHTS) {
			ret.setCol(6);
			ret.setRow(possilble[position]);
		}
		if (board.getForbidden() == null) {
			return ret;
		}
		if (!equalsPositionTypes(ret, board.getForbidden())) {
			return ret;
		}
		return createRandomPositionForShiftedCard();
	}

	private List<PositionType> possibleMoves() {
		List<PositionType> ret = new ArrayList<>();
		// ret.add(myPosition);
		PositionType tmp = new PositionType();
		tmp.setCol(myPosition.getCol());
		tmp.setRow(myPosition.getRow());
		findPositions(tmp, ret);
		return ret;
	}

	private void findPositions(PositionType tmp, List<PositionType> ret) {
		CardType cardType = board.getRow().get(tmp.getRow()).getCol().get(tmp.getCol());
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
			if (board.getRow().get(tmp.getRow() - 1).getCol().get(tmp.getCol()).getOpenings().isBottom()) {
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
			if (board.getRow().get(tmp.getRow()).getCol().get(tmp.getCol() + 1).getOpenings().isLeft()) {
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
			if (board.getRow().get(tmp.getRow()).getCol().get(tmp.getCol() - 1).getOpenings().isRight()) {
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
		} else if (board.getRow().get(tmp.getRow() + 1).getCol().get(tmp.getCol()).getOpenings().isTop()) {
			ret.add(tmp);
			PositionType newPos = new PositionType();
			newPos.setCol(tmp.getCol());
			newPos.setRow(tmp.getRow() + 1);
			if (!positionInRet(newPos, ret)) {
				findPositions(newPos, ret);
			}
		}
	}

	private boolean equalsPositionTypes(PositionType a, PositionType b) {
		return a.getCol() == b.getCol() && a.getRow() == b.getRow();
	}

	private MoveRating rateMove(PositionType toRate, PositionType treasurePosition) {
		// if toRate is safe

		if (treasurePosition == null) {
			return MoveRating.STAY;
		}
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
		if (treasurePosition.getRow() == toRate.getRow() - 2 && treasurePosition.getCol() == toRate.getCol()
				|| treasurePosition.getRow() == toRate.getRow() + 2 && treasurePosition.getCol() == toRate.getCol()
				|| treasurePosition.getCol() == toRate.getCol() - 2 && treasurePosition.getRow() == toRate.getRow()
				|| treasurePosition.getCol() == toRate.getCol() + 2 && treasurePosition.getRow() == toRate.getRow()) {
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

	private boolean isSave(PositionType position) {
		if (position.getCol() % 2 == 0 && position.getRow() % 2 == 0) {
			return true;
		}

		return false;

	}

}
