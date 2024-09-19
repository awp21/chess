package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {
    public static Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn()));

        int ones = 0;
        int twos = 0;

        if(color == ChessGame.TeamColor.WHITE){
            ones = 1;
            twos = 2;
        }
        else{
            ones = -1;
            twos = -2;
        }
            //DOUBLE JUMP
            if (startPosition.getRow() == 2 && color == ChessGame.TeamColor.WHITE){
                piece = board.getPiece(new ChessPosition(startPosition.getRow() + twos, startPosition.getColumn()));
                if (piece == null && board.getPiece(new ChessPosition(startPosition.getRow() + ones, startPosition.getColumn())) == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + twos, startPosition.getColumn()), null));
                }
            }
            //DOUBLE JUMP
            if (startPosition.getRow() == 7 && color == ChessGame.TeamColor.BLACK){
                piece = board.getPiece(new ChessPosition(startPosition.getRow() + twos, startPosition.getColumn()));
                if (piece == null && board.getPiece(new ChessPosition(startPosition.getRow() + ones, startPosition.getColumn())) == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + twos, startPosition.getColumn()), null));
                }
            }

            //is front empty?
            if (startPosition.getRow() + ones <= 8 && startPosition.getRow() + ones >= 1) {
                //if empty
                piece = board.getPiece(new ChessPosition(startPosition.getRow() + ones, startPosition.getColumn()));
                if (piece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + ones, startPosition.getColumn()), null));
                }
                // if I can take
                piece = board.getPiece(new ChessPosition(startPosition.getRow() + ones, startPosition.getColumn()+ones));
                if (piece != null && piece.getTeamColor() != color) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + ones, startPosition.getColumn()-ones), null));
                }
                piece = board.getPiece(new ChessPosition(startPosition.getRow() + ones, startPosition.getColumn()-ones));
                if (piece != null && piece.getTeamColor() != color) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + ones, startPosition.getColumn()-ones), null));
                }
            }




        return moves;
    }
}
