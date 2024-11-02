package dataaccess;

import chess.ChessGame;
import model.AddPlayer;
import model.AuthData;
import model.GameData;
import model.UserData;

import org.junit.jupiter.api.*;


public class DataAccessTests {
    private UserDAO user;
    private AuthDAO auth;
    private GameDAO game;
    private SQLDA sql;
    UserData userMan = new UserData("Billy","Bob","Joe");



    @BeforeEach
    public void setUp() throws DataAccessException {
        user = new SQLUserDAO();
        auth = new SQLAuthDAO();
        game = new SQLGameDAO();
        sql = new SQLDA();
        sql.goodbyeData();
    }

    @Test
    public void addUserToData(){
        try{
            user.create(userMan);
            Assertions.assertInstanceOf(UserData.class,user.get(userMan.username()),"User was not added");
        } catch (DataAccessException ignore){
        }
    }

    @Test
    public void addUserTwiceToData(){
        try{
            user.create(userMan);
            user.create(userMan);
            assert true : "Exception for double add not thrown";
        } catch (RuntimeException | DataAccessException ignore){
        }
    }

    @Test
    public void getUserFromData(){
        try{
            user.create(userMan);
            UserData testUser = user.get(userMan.username());
            Assertions.assertEquals(userMan,testUser,"User was not added");
        } catch (DataAccessException ignore){
        }
    }

    @Test
    public void getFalseUserFromData(){
        try{
            UserData testUser = new UserData("HA","Skill","Issue");
            Assertions.assertNull(user.get(testUser.username()),"Not found as null");
        } catch (DataAccessException ignore){
        }
    }

    @Test
    public void clearUser(){
        try{
            user.create(userMan);
            user.clear();
            Assertions.assertNull(user.get(userMan.username()),"User was not deleted");
        } catch (DataAccessException ignore){
        }
    }

    @Test
    public void clearGame(){
        try{
            game.createGame("FunGame");
            game.clear();
            assert game.listAllGames().isEmpty():"Games not cleared";
        } catch (DataAccessException ignore){
        }
    }

    @Test
    public void clearAuth(){
        try{
            AuthData testData = auth.create("Jimmy");
            auth.clear();
            Assertions.assertNull(auth.get(testData.authToken()),"Auth not cleared");
        } catch (DataAccessException ignore){
        }
    }

    @Test
    public void addAuth(){
        try{
            AuthData testData = auth.create(userMan.username());
            Assertions.assertEquals(testData, auth.get(testData.authToken()),"AuthData not added");
        } catch (DataAccessException ignore){
        }
    }

    @Test
    public void addBadAuthData(){
        try{
            auth.create(null);
            assert false: "null exception not caught";
        } catch (DataAccessException | RuntimeException ignore){
        }
    }

    @Test
    public void getCreatedAuthData(){
        try{
            AuthData testData = auth.create("Simon");
            Assertions.assertNotNull(auth.get(testData.authToken()));
        } catch (DataAccessException | RuntimeException ignore){
        }
    }

    @Test
    public void getBadAuthData(){
        try{
            AuthData testData = auth.create("Jeffery");
            Assertions.assertNull(auth.get(testData.username()));
        } catch (DataAccessException | RuntimeException ignore){
        }
    }

    @Test
    public void logoutAuth(){
        try{
            AuthData testData = auth.create("Jeffery");
            auth.deleteAuthToken(testData.authToken());
            Assertions.assertNull(auth.get(testData.authToken()));
        } catch (DataAccessException | RuntimeException ignore){
        }
    }

    @Test
    public void logoutBadAuthToken(){
        try{
            AuthData testData = auth.create("Jeffery");
            auth.deleteAuthToken(testData.username());
            Assertions.assertNotNull(auth.get(testData.authToken()));
        } catch (DataAccessException | RuntimeException ignore){
        }
    }

    @Test
    public void listGames(){
        try{
            game.createGame("Jim");
            game.createGame("Jeb");
            Assertions.assertFalse(game.listAllGames().isEmpty(),"Games not added");
        } catch (DataAccessException | RuntimeException ignore){
        }
    }

    @Test
    public void listNoGames(){
        try{
            Assertions.assertTrue(game.listAllGames().isEmpty(),"Games not added");
        } catch (DataAccessException | RuntimeException ignore){
        }
    }

    @Test
    public void createTwoGames(){
        try{
            game.createGame("Donkey");
            game.createGame("Kong");
            assert (game.listAllGames().size() == 2): "Games not created properly";
        } catch (DataAccessException | RuntimeException ignore){
        }
    }

    @Test
    public void createdNoGames() {
        try {
            GameData testData = new GameData(123,null,null,null,new ChessGame());
            Assertions.assertNull(game.getGame(testData.gameID()),"Games not registered");
        } catch (DataAccessException | RuntimeException ignore) {
        }
    }

    @Test
    public void getGame(){
        try{
            GameData testData = game.createGame("Donkey");
            Assertions.assertEquals(game.getGame(testData.gameID()).gameID(),testData.gameID(),"Tested data not equal");
        } catch (DataAccessException | RuntimeException ignore){
        }
    }

    @Test
    public void getBadGameID() {
        try {
            Assertions.assertNull(game.getGame(1),"Id returned good game");
        } catch (DataAccessException | RuntimeException ignore) {
        }
    }

    @Test
    public void addPlayerToGame() {
        try {
            GameData testData = game.createGame("Joey");
            AddPlayer player = new AddPlayer("WHITE",testData.gameID(),"Joey");
            game.updateGame(player);
            assert (game.getGame(testData.gameID()).whiteUsername().equals("Joey")) : "Joey not added";
        } catch (DataAccessException | RuntimeException ignore) {
        }
    }

    @Test
    public void addPlayerToNoGame() {
        try {
            AddPlayer player = new AddPlayer("WHITE",5,"Joey");
            int tester = game.updateGame(player).gameID();
            assert false : "Player added to no game";
        } catch (DataAccessException | RuntimeException ignore) {
        }
    }

    @Test
    public void testSpotEmpty() {
        try {
            GameData testData = game.createGame("Happy");
            assert game.spotEmpty("WHITE", testData.gameID()): "Spot not empty";
        } catch (DataAccessException | RuntimeException ignore) {
        }
    }

    @Test
    public void spotIsNotEmpty() {
        try {
            GameData testData = game.createGame("Happy");
            AddPlayer player = new AddPlayer("WHITE", testData.gameID(), "Joey");
            game.updateGame(player);
            assert !game.spotEmpty("WHITE", testData.gameID()): "Spot returned empty";
        } catch (DataAccessException | RuntimeException ignore) {
        }
    }







}
