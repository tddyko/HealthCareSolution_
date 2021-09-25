package com.gchc.ing.network.tr.data;

import com.google.gson.annotations.SerializedName;
import com.gchc.ing.network.tr.BaseData;
import com.gchc.ing.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Tr_content_special_bbslist_search extends BaseData {
    private final String TAG = Tr_content_special_bbslist_search.class.getSimpleName();

    public static class RequestData {
        public String insures_code; // 301
        public String pageNumber;
        public String bbs_title;
    }


    public JSONArray getArray(List<Tr_get_hedctdata.DataList> datas) {
        JSONArray array = new JSONArray();
        for (Tr_get_hedctdata.DataList data : datas) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("cmpny_code", data.cmpny_code );
                obj.put("info_day", data.info_day );
                obj.put("info_title_img", data.info_title_img );
                obj.put("info_title_url", data.info_title_url );
                obj.put("info_subject", data.info_subject );

                array.put(obj);
            } catch (JSONException e) {
                Logger.e(e);
            }
        }

        return array;
    }

    public Tr_content_special_bbslist_search() {
        super.conn_url = "http://m.shealthcare.co.kr/INGSK/WebService/INGSK_Mobile_Call.asmx/INGSK_mobile_Call";
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_content_special_bbslist_search.RequestData) {
            JSONObject body = new JSONObject();

            Tr_content_special_bbslist_search.RequestData data = (Tr_content_special_bbslist_search.RequestData) obj;
            body.put("api_code", "content_special_bbslist_search"); // content_special_bbslist_search
            body.put("insures_code", data.insures_code); // 301
            body.put("pageNumber", data.pageNumber); //
            body.put("bbs_title", data.bbs_title); //
            return body;
        }

        return super.makeJson(obj);
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("api_code")
    public String api_code; //
    @SerializedName("pageNumber")
    public String pageNumber; //
    @SerializedName("maxpageNumber")
    public String maxpageNumber; //
    @SerializedName("bbslist")
    public List<Tr_get_hedctdata.DataList> bbslist; //

}
