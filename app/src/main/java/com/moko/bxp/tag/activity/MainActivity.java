package com.moko.bxp.tag.activity;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.elvishew.xlog.XLog;
import com.google.gson.JsonObject;
import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.bxp.sethala.clients.Configuration;
import com.moko.bxp.sethala.clients.DeviceInformationClient;
import com.moko.bxp.sethala.clients.DfuMqttClient;
import com.moko.bxp.sethala.clients.PasswordMqttClient;
import com.moko.bxp.sethala.database.BeaconInformationModel;
import com.moko.bxp.sethala.database.SqliteHelper;
import com.moko.bxp.sethala.helpers.Tag;
import com.moko.bxp.sethala.interfaces.DeviceInformationService;
import com.moko.bxp.sethala.models.DeviceModel;
import com.moko.bxp.sethala.sqlite.BeaconDatabaseHelper;
import com.moko.bxp.tag.AppConstants;
import com.moko.bxp.tag.R;
import com.moko.bxp.tag.adapter.DeviceListAdapter;
import com.moko.bxp.tag.dialog.AlertMessageDialog;
import com.moko.bxp.tag.dialog.LoadingDialog;
import com.moko.bxp.tag.dialog.LoadingMessageDialog;
import com.moko.bxp.tag.dialog.PasswordDialog;
import com.moko.bxp.tag.dialog.ScanFilterDialog;
import com.moko.bxp.tag.entity.AdvInfo;
import com.moko.bxp.tag.utils.AdvInfoAnalysisImpl;
import com.moko.bxp.tag.utils.SPUtiles;
import com.moko.bxp.tag.utils.ToastUtils;
import com.moko.support.MokoBleScanner;
import com.moko.support.MokoSupport;
import com.moko.support.OrderTaskAssembler;
import com.moko.support.callback.MokoScanDeviceCallback;
import com.moko.support.entity.DeviceInfo;
import com.moko.support.entity.OrderCHAR;
import com.moko.support.entity.ParamsKeyEnum;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.RequiresApi;
import androidx.annotation.StyleableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements MokoScanDeviceCallback, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
    @BindView(R.id.rv_devices)
    RecyclerView rvDevices;
    @BindView(R.id.tv_device_num)
    TextView tvDeviceNum;
    @BindView(R.id.rl_edit_filter)
    RelativeLayout rl_edit_filter;
    @BindView(R.id.rl_filter)
    RelativeLayout rl_filter;
    @BindView(R.id.tv_filter)
    TextView tv_filter;
    @BindView(R.id.tv_ErrorMessage)
    TextView tv_ErrorMessage;
    private boolean mReceiverTag = false;
    private ConcurrentHashMap<String, AdvInfo> advInfoHashMap;
    private ArrayList<AdvInfo> advInfoList;
    private DeviceListAdapter adapter;
    private MokoBleScanner mokoBleScanner;
    private Handler mHandler;
    private boolean isPasswordError;
    private boolean isVerifyPassword;
    private List<BeaconInformationModel> pendingInstallations  =  new ArrayList<>();
    private BeaconDatabaseHelper databaseHelper ;
    private boolean updateFirmware = false;

    private String[] passwords = new String[] { "S3th4141976" ,"Moko4321", "S3th4l41976"};
    private List<String> triedPassword =  new ArrayList<>();
    public boolean cbAutoConnect = true;
    public boolean requestFirmware;
    public boolean resetDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MokoSupport.getInstance().init(getApplicationContext());
        databaseHelper = new BeaconDatabaseHelper(getApplicationContext());
        advInfoHashMap = new ConcurrentHashMap<>();
        advInfoList = new ArrayList<>();
        adapter = new DeviceListAdapter();
        adapter.replaceData(advInfoList);
        adapter.setOnItemChildClickListener(this);
        adapter.openLoadAnimation();
        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_recycleview_divider));
        rvDevices.addItemDecoration(itemDecoration);
        rvDevices.setAdapter(adapter);
        mHandler = new Handler(Looper.getMainLooper());
        mokoBleScanner = new MokoBleScanner(this);
        EventBus.getDefault().register(this);
        mSavedPassword = SPUtiles.getStringValue(this, AppConstants.SP_KEY_SAVED_PASSWORD, "");
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        if (!MokoSupport.getInstance().isBluetoothOpen()) {
            // 蓝牙未打开，开启蓝牙
            MokoSupport.getInstance().enableBluetooth();
        } else {
            if (animation == null) {
                startScan();
            }
        }
        setUpDatabase();
    }

    @Override
    protected void onStart() {
        this.syncDevices();
        super.onStart();
    }
    private  void setUpDatabase(){
        try {
            SqliteHelper sqlDatabase = new SqliteHelper(this);
            sqlDatabase.getWritableDatabase();
            sqlDatabase.close();
        }catch (Exception ex){

        }
    }
    private void  syncDevices(){
        try {
            if(cbAutoConnect){
                mokoBleScanner.stopScanDevice();
                onStopScan();
            }
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        try {

            DeviceInformationService deviceInformationService = DeviceInformationClient.getApiService();
            Call<DeviceModel> call = deviceInformationService.getDevices(Configuration.Authorization);
            call.enqueue(new Callback<DeviceModel>() {
                @Override
                public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                    if (response.isSuccessful()) {
                        DeviceModel model = response.body();
                        // Now you can use the Person object, which contains the parsed JSON data.
                        Log.d("MyActivity", "DeviceInformationService API CALL: "+ model.Message +" :: " + model.Success);
                        databaseHelper.deleteAllRecords();
                        pendingInstallations = model.Data;
                        if(model.Data.size()>0){
                            databaseHelper.insertMany(model.Data);
                        }
                        startScan();
                    } else {
                        // Handle error
                        Log.e("MyActivity", "DeviceInformationService API CALL: Error :: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<DeviceModel> call, Throwable t) {
                    // Handle failure
                    Log.e("MyActivity", "Error: " + t.getMessage());
                }
            });
        }catch (Exception ex){
            Log.e(Tag.AuthClient,ex.toString() );
        }
    }
    private String unLockResponse;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            if (animation != null) {
                                mHandler.removeMessages(0);
                                mokoBleScanner.stopScanDevice();
                                onStopScan();
                            }
                            break;
                        case BluetoothAdapter.STATE_ON:
                            if (animation == null) {
                                startScan();
                            }
                            break;

                    }
                }
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectStatusEvent(ConnectStatusEvent event) {
        String action = event.getAction();
        if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
            mPassword = "";
            // 设备断开，通知页面更新
            dismissLoadingProgressDialog();
            dismissLoadingMessageDialog();
            if (triedPassword.size()<3) {
                isVerifyPassword = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    showPasswordDialog();
                }
                return;
            }
            if (animation == null) {
                if (isPasswordError) {
                    isPasswordError = false;
                } else {
                    ToastUtils.showToast(MainActivity.this, "Connection failed");
                    dismissLoadingMessageDialog();
                    dismissLoadingProgressDialog();
                }
                startScan();
            }
        }
        if (MokoConstants.ACTION_DISCOVER_SUCCESS.equals(action)) {
            // 设备连接成功，通知页面更新
            dismissLoadingProgressDialog();
            if (!TextUtils.isEmpty(mPassword)) {
                showLoadingMessageDialog();
                mHandler.postDelayed(() -> {
                    ArrayList<OrderTask> orderTasks = new ArrayList<>();
                    orderTasks.add(OrderTaskAssembler.setPassword(mPassword));
                    MokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
                }, 500);
                return;
            }
            showLoadingProgressDialog();
            mHandler.postDelayed(() -> {
                ArrayList<OrderTask> orderTasks = new ArrayList<>();
                orderTasks.add(OrderTaskAssembler.getVerifyPasswordEnable());
                MokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
            }, 500);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderTaskResponseEvent(OrderTaskResponseEvent event) {
        final String action = event.getAction();
        if (MokoConstants.ACTION_ORDER_TIMEOUT.equals(action)) {
            dismissLoadingMessageDialog();
            dismissLoadingProgressDialog();
        }
        if (MokoConstants.ACTION_ORDER_FINISH.equals(action)) {
            dismissLoadingMessageDialog();
            dismissLoadingProgressDialog();
        }
        if (MokoConstants.ACTION_ORDER_RESULT.equals(action)) {
            OrderTaskResponse response = event.getResponse();
            OrderCHAR orderCHAR = (OrderCHAR) response.orderCHAR;
            int responseType = response.responseType;
            byte[] value = response.responseValue;
            switch (orderCHAR) {
                case CHAR_PASSWORD:
                    if (value.length == 5) {
                        int header = value[0] & 0xFF;// 0xEB
                        int flag = value[1] & 0xFF;// read or write
                        int cmd = value[2] & 0xFF;
                        if (header != 0xEB)
                            return;
                        ParamsKeyEnum configKeyEnum = ParamsKeyEnum.fromParamKey(cmd);
                        if (configKeyEnum == null) {
                            return;
                        }
                        int length = value[3] & 0xFF;
                        if (flag == 0x00 && length == 0x01) {
                            // read
                            int result = value[4] & 0xFF;
                            switch (configKeyEnum) {
                                case KEY_VERIFY_PASSWORD_ENABLE:
                                    if (result == 0x01) {
                                        isVerifyPassword = true;
                                        MokoSupport.getInstance().disConnectBle();
                                    } else {
                                        Intent i = new Intent(this, DeviceInfoActivity.class);
                                        i.putExtra(AppConstants.EXTRA_KEY_PASSWORD_VERIFICATION, false);
                                        i.putExtra(AppConstants.EXTRA_KEY_UPDATE_FIRMWARE , updateFirmware);
                                        i.putExtra(AppConstants.EXTRA_KEY_AUTOCONNECT, cbAutoConnect&&updateFirmware);
                                        i.putExtra(AppConstants.EXTRA_KEY_RESETDEVICE , resetDevice);
                                        i.putExtra(AppConstants.EXTRA_KEY_REQUESTFIRMWARE , requestFirmware);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismissLoadingMessageDialog();
                                            }
                                        });
                                        startActivityForResult(i, AppConstants.REQUEST_CODE_DEVICE_INFO);
                                    }
                                    break;
                            }
                        }
                        if (flag == 0x01 && length == 0x01) {
                            // write
                            int result = value[4] & 0xFF;
                            switch (configKeyEnum) {
                                case KEY_PASSWORD:
                                    if (result == 0xAA) {
                                        mSavedPassword = mPassword;
                                        SPUtiles.setStringValue(this, AppConstants.SP_KEY_SAVED_PASSWORD, mSavedPassword);
                                        XLog.i("Success");
                                        Intent i = new Intent(this, DeviceInfoActivity.class);
                                        i.putExtra(AppConstants.EXTRA_KEY_PASSWORD_VERIFICATION, true);
                                        i.putExtra(AppConstants.EXTRA_KEY_UPDATE_FIRMWARE , updateFirmware);
                                        i.putExtra(AppConstants.EXTRA_KEY_RESETDEVICE , resetDevice);
                                        i.putExtra(AppConstants.EXTRA_KEY_REQUESTFIRMWARE , requestFirmware);
                                        i.putExtra(AppConstants.EXTRA_KEY_AUTOCONNECT, cbAutoConnect&&updateFirmware);
                                        try {
                                            JsonObject jsonMessage = new JsonObject();
                                            String mac = !TextUtils.isEmpty(mSelectedDeviceMac) ?mSelectedDeviceMac : "";
                                            jsonMessage.addProperty("MacAddress",  mac);
                                            jsonMessage.addProperty("Password",  mPassword);
                                            PasswordMqttClient mqttClient = new PasswordMqttClient(getApplicationContext(),jsonMessage,mac);
                                            mqttClient.execute();
                                        } catch (Exception e) {
                                            Log.e(this.getLocalClassName(),e.toString());
                                            e.printStackTrace();
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismissLoadingMessageDialog();
                                            }
                                        });
                                        startActivityForResult(i, AppConstants.REQUEST_CODE_DEVICE_INFO);
                                    } else {
                                        isPasswordError = true;
                                        ToastUtils.showToast(this, "Password incorrect！"+ triedPassword.size()+"/"+passwords.length+" tries.");
                                        MokoSupport.getInstance().disConnectBle();
                                    }
                                    break;
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstants.REQUEST_CODE_DEVICE_INFO:
                    mPassword = "";
                    if (animation == null) {
                        startScan();
                    }
                    break;

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiverTag) {
            mReceiverTag = false;
            // 注销广播
            unregisterReceiver(mReceiver);
        }
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onStartScan() {
        advInfoHashMap.clear();
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                while (animation != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.replaceData(advInfoList);
                            tvDeviceNum.setText(String.format("DEVICE(%d)", advInfoList.size()));
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateDevices();
                }
            }
        }).start();
    }

    private AdvInfoAnalysisImpl advInfoAnalysisImpl;

    @Override
    public void onScanDevice(DeviceInfo deviceInfo) {
        AdvInfo advInfo = advInfoAnalysisImpl.parseDeviceInfo(deviceInfo);
        if (advInfo == null) return;
        advInfoHashMap.put(advInfo.mac, advInfo);
    }

    @Override
    public void onStopScan() {
        try {
            findViewById(R.id.iv_refresh).clearAnimation();
            animation = null;
            dismissLoadingMessageDialog();
            dismissLoadingProgressDialog();
        }catch (Exception e){
            Toast.makeText(this,  e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateDevices() {
        advInfoList.clear();
        if (!TextUtils.isEmpty(filterName)
                || !TextUtils.isEmpty(filterMac)
                || filterRssi != -100) {
            ArrayList<AdvInfo> advInfoListFilter = new ArrayList<>(advInfoHashMap.values());
            Iterator<AdvInfo> iterator = advInfoListFilter.iterator();
            pendingInstallations = databaseHelper.GetAll();
            while (iterator.hasNext()) {
                AdvInfo advInfo = iterator.next();
                if (advInfo.rssi > filterRssi) {
                    if (TextUtils.isEmpty(filterName) && TextUtils.isEmpty(filterMac)) {
                        Log.i("Scan Filter", "Scanning for any device");
                        continue;
                    }
                    else if(pendingUpdates){
                        Log.i("Scan Filter", "Scanning for pending updates only");
                        if (TextUtils.isEmpty(advInfo.mac)) {
                            iterator.remove();
                            continue;
                        }
                        BeaconInformationModel beacon = pendingInstallations.stream()
                                .filter(_beacon -> advInfo.mac.toLowerCase().replaceAll(":", "").equals(_beacon.getMacAddress().toLowerCase().replaceAll(":", "")))
                                .findAny()
                                .orElse(null);
                        if(beacon==null){
                            iterator.remove();
                            continue;
                        }
                    }
                    else {
                        if (!TextUtils.isEmpty(filterMac) && TextUtils.isEmpty(advInfo.mac)) {
                            iterator.remove();
                        } else if (!TextUtils.isEmpty(filterMac) && advInfo.mac.toLowerCase().replaceAll(":", "").contains(filterMac.toLowerCase())) {
                            continue;
                        } else if (!TextUtils.isEmpty(filterName) && TextUtils.isEmpty(advInfo.name)) {
                            iterator.remove();
                        } else if (!TextUtils.isEmpty(filterName) && advInfo.name.toLowerCase().contains(filterName.toLowerCase())) {
                            continue;
                        } else {
                            iterator.remove();
                        }
                    }
                } else {
                    iterator.remove();
                }
            }
            advInfoList.addAll(advInfoListFilter);
        } else {
            advInfoList.addAll(advInfoHashMap.values());
        }
        if(cbAutoConnect==true && pendingUpdates && advInfoList.size()>0){
            for (int i= 0 ; i<adapter.getData().size() ; i++ ) {
                AdvInfo advInfo= adapter.getItem(i);
                if (advInfo != null && !isFinishing()) {
                    mSelectedDeviceMac = advInfo.mac;
                    BeaconInformationModel beacon = databaseHelper.GetByMacAdrress(mSelectedDeviceMac.toUpperCase().replaceAll(":", ""));
                    if (beacon != null && !TextUtils.isEmpty(beacon.getIsUpToDate()) && beacon.getIsUpToDate() != "true") {
                        if (animation != null) {
                            mHandler.removeMessages(0);
                            mokoBleScanner.stopScanDevice();
                        }
                        connectDevice(advInfo);
                        break;
                    }
                }
            }
        }
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(advInfoList, new Comparator<AdvInfo>() {
            @Override
            public int compare(AdvInfo lhs, AdvInfo rhs) {
                if (lhs.rssi > rhs.rssi) {
                    return -1;
                } else if (lhs.rssi < rhs.rssi) {
                    return 1;
                }
                return 0;
            }
        });
    }

    private Animation animation = null;
    public String filterName;
    public String filterMac;
    public int filterRssi = -100;
    public  boolean pendingUpdates = true;

    private void startScan() {
        try {
            dismissLoadingMessageDialog();
            dismissLoadingProgressDialog();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        try {
            tv_ErrorMessage.setText("");
            if (!MokoSupport.getInstance().isBluetoothOpen()) {
                // 蓝牙未打开，开启蓝牙
                MokoSupport.getInstance().enableBluetooth();
                tv_ErrorMessage.setText("Bluetooth is not enabled");
                return;
            }
            animation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
            findViewById(R.id.iv_refresh).startAnimation(animation);
            advInfoAnalysisImpl = new AdvInfoAnalysisImpl();
            if(pendingUpdates ){
                pendingInstallations = databaseHelper.GetAll();
                ArrayList<String> beacons =    new ArrayList<>();
                for (BeaconInformationModel beacon: pendingInstallations) {
                    beacons.add(beacon.getMacAddress());
                }
                if(beacons.size()==0){
                    tv_ErrorMessage.setText("Checking for update. No pending updates found");
                    return;
                }
                mokoBleScanner.setBeaconList(beacons);
            }else{
                mokoBleScanner.setBeaconList(null);
            }
            mokoBleScanner.startScanDevice(this);
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }


    private LoadingDialog mLoadingDialog;

    private void showLoadingProgressDialog() {
        mLoadingDialog = new LoadingDialog();
        mLoadingDialog.show(getSupportFragmentManager());

    }

    private void dismissLoadingProgressDialog() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismissAllowingStateLoss();
    }

    private LoadingMessageDialog mLoadingMessageDialog;

    private void showLoadingMessageDialog() {
        mLoadingMessageDialog = new LoadingMessageDialog();
        mLoadingMessageDialog.setMessage("Verifying..");
        mLoadingMessageDialog.show(getSupportFragmentManager());

    }

    private void dismissLoadingMessageDialog() {
        if (mLoadingMessageDialog != null)
            mLoadingMessageDialog.dismissAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Date d = new Date();
        XLog.d("resuming main screen at "+ d.toString());
    }

    private String mPassword;
    private String mSavedPassword;
    private String mSelectedDeviceMac;

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (!MokoSupport.getInstance().isBluetoothOpen()) {
            // 蓝牙未打开，开启蓝牙
            MokoSupport.getInstance().enableBluetooth();
            return;
        }
        final AdvInfo advInfo = (AdvInfo) adapter.getItem(position);
        if (advInfo != null && !isFinishing()) {
            if (animation != null) {
                mHandler.removeMessages(0);
                mokoBleScanner.stopScanDevice();
            }
            mSelectedDeviceMac = advInfo.mac;
            connectDevice(advInfo);
        }
    }
    void connectDevice(AdvInfo advInfo){
        requestFirmware = false;
        resetDevice = false;
        //AdvInfo{name='Sethala M4 113', mac='B4:35:22:60:D5:49'}
        if(advInfo!=null && advInfo.name!=null && advInfo.name.toLowerCase().contains("sethala m4 113")){
            passwords = new String[] { "S3th4l41976", "Moko4321" , "S3th4141976"};
        }
        else if(advInfo!=null && advInfo.name!=null && advInfo.name.toLowerCase().contains("mk tag")){
            passwords = new String[] { "Moko4321" , "S3th4141976","S3th4l41976" };
        }else{
            passwords = new String[] {  "S3th4141976","Moko4321", "S3th4l41976" };
        }
        BeaconInformationModel beacon = databaseHelper.GetByMacAdrress(mSelectedDeviceMac.toUpperCase().replaceAll(":", ""));
        if (beacon != null && !TextUtils.isEmpty(beacon.getIsUpToDate()) && beacon.getIsUpToDate()!="true" ) {
            updateFirmware = true;
            boolean firmware = beacon.getRequestFirmware().equals("true");
            if(beacon.getRequestFirmware().equals("true"))
                requestFirmware = true;
            if(beacon.getReset().equals("true"))
                resetDevice = true;
        }else{
            updateFirmware = false;
        }
        showLoadingProgressDialog();
        mPassword = passwords[0];
        triedPassword.clear();
        triedPassword.add(mPassword);
        XLog.i(mPassword);
        ivRefresh.postDelayed(() -> MokoSupport.getInstance().connDevice(mSelectedDeviceMac), 500);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showPasswordDialog() {
        // show password
        if(triedPassword.size()!= 3){
            try {
                mPassword = passwords[triedPassword.size()];
                XLog.i(mPassword);
                triedPassword.add(mPassword);
                showLoadingProgressDialog();
                ivRefresh.postDelayed(() -> MokoSupport.getInstance().connDevice(mSelectedDeviceMac), 500);
                return;
            }catch (Exception x) {
                x.printStackTrace();
            }
        }
        final PasswordDialog dialog = new PasswordDialog();
        dialog.setPassword(mSavedPassword);
        dialog.setOnPasswordClicked(new PasswordDialog.PasswordClickListener() {
            @Override
            public void onEnsureClicked(String password) {
                if (!MokoSupport.getInstance().isBluetoothOpen()) {
                    MokoSupport.getInstance().enableBluetooth();
                    return;
                }
                XLog.i(password);
                mPassword = password;
                showLoadingProgressDialog();
                ivRefresh.postDelayed(() -> MokoSupport.getInstance().connDevice(mSelectedDeviceMac), 500);
            }

            @Override
            public void onDismiss() {
                if (animation == null)
                    startScan();
            }
        });
        dialog.show(getSupportFragmentManager());
    }
    public void onBack(View view) {
        if (isWindowLocked())
            return;
        back();
    }

    public void onAbout(View view) {
        if (isWindowLocked())
            return;
        startActivity(new Intent(this, AboutActivity.class));
    }

    public void onFilter(View view) {
        if (isWindowLocked())
            return;
        if (animation != null) {
            mHandler.removeMessages(0);
            mokoBleScanner.stopScanDevice();
        }
        ScanFilterDialog scanFilterDialog = new ScanFilterDialog(this);
        scanFilterDialog.setFilterName(filterName);
        scanFilterDialog.setFilterMac(filterMac);
        scanFilterDialog.setFilterRssi(filterRssi);
        scanFilterDialog.setPendingUpdates(pendingUpdates);
        scanFilterDialog.setAutConnect(cbAutoConnect);
        scanFilterDialog.setOnScanFilterListener((filterName, filterMac, filterRssi,pendingUpdates,cbAutoConnect) -> {
            MainActivity.this.filterName = filterName;
            MainActivity.this.filterMac = filterMac;
            String showFilterMac = "";
            if (filterMac.length() == 12) {
                StringBuffer stringBuffer = new StringBuffer(filterMac);
                stringBuffer.insert(2, ":");
                stringBuffer.insert(5, ":");
                stringBuffer.insert(8, ":");
                stringBuffer.insert(11, ":");
                stringBuffer.insert(14, ":");
                showFilterMac = stringBuffer.toString();
            } else {
                showFilterMac = filterMac;
            }
            MainActivity.this.pendingUpdates = pendingUpdates;
            MainActivity.this.cbAutoConnect = cbAutoConnect;
            MainActivity.this.filterRssi = filterRssi;
            if (!TextUtils.isEmpty(filterName)
                    || !TextUtils.isEmpty(showFilterMac)
                    || filterRssi != -100) {
                rl_filter.setVisibility(View.VISIBLE);
                rl_edit_filter.setVisibility(View.GONE);
                StringBuilder stringBuilder = new StringBuilder();
                if (!TextUtils.isEmpty(filterName)) {
                    stringBuilder.append(filterName);
                    stringBuilder.append(";");
                }
                if (!TextUtils.isEmpty(showFilterMac)) {
                    stringBuilder.append(showFilterMac);
                    stringBuilder.append(";");
                }
                if (filterRssi != -100) {
                    stringBuilder.append(String.format("%sdBm", filterRssi + ""));
                    stringBuilder.append(";");
                }
                tv_filter.setText(stringBuilder.toString());
            } else {
                rl_filter.setVisibility(View.GONE);
                rl_edit_filter.setVisibility(View.VISIBLE);
            }
            if (isWindowLocked())
                return;
            if (animation == null) {
                startScan();
            }
        });
        scanFilterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isWindowLocked())
                    return;
                if (animation == null) {
                    startScan();
                }
            }
        });
        scanFilterDialog.show();
    }

    private void back() {
        if (animation != null) {
            mHandler.removeMessages(0);
            mokoBleScanner.stopScanDevice();
        }
        AlertMessageDialog dialog = new AlertMessageDialog();
        dialog.setMessage(R.string.main_exit_tips);
        dialog.setOnAlertConfirmListener(() -> MainActivity.this.finish());
        dialog.show(getSupportFragmentManager());
    }

    public void onRefresh(View view) {
        if (isWindowLocked())
            return;
        if (!MokoSupport.getInstance().isBluetoothOpen()) {
            // 蓝牙未打开，开启蓝牙
            MokoSupport.getInstance().enableBluetooth();
            return;
        }
        if (animation == null) {
            startScan();
        } else {
            mHandler.removeMessages(0);
            mokoBleScanner.stopScanDevice();
        }
    }

    public void onFilterDelete(View view) {
        if (animation != null) {
            mHandler.removeMessages(0);
            mokoBleScanner.stopScanDevice();
        }
        rl_filter.setVisibility(View.GONE);
        rl_edit_filter.setVisibility(View.VISIBLE);
        filterName = "";
        filterMac = "";
        filterRssi = -100;
        if (isWindowLocked())
            return;
        if (animation == null) {
            startScan();
        }
    }
}
