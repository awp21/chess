package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves {
    public static Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<>();
        boolean t = true;
        //rook
        for(int x = 1; x <= 8 && t; x++){
            t = ChessPiece.getMovesHelper(board,startPosition,x,0,color, moves);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = ChessPiece.getMovesHelper(board,startPosition,x,0,color, moves);
        }
        t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = ChessPiece.getMovesHelper(board,startPosition,0,x,color, moves);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = ChessPiece.getMovesHelper(board,startPosition,0,x,color, moves);
        }
        //Bishop
        t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = ChessPiece.getMovesHelper(board,startPosition,x,x,color, moves);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = ChessPiece.getMovesHelper(board,startPosition,x,x,color, moves);
        }
        t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = ChessPiece.getMovesHelper(board,startPosition,-x,x,color, moves);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = ChessPiece.getMovesHelper(board,startPosition,-x,x,color, moves);
        }
        return moves;
    }
}
