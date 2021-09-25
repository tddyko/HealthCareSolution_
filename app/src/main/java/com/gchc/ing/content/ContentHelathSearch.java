package com.gchc.ing.content;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gchc.ing.R;
import com.gchc.ing.component.CDialog;
import com.gchc.ing.content.common.HttpAsyncTaskInterface;
import com.gchc.ing.content.common.MyScrollView;
import com.gchc.ing.content.common.MyScrollView.OnScrollStoppedListener;
import com.gchc.ing.content.common.Utils;
import com.gchc.ing.base.BaseFragment;

/**
 * 건강컨텐츠 메인 화면<br/>
 *
 */
public class ContentHelathSearch extends BaseFragment implements OnClickListener, HttpAsyncTaskInterface {
	private FragmentActivity ac;
//	private Context con;
	private int nowPage = 1;
	private int maxPage = 1;
	private boolean mLockListView;

	private LinearLayout customerVoiceListViewtest;

	private MyScrollView scroll;
	private List<ContentBean> customerDataList;

	private boolean scrollcheck = false;

	private List<ContentBean> tempList;

	private LayoutInflater inflater;
	
	String sSearched;
	TextView headTitle;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_health_search_content, null);	
		this.inflater = inflater;
		if(Utils.TEST){
			Log.i(Utils.TAG, "ContentHelathSearch");
		}
//		con = getActivity();
		ac = getActivity();
		
		/*TextView headTitle = (TextView)view.findViewById(R.id.headTitle);
		headTitle.setText(getString(R.string.healthContents));*/
		sSearched = getArguments().getString("sSearched");
		headTitle = (TextView)view.findViewById(R.id.tv_result);
		headTitle.setText("'"+sSearched+"' 포함된 결과입니다.");
		
		customerVoiceListViewtest = (LinearLayout)view.findViewById(R.id.customerVoiceListViewtest_search);

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
						customerRoadData();
					}
				}
			}
		});
		
		
		initFrame();
		return view;    	
	}
	
	/**
	 * 초기 설정
	 */
	private void initFrame() {
		mLockListView = true;

		Utils.progressBar.setVisibility(View.GONE);

		customerRoad();
	}    

	/**
	 * 모두보기 데이터 취득
	 */
	public void customerRoadData() {
//		String param = getString(R.string.content_Request_search_news, Utils.INSURESCODE, Utils.pref.getString("USER_NUMBER", ""),nowPage, sSearched );
//		if(Utils.TEST){
//			Log.i(Utils.TAG, "ContentHelathSearch customerRoadData param : "+param);
//			Log.i(Utils.TAG, "ContentHelathSearch customerRoadData "+ (Utils.pref.getString("apiURL", "")+Utils.URL_STR));
//		}
//		HttpAsyncTask rssTask = new HttpAsyncTask(this, Utils.pref.getString("apiURL", "")+Utils.URL_STR);
//		rssTask.setParam(param);
//		rssTask.execute();

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
	 * 모두보기 데이터 취득 및 화면 설정
	 */
	private void customerRoad() {
		customerVoiceListViewtest.setVisibility(View.VISIBLE);
		nowPage = 1;
		customerVoiceListViewtest.removeAllViews();

//		Utils.imageViewList = new ArrayList<ImageView>();
//		Utils.Sview = new ArrayList<View>();

		if (customerDataList != null) {
			customerDataList = new ArrayList<ContentBean>();
		} 
		customerRoadData();
	}

	public void onPreExecute() {
		Utils.progressBar.setVisibility(View.VISIBLE);
	}

	public void onPostExecute(String data) {
		if(Utils.TEST){
			Log.i(Utils.TAG, "ContentHelathSearch data : "+data);
		}
		Utils.progressBar.setVisibility(View.GONE);
		try {
			setCustomerAdapter(data);
		} catch (Exception e) {
		}
		mLockListView = false;
	}

	public void onError() {
		mLockListView = false;
	}    

	/**
	 * 모두보기 리스트 표시
	 * @param data
	 */
	private void setCustomerAdapter(String data) {
		
		if (customerDataList == null) {
			customerDataList = new ArrayList<ContentBean>();
		}
		tempList = new ArrayList<ContentBean>();
		try {
			String jsonData = Utils.getJosonDataFromXML(data);
			Object obj = JSONValue.parse(jsonData);
			JSONObject JSONONotice = (JSONObject)obj;

			String maxPageTxt = (String)JSONONotice.get("maxpageNumber");
			maxPage = Utils.getNewsMaxPageNum(maxPageTxt);

			if(Utils.TEST){
				Log.i(Utils.TAG, "ContentHelathSearch maxPage "+maxPage);
			}

			JSONArray object = (JSONArray)JSONONotice.get("bbslist");
			if(object == null || object.size()==0){
				headTitle.setText("'"+sSearched+"' 포함된 결과가 없습니다.");
			}
			if(object != null){
				for(int i =0 ; i < object.size(); i++) {
					JSONObject oj = (JSONObject)object.get(i);
					
					ContentBean temp = new ContentBean();
					temp.setTitle((String)oj.get("info_subject"));
//					temp.setContent((String)oj.get("g_content"));
					temp.setAddress((String)oj.get("info_title_url"));
					temp.setDay((String)oj.get("info_day"));

					tempList.add(temp);
				}
			}

			for (int i = 0; i < tempList.size(); i++) {
				customerDataList.add(tempList.get(i));
			}

			View vi;

			TextView contentTitle;
			TextView contenDay;
			ImageView contentIView;

			String sDay;

			for (int i = 0; i < tempList.size(); i++) {
				vi = inflater.inflate(R.layout.content_list_row, null);
				contentTitle = (TextView) vi.findViewById(R.id.content_title);
				contenDay = (TextView) vi.findViewById(R.id.content_day);
				contentIView = (ImageView) vi.findViewById(R.id.content_bar);
				final ContentBean temp = tempList.get(i);

				contentTitle.setText(temp.getTitle());
				if(temp.getDay().length()==8){
					sDay = temp.getDay();
					sDay = sDay.substring(0, 4)+"."+sDay.substring(4, 6)+"."+sDay.substring(6);
					if(Utils.TEST){
						Log.i(Utils.TAG, "ContentHelathSearch sDay "+sDay+ " content "+temp.getContent()+ " address "+temp.getAddress());
					}
					contenDay.setText(sDay);
				}
				if(i == tempList.size()+1){
					contentIView.setVisibility(View.GONE);
				}else{
					contentIView.setVisibility(View.VISIBLE);	
				}
				
				vi.setOnClickListener(null);

				customerVoiceListViewtest.addView(vi);

				vi .setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Bundle args = new Bundle();
						args.putString("content", temp.getAddress()/*+temp.getContent()*/);
						if(Utils.TEST){
							Log.d(Utils.TAG, "ContentHelathSearch content: "+temp.getAddress());
						}
//						AppManageHelper.addTabMenu(ac, TAB_MENU.MAIN, ContentFragment.class, getString(R.string.healthContents), true, args);
					}
				});
			}

			if(maxPage  == nowPage){
				scrollcheck = false;
			}else{
				nowPage++;
				scrollcheck = true;
			}
		} catch (Exception e) {
//			Utils.dialog(con, getString(R.string.notgetData));
			if(Utils.TEST)Log.e("hsh"," "+e);

			CDialog.showDlg(getContext(), getString(R.string.message_nowtime_over), new CDialog.DismissListener() {
				@Override
				public void onDissmiss() {

				}
			});
		}
	}

	@Override
	public void onClick(View v) {
	}	
}