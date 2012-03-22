package harveyf.plex_missing_locator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentException;
import org.dom4j.Node;

public class TvdbDAOImpl implements TvdbDAO {
	private static final String API_KEY = "59BBF0D44F98A838";
	private static final String ROOT_FOLDER = "/Users/harveyf/localTvdb/";
	private boolean dbInitialized = false;
	private String mainTvdbUrl;
	private String xmlMirror;
	private String bannerMirror;
	private String zipMirror;
	private Integer serverTime;
	
	
	public TvdbDAOImpl(String mainTvdbUrl) throws Exception{
		this.mainTvdbUrl = mainTvdbUrl;
		File rootFolder = new File(ROOT_FOLDER);
		if (rootFolder.exists()) {
			this.dbInitialized  = true;
		}
		
		this.locateMirrors();
		this.setServerTime();
	}
	
	private void setServerTime() throws IOException, Exception {
		String serverTimePath = this.mainTvdbUrl + "api/Updates.php?type=none";
		
		String value = TvdbUtils.getRemoteXmlValue(serverTimePath, "//Time");
		this.serverTime = Integer.valueOf(value);
	}

	private void locateMirrors() throws Exception {
		String mirrorsFilePath = this.mainTvdbUrl + "api/" + API_KEY + "/mirrors.xml";
		URL mirrorsFilePathURL = new URL(mirrorsFilePath);
		String mirrorsXml = IOUtils.toString(mirrorsFilePathURL.openStream());
		this.xmlMirror = TvdbUtils.computeMirror(mirrorsXml, TvdbDAO.xmlMirrorMask);
		this.bannerMirror = TvdbUtils.computeMirror(mirrorsXml, TvdbDAO.bannerMirrorMask);
		this.zipMirror = TvdbUtils.computeMirror(mirrorsXml, TvdbDAO.zipMirrorMask);
		
		
	}

	@Override
	public List<String> getEpisodeList(String tvdbId) throws Exception {
		File localData = new File(computeLocalSeriesFilePath(tvdbId));
		if (!localData.exists()) this.acquireSeries(tvdbId, localData);
		File xmlFile = new File(localData, "en.xml");
		String xmlContent = IOUtils.toString(new FileReader(xmlFile));
		List<Node> episodeNodes = TvdbUtils.extractNodes(xmlContent, "//Episode");
		List<String> episodeKeys = new ArrayList<String>();
		for (Node episodeNode : episodeNodes){
			episodeKeys.add(TvdbUtils.computeEpisodeName(episodeNode.selectSingleNode("SeasonNumber").getText(), episodeNode.selectSingleNode("EpisodeNumber").getText()));
		}
		Collections.sort(episodeKeys);
		
		return episodeKeys;
	}

	private void acquireSeries(String tvdbId, File localData) throws IOException, InterruptedException {
		boolean mkdirs = localData.mkdirs();
		if (!mkdirs) throw new IOException("Couldn't create local content");
//		<mirrorpath_zip>/api/<apikey>/series/<seriesid>/all/<language>.zip
		String contentUrl = this.zipMirror + "/api/" + API_KEY + "/series/" + tvdbId + "/all/en.zip";
		URL url = new URL(contentUrl);
		File output = new File(localData, "all.zip");
		IOUtils.copy(url.openStream(), new FileOutputStream(output));
		Process process = Runtime.getRuntime().exec("unzip " + output.getAbsolutePath() + " -d " + localData.getAbsolutePath());
		process.waitFor();
		File time = new File(localData, "time.txt");
		IOUtils.write(this.serverTime + "", new FileOutputStream(time));
	}

	private static String computeLocalSeriesFilePath(String tvdbId) {
		return ROOT_FOLDER + "/series/" + tvdbId + "/";
	}

}
