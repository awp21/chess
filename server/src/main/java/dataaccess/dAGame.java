package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;
import java.util.Set;

public class dAGame implements GameDAO{

    @Override
    public Set<GameData> listAllGames() {
        return gameDataBase;
    }

    @Override
    public GameData createGame(String gameName) {
        ChessGame game = new ChessGame();
        GameData gameAdded = new GameData(123,null,null,gameName,game);
        gameDataBase.add(gameAdded);
        return gameAdded;
    }

    @Override
    public GameData getGame() {
        return null;
    }

    @Override
    public GameData updateGame() {
        return null;
    }

    @Override
    public void clear() {
        gameDataBase = new HashSet<>();
    }

    private Set<GameData> gameDataBase = new HashSet<>();
}
