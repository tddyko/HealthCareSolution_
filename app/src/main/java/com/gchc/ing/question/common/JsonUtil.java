/**
 * Program Name : JsonUtil.java
 * Description : Samsung Wallet
 *
 * @author : 개발자이름
 * ***************************************************************
 * P R O G R A M H I S T O R Y
 * ***************************************************************
 * DATE : PROGRAMMER : CONTENT
 * 2013.04.09 : 개발자이름 : 수정내용
 */
package com.gchc.ing.question.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JsonUtil
 *
 * @author pinkred
 */
public class JsonUtil {
    /**
     * <PRE>
     * JSON String 정렬
     * </PRE>
     * <p>
     * jmkim9 2011. 12. 1. 오후 6:24:24
     *
     * @param jsonString JSON
     * @return String 결과
     */
    public static String getPretty(String jsonString) {

        final String INDENT = "    ";
        StringBuffer prettyJsonSb = new StringBuffer();

        int indentDepth = 0;
        String targetString = null;
        for (int i = 0; i < jsonString.length(); i++) {
            targetString = jsonString.substring(i, i + 1);
            if (targetString.equals("{") || targetString.equals("[")) {
                prettyJsonSb.append(targetString).append('\n');
                indentDepth++;
                for (int j = 0; j < indentDepth; j++) {
                    prettyJsonSb.append(INDENT);
                }
            } else if (targetString.equals("}") || targetString.equals("]")) {
                prettyJsonSb.append('\n');
                indentDepth--;
                for (int j = 0; j < indentDepth; j++) {
                    prettyJsonSb.append(INDENT);
                }
                prettyJsonSb.append(targetString);
            } else if (targetString.equals(",")) {
                prettyJsonSb.append(targetString);
                prettyJsonSb.append('\n');
                for (int j = 0; j < indentDepth; j++) {
                    prettyJsonSb.append(INDENT);
                }
            } else {
                prettyJsonSb.append(targetString);
            }

        }

        return prettyJsonSb.toString();

    }

    /**
     * jsonData를 JSONObject로 변환.
     * JsonObject를 Map으로 변환.
     * return Map(Json).
     **/
    public static Map<String, Object> getJSONParserData(byte[] jsonData) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String json = new String(jsonData);
            // <string></string> 문자열 빼기.
            json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
            // String to Json.
            JSONObject jsonObject = new JSONObject(json);

            if (json != JSONObject.NULL) {
                // JsonObject to Map
                resultMap = toMap(jsonObject);
            }
        } catch (Exception e) {
            CLog.w("Exception : " + e.toString());
        }
        return resultMap;
    }

    /**
     * JsonObject to Map.
     **/
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                // value가 Array형식.
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                // value가 Object형식.
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * JSONArray to List
     **/
    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
