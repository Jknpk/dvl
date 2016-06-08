package client;

import generated.AwaitMoveMessageType;
import generated.MoveMessageType;

public interface Taktik {

	public MoveMessageType getMove(AwaitMoveMessageType awaitMoveMessage, int ownPlayerId);
}
