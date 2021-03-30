/**
 * @Title:
 * @Package com.zhaoooo.zhao
 * @Description: TODO(用一句话描述)
 * @author 邢玉娜
 * @date
 * @version V1.0
 */
package com.example.oto01.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;

/**
 * @ClassName:
 * @Description: TODO(用一句话描述这个类的作用)
 * @author: yuna
 * @date:
 * @Version: 1.0
 *
 */
public class JsonUtils {
	public static <T> String toJson(Object object, Type type)
    {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(object, type);
        return jsonStr;
    }
	public static <T> String toJson(Object object)
    {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(object);
        return jsonStr;
    }
    public static <T> T fromJson(String jsonStr, Type type)
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, type);
    }
}
