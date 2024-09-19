package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves {
    public static Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn()));

        //check bounds ABOVE
        if (startPosition.getRow()+ 1 <= 8){
            //if empty
            piece = board.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()), null));
            }
            if (startPosition.getColumn()+ 1 <= 8){
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1));
                if(piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1), null));
                }
                // if I can take
                else if(piece.getTeamColor() != color){
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1), null));
                }
            }
            if (startPosition.getColumn()- 1 >= 1){
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1));
                if(piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1), null));
                }
                // if I can take
                else if(piece.getTeamColor() != color){
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1), null));
                }
            }
        }

        //check bounds BELOW
        if (startPosition.getRow()- 1 >= 1){
            //if empty
            piece = board.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()), null));
            }
            if (startPosition.getColumn()+ 1 <= 8){
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1));
                if(piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1), null));
                }
                // if I can take
                else if(piece.getTeamColor() != color){
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1), null));
                }
            }
            if (startPosition.getColumn()- 1 >= 1){
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1));
                if(piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1), null));
                }
                // if I can take
                else if(piece.getTeamColor() != color){
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1), null));
                }
            }
        }

        //check SIDES
        if (startPosition.getColumn()+ 1 <= 8){
            //if empty
            piece = board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn()+1));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn()+1), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn()+1), null));
            }
        }
        if (startPosition.getColumn()- 1 >= 1){
            //if empty
            piece = board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn()-1));
            if(piece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn()-1), null));
            }
            // if I can take
            else if(piece.getTeamColor() != color){
                moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn()-1), null));
            }
        }







        return moves;
    }
}
