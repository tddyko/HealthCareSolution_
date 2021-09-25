package com.gchc.ing.Service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.support.v4.app.Fragment;

import com.gchc.ing.R;
import com.gchc.ing.base.BaseFragment;
import com.gchc.ing.base.CommonActionBar;
import com.gchc.ing.base.IBaseFragment;

/**
 * Created by godaewon on 2017. 6. 14..
 */

public class ServiceManageFragment extends BaseFragment implements IBaseFragment {
    private WebView mWebView;

    public static Fragment newInstance() {
        ServiceManageFragment fragment = new ServiceManageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_health_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String sAddress = "http://insu.greenpio.com/ing/life.html";

        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.loadUrl(sAddress);
        mWebView.getSettings().setJavaScriptEnabled(false);
    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
        super.loadActionbar(actionBar);
        actionBar.setActionBarTitle("서비스 안내/신청");
    }

}
