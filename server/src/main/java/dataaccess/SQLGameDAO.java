package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AddPlayer;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.Set;

public class SQLGameDAO implements GameDAO{
    @Override
    public Set<GameData> listAllGames() throws DataAccessException {
        return Set.of();
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        String command = "INSERT INTO gamedatabase (id," +
                "gameName,ChessGame)" +
                "VALUES (?,?,?)";
        Random rand = new Random();
        int gameID = rand.nextInt(1000);
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            preparedStatement.setInt(1,gameID);
            //PROBLEM HERE WITH CREATE,WHY IS NULL CAUSING PROBLEMS
            preparedStatement.setString(2,gameName);
            String json = new Gson().toJson(new ChessGame());
            preparedStatement.setString(3,json);
            preparedStatement.executeUpdate();
            return new GameData(gameID,null,null,gameName,new ChessGame());
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT id, whiteUsername," +
                    " blackUsername, gameName," +
                    "ChessGame FROM gamedatabase WHERE id=?");
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
        return null;
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
}
