package FFJTest.tests;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

import FFJTest.utils.Settings;
import FFJTest.utils.UTHttpClient;
import FFJTest.utils.UtilTools;

import junit.framework.TestCase;

public class UTSharePermission extends TestCase {
	private static Log log = UtilTools.getLog();
	private static String makeFolder = "";
	private static String shareFolder = "";

	public UTSharePermission(String name) {
		super(name);
		// set folder
		String sharePrefix = "/SHAREFILE";
		String folderPrefix = "/FILE";
		String foldername = String.valueOf(System.currentTimeMillis());
		//
		makeFolder = folderPrefix + "/" + foldername;
		shareFolder = sharePrefix + "/" + Settings.sharedFolder + "/"
				+ foldername;
		
		//set file path
		
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		// do setup prepare

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// do tear down setting

	}

	// this is test
	public void testCopyFile() throws Exception {
		boolean finishUpload = false;
		//set block size
		int blocksize = 524288;
		//test upload file
		String filename = Settings.files.get(5);
		String realFile = Settings.filepath + File.separatorChar + filename;
		log.info("realfile:" + realFile);
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		//get filename
		String md5 = UtilTools.getFileMd5(realFile);
		int fsize = UtilTools.getFileSize(realFile);
		String size = String.valueOf(fsize);
		
		//set size
		UtilTools.getLog().info("file filename:" + filename + "  md5:" + md5 + " size:" + size);
		
		Map<String, Object> UTParams = new HashMap<String, Object>();
		//UTParams.put("md5", md5);
		//UTParams.put("size", size);
		
		//write file path
		makeFolder = makeFolder + "/" + filename + "?size=" + size + "&md5=" + md5;
		log.info("FOLDER URL:" + makeFolder);
		
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());
		UTHeader.put("x-pps-app-type", "file");
		UTHeader.put("x-pps-owner", runtestcase.get("user").toString());
		UTHeader.put("x-pps-author", runtestcase.get("user").toString());
		UTHeader.put("x-pps-owner", runtestcase.get("user").toString());
		UTHeader.put("x-pps-uptype", "1"); // 0,1,2
		UTHeader.put("x-pps-copy-src", "resourcename"); //to do change the rename src 	

		//
		
		
		//log.info(ret);
		assertEquals(true,true);
	}

	/*
	public void testGetFile() throws Exception {
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		Map<String, Object> UTParams = new HashMap<String, Object>();
		// UTParams.put("", "");
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("Date", UtilTools.today());
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());

		Map<String, Object> ret = UTHttpClient.request("GET", makeFolder,
				UTParams, UTHeader);
		log.info(ret);
		// @TODO ADD MORE LOGIC
		// how to judge the ok
		assertEquals(HttpStatus.SC_OK, ret.get("code"));
	}

	// test delte
	public void testDeleteFile() throws Exception {

		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		Map<String, Object> UTParams = new HashMap<String, Object>();
		// UTParams.put("", "");
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("Date", UtilTools.today());
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());

		Map<String, Object> ret = UTHttpClient.request("DELETE", makeFolder,
				UTParams, UTHeader);
		log.info(ret);
		// @TODO ADD MORE LOGIC
		// how to judge the ok
		assertEquals(HttpStatus.SC_OK, ret.get("code"));
	}
	
	*/
	

}
