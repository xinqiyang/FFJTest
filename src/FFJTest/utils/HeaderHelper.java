package FFJTest.utils;

import java.util.HashMap;
import java.util.Map;

public class HeaderHelper {
	
	//load local set of head
	public static Map setHead(String key,String value)
	{
		Map _map = new HashMap<String,String>();
		_map.put(key, value);
		return _map;
	}
	
	public static String getHead()
	{
		
		return "";
	}
		
	
	
}
