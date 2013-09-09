package FFJTest.tests;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

import FFJTest.utils.FolderAction;
import FFJTest.utils.PathGenerator;
import FFJTest.utils.Settings;
import FFJTest.utils.UTHttpClient;
import FFJTest.utils.UtilTools;

import junit.framework.TestCase;

public class UTShareFolder extends TestCase {
	private static Log log = UtilTools.getLog();
	private static String makeFolder;

	public UTShareFolder(String name) {
		super(name);

		makeFolder = PathGenerator.folderPathGenerator("time", "share");
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
	public void testAddFolder() throws Exception {
		String path = PathGenerator.folderPathGenerator("time", "share") + "add";
		Map<String, Object> retCreate = FolderAction.addFolder(path);
		Map<String, Object> ret = FolderAction.getFolder(path);
		assertEquals(HttpStatus.SC_OK, ret.get("code"));
	}

	public void testGetFolder() throws Exception {
		String path = PathGenerator.folderPathGenerator("time", "share");
		Map<String, Object> retCreate = FolderAction.addFolder(path);
		Map<String, Object> ret = FolderAction.getFolder(path);
		assertEquals(HttpStatus.SC_OK, ret.get("code"));
	}

	// rename share folder
	public void testRenameFolder() {
		// create , reanme ,get
		String oldPath = PathGenerator.folderPathGenerator("time", "share");
		Map<String, Object> retCreate = FolderAction.addFolder(oldPath);
		String newPath = oldPath + "rename";
		Map<String, Object> ret = FolderAction.renameFolder(oldPath, newPath);
		Map<String, Object> retGet = FolderAction.getFolder(newPath);
		assertEquals(HttpStatus.SC_OK, retGet.get("code"));

	}

	// test delte
	public void testDeleteFolder() throws Exception {
		String path = PathGenerator.folderPathGenerator("time", "share");
		Map<String, Object> retCreate = FolderAction.addFolder(path);
		Map<String, Object> delete = FolderAction.delFolder(path);
		assertEquals(HttpStatus.SC_OK, delete.get("code"));
	}

}
