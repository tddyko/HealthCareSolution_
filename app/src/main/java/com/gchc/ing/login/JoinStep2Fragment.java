package com.gchc.ing.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gchc.ing.R;
import com.gchc.ing.base.BaseFragment;
import com.gchc.ing.base.CommonActionBar;
import com.gchc.ing.base.value.Define;
import com.gchc.ing.component.CDialog;
import com.gchc.ing.network.tr.ApiData;
import com.gchc.ing.network.tr.data.Tr_login;
import com.gchc.ing.network.tr.data.Tr_mber_pwd_edit_exe;
import com.gchc.ing.network.tr.data.Tr_mber_reg_check_id;
import com.gchc.ing.network.tr.data.Tr_mber_user_call;
import com.gchc.ing.util.Logger;
import com.gchc.ing.util.SharedPref;
import com.gchc.ing.util.StringUtil;

/**
 * Created by kominhyuk on 2017. 6. 14..
 */

public class JoinStep2Fragment extends BaseFragment {
    private final String TAG = JoinStep2Fragment.class.getSimpleName();

    private TextView mIdTv;
    private TextView mIdErrTv;
    private EditText mIdEt;
    private EditText mPwdEt0;
    private EditText mPwdEt1;
    private EditText mPwdEt2;
    private TextView mPwdErrTv;

    private ImageView midErrIv;
    private ImageView mPwdErrIv0;
    private ImageView mPwdErrIv1;
    private ImageView mPwdErrIv2;

    private RelativeLayout relativeIdGroup;

    private JoinDataVo dataVo;

    private Button next_button;

    private boolean isIdCheck, isPwdCheck1, isPwdCheck2;

    private boolean mIsInfoEdit = false;

    public static Fragment newInstance() {

        JoinStep2Fragment fragment = new JoinStep2Fragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(this.getClass().getSimpleName(), "onCreate()");
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String bundleTitle = getArguments().getString(CommonActionBar.ACTION_BAR_TITLE);
            if (TextUtils.isEmpty(bundleTitle) == false) {
                mIsInfoEdit = true;
                isIdCheck   = false;
                isPwdCheck1 = false;
                isPwdCheck2 = false;
            }
        } else {
            isIdCheck   = false;
            isPwdCheck1 = false;
            isPwdCheck2 = false;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.join_step2_fragment, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataVo = (JoinDataVo) getArguments().getBinder(JoinStep1Fragment.JOIN_DATA);
        if (dataVo != null) {
            Logger.i(TAG, "onViewCreated.name=" + dataVo.getName());
            Logger.i(TAG, "onViewCreated.sex=" + dataVo.getSex());
            Logger.i(TAG, "onViewCreated.birth=" + dataVo.getBirth());
            Logger.i(TAG, "onViewCreated.phoneNum=" + dataVo.getPhoneNum());
        } else {
            dataVo = new JoinDataVo();
        }

        mIdTv       = (TextView) view.findViewById(R.id.join_step2_id_textview);
        mIdEt       = (EditText) view.findViewById(R.id.joins_step2_id_edittext);
        midErrIv    = (ImageView) view.findViewById(R.id.join_step2_id_imageview);
        mIdErrTv    = (TextView) view.findViewById(R.id.join_step2_id_error_textview);
        mPwdEt0     = (EditText) view.findViewById(R.id.join_step2_pwd_edittext0);
        mPwdErrIv0  = (ImageView) view.findViewById(R.id.join_step2_pwd_imageview0);
        mPwdEt1     = (EditText) view.findViewById(R.id.join_step2_pwd_edittext1);
        mPwdErrIv1  = (ImageView) view.findViewById(R.id.join_step2_pwd_imageview1);
        mPwdEt2     = (EditText) view.findViewById(R.id.join_step2_pwd_edittext2);
        mPwdErrIv2  = (ImageView) view.findViewById(R.id.join_step2_pwd_imageview2);
        mPwdErrTv   = (TextView) view.findViewById(R.id.join_step2_pwd_error_textview);

        relativeIdGroup = (RelativeLayout) view.findViewById(R.id.relativeIdGroup);

        next_button     = (Button) view.findViewById(R.id.next_button);

        if (mIsInfoEdit) {
            mIdErrTv.setVisibility(View.GONE);
        }else{
            mIdErrTv.setVisibility(View.VISIBLE);
        }

        mIdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validIdCheck();
            }
        });

        mPwdEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before >= 1) {
                    validPwdCheck();
                    BtnEnableCheck();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isValidPassword(s.toString())) {
                    validPwdCheck();
                    BtnEnableCheck();
                }
            }
        });

        mPwdEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before >= 1) {
                    validPwdCheck();
                    BtnEnableCheck();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isValidPassword(s.toString())) {
                    validPwdCheck();
                    BtnEnableCheck();
                }
            }
        });

        view.findViewById(R.id.next_button).setOnClickListener(mOnClickListener);

        // 액션바 타이틀이 있으면 환경설정에서 온것으로 판단
        if (getArguments() != null) {
            String bundleTitle = getArguments().getString(CommonActionBar.ACTION_BAR_TITLE);
            if (TextUtils.isEmpty(bundleTitle) == false) {
                next_button.setEnabled(true);
                mIsInfoEdit = true;
                next_button.setText(getString(R.string.join_step3_ecomplete));

                relativeIdGroup.setVisibility(View.GONE);
                mIdTv.setVisibility(View.VISIBLE);
                mPwdEt0.setVisibility(View.VISIBLE);
                loadPersonalInfo();
            }
        }
    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
        super.loadActionbar(actionBar);
        actionBar.setActionBarTitle(getString(R.string.join_inform_step2));
    }

    @Override
    public void onResume() {
        Log.d(this.getClass().getSimpleName(), "onResume()");
        super.onResume();

        BtnEnableCheck();
    }

    private boolean validCheck() {
        boolean isValid = true;
        if (validIdCheck() == false && !mIsInfoEdit) {
            isValid = false;
        }
        if (validPwdCheck() == false) {
            isValid = false;
        }
        return isValid;
    }

    boolean isValid = false;

    /**
     * 아이디체크
     *
     * @return
     */
    private boolean validIdCheck() {
        String email = mIdEt.getText().toString();
        dataVo.setId(email);

        if (mIdEt.getText().toString() == "")
            midErrIv.setVisibility(View.INVISIBLE);

        if (mIsInfoEdit) {
            mIdErrTv.setVisibility(View.GONE);
        }else{
            mIdErrTv.setVisibility(View.VISIBLE);
            mIdErrTv.setText(getString(R.string.join_step2_id_info));
            mIdErrTv.setTextColor(getResources().getColor(R.color.colorDark));
        }
        if ((StringUtil.isValidEmail(email) == false) && !mIsInfoEdit) {
            midErrIv.setBackgroundResource(R.drawable.icon_input_x);
            mIdErrTv.setText(getString(R.string.join_step2_id_error));
            mIdErrTv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            isIdCheck = false;
            BtnEnableCheck();
            return false;
        } else {
            Tr_mber_reg_check_id.RequestData requestData = new Tr_mber_reg_check_id.RequestData();
            requestData.mber_id = email;

            getData(getContext(), Tr_mber_reg_check_id.class, requestData, new ApiData.IStep() {
                @Override
                public void next(Object obj) {
                    if (obj instanceof Tr_mber_reg_check_id) {
                        Tr_mber_reg_check_id data = (Tr_mber_reg_check_id) obj;
                        if ("N".equals(data.mber_id_yn)) {  // 중복아님 N
                            midErrIv.setBackgroundResource(R.drawable.icon_input_o);
                            mIdErrTv.setVisibility(View.INVISIBLE);
                            if (mIsInfoEdit)
                                mIdErrTv.setVisibility(View.VISIBLE);
                            isValid = true;
                            isIdCheck = true;
                            BtnEnableCheck();
                        } else {
                            midErrIv.setBackgroundResource(R.drawable.icon_input_x);
                            mIdErrTv.setVisibility(View.VISIBLE);
                            mIdErrTv.setText(R.string.join_step2_id_error2);
                            mIdErrTv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            isValid = false;
                            isIdCheck = false;
                            BtnEnableCheck();
                        }
                    }
                }
            });
            BtnEnableCheck();
        }
        return isValid;
    }

    /**
     * 패스워드 체크
     *
     * @return
     */
    private boolean validPwdCheck() {
        String pwd1 = mPwdEt1.getText().toString();
        String pwd2 = mPwdEt2.getText().toString();

        dataVo.setAft_pwd(pwd1);

        if (StringUtil.isValidPassword(pwd1) || StringUtil.isValidPassword(pwd2)) {
            if (StringUtil.isValidPassword(pwd1)) {
                mPwdErrIv1.setBackgroundResource(R.drawable.icon_input_o);
                mPwdErrIv1.setVisibility(View.VISIBLE);
                mPwdErrTv.setVisibility(View.INVISIBLE);
                isPwdCheck1 = true;
            }
            if (StringUtil.isValidPassword(pwd2) && pwd1.equals(pwd2)) {
                mPwdErrIv2.setBackgroundResource(R.drawable.icon_input_o);
                mPwdErrIv2.setVisibility(View.VISIBLE);
                mPwdErrTv.setVisibility(View.INVISIBLE);
                isPwdCheck2 = true;
            }
            if ((StringUtil.isValidPassword(pwd1) || StringUtil.isValidPassword(pwd2)) && !(pwd1.equals("") || pwd2.equals("")) && !pwd1.equals(pwd2)) {
                mPwdErrIv1.setVisibility(View.VISIBLE);
                mPwdErrIv1.setBackgroundResource(R.drawable.icon_input_x);
                mPwdErrIv2.setVisibility(View.VISIBLE);
                mPwdErrIv2.setBackgroundResource(R.drawable.icon_input_x);
                mPwdErrTv.setVisibility(View.VISIBLE);
                mPwdErrTv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                mPwdErrTv.setText(getString(R.string.join_step2_pwd_error));
                isPwdCheck1 = false;
                isPwdCheck2 = false;
                pwdInit();
                BtnEnableCheck();
                return false;
            }
        } else {
            if (!StringUtil.isValidPassword(pwd1) || !StringUtil.isValidPassword(pwd2)) {
                mPwdErrTv.setVisibility(View.VISIBLE);
                mPwdErrTv.setText(getString(R.string.join_step2_pwd_error_leng8));
                if (!StringUtil.isValidPassword(pwd1) && !pwd1.equals("")) {
                    mPwdErrIv1.setBackgroundResource(R.drawable.icon_input_x);
                    mPwdErrIv1.setVisibility(View.VISIBLE);
                    pwdLength8();
                    isPwdCheck1 = false;
                }
                if (!StringUtil.isValidPassword(pwd2) && !pwd2.equals("")) {
                    mPwdErrIv2.setBackgroundResource(R.drawable.icon_input_x);
                    mPwdErrIv2.setVisibility(View.VISIBLE);
                    pwdLength8();
                    isPwdCheck2 = false;
                }
                pwdInit();
                BtnEnableCheck();
                return false;
            } else {
                mPwdErrIv1.setVisibility(View.VISIBLE);
                mPwdErrIv1.setBackgroundResource(R.drawable.icon_input_x);
                mPwdErrIv2.setVisibility(View.VISIBLE);
                pwdLength8();
                isPwdCheck1 = false;
                isPwdCheck2 = false;
                pwdInit();
                BtnEnableCheck();
                return false;
            }
        }
        pwdInit();
        BtnEnableCheck();
        return true;
    }

    private void pwdInit() {
        mPwdEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before >= 1) {
                    mPwdErrIv1.setVisibility(View.INVISIBLE);
                    mPwdErrTv.setVisibility(View.INVISIBLE);
                    isPwdCheck1 = false;
                }
                if (s.toString().getBytes().length == 0) {
                    mPwdErrIv1.setVisibility(View.INVISIBLE);
                    isPwdCheck1 = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPwdEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before >= 1) {
                    mPwdErrIv2.setVisibility(View.INVISIBLE);
                    mPwdErrTv.setVisibility(View.INVISIBLE);
                    isPwdCheck2 = false;
                }
                if (s.toString().getBytes().length == 0) {
                    mPwdErrIv2.setVisibility(View.INVISIBLE);
                    isPwdCheck2 = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void pwdLength8() {
        mPwdErrTv.setVisibility(View.VISIBLE);
        mPwdErrTv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        mPwdErrTv.setText(getString(R.string.join_step2_pwd_error_leng8));
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();
            if (R.id.next_button == vId) {
                if (validCheck()) {
                    if (mIsInfoEdit) {
                        // 회원정보 수정
                        doEditInfo();
                    } else {
                        CDialog.showDlg(getContext(), getString(R.string.join_step2_success_title), getString(R.string.join_step2_success_content), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 회원가입 스텝3로 이동
                                Bundle bundle = new Bundle();
                                bundle.putBinder(JoinStep1Fragment.JOIN_DATA, dataVo);
                                movePage(JoinStep3Fragment.newInstance(), bundle);
                            }
                        });
                    }
                }
            }
        }
    };

    private void loadPersonalInfo() {
        mIdEt.setEnabled(false);

        Tr_mber_user_call.RequestData requestData = new Tr_mber_user_call.RequestData();
        Tr_login info = Define.getInstance().getLoginInfo();
        requestData.mber_sn = info.mber_sn;
        getData(getContext(), Tr_mber_user_call.class, requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_mber_user_call) {
                    Tr_mber_user_call data = (Tr_mber_user_call) obj;

                    mIdTv.setText(data.mber_id);
                }
            }
        });
    }

    private void BtnEnableCheck() {
        // 수정모드라면
        if (mIsInfoEdit) {
            if (isPwdCheck1 && isPwdCheck2) {
                next_button.setEnabled(true);
            } else {
                next_button.setEnabled(false);
            }
        } else {
            if (isIdCheck && isPwdCheck1 && isPwdCheck2) {
                next_button.setEnabled(true);
            } else {
                next_button.setEnabled(false);
            }
        }
    }

    private void doEditInfo() {
        Tr_mber_pwd_edit_exe.RequestData requestData = new Tr_mber_pwd_edit_exe.RequestData();
        final Tr_login info = Define.getInstance().getLoginInfo();
        String old_pwd              = mPwdEt0.getText().toString();
        String new_pwd              = mPwdEt1.getText().toString();
        String email                = mIdTv.getText().toString();
        requestData.mber_sn         = info.mber_sn;
        requestData.mber_id         = email;
        requestData.bef_mber_pwd    = old_pwd;
        requestData.aft_mber_pwd    = new_pwd;

        getData(getContext(), Tr_mber_pwd_edit_exe.class, requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_mber_pwd_edit_exe) {
                    Tr_mber_pwd_edit_exe data = (Tr_mber_pwd_edit_exe) obj;
                    if ("Y".equals(data.reg_yn)) {
                        CDialog.showDlg(getContext(), getString(R.string.inform_pwd_modify_success), new CDialog.DismissListener() {
                            @Override
                            public void onDissmiss() {
                                boolean isAutoLogin = SharedPref.getInstance().getPreferences(SharedPref.IS_AUTO_LOGIN, false);
                                if (isAutoLogin && info != null) {
                                    if (mPwdEt1.getText().length() > 0) {
                                        SharedPref.getInstance().savePreferences(SharedPref.SAVED_LOGIN_PWD, mPwdEt1.getText().toString());
                                    }
                                }
                                onBackPressed();
                            }
                        });
                    } else {
                        CDialog.showDlg(getContext(), getString(R.string.inform_pwd_modify_fail));
                    }
                }
            }
        });
    }
}