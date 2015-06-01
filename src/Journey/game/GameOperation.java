package Journey.game;

/**
 * Abstract class representing any operation which may occur in the game. This
 * class will handle all events and keep track of all of the changes made during
 * the game, and provide a manageable record system of all operations made
 * throughout a game.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public interface GameOperation {
    
    /**
     * Applies the operation.
     */
    public abstract void apply();
    
    /**
     * Undoes the operation.
     */
    public abstract void undo();
    
}
