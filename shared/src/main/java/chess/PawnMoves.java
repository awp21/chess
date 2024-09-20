package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {
    public Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
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
                    addAllMoves(moves, new ChessPosition(startPosition.getRow()+ones, startPosition.getColumn()),startPosition,color);
                }
                // if I can take right
                if(startPosition.getColumn()+1<=8){
                    piece = board.getPiece(new ChessPosition(startPosition.getRow() + ones, startPosition.getColumn()+1));
                    if (piece != null && piece.getTeamColor() != color) {
                        addAllMoves(moves, new ChessPosition(startPosition.getRow()+ones, startPosition.getColumn()+1),startPosition,color);
                    }
                }
                // if I can take left
                if(startPosition.getColumn()-1>=1){
                    piece = board.getPiece(new ChessPosition(startPosition.getRow() + ones, startPosition.getColumn()-1));
                    if (piece != null && piece.getTeamColor() != color) {
                        addAllMoves(moves, new ChessPosition(startPosition.getRow()+ones, startPosition.getColumn()-1),startPosition,color);
                    }
                }
            }
        return moves;
    }

    public void addAllMoves(Collection<ChessMove> moves, ChessPosition piece, ChessPosition starting, ChessGame.TeamColor color) {
        if(piece.getRow() == 8 && color == ChessGame.TeamColor.WHITE){
            addPromotion(moves, piece, starting);
        }
        else if (piece.getRow() == 1 && color == ChessGame.TeamColor.BLACK){
            addPromotion(moves, piece, starting);
        }
        else {
            moves.add(new ChessMove(starting, piece, null));
        }
    }


    public void addPromotion(Collection<ChessMove> moves, ChessPosition piece, ChessPosition starting){
        moves.add(new ChessMove(starting, piece, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(starting, piece, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(starting, piece, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(starting, piece, ChessPiece.PieceType.KNIGHT));
    }


}
