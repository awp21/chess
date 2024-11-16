package client;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.SQLUserDAO;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ResponseException;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private UserData testUser = new UserData("Billy","Bob","Joe");
    private GameData testGame = new GameData(1,"White","Black","Game",new ChessGame());

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    @BeforeEach
    public void setUp(){
        try{
            AuthData data = serverFacade.registerUser(new UserData("a","b","c"));
            serverFacade.clearData(data.authToken());
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void loginTest() {
        try{
            serverFacade.registerUser(testUser);
            AuthData auth = serverFacade.loginUser(testUser);
            assert auth != null : "Not an AuthData";
        } catch (ResponseException ignore) {
            assert false : "Exception thrown";
        }
    }

    @Test
    public void loginNoRegisterTest() {
        try{
            AuthData auth = serverFacade.loginUser(testUser);
            assert false : "No exception thrown without register";
        } catch (ResponseException ignore) {
        }
    }

    @Test
    public void logoutTest() {
        try{
            AuthData auth = serverFacade.registerUser(testUser);
            serverFacade.logoutUser(auth.authToken());
        } catch (ResponseException ignore) {
            assert false : "Exception thrown";
        }
    }

    @Test
    public void logoutNoUserTest() {
        try{
            AuthData auth = serverFacade.registerUser(testUser);
            serverFacade.logoutUser(auth.authToken());
            serverFacade.logoutUser(auth.authToken());
            assert false: "User logged out twice";
        } catch (ResponseException ignore) {
        }
    }

    @Test
    public void listGamesTest(){
        try{
            AuthData auth = serverFacade.registerUser(testUser);
            serverFacade.createGame("TestGame",auth.authToken());
            serverFacade.createGame("TestGame2",auth.authToken());
            serverFacade.createGame("TestGame3",auth.authToken());
            ListGamesResult list = serverFacade.listGames(auth.authToken());
            assert list != null : "Not a ListGamesResult";
        }catch (ResponseException ignore){
        }
    }

    @Test
    public void listNoGamesTest(){
        try{
            AuthData auth = serverFacade.registerUser(testUser);
            ListGamesResult list = serverFacade.listGames(auth.authToken());
            assert list.games().isEmpty(): "Gameslist is not empty";
        }catch (ResponseException ignore){
        }
    }

    @Test
    public void joinGameTest(){
        try{
            AuthData auth = serverFacade.registerUser(testUser);
            serverFacade.createGame("TestGame",auth.authToken());
            AddPlayer player = new AddPlayer("WHITE",1,testUser.username());
            serverFacade.joinGame(player, auth.authToken());
        } catch (ResponseException ignore) {
            assert false: "Exception thrown";
        }
    }

    @Test
    public void joinNoGameTest(){
        try{
            AuthData auth = serverFacade.registerUser(testUser);
            AddPlayer player = new AddPlayer("WHITE",1,testUser.username());
            serverFacade.joinGame(player, auth.authToken());
            assert false :"Game that doesn't exist was joined";
        } catch (ResponseException ignore) {
        }
    }

    @Test
    public void createGameTest(){
        try{
            AuthData auth = serverFacade.registerUser(testUser);
            GameIDOnly gameID = serverFacade.createGame("TestGame",auth.authToken());
            assert gameID.gameID() == 1:"Game not made";
        } catch (ResponseException ignore) {
        }
    }

    @Test
    public void registerTest() {
        try{
            AuthData auth = serverFacade.registerUser(testUser);
            assert auth != null : "Not an AuthData";
        } catch (ResponseException e) {
            Assertions.assertTrue(false,"Register failed to return AuthData");
        }
    }

    @Test
    public void badRegisterTest() {
        boolean tester = false;
        try{
            AuthData auth = serverFacade.registerUser(testUser);
            AuthData authTwo = serverFacade.registerUser(testUser);
        } catch (ResponseException e) {
            tester = true;
        }
        Assertions.assertTrue(tester,"Exception not caught");
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }
}
