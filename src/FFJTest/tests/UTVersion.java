package FFJTest.tests;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;



import FFJTest.utils.UTHttpClient;
import FFJTest.utils.UtilTools;

import junit.framework.TestCase;

public class UTVersion extends TestCase {
	
	private static Log log = UtilTools.getLog();;

	public UTVersion(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();

	}

	protected void tearDown() throws Exception {
		super.tearDown();

	}
	
	public void testGetVersion() throws Exception {
		Map<String, Object> UTParams = new HashMap<String, Object>();
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		String versionPath = "/version";
		Map<String, Object> ret = null;
		//request 100
		long num = 1;
		long start = System.currentTimeMillis();
		for(int i=0;i<num;i++) {
			
			ret = UTHttpClient.request("GET", versionPath,
				UTParams, UTHeader);
			long s = System.currentTimeMillis();
			if(s-start >= 1000) {
				start = s;
				log.info("1 second:" + String.valueOf(i));
			}
		}
		
		log.info(ret);
		assertEquals(HttpStatus.SC_OK, ret.get("code"));
	}

	

}
