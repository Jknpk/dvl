/**
 * 
 */
package client;

import java.util.ArrayList;
import java.util.List;

import generated.BoardType;
import generated.CardType;
import generated.CardType.Openings;
import generated.CardType.Pin;
import generated.PositionType;
import generated.TreasureType;
import server.Card;
import server.Messages;
import server.Position;
import tools.Debug;
import tools.DebugLevel;

/**
 * @author oliver
 *
 */
public class BoardGenerator extends BoardType {


	public BoardGenerator() {
	}

	// Fuehrt nur das Hereinschieben der Karte aus!!!
	public BoardType proceedShift(BoardType board, PositionType shiftPosition, CardType shiftCard) {
		Debug.print(Messages.getString("Board.proceedShiftFkt"), DebugLevel.DEBUG); //$NON-NLS-1$
		Position sm = new Position(shiftPosition);
		if (sm.getCol() % 6 == 0) { // Col=6 oder 0
			if (sm.getRow() % 2 == 1) {
				// horizontal schieben
				int row = sm.getRow();
				int start = (sm.getCol() + 6) % 12; // Karte die rausgenommen
													// wird
				setShiftCard(getCard(row, start, board));

				if (start == 6) {
					for (int i = 6; i > 0; --i) {
						setCard(row, i, new Card(getCard(row, i - 1, board)),board);
					}
				} else {// Start==0
					for (int i = 0; i < 6; ++i) {
						setCard(row, i, new Card(getCard(row, i + 1, board)),board);
					}
				}
			}
		} else if (sm.getRow() % 6 == 0) {
			if (sm.getCol() % 2 == 1) {
				// vertikal schieben
				int col = sm.getCol();
				int start = (sm.getRow() + 6) % 12; // Karte die rausgenommen
													// wird
				setShiftCard(getCard(start, col, board));
				if (start == 6) {
					for (int i = 6; i > 0; --i) {
						setCard(i, col, new Card(getCard(i - 1, col, board)), board);
					}
				} else {// Start==0
					for (int i = 0; i < 6; ++i) {
						setCard(i, col, new Card(getCard(i + 1, col, board)), board);
					}
				}

			}
		}
		forbidden = sm.getOpposite(); //TODO: muss das hier raus?
		Card c = null;
		c = new Card(shiftCard);
		// Wenn Spielfigur auf neuer shiftcard steht,
		// muss dieser wieder aufs Brett gesetzt werden
		// Dazu wird Sie auf die gerade hereingeschoben
		// Karte gesetzt
		if (!shiftCard.getPin().getPlayerID().isEmpty()) {
			// Figur zwischenspeichern
			Pin temp = shiftCard.getPin();
			// Figur auf SchiebeKarte l��schen
			shiftCard.setPin(new Pin());
			// Zwischengespeicherte Figut auf
			// neuer Karte plazieren
			c.setPin(temp);
		}
		setCard(sm.getRow(), sm.getCol(), c, board);
		return board;
	}

	public void setCard(int row, int col, Card c, BoardType board) {
		// Muss ueberschrieben werden, daher zuerst entfernen und dann...
		board.getRow().get(row).getCol().remove(col);
		// ...hinzufuegen
		board.getRow().get(row).getCol().add(col, c);
	}

	public CardType getCard(int row, int col, BoardType board) {
		return board.getRow().get(row).getCol().get(col);
	}

	public PositionType findPlayer(Integer PlayerID, BoardType board) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				Pin pinsOnCard = getCard(i, j, board).getPin();
				for (Integer pin : pinsOnCard.getPlayerID()) {
					if (pin == PlayerID) {
						PositionType pos = new PositionType();
						pos.setCol(j);
						pos.setRow(i);
						return pos;
					}
				}
			}
		}
		// Pin nicht gefunden.
		// XXX: Darf eigentlich nicht vorkommen
		return null;
	}

	public PositionType findTreasure(TreasureType treasureType, BoardType board) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				TreasureType treasure = getCard(i, j, board).getTreasure();
				if (treasure == treasureType) {
					PositionType pos = new PositionType();
					pos.setCol(j);
					pos.setRow(i);
					return pos;
				}
			}
		}
		// Schatz nicht gefunden, kann nur bedeuten, dass Schatz sich auf
		// Schiebekarte befindet
		return null;
	}

}
