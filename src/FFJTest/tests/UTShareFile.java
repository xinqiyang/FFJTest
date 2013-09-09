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

public class UTShareFile extends TestCase {
	private static Log log = UtilTools.getLog();
	private static String makeFolder = "";
	private static String shareFolder = "";

	public UTShareFile(String name) {
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
	public void testUploadFile() throws Exception {
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
		UTHeader.put("Date", UtilTools.todayGMT());
		UTHeader.put("x-pps-modifydate", UtilTools.todayGMT());
		UTHeader.put("x-pps-creationdate", UtilTools.todayGMT());
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-device-id", runtestcase.get("x-device-id")
				.toString());
		UTHeader.put("x-pps-app-type", "file");
		UTHeader.put("x-pps-ownername", runtestcase.get("user").toString());
		UTHeader.put("x-pps-author", runtestcase.get("user").toString());
		UTHeader.put("x-pps-authorname", runtestcase.get("user").toString());
		UTHeader.put("x-pps-owner", runtestcase.get("user").toString());
		UTHeader.put("x-pps-uptype", "0"); // 0,1,2
		UTHeader.put("x-pps-editpermission", "1");
		UTHeader.put("Expect", "100-continue");
		UTHeader.put("Content-Type", "multipart/form-data");
		
		int st = 0;
		int ed = 0;
		int retryCount = 0;
		int retry503Count = 0;
		int len = 0;
		
		if(fsize<blocksize){
			blocksize = fsize;
		}
		
		byte[] buffer = null;
		
		File f = new File(realFile);
		FileInputStream in = null;
		in = new FileInputStream(f);
		
		
		//@TODO:
		while((len =in.read(buffer,st,blocksize)) != -1) {
			ed = st + len -1;
			log.info("UTFILE WHILE:"+ String.valueOf(st) + "-" + String.valueOf(ed));
			UTHeader.put("x-pps-file-write-range", String.valueOf(st) + "-" + String.valueOf(ed));
			//UTHeader.put("content-length", len);
			
			Map<String, Object> ret = UTHttpClient.requestFile("POST", makeFolder,buffer,
					UTParams, UTHeader);
			//post stream,
			
			int code = Integer.valueOf((String) ret.get("code"));// (int) ret.get("code");
			//
			if(code == HttpStatus.SC_OK) {
				retryCount = 0;
				log.info("update block ok:" + String.valueOf(st) + "-" + String.valueOf(ed)  + "   len:" + len);
				st += len;
				if((fsize - ed) < blocksize) {
					log.info("is small" + ed + "    bl:" + blocksize);
					blocksize = fsize - ed + 1;
				}
				log.info("blocksize:" + blocksize + "  ST:" + st);
				
			}else if(code ==  HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE){
				//return header
				Map<String,String> header = (Map<String, String>) ret.get("header");
				st = Integer.valueOf(header.get("x-pps-offset-in-database"));
				
				log.info("range not  block ok:" + String.valueOf(st) + "-" + String.valueOf(ed));
			}else if(code == HttpStatus.SC_SERVICE_UNAVAILABLE) {
				retryCount += 1;
				retry503Count +=1;
				log.info("service unable block ok:" + String.valueOf(st) + "-" + String.valueOf(ed));
			}
			//new buffer 
			buffer = new byte[blocksize];
			
			if(ed == (Integer.valueOf(size) -1)){
				finishUpload = true;
				in.close();
				log.info("upload all:" + String.valueOf(ed));
				break;
			}	
			log.info("END:" + st + " " + ed + "  " + blocksize + "   " + len);
		}
		
		//log.info(ret);
		assertEquals(true,finishUpload);
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
