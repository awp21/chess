package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves {
    public static Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        boolean t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = RookMoves.getMovesHelper(board, startPosition, x, 0, moves, color);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = RookMoves.getMovesHelper(board, startPosition, x, 0, moves, color);
        }
        t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = RookMoves.getMovesHelper(board, startPosition, 0, x, moves, color);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = RookMoves.getMovesHelper(board, startPosition, 0, x, moves, color);
        }

        t = true;
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
