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

	/* DOM vs SAX
	 *
	 * DOM (Document Object Model) Parsing
	 *  - loads whole XML document in memory
	 *  - is slower the larger the XML file
	 *
	 *
	 * SAX (Simple API for XML) Parsing
	 *  - loads small part of the XML file in memory
	 *  - faster then DOM for large XML file cause requires less memory
	 *  - better for unknown file size
	 *
	 * DOM winner
	 */

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

            // System.out.println("Root element :" + doc.getDocumentElement().getNodeName()); //Check to make sure of correct RootElement

            NodeList nList = doc.getElementsByTagName("game");

            // System.out.println("----------------------------"); // Used to Differentiate the stuff below and the stuff above. Not needed

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String name = "";
                    name = (eElement.getElementsByTagName("name").item(0).getTextContent());
                    //System.out.println(name); //Used for finding which game causes the break
                    /*
                     * This is apparently Unneeded
                     * name = name.substring(10, name.length() - 4);
                    */

                    int hours = 0;
                    if(eElement.getElementsByTagName("hoursOnRecord").getLength() > 0){
                    	String stringHours = (eElement.getElementsByTagName("hoursOnRecord").item(0).getTextContent());
                    	//System.out.println(stringHours); //Used for finding which games hours cause the break

                    	//Checking to see if the hours is going to have a comma in it and if so, removes the comma
                    	if(stringHours.length() >= 7)
                    	{
                    		stringHours = stringHours.substring(0,stringHours.indexOf(",")) + stringHours.substring(stringHours.indexOf(",")+1);
                    		//System.out.println(stringHours); //Used to test if the comma was correctly removed
                    	}
                        hours = (int) Math.round(Double.parseDouble(stringHours));
                    }

                    String completion = "Unfinished";
                    if (hours == 0) {
                        completion = "Unplayed";
                    }

                   //Makes sure we are actually creating a game that has a name.
                    if (!(name.equals(""))) {
                        Game game = new Game();
                        game.setName(name);
                        game.setSystem("Steam");
                        game.setHours(hours);
                        game.setCompletion(completion);
                        game.setRating(0);

                        //For Duplicate checking
                        boolean duplicate = false;
                        for (Game g : GameShelf.gameList.getGameList()) {
                            duplicate = g.compare(game);
                            if (duplicate == true) {
                                break;
                            }
                        }
                        if (duplicate == false) {
                            GameShelf.gameList.addGame(game);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GameShelf.refreshGameList();
    }
}
