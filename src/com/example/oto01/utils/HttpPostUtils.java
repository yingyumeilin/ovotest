package com.example.oto01.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.lidroid.xutils.http.client.util.URIBuilder;

public class HttpPostUtils {

	/**
	 * 连接网络请求
	 */
	public static String HttpGetJson(String url, List<BasicNameValuePair> list) {
		try {
			// HttpClient client = new HttpClientHelper().getHttpClient();
			// HttpPost http = new HttpPost(url);
			// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
			// "UTF-8");
			// http.setEntity(entity);
			// HttpResponse response = client.execute(http);
			// response.setHeader("Content-type", "text/html;charset=UTF-8");
			// int code = response.getStatusLine().getStatusCode();
			// if (200 == code) {
			// HttpEntity rsentity = response.getEntity();
			// String responseBody = EntityUtils.toString(rsentity, "UTF-8")
			// .trim();
			// return responseBody;
			// } else {
			// System.out.println("==========请求错误" + code);
			// }

			HttpClient client = new HttpClientHelper().getHttpClient();
			java.net.URI uri = new URIBuilder(url)
					.setParameter("shopsid", list.get(0).getValue())
					.setParameter("authcode", list.get(1).getValue())
					.build(null);
			HttpGet httpget = new HttpGet(uri);
			HttpResponse response = client.execute(httpget);
			int code = response.getStatusLine().getStatusCode();
			if (200 == code) {
				HttpEntity rsentity = response.getEntity();
				String responseBody = EntityUtils.toString(rsentity, "UTF-8")
						.trim();
				return responseBody;
			} else {
				System.out.println("==========请求错误" + code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 连接网络请求
	 */
	public static String HttpGetJsonTongXunLu(String url,
			List<BasicNameValuePair> list) {
		try {
			HttpClient client = new HttpClientHelper().getHttpClient();
			HttpPost http = new HttpPost(url);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
					"UTF-8");
			http.setEntity(entity);
			HttpResponse response = client.execute(http);
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			int code = response.getStatusLine().getStatusCode();
			if (200 == code) {
				HttpEntity rsentity = response.getEntity();
				String responseBody = EntityUtils.toString(rsentity, "UTF-8")
						.trim();
				return responseBody;
			} else {
				System.out.println("==========请求错误" + code);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 连接网络请求
	 */
	public static String HttpGetJson(String url) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost http = new HttpPost(url);
			HttpResponse response = client.execute(http);
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			int code = response.getStatusLine().getStatusCode();
			if (200 == code) {
				HttpEntity rsentity = response.getEntity();
				String responseBody = EntityUtils.toString(rsentity, "UTF-8")
						.trim();
				return responseBody;
			} else {
				System.out.println("==========请求错误" + code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 上传图片
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public static String formUpload(String urlStr, byte[] imagefile) {
		String res = "";
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");

			OutputStream out = new DataOutputStream(conn.getOutputStream());

			// file
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(
					imagefile));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
			return "1";
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

}
