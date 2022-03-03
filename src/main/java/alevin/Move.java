package alevin;

/**
 * Allows an actor to move.
 */
public class Move implements Command {
    private final int originX;
    private final int originY;
    private final int endpointX;
    private final int endpointY;
    private final Actor actor;
    
    public Move(Actor actor, int nextX, int nextY){
        originX = actor.getXPosition();
        originY = actor.getYPosition();
        endpointX = nextX;
        endpointY = nextY;
        this.actor = actor;
    }
    
    @Override
    public void execute(){
        actor.move(endpointX, endpointY);
    }
    
    @Override
    public void undo(){
        actor.move(originX, originY);
    }
}
