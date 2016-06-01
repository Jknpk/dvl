/**
 * 
 */
package client;

import java.util.List;

import generated.AwaitMoveMessageType;
import generated.BoardType;
import generated.BoardType.Row;
import generated.CardType;
import generated.MoveMessageType;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.Taktik#getMove(generated.AwaitMoveMessageType)
	 */
	@Override
	public MoveMessageType getMove(AwaitMoveMessageType awaitMoveMessage) {
		// TODO Auto-generated method stub
		board = awaitMoveMessage.getBoard();
		foundTreasures = awaitMoveMessage.getFoundTreasures();
		treasure = awaitMoveMessage.getTreasure();
		treasuresToGo = awaitMoveMessage.getTreasuresToGo();
		MoveMessageType moveMessage = new MoveMessageType();
		System.out.println("Treasure to go: " + treasure.name());
		// find treasure on board
		boolean found = false;
		List<Row> rows = board.getRow();
		for (int i = 0; i < rows.size(); i++) {
			List<CardType> cols = rows.get(i).getCol();
			for (int j = 0; j < cols.size(); j++) {
				if (cols.get(j).getTreasure() != null && cols.get(j).getTreasure().name().equals(treasure.name())) {
					System.out.println("searched card position\n\trow: " + i + " column: " + j);
					found = true;
					break;
				}
			}
			if (found) {
				break;
			}
		}
		return moveMessage;
	}

}
