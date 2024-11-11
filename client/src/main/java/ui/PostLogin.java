package ui;

import model.AuthData;

import java.util.Scanner;

public class PostLogin {
    private final String create = "create <NAME> - a game\n";
    private final String list = "list - games\n";
    private final String join = "join <ID> [WHITE|BLACK] - a game\n";
    private final String logout = "logout = when you are done\n";
    private final String quit = "quit - playing chess\n";
    private final String help = "help - with possible commands\n";
    private final String loggedInSet = "[LOGGED_IN] >>> ";
    Scanner reader = new Scanner(System.in);
    private AuthData authData;

    public PostLogin(AuthData auth){
        authData = auth;
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
                    System.out.println(create + list + join + logout + quit + help);
                    break;
                case "create":
                    System.out.println("Creating game...");
                    break;
                case "list":
                    System.out.println("Listing games...");
                    break;
                case "join":
                    System.out.println("Joining game...");
                    break;
                case "logout":
                    System.out.println("Logging out...");
                    return "";
                case "quit":
                    System.out.println("Quitting...");
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
