package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves {
    public static Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color){
        ArrayList<ChessMove> moves = new ArrayList<>();

        //loopy here to go through all possible moves
        int x = 1;
        // loop through up right
        while(startPosition.getRow()+x <= 8 && startPosition.getColumn()+x <= 8){
            //if empty
            ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow()+x, startPosition.getColumn()+x));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + x, startPosition.getColumn() + x), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + x, startPosition.getColumn() + x), null));
                break;
            }
            else{
                break;
            }
            x++;
        }
        x = 1;
        //down right loopy
        while(startPosition.getRow()-x >= 1 && startPosition.getColumn() + x <= 8){
            //if empty
            ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow()-x, startPosition.getColumn() + x));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - x, startPosition.getColumn() + x), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - x, startPosition.getColumn() + x), null));
                break;
            }
            else{
                break;
            }
            x++;
        }
        x = 1;
        //down left loopy
        while(startPosition.getRow() - x >= 1 && startPosition.getColumn() - x >= 1){
            //if empty
            ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow() - x , startPosition.getColumn() - x));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - x, startPosition.getColumn() - x), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - x , startPosition.getColumn() - x), null));
                break;
            }
            else{
                break;
            }
            x++;
        }
        x = 1;
        //Up left loopy
        while(startPosition.getColumn()-x >= 1 && startPosition.getRow() + x <= 8){
            //if emptx
            ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow() + x, startPosition.getColumn() - x));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + x, startPosition.getColumn() - x), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + x, startPosition.getColumn() - x), null));
                break;
            }
            else{
                break;
            }
            x++;
        }
        return moves;
    }











}
