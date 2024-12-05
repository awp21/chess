package ui;
import chess.ChessGame;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class WSClient extends Endpoint {
    public Session session;

    public WSClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                //CHANGE JSON TO OBJECT
                //MAKE SERVER MESSAGE
                Gson g = new Gson();
                ServerMessage recievedCommand = g.fromJson(message, ServerMessage.class);
                switch(recievedCommand.getServerMessageType()){
                    case NOTIFICATION:
                        System.out.println("Notification Recieved");
                        break;
                    case ERROR:
                        System.out.println("Error Recieved");
                        break;
                    case LOAD_GAME:
                        System.out.println("Loadgame Recieved");
                        break;
                }
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}