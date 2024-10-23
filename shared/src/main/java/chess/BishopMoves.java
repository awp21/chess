package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves {
    public static Collection<ChessMove> getBishopMoves(BoardInfo boardInfo) {
        Collection<ChessMove> moves = new ArrayList<>();
        boolean t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = ChessPiece.getMovesHelper(boardInfo, x,x, moves);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = ChessPiece.getMovesHelper(boardInfo,x,x, moves);
        }
        t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = ChessPiece.getMovesHelper(boardInfo,-x,x, moves);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = ChessPiece.getMovesHelper(boardInfo,-x,x, moves);
        }
        return moves;
    }











}
