package virtualgameshelf.backend.domain;

public class Game implements Comparable<Game>{
    private long ID; // AutoSet / Not Required
    private String name; // REQUIRED
    private String system; // REQUIRED
    private int hours = 0;
    private String completion; // REQUIRED
    private int rating = 0; // rating will be 1-5
    private String excess; /* will not be grab-able. Is only used to store
                              excess information from file grabs */

    /* Start Set Functions */
    public void setId(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setExcess(String excess) {
        this.excess = excess;
    }
    /* End Set Functions */

    /* Start Get Functions */
    public long getId() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSystem() {
        return system;
    }

    public int getHours() {
        return hours;
    }

    public String getCompletion() {
        return completion;
    }

    public int getRating() {
        return rating;
    }
    /* End Get Functions */

    /* Start Override Functions */
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Game game) {
        String console1 = system;
    	String console2 = (game.system);
    	int consoleComp = console1.compareTo(console2);

        if(consoleComp != 0) {
        	return consoleComp;
        } else {
        	String game1 = name.toLowerCase();
        	String game2 = (game.name.toLowerCase());
            return game1.compareTo(game2);
        }
    }
    /* End Override Functions */
}
