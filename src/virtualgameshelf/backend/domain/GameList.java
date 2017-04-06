package virtualgameshelf.backend.domain;

import java.util.*;

public class GameList {
    private long ID;
    private String name;
    private ArrayList<Game> gameList = new ArrayList<>();

    public void addGame(Game game) {
        gameList.add(game);
        Collections.sort(gameList);
    }

    public void addGame(String name, String system, int hours, String completion, int rating) {
        Game game = new Game(name, system, hours, completion, rating);
        gameList.add(game);
        Collections.sort(gameList);
    }

    /* Start Set Functions */
    public void setId(long ID) { this.ID = ID; }
    public void setName(String name) { this.name = name; }
    public void setGameList(ArrayList<Game> gameList) { this.gameList = gameList; }
    /* End Set Functions */

    /* Start Get Functions */
    public long getID() { return ID; }
    public String getName() { return name; }
    public ArrayList<Game> getGameList() { return gameList; }
    /* End Get Functions */
}
