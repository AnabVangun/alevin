package alevin;

/**
 * Allows an actor to move.
 */
public class Move implements Command {
    private final int[] origin;
    private final int[] endpoint;
    private final Actor actor;
    
    public Move(Actor actor, int nextX, int nextY){
        this.origin = new int[]{actor.getXPosition(), actor.getYPosition()};
        this.endpoint = new int[]{nextX, nextY};
        this.actor = actor;
    }
    
    @Override
    public void execute(){
        actor.move(endpoint[0], endpoint[1]);
    }
    
    @Override
    public void undo(){
        actor.move(origin[0], origin[1]);
    }
}
