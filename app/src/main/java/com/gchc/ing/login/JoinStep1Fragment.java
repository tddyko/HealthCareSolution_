package com.gchc.ing.login;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gchc.ing.MainActivity;
import com.gchc.ing.R;
import com.gchc.ing.base.BaseFragment;
import com.gchc.ing.base.CommonActionBar;
import com.gchc.ing.component.CDatePicker;
import com.gchc.ing.component.CDialog;
import com.gchc.ing.network.tr.ApiData;
import com.gchc.ing.network.tr.data.Tr_mber_check;
import com.gchc.ing.network.tr.data.Tr_mber_reg_check_hp;
import com.gchc.ing.network.tr.data.Tr_mber_reg_check_nm;
import com.gchc.ing.util.DeviceUtil;
import com.gchc.ing.util.EditTextUtil;
import com.gchc.ing.util.Logger;
import com.gchc.ing.util.StringUtil;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.gchc.ing.component.CDialog.LoginshowDlg;

/**
 * Created by kominhyuk on 2017. 6. 14..
 */

public class JoinStep1Fragment extends BaseFragment {
    public static String JOIN_DATA = "join_data";   // 회원가입시 사용할 데이터들
    private final String TAG = JoinStep1Fragment.class.getSimpleName();

    private LinearLayout mLinear;

    private TextView mNameErrTv;
    private EditText mNameEt;
    private TextView mBirthErrTv;
    private EditText mBirthEt;
    private TextView mPhonenumErrTv;
    private EditText mPhoneNumEt;

    private RadioGroup mSexRg;
    private RadioButton mSexMan;
    private RadioButton mSexWoman;

    private TextView mContractErrTv;
    private CheckBox mStep1Cb;
    private CheckBox mStep2Cb;
    private CheckBox mStep3Cb;

    private Button next_button;

    private JoinDataVo dataVo;

    private boolean isNameCheck, isBirthCheck, isPhoneCheck, isBoxCheck1, isBoxCheck2, isBoxCheck3;

    public static Fragment newInstance() {
        JoinStep1Fragment fragment = new JoinStep1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(this.getClass().getSimpleName(), "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.join_step1_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataVo  = new JoinDataVo();

        mLinear = (LinearLayout) view.findViewById(R.id.join_step1_linear);

        mNameErrTv      = (TextView) view.findViewById(R.id.join_step1_name_error_textview);
        mNameEt         = (EditText) view.findViewById(R.id.join_step1_name_edittext);
        mBirthErrTv     = (TextView) view.findViewById(R.id.join_step1_birth_error_textview);
        mBirthEt        = (EditText) view.findViewById(R.id.join_step1_birth_edittext);
        mPhonenumErrTv  = (TextView) view.findViewById(R.id.join_step1_phonenum_error_textview);
        mPhoneNumEt     = (EditText) view.findViewById(R.id.join_step1_phonenum_edittext);

        mSexRg          = (RadioGroup) view.findViewById(R.id.join_step1_sex_radio_group);
        mSexMan         = (RadioButton) view.findViewById(R.id.join_step1_man_radio_button);
        mSexWoman       = (RadioButton) view.findViewById(R.id.join_step1_woman_radio_button);

        mContractErrTv  = (TextView) view.findViewById(R.id.join_step1_contract_error_textview);
        mStep1Cb        = (CheckBox) view.findViewById(R.id.join_step1_checkbox1);
        mStep2Cb        = (CheckBox) view.findViewById(R.id.join_step1_checkbox2);
        mStep3Cb        = (CheckBox) view.findViewById(R.id.join_step1_checkbox3);

        next_button     = (Button) view.findViewById(R.id.next_button);

        mBirthEt.setOnClickListener(mOnClickListener);
        mBirthEt.setInputType(0);
        mStep1Cb.setOnClickListener(mOnClickListener);
        mStep2Cb.setOnClickListener(mOnClickListener);
        mStep3Cb.setOnClickListener(mOnClickListener);

        view.findViewById(R.id.join_step1_contract1_textview).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.join_step1_contract2_textview).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.join_step1_personal_info_textview).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.next_button).setOnClickListener(mOnClickListener);

        //키보드 가리기
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(mBirthEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        mNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString();

                if (name.length() > 2 || name.length() < 10) {
                    validNameCheck();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBirthEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validBirthCheck();
            }
        });

        mPhoneNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString();
                if (StringUtil.isValidPhoneNumber(phone) == true) {
                    validPhoneNumCheck();
                }
            }
        });
    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
        super.loadActionbar(actionBar);
        actionBar.setActionBarTitle(getString(R.string.join_inform_step1));     // 액션바 타이틀
    }

    @Override
    public void onResume() {
        Log.d(this.getClass().getSimpleName(), "onResume()");
        super.onResume();

        BtnEnableCheck();
    }


    private boolean validCheck() {
        boolean isValid = true;
        dataVo.setSex(mSexMan.isChecked() ? "1" : "2");
        if (validNameCheck() == false) {
            isValid = false;
        }
        if (validBirthCheck() == false) {
            isValid = false;
        }
        if (validPhoneNumCheck() == false) {
            isValid = false;
        }
        if (validContractCheck() == false) {
            isValid = false;
        }
        return isValid;
    }

    boolean isValid = false;

    /**
     * 이름 체크(닉네임)
     *
     * @return
     */
    private boolean validNameCheck() {
        String name = mNameEt.getText().toString();
        dataVo.setName(name);

        if (name.equals(""))
            mNameErrTv.setVisibility(View.INVISIBLE);
        if ((name.length() < 2) || (name.length() > 10)) {
            mNameErrTv.setText(R.string.join_step1_name_error2);
            mNameErrTv.setVisibility(View.VISIBLE);
            // EditTextUtil.focusAndShowKeyboard(getContext(), mNameEt);
            isNameCheck = false;
            return false;
        } else if (StringUtil.isSpecialWord(name) == false) {
            mNameErrTv.setVisibility(View.VISIBLE);
            mNameErrTv.setText(R.string.join_step1_dont_special_word);
            isNameCheck = false;
            isValid = false;
        } else {
            Tr_mber_reg_check_nm.RequestData requestData = new Tr_mber_reg_check_nm.RequestData();
            requestData.mber_nm = name;
            getData(getContext(), Tr_mber_reg_check_nm.class, requestData, new ApiData.IStep() {
                @Override
                public void next(Object obj) {
                    if (obj instanceof Tr_mber_reg_check_nm) {
                        Tr_mber_reg_check_nm data = (Tr_mber_reg_check_nm) obj;
                        if ("Y".equals(data.login_check_yn)) {
                            mNameErrTv.setVisibility(View.VISIBLE);
                            mNameErrTv.setText(R.string.join_step1_name_error);
                            isNameCheck = false;
                            isValid = false;
                        } else {
                            mNameErrTv.setVisibility(View.INVISIBLE);
                            isNameCheck = true;
                            isValid = true;
                        }
                    }
                }
            });
        }
        BtnEnableCheck();
        return isValid;
    }

    /**
     * 생년월일 체크
     *
     * @return
     */
    private boolean validBirthCheck() {

        String birth = mBirthEt.getTag() != null ? mBirthEt.getTag().toString() : "";       //mBirthEt.getText().toString();
        dataVo.setBirth(birth);
        if (birth.equals(""))
            mBirthErrTv.setVisibility(View.INVISIBLE);
        if (birth.length() == 0) {
            mBirthErrTv.setVisibility(View.VISIBLE);
            isBirthCheck = false;
            return false;
        } else {
            mBirthErrTv.setVisibility(View.INVISIBLE);
            isBirthCheck = true;
        }
        return true;
    }

    /**
     * 전화번호 체크
     *
     * @return
     */
    private boolean validPhoneNumCheck() {
        String phoneNum = mPhoneNumEt.getText().toString();
        dataVo.setPhoneNum(phoneNum);
        if (phoneNum.equals(""))
            mPhonenumErrTv.setVisibility(View.INVISIBLE);
        if (StringUtil.isValidPhoneNumber(phoneNum) == false) {
            mPhonenumErrTv.setText(R.string.join_step1_phone_num_error);
            mPhonenumErrTv.setVisibility(View.VISIBLE);
            isPhoneCheck = false;
            return false;
        } else {
            Tr_mber_reg_check_hp.RequestData requestData = new Tr_mber_reg_check_hp.RequestData();
            requestData.mber_hp = phoneNum;
            getData(getContext(), Tr_mber_reg_check_hp.class, requestData, new ApiData.IStep() {
                @Override
                public void next(Object obj) {
                    if (obj instanceof Tr_mber_reg_check_hp) {
                        Tr_mber_reg_check_hp data = (Tr_mber_reg_check_hp) obj;
                        if ("N".equals(data.mber_hp_yn)) {
                            mPhonenumErrTv.setVisibility(View.INVISIBLE);
                            isPhoneCheck = true;
                            isValid = true;
                        } else {
                            mPhonenumErrTv.setVisibility(View.VISIBLE);
                            mPhonenumErrTv.setText(getString(R.string.join_step1_phone_num_error2));
                            isPhoneCheck = false;
                            isValid = false;
                        }
                    }
                }
            });
        }
        BtnEnableCheck();
        return isValid;
    }

    /**
     * 약관동의 체크 박스
     *
     * @return
     */
    private boolean validContractCheck() {
        if (mStep1Cb.isChecked() == false || mStep2Cb.isChecked() == false || mStep3Cb.isChecked() == false) {
            mContractErrTv.setVisibility(View.VISIBLE);
            return false;
        } else {
            mContractErrTv.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    /**
     * Btn enable true / false
     */
    private void BtnEnableCheck() {

        if (isNameCheck && isBirthCheck && isPhoneCheck && isBoxCheck1 && isBoxCheck2 && isBoxCheck3) {
            next_button.setEnabled(true);
        } else {
            next_button.setEnabled(false);
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();
            if (R.id.next_button == vId) {
                if (validCheck()) {
                    doMemberCheck();
                }
            } else if (R.id.join_step1_contract1_textview == vId) {
                LoginshowDlg(getContext(), getString(R.string.join_step1_contract1_title), getString(R.string.join_step1_contract1_msg));
            } else if (R.id.join_step1_contract2_textview == vId) {
                LoginshowDlg(getContext(), getString(R.string.join_step1_contract2_title), getString(R.string.join_step1_contract2_msg));
            } else if (R.id.join_step1_personal_info_textview == vId) {
                LoginshowDlg(getContext(), getString(R.string.join_step1_contract3_title), getString(R.string.join_step1_contract3_msg));
            } else if (R.id.join_step1_checkbox1 == vId) {
                if (mStep1Cb.isChecked()) {
                    isBoxCheck1 = true;
                } else {
                    isBoxCheck1 = false;
                }
                validCheck();
                BtnEnableCheck();
            } else if (R.id.join_step1_checkbox2 == vId) {
                if (mStep2Cb.isChecked()) {
                    isBoxCheck2 = true;
                } else {
                    isBoxCheck2 = false;
                }
                validCheck();
                BtnEnableCheck();
            } else if (R.id.join_step1_checkbox3 == vId) {
                if (mStep3Cb.isChecked()) {
                    isBoxCheck3 = true;
                } else {
                    isBoxCheck3 = false;
                }
                validCheck();
                BtnEnableCheck();
            } else if (R.id.join_step1_birth_edittext == vId) {
                showDatePicker(v);
            }
        }
    };

    private void showDatePicker(View v) {
        GregorianCalendar calendar = new GregorianCalendar();
        String birth = mBirthEt.getText().toString().trim();
        String[] birthSpl = birth.split("\\.");

        int year    = calendar.get(Calendar.YEAR);
        int month   = calendar.get(Calendar.MONTH);
        int day     = calendar.get(Calendar.DAY_OF_MONTH);
        if (birthSpl.length == 3) {
            year    = Integer.parseInt("".equals(birthSpl[0]) ? "0" : birthSpl[0].trim());
            month   = Integer.parseInt("".equals(birthSpl[1]) ? "0" : birthSpl[1].trim()) - 1;
            day     = Integer.parseInt("".equals(birthSpl[2]) ? "0" : birthSpl[2].trim());
        } else {
            year    = 1970;
            month   = 1;
            day     = 1;
        }

        EditTextUtil.hideKeyboard(getContext(), mBirthEt);
        new CDatePicker(getContext(), dateSetListener, year, month, day).show();
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            String msg  = String.format("%d. %d. %d", year, monthOfYear, dayOfMonth);

            StringBuffer sb = new StringBuffer();
            sb.append(year);
            DecimalFormat df = new DecimalFormat("00");
            sb.append(df.format(monthOfYear));
            sb.append(df.format(dayOfMonth));

            mBirthEt.setText(msg);
            mBirthEt.setTag(sb.toString());
            validBirthCheck();
            Logger.i(TAG, "birth.tag=" + mBirthEt.getTag());

        }

    };

    private void doMemberCheck() {
        Tr_mber_check.RequestData requestData = new Tr_mber_check.RequestData();
        requestData.mber_nm     = dataVo.getName();
        requestData.mber_sex    = dataVo.getSex();
        requestData.mber_lifyea = dataVo.getBirth();
        requestData.mber_hp     = dataVo.getPhoneNum();
        requestData.phone_model = DeviceUtil.getPhoneModelName();

        getData(getContext(), Tr_mber_check.class, requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_mber_check) {
                    Tr_mber_check data = (Tr_mber_check) obj;
                    if ("Y".equals(data.data_yn)) {
                        dataVo.setMber_no(data.mber_no);
                        Bundle bundle = new Bundle();
                        bundle.putBinder(JoinStep1Fragment.JOIN_DATA, dataVo);
                        movePage(JoinStep2Fragment.newInstance(), bundle);
                    } else {
                        CDialog.showDlg(getContext(), getString(R.string.join_step1_mberchk_title), getString(R.string.join_step1_mberchk_content), null);
                    }
                }
            }
        });
    }
}