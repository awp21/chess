package ui;

import model.AuthData;
import model.UserData;

import java.util.Scanner;

public class PreLogin {
    private final String reg = "register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n";
    private final String login = "login <USERNAME> <PASSWORD> - to play chess\n";
    private final String quit = "quit - playing chess\n";
    private final String help = "help - with possible commands\n";
    private final String loggedOutSet = "[LOGGED_OUT] >>> ";
    Scanner reader = new Scanner(System.in);
    private PostLogin postLogin;
    private ServerFacade serverfacade;

    public PreLogin(){
        serverfacade = new ServerFacade(8080);
    }

    public void preLogLooper(){
        boolean continueLoop = true;
        String response;
        String [] parsedResponse;

        while(continueLoop) {
            System.out.print(loggedOutSet);
            response = reader.nextLine();
            try{
                parsedResponse = response.split(" ");
                responseHandler(parsedResponse);
            } catch (BadCommandException e) {
                parsedResponse = new String[] {"Bad"};
            }
            switch (parsedResponse[0]) {
                case "help":
                    System.out.println(reg + login + quit + help);
                    break;
                case "register":
                    System.out.println("Registering!");

                    UserData regUser = new UserData(parsedResponse[1],parsedResponse[2],parsedResponse[3]);
                    try{
                        AuthData auth = serverfacade.registerUser(regUser);
                        postLogin = new PostLogin(auth,serverfacade);
                    } catch (ResponseException e) {
                        System.out.println("RegisterFailed");
                        break;
                    }
                    System.out.println("User registered!");
                    if(postLogin.postLogLooper().equals("quit")){
                        return;
                    }
                    break;

                case "login":
                    UserData loginUser = new UserData(parsedResponse[1],parsedResponse[2],"");
                    try{
                        AuthData auth = serverfacade.loginUser(loginUser);
                        postLogin = new PostLogin(auth,serverfacade);
                    } catch (ResponseException e) {
                        System.out.println("LoginDidn'tWork");
                        break;
                    }
                    System.out.println("User logged in!");
                    if(postLogin.postLogLooper().equals("quit")){
                        return;
                    }
                    break;
                case "quit":
                    continueLoop = false;
                    System.out.println("Quitting!");
                    break;
                default:
                    System.out.println("Command not understood, try again");
                    break;
            }
        }
    }

    private void responseHandler(String [] response) throws BadCommandException{
        int len = response.length;
        switch(len){
            case 1:
                if(!response[0].equals("quit")&&!response[0].equals("help")){
                    throw new BadCommandException();
                }
                break;
            case 3:
                if(!response[0].equals("login")){
                    throw new BadCommandException();
                }
                break;
            case 4:
                if(!response[0].equals("register")){
                    throw new BadCommandException();
                }
                break;
            default:
                throw new BadCommandException();
        }
    }


}
