package server;

import chess.*;
import com.google.gson.Gson;
import model.DataPlayersObservers;
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
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        Gson g = new Gson();
        UserGameCommand receivedCommand = g.fromJson(message, UserGameCommand.class);
        DataPlayersObservers allData;
        try{
            allData = MessageExceptionTracker.checkTokenGame(receivedCommand.getAuthToken(), receivedCommand.getGameID());
        } catch (BadRequestException | UnauthorizedException e) {
            errorSender(e.getMessage(),session);
            return;
        }
        GameData gameData = allData.gameData();
        String sessionUsername = allData.username();
        String white = allData.white();
        String black = allData.black();

        sessionMapper(session,gameData.gameID());

        UserGameCommand.CommandType type = receivedCommand.getCommandType();
        switch (type){
            case CONNECT:
                loadHandlerJoin(session,allData);
                break;
            case LEAVE:
                List<Session> changeSessions = sessionMap.get(gameData.gameID());
                changeSessions.remove(session);
                sessionMap.replace(gameData.gameID(), changeSessions);
                for(Session s : changeSessions){
                    if(s.isOpen()){
                        notificationSender(sessionUsername +" has left",s);
                    }
                }
                //EDIT SQL OF GAME
                if(sessionUsername.equals(white)){
                    Service.removePlayer(gameData.gameID(), receivedCommand.getAuthToken(),true);
                }
                if(sessionUsername.equals(black)){
                    Service.removePlayer(gameData.gameID(), receivedCommand.getAuthToken(),false);
                }

                break;
            case RESIGN:
                resignHandler(allData,session,receivedCommand);
                break;
            case MAKE_MOVE:
                if(gameData.game().isBlackHasWon()||gameData.game().isWhiteHasWon()){
                    errorSender("Game is over",session);
                    return;
                }
                MakeMoveCommand makeMove = g.fromJson(message, MakeMoveCommand.class);
                ChessMove move = makeMove.getMove();
                Collection<ChessMove> validMoves = gameData.game().validMoves(move.getStartPosition());
                if(!validMoves.contains(move)){
                    errorSender("Invalid move",session);
                    return;
                }
                ChessGame editedGame = gameData.game();
                try{
                    ChessPiece piece = editedGame.getBoard().getPiece(move.getStartPosition());
                    switch(piece.getTeamColor()){
                        case WHITE:
                            if(!sessionUsername.equals(white)){
                                errorSender("You can't move a white piece",session);
                                return;
                            }
                            break;
                        case BLACK:
                            if(!sessionUsername.equals(black)){
                                errorSender("You can't move a black piece",session);
                                return;
                            }
                            break;
                    }
                    editedGame.makeMove(move);
                    Service.makeMove(editedGame, gameData.gameID(), makeMove.getAuthToken());
                    loadHandlerMove(session,allData,move);
                    return;
                } catch (UnauthorizedException e) {
                    errorSender("Unauthorized",session);
                    return;
                } catch (InvalidMoveException e){
                    errorSender(e.getMessage(),session);
                    return;
                }
            default:
                System.out.println("Default Case, didn't work");
                break;
        }
    }

    private void resignHandler(DataPlayersObservers allData,Session session,UserGameCommand receivedCommand) throws Exception {
        GameData gameData = allData.gameData();
        String sessionUsername = allData.username();
        String white = allData.white();
        String black = allData.black();
        System.out.println("White Username = "+white);
        System.out.println("Black Username = "+black);
        System.out.println("Person who sent session = "+sessionUsername);
        ChessGame resignGame = gameData.game();
        List<Session> resignedSessions = sessionMap.get(gameData.gameID());
        if(resignGame.isWhiteHasWon()||resignGame.isBlackHasWon()){
            errorSender("Game has ended",session);
            return;
        }
        if(!sessionUsername.equals(white)&&!sessionUsername.equals(black)){
            errorSender("You cannot resign as an observer",session);
            return;
        }
        if(sessionUsername.equals(white)){
            resignGame.setBlackHasWon();
        }
        if (sessionUsername.equals(black)){
            resignGame.setWhiteHasWon();
        }
        for(Session s : resignedSessions){
            if(s.isOpen()){
                notificationSender(sessionUsername + " has resigned!",s);
            }
        }
        Service.makeMove(resignGame, gameData.gameID(), receivedCommand.getAuthToken());
    }

    private void loadHandlerJoin(Session session, DataPlayersObservers allData) throws Exception {
        try{
            GameData gameData = allData.gameData();
            String white = allData.white();
            String black = allData.black();
            String sessionUsername = allData.username();
            loadGameSender(session,gameData);

            List<Session> j = sessionMap.get(gameData.gameID());
            for(Session s : j){
                if(!s.equals(session)&&s.isOpen()){
                    if(sessionUsername.equals(white)){
                        notificationSender(sessionUsername + " joined as white",s);
                    } else if (sessionUsername.equals(black)) {
                        notificationSender(sessionUsername + " joined as black",s);
                    }else {
                        notificationSender(sessionUsername + " joined as observer",s);
                    }
                }
            }
        }catch(WebSocketException e){
            System.out.println("WebsocketBroke");
        }
    }

    private void loadHandlerMove(Session session, DataPlayersObservers allData,ChessMove move) throws Exception {
        try{
            GameData gameData = allData.gameData();
            String white = allData.white();
            String black = allData.black();
            String sessionUsername = allData.username();

            loadGameSender(session,gameData);

            boolean blackCheckedWhite = gameData.game().isInCheck(ChessGame.TeamColor.WHITE);
            boolean whiteCheckedBlack = gameData.game().isInCheck(ChessGame.TeamColor.BLACK);
            boolean blackCheckMatedWhite = gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE);
            boolean whiteCheckMatedBlack = gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK);

            List<Session> j = sessionMap.get(gameData.gameID());

            for(Session s : j){
                if (!s.isOpen()) {
                    continue;
                }
                if(!s.equals(session)){
                    loadGameSender(s,gameData);
                    if(sessionUsername.equals(white)){
                        String moveAsLetters = makeMoveTranslator(move);
                        notificationSender(sessionUsername + " made move "+moveAsLetters,s);
                    } else if (sessionUsername.equals(black)) {
                        String moveAsLetters = makeMoveTranslator(move);
                        notificationSender(sessionUsername + " made move "+moveAsLetters,s);
                    }
                }
                if(blackCheckedWhite){
                    if(blackCheckMatedWhite){
                        notificationSender(white + " was check mated! Game over!",s);
                    }else {
                        notificationSender(white + " was checked!",s);
                    }
                }
                if(whiteCheckedBlack){
                    if(whiteCheckMatedBlack){
                        notificationSender(black + " was check mated! Game over!",s);
                    }else {
                        notificationSender(black + " was checked!",s);
                    }
                }

            }
        }catch(WebSocketException e){
            System.out.println("WebsocketBroke");
        }
    }

    private String makeMoveTranslator(ChessMove move){
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        int startPosRow = startPos.getRow();
        int startPosCol = startPos.getColumn();
        int endPosRow = endPos.getRow();
        int endPosCol = endPos.getColumn();
        char startRowCharacter = (char)(startPosRow+'0');
        char startColumnLetter = (char)(startPosCol+'A'-1);
        char endRowCharacter = (char)(endPosRow+'0');
        char endColumnLetter = (char)(endPosCol+'A'-1);

        return ""+startColumnLetter+startRowCharacter+" "+endColumnLetter+endRowCharacter;
    }

    private void sessionMapper(Session session, int gameId){
        List<Session> sessions = sessionMap.get(gameId);
        if(sessions == null){
            List<Session> addOneSession = new ArrayList<>();
            addOneSession.add(session);
            sessionMap.put(gameId,addOneSession);
        }else{
            if(sessions.contains(session)){
                return;
            }
            sessions.add(session);
            sessionMap.replace(gameId,sessions);
        }
    }

    private void loadGameSender(Session session,GameData gameData) throws Exception{
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