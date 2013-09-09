package FFJTest.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

public class FileAction {
	private static Log log = UtilTools.getLog();

	// update a file
	public static Map<?,?> uploadFile(String makeFolder, String path,
			String fileName, String upType) throws IOException,
			InterruptedException {
		boolean finishUpload = false;
		Map<String, Object> upRet = new HashMap<String, Object>();
		// set block size
		int blocksize = 524288;
		// test upload file
		String realFile = path + File.separatorChar + fileName;
		log.info("realfile:" + realFile);

		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		// get filename
		String md5 = UtilTools.getFileMd5(realFile);
		int fsize = UtilTools.getFileSize(realFile);
		String size = String.valueOf(fsize);

		// set size
		UtilTools.getLog().info(
				"file filename:" + fileName + "  md5:" + md5 + " size:" + size);

		Map<String, Object> UTParams = new HashMap<String, Object>();

		// write file path
		makeFolder = makeFolder + fileName + "?size=" + size + "&md5=" + md5;
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
		UTHeader.put("x-pps-uptype", upType); // 0,1,2
		UTHeader.put("x-pps-editpermission", "1");
		UTHeader.put("Expect", "100-continue");
		UTHeader.put("Content-Type", "multipart/form-data");

		int st = 0;
		int ed = 0;
		int retryCount = 0;
		int retry503Count = 0;
		int len = 0;

		if (fsize < blocksize) {
			blocksize = fsize;
		}

		RandomAccessFile read = new RandomAccessFile(realFile, "r");
		byte[] b = new byte[blocksize];
		// len = read.read(b,st,blocksize);
		read.seek(st);
		len = read.read(b);
		boolean postok = false;
		int cblock = 0;
		double allblock = Math.ceil(fsize / blocksize);

		while (len != -1) {
			ed = st + len - 1;
			// do post
			log.info("UTFILE WHILE:" + String.valueOf(st) + "-"
					+ String.valueOf(ed));
			UTHeader.put("x-pps-file-write-range", String.valueOf(st) + "-"
					+ String.valueOf(ed));
			// compute times
			long s = System.currentTimeMillis();
			Map<String, Object> ret = UTHttpClient.requestFile("POST",
					makeFolder, b, UTParams, UTHeader);
			long e = System.currentTimeMillis() - s;
			log.info("---****************-----post block:"
					+ String.valueOf(cblock) + "/" + String.valueOf(allblock)
					+ " time:" + String.valueOf(s) + "\n");
			int code =  Integer.valueOf((String) ret.get("code"));
			//
			if (code == HttpStatus.SC_OK) {
				cblock++;
				retryCount = 0;
				log.info("-update block ok------:" + String.valueOf(st) + "-"
						+ String.valueOf(ed) + "   len:" + blocksize);
				// log.info("blocksize:" + blocksize + "  ST:" + st);
				// load next block
				if (ed + 1 == fsize) {
					// post last block then ok
					finishUpload = true;
					break;
				} else {
					// post next
					// len = read.read(b,0,blocksize);
					// if is last block
					if (fsize - (ed + 1) < blocksize) {
						blocksize = fsize - (ed + 1);
					}
					b = new byte[blocksize];
					// get next
					st = st + len;
					read.seek(st);
					len = read.read(b);
				}
			} else if (code == HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE) {
				// return header
				Map<String, String> header = (Map<String, String>) ret
						.get("header");
				st = Integer.valueOf(header.get("x-pps-offset-in-database"));
				log.info("range not  block ok:" + String.valueOf(st) + "-"
						+ String.valueOf(ed));
				if (fsize - (ed + 1) < blocksize) {
					blocksize = fsize - (ed + 1);
				}
				b = new byte[blocksize];
				read.seek(st);
				read.read(b);
				// len = read.read(b,st,blocksize);
			} else if (code == HttpStatus.SC_SERVICE_UNAVAILABLE) {
				retryCount += 1;
				retry503Count += 1;
				log.info("service unable block ok:" + String.valueOf(st) + "-"
						+ String.valueOf(ed));
				Thread.sleep(100000);
				if (retryCount > 100) {
					log.error("Server is gone, timeout");
					break;
				}
			}
			log.info("b size:" + len + " " + st + "    " + ed + "   "
					+ blocksize);
		}

		read.close();
		//return key
		upRet.put("code", finishUpload);
		upRet.put("md5",md5);
		
		return upRet;
	}

	//TODO get file to local
	public static boolean getFile(String remote, String local,String isShare) throws IOException {
		boolean ok = true;
		// get remote from server ,then download and cal md5
		String key = PathGenerator.fileGenerator(remote, isShare);
		key = key.substring(0, key.length() - 1);
		//log.info("get key:" + key);
		Map<String, Object> UTParams = new HashMap<String, Object>();
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		
		UTHeader.put("x-pps-timestamp", UtilTools.timestamp());
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-device-id", "dc0553c7a11152e8f75eaf58fa5f06f0f1cf");

		int s = 0;
		int block = 262144;  //1048576
		int times = 0;
		long maxBloc = (long) Math.ceil(1024 * 1024 * 10000 / block);
		
		while (times < maxBloc) {
			// get
			String start = String.valueOf(s);
			String end = String.valueOf(s + block);
			// send repost then get
			UTHeader.put("range", "bytes=" + start + "-" + end);

			Map<String, Object> ret = UTHttpClient.request("GET", key,
					UTParams, UTHeader);
			//log.info(ret);
			
			InputStream in = (InputStream) ret.get("bodystream");
			//
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(local);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			int ch = 0;
			try {
				while ((ch = in.read()) != -1) {
					fos.write(ch);
				}
				
				start = start + ch;
				
				//last 
				if(ch < block) {
					break;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				fos.close();
				in.close();
			}
		}
		
		return ok;
	}

	//rename file 
	public static boolean renameFile(String oldPath, String newPath,String isShare) {
		String fullOldPath = PathGenerator.fileGenerator(oldPath, isShare);
		String fullNewPath = PathGenerator.fileGenerator(newPath, isShare);
		log.info("rename File Old,New: " + fullOldPath + "  " + fullNewPath);
		boolean ok = false;
		// get remote from server ,then download and cal md5
		Map<?, ?> runtestcase = (Map<?, ?>) Settings.runtestcase;
		Map<String, Object> UTParams = new HashMap<String, Object>();
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("content-type", "appication/octet-stream");
		UTHeader.put("x-pps-modifydate", UtilTools.today());
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-pps-uptype", "1");
		UTHeader.put("x-device-id", "");
		UTHeader.put("x-pps-author", runtestcase.get("user"));
		UTHeader.put("x-pps-authorname", runtestcase.get("user"));
		UTHeader.put("x-pps-move-src", fullOldPath);
		
		Map<String, Object> ret = UTHttpClient.request("PUT", fullNewPath,
				UTParams, UTHeader);
		
		if(Integer.valueOf((String) ret.get("code")) == HttpStatus.SC_OK)
		{
			ok = true;
		}
		return ok;
	}

	//del file
	public static boolean delFile(String filename,String isShare) {
		String path = PathGenerator.fileGenerator(filename, isShare);
		boolean ok = false;
		// get remote from server ,then download and cal md5
		Map<String, Object> UTParams = new HashMap<String, Object>();
		Map<String, Object> UTHeader = new HashMap<String, Object>();
		UTHeader.put("Authorization", Settings.pps);
		UTHeader.put("x-device-id", "");
		UTHeader.put("x-pps-app-type", "file");
		
		Map<String, Object> ret = UTHttpClient.request("DELETE", path,
				UTParams, UTHeader);
		if(Integer.valueOf((String) ret.get("code")) == HttpStatus.SC_OK)
		{
			ok = true;
		}
		return ok;
	}
}
