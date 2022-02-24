package alevin;

/**
 * Represents an actor, capable of taking actions.
 */
public class Actor {
    public final String name;
    private int xPosition;
    private int yPosition;
    /** Number of Health points of the actor. */
    private int currentHp;
    public final int damage;
    
    public Actor(String name, int initialHp, int damage, int xPos, int yPos){
        this.name = name;
        this.currentHp = initialHp;
        this.damage = damage;
        this.xPosition = xPos;
        this.yPosition = yPos;
    }
    /** Return the current number of health points of the actor. */
    public int getCurrentHp(){
        return this.currentHp;
    }
    /**
     * Subtract damages to the health points of the actor.
     * @param damage number of health points to deduct. If negative, the actor is healed.
     */
    public void injure(int damage){
        this.currentHp -= damage;
    }
    
    public boolean isAlive(){
        return (this.currentHp > 0);
    }
    /**
     * Move to a new position.
     * @param x the new x-coordinate of the actor.
     * @param y the new y-coordinate of the actor.
     */
    public void move(int x, int y){
        this.xPosition = x;
        this.yPosition = y;
    }
    
    public int getXPosition(){
        return this.xPosition;
    }
    public int getYPosition(){
        return this.yPosition;
    }
}
