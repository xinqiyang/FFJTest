package FFJTest.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UtilTools {
	
	//get file size
	public static int getFileSize(String path) throws IOException
	{
		int size = 0;
		File f = new File(path);
		if(f.exists()) {
			FileInputStream fs = null;
			fs = new FileInputStream(f);
			size = fs.available();
		}
		return size;
	}
	
	public static String getFileMd5(String path)
	{
		String md5 = "";
		File f = new File(path);
		if(!f.isFile()) {
			return md5;
		}
		MessageDigest digest =null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(f);
			while((len = in.read(buffer,0,1024))!= -1) {
				digest.update(buffer,0,len);
			}
			in.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		BigInteger bigInt = new BigInteger(1,digest.digest());
		md5 = bigInt.toString(16);
		return md5;
	}
	
	

	// input to stream
	public static String inputStream2String(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		Log log = getLog();
		//log.info("STREAM LENGTH:" + buffer.length());
		return buffer.toString();
	}

	// string 2 input stream
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	public static Log getLog() {
		StackTraceElement callerInfo = getCallerInfo();
		String className = callerInfo.getClassName();

		return LogFactory.getLog(className);
	}

	public static StackTraceElement getCallerInfo() {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		return trace[3];
	}

	public static String md5(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	
	public static String today() {
		Date today = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String time = f.format(today);
		return time;
	}
	
	public static String todayFormat(String format) {
		if(format.isEmpty()) {
			format = "yyyy/MM/dd HH:mm:ss";
		}
		Date today = new Date();
		SimpleDateFormat f = new SimpleDateFormat(format);
		String time = f.format(today);
		return time;
	}
	
	public static String gmt2yyyyMMddHHmmss(String gmt) {
		if (gmt == null || gmt.equals("")) {
			return "";
		}

		try {
			SimpleDateFormat df = new SimpleDateFormat(
					"E, dd MMM yyyy HH:mm:ss zzz", java.util.Locale.US);
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date date = df.parse(gmt);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
					java.util.Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			return sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String todayGMT()
	{
		return yyyyMMddHHmmss2gmt(todayFormat("yyyyMMddHHmmss"));
	}

	public static String yyyyMMddHHmmss2gmt(String yyyyMMddHHmmss) {
		if (yyyyMMddHHmmss == null || yyyyMMddHHmmss.equals("")) {
			return "";
		}

		try {
			SimpleDateFormat df = new SimpleDateFormat(
					"E, dd MMM yyyy HH:mm:ss zzz", java.util.Locale.US);
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
					java.util.Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date date = sdf.parse(yyyyMMddHHmmss);
			return df.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//set settings 
	public static List<String> setSettings(String files)
	{
		List<String> list = new ArrayList<String>();
		if(!files.isEmpty()) {
			String filesArr[] = files.split(",");
			list = Arrays.asList(filesArr);
		}else{
			getLog().error("get files list error!");
		}
		return list;
	}

	public static File getFileFromBytes(byte[] b) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
        	String filepath = String.valueOf(System.currentTimeMillis());
            file = new File(filepath);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                    //rm -rf filepath
                    file.delete();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }
	
	
	public static String timestamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}
	
	
	public static String tmpDir()
	{
		return System.getProperty( "java.io.tmpdir" ); 
	}
	
	public static List<List<String>> csvAnalysis(InputStreamReader fr) throws IOException
	{
		BufferedReader br = new BufferedReader(fr);  
        String rec = null;//row 
        String str;//cell 
        List<List<String>> listFile = new ArrayList<List<String>>();  
        try {             
            while ((rec = br.readLine()) != null) {  
                Pattern pCells = Pattern  
                        .compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");  
                Matcher mCells = pCells.matcher(rec);  
                List<String> cells = new ArrayList<String>();//one row one list  
                //get all cell
                while (mCells.find()) {  
                    str = mCells.group();  
                    str = str.replaceAll(  
                            "(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");  
                    str = str.replaceAll("(?sm)(\"(\"))", "$2");  
                    cells.add(str);  
                }  
                listFile.add(cells);  
            }             
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (fr != null) {  
                fr.close();  
            }  
            if (br != null) {  
                br.close();  
            }  
        }  
        return listFile;
		
		
		
	}
	
	
	
}
