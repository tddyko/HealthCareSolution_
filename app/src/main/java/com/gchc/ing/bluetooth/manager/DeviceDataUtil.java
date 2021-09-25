package com.gchc.ing.bluetooth.manager;

import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;
import android.util.SparseArray;

import com.gchc.ing.R;
import com.gchc.ing.base.BaseFragment;
import com.gchc.ing.base.value.Define;
import com.gchc.ing.bluetooth.model.BloodModel;
import com.gchc.ing.bluetooth.model.MessageModel;
import com.gchc.ing.bluetooth.model.WeightModel;
import com.gchc.ing.component.CDialog;
import com.gchc.ing.database.DBHelper;
import com.gchc.ing.database.DBHelperMessage;
import com.gchc.ing.database.DBHelperSugar;
import com.gchc.ing.database.DBHelperWeight;
import com.gchc.ing.main.BluetoothManager;
import com.gchc.ing.network.tr.ApiData;
import com.gchc.ing.network.tr.data.Tr_bdsg_info_input_data;
import com.gchc.ing.network.tr.data.Tr_bdwgh_goal_input;
import com.gchc.ing.network.tr.data.Tr_bdwgh_info_input_data;
import com.gchc.ing.network.tr.data.Tr_get_hedctdata;
import com.gchc.ing.network.tr.data.Tr_infra_message_write;
import com.gchc.ing.network.tr.data.Tr_login;
import com.gchc.ing.util.CDateUtil;
import com.gchc.ing.util.Logger;
import com.gchc.ing.util.SharedPref;
import com.gchc.ing.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.gchc.ing.util.CDateUtil.getForamtyyMMddHHmmssSS;

/**
 * Created by MrsWin on 2017-04-09.
 */

public class DeviceDataUtil {

    /**
     * 혈당 데이터 서버 및 sqlite에 저장
     *
     * @param dataModel
     */
    public void uploadSugarData(final BaseFragment baseFragment, final SparseArray<BloodModel> dataModel, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_bdsg_info_input_data inputData                   = new Tr_bdsg_info_input_data();
        Tr_login login                                      = Define.getInstance().getLoginInfo();

        Tr_bdsg_info_input_data.RequestData requestData     = new Tr_bdsg_info_input_data.RequestData();
        requestData.mber_sn     = login.mber_sn;
        requestData.ast_mass    = inputData.getArray(dataModel);

        baseFragment.getData(baseFragment.getContext(), inputData.getClass(), requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_bdsg_info_input_data) {
                    Tr_bdsg_info_input_data data = (Tr_bdsg_info_input_data) obj;
                    boolean isServerReg = "Y".equals(data.reg_yn);
                    if (isServerReg) {
                        registSugarDB(baseFragment, dataModel, true);

                        if (dataModel.size() > 0) {
                            BloodModel model = dataModel.get(dataModel.keyAt(dataModel.size() - 1));
                            if(model.getSugar() > 0.0f){
                                insertSugarMessage(baseFragment, dataModel);
                            }
                        }

                        if (iBluetoothResult != null)
                            iBluetoothResult.onResult(true);
                    } else {
                        CDialog.showDlg(baseFragment.getContext(), baseFragment.getContext().getString(R.string.text_regist_fail));
                    }
                }

            }
        }, new ApiData.IFailStep() {
            @Override
            public void fail() {
                registSugarDB(baseFragment, dataModel, false);

                if (dataModel.size() > 0) {
                    BloodModel model = dataModel.get(dataModel.keyAt(dataModel.size() - 1));
                    if(model.getSugar() > 0.0f){
                        insertSugarMessage(baseFragment, dataModel);
                    }
                }

                if (iBluetoothResult != null)
                    iBluetoothResult.onResult(false);
            }
        });
    }

    /**
     * 건강메시지 sqlite 등록하기(혈당)
     * @param baseFragment
     * @param dataModel
     */
    private void insertSugarMessage(BaseFragment baseFragment, SparseArray<BloodModel> dataModel) {
        // 메시지 DB 등록하기
        if (dataModel.size() > 0) {
            BloodModel model        = dataModel.get(dataModel.keyAt(dataModel.size() - 1));
            String message          = getSugarMessage(model, model.getBefore());
            if (TextUtils.isEmpty(message) == false) {
                SharedPref.getInstance().savePreferences(SharedPref.HEALTH_MESSAGE, true);
                MessageModel messageModel = new MessageModel();
                messageModel.setIdx(getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
                messageModel.setSugar("" + model.getSugar());
                messageModel.setRegdate("" + model.getRegTime());
                messageModel.setMessage(message);

                insertMesageDb(baseFragment, messageModel);
            }
        }
    }

    /**
     * 건강메시지 sqlite 등록하기(체지방)
     * @param baseFragment
     **/
    private void insertWeightMessage(BaseFragment baseFragment,  String weight, String reg, String fat) {
        // 메시지 DB 등록하기

        String message = getWeightMessage(weight, fat);
        if (TextUtils.isEmpty(message) == false) {
            SharedPref.getInstance().savePreferences(SharedPref.HEALTH_MESSAGE, true);
            MessageModel messageModel = new MessageModel();
            messageModel.setIdx(getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
            try {

                Thread.sleep(100);
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageModel.setWeight("" + weight);
            messageModel.setRegdate(reg);
            messageModel.setMessage(message);
            insertMesageDb(baseFragment, messageModel);
        }
    }

    /**
     * 건강메시지 서버 전송 및 sqlite 저장
     *
     * @param baseFragment
     * @param model
     */
    private void insertMesageDb(final BaseFragment baseFragment, final MessageModel model) {
        Tr_infra_message_write.RequestData reqData = new Tr_infra_message_write.RequestData();
        Tr_login login          = Define.getInstance().getLoginInfo();
        reqData.idx             = model.getIdx();
        reqData.mber_sn         = login.mber_sn;
        reqData.infra_message   = model.getMessage();

        baseFragment.getData(baseFragment.getContext(), Tr_infra_message_write.class, reqData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {

                DBHelper helper     = new DBHelper(baseFragment.getContext());
                DBHelperMessage db  = helper.getMessageDb();
                if (obj instanceof Tr_infra_message_write) {
                    Tr_infra_message_write data = (Tr_infra_message_write) obj;
                    db.insert(model, "Y".equals(data.reg_yn));
                } else {
                    db.insert(model, false);
                }
            }
        });
    }

    /**
     * 혈당 sqlite 저장하기
     *
     * @param baseFragment
     * @param dataModel
     * @param isServerRegist
     */
    private void registSugarDB(BaseFragment baseFragment, SparseArray<BloodModel> dataModel, boolean isServerRegist) {
        DBHelper helper = new DBHelper(baseFragment.getContext());
        DBHelperSugar db = helper.getSugarDb();
        db.insert(dataModel, isServerRegist);
    }

    /**
     * 체중 데이터 입력
     * @param baseFragment
     * @param weightModel
     * @param iBluetoothResult
     */
    public void uploadWeight(final BaseFragment baseFragment, final WeightModel weightModel, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_get_hedctdata.DataList data = new Tr_get_hedctdata.DataList();
        data.bmr        = "" + weightModel.getBmr();
        data.bodywater  = "" + weightModel.getBodyWater();
        data.bone       = "" + weightModel.getBone();
        data.fat        = "" + weightModel.getFat();
        data.heartrate  = "" + weightModel.getHeartRate();
        data.muscle     = "" + weightModel.getMuscle();
        data.obesity    = "" + weightModel.getObesity();
        data.weight     = "" + weightModel.getWeight();
        data.bdwgh_goal = "" + weightModel.getBdwgh_goal();

        data.idx        = weightModel.getIdx();
        data.regtype    = weightModel.getRegType();
        data.reg_de     = weightModel.getRegDate();

        List<Tr_get_hedctdata.DataList> datas = new ArrayList<>();
        datas.add(data);
        new DeviceDataUtil().uploadWeight(baseFragment, datas, iBluetoothResult);
    }

    /**
     * 목표체중 데이터 입력
     * @param baseFragment
     * @param weightModel
     * @param iBluetoothResult
     */
    public void uploadTargetWeight(final BaseFragment baseFragment, final WeightModel weightModel, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_bdwgh_goal_input inputData       = new Tr_bdwgh_goal_input();
        final Tr_login login                = Define.getInstance().getLoginInfo();

        Tr_bdwgh_goal_input.RequestData requestData         = new Tr_bdwgh_goal_input.RequestData();
        requestData.mber_sn                                 = login.mber_sn;
        requestData.mber_bdwgh_goal                         = Float.toString(weightModel.getBdwgh_goal());

        baseFragment.getData(baseFragment.getContext(), inputData.getClass(), requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_bdwgh_goal_input) {
                    Tr_bdwgh_goal_input data = (Tr_bdwgh_goal_input) obj;
                    if ("Y".equals(data.reg_yn)) {

                        if (iBluetoothResult != null)
                            iBluetoothResult.onResult(true);

                    } else {
                        CDialog.showDlg(baseFragment.getContext(), baseFragment.getContext().getString(R.string.text_regist_fail));
                    }
                }
            }
        }, new ApiData.IFailStep() {
            @Override
            public void fail() {

                if (iBluetoothResult != null)
                    iBluetoothResult.onResult(false);
            }
        });
    }

    /**
     * 체중데이터 업로드 및 Sqlite 저장
     * @param baseFragment
     * @param datas
     * @param iBluetoothResult
     */
    public void uploadWeight(final BaseFragment baseFragment, final List<Tr_get_hedctdata.DataList> datas, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_bdwgh_info_input_data inputData  = new Tr_bdwgh_info_input_data();
        final Tr_login login                = Define.getInstance().getLoginInfo();

        Tr_bdwgh_info_input_data.RequestData requestData    = new Tr_bdwgh_info_input_data.RequestData();
        requestData.mber_sn                                 = login.mber_sn;
        requestData.ast_mass                                = inputData.getArray(datas);

        baseFragment.getData(baseFragment.getContext(), inputData.getClass(), requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_bdwgh_info_input_data) {
                    Tr_bdwgh_info_input_data data = (Tr_bdwgh_info_input_data) obj;
                    if ("Y".equals(data.reg_yn)) {
                        for (Tr_get_hedctdata.DataList listdata : datas) {
                            if(StringUtil.getFloatVal(listdata.weight) > 0.0f || listdata.regtype.equals("D")){
                                insertWeightMessage(baseFragment, listdata.weight, listdata.reg_de, listdata.fat);
                                registWeightDB(baseFragment, datas, true);
                            }

                        }

                        if (iBluetoothResult != null)
                            iBluetoothResult.onResult(true);
                    } else {
                        CDialog.showDlg(baseFragment.getContext(), baseFragment.getContext().getString(R.string.text_regist_fail));
                    }
                }
            }
        }, new ApiData.IFailStep() {
            @Override
            public void fail() {
                for (Tr_get_hedctdata.DataList data : datas) {
                    if(StringUtil.getFloatVal(data.weight) > 0.0f || data.regtype.equals("D")){
                        insertWeightMessage(baseFragment, data.weight, data.reg_de, data.fat);
                        registWeightDB(baseFragment, datas, true);
                    }
                }

                if (iBluetoothResult != null)
                    iBluetoothResult.onResult(false);
            }
        });
    }

    private void registWeightDB(BaseFragment baseFragment, List<Tr_get_hedctdata.DataList> datas, boolean isServerRegist) {
        DBHelper helper     = new DBHelper(baseFragment.getContext());
        DBHelperWeight db   = helper.getWeightDb();
        db.insert(datas, isServerRegist);
    }

    /**
     * 건강메시지 체지방메시지 만들기
     */

    private String getRatingMsg(String fat) {
        String ratingMsg = "";
        int rating = getRating(fat);

        if (rating == 1) {
            ratingMsg = "체지방률 " + fat + "%로서 평균보다 상당히 적은 상태입니다.";
        } else if (rating == 2) {
            ratingMsg = "체지방률 " + fat + "%로서 평균보다 적은 상태입니다.";
        } else if (rating == 3) {
            ratingMsg = "체지방률 " + fat + "%로서 평균적인 상태입니다.";
        } else if (rating == 4) {
            ratingMsg = "체지방률 " + fat + "%로서 평균보다 많은 상태입니다.";
        } else if (rating == 5) {
            ratingMsg = "체지방률 " + fat + "%로서 평균보다 상당히 많은 상태입니다.";
        }
        return ratingMsg;
    }

    /**
     * 건강메시지 체지방 등급 만들기
     */

    private int getRating(String fat) {
        int rating = 0;                                                                                 // 체지방 등급
        Tr_login login = Define.getInstance().getLoginInfo();                                           // 회원 정보
        int sex         = StringUtil.getIntVal(login.mber_sex);                                         // 회원 성별
        String nowYear  = CDateUtil.getFormattedString_yyyy(System.currentTimeMillis());                // 현재 년도
        int rBirth      = StringUtil.getIntVal(login.mber_lifyea.substring(0, 4));                      // 회원 생년
        int rAge        = (StringUtil.getIntVal(nowYear) - rBirth);                                     // 회원 나이
        float bdfat     = StringUtil.getFloatVal(fat);                                                  // 회원 체지방률

        // 남자
        if (sex == 1) {
            if (((rAge >= 19 && rAge <= 24) && (bdfat <= 8.0))                            // 1등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat <= 9.4))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat <= 10.6))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat <= 12.9))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat <= 12.8))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat <= 13.2))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat <= 14.3))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat <= 14.4))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat <= 16.1))) {
                rating = 1;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 8.1 && bdfat <= 11.7))      // 2등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 9.5 && bdfat <= 13.7))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 10.7 && bdfat <= 14.5))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 13.0 && bdfat <= 16.7))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 12.9 && bdfat <= 15.6))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 13.3 && bdfat <= 16.5))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 14.4 && bdfat <= 17.7))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 14.5 && bdfat <= 18.0))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 16.2 && bdfat <= 17.8))) {
                rating = 2;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 11.8 && bdfat <= 16.6))     // 3등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 13.8 && bdfat <= 18.3))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 14.6 && bdfat <= 18.8))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 16.8 && bdfat <= 21.1))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 15.7 && bdfat <= 20.0))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 16.6 && bdfat <= 20.3))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 17.8 && bdfat <= 21.8))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 18.1 && bdfat <= 21.5))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 17.9 && bdfat <= 22.5))) {
                rating = 3;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 16.7 && bdfat <= 22.8))     // 4등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 18.4 && bdfat <= 24.4))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 18.9 && bdfat <= 23.0))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 21.2 && bdfat <= 25.1))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 20.1 && bdfat <= 24.0))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 20.4 && bdfat <= 24.8))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 21.9 && bdfat <= 25.9))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 21.6 && bdfat <= 25.1))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 22.6 && bdfat <= 27.5))) {
                rating = 4;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 22.9))                    // 5등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 24.5))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 23.1))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 25.2))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 24.1))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 24.9))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 26.0))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 25.2))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 27.6))) {
                rating = 5;
            }
            return rating;
        }
        // 여자
        if (sex == 2) {
            if (((rAge >= 19 && rAge <= 24) && (bdfat <= 19.0))                           // 1등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat <= 18.6))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat <= 18.9))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat <= 19.2))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat <= 19.8))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat <= 19.4))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat <= 19.7))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat <= 20.6))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat <= 21.2))) {
                rating = 1;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 19.1 && bdfat <= 22.3))     // 2등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 18.7 && bdfat <= 21.3))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 19.0 && bdfat <= 22.1))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 19.3 && bdfat <= 23.0))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 19.9 && bdfat <= 23.1))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 19.5 && bdfat <= 22.9))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 19.8 && bdfat <= 23.9))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 20.7 && bdfat <= 24.3))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 21.3 && bdfat <= 24.8))) {
                rating = 2;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 22.4 && bdfat <= 25.3))     // 3등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 21.4 && bdfat <= 24.9))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 22.2 && bdfat <= 24.8))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 23.1 && bdfat <= 27.0))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 23.2 && bdfat <= 28.0))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 23.0 && bdfat <= 27.7))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 24.4 && bdfat <= 27.8))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 24.4 && bdfat <= 28.9))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 24.9 && bdfat <= 29.2))) {
                rating = 3;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 25.4 && bdfat <= 29.6))     // 4등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 25.0 && bdfat <= 29.6))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 24.9 && bdfat <= 28.6))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 27.1 && bdfat <= 32.8))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 28.1 && bdfat <= 33.1))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 27.8 && bdfat <= 31.4))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 27.9 && bdfat <= 34.6))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 29.0 && bdfat <= 36.0))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 29.3 && bdfat <= 34.7))) {
                rating = 4;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 29.7))                    // 5등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 29.7))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 28.7))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 32.9))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 33.2))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 31.5))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 34.7))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 36.1))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 34.8))) {
                rating = 5;
            }
            return rating;
        }
        return rating;
    }

    /**
     * 건강메시지 체중 메시지 만들기
     * @return
     */

    private String getWeightMessage(String weight, String fat){
        Tr_login login  = Define.getInstance().getLoginInfo();                                          // 회원 정보
        String rWeight   = String.format("%.1f", StringUtil.getFloatVal(weight));  // 회원 체중
        float rHeight   = StringUtil.getFloat(login.mber_height) * 0.01f;                               // 회원 키
        float bmi       = StringUtil.getFloatVal(String.format("%.1f", StringUtil.getFloatVal(weight) / (rHeight * rHeight))); // 회원 BMI
        String lavelstr = "";
        if(bmi < 18.5) {
            lavelstr = "저체중";
        }else if(bmi >= 18.5 && bmi <= 22.9){
            lavelstr = "정상체중";
        }else if(bmi > 22.9 && bmi < 25.0){
            lavelstr = "과체중";
        }else if(bmi >= 25.0){
            lavelstr = "비만";
        }

        String message  = "측정된 체중 " + rWeight + "kg으로 계산된 BMI(체질량지수)는 " + bmi + "으로 " + lavelstr + "군에 해당합니다.";

        float bdfat = StringUtil.getFloatVal(fat);

        if (bdfat > 0) {
            if (message != "")
                message += "\n\n";
            message += getRatingMsg(fat);
        }

        // 저체중군
        if(bmi < 18.5) {
            // 추가메시지는 ||로 구분하여 넣는다.
            if (message != "")
                message += "\n\n";
            message     += "적절한 운동과 균형 잡힌 음식섭취를 통해 정상체중을 회복 할 수 있도록 노력이 필요합니다. \n" +
                    "체중증가가 지방만이 아닌 제지방의 증가까지 병행하여 목표 활동량 달성 노력과 함께 근력운동을 추가하여 근육의 양과 크기를 증가시키는 것이 중요합니다. \n" +
                    "점진적으로 목표를 수정하여 활동량과 식사량을 늘려주세요.";
            return message;
        }
        // 정상체중군
        if(bmi >= 18.5 && bmi <= 22.9) {
            // 추가메시지는 ||로 구분하여 넣는다.
            if (message != "")
                message += "\n\n";
            message     += "적절한 운동과 식사조절을 통해 건강한 체중을 유지하는 것이 중요합니다. \n" +
                    "점진적으로 목표를 수정하여 활동량을 늘려주세요.";
            return message;
        }
        // 비만군
        if(bmi >= 25.0) {
            // 추가메시지는 ||로 구분하여 넣는다.
            if (message != "")
                message += "\n\n";
            message     += "적절한 체중 감량에는 시간이 걸립니다. 가능한 매일 활동 목표 달성을 위해 노력해야 합니다. \n" +
                    "추천되는 목표활동량은 최소한입니다. 점진적으로 목표를 수정하여 활동량을 늘려가야 합니다. \n" +
                    "체중 감량을 위해 평소보다 하루 500~1,000kcal정도의 에너지 섭취량을 줄이세요.";
            return message;
        }
        return message;
    }

    /**
     * 건강메시지 혈당 만들기
     *
     * @param
     * @param eatType
     * @return
     */
    private String getSugarMessage(BloodModel model, String eatType) {
        String message = "";
        String tString = CDateUtil.HH_MM(model.getTime());
        String nowTime = StringUtil.getFormattedDateTime();


        float sugar = model.getSugar();
        // 식전
        if (eatType.equals("0")|| eatType.equals("1")|| eatType.equals("3")) {
            if (sugar <= 99) {
                //정상
                message = tString + " 식전 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 정상범위 안에 있습니다.\n";
                SharedPref.getInstance().savePreferences(SharedPref.SUGAR_OVER_CHECK, "N");
            } else if (sugar >= 100 && sugar <= 125) {
                //당뇨 전단계
                message = tString + " 식전 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 정상범위에서 벗어납니다. 정기적으로 혈당을 체크하고, 관리가 필요합니다.\n";
                SharedPref.getInstance().savePreferences(SharedPref.SUGAR_OVER_CHECK, "N");
            } else if (sugar >= 126) {
                //당뇨병
                message = tString + " 식전 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 정상범위에서 벗어납니다. 정기적으로 혈당을 체크하고, 관리가 필요합니다.\n";
                SharedPref.getInstance().savePreferences(SharedPref.SUGAR_OVER_CHECK, "Y");
                SharedPref.getInstance().savePreferences(SharedPref.SUAGR_OVER_TIME, nowTime);
            }
        }
        // 식후
        else {
            if (sugar <= 139) {
                //정상
                message = tString + "식후 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 정상범위 안에 있습니다.\n";
                SharedPref.getInstance().savePreferences(SharedPref.SUGAR_OVER_CHECK, "N");
            } else if (sugar >= 140 && sugar <= 199) {
                //당뇨 전단계
                message = tString + " 식후 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 정상범위에서 벗어납니다. 정기적으로 혈당을 체크하고, 관리가 필요합니다.\n";
                SharedPref.getInstance().savePreferences(SharedPref.SUGAR_OVER_CHECK, "N");
            } else if (sugar >= 200) {
                //당뇨병
                message = tString + " 식후 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 정상범위에서 벗어납니다. 정기적으로 혈당을 체크하고, 관리가 필요합니다.\n";
                SharedPref.getInstance().savePreferences(SharedPref.SUGAR_OVER_CHECK, "Y");
                SharedPref.getInstance().savePreferences(SharedPref.SUAGR_OVER_TIME, nowTime);
            }
        }
        Logger.i(TAG, "getSugarMessage=" + message);
        return message;
    }
}