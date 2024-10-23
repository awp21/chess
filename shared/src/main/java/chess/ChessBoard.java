package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    public ChessBoard(ChessBoard board) {
        squares = new ChessPiece[8][8];
        for(int r = 1; r<=8; r++){
            for(int c = 1; c<=8; c++){
                ChessPosition pos = new ChessPosition(r,c);
                squares[r-1][c-1] = board.getPiece(pos);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    // removes Piece?
    public void removePiece(ChessPosition position) {
        squares[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];

        for(int i = 1; i < 3; i++) {
            for(int j = 1; j <= 8; j++) {
                if(i == 1){
                    resetBoardHelper(i,j, ChessGame.TeamColor.WHITE);
                }
                else{
                    addPiece(new ChessPosition(i, j), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
                }

            }
        }
        for(int i = 8; i > 6; i--) {
            for(int j = 1; j <= 8; j++) {
                if(i == 8){
                    resetBoardHelper(i,j, ChessGame.TeamColor.BLACK);
                }
                else{
                    addPiece(new ChessPosition(i, j), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
                }

            }
        }
    }

    public void resetBoardHelper(int i, int j, ChessGame.TeamColor color) {
        switch (j) {
            case 1:
                addPiece(new ChessPosition(i, j), new ChessPiece(color, ChessPiece.PieceType.ROOK));
                break;
            case 2:
                addPiece(new ChessPosition(i, j), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
                break;
            case 3:
                addPiece(new ChessPosition(i, j), new ChessPiece(color, ChessPiece.PieceType.BISHOP));
                break;
            case 4:
                addPiece(new ChessPosition(i, j), new ChessPiece(color, ChessPiece.PieceType.QUEEN));
                break;

            case 5:
                addPiece(new ChessPosition(i, j), new ChessPiece(color, ChessPiece.PieceType.KING));
                break;

            case 6:
                addPiece(new ChessPosition(i, j), new ChessPiece(color, ChessPiece.PieceType.BISHOP));
                break;
            case 7:
                addPiece(new ChessPosition(i, j), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
                break;
            case 8:
                addPiece(new ChessPosition(i, j), new ChessPiece(color, ChessPiece.PieceType.ROOK));
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessBoard that)) return false;
        return Arrays.deepEquals(this.squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        String s = "ChessBoard{ ";
        for (ChessPiece[] row:squares){
            for (ChessPiece piece:row){
                if(piece!=null){
                    s += piece.toString() + " ";
                }
                else{
                    s += "0 ";
                }
            }
        }
        s += "}";
        return s;
    }
}
