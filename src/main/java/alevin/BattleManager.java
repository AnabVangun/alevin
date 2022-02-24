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
    /** Return the index of the actor currently playing. */
    public int getCurrentActorIndex(){
        return turnIndex;
    }
    /** Return the actor that won the battle, or null if no actor has won yet. */
    public Actor isWon(){
        return winner;
    }
    /** Move the actor currently playing to a new position. */
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
            result = actor.name + " moves from [" + actor.getXPosition() + ", " 
                    + actor.getYPosition() + "] to [" + x + ", " + y + "].";
            invoker.execute(new Move(actor, x, y));
        } else {
            result = actor.name + " tried to collide with " + collision.name 
                + ", could not move.";
            invoker.execute(new Move(actor, actor.getXPosition(), actor.getYPosition()));
        }
        turnIndex = (turnIndex+1) % actors.size();
        return result;
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
            result = attacker.name + " attacks " + target.name + " for " + damage 
                + " damage.";
        } else {
            damage = 0;
            result = attacker.name + " missed when attacking " + target.name;
        }
        invoker.execute(new Attack(target, damage));
        if (!target.isAlive()){
            winner = attacker;
        }
        turnIndex = (turnIndex+1) % actors.size();
        return result;
    }
    public void undo(){
        turnIndex = (turnIndex+actors.size()-1) % actors.size();
        invoker.undo();
    }
    public boolean canUndo(){
        return invoker.canUndo();
    }
    public void redo(){
        turnIndex = (turnIndex+1) % actors.size();
        invoker.redo();
    }
    public boolean canRedo(){
        return invoker.canRedo();
    }
}
