package com.example.oto01.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @Package:com.noway.net
 * @ClassName:MD5Utils
 * @Description:TODO（描述类的用途）
 * @author noway <br>
 *         <a href="www.z3a105.com">中三A105工作室</a><br>
 *         Email:<a href="mailto:meiw@z3a105.com">meiw@z3a105.com </a> <br>
 *         QQ:122320466 技术交流QQ群：144585591
 * @date:2014年1月22日下午11:56:37
 */
public class MD5Utils {
	/**
	 * 
	 * @Title:getMD5Str
	 * @Description:TODO
	 * @param @param str
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getMD5Str(String str) {
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
}
