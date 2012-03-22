package harveyf.plex_missing_locator;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class PlexContentAccessor {

	private URL plexUrl;
	private TvdbInterface tvdb;

	public PlexContentAccessor(URL plexUrl) {
		this.plexUrl = plexUrl;
	}

	public void listSeries() throws Exception {
		String listSeriesUrl = "/library/sections/2/all";
		List<Node> directoryNodes = this.getNodes(listSeriesUrl, "//Directory");
		
		for (Node node : directoryNodes){
			System.out.println(node.valueOf("@title"));
		}
	}

	public List<String> listEpisodes(int i) throws Exception {
		String seasonsUrl = "/library/metadata/" + i + "/children";
		String xpath = "//Directory";
		List<String> episodes = new ArrayList<String>();
		List<Node> seasonNodes = this.getNodes(seasonsUrl, xpath);
		for (Node seasonNode : seasonNodes){
			String key = seasonNode.valueOf("@key");
			List<Node> episodeNodes = this.getNodes(key, "//Video");
			for (Node episodeNode : episodeNodes){
				episodes.add(TvdbUtils.computeEpisodeName(seasonNode.valueOf("@index"), episodeNode.valueOf("@index")));
			}
		}
		Collections.sort(episodes);
		return episodes;
	}


	private List<Node> getNodes(String path, String xpath) throws Exception {
		URL url = new URL(this.plexUrl, path);		
		String xmlContent = IOUtils.toString(url.openStream());
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new StringReader(xmlContent));
		
		return document.selectNodes(xpath);
	}
// 59BBF0D44F98A838
	public void locateMissingEpisodes(int seriesId) throws Exception {
		String seriesUrl = "/library/metadata/" + seriesId + "/";
		List<Node> dbNodes = this.getNodes(seriesUrl, "//@guid");
		Node dbNode = dbNodes.get(0);
		String tvdbId = TvdbUtils.extractId(dbNode.getText());
		
		
		this.tvdb.obtainEpisodeList(tvdbId);
		
	}

}
