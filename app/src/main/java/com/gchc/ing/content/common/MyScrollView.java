package com.gchc.ing.content.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * ScrollView(사용처 : 고객의 소리)
 *
 */
public class MyScrollView extends ScrollView{

	private Runnable scrollerTask;
//	private int initialPosition;

	private int newCheck = 100;
	//private static final String TAG = "MyScrollView";

	public interface OnScrollStoppedListener{
		void onScrollStopped();
	}

	private OnScrollStoppedListener onScrollStoppedListener;

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

		scrollerTask = new Runnable() {

			public void run() {
				int newPosition = getScrollY();
				int maxPosition = getChildAt(0).getMeasuredHeight() - getHeight();
//				Log.i(Utils.TAG, "newPosition: "+newPosition);
//				Log.i(Utils.TAG, "maxPosition: "+maxPosition);
				if(newPosition == maxPosition || newPosition == 0){//has stopped
					if(onScrollStoppedListener!=null){
						onScrollStoppedListener.onScrollStopped();
					}
				}else{
					MyScrollView.this.postDelayed(scrollerTask, newCheck);
				}
			}
		};
	}

	public void setOnScrollStoppedListener(MyScrollView.OnScrollStoppedListener listener){
		onScrollStoppedListener = listener;
	}

	public void startScrollerTask(){

//		initialPosition = 0;
		MyScrollView.this.postDelayed(scrollerTask, newCheck);
	}

}