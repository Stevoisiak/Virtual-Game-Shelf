package virtualgameshelf.backend.persistance;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import virtualgameshelf.backend.domain.Game;
import virtualgameshelf.gui.GameShelf;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.net.URL;

/*
 * XML DOM example
 * https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
 */

public class SteamCommunityGameImporter {
    /**
     * Imports games from Steam community profile. Profile must be set to public
     * in 'My Privacy Settings'.
     * <p>
     * Games are retrieved via an XML file located by appending '&xml=1' to the
     * URL for a profile's gamelist.
     *
     * @param id
     *            Steam profile ID. (ie: https://steamcommunity.com/id/[ID])
     */

    // TODO: Will not work for accounts that have not set up a profile ID.
    //       For example, https://steamcommunity.com/profiles/76561198043604940

    // Steven: Possibly use https://github.com/xPaw/SteamID.php to convert
    //         between URL types? The conversion could happen in the GUI level, so this
    //         file just receives the profile ID

    public void steamCommunityAddGames(String id) {
        try {
            String url = "http://steamcommunity.com/id/" + id + "/games/?tab=all&xml=1";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new URL(url).openStream());

            // optional, but recommended
            // read this -
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("game");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String name = "";
                    name = (eElement.getElementsByTagName("name").item(0).getTextContent());
                    //System.out.println(name);
                    /*
                     * This is apparently Unneeded
                     * name = name.substring(10, name.length() - 4);
                    */

                    int hours = 0;
                    if(eElement.getElementsByTagName("hoursOnRecord").getLength() > 0){
                    	String stringHours = (eElement.getElementsByTagName("hoursOnRecord").item(0).getTextContent());
                    	// System.out.println(stringHours);
                        hours = (int) Math.round(Double.parseDouble(stringHours));
                    }

                    String completion = "Unfinished";
                    if (hours == 0) {
                        completion = "Unplayed";
                    }

                    Game game = new Game();
                    game.setName(name);
                    game.setSystem("PC");
                    game.setHours(hours);
                    game.setCompletion(completion);
                    game.setRating(0);
                    GameShelf.gameList.addGame(game);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GameShelf.refreshGameList();
    }
}
