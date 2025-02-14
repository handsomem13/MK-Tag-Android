package com.moko.bxp.tag.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.moko.bxp.tag.R;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class ScanFilterDialog extends BaseDialog {
    @BindView(R.id.et_filter_name)
    EditText etFilterName;
    @BindView(R.id.et_filter_mac)
    EditText etFilterMac;
    @BindView(R.id.tv_rssi)
    TextView tvRssi;
    @BindView(R.id.sb_rssi)
    SeekBar sbRssi;
    @BindView(R.id.cb_PendingUpdates)  CheckBox cbPendingUpdates;
    @BindView(R.id.cb_autoConnect)  CheckBox cbAutoConnect;
    private int filterRssi;
    private String filterName;
    private String filterMac;
    private boolean pendingUpdates;
    private boolean autConnect;
    public ScanFilterDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_scan_filter;
    }

    @Override
    protected void renderConvertView(View convertView, Object o) {
        tvRssi.setText(String.format("%sdBm", filterRssi + ""));
        sbRssi.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int rssi = (progress * -1);
                tvRssi.setText(String.format("%sdBm", rssi + ""));
                filterRssi = rssi;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbRssi.setProgress(Math.abs(filterRssi));
        if (!TextUtils.isEmpty(filterName)) {
            etFilterName.setText(filterName);
            etFilterName.setSelection(filterName.length());
        }
        if (!TextUtils.isEmpty(filterMac)) {
            etFilterMac.setText(filterMac);
            etFilterMac.setSelection(filterMac.length());
        }
        cbPendingUpdates.setChecked(pendingUpdates);
        cbAutoConnect.setChecked(autConnect);
        setDismissEnable(true);
    }

    @OnClick(R.id.iv_filter_name_delete)
    public void onFilterNameDete(View view) {
        etFilterName.setText("");
    }

    @OnClick(R.id.iv_filter_mac_delete)
    public void onFilterMacDelete(View view) {
        etFilterMac.setText("");
    }

    @OnClick(R.id.tv_done)
    public void onViewClicked(View view) {
        listener.onDone(etFilterName.getText().toString(), etFilterMac.getText().toString(), filterRssi,cbPendingUpdates.isChecked(),cbAutoConnect.isChecked());
        dismiss();
    }

    private OnScanFilterListener listener;

    public void setOnScanFilterListener(OnScanFilterListener listener) { this.listener = listener; }
    public void setFilterName(String filterName) { this.filterName = filterName; }
    public void setFilterMac(String filterMac) { this.filterMac = filterMac; }
    public void setFilterRssi(int filterRssi) { this.filterRssi = filterRssi; }
    public void setPendingUpdates(boolean pendingUpdates) { this.pendingUpdates = pendingUpdates;  }
    public void setAutConnect(boolean autConnect) { this.autConnect = autConnect; }

    public interface OnScanFilterListener {
        void onDone(String filterName, String filterMac, int filterRssi,boolean pendingUpdates,boolean cbAutoConnect);
    }
}
