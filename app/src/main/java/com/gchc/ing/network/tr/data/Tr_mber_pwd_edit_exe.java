package com.gchc.ing.network.tr.data;

import com.google.gson.annotations.SerializedName;
import com.gchc.ing.network.tr.BaseData;
import com.gchc.ing.network.tr.BaseUrl;
import com.gchc.ing.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kominhyuk on 2017. 6. 23..
 */

public class Tr_mber_pwd_edit_exe extends BaseData {
    private final String TAG = Tr_mber_pwd_edit_exe.class.getSimpleName();

    public static class RequestData {
        public String mber_sn;
        public String mber_id;
        public String bef_mber_pwd;
        public String aft_mber_pwd;
    }

    public Tr_mber_pwd_edit_exe() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj = " + obj);
        if(obj instanceof Tr_mber_pwd_edit_exe.RequestData) {
            JSONObject body = new JSONObject();
            Tr_mber_pwd_edit_exe.RequestData data = (Tr_mber_pwd_edit_exe.RequestData) obj;

            body.put("api_code", getApiCode(TAG));
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn);
            body.put("mber_id", data.mber_id);
            body.put("bef_mber_pwd", data.bef_mber_pwd);
            body.put("aft_mber_pwd", data.aft_mber_pwd);
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
