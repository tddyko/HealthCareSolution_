package com.gchc.ing.network.tr.data;

import com.google.gson.annotations.SerializedName;
import com.gchc.ing.network.tr.BaseData;
import com.gchc.ing.network.tr.BaseUrl;
import com.gchc.ing.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * FUNCTION NAME	mber_user_edit_exe	개인정보 정보수정 성별,생년,신장,체중, 목표체중
 * <p>
 * Input
 * 변수명	FUNCTION NAME 	설명
 * api_code		api 코드명 string
 * insures_code		회원사 코드
 * mber_sn		회원키값
 * mber_nm		이름
 * mber_sex		성별
 * mber_lifyea		생년(19781212)
 * mber_hp		휴대폰
 * mber_height		키
 * mber_bdwgh		몸무게
 * mber_bdwgh_goal		목표 몸무게
 * <p>
 * <p>
 * json = @"{   ""api_code"": ""mber_user_edit_exe"",   ""insures_code"": ""300"", ""mber_sn"": ""1000"", ""mber_nm"": ""닉네임_지구"", ""mber_sex"": ""1"",""mber_lifyea"": ""19750223"",""mber_height"": ""182"", ""mber_hp"": ""01085842344"",""mber_bdwgh"": ""86"",""mber_bdwgh_goal"": ""89""  }";
 * <p>
 * Output
 * 변수명		설명
 * api_code		api 코드명 string
 * insures_code		회원사 코드
 * mber_sn		회원키값
 * reg_yn		Y
 */

public class Tr_mber_user_edit_exe extends BaseData {
    private final String TAG = Tr_mber_user_edit_exe.class.getSimpleName();

    public static class RequestData {
        public String mber_sn;
        public String mber_sex;
        public String mber_lifyea;
        public String mber_height;
        public String mber_bdwgh;
        public String mber_bdwgh_goal;
    }

    public Tr_mber_user_edit_exe() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj = " + obj);
        if(obj instanceof Tr_mber_user_edit_exe.RequestData) {
            JSONObject body = new JSONObject();
            Tr_mber_user_edit_exe.RequestData data = (Tr_mber_user_edit_exe.RequestData) obj;

            body.put("api_code", getApiCode(TAG));
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn);
            body.put("mber_sex", data.mber_sex);
            body.put("mber_lifyea", data.mber_lifyea);
            body.put("mber_height", data.mber_height);
            body.put("mber_bdwgh", data.mber_bdwgh);
            body.put("mber_bdwgh_goal", data.mber_bdwgh_goal);
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
    @SerializedName("mber_sn")
    public String mber_sn;
    @SerializedName("reg_yn")
    public String reg_yn;

}
