package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves {
    public static Collection<ChessMove> getBishopMoves(BoardInfo boardInfo) {
        ChessBoard board = boardInfo.board();
        ChessPosition startPosition = boardInfo.position();
        ChessGame.TeamColor color = boardInfo.color();
        Collection<ChessMove> moves = new ArrayList<>();
        boolean t = true;
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
