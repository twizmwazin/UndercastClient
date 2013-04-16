package tc.oc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 MatchLoaderThread by molenzwiebel
 This class will take a url and load the contents in an
 other thread.
 A link that might be interesting to fetch:
 http://api.thijsmolendijk.nl/mod/servers.php -> Server:Players:Current map:Next map
 And player stats:
 http://api.thijsmolendijk.nl/user/parse.php?para1=<user> -> YAML with player data (I can change it to a different format, just poke me)
 */

class MatchLoaderThread extends Thread {
	private URL urlToLoad;
    private String contents;
	MatchLoaderThread(URL url) {  // constructor starts the thread.
		urlToLoad = url;
		this.panel = gui;
		start();
	}
	public void run() {
		InputStream in = null;
		try {
			URLConnection connection = urlToLoad.openConnection();
			in = connection.getInputStream();
            //Try to get the charset
			Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
			Matcher m = p.matcher(connection.getContentType());
            //If the charset is not found, use ISO-8859-1
			String charset = m.matches() ? m.group(1) : "ISO-8859-1";
			Reader r = new InputStreamReader(in, charset);
			StringBuilder buf = new StringBuilder();
            //Read the contents
			while (true) {
				int ch = r.read();
				if (ch < 0)
					break;
				buf.append((char) ch);
			}
			contents = buf.toString();
			//contents is now the content of the URL
			in.close();
			in = null;
		}
		catch (Exception e) {
			//Do something here, such as saying "Could not fetch server status" in a gui
		}
		finally {
			//Do something to let a class know that the fetching is done,
            //in a gui this could be: gui.updateScreen().
			if (in != null) {
				try {
					in.close();
				}
				catch (Exception e) {
				}
			}
		}
	}
    
    public void getContents() {
        return this.contents;
    }
}