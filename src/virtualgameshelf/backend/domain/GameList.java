package virtualgameshelf.backend.domain;

import java.util.*;

public class GameList {
    private long ID;
    private String name;

    private ArrayList<String> shrunkenConsoleList = new ArrayList<>(); // Small list of consoles (no doubles)
    private Game[][] sortedGameList; // Count for games per console
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

    public void setShrunkenConsoleList() {
    	shrunkenConsoleList.clear();
    	for(int i = 0; i < games.size(); i++) {
    		shrunkenConsoleList.add(games.get(i).getSystem());
    	}
    	Collections.sort(shrunkenConsoleList);
    	for (int i = 1; i < shrunkenConsoleList.size(); i++) {
            String a1 = shrunkenConsoleList.get(i);
            String a2 = shrunkenConsoleList.get(i-1);
            if (a1.equals(a2)) {
                shrunkenConsoleList.remove(a1);
    			i--;
            }
        }
    	setSortedGameList();
    }

    public void setSortedGameList() {
    	Collections.sort(games);
    	for (int i = 0; i < games.size(); i++) {
            String gameConsole = games.get(i).getSystem();
    		for(int y = 0; y < shrunkenConsoleList.size(); y++){
    			if (gameConsole.equals(shrunkenConsoleList.get(y))) {
    				sortedGameList[y][sortedGameList[y].length - 1] = games.get(i);
    			}
    		}
        }
    	games.clear();
    	for (int x = 0; x < shrunkenConsoleList.size(); x++) {
    		for(int y = 0; y < sortedGameList[x].length; y++) {
    			games.add(sortedGameList[x][y]);
    		}
    	}
    }

    public void addGame(String name, String system, int hours, String completion, int rating) {
        Game game = new Game();
        game.setName(name);
        game.setSystem(system);
        game.setHours(hours);
        game.setCompletion(completion);
        game.setRating(rating);

        games.add(game);
        setShrunkenConsoleList();
    }
}
