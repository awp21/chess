package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves {
    public static Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> movesBishop = BishopMoves.getBishopMoves(board, startPosition, color);
        Collection<ChessMove> movesRook = RookMoves.getRookMoves(board, startPosition, color);
        movesBishop.addAll(movesRook);
        return movesBishop;
    }
}
