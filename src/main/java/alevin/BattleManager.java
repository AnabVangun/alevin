package alevin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Manages the course of a battle. Keeps track of actors in presence and turns, and determines when
 * the battle is won and by whom.
 */
public class BattleManager {
    private final List<Actor> actors = new ArrayList<>();
    private final Invoker invoker = new Invoker();
    private int turnIndex = 0;
    private Actor winner = null;
    
    public BattleManager(Actor... actors){
        this.actors.addAll(Arrays.asList(actors));
    }
    
    public List<Actor> getActors(){
        return Collections.unmodifiableList(actors);
    }
    
    public int getCurrentActorIndex(){
        return turnIndex;
    }
    
    /** Return the actor that won the battle, or null if no actor has won yet. */
    public Actor isWon(){
        return winner;
    }
    
    /** 
     * Move the actor currently playing to a new position.
     * @param x x-coordinate of the new position.
     * @param y y-coordinate of the new position.
     * @return a String describing the movement.
     */
    public String move(int x, int y){
        Actor actor = actors.get(turnIndex);
        Actor collision = null;
        for (Actor candidate:actors){
            if (candidate.getXPosition() == x && candidate.getYPosition() == y){
                collision = candidate;
                break;
            }
        }
        String result;
        if (collision == null){
            result = forgeMoveDescription(x, y);
            invoker.execute(new Move(actor, x, y));
        } else {
            result = forgeCollisionDescription(collision);
            invoker.execute(new Move(actor, actor.getXPosition(), actor.getYPosition()));
        }
        turnIndex = (turnIndex+1) % actors.size();
        return result;
    }
    
    private String forgeCollisionDescription(Actor colliding){
        return String.format("%s tried to collide with %s, could not move.", 
                actors.get(turnIndex).name, colliding.name);
    }
    
    private String forgeMoveDescription(int newX, int newY){
        return String.format("%s moves from [%d,%d] to [%d,%d].", actors.get(turnIndex).name, 
            actors.get(turnIndex).getXPosition(), actors.get(turnIndex).getYPosition(), newX, newY);
    }
    
    /** Make the actor currently playing attack another actor. */
    public String attack(Actor target){
        Actor attacker = actors.get(turnIndex);
        int damage;
        String result;
        //check if in range
        if (Math.abs(attacker.getXPosition() - target.getXPosition()) <= 1 
            && Math.abs(attacker.getYPosition() - target.getYPosition()) <= 1){
            damage = attacker.damage;
            result = forgeAttackDescription(target, true, damage);
        } else {
            damage = 0;
            result = forgeAttackDescription(target, false, damage);
        }
        invoker.execute(new Attack(target, damage));
        if (!target.isAlive()){
            winner = attacker;
        }
        turnIndex = (turnIndex+1) % actors.size();
        return result;
    }
    
    public String forgeAttackDescription(Actor target, boolean hit, int damage){
        if (hit){
            return String.format("%s attacks %s for %d damage", actors.get(turnIndex).name, 
                target.name, damage);
        } else {
            return String.format("%s missed when attacking %s", actors.get(turnIndex).name,
                target.name);
        }
    }
    
    /** Undo the last performed action. */
    public void undo(){
        invoker.undo();
        turnIndex = (turnIndex+actors.size()-1) % actors.size();
    }
    
    public boolean canUndo(){
        return invoker.canUndo();
    }
    
    /** Perform again the last undone action. */
    public void redo(){
        invoker.redo();
        turnIndex = (turnIndex+1) % actors.size();
    }
    
    public boolean canRedo(){
        return invoker.canRedo();
    }
}
