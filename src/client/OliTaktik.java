/**
 * 
 */
package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import generated.AwaitMoveMessageType;
import generated.BoardType;
import generated.BoardType.Row;
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
	private int needRow;
	private int needColumn;
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
		// TODO Auto-generated method stub
		board = awaitMoveMessage.getBoard();
		foundTreasures = awaitMoveMessage.getFoundTreasures();
		treasure = awaitMoveMessage.getTreasure();
		treasuresToGo = awaitMoveMessage.getTreasuresToGo();
		this.ownPlayerId = ownPlayerId;
		myPosition = new PositionType();
		treasurePosition = new PositionType();
		// print treasure to go
		System.out.println("Treasure to go: " + treasure.name() + "\n");

		// print board
		printBoard();

		// print shifted card
		printShiftedCard();

		findMyPinPositionAndTreasurePosition();

		// find treasure and pin on board
		System.out.println("searched card position\n\trow: " + needRow + " column: " + needColumn);
		System.out.println("my position:\t" + myPosition.getRow() + " " + myPosition.getCol());

		// list with all possible moves
		List<PositionType> positionsToGo = possibleMoves();

		// check if i can go direct to treasure position
		for (PositionType positionType : positionsToGo) {
			if (positionType.getCol() == treasurePosition.getCol()
					&& positionType.getRow() == treasurePosition.getRow()) {
				myPosition = treasurePosition;
			}
		}

		MoveMessageType moveMessage = new MoveMessageType();
		PositionType input = createRandomPosition();
		moveMessage.setShiftPosition(input);
		moveMessage.setShiftCard(shiftCard);
		moveMessage.setNewPinPos(myPosition);
		return moveMessage;
	}

	private PositionType createRandomPosition() {
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
		return ret;
	}

	private List<PositionType> possibleMoves() {
		List<PositionType> ret = new ArrayList<>();
		ret.add(myPosition);
		PositionType tmp = new PositionType();
		tmp = myPosition;
		CardType cardType = board.getRow().get(tmp.getRow()).getCol().get(tmp.getCol());
		Openings openingsMyPosition = cardType.getOpenings();
		// TODO go throuhg all possible cards and change the position in ret
		return ret;
	}

	private void printShiftedCard() {
		System.out.println("shifted card: ");
		StringBuilder sb = new StringBuilder();
		shiftCard = board.getShiftCard();
		Openings openings = shiftCard.getOpenings();
		sb.append("\topen to: ");
		if (openings.isBottom()) {
			sb.append("bottem ");
		}
		if (openings.isLeft()) {
			sb.append("left ");
		}
		if (openings.isRight()) {
			sb.append("right ");
		}
		if (openings.isTop()) {
			sb.append("top ");
		}
		sb.append("\n");
		System.out.println(sb.toString());
	}

	private void printBoard() {
		System.out.println("Board: ");
		List<Row> rows = board.getRow();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows.size(); i++) {
			List<CardType> col = rows.get(i).getCol();
			for (int j = 0; j < col.size(); j++) {
				if (col.get(j).getTreasure() != null) {
					sb.append(col.get(j).getTreasure().name() + "   \t");
				} else {
					sb.append("******   \t");
				}
			}
			sb.append("\n");
		}
		sb.append("\n");
		System.out.println(sb.toString());
	}

	private boolean isDirectWay() {
		// TODO Auto-generated method stub
		return false;
	}

	private void findMyPinPositionAndTreasurePosition() {
		List<Row> rows = board.getRow();
		for (int i = 0; i < rows.size(); i++) {
			List<CardType> cols = rows.get(i).getCol();
			for (int j = 0; j < cols.size(); j++) {
				if (cols.get(j).getPin() != null && cols.get(j).getPin().getPlayerID() != null) {
					List<Integer> playerID = cols.get(j).getPin().getPlayerID();
					for (Integer integer : playerID) {
						if (Integer.valueOf(ownPlayerId) == integer) {
							myPosition.setRow(i);
							myPosition.setCol(j);
						}
					}
				}

				if (cols.get(j).getTreasure() != null && cols.get(j).getTreasure().name().equals(treasure.name())) {
					treasurePosition.setRow(i);
					treasurePosition.setCol(j);
					return;
				}
			}
		}
	}

}
