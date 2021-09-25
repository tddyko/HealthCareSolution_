package com.gchc.ing.network.tr.data;

import com.google.gson.annotations.SerializedName;
import com.gchc.ing.network.tr.BaseData;
import com.gchc.ing.network.tr.BaseUrl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 FUNCTION NAME	logincheck	로그인인증 키값 가져가기

 Input
 변수명	FUNCTION NAME 	설명
 api_code	 	api 코드명 string
 app_code	 	iOS, android
 insures_code	 	회원사 코드
 token	 	디바이스 토큰

 json = @"{""api_code"":""mber_check"",""app_code"":""android"",""insures_code"":""301"",""mber_nm"":""홍태진"",""mber_lifyea"":""19851225"",""mber_hp"":""01085842254"",""mber_nation"":""1"",""mber_sex"":""1"",""token"":""APA91bHCUpphD7XglAYaBx6YXkUwMvVxydBB2pNMSg_z-N13kQ4_1TObbhHt-Aoju6_YqguAQHKQQ2IGxFOgQODYGhkSuBxY7QSQtvm3hm_05lyGl7tnEmTnyQEUBWiF8KRErkoQ3BN8"",""phone_model"":""SM-N910S""}";

 Output
 변수명	 	설명

 api_code       api코드명 String
 insure_code    회원사 코드
 mber_no        ING 회원번호
 mber_id        회원 이메일
 data_yn        ING회원 여부
 */

public class Tr_mber_check extends BaseData {
    private static String TAG = Tr_mber_check.class.getSimpleName();

    public static class RequestData{
        public String mber_nm;
        public String mber_sex;
        public String mber_lifyea;
        public String mber_hp;
        public String mber_nation = "1";
        public String phone_model;
    }

    public Tr_mber_check() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        if(obj instanceof RequestData) {
            JSONObject body = new JSONObject();
            RequestData data = (RequestData)obj;

            body.put("api_code", "mber_check");
            body.put("app_code", APP_CODE);
            body.put("insures_code", INSURES_CODE);
            body.put("mber_nm", data.mber_nm);
            body.put("mber_sex", data.mber_sex);
            body.put("mber_lifyea", data.mber_lifyea);
            body.put("mber_hp", data.mber_hp);
            body.put("mber_nation", data.mber_nation);
            body.put("token", DEVICE_TOKEN);
            body.put("phone_model", data.phone_model);
            return body;
        }
        return super.makeJson(obj);
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/
    @SerializedName("api_code")
    public String api_code;
    @SerializedName("insures_code")
    public String insures_code;
    @SerializedName("mber_no")
    public String mber_no;
    @SerializedName("mber_id")
    public String mber_id;
    @SerializedName("data_yn")
    public String data_yn;
}
