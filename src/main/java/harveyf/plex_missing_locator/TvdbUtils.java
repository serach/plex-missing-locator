package harveyf.plex_missing_locator;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class TvdbUtils {

	public static String extractId(String someString) {
		return someString.substring(someString.indexOf(":") + 3, someString.indexOf("?"));
	}

	public static List<Node> extractNodes(String xmlContent, String xpath) throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new StringReader(xmlContent));
		
		return document.selectNodes(xpath);
		
	}

	public static String computeMirror(String xmlContent, int mask) throws Exception {
		List<Node> mirrorNodes = TvdbUtils.extractNodes(xmlContent, "//Mirror");
		List<String> mirrors = new ArrayList<String>();
		for (Node node : mirrorNodes){
			if ((Integer.valueOf(node.valueOf("typemask")) & mask) == mask){
				mirrors.add(node.valueOf("mirrorpath"));
			}
		}
		if (mirrors.isEmpty()) throw new Exception("Unable to locate mirror");
		
		return mirrors.get((int) Math.random() * mirrors.size());
	}

	public static String getRemoteXmlValue(String urlString, String xpath) throws IOException, Exception {
		URL url = new URL(urlString);
		String xml = IOUtils.toString(url.openStream());
		List<Node> nodes = extractNodes(xml, xpath);
		
		return nodes.get(0).getText();
	}

	public static String computeEpisodeName(String seasonIndex, String episodeIndex) {
		return "S" + computePaddedNumber(seasonIndex) + "E" + computePaddedNumber(episodeIndex);
	}

	public static String computePaddedNumber(String index) {
		if (index.length() < 2) return "0" + index;
		return index;
	}
}
