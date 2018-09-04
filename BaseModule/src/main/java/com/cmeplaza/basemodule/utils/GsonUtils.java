package com.cmeplaza.basemodule.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/3.
 */

public class GsonUtils {
    //将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        if (TextUtils.isEmpty(jsonData) || jsonData.length() < 6) {
            return null;
        }
        Gson gson = new Gson();
        T result = null;
        try {
            result = gson.fromJson(jsonData, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    // 将Json数组解析成相应的映射对象列表
    public static <T> ArrayList<T> parseJsonArrayWithGson(String jsonData,
                                                          Class<T> type) {
        if (TextUtils.isEmpty(jsonData) || jsonData.length() < 6) {
            return null;
        }
        ArrayList<T> mList = new ArrayList<>();
        JsonArray array = null;
        try {
            array = new JsonParser().parse(jsonData).getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        for (final JsonElement elem : array) {
            mList.add(new Gson().fromJson(elem, type));
        }
        return mList;
    }

    public static String parseClassToJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
