package com.gchc.ing.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private final String TAG = SharedPref.class.getSimpleName();
	
	private static SharedPref instance;
	private static Context mContext;
	
	public final String PREF_NAME = "greencross";

    public static String SIGN_UP = "sign_up";                       // 회원가입 여부
    public static String SAVED_LOGIN_ID = "saved_login_id";         // 저장된 아이디
    public static String IS_SAVED_LOGIN_ID = "is_saved_login_id";   // 아이디 저장 여부
    public static String SAVED_LOGIN_PWD = "saved_login_pwd";       // 저장된 아이디
    public static String IS_LOGIN_SUCEESS = "is_login_suceess";     // 로그인 성공 여부
    public static String IS_AUTO_LOGIN = "is_auto_login";           // 자동 로그인 여부
    public static String IS_ING_WEB_MEMBER = "is_ing_web_member";   // ing홈페이지에서 가입한 사람인지 여부
    public static String MAIN_CARD_LIST = "main_card_list";         // 메인화면 카드 리스트
    public static String IS_SAVED_HEALTH_MESSAGE_DB = "is_saved_health_message_db"; // 3개월치 건강메시지

    public static String IS_SAVED_MEAL_DB = "is_saved_meal_db";           // 3개월치 식사 데이터
    public static String IS_SAVED_FOOD_DB = "is_saved_food_db"; // 3개월치 음식 데이터
    public static String LOAD_MAIN_DATA = "load_main_data";     // 3개월치 데이터

    public static String INTRO_FOOD_DB = "intro_food_db";     // 기본 음식 데이터

    public static String HEALTH_MESSAGE = "health_message"; // 건강메시지
    public static String HEALTH_MESSAGE_CONFIRIM = "health_message_confirm"; // 건강메시지

    public static String SUGAR_OVER_CHECK = "sugar_over_check";
    public static String SUAGR_OVER_TIME = "sugar_over_time";
	public static SharedPref getInstance() {
		if (instance == null) {
			instance = new SharedPref();
		}
		return instance;
	}

	public void initContext(Context context) {
        mContext = context;
    }
	
	// 값 불러오기
    public String getPreferences(String key){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
    
 // 값 불러오기
    public int getPreferences(String key, int defValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, defValue);
    }

    // 값 불러오기
    public float getPreferences(String key, float defValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getFloat(key, defValue);
    }
 // 값 불러오기
    public boolean getPreferences(String key, boolean defValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defValue);
    }
    
    // 값 저장하기
    public void savePreferences(String key, String val){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, val);
        editor.commit();
    }
    
 // 값 저장하기
    public void savePreferences(String key, int val){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    // 값 저장하기
    public void savePreferences(String key, float val){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, val);
        editor.commit();
    }

    // 값 저장하기
    public void savePreferences(String key, boolean val){
        if (mContext == null) {
            Logger.e(TAG, "mContext is null");
        } else {
            SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(key, val);
            editor.commit();
        }
    }
     
    // 값(Key Data) 삭제하기
    public void removePreferences(String key){
        if (mContext == null) {
            Logger.e(TAG, "mContext is null");
        } else {
            SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(key);
            editor.commit();
        }
    }
     
    // 값(ALL Data) 삭제하기
    public void removeAllPreferences(){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
