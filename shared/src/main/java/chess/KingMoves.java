package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves {
    public static Collection<ChessMove> getKingMoves(BoardInfo boardInfo) {
        ChessBoard board = boardInfo.board();
        ChessPosition startPosition = boardInfo.position();
        ChessGame.TeamColor color = boardInfo.color();
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece.getMovesHelper(board,startPosition,1,-1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,1,0, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,1,1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,0,-1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,0,1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-1,-1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-1,0, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-1,1, color, moves);

        return moves;
    }

}
