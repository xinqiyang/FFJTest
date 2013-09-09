package FFJTest.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PathGenerator {

	static String[] pathType = new String[] { "time","value"};
	static String shareFilePrefix = "/SHAREFILE";
	static String filePrefix = "/FILE";
	//
	static String shareFolderPrefix = "/SHAREFOLDER";
	static String folderPrefix = "/FOLDER";
	
	//path generator
	public static String fileGenerator(String type,String isShare)
	{
		String path = "";
		String prefix = "";
		List list = Arrays.asList(pathType);
		
		if(list.contains(type)) {
			if(type == "time") {
				prefix = String.valueOf(System.currentTimeMillis());
			}
		}else{
			prefix = type;
		}
		
		if(isShare == "share") {
			Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
			path = shareFilePrefix + File.separatorChar + runtestcase.get("sharedfolder") + File.separatorChar + prefix;
		}else{
			path = filePrefix + File.separatorChar  + prefix;
		}
		return path +  File.separatorChar;
	}
	
	
	public static String folderPathGenerator(String type,String isShare)
	{
		String path = "";
		String prefix = "";
		List list = Arrays.asList(pathType);
		
		if(list.contains(type)) {
			if(type == "time") {
				prefix =  String.valueOf(System.currentTimeMillis());
			}
		}else{
			prefix  =  type;
		}
		
		if(isShare == "share") {
			Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
			
			path = shareFolderPrefix + File.separatorChar + runtestcase.get("sharedfolder") + File.separatorChar + prefix;
		}else{
			path = folderPrefix + File.separatorChar  + prefix;
		}
		return path;
	}
	
	
	
}
