package com.gchc.ing.question.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by User5 on 2015-05-20.
 */
public class Util {

    public static InputStream xmlParser(InputStream inputStream, String query) throws JDOMException, IOException
    {
        InputStream ipstream = inputStream;
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = (Document) builder.build(inputStream);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element itemlist = doc.getRootElement().getChild("itemlist");

        if (itemlist == null)
            return null;
        List list = itemlist.getChildren();

        ArrayList<String> dataList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            Element item = (Element) list.get(i);
            String address = item.getChildText("address");
            if (isFilter(query, address)) {
                address = cutAddrString(query, address);
                dataList.add(address);
            }
        }
        HashSet hs = new HashSet(dataList);
        dataList = new ArrayList<String>(hs);

        StringBuilder sb = new StringBuilder();
        for (String s : dataList) {
            sb.append(s);
            sb.append("/");
        }
        ByteArrayInputStream stream = null;
        try {
            stream = new ByteArrayInputStream(sb.toString().getBytes("EUC-KR"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ipstream = stream;
        return ipstream;
    }

    private static boolean isFilter(String str1, String str2) {
        String str = " " + str1;

        if (str2.indexOf(str) == -1) {
            return false;
        }
        int p = str2.indexOf(str1) + str1.length();
        if (p < str2.length()) {
            if (str2.charAt(p) != ' ') {
                return false;
            }
        }
        return true;
    }

    private static String cutAddrString(String str1, String str2) {
        int p = str2.indexOf(str1) + str1.length();
        return str2.substring(0, p);
    }

    public static String getQuery(Object obj) {
        if (null == obj) {
            return "";
        }
        String name = null;
        String value = null;
        if (obj instanceof HashMap) {
            HashMap<String, String> hashMap = (HashMap) obj;
            Iterator<String> iterator = hashMap.keySet().iterator();

            while (iterator.hasNext()) {
                name = iterator.next();
                value = hashMap.get(name);
                if (name.equals("query")) {
                    return value;
                }
            }
        }
        return "";
    }

    public static String makeStringCommaKm(int value, boolean isDistance) {
        double distance = (double) value;
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
        StringBuilder sb = new StringBuilder("");
        if (isDistance) {
            if (value < 1000) {
                sb.append((int) distance);
                sb.append("m");
            } else {
                sb.append(String.format("%.1f", distance / 1000));
                sb.append("km");
            }
        } else {
            return decimalFormat.format(distance);
        }
        return sb.toString();
    }

    public static String JSONParserStipulation(String jsonStr) {
        String result = "";
        try {
            JSONObject jsonResponse = new JSONObject(jsonStr);
            JSONArray jasonArray = jsonResponse.getJSONArray("data");
            for (int i = 0; i < jasonArray.length(); i++) {
                JSONObject actor = jasonArray.getJSONObject(i);
                result = actor.getString("stipulation");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    public static boolean JSONParserCheckOk(String jsonStr) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            result = jsonObject.getString("code");
        } catch (Exception e) {
            CLog.w("Exception : " + e.toString());
        }
        if (result.equals("100")) {
            return true;
        } else {
            return false;
        }
    }


    public static Boolean isShareAppInstall(Context context, String packName) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.getApplicationInfo(packName, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String makeStringComma(int value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
        return decimalFormat.format(value);
    }

    /**
     * <PRE>
     * Return Exception Log
     * </PRE>
     * <p/>
     * Suzy 2013. 1. 8. 오후 2:31:27
     *
     * @param e 예외
     * @return String 결과
     */
    public static String getExceptionLog(Exception e) {
        String exceptionAsStrting = "";
        if (e != null) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            exceptionAsStrting = sw.toString();
        }
        return exceptionAsStrting;
    }

    public static void recursiveRecycle(View root) {
        if (root == null)
            return;
        root.setBackgroundDrawable(null);
        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)root;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                recursiveRecycle(group.getChildAt(i));
            }

            if (!(root instanceof AdapterView)) {
                group.removeAllViews();
            }

        }

        if (root instanceof ImageView) {
            ((ImageView)root).setImageDrawable(null);
        }

        root = null;
        return;
    }

    public static void recycleDrawable(Drawable drawable){
        if(null == drawable) return;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }
    }

    public static List<String> setGameResultStr(String str){
        List<String> l_str = new ArrayList<>();
        if (null != str) {
            String[] arr = str.split("_");
            for (String s : arr) {
                l_str.add(s);
            }
        }
        return l_str;
    }


    /**
     * xml 파싱
     * @param xml xml 정규식
     * @return    문자열
     * @throws Exception
     */
    public static String parseXml(String xml) throws Exception{

        String jsonStr = "";

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xml));

        int eventType = parser.getEventType();
        String mTag = "";

        while( eventType != XmlPullParser.END_DOCUMENT){		// 종료 도큐멘트가 아니라면
            switch(eventType){
                case XmlPullParser.START_DOCUMENT:
                case XmlPullParser.END_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    mTag = parser.getName();
//                    Log.i("XmlPullParser.START_TAG = "+parser.getName().toString());
                    break;
                case XmlPullParser.END_TAG:
                    break;
                case XmlPullParser.TEXT:
//                    Log.i("XmlPullParser.START_TAG = "+parser.getText());
                    CLog.i("mTag = " + mTag);
                    if(mTag.equals("string")){  // string tag 값 리턴
                        jsonStr = parser.getText();
                        return jsonStr;
                    }
                    break;
            }
            eventType = parser.next();
        }

        return jsonStr;
    }

    /**
     * 해당 요일의 월~일 구하기
     * @param date 날짜 ex:20160422
     * @return 결과값 ex:20160417 ,20160418 , 20160419 , 20160420 , 20160421 , 20160422 , 20160423
     */
    public static String[] getWeekMonSun(String date){

        Calendar cal = Calendar.getInstance();
        int yy =Integer.parseInt(date.substring(0, 4));
        int mm =Integer.parseInt(date.substring(4, 6))-1;
        int dd =Integer.parseInt(date.substring(6, 8));
        cal.set(yy, mm,dd);
//        }
        String[] arrYMD = new String[7];

        int inYear = cal.get(cal.YEAR);
        int inMonth = cal.get(cal.MONTH);
        int inDay = cal.get(cal.DAY_OF_MONTH);
        int yoil = cal.get(cal.DAY_OF_WEEK); //요일나오게하기(숫자로)
        if(yoil != 1){   //해당요일이 일요일이 아닌경우
            yoil = yoil-2;
        }else{           //해당요일이 일요일인경우
            yoil = 7;
        }
        inDay = inDay-yoil;
        for(int i = 0; i < 7;i++){
            cal.set(inYear, inMonth, inDay+i);  //
            String y = Integer.toString(cal.get(cal.YEAR));
            String m = Integer.toString(cal.get(cal.MONTH)+1);
            String d = Integer.toString(cal.get(cal.DAY_OF_MONTH));
            if(m.length() == 1) m = "0" + m;
            if(d.length() == 1) d = "0" + d;

            arrYMD[i] = y+m +d;

        }

        return arrYMD;
    }


    /**
     * 두개의 날짜 비교
     * @param yourdate 비교하고싶은 날짜
     * @param dateformat 데이터포멧형식 ex:yyyyMMdd
     * @return 비교하고싶은 날짜와 현재날짜의 비교한 결과값
     */
    public static long getTwoDateCompare(String yourdate , String dateformat) {
        long diffDays = 0;
        long diff = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateformat);
            Date beginDate = formatter.parse(yourdate);
            Date endDate = new Date();

            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            diff = endDate.getTime() - beginDate.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);

        }catch (Exception e){
            CLog.e(e.toString());
        }

        return diffDays;
    }


    /**
     * 해당 요일의 일~월 구하기
     * @param date 날짜 ex:20160614
     * @return 결과값 ex:20160612 ,20160613 , 20160614 , 20160615 , 20160616 , 20160617 , 20160618
     */
    public static String[] getWeekSunMon(String date){

//        CLog.i("date --> " + date);

        Calendar cal = Calendar.getInstance();
        int yy =Integer.parseInt(date.substring(0, 4));
        int mm =Integer.parseInt(date.substring(4, 6))-1;
        int dd =Integer.parseInt(date.substring(6, 8));
        cal.set(yy, mm,dd);
//        }
        String[] arrYMD = new String[7];

        int inYear = cal.get(cal.YEAR);
        int inMonth = cal.get(cal.MONTH);
        int inDay = cal.get(cal.DAY_OF_MONTH);
        int yoil = cal.get(cal.DAY_OF_WEEK); //요일나오게하기(숫자로)
//        if(yoil != 1){   //해당요일이 일요일이 아닌경우
            yoil = yoil-2;
//        }else{           //해당요일이 일요일인경우
//            yoil = -1;
//        }
        inDay = inDay-yoil;
        for(int i = 0; i < 7;i++){
            cal.set(inYear, inMonth, inDay+i-1);  //
            String y = Integer.toString(cal.get(cal.YEAR));
            String m = Integer.toString(cal.get(cal.MONTH)+1);
            String d = Integer.toString(cal.get(cal.DAY_OF_MONTH));
            if(m.length() == 1) m = "0" + m;
            if(d.length() == 1) d = "0" + d;

            arrYMD[i] = y+m +d;

//            CLog.i("arrYMD[" + i + "] ---> " + arrYMD[i]);
        }

        return arrYMD;
    }



    /**
     * 해당 날짜에 요일 구하기
     * @param date  날짜
     * @param date_format   데이트포멧타입
     * @return 요일
     */
    public static String getSunDayWeek(String date, String date_format) {

        Calendar calendar = Calendar.getInstance();
        Date weekkDate = getDateFormat(date, date_format);

        int year = weekkDate.getYear() + 1900;
        int month = weekkDate.getMonth() ;
        int day = weekkDate.getDate();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        String week = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week = "(일)";
                break;
            case 2:
                week = "(월)";
                break;
            case 3:
                week = "(화)";
                break;
            case 4:
                week = "(수)";
                break;
            case 5:
                week = "(목)";
                break;
            case 6:
                week = "(금)";
                break;
            case 7:
                week = "(토)";
                break;
        }


        return week;
    }

    /**
     * 숫자를 2자리 수로 표현
     * @param num 숫자
     * @return
     */
    public static String getTwoDateFormat(int num){
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(num);
    };
	
	
    /**
     * yyyy-MM-dd 형식의 문자열을 Date 형태로 변환
     * @param date 날짜 문자열
     * @param date_format 패턴
     * @return
     */
    public static Date getDateFormat(String date, String date_format){
        SimpleDateFormat mFormat = new SimpleDateFormat(date_format);
        Date mDate = null;
        try {
            mDate = mFormat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mDate;
    }


    /**
     * 중복제거
     * @param str   문자열
     * @param token 구분자
     * @return 중복제거된 문자열
     */
    public static String removeDuplicateStringToken(String str, String token){
        String removeDubString="";
        String[] array = TextUtils.split(str, token.trim());
        TreeSet ts=new TreeSet();
        for(int i=0; i<array.length; i++){
            ts.add(array[i]);
        }
        Iterator it=ts.iterator();
        while(it.hasNext()){
            removeDubString+=(String)it.next()+token.trim();
        }
        removeDubString=removeDubString.substring(0, removeDubString.lastIndexOf(token.trim()));


        return removeDubString;
    }

    public static String Comma_won(int junsu) {
        DecimalFormat Commas = new DecimalFormat("#,###");
        String result_int = (String)Commas.format(junsu);
        return result_int;
    }
}

