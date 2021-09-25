package com.gchc.ing.question.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gchc.ing.R;
import com.gchc.ing.question.QuestionnaireActivity;

/**
 * 질문 리스트뷰 어댑터.
 */
public class QuestionnaireListView_Adapter extends BaseAdapter {
    private QuestionnaireActivity mQuestionnaireActivity;
    private UserInfo user = null;

    public QuestionnaireListView_Adapter(QuestionnaireActivity activity) {
        mQuestionnaireActivity = activity;
        user = new UserInfo(activity.getContext());
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mQuestionnaireActivity.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_questionnaire_listview, null);
            holder.item_question_TextView_title = (TextView) convertView.findViewById(R.id.item_question_TextView_title);
            holder.item_question_TextView_level = (TextView) convertView.findViewById(R.id.item_question_TextView_level);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (position) {
            case 0:
                holder.item_question_TextView_title.setText("골다공증 발생 위험도");
                break;
            case 1:
                holder.item_question_TextView_title.setText("폐암 발생 위험도");
                break;
            case 2:
                holder.item_question_TextView_title.setText("뇌졸중 발생 위험도");
                break;
            case 3:
                holder.item_question_TextView_title.setText("대장암 발생 위험도");
                break;
            case 4:
                holder.item_question_TextView_title.setText("당뇨병 발생 위험도");
                break;
            case 5:
                holder.item_question_TextView_title.setText("자궁경부암 발생 위험도");
                break;
            case 6:
                holder.item_question_TextView_title.setText("심장병 발생 위험도");
                break;
            case 7:
                holder.item_question_TextView_title.setText("암 발생 위험도");
                break;
            case 8:
                holder.item_question_TextView_title.setText("난소암 발생 위험도");
                break;
            case 9:
                holder.item_question_TextView_title.setText("구강암 발생 위험도");
                break;
        }
        holder.item_question_TextView_level.setText(getLevel(position + 1));
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        user = new UserInfo(mQuestionnaireActivity.getContext());
    }

    public class ViewHolder {
        public TextView item_question_TextView_title, item_question_TextView_level;
    }


    private String getLevel(int no)
    {
        switch (user.getQuestionLevel(no))
        {
            case 0:
                return "체크하기";
            case 1:
                return "높음";
            case 2:
                return "중간";
            case 3:
                return "낮음";
            case 4:
                return "매우낮음";
        }
        return "체크하기";
    }
}
