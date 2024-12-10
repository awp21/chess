package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import model.AddPlayer;
import model.GameData;

import java.util.Set;

public interface GameDAO {
    Set<GameData> listAllGames() throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameId) throws DataAccessException;
    GameData updateGame(AddPlayer addPlayer) throws DataAccessException;
    boolean spotEmpty(String color, int gameId) throws DataAccessException;
    void clear() throws DataAccessException;
    void makeMoveInGame(ChessGame game, int gameId) throws DataAccessException;
    void removePlayer(boolean kickWhite, int gameId) throws DataAccessException;
}
