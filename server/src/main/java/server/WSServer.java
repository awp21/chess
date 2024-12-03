package server;

import com.google.gson.Gson;
import model.AddPlayer;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import websocket.commands.UserGameCommand;

@WebSocket
public class WSServer {
    private Session session;
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        this.session = session;
        Gson g = new Gson();
        UserGameCommand recievedCommand = g.fromJson(message, UserGameCommand.class);

        UserGameCommand.CommandType type = recievedCommand.getCommandType();
        switch (type){
            case CONNECT:
                System.out.println("Connect Recieved!");
                break;
            case LEAVE:
                System.out.println("Leave Recieved!");
                break;
            case RESIGN:
                System.out.println("Resign Recieved!");
                break;
            case MAKE_MOVE:
                System.out.println("MakeMove Recieved!");
                break;
            default:
                System.out.println("Default Case, didn't work");
                break;
        }

        //SEND DIFFERENT SESSIONS??? MAKE A LIST THAT SORTS SESSIONS FOR LOADGAME/NOTIFICATIONS
        session.getRemote().sendString(message);
    }

}