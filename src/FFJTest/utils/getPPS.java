package FFJTest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class getPPS {

	private static Log log = UtilTools.getLog();
	
	public static String imPPS() {
		// serverp get the server

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
			//  
			String[] tempUserKey = userKey.split(":");
			Settings.userKey = tempUserKey[0];
			retPPS = "PPS f3a16785a23b07181a23ad29b16f29c7:" + varKey + ":" + kigyoKey + ":" + userKey;
			//retPPS ="PPS f3a16785a23b07181a23ad29b16f29c7:13076026372e4935d8cd901da2472492abc33ed466:1351157433882e26e9be08d9c2bcebf7bc63965e84:135115760237a44b9bbfb185f988cc6e5fd80fe2c7"; //
			Settings.pps = retPPS;
			log.info(retPPS);
		}else{
			log.error("Login Error:" +  path + "   Code:" + ret.get("code"));
			//System.exit(-1);
		}
		return retPPS;
	}

}
