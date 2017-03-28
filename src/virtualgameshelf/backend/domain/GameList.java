package virtualgameshelf.backend.domain;

import java.util.*;

public class GameList {
    private long ID;
    private String name;

    private String[] consoleList; // Giant list of consoles (an array based on game so you know which to grab)
    private String[] shrunkenConsoleList; // Small list of consoles (no doubles)
    private int[] consoleGames; // Count for games per console
    private List<Game> games = new ArrayList<>();

    /* Start Set Functions */
    public void setId(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGame(List<Game> games) {
        this.games = games;
    }
    /* End Set Functions */

    /* Start Get Functions */
    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public List<Game> getGame() {
        return games;
    }
    /* End Get Functions */

    public void createShrunkenList() {
        int y = 1; // y is going to be the shrunkenConsoleList size
        boolean comparison = false;
        /* for (int i = 0; i < consoleList.length(); i++) {
         *     if(i == 0) {
         *         shrunkenConsoleList[0] = consoleList[i];
         *     } else {
         *         for (int x = 0; x < y; x++) {
         *             //work here
         *         }
         *     }
         *  }
         */
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void addGame(String name, String system, int hours, String completion, int rating) {
        Game game = new Game(name, system, hours, completion, rating);
        games.add(game);
    }
}
