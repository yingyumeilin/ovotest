package com.example.oto01.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpRequestUtil {

	private final static int CONNECT_TIMEOUT = 5000;
	private final static int REPEATS = 3;
	private final static int READ_TIMEOUT = 10000;

	public static InputStream requestByXML(String path, String xml) {
		int requestCounts = 0;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		byte[] data = xml.getBytes();

		while (requestCounts < REPEATS) {
			try {
				URL url = new URL(path);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(CONNECT_TIMEOUT);
				conn.setReadTimeout(READ_TIMEOUT);
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type",
						"text/xml; charset=UTF-8");
				conn.setRequestProperty("Content-Length",
						String.valueOf(data.length));
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(data);
				outputStream.flush();
				outputStream.close();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					throw new RuntimeException();
				} else {
					inputStream = conn.getInputStream();
				}

				if (null == inputStream) {
					throw new RuntimeException();
				} else {
					requestCounts = REPEATS;
				}
			} catch (IOException e) {
				requestCounts++;
			} catch (RuntimeException e) {
				requestCounts++;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return inputStream;
	}

	public static InputStream requestByJSON(String path, String json) {
		int requestCounts = 0;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		byte[] data = json.getBytes();

		while (requestCounts < REPEATS) {
			try {
				URL url = new URL(path);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(CONNECT_TIMEOUT);
				conn.setReadTimeout(READ_TIMEOUT);
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type",
						"text/json; charset=UTF-8");
				conn.setRequestProperty("Content-Length",
						String.valueOf(data.length));
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(data);
				outputStream.flush();
				outputStream.close();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					throw new RuntimeException();
				} else {
					inputStream = conn.getInputStream();
				}

				if (null == inputStream) {
					throw new RuntimeException();
				} else {
					requestCounts = REPEATS;
				}
			} catch (IOException e) {
				requestCounts++;
			} catch (RuntimeException e) {
				requestCounts++;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return inputStream;
	}

	public static InputStream requestByParams(String path,
			Map<String, String> params) throws Exception {
		List<NameValuePair> paramsPairs = new ArrayList<NameValuePair>();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				paramsPairs.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramsPairs,
				"UTF-8");
		HttpPost httpPost = new HttpPost(path);
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return response.getEntity().getContent();
		} else {
			return null;
		}
	}

	public static InputStream getStreamFromURL(String path) {
		int requestCounts = 0;
		HttpURLConnection conn = null;
		InputStream resultStream = null;

		while (requestCounts < REPEATS) {
			try {
				URL url = new URL(path);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(CONNECT_TIMEOUT);
				System.out.println("------:" + conn.getResponseCode());
				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					throw new RuntimeException();
				} else {
					resultStream = conn.getInputStream();
				}

				if (resultStream == null) {
					throw new RuntimeException();
				} else {
					requestCounts = REPEATS;
				}
			} catch (IOException e) {
				requestCounts++;
				e.printStackTrace();
			} catch (RuntimeException e) {
				requestCounts++;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultStream;
	}

	public static boolean downloadFile(String urlPath, String filePath,
			String fileName) {
		boolean flag = false;
		int downloadedSize = 0, totalsize;
		float per;
		try {
			URL url = new URL(urlPath);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			File fileDir = new File(filePath);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			File file = new File(filePath, fileName);
			FileOutputStream fileOutput = new FileOutputStream(file);

			InputStream inputStream = urlConnection.getInputStream();
			totalsize = urlConnection.getContentLength();
			byte[] buffer = new byte[1024 * 1024];
			int bufferLength = 0;

			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				per = ((float) downloadedSize / totalsize) * 100;
				System.out.println("Total PDF File size  : "
						+ (totalsize / 1024) + " KB\n\nDownloading PDF "
						+ (int) per + "% complete");
			}
			fileOutput.close();
			flag = true;
		} catch (final Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
}