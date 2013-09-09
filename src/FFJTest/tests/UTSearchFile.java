package FFJTest.tests;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;


import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

import FFJTest.utils.Settings;
import FFJTest.utils.UTHttpClient;
import FFJTest.utils.UtilTools;

public class UTSearchFile extends TestCase {

	private static Log log = UtilTools.getLog();
	private static String makeFolder = "";

	public UTSearchFile(String name) {
		super(name);
		//set path url
		makeFolder = "/Share/SEARCH/";
	}

	protected void setUp() throws Exception {
		super.setUp();
		// do setup prepare

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// do tear down setting

	}
	
	public void testSearchFile() throws Exception {
		
		//filename 
		//get test ext
		
		
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		Map<String, Object> UTParams = new HashMap<String, Object>();
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("x-pps-owner", runtestcase.get("user").toString());
		UTHeader.put("x-pps-uptype", "1");
		UTHeader.put("x-pps-author", runtestcase.get("user").toString());
		UTHeader.put("Authorization", Settings.pps);
		Map<String, Object> ret = UTHttpClient.request("GET", makeFolder,
				UTParams, UTHeader);
		log.info(ret);
		// how to judge the ok
		assertEquals(HttpStatus.SC_OK, ret.get("code"));
	}

	
}
