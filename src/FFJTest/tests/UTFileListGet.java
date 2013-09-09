package FFJTest.tests;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

import FFJTest.utils.FolderAction;
import FFJTest.utils.PathGenerator;
import FFJTest.utils.Settings;
import FFJTest.utils.UTHttpClient;
import FFJTest.utils.UtilTools;

import junit.framework.TestCase;

public class UTFileListGet extends TestCase {
	private static Log log = UtilTools.getLog();
	private static String makeFolder = "";

	public UTFileListGet(String name) {
		super(name);
		// set folder
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

	public Long getFolderTest(String p)
	{
		Long start = System.currentTimeMillis();
		
		// 000000t1   000000t2  000000t3   
		String path = PathGenerator.folderPathGenerator(p,"false");
		//log.info("GET PATH:" + path);
		
		Map<String, Object> ret = FolderAction.getCSVFolder(path);
		//log.info("GET CSV: " + ret.get("body"));
		Long stop = System.currentTimeMillis();
		long timeUsed = (stop - start );
		log.info("-------------------timeUsed:" + p + "  " + timeUsed);
		return timeUsed;
	}

	public void testGetFolder() throws Exception {
		Long time = 0L;
		Long time01 = 0L;
		Long t = 150L;
		String p = "000000t1";//   000000t2  000000t3   ;
		String s = "000000t3";
		for(int i=0;i<t;i++){
			//ru test 
			time += getFolderTest(p);
			time01 += getFolderTest(s);
			
		}
		log.info("Average Time:" + (time/t) + "   T2:" + (time01/t));
		assertEquals(HttpStatus.SC_OK, 200);
	}
	
	
	
}
