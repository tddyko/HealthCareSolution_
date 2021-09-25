package com.gchc.ing.question.common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * RequestUtil
 * 서버에 전달할 parameter.
 */
public class RequestUtil {

    public static Map<String, JSONObject> getJSONParameter(JSONObject jObject, String docno) {
        HashMap<String, JSONObject> map = new HashMap<>();
        try {
            jObject.put("DOCNO", docno);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        map.put("strJson", jObject);
        return map;
    }
}
