package com.example.oto01.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class StreamUtil {

	public static InputStream stringToInputStream(String content) {
		if (content == null || content.equals("")) {
			return null;
		}
		return new ByteArrayInputStream(content.getBytes());
	}

	public static String inputStreamToString(InputStream inputStream) {
		if (inputStream == null) {
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stringBuilder.toString();
	}

	public static byte[] inputStreamToBytes(InputStream inputStream)
			throws Exception {
		if (inputStream == null) {
			return null;
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		os.close();
		inputStream.close();
		return os.toByteArray();
	}
}