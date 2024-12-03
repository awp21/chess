package ui;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.AddPlayer;
import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;

import java.util.Scanner;

public class InGameUI {
    private final String redraw = "redraw - the board\n";
    private final String leave = "leave - the game\n";
    private final String makeMove = "move - <position> to <position>\n";
    private final String resign = "resign - forfeit the game\n";
    private final String highlight = "highlight - the legal moves of <position>\n";
    private final String help = "help - with possible commands\n";
    private final String inGame = "[PLAYING] >>> ";
    Scanner reader = new Scanner(System.in);
    private AuthData authData;
    private WSClient ws;

    public InGameUI(AuthData auth){
        authData = auth;
        try{
            ws = new WSClient();
            //PUT AT START OF GAME
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, auth.authToken(), 3);
            Gson g = new Gson();
            String json = g.toJson(command);
            ws.send(json);

        } catch (Exception e) {
            System.out.println("I've decided not to work right now.");
        }
    }

    public void inGameLooper(){
        String response;
        String [] parsedResponse;

        while(true) {
            System.out.print(inGame);
            response = reader.nextLine();
            try{
                parsedResponse = response.split(" ");
                responseHandler(parsedResponse);
            } catch (BadCommandException e) {
                parsedResponse = new String[] {"Bad"};
            }
            switch (parsedResponse[0]) {
                case "help":
                    System.out.println(redraw + leave + makeMove + resign + highlight + help);
                    break;
                case "redraw":
                    System.out.println("Redrawing board...");
                    //RE DRAW BOARD
                    break;
                case "leave":
                    System.out.println("Leaving game...");
                    //SERVER MESSAGE PLAYER LEFT GAME
                    return;
                case "move":
                    System.out.println("Making move...");
                    //SERVER MESSAGE PLAYER MADE MOVE
                    ChessPosition start = chessPositionTranslator(parsedResponse[1]);
                    ChessPosition end = chessPositionTranslator(parsedResponse[2]);
                    System.out.println(start.getRow());
                    System.out.println(start.getColumn());
                    System.out.println("Move Made!");
                    break;
                case "resign":
                    System.out.println("Resigning...");
                    //SERVER MESSAGE PLAYER resigned


                    System.out.println("Resigned");
                    break;
                case "highlight":
                    System.out.println("Highlighting moves...");
                    //HIGHLIGHT MOVE PRINTBOARD
                    break;
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
                if(!first.equals("redraw")&&!first.equals("help")&&!first.equals("leave")&&!first.equals("resign")){
                    throw new BadCommandException();
                }
                break;
            case 2:
                if(!first.equals("highlight")){
                    throw new BadCommandException();
                }
                break;
            case 3:
                if(!first.equals("move")){
                    throw new BadCommandException();
                }
                break;
            default:
                throw new BadCommandException();
        }
    }

    private ChessPosition chessPositionTranslator(String input){
        char column = input.charAt(0);
        char row = input.charAt(1);
        int c = column - 'A' + 1;
        int r = Integer.parseInt(String.valueOf(row));
        return new ChessPosition(r,c);
        //NOT TESTED
    }

}
