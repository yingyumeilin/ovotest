package com.example.oto01.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import com.example.oto01.model.Constant;

public class UpLoadFile {
	private static File file;

	public static String uploadImage(String url, String imagePath,
			Map<String, String> params) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;
		try {
			MultipartEntity entity = new MultipartEntity();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
			}
			file = new File(imagePath);
			entity.addPart("file", new FileBody(file));
			post.setEntity(entity);
			
			HttpParams params1 = new BasicHttpParams();
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", TrustAllSSLSocketFactory
					.getDefault(), 443));
			ClientConnectionManager manager = new ThreadSafeClientConnManager(
					params1, registry);
			DefaultHttpClient client = new DefaultHttpClient(manager,params1 );
			response = client.execute(post);
			System.out.println("-----上传图片URL------->" + url);
			if (response.getStatusLine().getStatusCode() == 200) {
				String string = EntityUtils.toString(response.getEntity(),
						"utf-8");
				System.out.println("-------string------>" + string);
				System.out
						.println("-------Constant.UPDATA_ID_CARD--------------->"
								+ Constant.UPDATA_ID_CARD);
				if (url.equals(Constant.UPDATA_ID_CARD)) {
					return string;
				} else {
					String path = string.substring(2, string.length() - 2);
					System.out.println("-------path------>" + path);
					return path;
				}

			} else {
				System.out.println("------code----->"
						+ response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}

	private List<String> getImagesPath(String res) {
		try {
			List<String> list = new ArrayList<String>();
			JSONArray jsonArray = new JSONArray(res);
			for (int i = 0; i < jsonArray.length(); i++) {
				String string = jsonArray.getString(i);
				list.add(string);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
