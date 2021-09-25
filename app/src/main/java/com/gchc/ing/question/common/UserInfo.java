package com.gchc.ing.question.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 로그인후 유저정보.
 */
public class UserInfo {
    private String seq = null, id = null, pw = null, name = null, birth = null, phone = null, app_version = null, hiplanner_name = null, hiplanner_phone = null, nurse_name = null, nurse_phone = null, market_url = null;
    private String nick = null , nunm = null , nuhp = null , fpnm = null , fphp = null;
    private int sex = 0, forigner = 0;
    private boolean set_auto_login = false, set_save_id = false, set_cancer_member = false, set_stroke_member = false;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    public UserInfo(Context context) {
        sp = context.getSharedPreferences("login", context.MODE_PRIVATE);
        editor = sp.edit();
        hiplanner_name = sp.getString("HINAME", null);
        hiplanner_phone = sp.getString("HIPHONE", null);
        nurse_name = sp.getString("NNAME", null);
        nurse_phone = sp.getString("NPHONE", null);
        market_url = sp.getString("NEW_LINK", null);
        seq = sp.getString("SEQ", null);
        id = sp.getString("ID", null);
        pw = sp.getString("PW", null);
        name = sp.getString("NAME", null);
        phone = sp.getString("PHONE", null);
        app_version = sp.getString("APPVER", null);
        birth = sp.getString("BIRTH", null);
        sex = sp.getInt("SEX", 0);
        forigner = sp.getInt("FORIGNER", 0);
        set_auto_login = sp.getBoolean("AUTO_LOGIN", false);
        set_save_id = sp.getBoolean("SAVE_ID", false);
        set_cancer_member = sp.getBoolean("PMYN", false);
        set_stroke_member = sp.getBoolean("PMSTROKEYN", false);
        nick = sp.getString("NICK", null);
        nunm = sp.getString("NUNM", null);
        nuhp = sp.getString("NUHP", null);
        fpnm = sp.getString("FPNM", null);
        fphp = sp.getString("FPHP", null);
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        editor.putString("SEQ", seq);
        this.seq = seq;
        editor.commit();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        editor.putString("ID", id);
        this.id = id;
        editor.commit();
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        editor.putString("PW", pw);
        this.pw = pw;
        editor.commit();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        editor.putString("NAME", name);
        this.name = name;
        editor.commit();
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        editor.putString("BIRTH", birth);
        this.birth = birth;
        editor.commit();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        editor.putString("PHONE", phone);
        this.phone = phone;
        editor.commit();
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        editor.putInt("SEX", sex);
        this.sex = sex;
        editor.commit();
    }

    public int getForigner() {
        return forigner;
    }

    public void setForigner(int forigner) {
        editor.putInt("FORIGNER", forigner);
        this.forigner = forigner;
        editor.commit();
    }

    public boolean getAuto_login() {
        return set_auto_login;
    }

    public void setAuto_login(boolean set_auto_login) {
        editor.putBoolean("AUTO_LOGIN", set_auto_login);
        this.set_auto_login = set_auto_login;
        editor.commit();
    }

    public boolean getSave_id() {
        return set_save_id;
    }

    public void setSave_id(boolean set_save_id) {
        editor.putBoolean("SAVE_ID", set_save_id);
        this.set_save_id = set_save_id;
        editor.commit();
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        editor.putString("APPVER", app_version);
        this.app_version = app_version;
        editor.commit();
    }

    public String getHiplanner_name() {
        return hiplanner_name;
    }

    public void setHiplanner_name(String hiplanner_name) {
        editor.putString("HINAME", hiplanner_name);
        this.hiplanner_name = hiplanner_name;
        editor.commit();
    }

    public String getHiplanner_phone() {
        return hiplanner_phone;
    }

    public void setHiplanner_phone(String hiplanner_phone) {
        editor.putString("HIPHONE", hiplanner_phone);
        this.hiplanner_phone = hiplanner_phone;
        editor.commit();
    }

    public String getNurse_name() {
        return nurse_name;
    }

    public void setNurse_name(String nurse_name) {
        editor.putString("NNAME", nurse_name);
        this.nurse_name = nurse_name;
        editor.commit();
    }

    public String getNurse_phone() {
        return nurse_phone;
    }

    public void setNurse_phone(String nurse_phone) {
        editor.putString("NPHONE", nurse_phone);
        this.nurse_phone = nurse_phone;
        editor.commit();
    }

    public String getMarket_url() {
        return market_url;
    }

    public void setMarket_url(String market_url) {
        editor.putString("NEW_LINK", market_url);
        this.market_url = market_url;
        editor.commit();
    }

    public void setQuestion(int no, int level, int sum)
    {
        editor.putInt("QUESTION" + no, level);
        editor.putInt("QUESTION" + no + "_SUM", sum);
        editor.commit();
    }

    public int getQuestionLevel(int no)
    {
        return sp.getInt("QUESTION" + no, 0);
    }

    public int getQuestionSum(int no)
    {
        return sp.getInt("QUESTION" + no + "_SUM", 0);
    }

    public void clearQuestion()
    {
        for(int i=0; i < 10; i++)
        {
            editor.putInt("QUESTION" + i, 0);
            editor.putInt("QUESTION" + i + "_SUM", 0);
        }
        editor.commit();
    }

    public boolean getCancerMember() {
        return set_cancer_member;
    }

    public void setCancerMember(boolean set_cancer_member) {
        editor.putBoolean("PMYN", set_cancer_member);
        this.set_cancer_member = set_cancer_member;
        editor.commit();
    }

    public boolean getStrokeMember() {
        return set_stroke_member;
    }

    public void setStrokeMember(boolean set_stroke_member) {
        editor.putBoolean("PMSTROKEYN", set_stroke_member);
        this.set_stroke_member = set_stroke_member;
        editor.commit();
    }

    /**
     * 닉네임 가져오기
     * @return string
     */
    public String getNick() {
        return nick;
    }

    /**
     * 닉네임 설정
     * @param nick 닉네임
     */
    public void setNick(String nick) {
        editor.putString("NICK", nick);
        this.nick = nick;
        editor.commit();
    }

    public String getNunm() {
        return nunm;
    }

    public void setNunm(String nunm) {
        editor.putString("NUNM", nunm);
        this.nunm = nunm;
        editor.commit();
    }

    public String getNuhp() {
        return nuhp;
    }

    public void setNuhp(String nuhp) {
        editor.putString("NUHP", nuhp);
        this.nuhp = nuhp;
        editor.commit();
    }

    public String getFpnm() {
        return fpnm;
    }

    public void setFpnm(String fpnm) {
        editor.putString("FPNM", fpnm);
        this.fpnm = fpnm;
        editor.commit();
    }

    public String getFphp() {
        return fphp;
    }

    public void setFphp(String fphp) {
        editor.putString("FPHP", fphp);
        this.fphp = fphp;
        editor.commit();
    }
}
