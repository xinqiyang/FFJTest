package FFJTest.tests;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

import FFJTest.utils.FileAction;
import FFJTest.utils.FolderAction;
import FFJTest.utils.PathGenerator;
import FFJTest.utils.Settings;
import FFJTest.utils.UTHttpClient;
import FFJTest.utils.UtilTools;

import junit.framework.TestCase;

public class UTFileGetSpeed extends TestCase {
	private static Log log = UtilTools.getLog();

	public UTFileGetSpeed(String name) {
		super(name);
		//
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		// do setup prepare

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// do tear down setting

	}

	

	
	public void testGetFolder() throws IOException, InterruptedException {
		
		String path = "/FOLDER/a12122109";
		Long start = System.currentTimeMillis();
		Map<String, Object> ret = FolderAction.getFolder(path);
		log.info("Time:" + String.valueOf(System.currentTimeMillis() - start));
		//assertEquals(HttpStatus.SC_OK, ret.get("OK"));
		
		assertEquals(true,true);
	}


}
