package com.moko.support;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.util.Log;

import com.elvishew.xlog.XLog;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.support.callback.MokoScanDeviceCallback;
import com.moko.support.entity.DeviceInfo;
import com.moko.support.entity.OrderServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.core.content.ContextCompat;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public final class MokoBleScanner {

    private MokoLeScanHandler mMokoLeScanHandler;
    private MokoScanDeviceCallback mMokoScanDeviceCallback;

    private Context mContext;
    private ArrayList<String> beaconList = new ArrayList<>();
    public MokoBleScanner(Context context) {
        mContext = context;
    }

    public void setBeaconList(ArrayList<String> beaconList) {
        this.beaconList = beaconList;
    }

    public void startScanDevice(MokoScanDeviceCallback callback) {
        mMokoScanDeviceCallback = callback;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            XLog.i("Start scan");
        }
        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setLegacy(false)
                .build();
//        List<ScanFilter> scanFilterList = new ArrayList<>();
//        ScanFilter.Builder builder = new ScanFilter.Builder();
//        builder.setServiceData(new ParcelUuid(OrderServices.SERVICE_ADV_TRIGGER.getUuid()), null);
//        scanFilterList.add(builder.build());
        ScanFilter.Builder builder = new ScanFilter.Builder();
        List<ScanFilter> scanFilters = new ArrayList<>();
        if(beaconList!=null && beaconList.size()>0){
            for (String beacon: beaconList) {
                try {
                    String mac = "";
                    if (beacon.length() == 12) {
                        StringBuffer stringBuffer = new StringBuffer(beacon);
                        stringBuffer.insert(2, ":");
                        stringBuffer.insert(5, ":");
                        stringBuffer.insert(8, ":");
                        stringBuffer.insert(11, ":");
                        stringBuffer.insert(14, ":");
                        mac = stringBuffer.toString();
                    } else {
                        mac = beacon;
                    }
                    Log.i("MokoLeScanHandler", "Adding " + mac + " Address to the filter");
                    ScanFilter scanFilter = new ScanFilter.Builder()
                            .setDeviceAddress(mac)
                            .build();
                    scanFilters.add(scanFilter);
                }catch (Exception e){
                    Log.e("MokoLeScanHandler", e.toString());
                }
            }
        }
        mMokoLeScanHandler = new MokoLeScanHandler(callback);
        scanner.startScan(scanFilters, settings, mMokoLeScanHandler);
        callback.onStartScan();
    }

    public void stopScanDevice() {
        if (mMokoLeScanHandler != null && mMokoScanDeviceCallback != null) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                XLog.i("End scan");
            }
            final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            scanner.stopScan(mMokoLeScanHandler);
            mMokoScanDeviceCallback.onStopScan();
            mMokoLeScanHandler = null;
            mMokoScanDeviceCallback = null;
        }
    }

    public static class MokoLeScanHandler extends ScanCallback {

        private MokoScanDeviceCallback callback;

        public MokoLeScanHandler(MokoScanDeviceCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result != null) {
                BluetoothDevice device = result.getDevice();
                byte[] scanRecord = result.getScanRecord().getBytes();
                String name = result.getScanRecord().getDeviceName();
                int rssi = result.getRssi();
                if (scanRecord.length == 0 || rssi == 127) {
                    return;
                }
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.name = name;
                deviceInfo.rssi = rssi;
                deviceInfo.mac = device.getAddress();
                String scanRecordStr = MokoUtils.bytesToHexString(scanRecord);
                deviceInfo.scanRecord = scanRecordStr;
                deviceInfo.scanResult = result;
                callback.onScanDevice(deviceInfo);
            }
        }
    }
}
