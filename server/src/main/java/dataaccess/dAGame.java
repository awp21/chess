package dataaccess;

import model.GameData;
import model.UserData;

import java.util.HashSet;
import java.util.Set;

public class dAGame implements GameDAO{

    @Override
    public GameData listAllGames() {
        return null;
    }

    @Override
    public GameData createGame() {
        return null;
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
