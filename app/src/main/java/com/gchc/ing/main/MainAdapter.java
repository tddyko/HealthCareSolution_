package com.gchc.ing.main;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.gchc.ing.R;
import com.gchc.ing.base.BaseFragment;
import com.gchc.ing.base.value.Define;
import com.gchc.ing.content.ContentHelath;
import com.gchc.ing.database.DBHelper;
import com.gchc.ing.database.DBHelperFoodMain;
import com.gchc.ing.database.DBHelperSugar;
import com.gchc.ing.database.DBHelperWeight;
import com.gchc.ing.food.FoodManageFragment;
import com.gchc.ing.network.tr.data.Tr_get_hedctdata;
import com.gchc.ing.network.tr.data.Tr_login;
import com.gchc.ing.sample.CircleProgressBar;
import com.gchc.ing.sugar.SugarManageFragment;
import com.gchc.ing.util.Logger;
import com.gchc.ing.util.StringUtil;
import com.gchc.ing.weight.WeightManageFragment;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private final String TAG = MainAdapter.class.getSimpleName();

    private BaseFragment mBaseFragment;
    private List<MainCardData> mainCardList;
    private LinearLayout mCardTintLayout;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout progressLayout;
        public ImageView addItemIv;
        public CardView cardview;
        public CircleProgressBar progress;
        public TextView valueTv;
        public TextView titleTv;

        private LinearLayout foregroundLayout;

        public MyViewHolder(View view) {
            super(view);

            progressLayout = (FrameLayout) view.findViewById(R.id.main_progress_layout);
            addItemIv = (ImageView) view.findViewById(R.id.main_add_btn);

            foregroundLayout = (LinearLayout) view.findViewById(R.id.cardview_tint_layout);
            cardview = (CardView) view.findViewById(R.id.card_view);
            progress = (CircleProgressBar) itemView.findViewById(R.id.card_progressBar);
            valueTv = (TextView) itemView.findViewById(R.id.main_progress_value);
            titleTv = (TextView) itemView.findViewById(R.id.main_progress_title);

            //main touch fasl
            foregroundLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mCardTintLayout = foregroundLayout;
                    }
                    return false;
                }
            });

        }
    }


    public void setDragState(boolean isDrag) {
        if (mCardTintLayout != null) {
            if (isDrag)
                mCardTintLayout.setBackgroundResource(R.drawable.draw_circle_44444444);
            else
                mCardTintLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        if (isDrag == false) {
            // 변경된 순서 Sqlite 저장
            DBHelper dbHelper = new DBHelper(mBaseFragment.getContext());
            int idx = 0;
            for (MainCardData data : mainCardList) {
                if (mainCardList != null) {
                    dbHelper.updateIdx(idx++, data.getCardE().name());
                }
            }

            if (Logger.mUseLogSetting)
                dbHelper.getResult();
        }
    }

    public MainAdapter(BaseFragment basefragment) {
        this.mBaseFragment = basefragment;
    }

    public void setData(List<MainCardData> mainCardList) {
        this.mainCardList = mainCardList;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_view, parent, false);
        itemView.setBackgroundColor(Color.TRANSPARENT);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MainCardData mainCard = mainCardList.get(position);
        final MainCardData.CardE data = mainCard.getCardE();

        if (data == MainCardData.CardE.ADD) {
            // 추가버튼 세팅
            holder.progressLayout.setVisibility(View.INVISIBLE);
            holder.addItemIv.setVisibility(View.VISIBLE);   // 추가버튼 보이기
//            holder.foregroundLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //건간정보
//
//
//                    //그린케어
//                    new MainAddDialog(mBaseFragment.getContext(), mainCardList, new MainAddDialog.IAddDialog() {
//                        @Override
//                        public void onDismiss() {
//                            ((MainFragment) mBaseFragment).prepareMainItems();
//                        }
//                    }).show();
//                }
//            });
//            return;
        } else {
            holder.progressLayout.setVisibility(View.VISIBLE);
            holder.addItemIv.setVisibility(View.GONE);
        }

        holder.titleTv.setText(String.format("%,d", mainCard.getValue()));
        holder.titleTv.setTextColor(mainCard.getCardColor());
        holder.titleTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, mainCard.getCardImage(), 0, 0);

        holder.valueTv.setText(data.getCardName());

        holder.progress.setStrokeWidth(20f);   // 프로그래스 두께
        holder.progress.setColor(mainCard.getCardColor());

        holder.progress.setMax(mainCard.getMaxValue());         // 최대값
        holder.progress.setProgress(mainCard.getValue());       // 입력 값

        holder.foregroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveMenu(data);
            }
        });

        if (data == MainCardData.CardE.SUGAR) {
            setProgressSugar(holder.progress, holder.titleTv);
            holder.valueTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        }

        if (data == MainCardData.CardE.WEIGHT) {
            setProgressWeight(holder.progress, holder.titleTv, holder);
        }

        if (data == MainCardData.CardE.FOOD) {
            setProgressFood(holder.progress, holder.titleTv, holder);
        }

        // 3개월치 데이터 넣기
        int reqDataType = position + 1;
    }

    private void moveMenu(MainCardData.CardE data) {

        if(data == MainCardData.CardE.ADD){
            mBaseFragment.movePage(ContentHelath.newInstance());
        }else if (data == MainCardData.CardE.SUGAR) {
            mBaseFragment.movePage(SugarManageFragment.newInstance());
        } else if (data == MainCardData.CardE.WEIGHT) {
            mBaseFragment.movePage(WeightManageFragment.newInstance());
        } else if (data == MainCardData.CardE.FOOD) {
            mBaseFragment.movePage(FoodManageFragment.newInstance());
        }
    }


    public void swap(int firstPosition, int secondPosition) {

        int maxPosition = firstPosition > secondPosition ? firstPosition : secondPosition;

        // 마지막 ADD 버튼에 영향 받지 않도록 처리
        if (maxPosition != mainCardList.size() - 1) {
            notifyItemMoved(firstPosition, secondPosition);

            // 데이터 정렬
            MainCardData firstData = mainCardList.get(firstPosition);
            MainCardData secondData = mainCardList.get(secondPosition);

            Logger.i(TAG, "before swap.firstPosition[" + firstPosition + "]=" + firstData.getCardE().name()
                    + ", secondPosition[" + secondPosition + "]=" + secondData.getCardE().name());

            mainCardList.remove(firstPosition);
            mainCardList.add(secondPosition, firstData);
        }

    }

    public void remove(int position) {
        mainCardList.remove(position);
        notifyItemRemoved(position);

        Logger.i(TAG, "remove.position=" + position);
        for (MainCardData data : mainCardList) {
            Logger.i(TAG, "remove=" + data.getCardE().name() + "\n");
        }
    }

    @Override
    public int getItemCount() {
        return mainCardList.size();
    }


    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            return false;
        }
    }


    /**
     * 혈당 프로그래스를 세팅
     *
     * @param tv
     */
    private void setProgressSugar(CircleProgressBar progress, TextView tv) {

        DBHelper helper = new DBHelper(mBaseFragment.getContext());
        DBHelperSugar sugarDb = helper.getSugarDb();
        String sugar = sugarDb.getResultMain(helper);
        sugar = TextUtils.isEmpty(sugar) ? "0" : sugar;
        float val = Float.parseFloat(sugar);
        tv.setText((int) val + "mg/dl");

        progress.setProgress(100);   // 프로그래스 현재값 세팅
        progress.setMax(100);   // 프로그래스 최대치 세팅
    }
    public GoogleApiClient mClient = null;
    public void onDetach() {
        Logger.i(TAG, "onDetach.mClient=" + mClient);
        if (mClient != null) {
            Logger.i(TAG, "onDetach.mClient=" + mClient + ", isConnected()=" + mClient.isConnected());
            mClient.stopAutoManage(mBaseFragment.getActivity());
            mClient.disconnect();
            mClient = null;
        }
    }

    public String makeStringComma(String str) {
        if (str.length() == 0)
            return "";
        long value = Long.parseLong(str);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }

    /**
     * 식사
     *
     * @param tv
     */
    private void setProgressFood(CircleProgressBar progress, TextView tv, MyViewHolder holder) {
        DBHelper helper = new DBHelper(mBaseFragment.getContext());
        DBHelperFoodMain foodDb = helper.getFoodMainDb();
        String caloria = foodDb.getResultMain();

        Tr_login login = Define.getInstance().getLoginInfo();

        float cal_val = StringUtil.getFloatVal(caloria);
        int rActqy = Integer.parseInt(login.mber_actqy);
        int rSex = Integer.parseInt(login.mber_sex);
        float rWeight = 0.0f;
        DBHelperWeight WeightDb = helper.getWeightDb();
        DBHelperWeight.WeightStaticData bottomData = WeightDb.getResultStatic();

        if(bottomData.getWeight().isEmpty()){
            rWeight = Float.parseFloat(login.mber_bdwgh);
        }else{
            rWeight = Float.parseFloat(login.mber_bdwgh_app);
        }

        float rHeight = Float.parseFloat(login.mber_height);
        rHeight = rHeight * 0.01f;

        float mWeight;   //표준체중
        float mHeight = rHeight;
        mHeight = StringUtil.getFloatVal(String.format("%.2f", mHeight));

        if (rSex == 2){
            //여성
            mWeight = StringUtil.getFloatVal(String.format("%.1f",(mHeight*mHeight) *21));
        }else {
            //남성
            mWeight = StringUtil.getFloatVal(String.format("%.1f",(mHeight*mHeight) *22));
        }

        float re = rWeight/(rHeight * rHeight);
        float fat = 0.f;
        if(re < 18.5){
            //"저체중"
            if(rActqy == 1){
                fat = 35.f;
            }else if(rActqy == 2){
                fat = 40.f;
            }else if(rActqy == 3){
                fat = 45.f;
            }
        }else if(re >= 18.5 && re <=22.9){
            //"정상";
            if(rActqy == 1){
                fat = 30.f;
            }else if(rActqy == 2){
                fat = 35.f;
            }else if(rActqy == 3){
                fat = 40.f;
            }
        }else if(re >= 23.0) {
            //"과체중";
            if (rActqy == 1) {
                fat = 25.f;
            } else if (rActqy == 2) {
                fat = 30.f;
            } else if (rActqy == 3) {
                fat = 35.f;
            }
        }

        int recomCal = (int)(mWeight * fat);  // 권장섭취량

        tv.setText(makeStringComma(Integer.toString((int) cal_val).replace(",", "")));
        holder.valueTv.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.mian_frag, 0, 0, 0);
        holder.valueTv.setText(" " + makeStringComma(Integer.toString(recomCal).replace(",", "")) + "Kcal");

        if (recomCal == 0) {
            cal_val = 0.f;
        }

        progress.setProgress(cal_val);   // 프로그래스 현재값 세팅
        progress.setMax(recomCal);
    }

    /**
     * 체중 프로그래스를 세팅
     *
     * @param tv
     */
    private void setProgressWeight(CircleProgressBar progress, TextView tv, MyViewHolder holder) {
        DBHelper helper = new DBHelper(mBaseFragment.getContext());
        DBHelperWeight db = helper.getWeightDb();
        Tr_get_hedctdata.DataList dataList = db.getResultMain();
        Tr_login login = Define.getInstance().getLoginInfo();

        float weight = StringUtil.getFloatVal(dataList.weight);
        float weight_goal = StringUtil.getFloatVal(login.mber_bdwgh_goal);

        tv.setText(StringUtil.getNoneZeroString(weight) + "kg");
        holder.valueTv.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.mian_frag, 0, 0, 0);
        holder.valueTv.setText(" " + Integer.toString((int) weight_goal) + "Kg");

        if ((int) weight == 0.f || (int) weight_goal == 0) {
            progress.setProgress(0);
            progress.setMax(0);
        } else if ((int) weight_goal > (int) weight) {
            progress.setProgress(weight);
            progress.setMax((int) weight_goal);
        } else {
            progress.setProgress(weight_goal);
            progress.setMax((int) weight);
        }
    }

    /**
     * 체중 프로그래스를 세팅
     *
     * @param tv
     */
    private void setProgressFat(CircleProgressBar progress, TextView tv) {
        DBHelper helper = new DBHelper(mBaseFragment.getContext());
        DBHelperWeight db = helper.getWeightDb();
        Tr_get_hedctdata.DataList dataList = db.getResultMain();

        float fat = StringUtil.getFloatVal(dataList.fat);
        tv.setText(fat + "%");

        progress.setProgress(100);   // 프로그래스 현재값 세팅
        progress.setMax(100);   // 프로그래스 최대치 세팅
    }
}
