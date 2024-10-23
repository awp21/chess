package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves {
    public static Collection<ChessMove> getKingMoves(BoardInfo boardInfo) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece.getMovesHelper(boardInfo,1,-1, moves);
        ChessPiece.getMovesHelper(boardInfo,1,0,  moves);
        ChessPiece.getMovesHelper(boardInfo,1,1,  moves);
        ChessPiece.getMovesHelper(boardInfo,0,-1, moves);
        ChessPiece.getMovesHelper(boardInfo,0,1,  moves);
        ChessPiece.getMovesHelper(boardInfo,-1,-1, moves);
        ChessPiece.getMovesHelper(boardInfo,-1,0, moves);
        ChessPiece.getMovesHelper(boardInfo,-1,1, moves);

        return moves;
    }

}
