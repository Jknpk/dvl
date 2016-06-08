/**
 * 
 */
package client;

import java.util.ArrayList;
import java.util.List;

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
		// print treasure to go
		System.out.println("Treasure to go: " + treasure.name() + "\n");

		// print board
		printBoard();

		// print shifted card
		printShiftedCard();
		
		// TODO print pin position
		
		MoveMessageType moveMessage = new MoveMessageType();
		// find treasure on board
		findTreasurePosition();
		System.out.println("searched card position\n\trow: " + needRow + " column: " + needColumn);
		// is direct way to treasure
		List<PositionType> positionsToGo = possibleMoves();
		
		boolean directWay = isDirectWay();
		if (directWay) {
			PositionType position = new PositionType();
			position.setRow(needRow);
			position.setCol(needColumn);
			moveMessage.setNewPinPos(position);
		}

		return moveMessage;
	}

	private List<PositionType> possibleMoves() {
		List<PositionType> ret = new ArrayList<>();
		
		return ret;
	}

	private void printShiftedCard() {
		System.out.println("shifted card: ");
		StringBuilder sb = new StringBuilder();
		CardType shiftCard = board.getShiftCard();
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
				if ( col.get(j).getPin() != null &&  col.get(j).getPin().getPlayerID() != null) {
					List<Integer> playerID = col.get(j).getPin().getPlayerID();
					for (Integer integer : playerID) {
						if (Integer.valueOf(ownPlayerId) == integer) {
							myPosition.setRow(i);
							myPosition.setCol(j);
							System.out.println("My position:\t" + i + " " + j);
						}
					}
				}
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

	private void findTreasurePosition() {
		List<Row> rows = board.getRow();
		for (int i = 0; i < rows.size(); i++) {
			List<CardType> cols = rows.get(i).getCol();
			for (int j = 0; j < cols.size(); j++) {
				if (cols.get(j).getTreasure() != null && cols.get(j).getTreasure().name().equals(treasure.name())) {
					needRow = i;
					needColumn = j;
					return;
				}
			}
		}
	}

}
