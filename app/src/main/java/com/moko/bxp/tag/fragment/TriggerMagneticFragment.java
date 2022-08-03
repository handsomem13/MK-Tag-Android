package com.moko.bxp.tag.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.moko.bxp.tag.R;
import com.moko.bxp.tag.activity.SlotDataActivity;
import com.moko.bxp.tag.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TriggerMagneticFragment extends Fragment {

    private static final String TAG = TriggerMagneticFragment.class.getSimpleName();

    @BindView(R.id.iv_start)
    ImageView ivStart;
    @BindView(R.id.et_duration)
    EditText etDuration;
    @BindView(R.id.iv_stop)
    ImageView ivStop;
    @BindView(R.id.tv_trigger_tips)
    TextView tvTriggerTips;


    private SlotDataActivity activity;
    private boolean mIsStart = true;
    private int mDuration = 30;


    public TriggerMagneticFragment() {
    }

    public static TriggerMagneticFragment newInstance() {
        TriggerMagneticFragment fragment = new TriggerMagneticFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_trigger_magnetic, container, false);
        ButterKnife.bind(this, view);
        activity = (SlotDataActivity) getActivity();
        if (activity.slotData.triggerType == 6) {
            // 霍尔触发
            mIsStart = activity.slotData.triggerAdvStatus == 1;
            mDuration = activity.slotData.triggerAdvDuration;
        }
        changeTips();
        etDuration.setText(String.valueOf(mDuration));
        etDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String durationStr = s.toString();
                if (!TextUtils.isEmpty(durationStr)) {
                    mDuration = Integer.parseInt(durationStr);
                    changeTips();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    public int getDuration() {
        if (mIsStart) {
            String durationStr = etDuration.getText().toString();
            if (TextUtils.isEmpty(durationStr)) {
                ToastUtils.showToast(getActivity(), "The advertising can not be empty.");
                return -1;
            }
            int duration = Integer.parseInt(durationStr);
            if (duration > 65535) {
                ToastUtils.showToast(activity, "The advertising range is 0~65535");
                return -1;
            }
            return duration;
        } else {
            return 30;
        }
    }

    public void magneticStart() {
        mIsStart = true;
        changeTips();
    }

    public void magneticStop() {
        mIsStart = false;
        changeTips();
    }


    private void changeTips() {
        String triggerTips = getString(R.string.trigger_magnetic_tips, mIsStart ?
                (mDuration == 0 ? "start advertising" : String.format("start advertising for %ds", mDuration)) :
                "stop advertising", mIsStart ? "stop" : "start");
        tvTriggerTips.setText(triggerTips);
        ivStart.setImageResource(mIsStart ? R.drawable.icon_selected : R.drawable.icon_unselected);
        ivStop.setImageResource(mIsStart ? R.drawable.icon_unselected : R.drawable.icon_selected);
    }

    public int getTriggerAdvStatus() {
        return mIsStart ? 1 : 0;
    }
}
