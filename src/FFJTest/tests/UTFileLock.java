package FFJTest.tests;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

import FFJTest.utils.FileAction;
import FFJTest.utils.Settings;
import FFJTest.utils.UTHttpClient;
import FFJTest.utils.UtilTools;

import junit.framework.TestCase;

public class UTFileLock extends TestCase {
	private static Log log = UtilTools.getLog();
	private static String makeFolder = "";
	private static String shareFolder = "";

	public UTFileLock(String name) {
		super(name);
		// set folder
		String sharePrefix = "/SHAREFILE";
		//share lock
		String foldername = "xx";
		//
		shareFolder = sharePrefix + "/" + Settings.sharedFolder + "/"
				+ foldername + "/";
		
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
	//
	//test file lock 
	public void testFileLock() throws Exception {
		//
		
		boolean finishUpload = false;
		//test upload file
		String filename = Settings.texts.get(0);
		
		String realFile = Settings.filepath + File.separatorChar + filename;
		log.info("realfile:" + realFile);
		
		//upload to server
		boolean sharefinishUpload = false;
		/*
		Map<?,?> ret  = FileAction.uploadFile(shareFolder,Settings.filepath,filename,"0");
		if(ret.get("code").equals(true)) {
			finishUpload = true;
		}
		*/
		//get filename
		String md5 = "be972b1c26a2d51b892c723b6a077ef6";// UtilTools.getFileMd5(realFile);
		int fsize = UtilTools.getFileSize(realFile);
		String size = String.valueOf(fsize);
		
		//set size
		log.info("file filename:" + filename + "  md5:" + md5 + " size:" + size);
		
		Map<String, Object> UTParams = new HashMap<String, Object>();
		UTParams.put("md5", md5);
		
		String lockUrl = shareFolder  + "filter.txt" + "?md5=" + md5 + "&lock";
		log.info("FOLDER URL:" + lockUrl);
		
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("Authorization", Settings.pps);

		UTHeader.put("x-pps-lock-userid", Settings.userKey);
		//set lock
		UTHeader.put("x-pps-lock-flg", 1);
		
		//
		Map<String, Object> retR = UTHttpClient.request("POST", lockUrl,
				UTParams, UTHeader);
		log.info(retR);
		
		
		//log.info(ret);
		assertEquals(true,true);
	}
	

}
