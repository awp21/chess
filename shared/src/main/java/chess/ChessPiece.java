package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        ArrayList<ChessMove> moves = new ArrayList<>();
        //all pieces right now are rooks

        //loopy here to go through all possible moves
        int x = 1;
        int y = 1;

        while(myPosition.getRow()+x <= 8){
            //if empty
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+x, myPosition.getColumn()));
            if(piece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + x, myPosition.getColumn()), null));
            }
            // if I can take
            else if(piece != null && piece.getTeamColor() != this.getTeamColor()){
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + x, myPosition.getColumn()), null));
                break;
            }
            else{
                break;
            }
            x++;
        }
//        while(myPosition.getColumn()+y <= 8){
//            moves.add(new ChessMove(myPosition, endPosition, null));
//        }
//        while(myPosition.getRow()-x >= 1){
//            moves.add(new ChessMove(myPosition, endPosition, null));
//        }
//        while(myPosition.getColumn()-y >= 1){
//            moves.add(new ChessMove(myPosition, endPosition, null));
//        }


        return moves;

    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + color +
                ", type=" + type +
                '}';
    }
}
