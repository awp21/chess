package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves {
    public static Collection<ChessMove> getKnightMoves(BoardInfo boardInfo) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece.getMovesHelper(boardInfo,2,-1, moves);
        ChessPiece.getMovesHelper(boardInfo,2,1, moves);
        ChessPiece.getMovesHelper(boardInfo,1,-2, moves);
        ChessPiece.getMovesHelper(boardInfo,1,2, moves);
        ChessPiece.getMovesHelper(boardInfo,-1,-2, moves);
        ChessPiece.getMovesHelper(boardInfo,-1,2, moves);
        ChessPiece.getMovesHelper(boardInfo,-2,-1, moves);
        ChessPiece.getMovesHelper(boardInfo,-2,1, moves);

        return moves;
    }
}
