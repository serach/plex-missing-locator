package harveyf.plex_missing_locator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.junit.Test;

public class TvdbUtilsTest {

	@Test
	public void testIdExtractor(){
		String someString = "com.plexapp.agents.thetvdb://76290?lang=en";
		String extractedId = TvdbUtils.extractId(someString);
		assertEquals("76290", extractedId);
	}
	
	
	@Test
	public void testXmlMask() throws Exception{
		String xmlInput = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Mirrors>" +
"<Mirror>" +
"<id>1</id>" +
"<mirrorpath>http://thetvdb.com</mirrorpath>" +
"<typemask>7</typemask>" +
"</Mirror>" +
"</Mirrors>";
		String computedMirror = TvdbUtils.computeMirror(xmlInput, TvdbDAO.xmlMirrorMask);
		assertEquals("http://thetvdb.com", computedMirror);

	}
	
	@Test
	public void testZipMask() throws Exception{
		String xmlInput = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Mirrors>" +
"<Mirror>" +
"<id>1</id>" +
"<mirrorpath>http://thetvdb.com</mirrorpath>" +
"<typemask>7</typemask>" +
"</Mirror>" +
"</Mirrors>";
		String computedMirror = TvdbUtils.computeMirror(xmlInput, TvdbDAO.zipMirrorMask);
		assertEquals("http://thetvdb.com", computedMirror);

	}
	
	@Test
	public void testXmlMaskOnlyAvaillable() throws Exception{
		String xmlInput = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Mirrors>" +
"<Mirror>" +
"<id>1</id>" +
"<mirrorpath>http://thetvdb.com</mirrorpath>" +
"<typemask>1</typemask>" +
"</Mirror>" +
"</Mirrors>";
		String computedMirror = TvdbUtils.computeMirror(xmlInput, TvdbDAO.xmlMirrorMask);
		assertEquals("http://thetvdb.com", computedMirror);

	}
	
	@Test
	public void testZipMaskXmlOnlyAvaillable() {
		String xmlInput = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Mirrors>" +
"<Mirror>" +
"<id>1</id>" +
"<mirrorpath>http://thetvdb.com</mirrorpath>" +
"<typemask>1</typemask>" +
"</Mirror>" +
"</Mirrors>";
		try {
			String computedMirror = TvdbUtils.computeMirror(xmlInput, TvdbDAO.zipMirrorMask);
			fail();
		} catch (Exception e) {
			
		}
	}
	@Test
	public void testXmlMasOnMany() throws Exception{
		String xmlInput = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Mirrors>" +
				"<Mirror>" +
				"<id>1</id>" +
				"<mirrorpath>http://thetvdb1.com</mirrorpath>" +
				"<typemask>1</typemask>" +
				"</Mirror>" +
				"<Mirror>" +
				"<id>1</id>" +
				"<mirrorpath>http://thetvdb2.com</mirrorpath>" +
				"<typemask>1</typemask>" +
				"</Mirror>" +
"</Mirrors>";
		String computedMirror = TvdbUtils.computeMirror(xmlInput, TvdbDAO.xmlMirrorMask);
		List<String> validUrls = new ArrayList<String>();
		validUrls.add("http://thetvdb1.com");
		validUrls.add("http://thetvdb2.com");
		assertTrue(validUrls.contains(computedMirror));

	}
}
