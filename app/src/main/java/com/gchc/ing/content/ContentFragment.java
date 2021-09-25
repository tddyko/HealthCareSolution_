package com.gchc.ing.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gchc.ing.R;
import com.gchc.ing.base.BaseFragment;
import com.gchc.ing.base.CommonActionBar;
import com.gchc.ing.base.IBaseFragment;
import com.gchc.ing.question.QuestionnaireActivity;
import com.gchc.ing.util.Logger;

/**
 * 스마트 헬스케어 서비스 소개 화면
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class ContentFragment extends BaseFragment implements IBaseFragment {

	private final String TAG = ContentFragment.class.getSimpleName();

	public static final String WEBVIEW_URL       = "webview_url";

	private WebView mWebView;
	private Context con;
	Handler handler = null;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.content_health_fragment, container, false);

		String sAddress = getArguments().getString(WEBVIEW_URL);
		con = getActivity();
		handler = new Handler();


		mWebView = (WebView) view.findViewById(R.id.webview);
		mWebView.loadUrl(sAddress);
//		mWebView.loadData(creteHtmlBody(sAddress), "text/html", "utf-8");
//		mWebView.loadDataWithBaseURL(null, creteHtmlBody(sAddress), "text/html", "utf-8", null);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new AndroidBridge(), "android");
		mWebView.setWebViewClient(new WebViewClientClass());
		mWebView.getSettings().setDefaultZoom(mWebView.getSettings().getDefaultZoom().FAR);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.setInitialScale( 1 );

		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDefaultTextEncodingName("euc-kr");
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(final WebView view, final String url, final String message, JsResult result) {
				Toast tos = Toast.makeText(con, message, Toast.LENGTH_LONG);
				tos.setGravity(Gravity.CENTER, 0, 0);
				tos.show();
				result.confirm();
				return true;
			}
		});

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);


	}

	@Override
	public void loadActionbar(CommonActionBar actionBar) {
		super.loadActionbar(actionBar);
		actionBar.setActionBarTitle("건강정보");
	}

	public  String creteHtmlBody(String imagUrl){
		StringBuffer sb = new StringBuffer("<HTML>");
		sb.append("<HEAD>");
		sb.append("</HEAD>");
		sb.append("<BODY  style='margin: 0; padding: 0'>");
		sb.append("<img width=\"100%\" src=\"" + imagUrl+"\">");
		sb.append("</BODY>");
		sb.append("</HTML>");

		return sb.toString();
	}	

	/**
	 * JavascriptInterface 
	 */
	private class AndroidBridge {
		@SuppressWarnings("unused")
		public void setMessage(final String arg) {
			handler.post(new Runnable() {
				public void run() {
					Logger.i(TAG, "contenfragment setmessage call");
				}
			});
		}
	}
	/**
	 * WebViewClient
	 */
	private class WebViewClientClass extends WebViewClient { 
		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, final String url) { 
			Logger.i(TAG, "contenfragment WebViewClientClass call");
			view.loadUrl(url); 

			return true; 
		} 
	}

}
