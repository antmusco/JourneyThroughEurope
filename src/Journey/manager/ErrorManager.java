package Journey.manager;

import Journey.ui.JourneyUI;

/**
 * Class which handles all errors for the Journey Framework. This class will
 * either handle all errors directly, or route errors to their appropriate
 * handling methods.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-25-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class ErrorManager {

    /**
     * JourneyUI instance to which this ErrorManager is assigned.
     */
    public JourneyUI ui;
    
    /**
     * Constructor which initializes the JourneyUI to which this ErrorManager
     * is assigned.
     * 
     * @param ui
     */
    public ErrorManager(JourneyUI ui) {
        
        this.ui = ui;
        
    }

    /**
     * Method called if user attempts to begin a new game before initializing
     * the players.
     */
    public void respondToNoPlayers() {
        
        /* Notify user that no players have been selected */
        
    }
    
}
