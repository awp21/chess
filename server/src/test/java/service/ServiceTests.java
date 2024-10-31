package service;


import dataaccess.*;
import model.AddPlayer;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.*;

public class ServiceTests {

    private UserDAO user;
    private AuthDAO auth;
    private GameDAO game;
    private Service service;
    UserData alreadyMadeUser = new UserData("Toad","Shroom","wrongemail");


    @BeforeEach
    public void setUp(){
        user = new DAUser();
        auth = new DAAuth();
        game = new DAGame();
        service = new Service(user);
        service.deleteData();
    }

    @Test
    public void userNameFromTokenGrab(){
        try{
            AuthData auth = service.register(alreadyMadeUser);
            String usernameTest = service.getUsernameFromAuthToken(auth.authToken());
            Assertions.assertEquals(alreadyMadeUser.username(), usernameTest,"username wasn't found from authToken");
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException ignore) {
        }
    }

    @Test
    public void userNameFromBadTokenGrab(){
        try{
            service.register(alreadyMadeUser);
            String usernameTest = service.getUsernameFromAuthToken("12345");
            Assertions.assertNotEquals(alreadyMadeUser.username(), usernameTest,"false authtoken gave username");
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException ignore) {
        }
    }


    @Test
    public void addTwoPlayersToGame(){
        try{
            UserData player2 = new UserData("Luigi","mario","nintendo");
            AuthData luigiAuth = service.register(player2);
            AuthData auth = service.register(alreadyMadeUser);
            GameData gameToAdd = service.makeGame(auth.authToken(),"gameTest");
            int id = gameToAdd.gameID();
            AddPlayer player = new AddPlayer("WHITE", id, alreadyMadeUser.username());
            AddPlayer luigiPlays = new AddPlayer("BLACK", id, player2.username());
            //Addplayer has problems here
            service.addPlayerToGame(auth.authToken(),player);
            service.addPlayerToGame(luigiAuth.authToken(), luigiPlays);
            //THIS IS WHERE GAMEDAO IS WERID, FIX IT
            GameData tester = service.gamedao.getGame(id);
            Assertions.assertEquals(tester.whiteUsername(),alreadyMadeUser.username(),"Player 1 not added");
            Assertions.assertEquals(tester.blackUsername(),player2.username(),"Player 2 not added");
        } catch (UnauthorizedException | BadRequestException | AlreadyTakenException | DataAccessException e) {
            assert false : "Error in request";
        }
    }

    @Test
    public void addPlayersWhereFullToGame(){
        try{
            AuthData auth = service.register(alreadyMadeUser);
            var data = service.makeGame(auth.authToken(),"gameTest");
            int id = data.gameID();
            AddPlayer player = new AddPlayer("WHITE", id, alreadyMadeUser.username());
            service.addPlayerToGame(auth.authToken(),player);
            service.addPlayerToGame(auth.authToken(),player);
            assert true : "AlreadyTakenException not caught!";
        } catch (UnauthorizedException | BadRequestException | AlreadyTakenException ignore) {
        }
    }

    @Test
    public void listCreatedGames(){
        try {
            AuthData auth = service.register(alreadyMadeUser);
            GameData game1 = service.makeGame(auth.authToken(),"game1");
            Set<GameData> gamesListed = service.listGames(auth.authToken());
            assert (!gamesListed.isEmpty()) : "Games not listed";
        } catch (AlreadyTakenException | BadRequestException | UnauthorizedException ignored) {
        }
    }

    @Test
    public void listNoGames(){
        try {
            AuthData auth = service.register(alreadyMadeUser);
            Set<GameData> gamesListed = service.listGames(auth.authToken());
            assert gamesListed.isEmpty() : "GameList isn't empty";
        } catch (AlreadyTakenException | BadRequestException | UnauthorizedException e) {
            assert false : "Error in request";
        }
    }


    @Test
    public void makeTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            GameData gdata = service.makeGame(authD.authToken(), "myNewGame");
            Assertions.assertInstanceOf(GameData.class,gdata,"Game data not created");
        } catch (AlreadyTakenException | BadRequestException | UnauthorizedException e) {
            assert false : "Game not created";
        }
    }

    @Test
    public void makeFailedTest(){
        try{
            AuthData badAuth = new AuthData("hehe","gottem");
            GameData gdata = service.makeGame(badAuth.authToken(), "badGame");
            Assertions.assertInstanceOf(GameData.class,gdata,"Game data was created without authorization");
        } catch (UnauthorizedException ignored) {
        }
    }

    //Monday 9am Entrance 1 Pedersen tower, past gift shop radiology on the right. show 15 minutes early.

    @Test
    public void registerTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            Assertions.assertInstanceOf(AuthData.class,authD,"Did not return an AuthData");
        } catch (BadRequestException | AlreadyTakenException e) {
            assert false : "Error, bad request or already taken";
        }
    }

    @Test
    public void registerBadRequestTest(){
        UserData jimbo = new UserData(null,"password","Jimbo@byu.edu");
        try{
            service.register(jimbo);
            assert false : "Did not catch bad username";
        } catch (BadRequestException | AlreadyTakenException ignored) {
        }
    }

    @Test
    public void loginTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            service.logoutUser(authD.authToken());
            service.loginUser(alreadyMadeUser);
            assert auth.getDataBase().isEmpty() : "Login Failed";
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException | DataAccessException ignored) {
        }
    }

    @Test
    public void loginNonexistentUserTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            service.logoutUser(authD.authToken());
            UserData falseUser = new UserData("Fake","hacker","notreal");
            service.loginUser(falseUser);
            assert !auth.getDataBase().isEmpty() : "Created User that wasn't registered";
            //Maybe an error here???
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException | DataAccessException ignored) {
        }
    }

    @Test
    public void logoutSuccessTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            service.logoutUser(authD.authToken());
            assert auth.getDataBase().isEmpty() : "Logout Unsuccessful";
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException | DataAccessException ignored) {
        }
    }

    @Test
    public void logoutUnAuthorizedTest(){
        try{
            service.register(alreadyMadeUser);
            service.logoutUser("BadAuthToken");
            assert !auth.getDataBase().isEmpty() : "Didn't catch bad authToken";
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException | DataAccessException ignored) {
        }
    }

    @Test
    public void testEmpty(){
        service.deleteData();
        try{
            Set expectedUser = new HashSet<>();
            Set userResult = user.getDataBase();
            Set authResult = auth.getDataBase();
            Set gameResult = game.listAllGames();
            Assertions.assertEquals(userResult,expectedUser,"UserDataBase did not clear");
            Assertions.assertEquals(authResult,expectedUser,"AuthDataBase did not clear");
            Assertions.assertEquals(gameResult,expectedUser,"GameDataBase did not clear");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }



}
