package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves {
    public static Collection<ChessMove> getQueenMoves(BoardInfo boardInfo) {
        Collection<ChessMove> movesBishop = BishopMoves.getBishopMoves(boardInfo);
        Collection<ChessMove> movesRook = RookMoves.getRookMoves(boardInfo);
        movesBishop.addAll(movesRook);
        return movesBishop;
    }
}
