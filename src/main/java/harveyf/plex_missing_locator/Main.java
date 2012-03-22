package harveyf.plex_missing_locator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.dom4j.DocumentException;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) throws Exception
    {
    	URL plexUrl = new URL("http://localhost:32400");
        PlexContentAccessor ca = new PlexContentAccessor(plexUrl);
        List<String> plexEpisodes = ca.listEpisodes(14798);
        TvdbDAOImpl tvdbDAOImpl = new TvdbDAOImpl("http://www.thetvdb.com/");
        List<String> fullEpisodeList = tvdbDAOImpl.getEpisodeList("76290");
        for (String episode : fullEpisodeList) {
        	if (!plexEpisodes.contains(episode)) System.out.println(episode);
        }
        
    }
}
