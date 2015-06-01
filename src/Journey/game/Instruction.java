package Journey.game;

/**
 * Abstract class representing an Instruction that a card may have.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public abstract class Instruction implements GameOperation {
    
    /**
     * Action to be done by the instruction on the indicated player.
     * 
     * @param player 
     *          Player to which this instruction is to be applied.
     */
    public abstract void applyTo(Player player);
   
}
