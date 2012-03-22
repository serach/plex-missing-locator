package harveyf.plex_missing_locator;

import java.io.IOException;

public class TvdbInterface {
	private TvdbDAO tvdbDao;
	
	public TvdbInterface(TvdbDAO tvdbDao) {
		this.tvdbDao = tvdbDao;
	}

	public void obtainEpisodeList(String tvdbId) throws Exception {
		this.tvdbDao.getEpisodeList(tvdbId);
		
	}

	

}
