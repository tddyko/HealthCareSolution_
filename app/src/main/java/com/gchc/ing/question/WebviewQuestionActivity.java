package com.gchc.ing.question;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.gchc.ing.MainActivity;
import com.gchc.ing.base.BaseFragment;
import com.gchc.ing.base.CommonActionBar;
import com.gchc.ing.base.value.Define;
import com.gchc.ing.network.tr.ApiData;
import com.gchc.ing.network.tr.data.Tr_hra_check_result_input;
import com.gchc.ing.network.tr.data.Tr_login;
import com.gchc.ing.question.common.RequestUtil;
import com.gchc.ing.util.Logger;
import com.gchc.ing.util.StringUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import com.gchc.ing.R;
import com.gchc.ing.question.common.Defined;
import com.gchc.ing.question.common.FileManager;
import com.gchc.ing.question.common.UserInfo;
import com.gchc.ing.question.common.EServerAPI;
import com.gchc.ing.question.common.Record;
import com.gchc.ing.question.common.CLog;

import static android.view.PixelCopy.request;

/**
 * 질병 위험도 체크.
 */
// implements ActionSheet.ActionSheetListener{
public class WebviewQuestionActivity extends BaseFragment {

    public static final String TITLE     = "title";
    public static final String URL       = "url";
    public static final String POS       = "pos";

    private WebView activity_question_WebView_webview;
    private final Handler handler = new Handler();
    private boolean isContent = false;
    private String content = "";
    private int titlePos = 0;
    private int targetPos = 0;
    private int mSum = 0;
    private String sUrl = "";

    public static Fragment newInstance() {
        WebviewQuestionActivity fragment = new WebviewQuestionActivity();
        return fragment;
    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
        super.loadActionbar(actionBar);
        actionBar.setActionBarTitle(initTitle(titlePos));       // 액션바 타이틀
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_question_webview, container, false);

        titlePos = StringUtil.getIntVal(getArguments().getString(TITLE));
        targetPos = StringUtil.getIntVal(getArguments().getString(POS));
        sUrl = getArguments().getString(URL);

        init(view);

        return view;
    }
    private String initTitle(int position)
    {

        switch (position) {
            case 0:
                return getString(R.string.question1);
            case 1:
                return getString(R.string.question2);
            case 2:
                return getString(R.string.question3);
            case 3:
                return getString(R.string.question4);
            case 4:
                return getString(R.string.question5);
            case 5:
                return getString(R.string.question6);
            case 6:
                return getString(R.string.question7);
            case 7:
                return getString(R.string.question8);
            case 8:
                return getString(R.string.question9);
            case 9:
                return getString(R.string.question10);
            case 100:
                return getString(R.string.answer);
            case 101:
                return "서비스 안내";

        }
        return "서비스 안내";
    }

    private void init(View view) {

        activity_question_WebView_webview = (WebView) view.findViewById(R.id.activity_question_WebView_webview);
        activity_question_WebView_webview.getSettings().setJavaScriptEnabled(true);
        activity_question_WebView_webview.setWebViewClient(new WebViewClient());
        activity_question_WebView_webview.getSettings().setDefaultTextEncodingName("utf-8");
        activity_question_WebView_webview.getSettings().setBuiltInZoomControls(true); // 줌 허용
        activity_question_WebView_webview.getSettings().setSupportZoom(true);
        activity_question_WebView_webview.getSettings().setDisplayZoomControls(false);
        activity_question_WebView_webview.addJavascriptInterface(new AndroidBridge(), "HybridApp");
        activity_question_WebView_webview.setWebViewClient(new WebViewClientClass());
        activity_question_WebView_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(getContext())
                        .setTitle("알림")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }
        });

        if(titlePos < 100)
        {
            activity_question_WebView_webview.loadUrl(sUrl);
        }
        else if(titlePos == 100) // 진단 결과
        {
            UserInfo userInfo = new UserInfo(getContext());
            int pos = targetPos;
            mSum = userInfo.getQuestionSum(pos);

            Tr_hra_check_result_input.RequestData requestData = new Tr_hra_check_result_input.RequestData();
            Tr_login login = Define.getInstance().getLoginInfo();

            requestData.insures_code = "301";
            requestData.mber_sn = login.mber_sn;
            requestData.total_score = Integer.toString(mSum);
            requestData.moon_key = Integer.toString(targetPos);

            getData(getContext(), Tr_hra_check_result_input.class, requestData, new ApiData.IStep() {
                @Override
                public void next(Object obj) {
                    if (obj instanceof Tr_hra_check_result_input) {
                        Tr_hra_check_result_input data = (Tr_hra_check_result_input) obj;

                        int level = Integer.parseInt(data.d_level);
                        UserInfo userInfo = new UserInfo(getContext());
                        CLog.i("SAVE : " + targetPos + " / " + level + " / " + mSum);

                        userInfo.setQuestion(targetPos, level, mSum);

                        String str = data.comment;
                        str = str.replaceAll("&lt;", "<");
                        str = str.replaceAll("&gt;", ">");
                        str = str.replaceAll("\n" , "");
                        str = str.replaceAll("\r", "");
                        str = str.replaceAll("\t", "");

                        updateLayoutAnswer(str);

                    }
                }
            });
        }
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("mailto:") || url.startsWith(("tel:")))
                return false;
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(isContent)
            {
                isContent = false;
                activity_question_WebView_webview.loadUrl(String.format("javascript:sendContent('%s');",  content));
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    private void updateLayoutAnswer(final String comment)
    {
        titlePos = 100;
        loadActionbar(getCommonActionBar());
        content = comment;
        isContent = true;
        activity_question_WebView_webview.loadUrl("file:///android_asset/answer.html");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void sendSum(String no, String sum) {
            Log.i("SEND", "no = " + no + " sum = " + sum);

            titlePos = 100;
            targetPos = Integer.parseInt(no);
            mSum = Integer.parseInt(sum);

            Tr_hra_check_result_input.RequestData requestData = new Tr_hra_check_result_input.RequestData();

            requestData.insures_code = "301";
            requestData.mber_sn = "1121";
            requestData.total_score = sum;
            requestData.moon_key = no;

            getData(getContext(), Tr_hra_check_result_input.class, requestData, new ApiData.IStep() {
                @Override
                public void next(Object obj) {
                    if (obj instanceof Tr_hra_check_result_input) {
                        Tr_hra_check_result_input data = (Tr_hra_check_result_input) obj;

                        int level = Integer.parseInt(data.d_level);
                        UserInfo userInfo = new UserInfo(getContext());
                        CLog.e("SAVE : " + targetPos + " / " + level + " / " + mSum);

                        userInfo.setQuestion(targetPos, level, mSum);

                        String str = data.comment;
                        str = str.replaceAll("&lt;", "<");
                        str = str.replaceAll("&gt;", ">");
                        str = str.replaceAll("\n" , "");
                        str = str.replaceAll("\r", "");
                        str = str.replaceAll("\t", "");

                        updateLayoutAnswer(str);

                    }
                }
            });

        }

        @JavascriptInterface
        public void sendLink(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // bmi_input question html에 호출하는 태그가 없음
                }
            });
        }

        @JavascriptInterface
        public void sendReCall() {
            handler.post(new Runnable()
            {
                public void run()
                {
                    int stitle = StringUtil.getIntVal(getArguments().getString(TITLE));
                    int spos = StringUtil.getIntVal(getArguments().getString(POS));

                    Logger.i("sendReCall", " [stitle] : "+stitle+" [surl] : "+sUrl+" [spos] : " +spos);
                    titlePos = spos - 1;
                    targetPos = spos;

                    loadActionbar(getCommonActionBar());
                    content = "";
                    isContent = false;
                    activity_question_WebView_webview.loadUrl(sUrl);
                }
            });
        }

        @JavascriptInterface
        public void sendFinsh() {
            handler.post(new Runnable() {
                public void run() {
                    onBackPressed();
                }
            });
        }
    }
}
