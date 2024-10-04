package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor color, ChessPiece.PieceType type) {
        this.color = color;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type) {
            case ROOK:
                return RookMoves.getRookMoves(board, myPosition, color);
            case BISHOP:
                return BishopMoves.getBishopMoves(board, myPosition, color);
            case QUEEN:
                return QueenMoves.getQueenMoves(board, myPosition, color);
            case KING:
                return KingMoves.getKingMoves(board, myPosition, color);
            case KNIGHT:
                return KnightMoves.getKnightMoves(board, myPosition, color);
            case PAWN:
                return PawnMoves.getPawnMoves(board, myPosition, color);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + color +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    public static boolean getMovesHelper(ChessBoard board, ChessPosition startPosition, int rOff, int cOff, ChessGame.TeamColor color, Collection<ChessMove> moves) {

        int r = startPosition.getRow()+rOff;
        int c = startPosition.getColumn()+cOff;
        if(1<=r && r<=8 && 1<=c && c<=8){
            ChessPosition pos = new ChessPosition(r, c);
            ChessPiece Piece = board.getPiece(pos);
            if(Piece == null){
                moves.add(new ChessMove(startPosition, pos,null));
                return true;
            }
            else if(board.getPiece(pos).getTeamColor() != color){
                moves.add(new ChessMove(startPosition, pos,null));
                return false;
            }
        }
        return false;
    }


}
