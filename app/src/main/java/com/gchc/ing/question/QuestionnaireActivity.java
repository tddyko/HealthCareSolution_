package com.gchc.ing.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gchc.ing.base.BaseFragment;


import com.gchc.ing.R;
import com.gchc.ing.base.CommonActionBar;
import com.gchc.ing.question.common.QuestionnaireListView_Adapter;
import com.gchc.ing.question.common.UserInfo;

/**
 * 질병 위험도 체크.
 */
public class QuestionnaireActivity extends BaseFragment {
    private UserInfo user;
    private QuestionnaireListView_Adapter adapter = null;

    public static Fragment newInstance() {
        QuestionnaireActivity fragment = new QuestionnaireActivity();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_questionnaire, container, false);
        return view;
    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
        super.loadActionbar(actionBar);
        actionBar.setActionBarTitle("건강체크");       // 액션바 타이틀
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
    }

    private void init(View view) {

        ListView activity_question_ListView_list = (ListView) view.findViewById(R.id.activity_question_ListView_list);
        adapter = new QuestionnaireListView_Adapter(QuestionnaireActivity.this);
        activity_question_ListView_list.setAdapter(adapter);
        activity_question_ListView_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user = new UserInfo(getContext());
                if(user.getQuestionLevel(position + 1) == 0)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString(WebviewQuestionActivity.TITLE, ""+position);
                    bundle.putString(WebviewQuestionActivity.URL, "file:///android_asset/question" + (position + 1) + ".html");
                    bundle.putString(WebviewQuestionActivity.POS, ""+(position + 1));
                    movePage(WebviewQuestionActivity.newInstance(), bundle);
                }
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putString(WebviewQuestionActivity.TITLE, ""+100);
                    bundle.putString(WebviewQuestionActivity.URL, "file:///android_asset/question" + (position + 1) + ".html");
                    bundle.putString("COMMENT", "");
                    bundle.putString(WebviewQuestionActivity.POS, ""+(position + 1));
                    movePage(WebviewQuestionActivity.newInstance(), bundle);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}
