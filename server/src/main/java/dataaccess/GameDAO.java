package dataaccess;

import model.GameData;

import java.util.Set;

public interface GameDAO {
    Set<GameData> listAllGames() throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame() throws DataAccessException;
    GameData updateGame() throws DataAccessException;
    void clear() throws DataAccessException;
}
