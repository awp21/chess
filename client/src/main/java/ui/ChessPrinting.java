package ui;

import chess.*;

public class ChessPrinting {
    private ChessBoard board;
    private static String whiteEmptySquare = EscapeSequences.SET_BG_COLOR_WHITE+EscapeSequences.EMPTY+EscapeSequences.RESET_BG_COLOR;
    private static String blackEmptySquare = EscapeSequences.SET_BG_COLOR_BLACK+EscapeSequences.EMPTY+EscapeSequences.RESET_BG_COLOR;

    public ChessPrinting(ChessBoard b) {
        board = b;
        printBoard();
    }

    private void printBoard(){
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        System.out.println(" A  B  C  D  E  F  G  H ");
        for(int r = 8; r>=1;r--){
            printRow(r);
        }
    }

    private void printRow(int row){
        for(int col = 1; col<=8; col++){
            ChessPosition pos = new ChessPosition(row,col);
            System.out.print(colorPrinter(pos));
            ChessPiece piece = board.getPiece(pos);
            //What if piece isn't there?
            if(piece != null){
                System.out.print(piecePrinter(piece));
            }else{
                System.out.print(EscapeSequences.EMPTY);
            }
        }
        System.out.println(EscapeSequences.RESET_BG_COLOR);
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



}
