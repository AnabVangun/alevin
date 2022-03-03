package alevin;

/**
 * Interface implemented by all actions that can be undone.
 */
public interface Command {
    
    /**
     * Perform the command.
     */
    void execute();
    
    /**
     * Undo the command. This assumes that the global state is exactly as it was right after 
     * {@link #execute()} was called.
     */
    void undo();
}
