package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {
    public static Collection<ChessMove> getPawnMoves(BoardInfo boardInfo) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessBoard board = boardInfo.board();
        ChessPosition startPosition = boardInfo.position();
        ChessGame.TeamColor color = boardInfo.color();
        getPawnMovesHelper(boardInfo,moves);
        return moves;
    }

    public static void getPawnMovesHelper(BoardInfo boardInfo, Collection<ChessMove> moves) {
        ChessBoard board = boardInfo.board();
        ChessPosition startPosition = boardInfo.position();
        ChessGame.TeamColor color = boardInfo.color();
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
            pawnMoves(boardInfo,pos,moves);
        }

        pos = new ChessPosition(startPosition.getRow()+r, startPosition.getColumn()+1);
        if(pos.getColumn()>=1 && pos.getColumn()<=8){
            piece = board.getPiece(pos);
            if(piece != null && piece.getTeamColor() != color){
                pawnMoves(boardInfo,pos,moves);
            }
        }

        pos = new ChessPosition(startPosition.getRow()+r, startPosition.getColumn()-1);
        if(pos.getColumn()>=1 && pos.getColumn()<=8){
            piece = board.getPiece(pos);
            if(piece != null && piece.getTeamColor() != color){
                pawnMoves(boardInfo,pos,moves);
            }
        }
    }

    public static void pawnMoves(BoardInfo boardInfo, ChessPosition endPosition, Collection<ChessMove> moves) {
        ChessBoard board = boardInfo.board();
        ChessPosition startPosition = boardInfo.position();
        ChessGame.TeamColor color = boardInfo.color();
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
