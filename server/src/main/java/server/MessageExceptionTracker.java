package server;

import model.DataPlayersObservers;
import model.GameData;
import service.BadRequestException;
import service.Service;
import service.UnauthorizedException;

public class MessageExceptionTracker{

    public static DataPlayersObservers checkTokenGame(String authToken, int gameID) throws UnauthorizedException, BadRequestException {
        String username = Service.getUsernameFromAuthToken(authToken);
        GameData gameData = Service.getGameFromID(gameID,authToken);
        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();
        return new DataPlayersObservers(gameData,username,white,black);
    }

}
