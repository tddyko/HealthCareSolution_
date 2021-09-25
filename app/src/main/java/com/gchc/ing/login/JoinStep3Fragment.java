package com.gchc.ing.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gchc.ing.R;
import com.gchc.ing.base.BaseFragment;
import com.gchc.ing.base.CommonActionBar;
import com.gchc.ing.base.value.Define;
import com.gchc.ing.component.CDialog;
import com.gchc.ing.database.DBHelper;
import com.gchc.ing.database.DBHelperWeight;
import com.gchc.ing.network.tr.ApiData;
import com.gchc.ing.network.tr.data.Tr_login;
import com.gchc.ing.network.tr.data.Tr_mber_user_call;
import com.gchc.ing.network.tr.data.Tr_mber_user_edit_exe;
import com.gchc.ing.util.ClearEditTextViewWatcher;
import com.gchc.ing.util.Logger;
import com.gchc.ing.util.StringUtil;

/**
 * Created by kominhyuk on 2017. 6. 14..
 */

public class JoinStep3Fragment extends BaseFragment {
    private final String TAG = JoinStep3Fragment.class.getSimpleName();

    private EditText mHeightEt;
    private TextView mHeightErrTv;
    private EditText mWeightEt;
    private TextView mWeightErrTv;
    private EditText mTargetWeightEt;
    private TextView mTargetWeightErrTv;
    private Button next_button;
    private JoinDataVo dataVo;

    private boolean mIsInfoEdit;
    private boolean isValiHeight, isValiWeight, isValiTarWeight;

    public static Fragment newInstance() {
        JoinStep3Fragment fragment = new JoinStep3Fragment();
        return fragment;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.d(TAG, "oncreate()");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.join_step3_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataVo = (JoinDataVo) getArguments().getBinder(JoinStep1Fragment.JOIN_DATA);
        if (dataVo != null) {
            Logger.i(TAG, "onViewCreated.id=" + dataVo.getId());
            Logger.i(TAG, "onViewCreated.pwd=" + dataVo.getAft_pwd());
        } else {
            dataVo = new JoinDataVo();
        }
        mHeightEt           = (EditText) view.findViewById(R.id.join_step3_height_edittext);
        mHeightErrTv        = (TextView) view.findViewById(R.id.join_step3_height_error_textview);
        mWeightEt           = (EditText) view.findViewById(R.id.join_step3_weight_edittext);
        mWeightErrTv        = (TextView) view.findViewById(R.id.join_step3_weight_error_textview);
        mTargetWeightEt     = (EditText) view.findViewById(R.id.join_step3_target_weight_edittext);
        mTargetWeightErrTv  = (TextView) view.findViewById(R.id.join_step3_target_weight_error_textview);

        next_button         = (Button) view.findViewById(R.id.next_button);

        new ClearEditTextViewWatcher(mHeightEt, 300L);
        setTextWatcher(mWeightEt, 300f, 2);
        setTextWatcher(mTargetWeightEt, 300f, 2);

        view.findViewById(R.id.next_button).setOnClickListener(mOnclickListener);

        mHeightEt.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validCheck();
                BtnEnableCheck();
            }
        });
        mWeightEt.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validCheck();
                BtnEnableCheck();
            }
        });
        mTargetWeightEt.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validCheck();
                BtnEnableCheck();
            }
        });

        if (getArguments() != null) {
            String bundleTitle = getArguments().getString(CommonActionBar.ACTION_BAR_TITLE);
            if (TextUtils.isEmpty(bundleTitle) == false) {
                mIsInfoEdit = true;
                next_button.setText(getString(R.string.text_modify));
                mWeightEt.setEnabled(false);

                loadPersonalInfo();
            }
        }
    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
        super.loadActionbar(actionBar);
        actionBar.setActionBarTitle(getString(R.string.join_inform_step3));
    }

    @Override
    public void onResume() {
        Logger.i(TAG, "onResume()");
        super.onResume();
    }

    private void BtnEnableCheck() {
        if (isValiHeight && isValiWeight && isValiTarWeight) {
            next_button.setEnabled(true);
        } else {
            next_button.setEnabled(false);
        }
    }

    private boolean validCheck() {
        boolean isValid = true;
        if (validHeightCheck() == false) {
            isValid = false;
        }

        if (validWeightCheck() == false) {
            isValid = false;
        }
        if (validTargetWeightCheck() == false) {
            isValid = false;
        }
        return isValid;
    }

    View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();
            if (R.id.next_button == vId) {
                if (validCheck()) {
                    if (mIsInfoEdit) {
                        doEditInfo();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putBinder(JoinStep1Fragment.JOIN_DATA, dataVo);
                        movePage(JoinStep4Fragment.newInstance(), bundle);
                    }
                }
            }
        }
    };

    /**
     * 키 체크
     *
     * @return
     */
    private boolean validHeightCheck() {
        String heightStr = mHeightEt.getText().toString();
        int height = StringUtil.getIntVal(heightStr);
        dataVo.setHeight(heightStr);
        if (height < 80 && height > 300) {
            mHeightErrTv.setVisibility(View.VISIBLE);
            isValiHeight = false;
            return false;
        } else {
            mHeightErrTv.setVisibility(View.INVISIBLE);
            isValiHeight = true;
        }
        return true;
    }

    /**
     * 몸무게 체크
     *
     * @return
     */
    private boolean validWeightCheck() {
        String weightStr = mWeightEt.getText().toString();
        float weight = StringUtil.getFloat(weightStr);
        dataVo.setWeight(weightStr);

        if (mIsInfoEdit) {
            DBHelper helper         = new DBHelper(getContext());
            DBHelperWeight weightDb = helper.getWeightDb();
            DBHelperWeight.WeightStaticData bottomData = weightDb.getResultStatic();
            if (!bottomData.getWeight().isEmpty()) {
                mWeightErrTv.setText(getString(R.string.join_step3_weight_error));
                mWeightErrTv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                mWeightErrTv.setVisibility(View.VISIBLE);
            } else {
                mWeightErrTv.setVisibility(View.INVISIBLE);
            }

            isValiWeight = true;
        } else {

            Logger.i(TAG, "str=" + "".equals(weightStr) + ", weight=" + weight + ", weight=" + (weight > 20f && weight < 300f));
            if ("".equals(weightStr) || (weight > 20f && weight < 300f) == false) {
                mWeightErrTv.setVisibility(View.VISIBLE);
                isValiWeight = false;
                return false;
            } else {
                mWeightErrTv.setVisibility(View.INVISIBLE);
                isValiWeight = true;
            }
            return true;
        }
        return true;
    }

    private boolean validTargetWeightCheck() {
        String weightStr = mTargetWeightEt.getText().toString();
        float weight = StringUtil.getFloat(weightStr);
        dataVo.setTargetWeight(weightStr);

        if ("".equals(weightStr) || (weight > 20f && weight < 300f) == false) {
            mTargetWeightErrTv.setVisibility(View.VISIBLE);
            isValiTarWeight = false;
            return false;
        } else {
            mTargetWeightErrTv.setVisibility(View.INVISIBLE);
            isValiTarWeight = true;
        }
        return true;
    }

    private String beforeText = "";

    private void setTextWatcher(final EditText editText, final float maxVal, final int dotAfterCnt) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String str = s.toString();
                if (beforeText.equals(str)) {
                    if (str.equals("") == false)
                        editText.setSelection(str.length());
                    return;
                }

                if (str.length() != 0) {
                    float val = StringUtil.getFloat(s.toString());

                    if (val == 0 || val > maxVal) {
                        str = str.substring(0, str.length() - 1);
                        beforeText = str;
                        editText.setText(str);
                    }

                    String[] dotAfter = str.split("\\.");
                    if (dotAfter.length >= 2 && (dotAfter[1].length() > dotAfterCnt)) {
                        str = str.substring(0, str.length() - 1);
                        beforeText = str;
                        editText.setText(str);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(textWatcher);
    }

    private void loadPersonalInfo() {
        Tr_mber_user_call.RequestData requestData = new Tr_mber_user_call.RequestData();
        Tr_login info       = Define.getInstance().getLoginInfo();
        requestData.mber_sn = info.mber_sn;
        getData(getContext(), Tr_mber_user_call.class, requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_mber_user_call) {
                    Tr_mber_user_call data  = (Tr_mber_user_call)obj;
                    Tr_login login          = Define.getInstance().getLoginInfo();

                    mHeightEt.setText(login.mber_height);

                    DBHelper helper         = new DBHelper(getContext());
                    DBHelperWeight WeightDb = helper.getWeightDb();
                    DBHelperWeight.WeightStaticData bottomData = WeightDb.getResultStatic();
                    if(bottomData.getWeight().isEmpty()){
                        mWeightEt.setText(data.mber_bdwgh);
                    }else{
                        mWeightEt.setText(bottomData.getWeight());
                    }

                    mTargetWeightEt.setText(login.mber_bdwgh_goal);
                }
            }
        });
    }

    private void doEditInfo() {
        Tr_mber_user_edit_exe.RequestData requestData = new Tr_mber_user_edit_exe.RequestData();
        final Tr_login info         = Define.getInstance().getLoginInfo();
        requestData.mber_sn         = info.mber_sn;
        requestData.mber_sex        = info.mber_sex;
        requestData.mber_lifyea     = info.mber_lifyea;
        requestData.mber_height     = dataVo.getHeight();
        requestData.mber_bdwgh      = dataVo.getWeight();
        requestData.mber_bdwgh_goal = dataVo.getTargetWeight();
        getData(getContext(), Tr_mber_user_edit_exe.class, requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_mber_user_edit_exe) {
                    Tr_mber_user_edit_exe data = (Tr_mber_user_edit_exe) obj;

                    if ("Y".equals(data.reg_yn)) {
                        CDialog.showDlg(getContext(), getString(R.string.inform_modify_success), new CDialog.DismissListener() {
                            @Override
                            public void onDissmiss() {
                                Tr_login login          = Define.getInstance().getLoginInfo();
                                login.mber_height       = dataVo.getHeight();
                                login.mber_bdwgh        = dataVo.getWeight();
                                login.mber_bdwgh_goal   = dataVo.getTargetWeight();
                                Define.getInstance().setLoginInfo(login);
                                onBackPressed();
                            }
                        });
                    } else {
                        CDialog.showDlg(getContext(), getString(R.string.inform_modify_fail));
                    }
                }
            }
        });
    }
}