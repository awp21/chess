package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves {
    public static Collection<ChessMove> getKnightMoves(BoardInfo boardInfo) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessBoard board = boardInfo.board();
        ChessPosition startPosition = boardInfo.position();
        ChessGame.TeamColor color = boardInfo.color();
        ChessPiece.getMovesHelper(board,startPosition,2,-1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,2,1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,1,-2, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,1,2, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-1,-2, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-1,2, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-2,-1, color, moves);
        ChessPiece.getMovesHelper(board,startPosition,-2,1, color, moves);

        return moves;
    }
}
