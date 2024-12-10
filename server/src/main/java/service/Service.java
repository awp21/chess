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
    private static final UserDAO USERDAO= new SQLUserDAO();
    private static final AuthDAO AUTHDAO = new SQLAuthDAO();
    public static final GameDAO GAMEDAO = new SQLGameDAO();

    //MAKE METHOD


    public static void deleteData(){
        try {
            USERDAO.clear();
            AUTHDAO.clear();
            GAMEDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameData getGameFromID(int gameID,String authToken)throws UnauthorizedException, BadRequestException{
        try{
            if(authToken != null) {
                if(AUTHDAO.get(authToken) == null) {
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
                if(AUTHDAO.get(authToken) == null) {
                    throw new UnauthorizedException("Bad Authtoken");
                }
            }else{
                throw new UnauthorizedException("AuthToken Null");
            }
            getGameFromID(gameId, authToken);
            GAMEDAO.makeMoveInGame(game, gameId);
        }catch(DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    public static void removePlayer(int gameId, String authToken, boolean kickWhite)throws UnauthorizedException, BadRequestException{
        try{
            if(authToken != null) {
                if(AUTHDAO.get(authToken) == null) {
                    throw new UnauthorizedException("Bad Authtoken");
                }
            }else{
                throw new UnauthorizedException("AuthToken Null");
            }
            getGameFromID(gameId, authToken);
            GAMEDAO.removePlayer(kickWhite, gameId);
        }catch(DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    public static String getUsernameFromAuthToken(String authToken)throws UnauthorizedException{
        try{
            if(authToken != null) {
                if (AUTHDAO.get(authToken) != null) {
                    return AUTHDAO.get(authToken).username();
                }
            }
            throw new UnauthorizedException("Error: unauthorized");
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }

    }

    public static void addPlayerToGame(String authToken, AddPlayer player)throws UnauthorizedException, BadRequestException,AlreadyTakenException{
        try{
            if(AUTHDAO.get(authToken)!=null){
                if(GAMEDAO.getGame(player.gameID())!=null && player.playerColor() != null){
                    if(GAMEDAO.spotEmpty(player.playerColor(),player.gameID())){
                        GAMEDAO.updateGame(player);
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
            if(AUTHDAO.get(auth)!=null){
                return GAMEDAO.listAllGames();
            }
            throw new UnauthorizedException("Error: unauthorized");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameData makeGame(String authToken,String gameName)throws UnauthorizedException{
        try{
            if(AUTHDAO.get(authToken)!=null){
                return GAMEDAO.createGame(gameName);
            }
            throw new UnauthorizedException("Error: unauthorized");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logoutUser(String authToken)throws UnauthorizedException{
        try{
            if(AUTHDAO.get(authToken)!=null){
                AUTHDAO.deleteAuthToken(authToken);
            }else{
                throw new UnauthorizedException("Error: unauthorized");
            }

        }catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthData loginUser(UserData user) throws UnauthorizedException, BadRequestException {
        try {
            if(null==USERDAO.get(user.username())){
                throw new BadRequestException("Error: user does not exist");
            }
            if(!BCrypt.checkpw(user.password(),USERDAO.get(user.username()).password())){
                throw new UnauthorizedException("Error: incorrect password");
            }

            return AUTHDAO.create(user.username());
        }catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthData register(UserData newUser) throws AlreadyTakenException, BadRequestException{
        try {
            if(newUser.username() == null || newUser.email() == null || newUser.password() == null){
                throw new BadRequestException("Error: bad request");
            }

            if(null!=USERDAO.get(newUser.username())){
                throw new AlreadyTakenException("Error: already taken");
            }

            String hashedPassword = BCrypt.hashpw(newUser.password(), BCrypt.gensalt());
            newUser = new UserData(newUser.username(),hashedPassword, newUser.email());

            USERDAO.create(newUser);
            return AUTHDAO.create(newUser.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
