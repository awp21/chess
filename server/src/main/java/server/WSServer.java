package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.BadRequestException;
import service.Service;
import service.UnauthorizedException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.*;

@WebSocket
public class WSServer {

    private Map<Integer, List<Session>> sessionMap = new HashMap<>();
    private GameData gameData;
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
//        System.out.printf("Received: %s", message);

        Gson g = new Gson();
        UserGameCommand recievedCommand = g.fromJson(message, UserGameCommand.class);
        int gameId = recievedCommand.getGameID();
        try{
            gameData = Service.getGameFromID(gameId,recievedCommand.getAuthToken());
        } catch (UnauthorizedException e) {
            errorSender("Unauthorized",session);
            return;
        }

//        System.out.println(gameData);

        sessionMapper(session,gameId);

        UserGameCommand.CommandType type = recievedCommand.getCommandType();
        switch (type){
            case CONNECT:
//                System.out.println("Connect Recieved!");
                loadHandler(session,recievedCommand,gameId,false);
                break;
            case LEAVE:
//                System.out.println("Leave Recieved!");
                break;
            case RESIGN:
//                System.out.println("Resign Recieved!");
                break;
            case MAKE_MOVE:
//                System.out.println("MakeMove Recieved!");
                MakeMoveCommand makeMove = g.fromJson(message, MakeMoveCommand.class);
                //MOVE HERE IS NULL
                ChessMove move = makeMove.getMove();
                Collection<ChessMove> validMoves = gameData.game().validMoves(move.getStartPosition());
                if(validMoves.contains(move)){
                    ChessGame editedGame = gameData.game();
                    try{
                        editedGame.makeMove(move);
                        Service.makeMove(editedGame,gameId,makeMove.getAuthToken());
                        gameData = Service.getGameFromID(gameId,recievedCommand.getAuthToken());
                        loadHandler(session,recievedCommand,gameId,true);
                    } catch (UnauthorizedException e) {
                        errorSender("Unauthorized",session);
                        return;
                    } catch (InvalidMoveException e){
                        errorSender(e.getMessage(),session);
                        return;
                    }
                }else{
                    errorSender("Invalid move",session);
                }
                break;
            default:
                System.out.println("Default Case, didn't work");
                break;
        }
    }

    private void loadHandler(Session session, UserGameCommand recievedCommand, int gameId,boolean moveMade) throws Exception {
        try{
            Service.getUsernameFromAuthToken(recievedCommand.getAuthToken());
            Set<GameData> games = Service.listGames(recievedCommand.getAuthToken());

            Iterator<GameData> gameIterator = games.iterator();
            boolean idCheck = false;
            int gameIDInt = -1;
            while(gameIterator.hasNext()){
                gameIDInt = gameIterator.next().gameID();
                if(gameIDInt == gameId){
                    idCheck = true;
                }
            }
            if(idCheck){
                loadGameSender(session);
                List<Session> j = sessionMap.get(gameId);
                for(Session s : j){
                    if(!s.equals(session)&&s.isOpen()){
                        notificationSender("another joined",s);
                        //MAYBE START CLOSING SOME HERE
                    }
                    //I BREAK HERE
                    if(moveMade&&!s.equals(session)&&s.isOpen()){
                        loadGameSender(s);
                    }
                }
            }else{
                throw new BadRequestException("Game ID Doesn't exist");
            }
        }catch (UnauthorizedException e) {
            errorSender("Unauthorized",session);
        }catch (BadRequestException e){
            errorSender(e.getMessage(),session);
        }catch(WebSocketException e){
            System.out.println("WEbsocketBroke");
        }
    }

    private void sessionMapper(Session session, int gameId){
        List<Session> sessions = sessionMap.get(gameId);
        if(sessions == null){
            List<Session> addOneSession = new ArrayList<>();
            addOneSession.add(session);
            sessionMap.put(gameId,addOneSession);
        }else{
            sessions.add(session);
            sessionMap.replace(gameId,sessions);
        }
    }

    private void loadGameSender(Session session) throws Exception{
        Gson g = new Gson();
        ServerMessage serverMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameData);
        String jsonMessage = g.toJson(serverMessage);
        session.getRemote().sendString(jsonMessage);
    }

    private void errorSender(String error,Session session) throws Exception{
        Gson g = new Gson();
        ServerMessage serverMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,error);
        String jsonMessage = g.toJson(serverMessage);
        session.getRemote().sendString(jsonMessage);
    }

    private void notificationSender(String notification,Session session) throws Exception{
        Gson g = new Gson();
        ServerMessage serverMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,notification);
        String jsonMessage = g.toJson(serverMessage);
        session.getRemote().sendString(jsonMessage);
    }

}