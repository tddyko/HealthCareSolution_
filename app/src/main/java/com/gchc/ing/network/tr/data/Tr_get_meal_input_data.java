package com.gchc.ing.network.tr.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.gchc.ing.network.tr.BaseData;
import com.gchc.ing.network.tr.BaseUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 데이터 가져오기(식사)

 *식사일지 (아침,점심,간식,아침간식,점심간식,저녁간식) 등록하기
 * // 아침:a,점심 b ,저녁 c , 아침간식 d , 점심간식 e , 저녁간식 f
 * // 식사 data 가져오기 시작일 ~ 끝나는 일
 {
 json={
 "api_code": "get_meal_input_data",
 "insures_code": "300",
 "mber_sn": "1000",
 "begin_day": "20170301",
 "end_day": "20170430"
 }

 
 //////receive
 <string xmlns="http://tempuri.org/">{
 "api_code": "get_meal_input_data",
 "insures_code": "300",
 "mber_sn": "1000",
 "data_list": [
 {
 "idx": "77777788889",
 "amounttime": "10",
 "mealtype": "a",
 "calorie": "111",
 "picture": "",
 "regdate": "201704161007"
 },
 {
 "idx": "77777788820",
 "amounttime": "10",
 "mealtype": "a",
 "calorie": "111",
 "picture": "",
 "regdate": "201704161007"
 }
 ]
 }</string>
 */

public class Tr_get_meal_input_data extends BaseData {
	private final String TAG = Tr_get_meal_input_data.class.getSimpleName();


	public static class RequestData {
          public String mber_sn; // 1000",
          public String begin_day; // 10",
          public String end_day; // 10",

	}

	public Tr_get_meal_input_data() {
		super.conn_url = BaseUrl.COMMON_URL;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = new JSONObject();//getBaseJsonObj("login_id");

			RequestData data = (RequestData) obj;
            body.put("api_code", getApiCode(TAG));
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn ); // 1000",
            body.put("begin_day", data.begin_day ); //
            body.put("end_day", data.end_day ); //

			return body;
		}

		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/
    
	@SerializedName("api_code")        //		api 코드명 string
	public String api_code;        //		api 코드명 string
	@SerializedName("insures_code")//		회원사 코드
	public String insures_code;    //		회원사 코드
    @SerializedName("mber_sn")
    public String mber_sn;
    @SerializedName("data_list")
    public List<ReceiveDatas> data_list = new ArrayList<>();

    public static class ReceiveDatas implements Parcelable {
        @SerializedName("idx")
        public String idx; // 77777788820",
        @SerializedName("amounttime")
        public String amounttime; // 10",
        @SerializedName("mealtype")
        public String mealtype; // a",
        @SerializedName("calorie")
        public String calorie; // 111",
        @SerializedName("picture")
        public String picture; // ",
        @SerializedName("regdate")
        public String regdate; // 201704161007"

        public ReceiveDatas() {}

        public ReceiveDatas(Parcel in) {
            idx = in.readString();
            amounttime = in.readString();
            mealtype = in.readString();
            calorie = in.readString();
            picture = in.readString();
            regdate = in.readString();
        }

        public static final Creator<ReceiveDatas> CREATOR = new Creator<ReceiveDatas>() {
            @Override
            public ReceiveDatas createFromParcel(Parcel in) {
                return new ReceiveDatas(in);
            }

            @Override
            public ReceiveDatas[] newArray(int size) {
                return new ReceiveDatas[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(idx);
            dest.writeString(amounttime);
            dest.writeString(mealtype);
            dest.writeString(calorie);
            dest.writeString(picture);
            dest.writeString(regdate);
        }
    }

}
