package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.util.Collection;
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
    private GameData gameData;
    private WSClient ws;
    private ChessPrinting chessPrinting;
    private int gameID;
    private String color;

    //KEEP TRACK IF JOINED AS WHITE OR BLACK OR OBSERVER

    //WHEN I GET FIRST LOADGAME, SET MY GAMEDATA
    public InGameUI(AuthData auth,int gameId,String color){
        authData = auth;
        gameID = gameId;
        this.color = color;

        try{
            ws = new WSClient(this);
            commandSender(UserGameCommand.CommandType.CONNECT);
        } catch (Exception e) {
            System.out.println("I've decided not to work right now.");
        }
        //HERE
        inGameLooper();
    }

    public void setGameData(GameData game){
        gameData = game;
        chessPrinting = new ChessPrinting(game);
        printColorBoard();
    }

    public void printColorBoard(){
        if(color.equals("BLACK")){
            chessPrinting.printBlackBoard();
        }else{
            chessPrinting.printWhiteBoard();
        }
        System.out.print(inGame);
    }


    public void inGameLooper(){
        String response;
        String [] parsedResponse;

        while(true) {

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
                    chessPrinting = new ChessPrinting(gameData);
                    printColorBoard();
                    //RE DRAW BOARD
                    break;
                case "leave":
                    System.out.println("Leaving game...");
                    //SERVER MESSAGE PLAYER LEFT GAME
                    try{
                        commandSender(UserGameCommand.CommandType.LEAVE);
                    } catch (Exception e) {
                        System.out.println("Leave send failed");
                    }

                    return;
                case "move":
                    System.out.println("Making move...");
                    //SERVER MESSAGE PLAYER MADE MOVE
                    ChessPosition start = chessPositionTranslator(parsedResponse[1]);
                    ChessPosition end = chessPositionTranslator(parsedResponse[2]);
                    ChessPiece.PieceType promote = null;
                    try{
                        String promoteString = parsedResponse[3];
                        promote = promoteStringTranslator(promoteString);
                    } catch (Exception ignore) {
                    }


                    ChessMove move = new ChessMove(start,end,promote);
                    try{
                        moveSender(move);
                    } catch (Exception e) {
                        System.out.println("Makemove send failed");
                    }
                    break;
                case "resign":

                    boolean waitForResign = true;
                    while(waitForResign) {
                        System.out.println("Are you sure you want to resign? y/n");
                        String res = reader.nextLine();
                        if (res.equals("y")) {
                            System.out.println("Resigning!");
                            waitForResign = false;
                            try{
                                commandSender(UserGameCommand.CommandType.RESIGN);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else if (res.equals("n")) {
                            System.out.println("Continuing Game!");
                            waitForResign = false;
                        } else {
                            System.out.println("Command not understood, try that again?");
                        }
                    }

                    break;
                case "highlight":
                    System.out.println("Highlighting moves...");

                    chessPrinting = new ChessPrinting(gameData);
                    chessPrinting.highlightSetter(true);
                    ChessPosition pos = chessPositionTranslator(parsedResponse[1]);
                    Collection<ChessMove> moves = gameData.game().validMoves(pos);
                    chessPrinting.collectionSetter(moves);
                    printColorBoard();
                    chessPrinting.highlightSetter(false);
                    break;
                default:
                    System.out.println("Command not understood, try again");
                    break;
            }
        }
    }

    //RESIGN MUST PROMPT TO RESIGN
    //RESIGN MUST include name not color



    private ChessPiece.PieceType promoteStringTranslator(String promoteString){
        switch(promoteString){

        }
        return ChessPiece.PieceType.PAWN;
    }

    private void commandSender(UserGameCommand.CommandType type)throws Exception{
        UserGameCommand command = new UserGameCommand(type, authData.authToken(), gameID);
        Gson g = new Gson();
        String json = g.toJson(command);
        ws.send(json);
    }

    private void moveSender(ChessMove move)throws Exception{
        MakeMoveCommand command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authData.authToken(), gameData.gameID(),move);
        Gson g = new Gson();
        String json = g.toJson(command);
        ws.send(json);
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
    }

}
