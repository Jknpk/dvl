/**
 * 
 */
package client;

import generated.BoardType;
import generated.CardType;
import generated.MoveMessageType;
import generated.PositionType;
import generated.CardType.Pin;
import server.Board;
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

	private BoardType board;

	public BoardGenerator(BoardType board) {
		this.board = board;
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
				setShiftCard(getCard(row, start));

				if (start == 6) {
					for (int i = 6; i > 0; --i) {
						setCard(row, i, new Card(getCard(row, i - 1)));
					}
				} else {// Start==0
					for (int i = 0; i < 6; ++i) {
						setCard(row, i, new Card(getCard(row, i + 1)));
					}
				}
			}
		} else if (sm.getRow() % 6 == 0) {
			if (sm.getCol() % 2 == 1) {
				// vertikal schieben
				int col = sm.getCol();
				int start = (sm.getRow() + 6) % 12; // Karte die rausgenommen
													// wird
				setShiftCard(getCard(start, col));
				if (start == 6) {
					for (int i = 6; i > 0; --i) {
						setCard(i, col, new Card(getCard(i - 1, col)));
					}
				} else {// Start==0
					for (int i = 0; i < 6; ++i) {
						setCard(i, col, new Card(getCard(i + 1, col)));
					}
				}

			}
		}
		forbidden = sm.getOpposite();
		Card c = null;
		c = new Card(shiftCard);
		// Wenn Spielfigur auf neuer shiftcard steht,
		// muss dieser wieder aufs Brett gesetzt werden
		// Dazu wird Sie auf die gerade hereingeschoben
		// Karte gesetzt
		if (!shiftCard.getPin().getPlayerID().isEmpty()) {
			// Figur zwischenspeichern
			Pin temp = shiftCard.getPin();
			// Figur auf SchiebeKarte löschen
			shiftCard.setPin(new Pin());
			// Zwischengespeicherte Figut auf
			// neuer Karte plazieren
			c.setPin(temp);
		}
		setCard(sm.getRow(), sm.getCol(), c);
		this.board = board;
		return board;
	}

	public void setCard(int row, int col, Card c) {
		// Muss ueberschrieben werden, daher zuerst entfernen und dann...
		board.getRow().get(row).getCol().remove(col);
		// ...hinzufuegen
		board.getRow().get(row).getCol().add(col, c);
	}

	public CardType getCard(int row, int col) {
		return board.getRow().get(row).getCol().get(col);
	}
	
	public String boardToString() {
		StringBuilder sb = new StringBuilder();
//		sb.append("Board [currentTreasure=" + currentTreasure + "]\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(Messages.getString("Board.Board")); //$NON-NLS-1$
		sb.append(" ------ ------ ------ ------ ------ ------ ------ \n"); //$NON-NLS-1$
		for (int i = 0; i < getRow().size(); i++) {
			StringBuilder line1 = new StringBuilder("|"); //$NON-NLS-1$
			StringBuilder line2 = new StringBuilder("|"); //$NON-NLS-1$
			StringBuilder line3 = new StringBuilder("|"); //$NON-NLS-1$
			StringBuilder line4 = new StringBuilder("|"); //$NON-NLS-1$
			StringBuilder line5 = new StringBuilder("|"); //$NON-NLS-1$
			StringBuilder line6 = new StringBuilder("|"); //$NON-NLS-1$
			for (int j = 0; j < getRow().get(i).getCol().size(); j++) {
				Card c = new Card(getCard(i, j));
				if (c.getOpenings().isTop()) {
					line1.append("##  ##|"); //$NON-NLS-1$
					line2.append("##  ##|"); //$NON-NLS-1$
				} else {
					line1.append("######|"); //$NON-NLS-1$
					line2.append("######|"); //$NON-NLS-1$
				}
				if (c.getOpenings().isLeft()) {
					line3.append("  "); //$NON-NLS-1$
					line4.append("  "); //$NON-NLS-1$
				} else {
					line3.append("##"); //$NON-NLS-1$
					line4.append("##"); //$NON-NLS-1$
				}
				if (c.getPin().getPlayerID().size() != 0) {
					line3.append("S"); //$NON-NLS-1$
				} else {
					line3.append(" "); //$NON-NLS-1$
				}
				if (c.getTreasure() != null) {
					String name = c.getTreasure().name();
					switch (name.charAt(1)) {
					case 'Y':
						// Symbol
						line3.append("T"); //$NON-NLS-1$
						break;
					case 'T':
						// Startpunkt
						line3.append("S"); //$NON-NLS-1$
						break;
					}
					line4.append(name.substring(name.length() - 2));
				} else {
					line3.append(" "); //$NON-NLS-1$
					line4.append("  "); //$NON-NLS-1$
				}
				if (c.getOpenings().isRight()) {
					line3.append("  |"); //$NON-NLS-1$
					line4.append("  |"); //$NON-NLS-1$
				} else {
					line3.append("##|"); //$NON-NLS-1$
					line4.append("##|"); //$NON-NLS-1$
				}
				if (c.getOpenings().isBottom()) {
					line5.append("##  ##|"); //$NON-NLS-1$
					line6.append("##  ##|"); //$NON-NLS-1$
				} else {
					line5.append("######|"); //$NON-NLS-1$
					line6.append("######|"); //$NON-NLS-1$
				}
			}
			sb.append(line1.toString() + "\n"); //$NON-NLS-1$
			sb.append(line2.toString() + "\n"); //$NON-NLS-1$
			sb.append(line3.toString() + "\n"); //$NON-NLS-1$
			sb.append(line4.toString() + "\n"); //$NON-NLS-1$
			sb.append(line5.toString() + "\n"); //$NON-NLS-1$
			sb.append(line6.toString() + "\n"); //$NON-NLS-1$
			sb.append(" ------ ------ ------ ------ ------ ------ ------ \n"); //$NON-NLS-1$
		}

		return sb.toString();
	}


}
