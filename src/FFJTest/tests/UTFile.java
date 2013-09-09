package FFJTest.tests;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

import FFJTest.utils.FileAction;
import FFJTest.utils.PathGenerator;
import FFJTest.utils.Settings;
import FFJTest.utils.UTHttpClient;
import FFJTest.utils.UtilTools;

import junit.framework.TestCase;

public class UTFile extends TestCase {
	private static Log log = UtilTools.getLog();

	public UTFile(String name) {
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

	// this is test
	// upload more files
	public void testUploadFile() throws Exception {
		
		
		boolean finishUpload = false;
		//get more 
		//set multi  uptype,share,filesize
		String filename =  Settings.texts.get(0); // Settings.files.get(5);
		String makeFolder = PathGenerator.fileGenerator("time", "false");
		//run upload file
		
		Map<?,?> ret  = FileAction.uploadFile(makeFolder,Settings.filepath,filename,"0");
		if(ret.get("code").equals(true)) {
			finishUpload = true;
		}
		
		String makeFoldershare = PathGenerator.fileGenerator("time", "share");
		//run upload file
		boolean sharefinishUpload = false;
		ret  = FileAction.uploadFile(makeFoldershare,Settings.filepath,filename,"0");
		if(ret.get("code").equals(true)) {
			sharefinishUpload = true;
		}
		assertEquals(true,finishUpload && sharefinishUpload);
	}
	
	public void testUploadShareFile() throws Exception {
		
		
		boolean finishUpload = false;
		//get more 
		//set multi  uptype,share,filesize
		String filename =  Settings.texts.get(1); // Settings.files.get(5);
		String makeFoldershare = PathGenerator.fileGenerator("time", "share");
		//run upload file
		Map<?,?> ret = FileAction.uploadFile(makeFoldershare,Settings.filepath,filename,"0");
		if(ret.get("code").equals(true)) {
			finishUpload = true;
		}
		assertEquals(true,finishUpload);
	}

	
	public void testGetFile() throws IOException, InterruptedException {
		boolean ret = false;
		
		//upload file ,then down load file 
		String filename =  Settings.texts.get(1); 
		String makeFolder = PathGenerator.fileGenerator("time", "false");
		//run upload file
		Map<?,?> upRet = FileAction.uploadFile(makeFolder,Settings.filepath,filename,"0");
		if(upRet.get("code").equals(true)) {
			//upload file
			log.info("upload file: " + makeFolder);
			//get folder ,then get the result of data
			
			
			//parse csv 
			
			//
			String tmpFile = UtilTools.tmpDir() + File.pathSeparator + String.valueOf(System.currentTimeMillis());
			log.info("download file: " + tmpFile);
			boolean getRet = FileAction.getFile(makeFolder, tmpFile, "false");
			//download file
			//get file md5
			
		}
		
		
		assertEquals(true,ret);
	}

	/*

	public void testGetFile() throws Exception {
		
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		Map<String, Object> UTParams = new HashMap<String, Object>();
		
		//url of file ,then download file
		makeFolder = "";
		
		// UTParams.put("", "");
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("Date", UtilTools.today());
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());

		Map<String, Object> ret = UTHttpClient.request("GET", makeFolder,
				UTParams, UTHeader);
		log.info(ret);
		
		//save to local then get md5 ,then check then delete
		
		
		// @TODO ADD MORE LOGIC
		// how to judge the ok
		assertEquals(HttpStatus.SC_OK, ret.get("code"));
	}

	// rename folder
	public void testRenameFile() {
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
		UTHeader.put("x-pps-folder-rename-src", makeFolder);
		
		String renameStr = makeFolder + "rename";
		
		Map<String, Object> ret = UTHttpClient.request("PUT", renameStr,
				UTParams, UTHeader);
		log.info(ret);
		
		Map<String, Object> UTHeaderGet = new HashMap<String, Object>();
		UTHeaderGet.put("Date", UtilTools.today());
		UTHeaderGet.put("Authorization", Settings.pps);
		UTHeaderGet.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());
		
		Map<String, Object> retGet = UTHttpClient.request("GET", renameStr,
				UTParams, UTHeaderGet);
		log.info(retGet);
		
		//rename ok
		makeFolder = renameStr;

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
