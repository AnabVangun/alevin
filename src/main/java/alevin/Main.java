package alevin;

import java.util.Scanner;

/**
 * Client application. Handles I/O with the user.
 */
public class Main {

    /**
     * Run the game.
     * @param args ignored
     */
    public static void main(String[] args) {
        new Main().runGame(new Actor("Player1", 10, 2, 0, 0), new Actor("Player2", 15, 1, 3, 6));
    }
    
    private final Scanner scanner = new Scanner(System.in);
    private static final String NEWLINE = System.getProperty("line.separator");
    private BattleManager manager;
    
    Main(){}
    
    public void runGame(Actor player1, Actor player2){
        manager = new BattleManager(player1, player2);
        System.out.println("Battle beginning");
        while (manager.isWon() == null){
            System.out.println(getDisplayableStatus());
            handleTurn();
        }
        System.out.println(manager.isWon().name + " has won the battle");
    }
    
    /**
     * Format the current state of the battle to display it to the user.
     * @return a String representing the state of the players.
     */
    private String getDisplayableStatus(){
        StringBuilder builder = new StringBuilder("Battle status:").append(NEWLINE);
        for (Actor actor:manager.getActors()){
            builder.append("Actor ").append(actor.name).append("\t\tPosition: ")
                .append("[").append(actor.getXPosition()).append(", ").append(actor.getYPosition())
                .append("]\t\tHealth points:").append(actor.getCurrentHp()).append(NEWLINE);
        }
        return builder.toString();
    }
    
    /**
     * Handle the course of a turn. Handle user input and perform the selected action.
     * @param manager BattleManager handling the battle.
     * @param scanner Scanner used to parse the user input.
     */
    private void handleTurn(){
        System.out.println("It is " + manager.getActors().get(manager.getCurrentActorIndex()).name 
            + "'s turn. What will he do?");
        boolean search = true;
        String result;
        while(search){
            System.out.println(possibleOptions());
            String input = scanner.next();
            search = false;
            switch (input){
                case "A":
                    result = manager.attack(manager.getActors().get(
                        1-manager.getCurrentActorIndex()));
                    break;
                case "M":
                    result = manager.move(getMoveDestination("x"), 
                            getMoveDestination("y"));
                    break;
                case "U":
                    if (manager.canUndo()){
                        manager.undo();
                        result = "Last command undone";
                    } else {
                        result = "No command to undo";
                        search = true;
                    }
                    break;
                case "R":
                    if (manager.canRedo()){
                        manager.redo();
                        result = "Last undone commande redone";
                    } else {
                        result = "No command to redo";
                        search = true;
                    }
                    break;
                default:
                    result = "Could not interpret " + input + " as a command";
                    search = true;
            }
            System.out.println(result);
        }
    }
    
    /**
     * Format the options available to a user.
     * @return a String presenting all the available options to the user.
     */
    private String possibleOptions(){
        StringBuilder builder = new StringBuilder("Possible options are:").append(NEWLINE)
            .append("A: attack the other actor.").append(NEWLINE)
            .append("M: move to a new position.");
        if (manager.canUndo()){
            builder.append(NEWLINE).append("U: undo the last command.");
        }
        if (manager.canRedo()){
            builder.append(NEWLINE).append("R: redo the last command.");
        }
        return builder.toString();
    }
    
    /**
     * Handle the acquisition of a coordinate for a {@link Move} action from the user.
     * @param coordinate Which of the coordinates to ask from the user.
     * @return 
     */
    private int getMoveDestination(String coordinate){
        System.out.println("Enter the " + coordinate + "-coordinate of your destination");
        if (!scanner.hasNextInt()){
            do{
                System.out.println("Could not parse " + scanner.next() 
                        + " as an integer, please try again");
            } while (!scanner.hasNextInt());
        }
        return scanner.nextInt();
    }
}
