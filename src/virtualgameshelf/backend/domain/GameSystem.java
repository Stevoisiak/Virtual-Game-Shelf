package virtualgameshelf.backend.domain;

public class GameSystem {
    String name;
    String fullName;
    /** true=show, false=hide */
    boolean display; // Default to true or false?

    /* 
     * Example system:
     *     name = PS4;
     *     fullName = PlayStation 4;
     *     display = true;
     */

    public GameSystem(String name, String fullName, boolean display) {
        this.name = name;
        this.fullName = fullName;
        this.display = display;
    }
    
    public GameSystem(String name, String fullName) {
        
    }
    
    /** Constructor for when only a single name is available */
    public GameSystem(String name) {
        
    }
}
