package com.gchc.ing.network.tr.data;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.gchc.ing.network.tr.BaseData;
import com.gchc.ing.network.tr.BaseUrl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 FUNCTION NAME	login	로그인

 Input
 변수명	FUNCTION NAME 	설명
 api_code		api 코드명 string
 app_code		iOS, android
 insures_code		회원사 코드
 token		디바이스 토큰
 mber_id		회원아이디
 mber_pwd		회원 비밀번호
 app_code		os 버전
 phone_model		폰 모델명(SM-N9105
 pushk
 app_ver		앱 버전



 json = @"{   ""api_code"": ""login"",  ""insures_code"": ""300"", ""token"": ""APA91bHanHsaue_chJqab7tKn04XSjGrr4JvyvyrHoB2uZCx9eRY54aCrk14L0MfTx1DhSbgaUYaWGYfoBnPqO7aJSRA-xdU2gEgprAWRxraSI7cDLEVnAqyXnrZpYigAE9OmSNSPnkK9lI0zjNQZhQgwn3uDgpLRYh8mM9uHq1FLOfYhYNhA1E"", ""mber_id"": ""tjhong@naver.com"" , ""mber_pwd"": ""999999a"", ""app_code"": ""android19"",""phone_model"": ""SM-N910S"",""pushk"": """", ""app_ver"": ""1.20"" }";

 Output
 변수명		설명
 api_code		api 코드명 string
 insures_code		회원사 코드
 mber_sn		회원 키값
 log_yn		Y
 tot_basic_goal
 day_basic_goal
 default_basic_goal
 mber_hp_newyn
 mber_sex		성별 1(남) 2,(여)
 mber_height		회원 키
 mber_bdwgh		회원체중
 mber_actqy		활동량 1,2,3

 */

public class Tr_login extends BaseData {
	private final String TAG = Tr_login.class.getSimpleName();


	public static class RequestData {
        public String mber_id;      // tjhong@naver.com
        public String mber_pwd;     // 999999a
        public String phone_model;  // SM-N910S
        public String pushk;        //
        public String app_ver;      // 1.20
        public String mber_lifyea;

	}

	public Tr_login(Context context) {
		mContext = context;

		super.conn_url = BaseUrl.COMMON_URL;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj("login");

			RequestData data = (RequestData) obj;

			body.put("api_code","login"); //  "login",
			body.put("insures_code", INSURES_CODE); //  "301",
			body.put("token", DEVICE_TOKEN);
			body.put("mber_id",data.mber_id); //  "tjhong@naver.com",
			body.put("mber_pwd",data.mber_pwd); //  "999999a",
			body.put("app_code", APP_CODE); //  "android19",
			body.put("phone_model",data.phone_model); //  "SM-N910S",
			body.put("pushk", ""); //  "",
			body.put("app_ver",data.app_ver); //  "1.2w0"

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
    @SerializedName("mber_sn")
    public String mber_sn;
    @SerializedName("mber_id")
    public String mber_id;
    @SerializedName("mber_pwd")
    public String mber_pwd;
    @SerializedName("add_reg_yn")
    public String add_reg_yn;
    @SerializedName("log_yn")
    public String log_yn;
    @SerializedName("mber_sex")
    public String mber_sex;
    @SerializedName("mber_nm")
    public String mber_nm;
    @SerializedName("mber_hp")
    public String mber_hp;
    @SerializedName("mber_height")
    public String mber_height;
    @SerializedName("mber_bdwgh")
    public String mber_bdwgh;
    @SerializedName("mber_bdwgh_app")
    public String mber_bdwgh_app;
    @SerializedName("mber_bmi")
    public String mber_bmi;
    @SerializedName("mber_bmi_level")
    public String mber_bmi_level;
    @SerializedName("mber_bdwgh_goal")  // 목표체중
    public String mber_bdwgh_goal;
    @SerializedName("medicine_yn")
    public String medicine_yn;
    @SerializedName("smkng_yn")  // 목표체중
    public String smkng_yn;
    @SerializedName("mber_actqy")
    public String mber_actqy;
    @SerializedName("disease_nm")
    public String disease_nm;
    @SerializedName("mber_zone")
    public String mber_zone;
    @SerializedName("mber_lifyea")
    public String mber_lifyea;
}
