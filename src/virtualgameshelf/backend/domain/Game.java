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

    public Game () {
        // TODO: Set unique ID?
    }

    public Game (String name, String system, int hours, String completion, int rating) {
        // TODO: Set unique ID?
        this.name = name;
        this.system = system;
        this.hours = hours;
        this.completion = completion;
        this.rating = rating;
    }

    /** Used by VirtualGameShelf.getGameIndex() to locate Game in GameList */
    public String gameString() {
        return name + "\n" + system + "\n" + completion + "\n" + hours + " hours played \n" + rating + " star(s)";
    }

    /* Start Override Functions */
    @Override
    public String toString() {
        return this.name        + ", " +
                this.system     + ", " +
                this.hours      + " hour" + (this.hours == 1 ? "" : "s") + ", " +
                this.completion + ", " +
                this.rating     + " star" + (this.rating == 1 ? "" : "s");
    }

    /** Returns an array containing game information */
    public String[] toStringArray() {
        String[] stringArray = { this.name, this.system, Integer.toString(this.hours), this.completion,
                Integer.toString(this.rating) };
        return stringArray;
    }

    /** Sorts by system, then by name */
    @Override
    public int compareTo(Game game) {
        String system1 = this.system;
        String system2 = (game.system);
        int systemComp = system1.compareTo(system2);

        if (systemComp != 0) {
            return systemComp;
        } else {
            String game1 = this.name.toLowerCase();
            String game2 = (game.name.toLowerCase());
            return game1.compareTo(game2);
        }
    }
    /* End Override Functions */

    /* Start Set Functions */
    public void setId(long ID) { this.ID = ID; }
    public void setName(String name) { this.name = name; }
    public void setSystem(String system) { this.system = system; }
    public void setHours(int hours) { this.hours = hours; }
    public void setCompletion(String completion) { this.completion = completion; }
    public void setRating(int rating) { this.rating = rating; }
    public void setExcess(String excess) { this.excess = excess; }
    /* End Set Functions */

    /* Start Get Functions */
    public long getId() { return ID; }
    public String getName() { return name; }
    public String getSystem() { return system; }
    public int getHours() { return hours; }
    public String getCompletion() { return completion; }
    public int getRating() { return rating; }
    /* End Get Functions */

}
