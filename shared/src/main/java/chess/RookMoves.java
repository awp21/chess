package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves {

    public static Collection<ChessMove> getRookMoves(BoardInfo boardInfo) {
        ChessBoard board = boardInfo.board();
        ChessPosition startPosition = boardInfo.position();
        ChessGame.TeamColor color = boardInfo.color();
        Collection<ChessMove> moves = new ArrayList<>();
        boolean t = true;
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
        return moves;
    }
}
