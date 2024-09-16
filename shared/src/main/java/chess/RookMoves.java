package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves {

    public static Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //all pieces right now are rooks

        //loopy here to go through all possible moves
        int x = 1;
        int y = 1;
        // loop through positive x
        while(startPosition.getRow()+x <= 8){
            //if empty
            ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow()+x, startPosition.getColumn()));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + x, startPosition.getColumn()), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + x, startPosition.getColumn()), null));
                break;
            }
            else{
                break;
            }
            x++;
        }
        x = 1;
        //Negative x loopy
        while(startPosition.getRow()-x >= 1){
            //if empty
            ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow()-x, startPosition.getColumn()));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - x, startPosition.getColumn()), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - x, startPosition.getColumn()), null));
                break;
            }
            else{
                break;
            }
            x++;
        }
        //Positive y loopy
        while(startPosition.getColumn()+y <= 8){
            //if empty
            ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn()+y));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() + y), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() + y), null));
                break;
            }
            else{
                break;
            }
            y++;
        }
        y = 1;
        //Negative y loopy
        while(startPosition.getColumn()-y >= 1){
            //if empty
            ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn()-y));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() - y), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() - y), null));
                break;
            }
            else{
                break;
            }
            y++;
        }
        return moves;
    }




}
