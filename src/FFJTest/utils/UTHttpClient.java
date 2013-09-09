package FFJTest.utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class UTHttpClient {

	private static Log log = UtilTools.getLog();

	// return 0=>code,1=>content,2=>header
	public static Map<String, Object> request(String method, String path,
			Map<String, Object> UTParams, Map<String, Object> UTHeader) {
		Map<String, Object> _ret = new HashMap<String, Object>();
		
		path = Settings.apiserver + path;
		HttpClient httpclient = new DefaultHttpClient();
		// set timeout time
		httpclient = wrapClient(httpclient);
		HttpUriRequest httprequest;
		if (method.equals("GET")) {
			// set url
			ArrayList<BasicNameValuePair> headerParams = new ArrayList<BasicNameValuePair>();
			String headerPramStr = "";
			if (UTParams.size() > 0) {
				for (Map.Entry<String, Object> entry : UTParams.entrySet()) {
					headerParams.add(new BasicNameValuePair(entry.getKey()
							.toString(), entry.getValue().toString()));
				}

				headerPramStr = URLEncodedUtils.format(headerParams, "UTF-8");
			} else {

			}
			path = path + headerPramStr;
			httprequest = new HttpGet(path);
		} else if (method.equals("POST")) {
			MultipartEntity mentity = new MultipartEntity();
			for (Map.Entry<String, Object> entry : UTParams.entrySet()) {
				try {
					// POST file body
					mentity.addPart(
							entry.getKey().toString(),
							new StringBody(entry.getValue().toString(), Charset
									.forName("UTF-8")));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			httprequest = new HttpPost(path);
			// set post entity
			((HttpPost) httprequest).setEntity(mentity);
		} else if (method.equals("PUT")) {
			MultipartEntity entity = new MultipartEntity();
			for (Map.Entry<String, Object> entry : UTParams.entrySet()) {
				try {
					// POST file body
					entity.addPart(
							entry.getKey().toString(),
							new StringBody(entry.getValue().toString(), Charset
									.forName("UTF-8")));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			httprequest = new HttpPut(path);
			((HttpPut) httprequest).setEntity(entity);
		} else if (method.equals("DELETE")) {
			// how to use the method of the method?
			// how to use delete put params
			httprequest = new HttpDelete(path);
		} else {
			// error
			httprequest = new HttpGet(path);
		}

		// set http header
		//log.info(UTHeader.toString());
		for (Map.Entry<String, Object> entry : UTHeader.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			httprequest.setHeader(key, value);
		}

		HttpResponse response = null;
		try {
			response = httpclient.execute(httprequest);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// log.info(response);
		if (response != null) {
			// get response header
			Header headers[] = response.getAllHeaders();

			// log.info(headers);

			int i = 0;
			Map<String, String> hmap = new HashMap<String, String>();
			while (i < headers.length) {
				hmap.put(headers[i].getName().toString(), headers[i].getValue()
						.toString());
				i++;
			}

			// log.info(response.getStatusLine().getStatusCode());
			_ret.put("code", response.getStatusLine().getStatusCode());
			_ret.put("header", hmap);
			
			// get response status
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// get response content
				try {
					HttpEntity entity = response.getEntity();
					/*
					 * byte[] bytes = EntityUtils.toByteArray(entity); String
					 * charset = ""; charset =
					 * EntityUtils.getContentCharSet(entity);
					 */
					InputStream instream = entity.getContent();
					
					_ret.put("code", HttpStatus.SC_OK);
					_ret.put("body", UtilTools.inputStream2String(instream));
					_ret.put("bodystream", instream);
					
					// out charset
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				_ret.put("body", "");
			}

		}
		// close the connection
		httpclient.getConnectionManager().shutdown();
		return _ret;
	}

	@SuppressWarnings("null")
	public static Map<String, Object> requestFile(String method, String path,
			byte[] body, Map<String, Object> UTParams,
			Map<String, Object> UTHeader) {
		Map<String, Object> _ret = new HashMap<String, Object>();
		// set path
		path = Settings.apiserver + path;
		// http get
		HttpClient httpclient = new DefaultHttpClient();
		// set timeout time
		httpclient = wrapClient(httpclient);
		HttpUriRequest httprequest;

 
		log.info("body length:" + body.length);
		// set file body
		FileBody bin = new FileBody(UtilTools.getFileFromBytes(body));
		
		MultipartEntity entity = new MultipartEntity();
		for (Map.Entry<String, Object> entry : UTParams.entrySet()) {
			try {
				// POST file body
				entity.addPart(entry.getKey().toString(), new StringBody(entry
						.getValue().toString(), Charset.forName("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		ByteArrayEntity baentity = null;
		if(body !=null)
		{
			baentity = new ByteArrayEntity(body);
		}
		
		//@TODO:have a bug.
		//entity.addPart("",bin);
				
		log.info("request file:" + path);
		// set body
		if (method.equals("POST")) {
			httprequest = new HttpPost(path);
		} else {
			httprequest = new HttpPut(path);
		}
		// set post entity
		((HttpPost) httprequest).setEntity(entity);
		//((HttpPost) httprequest).setEntity(fileEntity);
		((HttpPost) httprequest).setEntity(baentity);
		
		// set http header
		for (Map.Entry<String, Object> entry : UTHeader.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			httprequest.setHeader(key, value);
		}

		HttpResponse response = null;
		try {
			response = httpclient.execute(httprequest);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// log.info(response);
		if (response != null) {
			// get response header
			Header headers[] = response.getAllHeaders();

			// log.info(headers);

			int i = 0;
			Map<String, String> hmap = new HashMap<String, String>();
			while (i < headers.length) {
				hmap.put(headers[i].getName().toString(), headers[i].getValue()
						.toString());
				i++;
			}

			log.info(response.getStatusLine().getStatusCode());
			_ret.put("code", response.getStatusLine().getStatusCode());
			_ret.put("header", hmap);
			// get response status
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// get response content
				try {
					HttpEntity hentity = response.getEntity();
					/*
					 * byte[] bytes = EntityUtils.toByteArray(entity); String
					 * charset = ""; charset =
					 * EntityUtils.getContentCharSet(entity);
					 */
					InputStream instream = hentity.getContent();
					_ret.put("code", HttpStatus.SC_OK);
					_ret.put("body", UtilTools.inputStream2String(instream));
					// out charset
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				_ret.put("body", "");
			}

		}
		// close the connection
		httpclient.getConnectionManager().shutdown();
		return _ret;
	}

	public static HttpClient wrapClient(HttpClient base) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = base.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return new DefaultHttpClient(ccm, base.getParams());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
