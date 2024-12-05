package server;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AddPlayer;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.BadRequestException;
import service.Service;
import service.UnauthorizedException;
import spark.Spark;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.*;

@WebSocket
public class WSServer {

    private Map<Integer, List<Session>> sessionMap = new HashMap<>();
    private Session session;
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        this.session = session;

        Gson g = new Gson();
        UserGameCommand recievedCommand = g.fromJson(message, UserGameCommand.class);
        int gameId = recievedCommand.getGameID();
        sessionMapper(this.session,gameId);
        //CALL NEW SERVICE METHOD


        UserGameCommand.CommandType type = recievedCommand.getCommandType();
        switch (type){
            case CONNECT:
                System.out.println("Connect Recieved!");
                loadHandler(recievedCommand,gameId);
                break;
            case LEAVE:
                System.out.println("Leave Recieved!");

                break;
            case RESIGN:
                System.out.println("Resign Recieved!");
                break;
            case MAKE_MOVE:
                System.out.println("MakeMove Recieved!");
                loadGameSender(new ChessGame(),session);
                break;
            default:
                System.out.println("Default Case, didn't work");
                break;
        }


        //SEND DIFFERENT SESSIONS??? MAKE A LIST THAT SORTS SESSIONS FOR LOADGAME/NOTIFICATIONS
    }

    private void loadHandler(UserGameCommand recievedCommand, int gameId) throws Exception {
        try{
            System.out.println("Trying to send Load");
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
                loadGameSender(new ChessGame(),session);
                List<Session> j = sessionMap.get(gameId);
                for(Session s : j){
                    if(!s.equals(session)&&s.isOpen()){
                        notificationSender("another joined",s);
                        //MAYBE START CLOSING SOME HERE
                    }
                }

                //SEND ACTUAL GAME
            }else{
                throw new BadRequestException("Game ID Doesn't exist");
            }
        }catch (UnauthorizedException e) {
            System.out.println("Trying to send auth Error");
            errorSender("Unauthorized",session);
        }catch (BadRequestException e){
            System.out.println("Trying to send id error");
            errorSender(e.getMessage(),session);
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

    private void loadGameSender(ChessGame game,Session session) throws Exception{
        Gson g = new Gson();
        ServerMessage serverMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,new ChessGame());
        String jsonMessage = g.toJson(serverMessage);
        session.getRemote().sendString(jsonMessage);
        System.out.println(jsonMessage);
    }

    private void errorSender(String error,Session session) throws Exception{
        Gson g = new Gson();
        ServerMessage serverMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,error);
        String jsonMessage = g.toJson(serverMessage);
        session.getRemote().sendString(jsonMessage);
        System.out.println("Sent a ServerMessage Back");
    }

    private void notificationSender(String notification,Session session) throws Exception{
        Gson g = new Gson();
        ServerMessage serverMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,notification);
        String jsonMessage = g.toJson(serverMessage);
        session.getRemote().sendString(jsonMessage);
        System.out.println("Sent a ServerMessage Back");
    }

}