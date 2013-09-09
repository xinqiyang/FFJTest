package FFJTest.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

public class FolderAction {

	private static Log log = UtilTools.getLog();

	// add Folder paths
	public static Map<String, Object> addFolder(String folderPath) {
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		Map<String, Object> UTParams = new HashMap<String, Object>();
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("Date", UtilTools.today());
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());

		Map<String, Object> ret = UTHttpClient.request("POST", folderPath,
				UTParams, UTHeader);
		log.info(ret);
		return ret;
	}

	// add Folder paths
	public static Map<String, Object> renameFolder(String oldPath, String newPath) {
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		Map<String, Object> UTParams = new HashMap<String, Object>();
		// UTParams.put("", "");
		Map<String, Object> UTHeader = new HashMap<String, Object>();

		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());
		UTHeader.put("x-pps-owner", runtestcase.get("user"));
		UTHeader.put("x-pps-uptype", "1");
		UTHeader.put("x-pps-author", runtestcase.get("user"));
		UTHeader.put("x-pps-folder-rename-src", oldPath);
		Map<String, Object> ret = UTHttpClient.request("PUT", newPath,
				UTParams, UTHeader);
		log.info(ret);
		// rename ok
		return ret;
	}

	// add Folder paths
	public static Map<String, Object> delFolder(String folderPath) {
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		Map<String, Object> UTParams = new HashMap<String, Object>();
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("Date", UtilTools.today());
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());

		Map<String, Object> ret = UTHttpClient.request("DELETE", folderPath,
				UTParams, UTHeader);
		return ret;
	}

	// get Folder paths
	public static Map<String, Object> getFolder(String folderPath) {
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		Map<String, Object> UTParams = new HashMap<String, Object>();
		// UTParams.put("", "");
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("Date", UtilTools.today());
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());

		Map<String, Object> ret = UTHttpClient.request("GET", folderPath,
				UTParams, UTHeader);
		//log.info(ret);
		return ret;
	}
	
	public static Map<String, Object> getCSVFolder(String folderPath) {
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		Map<String, Object> UTParams = new HashMap<String, Object>();
		// UTParams.put("", "");
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("Date", UtilTools.today());
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());
		folderPath = folderPath + "?output=csv";
		Map<String, Object> ret = UTHttpClient.request("GET", folderPath,
				UTParams, UTHeader);
		//log.info(ret);
		return ret;
	}
	
}
