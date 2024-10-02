package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor TeamTurn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        TeamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return TeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        TeamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validM = null;
        return validM;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {


        //assuming always valid
        ChessPosition start = move.getStartPosition();
//        Collection<ChessMove> testThese = validMoves(start);
//        if(!testThese.contains(move)){
//            throw new InvalidMoveException("Invalid Move");
//        }
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        if(piece == null){
            throw new InvalidMoveException("Ain't no piece there");
        }

        if(piece.getTeamColor() != TeamTurn){
            throw new InvalidMoveException("Not yo turn");
        }

        ChessPiece.PieceType promotes = move.getPromotionPiece();
        if (promotes == null){
            board.addPiece(end,piece);
            board.removePiece(start);
        }else{
            ChessPiece pawnPromotion = new ChessPiece(piece.getTeamColor(),promotes);
            board.addPiece(end,pawnPromotion);
            board.removePiece(start);
        }
        //switch color
        TeamTurn = TeamTurn==TeamColor.BLACK? TeamColor.WHITE : TeamColor.BLACK;


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isinCheckHelper(board, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }


    private static boolean isinCheckHelper(ChessBoard board, ChessGame.TeamColor color){
        ChessPosition kingPos = null;
        outerloop:
        for(int x = 1; x<=8; x++) {
            for (int y = 1; y <= 8; y++) {
                ChessPosition pos = new ChessPosition(x,y);
                ChessPiece piece = board.getPiece(pos);
                if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color){
                    kingPos = pos;
                    break outerloop;
                }
            }
        }
        if(kingPos == null){
            return false;
        }

        for(int x = 1; x<=8; x++){
            for(int y = 1; y<=8; y++){
                ChessPosition pos = new ChessPosition(x,y);
                ChessPiece piece = board.getPiece(pos);
                if(piece != null && piece.getTeamColor()!=color){
                    Collection<ChessMove> checks = piece.pieceMoves(board,pos);
                    for(ChessMove move : checks){
                        ChessPosition attack = move.getEndPosition();
                        if(attack.equals(kingPos)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


}
