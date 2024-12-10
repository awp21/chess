package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AddPlayer;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SQLGameDAO implements GameDAO{
    @Override
    public Set<GameData> listAllGames() throws DataAccessException {
        Set<GameData> allGames = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM gamedatabase");
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {

                String gameString = rs.getString("ChessGame");
                ChessGame game = new Gson().fromJson(gameString, ChessGame.class);
                GameData addedGame = new GameData(rs.getInt("id"), rs.getString("whiteUsername"),
                        rs.getString("blackUsername"), rs.getString("gameName"),
                        game);
                allGames.add(addedGame);
            }
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        return allGames;
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        String command = "INSERT INTO gamedatabase (gameName,ChessGame) VALUES (?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,gameName);
            String json = new Gson().toJson(new ChessGame());
            preparedStatement.setString(2,json);
            preparedStatement.executeUpdate();
            ResultSet res = preparedStatement.getGeneratedKeys();
            res.next();
            return new GameData(res.getInt(1),null,null,gameName,new ChessGame());
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT * FROM gamedatabase WHERE id=?");
            preparedStatement.setInt(1, gameId);
            var rs = preparedStatement.executeQuery();
            if(!rs.next()){
                return null;
            }
            var idNumber = rs.getInt("id");
            if(idNumber == gameId){
                String gameString = rs.getString("ChessGame");
                ChessGame game = new Gson().fromJson(gameString, ChessGame.class);

                return new GameData(rs.getInt("id"),rs.getString("whiteUsername"),
                        rs.getString("blackUsername"),rs.getString("gameName"),
                        game);
                //FIND WAY TO MAKE CHESSGAME STRING TO CHESSGAME
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public GameData updateGame(AddPlayer addPlayer) throws DataAccessException {
        GameData game = getGame(addPlayer.gameID());
        boolean whiteAdded;
        if(addPlayer.playerColor().equals("WHITE")){
            game = new GameData(addPlayer.gameID(), addPlayer.nameOfUser(), game.blackUsername(), game.gameName(), game.game());
            whiteAdded=true;
        }else{
            game = new GameData(addPlayer.gameID(), game.whiteUsername(), addPlayer.nameOfUser(), game.gameName(), game.game());
            whiteAdded=false;
        }
        String command;
        if(whiteAdded){
            command = "UPDATE gamedatabase SET whiteUsername=? WHERE id=?";
        }else{
            command = "UPDATE gamedatabase SET blackUsername=? WHERE id=?";
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            if(whiteAdded){
                preparedStatement.setString(1,game.whiteUsername());
            }else {
                preparedStatement.setString(1, game.blackUsername());
            }
            preparedStatement.setInt(2,game.gameID());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return game;
    }

    @Override
    public boolean spotEmpty(String color, int gameId) throws DataAccessException {
        GameData testGame = getGame(gameId);
        String ret = (color.equals("WHITE")) ? testGame.whiteUsername(): testGame.blackUsername();
        return ret == null;
    }

    @Override
    public void clear() throws DataAccessException {
        String command = "TRUNCATE gamedatabase";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void makeMoveInGame(ChessGame game, int gameId) throws DataAccessException {
        String command = "UPDATE gamedatabase SET ChessGame=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            String json = new Gson().toJson(game);
            preparedStatement.setString(1,json);
            preparedStatement.setInt(2,gameId);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removePlayer(boolean kickWhite, int gameId) throws DataAccessException {
        String command = kickWhite ? "UPDATE gamedatabase SET whiteUsername =? WHERE id =?" : "UPDATE gamedatabase SET blackUsername =? WHERE id =?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            //HERE WILL PROBABLY BREAK
            preparedStatement.setString(1,null);
            preparedStatement.setInt(2,gameId);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
