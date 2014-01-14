package undercast.client.internetTools;

import java.io.Reader;
import java.io.StringReader;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/*
 * @author molenzwiebel
 * PlayerStatsHTMLParser,
 * a class which will try parse the given html to get the stats of a player.
 * 
 * @returns String[12]. A string array with all player data in the following order:
 * kills, deaths, friends, kdr, kkr, server joins, forum posts, forum threads, raindrops, wools placed, cores broken, monuments broken
 */
public class PlayerStatsHTMLParser {

    public static String[] parse(String string) throws Exception {
        // Create a reader
        Reader HTMLReader = new StringReader(string);
        // Create a reader
        ParserDelegator pd = new ParserDelegator();
        // Create our own parse handler
        PlayerParser p = new PlayerParser();
        NextParser p2 = new NextParser();
        // Parse
        pd.parse(HTMLReader, p, false);
        // Return data
        return p.playerData;
    }
}

class PlayerParser extends HTMLEditorKit.ParserCallback {
    // Currently in a h4 tag?

    private boolean inTD = false;
    // The number of attributes already gotten (such as name, players, map)
    private int count = 0;
    // # of map currently parsing
    // Data
    public String[] playerData = new String[12];

    // Function called when a tag (<tagName>) is opened
    @Override
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        // If it is a tag we want, make sure to have a look at it
        if (t.equals(HTML.Tag.H2)) {
            inTD = true;
        }
    }

    @Override
    public void handleEndTag(HTML.Tag t, int pos) {
        // Close the tag (</tagName>)
        if (t.equals(HTML.Tag.H2)) {
            inTD = false;
        }
    }

    @Override
    public void handleText(char[] data, int pos) {
        // Handle the text in between tags (<tag>TEXT</tag>)
        if (inTD) {
            try {
                // If it is not a integer, it will stop after this line (I
                // replace . with "" for ease, it works fine if you parse the
                // string as a double)
                Integer.parseInt(new String(data).replace(" ", "").replace(".", ""));
                // It is a integer, store the string data
                playerData[count] = new String(data).replace(" ", "");
                count++;
            } catch (Exception e) {
                // Not a number, just do nothing
            }
        }
    }
}