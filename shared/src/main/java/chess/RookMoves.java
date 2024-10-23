package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves {

    public static Collection<ChessMove> getRookMoves(BoardInfo boardInfo) {
        Collection<ChessMove> moves = new ArrayList<>();
        boolean t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = ChessPiece.getMovesHelper(boardInfo,x,0, moves);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = ChessPiece.getMovesHelper(boardInfo,x,0, moves);
        }
        t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = ChessPiece.getMovesHelper(boardInfo,0,x, moves);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = ChessPiece.getMovesHelper(boardInfo,0,x, moves);
        }
        return moves;
    }
}
