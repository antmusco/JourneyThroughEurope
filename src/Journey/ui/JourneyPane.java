package Journey.ui;

/**
 * Interface which consolidates categories of functions for managing a pane 
 * which is apart of the Journey Framework. It is designed to streamline the
 * implementation of a new pane.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Nov-09-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public interface JourneyPane {
    
    /**
     * Method to initialize the background of the pane.
     */
    abstract void initBackground();
    
    /**
     * Method to initialize the controls of the pane.
     */
    abstract void initComponents();
    
    /**
     * Method to set the behaviors of all of the controls of the pane.
     */
    abstract void setComponentBehaviors();
    
    /**
     * Method to set the layout of all of the controls of the pane.
     */
    abstract void setComponentLayouts();
    
}
