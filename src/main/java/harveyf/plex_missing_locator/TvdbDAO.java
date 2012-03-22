package harveyf.plex_missing_locator;

import java.io.IOException;
import java.util.List;

public interface TvdbDAO {
	public static final int xmlMirrorMask = 1;
	public static final int bannerMirrorMask = 2;
	public static final int zipMirrorMask = 4;
	

	public List<String> getEpisodeList(String tvdbId) throws IOException, Exception;

}
