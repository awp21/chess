import java.util.Scanner;

public class Prelogin {
    private String reg = "register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n";
    private String login = "login <USERNAME> <PASSWORD> - to play chess\n";
    private String quit = "quit - playing chess\n";
    private String help = "help - with possible commands\n";
    private String loggedOutSet = "[LOGGED_OUT] >>> ";
    Scanner reader = new Scanner(System.in);

    public Prelogin(){

    }

    public void preLogLooper(){
        boolean continueLoop = true;
        while(continueLoop) {
            System.out.print(loggedOutSet);
            String response = reader.next();
            switch (response) {
                case "help":
                    System.out.println(reg + login + quit + help);
                    break;
                case "breakIt":
                    continueLoop = false;
                    break;
                default:
                    System.out.println("Command not understood, try again");
            }
        }
    }


}
