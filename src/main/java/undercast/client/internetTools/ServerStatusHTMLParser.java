package undercast.client.internetTools;

import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * @author molenzwiebel ServerStatusHTMLParser, a class which will try parse the given html to get the current map, name, players and next map. Returns: A String[][] with as first value the server (0 = alpha, 11 = nostalgia) and as second value the thing you want. 0 = Name, 1 = Players, 2 = Now playing map, 3 = Next map, 4 = Gametype So, data[0][2] will give the now playing map on Alpha and
 *         data[11][1] will give the players on Nostalgia
 */
public class ServerStatusHTMLParser {

    // Function to remove the last character is it is a space
    public static String stripLastSpace(String str) {

        if (str.length() > 0 && str.charAt(str.length() - 1) == ' ') {
            str = str.substring(0, str.length() - 1);
            return str;
        } else {
            return str;
        }
    }

    private static String compile(String str) {
        int index1, index2;
        int currentIndex = 0;
        while (str.indexOf("<div class='span4'>\n<h3>Information", currentIndex) != -1) {
            index1 = str.indexOf("<div class='span4'>\n<h3>Information", currentIndex);
            index2 = str.indexOf("</div>\n<div class='span8'>\n<div class='row'>", currentIndex);
            currentIndex = index2 + 1;
            str = str.replace(str.substring(index1, index2), "");
        }
        return str;
    }

    public static String[][] emergencyParse(String string) throws Exception {
        string = string.replace("emergency_parser@", "");
        String[][] toReturn = new String[999][5];
        int index = 0;
        for (String str : string.split("@")) {
            String[] data = str.split("\\|");
            for (int i = 0; i < data.length; i++) {
                toReturn[index][i] = data[i];
            }
            index++;
        }
        return toReturn;
    }

    public static String[][] parse(String string) throws Exception {
        if (string.startsWith("emergency_parser@"))
            return emergencyParse(string);
        String realSource = string;
        realSource = realSource.replace(realSource.substring(realSource.indexOf("<div class='tab-pane active' id='main'>"), realSource.indexOf("<div class='tab-pane' id='us'>")), "");
        String goodSource = compile(realSource);
        // Create 2 readers
        Reader HTMLReader = new StringReader(goodSource);
        Reader HTMLReader2 = new StringReader(goodSource);
        // Create 2 parsers
        ParserDelegator pd = new ParserDelegator();
        ParserDelegator pd2 = new ParserDelegator();
        // Create our own parse handlers
        Parser p = new Parser();
        NextParser p2 = new NextParser();
        // Parse
        pd.parse(HTMLReader, p, false);
        pd2.parse(HTMLReader2, p2, false);
        // Make up return values
        int c = 0;
        for (int i = 0; i < p2.mapData.length; i++) {
            if (p.mapData[i][1] != null && Integer.parseInt((String) p.mapData[i][1]) != 0) {
                p.mapData[i][3] = p2.mapData[c];
            } else {
                p.mapData[i][3] = "Unknown";
                c--;
            }
            c++;
        }
        return p.mapData;
    }
}

class Parser extends HTMLEditorKit.ParserCallback {
    // Currently in a h4 tag?
    private boolean inTD = false;
    // The number of attributes already gotten (such as name, players, map)
    private int count = 0;
    // # of map currently parsing
    private int mapCount = -1;
    // Data
    public String[][] mapData = new String[999][6];
    // The current gametype
    public String gametype;
    public String region;

    private boolean hasID(MutableAttributeSet a, String prefix) {
        return a.containsAttribute(HTML.Attribute.ID, prefix + "project-ares") || a.containsAttribute(HTML.Attribute.ID, prefix + "blitz") || a.containsAttribute(HTML.Attribute.ID, prefix + "ghost-squadron") || a.containsAttribute(HTML.Attribute.ID, prefix + "lobby");
    }

    // Function called when a tag (<tagName>) is opened
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        // If it is a tag we want, make sure to have a look at it
        if (t.equals(HTML.Tag.H4)) {
            inTD = true;
            count = 1;
            mapCount++;
            return;
        }

        if (t.equals(HTML.Tag.DIV) && (this.hasID(a, "us-") || this.hasID(a, "eu-"))) {
            gametype = ((String) a.getAttribute(HTML.Attribute.ID)).replace("eu-", "").replace("us-", "");
        }

        if (t.equals(HTML.Tag.DIV) && (a.containsAttribute(HTML.Attribute.ID, "us") || a.containsAttribute(HTML.Attribute.ID, "eu"))) {
            region = (String) a.getAttribute(HTML.Attribute.ID);
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        // Close the tag (</tagName>)
        if (t.equals(HTML.Tag.H4)) {
            inTD = false;
            count = 0;
        }

    }

    public void handleText(char[] data, int pos) {
        // Handle the text in between tags (<tag>TEXT</tag>)
        if (inTD) {
            String str = new String(data);
            if (str.contains("Lobby") || str.contains("Project Ares") || str.contains("Blitz") || str.contains("Ghost Squadron")) {
                inTD = false;
                mapCount--;
                return;
            }
            mapData[mapCount][count - 1] = ServerStatusHTMLParser.stripLastSpace(str.replace("Now: ", ""));
            mapData[mapCount][4] = gametype;
            mapData[mapCount][5] = region;
            count++;
        }
    }
}

class NextParser extends HTMLEditorKit.ParserCallback {
    // Currently in a a tag?
    private boolean inTD = false;
    // # of map currently parsing
    private int mapCount = 0;
    // Data
    public String[] mapData = new String[999];

    // Function called when a tag (<tagName>) is opened
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        // If it is a tag we want, make sure to have a look at it
        if (t.equals(HTML.Tag.A)) {
            inTD = true;
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        // Close the tag (</tagName>)
        if (t.equals(HTML.Tag.A)) {
            inTD = false;
        }
    }

    public void handleText(char[] data, int pos) {
        // Handle the text in between tags (<tag>TEXT</tag>)
        if (inTD) {
            // Make sure we only parse "Next: Map"
            if (!new String(data).contains("Next:")) {
                inTD = false;
            } else {
                // Write data
                mapData[mapCount] = new String(data).replace("Next: ", "");
                mapCount++;
            }
        }
    }
}