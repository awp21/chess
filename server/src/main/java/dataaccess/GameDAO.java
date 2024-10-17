package dataaccess;

import model.GameData;

public interface GameDAO {
    GameData listAllGames();
    GameData createGame();
    GameData getGame();
    GameData updateGame();
    void clear();
}
