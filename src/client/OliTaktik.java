/**
 * 
 */
package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
		shiftCard = awaitMoveMessage.getBoard().getShiftCard();
		this.ownPlayerId = ownPlayerId;
		myPosition = new PositionType();
		treasurePosition = new PositionType();
		
		//TODO
		
		Executor executor = Executors.newFixedThreadPool(4);
		CompletionService<BestCardPosition> completionService = new ExecutorCompletionService<BestCardPosition>(executor);

		Card cardShiftCard= new Card(shiftCard);
		List<Card> shiftCards = cardShiftCard.getPossibleRotations();
		
		// Die Karten sehen alle richtig gedreht aus..
		System.out.println("so sehen die 4 ShiftCards aus:");
		for(int i = 0; i < shiftCards.size(); i++){
			System.out.println("Karte " + i +":");
			System.out.println(shiftCards.get(i).toString());
		}
		
		for(int i = 0; i < 4; i++) {
			completionService.submit(new KI(shiftCards.get(i), board, treasurePosition, myPosition, treasure, ownPlayerId));
		}
		
		int received = 0;
		boolean errors = false;
		
		BestCardPosition[] bcps = new BestCardPosition[4];
		while(received < 4) {
		      Future<BestCardPosition> resultFuture = null;
			try {
				resultFuture = completionService.take();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} //blocks if none available
		      try {
		    	  if(resultFuture != null){
		    		  BestCardPosition result = resultFuture.get();
			    	  bcps[received] = result;
			    	  received ++;
		    	  }
		      }
		      catch(Exception e) {
		         System.out.println("Es gab Errors!");
		         e.printStackTrace();
		         errors = true;
		      }
		}
		
		for(int i = 0; i < 4; i++){
			System.out.println(bcps[i] + "  " + bcps[i].getMr().getValue());
		}
		
		int best = 0;
		for(int i = 0; i < 3; i++){
			if(bcps[i].getMr().getValue() < bcps[i+1].getMr().getValue()){
				best = i+1;
			}
		}
		
		
		
		//BoardGenerator generator = new BoardGenerator();
		// print treasure to go
		//System.out.println("Treasure to go: " + treasure.name() + "\n");
		// print board
		//printBoard();

		// print shifted card
		//printShiftedCard();

		// shift board to get acutely board after shifting
		//PositionType input = createRandomPositionForShiftedCard();
		// TODO my set position after shifting

		//System.out.println(input.getRow());
		//System.out.println(input.getCol());
		//board = generator.proceedShift(board, input, shiftCard);
		//printBoard();
		//myPosition = generator.findPlayer(ownPlayerId, board);
		//treasurePosition = generator.findTreasure(treasure, board);

		// findMyPinPositionAndTreasurePosition();

		// find treasure and pin on board
		//System.out.println("searched card position\n\trow: " + treasurePosition.getRow() + " column: " + treasurePosition.getCol());
		//System.out.println("my position:\t" + myPosition.getRow() + " " + myPosition.getCol());

		// list with all possible moves
		//List<PositionType> positionsToGo = possibleMoves();
		//System.err.println(positionsToGo.size());
		// check if i can go direct to treasure position
		//boolean direct = false;
		//for (PositionType positionType : positionsToGo) {
		//	if (equalsPositionTypes(treasurePosition, positionType)) {
		//		myPosition = treasurePosition;
		//		direct = true;
		//		break;
		//	}
		//}
		//if (!direct) {
			// myPosition = positionsToGo.get(positionsToGo.size() - 1);
		//}
		System.out.println("Folgendes Paket wird an den Server geschickt:");
		System.out.println("Shiftposition: (" + bcps[best].getShiftPosition().getRow() + "|" + bcps[best].getShiftPosition().getCol() + ")");
		System.out.println("Shiftcard: " + bcps[best].getShiftCard());
		System.out.println("Neue Pin Position: (" + bcps[best].getNewMyPosition().getRow() + "|" + bcps[best].getNewMyPosition().getCol() + ")");
		
		MoveMessageType moveMessage = new MoveMessageType();
		moveMessage.setShiftPosition(bcps[best].getShiftPosition());
		moveMessage.setShiftCard(bcps[best].getShiftCard());
		moveMessage.setNewPinPos(bcps[best].getNewMyPosition());
		return moveMessage;
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
		// TODO go throuhg all possible cards and add the position to ret
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

	private boolean equalsPositionTypes(PositionType a, PositionType b) {
		return a.getCol() == b.getCol() && a.getRow() == b.getRow();
	}

}
