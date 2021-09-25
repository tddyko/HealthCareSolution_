package com.gchc.ing.login;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import java.io.FileDescriptor;

/**
 * Created by MrsWin on 2017-03-26.
 */

public class JoinDataVo implements IBinder {
    private String mber_no;         // ING회원 번호
    private String id;              // 아이디
    private String bef_pwd;         // 변경 전 비밀번호
    private String aft_pwd;         // 변경 후 비밀번호
    private String name;            // 이름
    private String phoneNum;        // 전화번호
    private String sex;             // 성별
    private String birth;           // 생년월일
    private String height;          // 키
    private String weight;          // 몸무게
    private String targetWeight;    // 목표몸무게
    private int activeType;         // 활동정보
    private String haveDisease;     // 보유질환
    private String medicine;        // 복약중 약
    private String smoke;           // 흡연여부


    public String getMber_no() {
        return mber_no;
    }

    public void setMber_no(String mber_no) {
        this.mber_no = mber_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getAft_pwd() {
        return aft_pwd;
    }

    public void setAft_pwd(String aft_pwd) {
        this.aft_pwd = aft_pwd;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }

    public int getActiveType() {
        return activeType;
    }

    public void setActiveType(int activeType) {
        this.activeType = activeType;
    }

    public String getHaveDisease() {
        return haveDisease;
    }

    public void setHaveDisease(String haveDisease) {
        this.haveDisease = haveDisease;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    @Override
    public String getInterfaceDescriptor() throws RemoteException {
        return null;
    }

    @Override
    public boolean pingBinder() {
        return false;
    }

    @Override
    public boolean isBinderAlive() {
        return false;
    }

    @Override
    public IInterface queryLocalInterface(String descriptor) {
        return null;
    }

    @Override
    public void dump(FileDescriptor fd, String[] args) throws RemoteException {

    }

    @Override
    public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {

    }

    @Override
    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        return false;
    }

    @Override
    public void linkToDeath(DeathRecipient recipient, int flags) throws RemoteException {

    }

    @Override
    public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
        return false;
    }

}
