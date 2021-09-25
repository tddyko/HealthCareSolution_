package com.gchc.ing.content.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TabHost;

/**
 * 공통 사용 메소드 모음
 *
 */
public class Utils {

	/** 구글계정 프로젝트 번호 **/
	//	public static final String SENDER_ID = "604504064704";
	//	public static final String SENDER_ID = "177778920885";
	//	public static final String SENDER_ID = "711153339175";
	public static final String SENDER_ID = "58762191394";
	/** 보험사코드 **/
	public static final String INSURESCODE="107";
	/** API URL STR**/
	public static final String URL_STR="/KB_mobile_Call";
	public static int NOTI_ID = 0;
	public static int NOTI_ID2 = 0;

	public static final String AUTH_URL = "http://m.shealthcare.co.kr/appmbKBhp/MBHP_JOIN.ASP";

	/** 
	 * 수정 사항
	 * 수정사항1 : LoginActivity 56라인 DeviceToken 수정
	 * 수정내용 : 현재 테스트를 위해 단말기의 디바이스 토큰(안드로이드는 RegID)대신 DeviceToken 이라는 문자 보냄
	 * 
	 * 수정사항2 : LoginActivity 116라인
	 * 로그인 협의 후 완료 URL 수정
	 */
	public static String TAG="hsh";
	public static boolean TEST = false;

	public static boolean bAppRunned = false;
	public static boolean bAppPush = false;
	//	public static boolean bAppAuth = false;

	public static List<View> Sview = null;
	public static FragmentActivity FRAGMENTACTIVITY1;
	public static FragmentActivity FRAGMENTACTIVITY2;
	public static FragmentActivity FRAGMENTACTIVITY3;

	public static int TABCNT=0;
	public static boolean VERSIONCHECK = true;		//버전이 같은지 틀린지
	public static ScrollView SVIEW;					//메인화면 스크롤뷰
	public static int displayWidth;					//화면 크기 가로
	public static int displayHeight;				//화면 크기 세로
	public static Intent AINTENT;					//메인탭 Intent
	public static SharedPreferences pref;
	public static SharedPreferences.Editor editor;
	public static ProgressBar progressBar;	//진행바
	public static TabHost tabHost;	//Tab 메뉴 관련
	public static EditText editText1; //글쓰기 EDIT BOX
	public static boolean checkOk = false; //고객의 소리 관련 flag
	public static String regId = "";
	public static String NOW_VERSION = "";
	public static String NEW_VERSION = "";

	public static List<ImageButton> imageButtonList;
	public static List<ImageView> imageViewList;
	public static int rectHeight;
	public static LinearLayout tablinear;
	public static int tablinearHeight;
	//	public static GotoClass gotoClass;

	public static final long DURATION_TIME = 2000L;
	public static long prevPressTime = 0L;

	public static void init(Activity activity){
		pref = activity.getSharedPreferences("pref", Activity.MODE_PRIVATE);
		editor = pref.edit(); 
	}

	/**
	 * 다이아로그 출력
	 * @param con 해당화면 Context
	 * @param msg 출력할 메세지
	 */
	/*public static void dialog(Context con, String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
		alertDialogBuilder.setTitle("");
		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setPositiveButton("확인",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}*/

	/**
	 * Html 유니코드 변환
	 * @param data
	 * @return String
	 */
	public static String getHTML(String data) {
		String jsonData = data.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
		return jsonData;
	}
	/**
	 * 서버로 부터 받은 Json 형식 중 필요없는 부분 제거
	 * @param data 서버로 부터 받은 문자
	 * @return json
	 */
	public static String getJosonDataFromXML(String data) {
		String jsonData = data.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?><string xmlns=\"http://tempuri.org/\">", "").replace("</string>", "");
		if(jsonData.equals("")){
			return "";
		}
		return jsonData;
	}

	/**
	 * 게시물의 전체 수를 이용해 전체 페이지 수를 산출
	 * @param data
	 * @return int
	 */
	public static int getMaxPageNum(String data) {
		int result = 0;
		try {
			int param = Integer.parseInt(data);
			result = param / 10;
			if (param % 10 != 0) {
				result = result + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return result;
	}	

	public static int getNewsMaxPageNum(String data) {
		int result = 0;
		try {
			int param = Integer.parseInt(data);
			result = param / 15;
			if (param % 15 != 0) {
				result = result + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return result;
	}	

	/** 상담원 연결 시간 체크 */
	//	public static boolean checkTime(){
	//		boolean check = false;
	//		long now = System.currentTimeMillis();
	//
	//		Calendar scal =  Calendar.getInstance();
	//		scal.set(scal.get(Calendar.YEAR), scal.get(Calendar.MONTH), scal.get(Calendar.DAY_OF_MONTH), 9, 0);
	//		long smilliSecond = scal.getTimeInMillis();
	//
	//		Calendar ecal =  Calendar.getInstance();
	//		ecal.set(ecal.get(Calendar.YEAR), ecal.get(Calendar.MONTH), ecal.get(Calendar.DAY_OF_MONTH), 11, 0);
	//		long emilliSecond = ecal.getTimeInMillis();
	//
	//		if(smilliSecond < now && now < emilliSecond){
	//			check = true;
	//		}
	//
	//		return check;
	//	}
	/**
	 * 리스트뷰 이미지 표시 중 사용
	 * @param is
	 * @param os
	 */
	public static void CopyStream(InputStream is, OutputStream os){
		final int buffer_size=1024;
		try{
			byte[] bytes=new byte[buffer_size];
			for(;;){
				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;
				os.write(bytes, 0, count);
			}
		}catch(Exception ex){
			if(Utils.TEST){
				Log.e("Exception", "Utils convertDipsToPixels Exception");
			}
		}
	}
	/**
	 * 한자리 숫자를 두자리 문자로 변환
	 * @param a
	 * @return String
	 */
	public static String zeroTwo(int a){
		String result = Integer.toString(a);
		if(a < 10){
			result = "0"+a;
		}
		return result;
	}
	/**
	 * 날짜 형식 변환(월 일)
	 * @param time
	 * @return String
	 */
	public static String dateFormatStr(String time){
		String timeform = "";
		timeform = time.substring(4,6)+"월 "+time.substring(6,8)+"일 ("+time.substring(0,4)+")";
		return timeform;
	}
	/**
	 * 날짜 형식 변환(..)
	 * @param time
	 * @return String
	 */
	public static String dateFormat(String time){
		String timeform = "";
		timeform = time.substring(0,4)+"."+time.substring(4,6)+"."+time.substring(6,8);
		return timeform;
	}
	/**
	 * 날짜 형식 변환(.. 오전 : )
	 * @param time
	 * @return String
	 */
	public static String timeFormat(String time){
		String timeform = "";
		try {
			timeform = time.substring(0,4)+"."+time.substring(4,6)+"."+time.substring(6,8);
			int hour = Integer.valueOf(time.substring(8,10));
			if(hour > 12){
				timeform = timeform + ", "+"오후 "+ (hour-12) +":"+ time.substring(10,12);
			}else{
				timeform = timeform + ", "+"오전 "+ (hour-12) +""+ time.substring(10,12);
			}
		} catch (Exception e) {
		}
		if(timeform.equals("")){
			timeform = time;
		}
		return timeform;
	}
	/**
	 * 날짜 형식 변환(년 월 일 오전 : )
	 * @param time
	 * @return String
	 */
	public static String timeFormatStra(String time){
		String timeform = "";
		try {
			timeform = time.substring(0,4)+"년 "+time.substring(4,6)+"월 "+time.substring(6,8)+"일";
			int hour = Integer.valueOf(time.substring(8,10));
			if(hour > 12){
				timeform = timeform + " "+"오후  "+ zeroTwo((hour-12)) +":"+ time.substring(10,12);
			}else{
				timeform = timeform + " "+"오전  "+ zeroTwo((hour)) +":"+ time.substring(10,12);
			}
		} catch (Exception e) {
		}
		if(timeform.equals("")){
			timeform = time;
		}
		return timeform;
	}
	/**
	 * 날짜 형식 변환(.. 오전 : )
	 * @param time
	 * @return String
	 */
	public static String timeFormatStr(String time){
		String timeform = "";
		try {
			timeform = time.substring(0,4)+". "+time.substring(4,6)+". "+time.substring(6,8)+".";
			int hour = Integer.valueOf(time.substring(8,10));
			if(hour > 12){
				timeform = timeform + " "+"오후  "+ zeroTwo((hour-12)) +":"+ time.substring(10,12);
			}else{
				timeform = timeform + " "+"오전  "+ zeroTwo((hour)) +":"+ time.substring(10,12);
			}
		} catch (Exception e) {
		}                    
		if(timeform.equals("")){
			timeform = time;
		}
		return timeform;
	}
	/**
	 * 시간 형식 변환(오전 :)
	 * @param time
	 * @return String
	 */
	public static String timeOnlyFormatStr(String time){
		String timeform = "";
		try {
			int hour = Integer.valueOf(time.substring(0,2));
			if(hour > 12){
				timeform = timeform + " "+"오후  "+ zeroTwo((hour-12)) +":"+ time.substring(2,4);
			}else{
				timeform = timeform + " "+"오전  "+ zeroTwo((hour)) +":"+ time.substring(2,4);
			}
		} catch (Exception e) {
		}
		if(timeform.equals("")){
			timeform = time;
		}
		return timeform;
	}

	/**
	 * 날짜 형식변환(년 월 일 요일)
	 * @param time
	 * @return String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String timeFormatStrK(String time){
		String timeform = "";
		try {
			timeform = time.substring(0,4)+"년 "+time.substring(4,6)+"월 "+time.substring(6,8)+"일 ";

			Date dateTemp = null;
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String dates = time.substring(0,4)+time.substring(4,6)+time.substring(6,8);
			try {
				dateTemp = dateFormat.parse(dates);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Calendar cals = Calendar.getInstance();
			cals.setTime(dateTemp);
			int week = cals.get(Calendar.DAY_OF_WEEK);
			String weekStr = "";
			switch (week){
			case 1:
				weekStr = "일요일";
				break;
			case 2:
				weekStr = "월요일";
				break;
			case 3:
				weekStr = "화요일";
				break;
			case 4:
				weekStr = "수요일";
				break;
			case 5:
				weekStr = "목요일";
				break;
			case 6:
				weekStr = "금요일";
				break;
			case 7:
				weekStr = "토요일";
				break;
			}
			timeform = timeform + " "+weekStr;

		} catch (Exception e) {
		}
		if(timeform.equals("")){
			timeform = time;
		}
		return timeform;
	}

	public static String replaceEx(String string) {
		String result = string.replaceAll("\r\n","\r\n");
		return result;
	}

	public static void init(Context arg0) {
		pref = arg0.getSharedPreferences("pref", Activity.MODE_PRIVATE);
		editor = pref.edit(); 
	}

	public static final String[] LOCATION_PERMS={
		Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
	};
	
	public static boolean canAccessStorage(Context context){
		boolean b1= hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
		boolean b2 = hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
		if(Utils.TEST){
			Log.e("hsh", "b1 "+b1+" b2 "+b2);
		}
        return( b1 && b2);
    }
	public static boolean hasPermission(Context context, String perm) {
        return(PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, perm));
    }
}
