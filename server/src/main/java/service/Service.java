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
    private static final UserDAO Userdao= new SQLUserDAO();
    private static final AuthDAO Authdao = new SQLAuthDAO();
    public static final GameDAO Gamedao = new SQLGameDAO();

    //MAKE METHOD


    public static void deleteData(){
        try {
            Userdao.clear();
            Authdao.clear();
            Gamedao.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameData getGameFromID(int gameID,String authToken)throws UnauthorizedException, BadRequestException{
        try{
            if(authToken != null) {
                if(Authdao.get(authToken) == null) {
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
                if(Authdao.get(authToken) == null) {
                    throw new UnauthorizedException("Bad Authtoken");
                }
            }else{
                throw new UnauthorizedException("AuthToken Null");
            }
            getGameFromID(gameId, authToken);
            Gamedao.makeMoveInGame(game, gameId);
        }catch(DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    public static void removePlayer(int gameId, String authToken, boolean kickWhite)throws UnauthorizedException, BadRequestException{
        try{
            if(authToken != null) {
                if(Authdao.get(authToken) == null) {
                    throw new UnauthorizedException("Bad Authtoken");
                }
            }else{
                throw new UnauthorizedException("AuthToken Null");
            }
            getGameFromID(gameId, authToken);
            Gamedao.removePlayer(kickWhite, gameId);
        }catch(DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    public static String getUsernameFromAuthToken(String authToken)throws UnauthorizedException{
        try{
            if(authToken != null) {
                if (Authdao.get(authToken) != null) {
                    return Authdao.get(authToken).username();
                }
            }
            throw new UnauthorizedException("Error: unauthorized");
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }

    }

    public static void addPlayerToGame(String authToken, AddPlayer player)throws UnauthorizedException, BadRequestException,AlreadyTakenException{
        try{
            if(Authdao.get(authToken)!=null){
                if(Gamedao.getGame(player.gameID())!=null && player.playerColor() != null){
                    if(Gamedao.spotEmpty(player.playerColor(),player.gameID())){
                        Gamedao.updateGame(player);
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
            if(Authdao.get(auth)!=null){
                return Gamedao.listAllGames();
            }
            throw new UnauthorizedException("Error: unauthorized");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameData makeGame(String authToken,String gameName)throws UnauthorizedException{
        try{
            if(Authdao.get(authToken)!=null){
                return Gamedao.createGame(gameName);
            }
            throw new UnauthorizedException("Error: unauthorized");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logoutUser(String authToken)throws UnauthorizedException{
        try{
            if(Authdao.get(authToken)!=null){
                Authdao.deleteAuthToken(authToken);
            }else{
                throw new UnauthorizedException("Error: unauthorized");
            }

        }catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthData loginUser(UserData user) throws UnauthorizedException, BadRequestException {
        try {
            if(null==Userdao.get(user.username())){
                throw new BadRequestException("Error: user does not exist");
            }
            if(!BCrypt.checkpw(user.password(),Userdao.get(user.username()).password())){
                throw new UnauthorizedException("Error: incorrect password");
            }

            return Authdao.create(user.username());
        }catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthData register(UserData newUser) throws AlreadyTakenException, BadRequestException{
        try {
            if(newUser.username() == null || newUser.email() == null || newUser.password() == null){
                throw new BadRequestException("Error: bad request");
            }

            if(null!=Userdao.get(newUser.username())){
                throw new AlreadyTakenException("Error: already taken");
            }

            String hashedPassword = BCrypt.hashpw(newUser.password(), BCrypt.gensalt());
            newUser = new UserData(newUser.username(),hashedPassword, newUser.email());

            Userdao.create(newUser);
            return Authdao.create(newUser.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
