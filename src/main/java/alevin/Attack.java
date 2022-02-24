package alevin;

/**
 * Allows an actor to attack another.
 */
public class Attack implements Command {
    private final int damage;
    private final Actor target;
    
    public Attack(Actor target, int damage){
        this.damage = damage;
        this.target = target;
    }
    
    @Override
    public void execute(){
        target.injure(damage);
    }
    
    @Override
    public void undo(){
        target.injure(-damage);
    }
}
