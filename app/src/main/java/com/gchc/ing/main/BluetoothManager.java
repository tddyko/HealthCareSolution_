package com.gchc.ing.main;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

import com.gchc.ing.base.value.Define;
import com.gchc.ing.bluetooth.device.BaseDevice;
import com.gchc.ing.bluetooth.device.BloodDevice;
import com.gchc.ing.bluetooth.device.WeightDevice;
import com.gchc.ing.bluetooth.manager.DeviceDataUtil;
import com.gchc.ing.bluetooth.manager.DeviceManager;
import com.gchc.ing.bluetooth.model.BloodModel;
import com.gchc.ing.bluetooth.model.WeightModel;
import com.gchc.ing.component.CDialog;
import com.gchc.ing.database.DBHelper;
import com.gchc.ing.database.DBHelperWeight;
import com.gchc.ing.network.tr.data.Tr_login;
import com.gchc.ing.util.CDateUtil;
import com.gchc.ing.util.Logger;

import java.util.Date;
import java.util.Set;

public class BluetoothManager {
    private final String TAG = BluetoothManager.class.getSimpleName();

    private BloodDevice mDeviceBlood;
    private WeightDevice mDeviceWeight;

    private BluetoothAdapter bluetoothAdapter;
    private boolean bluetoothOpenCheck = false;

    private MainFragment mMainFragment;

    public BluetoothManager(MainFragment mainFragment) {
        mMainFragment           = mainFragment;
        this.bluetoothAdapter   = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {

            CDialog.showDlg(mainFragment.getContext(), "해당 단말은 블루투스를 지원하지 않습니다.");
            return;
        }
        Logger.i(TAG, "BluetoothManager start");

        connectDevices();
    }

    public void onResume() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled() && !bluetoothOpenCheck) {
//            Toast.makeText(mMainFragment.getContext(), "해당 기능을 진행하기위해 블루투스를 켜주세요.", Toast.LENGTH_LONG).show();
//            openBluetoothSetting();
//            bluetoothOpenCheck = true;
            return;
        }
        connectDevices();
    }

    public void onPause() {
        disconnectDevices();
    }

    private void connectDevices() {
        Logger.i(TAG, "Bluetooth.connectDevices()");
        connectBloodDevice();
        connectWeightDevice();
    }

    private boolean isPairedDevice(BluetoothDevice device) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices != null) {
            for (BluetoothDevice pairedDevice : pairedDevices) {
                if (pairedDevice.equals(device)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void connectBloodDevice() {

        if (mDeviceBlood != null) {
            mDeviceBlood.disconnect();
        }

        if (DeviceManager.isRegDevice(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_BLOOD)) {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DeviceManager.getRegDeviceAddress(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_BLOOD));
            if (isPairedDevice(device)) {
                mDeviceBlood = new BloodDevice(device);
                mDeviceBlood.setOnBluetoothListener(onBloodDeviceListener);
                mDeviceBlood.connect(mMainFragment.getContext());
            } else {
//                String message = "해당 단말은 페어링 모드에서 페어링이 필요합니다.(설정창에서 페어링해주세요.)";
//                CDialog.showDlg(mMainFragment.getContext(), message, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        openBluetoothSetting();
//                    }
//                }, null);

            }
        } else {
            Logger.e(TAG, "BlueTooth 혈당계가 등록되어 있지 않습니다.");
        }
    }

    private void connectWeightDevice() {
        if (mDeviceWeight != null) {
            mDeviceWeight.disconnect();
        }
        if (DeviceManager.isRegDevice(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_WEIGHT)) {
            /*
            회원정보로 벤드업데이트-벤트초기화 되어 보류합니다.
            Tr_login info = Define.getInstance().getLoginInfo();
            int mber_height = StringUtil.getIntVal(info.mber_height);
            float mber_weight = StringUtil.getFloatVal(info.mber_bdwgh_app);
            int mber_sex = StringUtil.getIntVal(info.mber_sex);
            int rBirth      = StringUtil.getIntVal(info.mber_lifyea.substring(0, 4));                      // 회원 생년
            String nowYear  = CDateUtil.getFormattedString_yyyy(System.currentTimeMillis());                // 현재 년도
            int rAge        = (StringUtil.getIntVal(nowYear) - rBirth);                                     // 회원 나이
            BaseDevice.Gender sex = mber_sex==1?WeightDevice.Gender.MAN:WeightDevice.Gender.WOMAN;
            */
            BluetoothDevice device  = bluetoothAdapter.getRemoteDevice(DeviceManager.getRegDeviceAddress(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_WEIGHT));
            mDeviceWeight           = new WeightDevice(device);
            mDeviceWeight.setOnBluetoothListener(onWeightDeviceListener);
            mDeviceWeight.setUserInfo(26, 181, BaseDevice.Gender.MAN);
            mDeviceWeight.connect(mMainFragment.getContext());
        } else {
            Logger.e(TAG, "BlueTooth 체중계가 등록되어 있지 않습니다.");
        }
    }

    private void disconnectDevices() {
        if (mDeviceBlood != null) {
            mDeviceBlood.disconnect();
            mDeviceBlood = null;
        }
        if (mDeviceWeight != null) {
            mDeviceWeight.disconnect();
            mDeviceWeight = null;
        }
    }


    private BaseDevice.OnBluetoothListener<SparseArray<BloodModel>> onBloodDeviceListener = new BaseDevice.OnBluetoothListener<SparseArray<BloodModel>>() {
        @Override
        public void onStart() {
            Logger.i(TAG, "BlueTooth onBloodDeviceListener onStart()");
        }

        @Override
        public void onConnected() {
            Logger.i(TAG, "BlueTooth onBloodDeviceListener onConnected()");

            mDeviceBlood.readData();
        }

        @Override
        public void onDisConnected() {
            Logger.i(TAG, "BlueTooth onBloodDeviceListener onDisConnected()");
        }

        @Override
        public void onReceivedData(SparseArray<BloodModel> dataModel) {
            Log.d(TAG, "BlueTooth Read Blood: size="+dataModel.size() +", " + dataModel.toString());
            if (dataModel.size() > 0) {
                BloodModel data = dataModel.get(dataModel.keyAt(dataModel.size() - 1));
                data.setRegtype(BloodModel.INPUT_TYPE_DEVICE);
                data.setRegTime(CDateUtil.getForamtyyyyMMddHHmmss(new Date(System.currentTimeMillis())));
                data.setBefore(""+data.getEatType());
                Logger.i(TAG, "onReceivedData getEatType="+data.getEatType());
                Logger.i(TAG, "onReceivedData getTime="+data.getTime());
                Logger.i(TAG, "onReceivedData getIdx="+data.getIdx());
                Logger.i(TAG, "onReceivedData getSequenceNumber="+data.getSequenceNumber());
                Logger.i(TAG, "onReceivedData getSugar="+data.getSugar());
                Logger.i(TAG, "onReceivedData getRegTime="+data.getRegTime());

                //건강메시지 전달
                new DeviceDataUtil().uploadSugarData(mMainFragment, dataModel, new IBluetoothResult() {
                    @Override
                    public void onResult(boolean isSuccess) {
                        Logger.i(TAG, "uploadSugarData isSuccess="+isSuccess);

                        mMainFragment.notifyAdapter();
                    }
                });
            }
        }
    };

    /**
     * 체중계 리스너
     */
    private BaseDevice.OnBluetoothListener<WeightModel> onWeightDeviceListener = new BaseDevice.OnBluetoothListener<WeightModel>() {
        @Override
        public void onStart() {
            Logger.i(TAG, "BlueTooth onWeightDeviceListener onStart()");
        }

        @Override
        public void onConnected() {
            Logger.i(TAG, "BlueTooth onWeightDeviceListener onConnected()");
            mDeviceWeight.readData();
        }

        @Override
        public void onDisConnected() {
            Logger.i(TAG, "BlueTooth onWeightDeviceListener onDisConnected()");
        }

        @Override
        public void onReceivedData(final WeightModel dataModel) {
            Log.d(TAG, "BlueTooth Read Weight:" + dataModel.toString());
            dataModel.setRegType("D");
            dataModel.setIdx(CDateUtil.getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
            dataModel.setRegDate(CDateUtil.getForamtyyyyMMddHHmmss(new Date(System.currentTimeMillis())));

            new DeviceDataUtil().uploadWeight(mMainFragment, dataModel, new IBluetoothResult() {
                @Override
                public void onResult(boolean isSuccess) {


                    Tr_login login = Define.getInstance().getLoginInfo();

                    DBHelper helper = new DBHelper(mMainFragment.getContext());
                    DBHelperWeight weightDb = helper.getWeightDb();
                    DBHelperWeight.WeightStaticData bottomData = weightDb.getResultStatic();
                    if(!bottomData.getWeight().isEmpty()) {
                        login.mber_bdwgh_app = Float.toString(dataModel.getWeight());
                    }

                    Define.getInstance().setLoginInfo(login);

                    mMainFragment.notifyAdapter();
                }
            });
        }
    };

    protected void openBluetoothSetting() {
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        mMainFragment.startActivity(intentOpenBluetoothSettings);
    }

    public interface IBluetoothResult {
        void onResult(boolean isSuccess);
    }
}
