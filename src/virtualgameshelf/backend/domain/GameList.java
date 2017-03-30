package virtualgameshelf.backend.domain;

import java.util.*;

public class GameList {
    private long ID;
    private String name;

    private ArrayList<Game> games = new ArrayList<>();

    /* Start Set Functions */
    public void setId(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGame(ArrayList<Game> games) {
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

    public void addGame(String name, String system, int hours, String completion, int rating) {
        Game game = new Game();
        game.setName(name);
        game.setSystem(system);
        game.setHours(hours);
        game.setCompletion(completion);
        game.setRating(rating);

        games.add(game);
        Collections.sort(games);
    }
}
