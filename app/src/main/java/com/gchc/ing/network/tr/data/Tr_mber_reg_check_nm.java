package com.gchc.ing.network.tr.data;

import com.google.gson.annotations.SerializedName;
import com.gchc.ing.network.tr.BaseData;
import com.gchc.ing.network.tr.BaseUrl;
import com.gchc.ing.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 FUNCTION NAME	mber_reg_check_nm	닉네임 중복체크

 Input
 변수명	FUNCTION NAME 	설명
 api_code		api 코드명 string
 insures_code		회원사 코드
 token		디바이스 토큰
 mber_nm        이름

 json = @"{   "api_code": "mber_reg_check_nm",   "insures_code": "300", "token": "deviceToken", "mber_nm": "닉네임" }";
 Output
 변수명		설명
 api_code		api 코드명 string
 insures_code		회원사 코드
 MBER_CHECK_TYP		이름
 MBER_CHECK_YN		(중복 Y) N 일때만 (회원가입


 */

public class Tr_mber_reg_check_nm extends BaseData {
    private final String TAG = Tr_mber_reg_check_nm.class.getSimpleName();

    public static class RequestData {
       public String mber_nm; //		이름 넣기
    }

    public Tr_mber_reg_check_nm() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mber_reg_check_nm.RequestData) {
            Tr_mber_reg_check_nm.RequestData data = (Tr_mber_reg_check_nm.RequestData) obj;

            JSONObject body = new JSONObject();
            body.put("api_code", "mber_reg_check_nm");
            body.put("insures_code", INSURES_CODE);

            body.put("token", DEVICE_TOKEN);
            body.put("mber_nm", data.mber_nm);

            return body;
        }

        return super.makeJson(obj);
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("api_code")//		api 코드명 string
    public String api_code;//		api 코드명 string
    @SerializedName("insures_code")//		회원사 코드
    public String insures_code;//		회원사 코드
    @SerializedName("login_check_yn")
    public String login_check_yn;

}
