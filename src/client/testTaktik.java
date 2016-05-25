package client;

import java.util.List;

import generated.AwaitMoveMessageType;
import generated.BoardType;
import generated.CardType;
import generated.MoveMessageType;
import generated.PositionType;
import generated.TreasureType;
import generated.TreasuresToGoType;

public class testTaktik implements Taktik {

	public MoveMessageType getMove(AwaitMoveMessageType awaitMoveMessage) {
		BoardType bt = awaitMoveMessage.getBoard();
		TreasureType tp = awaitMoveMessage.getTreasure();
		List<TreasureType> ltp = awaitMoveMessage.getFoundTreasures();
		List<TreasuresToGoType> lttg = awaitMoveMessage.getTreasuresToGo();
		
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
		if(bt.getShiftCard().getOpenings() != null){
			if (bt.getShiftCard().getOpenings().isTop())
				s += "oben ";
			if (bt.getShiftCard().getOpenings().isRight())
				s += "rechts ";
			if (bt.getShiftCard().getOpenings().isBottom())
				s += "unten ";
			if (bt.getShiftCard().getOpenings().isLeft())
				s += "links ";
			System.out.println(s);
		}
		
		s = "Spielfeld: \n";
		for(int i = 0; i < bt.getRow().size(); i++){
			for(int j = 0; j < bt.getRow().size(); j++){
				if(bt.getRow().get(i).getCol().get(j).getTreasure() != null){
					s += bt.getRow().get(i).getCol().get(j).getTreasure().name() + "\t";
				}
				else{
					s += "\t";
				}
				
			}
			s+= "\n";
		}
		System.out.println(s);
	
		// Gibt die schon gefundenen Schätze aller Spieler aus 
		for(int i = 0; i < ltp.size(); i++){
			System.out.println(i+ ": " + ltp.get(i).name());
		}
		
		
		
		// So viele Schätze müssen die einzelnen Spieler noch finden!
		for(int i = 0; i < lttg.size(); i++){
			System.out.println(i+ ": " + lttg.get(i).getTreasures());
		}
		
		
		
		
		
		
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
