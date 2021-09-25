package com.gchc.ing.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.gchc.ing.R;
import com.gchc.ing.base.CommonActionBar;
import com.gchc.ing.base.DummyActivity;
import com.gchc.ing.component.CDialog;
import com.gchc.ing.component.CFontEditText;
import com.gchc.ing.content.common.HttpAsyncTaskInterface;
import com.gchc.ing.content.common.MyScrollView;
import com.gchc.ing.content.common.MyScrollView.OnScrollStoppedListener;
import com.gchc.ing.base.BaseFragment;
import com.gchc.ing.network.tr.ApiData;
import com.gchc.ing.network.tr.data.Tr_content_special_bbslist;
import com.gchc.ing.network.tr.data.Tr_content_special_bbslist_search;
import com.gchc.ing.network.tr.data.Tr_get_hedctdata;
import com.gchc.ing.question.QuestionnaireActivity;
import com.gchc.ing.util.StringUtil;

/**
 * 건강컨텐츠 메인 화면<br/>
 *
 */
public class ContentHelath extends BaseFragment implements OnClickListener, HttpAsyncTaskInterface {
	private FragmentActivity ac;
//	private Context con;
	private int nowPage = 1;
	private int maxPage = 1;

	private boolean mDataStateList = true;
	private boolean mLockListView = false;
	private boolean scrollcheck = true;

	private LinearLayout customerVoiceListViewtest;

	private MyScrollView scroll;

	private LayoutInflater inflater;

	CFontEditText et_search;
	Button btn_search;

	public static Fragment newInstance() {
		ContentHelath fragment = new ContentHelath();
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_health_content, container, false);
		this.inflater = inflater;

		return view;
	}

	@Override
	public void loadActionbar(CommonActionBar actionBar) {
		super.loadActionbar(actionBar);
		actionBar.setActionBarTitle("건강정보");
		actionBar.setActionBarWriteBtn(QuestionnaireActivity.class, new Bundle());// 액션바 입력 버튼
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ac = getActivity();
		et_search = (CFontEditText)view.findViewById(R.id.et_search);
		et_search.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				switch (actionId) {
					case EditorInfo.IME_ACTION_DONE:
						hideKeyBoard();
						inithealthSearchDataSet();
						break;
					default:
						break;
				}
				return true;
			}
		});

		btn_search = (Button)view.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyBoard();
				inithealthSearchDataSet();
			}
		});

		customerVoiceListViewtest = (LinearLayout)view.findViewById(R.id.customerVoiceListViewtest);

		scroll = (MyScrollView)view.findViewById(R.id.scroll);
		scroll.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					scroll.startScrollerTask();
				}
				return false;
			}
		});

		scroll.setOnScrollStoppedListener(new OnScrollStoppedListener() {
			public void onScrollStopped() {
				int maxPosition = scroll.getChildAt(0).getMeasuredHeight() - scroll.getHeight();
				if(maxPosition == scroll.getScrollY() && mLockListView == false) {
					if(scrollcheck){

						mLockListView = true;

						if(mDataStateList){
							healthListDataSet();
						}else{
							healthSearchDataSet();
						}

					}
				}
			}
		});

		initFrame();

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 초기 설정
	 */
	private void initFrame() {
		mLockListView = true;
		mDataStateList = true;

		showProgress();
		hideProgress();

		customerRoad();
	}

	/**
	 * 모두보기 데이터 취득 및 화면 설정
	 */
	private void customerRoad() {
		customerVoiceListViewtest.setVisibility(View.VISIBLE);
		customerVoiceListViewtest.removeAllViews();

		nowPage = 1;

		healthListDataSet();
	}

	/**
	 * 리스트 API로 데이터 셋
	 */
	public void healthListDataSet(){
		Tr_content_special_bbslist.RequestData requestData = new Tr_content_special_bbslist.RequestData();

		requestData.insures_code = "301";
		requestData.pageNumber = Integer.toString(nowPage);

		getData(getContext(), Tr_content_special_bbslist.class, requestData, new ApiData.IStep() {
			@Override
			public void next(Object obj) {
				if (obj instanceof Tr_content_special_bbslist) {
					Tr_content_special_bbslist data = (Tr_content_special_bbslist) obj;

					View vi;
					TextView contentTitle;
					TextView contenDay;
					ImageView contentIView;
					String sDay;

					maxPage = StringUtil.getIntVal(data.maxpageNumber);

					for (int i = 0; i < data.bbslist.size(); i++) {
						vi = inflater.inflate(R.layout.content_list_row, null);
						contentTitle = (TextView) vi.findViewById(R.id.content_title);
						contenDay = (TextView) vi.findViewById(R.id.content_day);
						contentIView = (ImageView) vi.findViewById(R.id.content_bar);
						final Tr_get_hedctdata.DataList temp = data.bbslist.get(i);

						contentTitle.setText(temp.info_subject);
						if(temp.info_day.length()==8){
							sDay = temp.info_day;
							sDay = sDay.substring(0, 4)+"."+sDay.substring(4, 6)+"."+sDay.substring(6);
							contenDay.setText(sDay);
						}
						if(i == data.bbslist.size()+1){
							contentIView.setVisibility(View.GONE);
						}else{
							contentIView.setVisibility(View.VISIBLE);
						}

						vi.setOnClickListener(null);

						customerVoiceListViewtest.addView(vi);

						vi .setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Bundle bundle = new Bundle();
								bundle.putString(ContentFragment.WEBVIEW_URL, temp.info_title_url);
//								movePage(ContentFragment, bundle);

								BaseFragment visibleFragment = (BaseFragment) getVisibleFragment();
								DummyActivity.startActivityForResult(visibleFragment, 1111, ContentFragment.class, bundle);
							}
						});
					}

					if(maxPage  == nowPage){
						mLockListView = true;
						scrollcheck = false;
					}else{
						nowPage++;
						mLockListView = false;
						scrollcheck = true;
					}

				}
			}
		});
	}

	/**
	 * 검색리스트 API로 데이터 셋
	 */
	public void healthSearchDataSet(){
		String sSearched = et_search.getText().toString();
		if(sSearched.length()>0){

			mDataStateList = false;

			Tr_content_special_bbslist_search.RequestData requestData = new Tr_content_special_bbslist_search.RequestData();

			requestData.insures_code = "301";
			requestData.bbs_title = sSearched;
			requestData.pageNumber = Integer.toString(nowPage);

			getData(getContext(), Tr_content_special_bbslist_search.class, requestData, new ApiData.IStep() {
				@Override
				public void next(Object obj) {
					if (obj instanceof Tr_content_special_bbslist_search) {
						Tr_content_special_bbslist_search data = (Tr_content_special_bbslist_search) obj;

						if(data.bbslist == null){
							CDialog.showDlg(getContext(), "결과값이 없습니다.");
							return;
						}

						View vi;
						TextView contentTitle;
						TextView contenDay;
						ImageView contentIView;
						String sDay;

						maxPage = StringUtil.getIntVal(data.maxpageNumber);

						for (int i = 0; i < data.bbslist.size(); i++) {
							vi = inflater.inflate(R.layout.content_list_row, null);
							contentTitle = (TextView) vi.findViewById(R.id.content_title);
							contenDay = (TextView) vi.findViewById(R.id.content_day);
							contentIView = (ImageView) vi.findViewById(R.id.content_bar);
							final Tr_get_hedctdata.DataList temp = data.bbslist.get(i);

							contentTitle.setText(temp.info_subject);
							if(temp.info_day.length()==8){
								sDay = temp.info_day;
								sDay = sDay.substring(0, 4)+"."+sDay.substring(4, 6)+"."+sDay.substring(6);
								contenDay.setText(sDay);
							}
							if(i == data.bbslist.size()+1){
								contentIView.setVisibility(View.GONE);
							}else{
								contentIView.setVisibility(View.VISIBLE);
							}

							vi.setOnClickListener(null);

							customerVoiceListViewtest.addView(vi);

							vi .setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Bundle bundle = new Bundle();
									bundle.putString(ContentFragment.WEBVIEW_URL, temp.info_title_url);
//								movePage(ContentFragment, bundle);

									BaseFragment visibleFragment = (BaseFragment) getVisibleFragment();
									DummyActivity.startActivityForResult(visibleFragment, 1111, ContentFragment.class, bundle);
								}
							});
						}

						if(maxPage  == nowPage){
							mLockListView = true;
							scrollcheck = false;
						}else{
							nowPage++;
							mLockListView = false;
							scrollcheck = true;
						}

					}
				}
			});
		}else{
			initFrame();
		}
	}

	public void inithealthSearchDataSet(){
		customerVoiceListViewtest.removeAllViews();

		nowPage = 1;

		healthSearchDataSet();
	}

	public void onPreExecute() {
		hideProgress();
	}

	public void onPostExecute(String data) {

		hideProgress();

		mLockListView = false;
	}

	public void onError() {
		mLockListView = false;
	}

	private void hideKeyBoard() {
		et_search.clearFocus();
		et_search.setFocusable(false);
		et_search.setFocusableInTouchMode(true);
		InputMethodManager imm = (InputMethodManager) ac.getSystemService(ac.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
	}
	
	@Override
	public void onClick(View v) {

	}	
}