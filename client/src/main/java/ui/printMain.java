package ui;

import chess.ChessBoard;

public class printMain {
        public static void main(String[] args) {
            ChessBoard testBoard = new ChessBoard();
            testBoard.resetBoard();
            ChessPrinting test = new ChessPrinting(testBoard);
        }
}
