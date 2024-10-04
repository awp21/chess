package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves {
    public static Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<>();

        ChessPiece.getMovesHelper(board,startPosition,2,-1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,2,1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,1,-2, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,1,2, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-1,-2, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-1,2, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-2,-1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-2,1, color, moves);

        return moves;
    }
}
