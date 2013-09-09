package FFJTest.tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import FFJTest.utils.FolderAction;
import FFJTest.utils.PathGenerator;
import FFJTest.utils.UtilTools;

import junit.framework.TestCase;

public class SelfTest extends TestCase {
	
	private static Log log = UtilTools.getLog();
	
	public void testSelf() throws IOException {
		
		String folder = "2012-11-2718-44-01";
		String path = PathGenerator.folderPathGenerator(folder, "false");
		log.info("path:" + path);
		
		Map<?,?> ret = FolderAction.getCSVFolder(path);
		log.info(ret.get("body"));
		InputStreamReader reader = new InputStreamReader((InputStream) ret.get("bodystream"));
		List l = UtilTools.csvAnalysis(reader);
		//log.info(l.toArray().toString());
		log.info("size:" + l.size());
		for(int i=0;i<l.size();i++){
			List sub = (List) l.get(i);
			log.info(sub.get(2).toString());
		}

		
		//list title
		
		
		
		/*
		String timepath1 = PathGenerator.folderPathGenerator("time", "false");
		String timepath2 = PathGenerator.folderPathGenerator("abc123", "false");
		String timepath3 = PathGenerator.folderPathGenerator("time", "share");
		String timepath4 = PathGenerator.folderPathGenerator("abc123", "share");
		

		String timepath13 = PathGenerator.fileGenerator("time", "false");
		String timepath14 = PathGenerator.fileGenerator("abc123", "false");
		String timepath15 = PathGenerator.fileGenerator("time", "share");
		String timepath16 = PathGenerator.fileGenerator("abc123", "share");
		
		log.info(timepath1  + "\n" + timepath2 + "\n" + timepath3 + "\n" + timepath4 + "\n") ;

		log.info(timepath13  + "\n" + timepath14 + "\n" + timepath15 + "\n" + timepath16 + "\n") ;
		*/
		
		assertEquals(true,true);
	}

}
