package ui;

import chess.*;
import model.GameData;

import java.util.Collection;
import java.util.HashSet;

public class ChessPrinting {
    private GameData gameData;
    private Collection<ChessMove> validMoves;
    private Boolean highlightCheck;

    public ChessPrinting(GameData gameData) {
        this.gameData = gameData;
        highlightCheck = false;
    }

    public void printBlackBoard(){
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        System.out.println("H   G   F  E   D  C   B   A ");
        for(int r = 1; r<=8;r++){
            printRow(r,"Black");
        }
        System.out.println("Black is "+gameData.blackUsername());
    }

    public void printWhiteBoard(){
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        System.out.println("A   B   C   D  E   F   G   H ");
        for(int r = 8; r>=1;r--){
            printRow(r,"White");
        }
        System.out.println("White is "+gameData.whiteUsername());
    }

    private void printRow(int row,String color){
        ChessBoard board = gameData.game().getBoard();
        switch(color){
            case "White":
                for(int col = 1; col<=8; col++){
                    loopPrinter(row,col,board);
                }
                break;
            case "Black":
                for(int col = 8; col>=1; col--){
                    loopPrinter(row,col,board);
                }
                break;
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.println(" "+row);
    }

    private String colorPrinter(ChessPosition pos){
        boolean rowIsEven = pos.getRow()%2 == 0;
        boolean colIsEven = pos.getColumn()%2 == 0;
        if(!rowIsEven&colIsEven || rowIsEven&!colIsEven){
            return EscapeSequences.SET_BG_COLOR_WHITE;
        }
        return EscapeSequences.SET_BG_COLOR_BLACK;
    }

    private String piecePrinter(ChessPiece piece){
        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor color = piece.getTeamColor();
        switch(color){
            case WHITE:
                switch (type) {
                    case ROOK:
                        return EscapeSequences.WHITE_ROOK;
                    case BISHOP:
                        return EscapeSequences.WHITE_BISHOP;
                    case QUEEN:
                        return EscapeSequences.WHITE_QUEEN;
                    case KING:
                        return EscapeSequences.WHITE_KING;
                    case KNIGHT:
                        return EscapeSequences.WHITE_KNIGHT;
                    case PAWN:
                        return EscapeSequences.WHITE_PAWN;
                }
            case BLACK:
                switch (type) {
                    case ROOK:
                        return EscapeSequences.BLACK_ROOK;
                    case BISHOP:
                        return EscapeSequences.BLACK_BISHOP;
                    case QUEEN:
                        return EscapeSequences.BLACK_QUEEN;
                    case KING:
                        return EscapeSequences.BLACK_KING;
                    case KNIGHT:
                        return EscapeSequences.BLACK_KNIGHT;
                    case PAWN:
                        return EscapeSequences.BLACK_PAWN;
                }
        }
        return null;
    }

    public void highlightSetter(Boolean tf){
        highlightCheck = tf;
    }

    public void collectionSetter(Collection<ChessMove> validMoves){
        this.validMoves = validMoves;
    }

    private void loopPrinter(int row, int col,ChessBoard board){
        ChessPosition pos = new ChessPosition(row,col);
        if(!highlightCheck){
            System.out.print(colorPrinter(pos));
        }
        else{
            Collection<ChessPosition> possibleMoves = new HashSet<>();
            for(ChessMove m : validMoves){
                possibleMoves.add(m.getEndPosition());
            }
            if(possibleMoves.contains(pos)){
                System.out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
            }else{
                System.out.print(colorPrinter(pos));
            }
        }
        //HERE ADD HIGHLIGHTS
        ChessPiece piece = board.getPiece(pos);
        if(piece != null){
            System.out.print(piecePrinter(piece));
        }else{
            System.out.print(EscapeSequences.EMPTY);
        }
    }




}
