package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves {
    public static Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn()));
        // TOP ROW
        if (startPosition.getRow()+ 2 <= 8){
            if (startPosition.getColumn()+ 1 <= 8){
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow()+2, startPosition.getColumn()+1));
                if(piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+2, startPosition.getColumn()+1), null));
                }
                // if I can take
                else if(piece.getTeamColor() != color){
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+2, startPosition.getColumn()+1), null));
                }
            }
            if (startPosition.getColumn()- 1 >= 1){
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow()+2, startPosition.getColumn()-1));
                if(piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+2, startPosition.getColumn()-1), null));
                }
                // if I can take
                else if(piece.getTeamColor() != color){
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+2, startPosition.getColumn()-1), null));
                }
            }
        }
        //SECOND ROW
        if (startPosition.getRow()+ 1 <= 8){
            if (startPosition.getColumn()+ 2 <= 8){
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+2));
                if(piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+2), null));
                }
                // if I can take
                else if(piece.getTeamColor() != color){
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+2), null));
                }
            }
            if (startPosition.getColumn()- 2 >= 1){
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-2));
                if(piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-2), null));
                }
                // if I can take
                else if(piece.getTeamColor() != color){
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-2), null));
                }
            }
        }
        //THIRD ROW
        if (startPosition.getRow()-1 >= 1){
            if (startPosition.getColumn()+ 2 <= 8){
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+2));
                if(piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+2), null));
                }
                // if I can take
                else if(piece.getTeamColor() != color){
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+2), null));
                }
            }
            if (startPosition.getColumn()- 2 >= 1){
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-2));
                if(piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-2), null));
                }
                // if I can take
                else if(piece.getTeamColor() != color){
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-2), null));
                }
            }
        }
        //LAST ROW
        if (startPosition.getRow()-2 >= 1) {
            if (startPosition.getColumn() + 1 <= 8) {
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1));
                if (piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1), null));
                }
                // if I can take
                else if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1), null));
                }
            }
            if (startPosition.getColumn() - 1 >= 1) {
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() - 1));
                if (piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() - 1), null));
                }
                // if I can take
                else if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() - 1), null));
                }
            }
        }
        return moves;
    }
}
