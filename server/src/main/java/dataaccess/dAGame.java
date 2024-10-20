package dataaccess;

import chess.ChessGame;
import model.AddPlayer;
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
    public GameData getGame(int gameId) {
        for(GameData game: gameDataBase){
            if(game.gameID() == gameId){
                return game;
            }
        }
        return null;
    }

    @Override
    public GameData updateGame(AddPlayer addPlayer) {
        GameData game = getGame(addPlayer.gameID());
        if(addPlayer.playerColor().equals("WHITE")){
            game = new GameData(addPlayer.gameID(), addPlayer.nameOfUser(), game.blackUsername(), game.gameName(), game.game());
        }else{
            game = new GameData(addPlayer.gameID(), game.whiteUsername(), addPlayer.nameOfUser(), game.gameName(), game.game());
        }
        return game;
    }

    @Override
    public boolean spotEmpty(String color, int gameId) throws DataAccessException {
        GameData game = getGame(gameId);
        String ret = (color.equals("WHITE")) ? game.whiteUsername(): game.blackUsername();
        return ret != null;
    }

    @Override
    public void clear() {
        gameDataBase = new HashSet<>();
    }

    private Set<GameData> gameDataBase = new HashSet<>();
}
