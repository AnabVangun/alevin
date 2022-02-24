package alevin;

import java.util.Deque;
import java.util.LinkedList;
/**
 * Invoker of the command design pattern. Handles the execution, undoing and redoing of commands.
 */
public class Invoker {
    private final Deque<Command> executedCommands = new LinkedList<>();
    private final Deque<Command> undoneCommands = new LinkedList<>();
    private final static int MAX_UNDO_STACK_SIZE = 3;
    
    Invoker(){}
    /**
     * Utility method to factor the internal workings of archiving a command.
     * @param command Command executed or undone.
     * @param receiver Queue in which the command must be archived.
     */
    private static void commandArchiver(Command command, Deque<Command> receiver){
        receiver.addFirst(command);
        if (receiver.size() > MAX_UNDO_STACK_SIZE){
            receiver.removeLast();
        }
    }
    public void execute(Command command){
        command.execute();
        commandArchiver(command, executedCommands);
        undoneCommands.clear();
    }
    public void undo(){
        Command command = executedCommands.removeFirst();
        command.undo();
        commandArchiver(command, undoneCommands);
    }
    public boolean canUndo(){
        return executedCommands.size() > 0;
    }
    public void redo(){
        Command command = undoneCommands.removeFirst();
        command.execute();
        commandArchiver(command, executedCommands);
    }
    public boolean canRedo(){
        return undoneCommands.size() > 0;
    }
}