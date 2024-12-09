package service;


import chess.ChessGame;
import dataaccess.*;
import model.AddPlayer;
import model.AuthData;
import model.UserData;
import model.GameData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Iterator;
import java.util.Set;

public class Service {
    private static final UserDAO userdao= new SQLUserDAO();
    private static final AuthDAO authdao = new SQLAuthDAO();
    public static final GameDAO gamedao = new SQLGameDAO();

    //MAKE METHOD


    public static void deleteData(){
        try {
            userdao.clear();
            authdao.clear();
            gamedao.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameData getGameFromID(int gameID,String authToken)throws UnauthorizedException, BadRequestException{
        try{
            if(authToken != null) {
                if(authdao.get(authToken) == null) {
                    throw new UnauthorizedException("Bad Authtoken");
                }
            }else{
                throw new UnauthorizedException("AuthToken Null");
            }
            Iterator<GameData> gameDataIterator = listGames(authToken).iterator();
            GameData gameDataTester = null;
            while(gameDataIterator.hasNext()){
                gameDataTester = gameDataIterator.next();
                if(gameDataTester.gameID()==gameID){
                    return gameDataTester;
                }
            }
            throw new BadRequestException("Game doesn't exist");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void makeMove(ChessGame game, int gameId, String authToken) throws UnauthorizedException, BadRequestException{
        try{
            if(authToken != null) {
                if(authdao.get(authToken) == null) {
                    throw new UnauthorizedException("Bad Authtoken");
                }
            }else{
                throw new UnauthorizedException("AuthToken Null");
            }
            if(getGameFromID(gameId,authToken)!=null){
                gamedao.makeMoveInGame(game,gameId);
                return;
            }
            throw new BadRequestException("GameID doesn't exist");
        }catch(DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    public static String getUsernameFromAuthToken(String authToken)throws UnauthorizedException{
        try{
            if(authToken != null) {
                if (authdao.get(authToken) != null) {
                    return authdao.get(authToken).username();
                }
            }
            throw new UnauthorizedException("Error: unauthorized");
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }

    }

    public static void addPlayerToGame(String authToken, AddPlayer player)throws UnauthorizedException, BadRequestException,AlreadyTakenException{
        try{
            if(authdao.get(authToken)!=null){
                if(gamedao.getGame(player.gameID())!=null && player.playerColor() != null){
                    if(gamedao.spotEmpty(player.playerColor(),player.gameID())){
                        gamedao.updateGame(player);
                    }else{
                        throw new AlreadyTakenException("Error: already taken");
                    }
                }else{
                    throw new BadRequestException("Error: game does not exist");
                }
            }else{
                throw new UnauthorizedException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<GameData> listGames(String auth)throws UnauthorizedException{
        try{
            if(authdao.get(auth)!=null){
                return gamedao.listAllGames();
            }
            throw new UnauthorizedException("Error: unauthorized");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameData makeGame(String authToken,String gameName)throws UnauthorizedException{
        try{
            if(authdao.get(authToken)!=null){
                return gamedao.createGame(gameName);
            }
            throw new UnauthorizedException("Error: unauthorized");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logoutUser(String authToken)throws UnauthorizedException{
        try{
            if(authdao.get(authToken)!=null){
                authdao.deleteAuthToken(authToken);
            }else{
                throw new UnauthorizedException("Error: unauthorized");
            }

        }catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthData loginUser(UserData user) throws UnauthorizedException, BadRequestException {
        try {
            if(null==userdao.get(user.username())){
                throw new BadRequestException("Error: user does not exist");
            }
            if(!BCrypt.checkpw(user.password(),userdao.get(user.username()).password())){
                throw new UnauthorizedException("Error: incorrect password");
            }

            return authdao.create(user.username());
        }catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthData register(UserData newUser) throws AlreadyTakenException, BadRequestException{
        try {
            if(newUser.username() == null || newUser.email() == null || newUser.password() == null){
                throw new BadRequestException("Error: bad request");
            }

            if(null!=userdao.get(newUser.username())){
                throw new AlreadyTakenException("Error: already taken");
            }

            String hashedPassword = BCrypt.hashpw(newUser.password(), BCrypt.gensalt());
            newUser = new UserData(newUser.username(),hashedPassword, newUser.email());

            userdao.create(newUser);
            return authdao.create(newUser.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
