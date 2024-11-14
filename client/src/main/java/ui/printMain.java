package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

public class printMain {
        public static void main(String[] args) {
            GameData gameData = new GameData(1,"Happy","Sad","GameName",new ChessGame());
            ChessPrinting test = new ChessPrinting(gameData);
        }
}
