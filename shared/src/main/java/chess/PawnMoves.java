package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {
    public static Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<>();

        getPawnMovesHelper(board, startPosition, color, moves);

        return moves;
    }

    public static void getPawnMovesHelper(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int r = 0;
        if(color == ChessGame.TeamColor.WHITE){
            r = 1;
        }
        else{
            r = -1;
        }

        ChessPosition pos = new ChessPosition(startPosition.getRow()+r, startPosition.getColumn());
        ChessPiece piece = board.getPiece(pos);
        if(piece == null){
            pawnMoves(board,startPosition,pos,color,moves);
        }

        pos = new ChessPosition(startPosition.getRow()+r, startPosition.getColumn()+1);
        if(pos.getColumn()>=1 && pos.getColumn()<=8){
            piece = board.getPiece(pos);
            if(piece != null && piece.getTeamColor() != color){
                pawnMoves(board,startPosition,pos,color,moves);
            }
        }

        pos = new ChessPosition(startPosition.getRow()+r, startPosition.getColumn()-1);
        if(pos.getColumn()>=1 && pos.getColumn()<=8){
            piece = board.getPiece(pos);
            if(piece != null && piece.getTeamColor() != color){
                pawnMoves(board,startPosition,pos,color,moves);
            }
        }
    }

    public static void pawnMoves(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {

        int startingRow = 0;
        int endRow = 0;
        int doubleChange = 0;
        if(color == ChessGame.TeamColor.WHITE){
            startingRow = 2;
            endRow = 8;
            doubleChange = 2;
        }
        else{
            startingRow = 7;
            endRow = 1;
            doubleChange = -2;
        }

        if(startPosition.getRow()==startingRow){
            ChessPosition doubleJump = new ChessPosition(startPosition.getRow()+doubleChange,startPosition.getColumn());
            if(board.getPiece(doubleJump) == null){
                moves.add(new ChessMove(startPosition,doubleJump,null));
            }
        }

        if(endPosition.getRow() == endRow){
            moves.add(new ChessMove(startPosition,endPosition, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(startPosition,endPosition, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(startPosition,endPosition, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(startPosition,endPosition, ChessPiece.PieceType.QUEEN));
            return;
        }
        moves.add(new ChessMove(startPosition,endPosition, null));
    }

}
