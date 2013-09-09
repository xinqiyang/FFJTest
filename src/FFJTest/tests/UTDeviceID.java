package FFJTest.tests;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import FFJTest.utils.Settings;
import FFJTest.utils.UTHttpClient;
import FFJTest.utils.UtilTools;
import FFJTest.utils.XmlHelper;

import junit.framework.TestCase;

public class UTDeviceID extends TestCase {
	private static Log log = UtilTools.getLog();
	private static String makeFolder = "";
	private static String shareFolder = "";

	public UTDeviceID(String name) {
		super(name);
		makeFolder = "/Version";
	}

	protected void setUp() throws Exception {
		super.setUp();
		// do setup prepare

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// do tear down setting

	}
	
	public void testDevice() throws Exception {
		//
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;

		// kigyoid + account + password
		String path = "/KeepData/PacServlet/Account/"
				+ runtestcase.get("kigyo").toString() + "/"
				+ runtestcase.get("user").toString() + "/"
				+ UtilTools.md5(runtestcase.get("pwd").toString());
		// add params
		Map<String, Object> UTParams = new HashMap<String, Object>();
		// UTParams.put("", "");
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		// @TODO need change
		UTHeader.put("requestID", runtestcase.get("requestID").toString());
		UTHeader.put("x-device-id", runtestcase.get("x-device-id").toString());

		Map<String,Object> ret = new HashMap<String,Object>();
		ret = UTHttpClient.request("GET", path, UTParams, UTHeader);
		
		String retStr = ret.get("body").toString();
		String retPPS = "";
		//log.info("RETURN:" + retStr);
		if(retStr != "") { 
			Document d = XmlHelper.newDocument(retStr);
			Node firstNode = d.getFirstChild();
			String varKey = XmlHelper.getText(firstNode, "var_key");
			String kigyoKey = XmlHelper.getText(firstNode, "kigyo_key");
			String userKey = XmlHelper.getText(firstNode, "user_key");
			//cut userKey
			String[] uk = userKey.split("\n|\r");
			//log.info(uk[0].toString());
			
			userKey = uk[0].toString();
			retPPS = "PPS f3a16785a23b07181a23ad29b16f29c7:" + varKey + ":" + kigyoKey + ":" + userKey;
			
			Settings.pps = retPPS;
			log.info(retPPS);
		}else{
			log.error("Login Error:" +  path + "   Code:" + ret.get("code"));
			System.exit(-1);
		}
		
		// how to judge the ok
		assertEquals(true,true);
	}

	

}
