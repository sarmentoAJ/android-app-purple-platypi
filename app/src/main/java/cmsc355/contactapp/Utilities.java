package cmsc355.contactapp;

import android.support.v4.util.ArrayMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

class Utilities {
    static String GenerateRandomString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);
        for(int i =0; i<stringBuilder.capacity();i++) {
            stringBuilder.append((char) ('a' + random.nextInt(26)));
        }
        return stringBuilder.toString();
    }

    static ArrayMap<String, Object> JSONToMap(JSONObject json) {
        ArrayMap<String, Object> retMap = new ArrayMap<>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    private static ArrayMap<String, Object> toMap(JSONObject object) {
        ArrayMap<String, Object> map = new ArrayMap<>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = new Object();
            try {
                value = object.get(key);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private static ArrayList<Object> toList(JSONArray array) {
        ArrayList<Object> list = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            Object value = new Object();
            try {
                value = array.get(i);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    static ArrayList<String> JSONKeysToStringList(JSONObject json) {
        ArrayList<String> stringList = new ArrayList<>();
        Iterator<String> keysItr = json.keys();
        while (keysItr.hasNext()) {
            stringList.add(keysItr.next());
        }
        return stringList;
    }
}
