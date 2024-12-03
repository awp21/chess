package ui;

import chess.ChessGame;
import model.AddPlayer;
import model.AuthData;
import model.GameData;
import model.ListGamesResult;

import java.util.*;

public class PostLogin {
    private final String create = "create <NAME> - a game\n";
    private final String list = "list - games\n";
    private final String join = "join <ID> [WHITE|BLACK] - a game\n";
    private final String observe = "observe <ID> - a game\n-";
    private final String logout = "logout = when you are done\n";
    private final String quit = "quit - playing chess\n";
    private final String help = "help - with possible commands\n";
    private final String loggedInSet = "[LOGGED_IN] >>> ";
    Scanner reader = new Scanner(System.in);
    private AuthData authData;
    private ServerFacade serverfacade;
    private ListGamesResult games;
    private Map<Integer,Integer> idMap;
    private List<GameData> gamesList;

    public PostLogin(AuthData auth,ServerFacade serverFacade){
        authData = auth;
        serverfacade = serverFacade;
        try{
            games = serverfacade.listGames(authData.authToken());
            gamesList = mapGames();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private void listAllGames(){
        for(int i = 0; i<gamesList.size();i++){
            GameData game = gamesList.get(i);
            int gameNum = i+1;
            String gameName = game.gameName();
            String whiteName = game.whiteUsername();
            String blackName = game.blackUsername();
            System.out.println("Game number: "+gameNum+" Name: "+gameName);
            System.out.println("White Player: "+whiteName);
            System.out.println("Black Player: "+blackName);
            System.out.println();
        }
    }

    private List<GameData> mapGames(){
        List<GameData> gamesList = new ArrayList<>(games.games());
        idMap = new HashMap<>();
        for(int i = 0; i<gamesList.size();i++){
            GameData game = gamesList.get(i);
            int gameId = game.gameID();
            int gameNum = i+1;
            idMap.put(gameNum,gameId);
        }
        return gamesList;
    }

    public String postLogLooper(){
        String response;
        String [] parsedResponse;
        while(true) {
            System.out.print(loggedInSet);
            response = reader.nextLine();
            try{
                parsedResponse = response.split(" ");
                responseHandler(parsedResponse);
            } catch (BadCommandException e) {
                parsedResponse = new String[] {"Bad"};
            }
            switch (parsedResponse[0]) {
                case "help":
                    System.out.println(create + list + join + observe + logout + quit + help);
                    break;
                case "create":
                    System.out.println("Creating game...");
                    try{
                        serverfacade.createGame(parsedResponse[1],authData.authToken());
                        games = serverfacade.listGames(authData.authToken());
                        gamesList = mapGames();
                    } catch (ResponseException e) {
                        System.out.println("Shoot, something threw");
                    }
                    break;
                case "list":
                    System.out.println("Listing games...");
                    try{
                        games = serverfacade.listGames(authData.authToken());
                        gamesList = mapGames();
                        listAllGames();
                    } catch (ResponseException e) {
                        System.out.println("Shoot, something threw in listgames");
                    }
                    break;
                case "join":
                    System.out.println("Joining game...");
                    try{
                        String color = parsedResponse[2];
                        if(color.equals("BLACK") || color.equals("WHITE")){
                            int gameNumber = idMap.get(Integer.parseInt(parsedResponse[1]));
                            AddPlayer player = new AddPlayer(parsedResponse[2],gameNumber, authData.username());
                            serverfacade.joinGame(player,authData.authToken());
                            gamesList = mapGames();

                            //INSTEAD OF HERE
                            InGameUI ingame = new InGameUI(authData);
                            ingame.inGameLooper();

                        }else{
                            throw new Exception();
                        }
                    } catch (ResponseException |NumberFormatException | NullPointerException e) {
                        System.out.println("Request not understood, try again");
                    } catch (Exception e) {
                        System.out.println("Color isn't white or black!");
                    }
                    //MAYBE UI GOES HERE??


                    break;
                case "observe":
                    System.out.println("Observing game");
                    //GET GAMEDATA TO OBSERVE!!!
                    try{
                        int lookGame = Integer.parseInt(parsedResponse[1])-1;
                        GameData gameToSee = gamesList.get(Integer.parseInt(parsedResponse[1])-1);
                        serverfacade.observeGame(gameToSee);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("This game number is out of bounds");
                    }catch(NumberFormatException e){
                        System.out.println("Command not understood, try again");
                    }
                    break;

                case "logout":
                    System.out.println("Logging out...");
                    try{
                        serverfacade.logoutUser(authData.authToken());
                    } catch (ResponseException e) {
                        System.out.println("Shoot, something in logout");
                    }
                    return "";
                case "quit":
                    System.out.println("Quitting...");
                    try{
                        serverfacade.logoutUser(authData.authToken());
                    } catch (ResponseException e) {
                        System.out.println("Shoot, something in logout");
                    }
                    return "quit";
                default:
                    System.out.println("Command not understood, try again");
            }
        }
    }

    private void responseHandler(String [] response) throws BadCommandException{
        int len = response.length;
        String first = response[0];
        switch(len){
            case 1:
                if(!first.equals("quit")&&!first.equals("help")&&!first.equals("logout")&&!first.equals("list")){
                    throw new BadCommandException();
                }
                break;
            case 2:
                if(!first.equals("create")&&!first.equals("observe")){
                    throw new BadCommandException();
                }
                break;
            case 3:
                if(!first.equals("join")){
                    throw new BadCommandException();
                }
                break;
            default:
                throw new BadCommandException();
        }
    }
}
