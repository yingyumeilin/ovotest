package com.example.oto01.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.util.Log;

public class HttpUtil {
	private static final int HTTP_REQUEST_TIMEOUT = 1000 * 60;
	public static boolean TIME_OUT = false;

	public static String doGet(String url) throws Exception {
		Log.d("TAG", "doGet : " + url);
		String result = null;

		HttpGet httpGet = new HttpGet(url);
		TIME_OUT = false;
		// HttpClient httpClient = new DefaultHttpClient();
		HttpParams params1 = new BasicHttpParams();
		HttpConnectionParams
				.setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", TrustAllSSLSocketFactory
				.getDefault(), 443));
		ClientConnectionManager manager = new ThreadSafeClientConnManager(
				params1, registry);
		DefaultHttpClient client = new DefaultHttpClient(manager, params1);

		// HttpParams params1 = httpClient.getParams();
		// HttpConnectionParams
		// .setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
		// HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
		HttpResponse httpResponse = client.execute(httpGet);

		int responseCode;
		try {
			responseCode = httpResponse.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("TAG", "responseCode : 4444444");
			responseCode = 404;
			TIME_OUT = true;
		}
		if (responseCode == 200) {
			result = EntityUtils.toString(httpResponse.getEntity());
		} else {
			result = responseCode + "";
		}
		return result;
	}

	// public static String doPost(String url) throws Exception {
	// String result = null;
	// HttpPost httpPost = new HttpPost(url);
	// TIME_OUT = false;
	// HttpClient httpClient = new DefaultHttpClient();
	// HttpParams params1 = httpClient.getParams();
	// HttpConnectionParams
	// .setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
	// HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
	// HttpResponse httpResponse = httpClient.execute(httpPost);
	// int responseCode;
	// try {
	// responseCode = httpResponse.getStatusLine().getStatusCode();
	// } catch (Exception e) {
	// e.printStackTrace();
	// responseCode = 404;
	// TIME_OUT = true;
	// }
	// if (responseCode == 200) {
	// result = EntityUtils.toString(httpResponse.getEntity());
	// } else {
	// result = responseCode + "";
	// }
	// return result;
	// }

	/**
	 * sample: json = "[[\"name\",\"aaa\"],[\"password\",\"bbb\"]]";
	 */
	// public static String doGet(String url, String json) throws Exception {
	// String result = null;
	// url = url + "?";
	// JSONArray jsonArray = new JSONArray(json);
	// for (int i = 0; i < jsonArray.length(); i++) {
	// JSONArray ja = (JSONArray) jsonArray.get(i);
	// String key = ja.getString(0);
	// String value = ja.getString(1);
	// url += key + "=" + value + "&";
	// }
	// url = url.substring(0, url.length() - 1);
	// HttpGet httpGet = new HttpGet(url);
	// TIME_OUT = false;
	// HttpClient httpClient = new DefaultHttpClient();
	// HttpParams params1 = httpClient.getParams();
	// HttpConnectionParams
	// .setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
	// HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
	// HttpResponse httpResponse = httpClient.execute(httpGet);
	//
	// int responseCode;
	// try {
	// responseCode = httpResponse.getStatusLine().getStatusCode();
	// } catch (Exception e) {
	// e.printStackTrace();
	// responseCode = 404;
	// TIME_OUT = true;
	// }
	// if (responseCode == 200) {
	// result = EntityUtils.toString(httpResponse.getEntity());
	// } else {
	// result = responseCode + "";
	// }
	// return result;
	// }

	// public static String doGet(String url, String usrId, String addtime)
	// throws Exception {
	// String result = null;
	// url = url + "?";
	// url = url + "admin_id=" + usrId + "&addtime=" + addtime;
	// Log.d("TAG", "------》url : " + url);
	// HttpGet httpGet = new HttpGet(url);
	// TIME_OUT = false;
	// HttpClient httpClient = new DefaultHttpClient();
	// HttpParams params1 = httpClient.getParams();
	// HttpConnectionParams
	// .setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
	// HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
	// HttpResponse httpResponse = httpClient.execute(httpGet);
	//
	// int responseCode;
	// try {
	// responseCode = httpResponse.getStatusLine().getStatusCode();
	// } catch (Exception e) {
	// e.printStackTrace();
	// responseCode = 404;
	// TIME_OUT = true;
	// }
	// if (responseCode == 200) {
	// result = EntityUtils.toString(httpResponse.getEntity());
	// } else {
	// result = responseCode + "";
	// }
	// return result;
	// }

	/**
	 * sample: json = "[[\"name\",\"aaa\"],[\"password\",\"bbb\"]]";
	 */
	public static String doPost(String url, String json) throws Exception {
		Log.d("TAG", "url : " + url);
		String result = null;
		HttpPost httpPost = new HttpPost(url);
		TIME_OUT = false;
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		JSONArray jsonArray = new JSONArray(json);

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONArray ja = (JSONArray) jsonArray.get(i);
			String key = ja.getString(0);
			String value = ja.getString(1);
			params.add(new BasicNameValuePair(key, value));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

		HttpParams params1 = new BasicHttpParams();
		HttpConnectionParams
				.setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", TrustAllSSLSocketFactory
				.getDefault(), 443));
		ClientConnectionManager manager = new ThreadSafeClientConnManager(
				params1, registry);
		DefaultHttpClient client = new DefaultHttpClient(manager, params1);

		// HttpClient httpClient = new DefaultHttpClient();
		// HttpParams params1 = httpClient.getParams();
		// HttpConnectionParams
		// .setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
		// HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
		HttpResponse httpResponse = client.execute(httpPost);

		int responseCode;
		try {
			responseCode = httpResponse.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
			responseCode = 404;
			TIME_OUT = true;
		}
		if (responseCode == 200) {
			result = EntityUtils.toString(httpResponse.getEntity());
		} else {
			result = responseCode + "";
		}
		return result;
	}

	// /**
	// * sample: json = "[[\"name\",\"aaa\"],[\"password\",\"bbb\"]]";
	// */
	// public static String doPost1(String url, String json) throws Exception {
	// Log.d("TAG", "url : " + url);
	// String result = null;
	// HttpPost httpPost = new HttpPost(url);
	// TIME_OUT = false;
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// JSONObject jsonObject = new JSONObject(json);
	// jsonObject.getJSONArray("user_arr");
	//
	// JSONArray jsonArray = new JSONArray(json);
	//
	// for (int i = 0; i < jsonArray.length(); i++) {
	// JSONArray ja = (JSONArray) jsonArray.get(i);
	// String key = ja.getString(0);
	// String value = ja.getString(1);
	// params.add(new BasicNameValuePair(key, value));
	// }
	// httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	// HttpClient httpClient = new DefaultHttpClient();
	// HttpParams params1 = httpClient.getParams();
	// HttpConnectionParams
	// .setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
	// HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
	// HttpResponse httpResponse = httpClient.execute(httpPost);
	//
	// int responseCode;
	// try {
	// responseCode = httpResponse.getStatusLine().getStatusCode();
	// } catch (Exception e) {
	// e.printStackTrace();
	// responseCode = 404;
	// TIME_OUT = true;
	// }
	// if (responseCode == 200) {
	// result = EntityUtils.toString(httpResponse.getEntity());
	// } else {
	// result = responseCode + "";
	// }
	// return result;
	// }

	public static String doPost(String url, String key, String value)
			throws Exception {
		String result = null;
		HttpPost httpPost = new HttpPost(url);
		TIME_OUT = false;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(key, value));
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

		HttpParams params1 = new BasicHttpParams();
		HttpConnectionParams
				.setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", TrustAllSSLSocketFactory
				.getDefault(), 443));
		ClientConnectionManager manager = new ThreadSafeClientConnManager(
				params1, registry);
		DefaultHttpClient client = new DefaultHttpClient(manager, params1);

		// HttpClient httpClient = new DefaultHttpClient();
		// HttpParams params1 = httpClient.getParams();
		// HttpConnectionParams
		// .setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
		// HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
		HttpResponse httpResponse = client.execute(httpPost);

		int responseCode;
		try {
			responseCode = httpResponse.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
			responseCode = 404;
			TIME_OUT = true;
		}
		if (responseCode == 200) {
			result = EntityUtils.toString(httpResponse.getEntity());
		} else {
			result = responseCode + "";
		}
		return result;
	}

	/**
	 * 从网上获取内容post方式
	 * 
	 * @throws Exception
	 */
	public static String getInfoByPost(String aUrl,
			ArrayList<BasicNameValuePair> params) {
		String result = "";

		try {
			
			// HttpParams params1 = new BasicHttpParams();
			// HttpConnectionParams
			// .setConnectionTimeout(params1, HTTP_REQUEST_TIMEOUT);
			// HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
			// SchemeRegistry registry = new SchemeRegistry();
			// registry.register(new Scheme("http", PlainSocketFactory
			// .getSocketFactory(), 80));
			// registry.register(new Scheme("https", TrustAllSSLSocketFactory
			// .getDefault(), 443));
			// ClientConnectionManager manager = new
			// ThreadSafeClientConnManager(
			// params1, registry);
			// DefaultHttpClient client = new DefaultHttpClient(manager, params1);
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpParams params1 = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params1,
					HTTP_REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params1, HTTP_REQUEST_TIMEOUT);
			HttpPost httpPost = new HttpPost(aUrl);

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			httpPost.setEntity(entity);

			HttpResponse response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				result += convertStreamToString(httpEntity.getContent());
			} else {
				result = "400";
			}
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			e.printStackTrace(); // then return String would be empty!
		}

		return result;
	}

	/**
	 * 将json转为String
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/************************ upload file **********************/
	public static byte[] postXml(String path, String xml, String encoding)
			throws Exception {
		byte[] data = xml.getBytes(encoding);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		TIME_OUT = false;
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "text/xml; charset=" + encoding);
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setConnectTimeout(5 * 1000);
		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);
		outStream.flush();
		outStream.close();
		if (conn.getResponseCode() == 200) {
			return readStream(conn.getInputStream());
		} else {
			TIME_OUT = true;
		}
		return null;
	}

	/**
	 * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功�? <FORM METHOD=POST
	 * ACTION="http://192.168.0.200:8080/ssi/fileload/test.do"
	 * enctype="multipart/form-data"> <INPUT TYPE="text" NAME="name"> <INPUT
	 * TYPE="text" NAME="id"> <input type="file" name="imagefile"/> <input
	 * type="file" name="zip"/> </FORM>
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost�?27.0.0.1这样的路径测试，
	 *            因为它会指向手机模拟器，你可以使用http://
	 *            www.baidu.com或http://192.168.1.10:8080这样的路径测�?
	 * @param params
	 *            请求参数 key为参数名,value为参数�?
	 * @param file
	 *            上传文件
	 */
	public static boolean post(String path, Map<String, String> params,
			FormFile[] files) throws Exception {
		// 数据分隔�?
		final String BOUNDARY = "---------------------------7da2137580612";
		// 数据结束标志"---------------------------7da2137580612--"
		final String endline = "--" + BOUNDARY + "--/r/n";

		// 下面两个for循环都是为了得到数据长度参数，依据表单的类型而定
		// 首先得到文件类型数据的�?长度(包括文件分割�?
		int fileDataLength = 0;
		for (FormFile uploadFile : files) {
			StringBuilder fileExplain = new StringBuilder();
			fileExplain.append("--");
			fileExplain.append(BOUNDARY);
			fileExplain.append("/r/n");
			fileExplain.append("Content-Disposition: form-data;name=\""
					+ uploadFile.getParameterName() + "\";filename=\""
					+ uploadFile.getFilname() + "\"/r/n");
			fileExplain.append("Content-Type: " + uploadFile.getContentType()
					+ "/r/n/r/n");
			fileExplain.append("/r/n");
			fileDataLength += fileExplain.length();
			if (uploadFile.getInStream() != null) {
				fileDataLength += uploadFile.getFile().length();
			} else {
				fileDataLength += uploadFile.getData().length;
			}
		}
		// 再构造文本类型参数的实体数据
		StringBuilder textEntity = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			textEntity.append("--");
			textEntity.append(BOUNDARY);
			textEntity.append("/r/n");
			textEntity.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"/r/n/r/n");
			textEntity.append(entry.getValue());
			textEntity.append("/r/n");
		}

		// 计算传输给服务器的实体数据�?长度(文本总长�?数据总长�?分隔�?
		int dataLength = textEntity.toString().getBytes().length
				+ fileDataLength + endline.getBytes().length;

		URL url = new URL(path);
		// 默认端口号其实可以不�?
		int port = url.getPort() == -1 ? 80 : url.getPort();
		// 建立�?��Socket链接
		Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
		// 获得�?��输出流（从Android流到web�?
		OutputStream outStream = socket.getOutputStream();
		// 下面完成HTTP请求头的发�?
		String requestmethod = "POST " + url.getPath() + " HTTP/1.1/r/n";
		outStream.write(requestmethod.getBytes());
		// 构建accept
		String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*/r/n";
		outStream.write(accept.getBytes());
		// 构建language
		String language = "Accept-Language: zh-CN/r/n";
		outStream.write(language.getBytes());
		// 构建contenttype
		String contenttype = "Content-Type: multipart/form-data; boundary="
				+ BOUNDARY + "/r/n";
		outStream.write(contenttype.getBytes());
		// 构建contentlength
		String contentlength = "Content-Length: " + dataLength + "/r/n";
		outStream.write(contentlength.getBytes());
		// 构建alive
		String alive = "Connection: Keep-Alive/r/n";
		outStream.write(alive.getBytes());
		// 构建host
		String host = "Host: " + url.getHost() + ":" + port + "/r/n";
		outStream.write(host.getBytes());
		// 写完HTTP请求头后根据HTTP协议再写�?��回车换行
		outStream.write("/r/n".getBytes());
		// 把所有文本类型的实体数据发�?出来
		outStream.write(textEntity.toString().getBytes());

		// 把所有文件类型的实体数据发�?出来
		for (FormFile uploadFile : files) {
			StringBuilder fileEntity = new StringBuilder();
			fileEntity.append("--");
			fileEntity.append(BOUNDARY);
			fileEntity.append("/r/n");
			fileEntity.append("Content-Disposition: form-data;name=\""
					+ uploadFile.getParameterName() + "\";filename=\""
					+ uploadFile.getFilname() + "\"/r/n");
			fileEntity.append("Content-Type: " + uploadFile.getContentType()
					+ "/r/n/r/n");
			outStream.write(fileEntity.toString().getBytes());
			// 边读边写
			if (uploadFile.getInStream() != null) {
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
					outStream.write(buffer, 0, len);
				}
				uploadFile.getInStream().close();
			} else {
				outStream.write(uploadFile.getData(), 0,
						uploadFile.getData().length);
			}
			outStream.write("/r/n".getBytes());
		}
		// 下面发�?数据结束标志，表示数据已经结�?
		outStream.write(endline.getBytes());
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		// 读取web服务器返回的数据，判断请求码是否�?00，如果不�?00，代表请求失�?
		if (reader.readLine().indexOf("200") == -1) {
			return false;
		}
		outStream.flush();
		outStream.close();
		reader.close();
		socket.close();
		return true;
	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost�?27.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.baidu.com或http://192.168.1.10:8080这样的路径测�?
	 * @param params
	 *            请求参数 key为参数名,value为参数�?
	 */
	public static boolean post(String path, Map<String, String> params,
			FormFile file) throws Exception {
		return post(path, params, new FormFile[] { file });
	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost�?27.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.baidu.com或http://192.168.1.10:8080这样的路径测�?
	 * @param params
	 *            请求参数 key为参数名,value为参数�?
	 * @param encode
	 *            编码
	 */
	public static byte[] postFromHttpClient(String path,
			Map<String, String> params, String encode) throws Exception {
		// 用于存放请求参数
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			formparams.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
				encode);
		HttpPost httppost = new HttpPost(path);
		httppost.setEntity(entity);
		// 看作是浏览器
		HttpClient httpclient = new DefaultHttpClient();
		// 发�?post请求
		HttpResponse response = httpclient.execute(httppost);
		return readStream(response.getEntity().getContent());
	}

	/**
	 * 发�?请求
	 * 
	 * @param params
	 *            请求参数 key为参数名�?value为参数�?
	 */
	public static byte[] post(String path, Map<String, String> params,
			String encode) throws Exception {
		// String params = "method=save&name="+ URLEncoder.encode("老毕",
		// "UTF-8")+ "&age=28&";//�?��发�?的参�?
		StringBuilder parambuilder = new StringBuilder("");
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				parambuilder.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), encode))
						.append("&");
			}
			parambuilder.deleteCharAt(parambuilder.length() - 1);
		}
		byte[] data = parambuilder.toString().getBytes();
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置允许对外发�?请求参数
		conn.setDoOutput(true);
		// 设置不进行缓�?
		conn.setUseCaches(false);
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("POST");
		// 下面设置http请求�?
		conn.setRequestProperty(
				"Accept",
				"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		conn.setRequestProperty("Accept-Language", "zh-CN");
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setRequestProperty("Connection", "Keep-Alive");

		// 发�?参数
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(data);// 把参数发送出�?
		outStream.flush();
		outStream.close();
		if (conn.getResponseCode() == 200) {
			return readStream(conn.getInputStream());
		}
		return null;
	}

	/**
	 * 读取�?
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	/**
	 * 从输入流读取数据
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	/**
	 * 使用JavaBean封装上传文件数据
	 * 
	 */
	public static class FormFile {
		// 上传文件的数�?
		private byte[] data;
		private InputStream inStream;
		private File file;
		// 文件名称
		private String filname;
		// 请求参数名称
		private String parameterName;
		// 内容类型
		private String contentType = "application/octet-stream";

		/**
		 * 上传小文件，把文件数据先读入内存
		 */
		public FormFile(String filname, byte[] data, String parameterName,
				String contentType) {
			this.data = data;
			this.filname = filname;
			this.parameterName = parameterName;
			if (contentType != null)
				this.contentType = contentType;
		}

		/**
		 * 上传大文件，�?��读文件数据一边上�?
		 */
		public FormFile(String filname, File file, String parameterName,
				String contentType) {
			this.filname = filname;
			this.parameterName = parameterName;
			this.file = file;
			try {
				this.inStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (contentType != null)
				this.contentType = contentType;
		}

		public File getFile() {
			return file;
		}

		public InputStream getInStream() {
			return inStream;
		}

		public byte[] getData() {
			return data;
		}

		public String getFilname() {
			return filname;
		}

		public void setFilname(String filname) {
			this.filname = filname;
		}

		public String getParameterName() {
			return parameterName;
		}

		public void setParameterName(String parameterName) {
			this.parameterName = parameterName;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
	}

	/********************* end of upload file ***********************/

	public String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			buffer = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * @return -1:文件下载出错 0:文件下载成功 1:文件已经存在
	 */
	public int downFile(String urlStr, String path, String fileName) {
		// InputStream inputStream = null;
		try {
			// FileUtils fileUtils = new FileUtils();
			//
			// if(fileUtils.isFileExist(path + fileName)){
			// return 1;
			// } else {
			// inputStream = getInputStreamFromURL(urlStr);
			// File resultFile = fileUtils.write2SDFromInput(path, fileName,
			// inputStream);
			// if(resultFile == null){
			// return -1;
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			// try {
			// inputStream.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		return 0;
	}

	/**
	 * 根据URL得到输入�?
	 */
	public InputStream getInputStreamFromURL(String urlStr) {
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(urlStr);
			urlConn = (HttpURLConnection) url.openConnection();
			inputStream = urlConn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}
}