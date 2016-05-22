package client;

import generated.AwaitMoveMessageType;
import generated.MoveMessageType;
import generated.*;

public class testTaktik implements Taktik {

	public MoveMessageType getMove(AwaitMoveMessageType awaitMoveMessage) {
		BoardType bt = awaitMoveMessage.getBoard();
		TreasureType tp = awaitMoveMessage.getTreasure();
		awaitMoveMessage.getFoundTreasures();
		awaitMoveMessage.getTreasuresToGo();

		// Zu dem Ding müssen wir hin!
		System.out.println("Zu diesem Schatz müssen wir unbedingt hin: "
				+ tp.name());
		// Hier hat der letzte Fritze seine Karte reingesteckt
//		System.out
//				.println("Verboten die neue Reinschieb-Karte reinzustecken::\nSpalte: "
//						+ bt.getForbidden().getCol()
//						+ "\nZeile: "
//						+ bt.getForbidden().getRow());
		// Hier sind die Öffnungen unserer Karte zu sehen
		String s = "Neue Reinschiebkarte ist offen nach: ";
		if (bt.getShiftCard().getOpenings().isTop())
			s += "oben ";
		if (bt.getShiftCard().getOpenings().isRight())
			s += "rechts ";
		if (bt.getShiftCard().getOpenings().isBottom())
			s += "unten ";
		if (bt.getShiftCard().getOpenings().isLeft())
			s += "links ";
		System.out.println(s);
		s = "Spielfeld: \n";
		for(int i = 0; i < bt.getRow().size(); i++){
			for(int j = 0; j < bt.getRow().size(); j++){
				s += bt.getRow().get(i).getCol().get(j).getTreasure().name() + " ";
			}
			s+= "\n";
		}
		System.out.println(s);
		
		
		PositionType newPinPos = new PositionType();
		newPinPos.setCol(0);
		newPinPos.setRow(0);

		CardType ct = new CardType();
		ct.setOpenings(null);
		ct.setPin(null);
		ct.setTreasure(null);

		PositionType shiftPosition = new PositionType();
		shiftPosition.setCol(0);
		shiftPosition.setRow(0);

		MoveMessageType moveMessageType = new MoveMessageType();
		moveMessageType.setNewPinPos(newPinPos);
		moveMessageType.setShiftCard(ct);
		moveMessageType.setShiftPosition(shiftPosition);

		return moveMessageType;
	}

}
