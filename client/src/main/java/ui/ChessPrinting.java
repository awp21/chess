package ui;

import chess.ChessBoard;

public class ChessPrinting {
    private ChessBoard board;

    public ChessPrinting(ChessBoard b) {
        board = b;
        printBoard();
    }

    private static void printBoard(){
        System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
        System.out.print("So this is blue");
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
    }


}
