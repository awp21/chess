package ui;

import java.util.Scanner;

public class PostLogin {
    private String create = "create <NAME> - a game\n";
    private String list = "list - games\n";
    private String join = "join <ID> [WHITE|BLACK] - a game\n";
    private String logout = "logout = when you are done\n";
    private String quit = "quit - playing chess\n";
    private String help = "help - with possible commands\n";
    private String loggedInSet = "[LOGGED_IN] >>> ";
    Scanner reader = new Scanner(System.in);

    public PostLogin(){

    }

    public void postLogLooper(){
        boolean continueLoop = true;
        String response;
        String [] parsedResponse;
        while(continueLoop) {
            System.out.print(loggedInSet);
            response = reader.next();
            parsedResponse = response.split(" ");
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
                    continueLoop = false;
                    System.out.println("Logging out...");
                    break;
                case "quit":
                    continueLoop = false;
                    System.out.println("Quitting...");
                    break;
                default:
                    System.out.println("Command not understood, try again");
            }
        }
    }


}
