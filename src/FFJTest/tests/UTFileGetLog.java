package FFJTest.tests;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

import FFJTest.utils.FileAction;
import FFJTest.utils.FolderAction;
import FFJTest.utils.PathGenerator;
import FFJTest.utils.Settings;
import FFJTest.utils.UTHttpClient;
import FFJTest.utils.UtilTools;

import junit.framework.TestCase;

public class UTFileGetLog extends TestCase {
	private static Log log = UtilTools.getLog();

	public UTFileGetLog(String name) {
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

	

	
	public void testGetFile() throws IOException, InterruptedException {
		
		String path =  "ec1df790-07a1-4946-a18e-e9f7908dd8eefile"; //"ec1de27f-18e1-439d-bfeb-082ec8a375bdfile";//
		
		Long start = System.currentTimeMillis();
		String local = "./out.file";
		for (int i = 0;i<999999;i++) {
			boolean ret = FileAction.getFile(path, local, "false");
			log.info("count:" + String.valueOf(i));
		}
		//log.info("Time:" + String.valueOf(System.currentTimeMillis() - start));
		
		assertEquals(true,true);
	}


}
