package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves {

    public static Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color){
        ArrayList<ChessMove> moves = new ArrayList<>();

        boolean t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = getMovesHelper(board, startPosition, x, 0, moves, color);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = getMovesHelper(board, startPosition, x, 0, moves, color);
        }
        t = true;
        for(int x = 1; x <= 8 && t; x++){
            t = getMovesHelper(board, startPosition, 0, x, moves, color);
        }
        t = true;
        for(int x = -1; x >= -8 && t; x--){
            t = getMovesHelper(board, startPosition, 0, x, moves, color);
        }



        return moves;
    }

    public static boolean getMovesHelper(ChessBoard board, ChessPosition startPosition, int rowOff, int colOff, Collection<ChessMove> moves, ChessGame.TeamColor color){
        ChessPosition endingPosition = new ChessPosition(startPosition.getRow()+rowOff, startPosition.getColumn()+colOff);
        int r = endingPosition.getRow();
        int c = endingPosition.getColumn();
        if(r>=1 && r<=8 && c>=1 && c<=8){
            ChessPiece piece = board.getPiece(endingPosition);
            if (piece == null){
                moves.add(new ChessMove(startPosition,endingPosition,null));
                return true;
            }
            if (piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition,endingPosition,null));
                return false;
            }
        }
        return false;
    }
}
