package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves {
    public static Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color){
        ArrayList<ChessMove> moves = new ArrayList<>();

        boolean t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = RookMoves.getMovesHelper(board, startPosition, x, x, moves, color);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = RookMoves.getMovesHelper(board, startPosition, x, x, moves, color);
        }
        t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = RookMoves.getMovesHelper(board, startPosition, -x, x, moves, color);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = RookMoves.getMovesHelper(board, startPosition, -x, x, moves, color);
        }


        return moves;
    }











}
