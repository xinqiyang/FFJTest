package FFJTest.runner;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.log4j.PropertyConfigurator;
import org.junit.runners.Suite;

import FFJTest.utils.Settings;
import FFJTest.utils.UtilTools;
import FFJTest.utils.getPPS;

import com.esotericsoftware.yamlbeans.YamlReader;

public class DynamicSuite extends Suite {
	public static String tests = null;
	 private static final String LOG4J_CONF_FILE = "./config/log4j.properties";
	 private static Log log = UtilTools.getLog();
	static {
		
		try {
			log.info("================== init env start ====================");
			initEnv();
			log.info("================== init env end ====================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public DynamicSuite(Class<?> setupClass) throws Exception {
		super(setupClass,findTestClass());
	}
	
	public static void initEnv() throws Exception {
		PropertyConfigurator.configure(LOG4J_CONF_FILE);
		YamlReader reader = new YamlReader(new FileReader("./config/test.yml"));
		Object object = reader.read();
		Map map = (Map)object;
		Settings.map = map;
		String runTestCase = map.get("runtestcase").toString();
		@SuppressWarnings("rawtypes")
		Map testcase = (Map)map.get(runTestCase);
		tests = testcase.get("tests").toString();
		Settings.runtestcase = testcase;
		Settings.tests = tests;
		Settings.apiserver = map.get("apiserver").toString();
		
	}
	
	//findTestClass and then run Test
	public static Class[] findTestClass() throws Exception {
		if(tests.equals(null) || tests.equals("")) {
			System.out.println("Tests count is 0, please check!");
			System.exit(-1);
		}
		String[] testsList = tests.split(",");
		List<Class> classList = new ArrayList<Class>();
		//laod static code to jvm
		//@TODO: set the keepdata
		//add class to list
		log.info("Load TestCase:");
		for(String test : testsList) {
			log.info("FFJTest.tests." + test );
			classList.add(Class.forName("FFJTest.tests." + test));
		}
		//conf and run
		Class[] classes = new Class[classList.size()];
		classList.toArray(classes);
		return classes;
	}

}
