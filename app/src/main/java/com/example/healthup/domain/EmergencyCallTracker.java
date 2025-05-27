package com.example.healthup.domain;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.healthup.Sos.UserSosInfoActivity;

public class EmergencyCallTracker extends PhoneStateListener {
    private final Context context;
    private boolean wasCalling = false;
    private final String calledNumber;

    public EmergencyCallTracker(Context context, String calledNumber) {
        this.context = context.getApplicationContext(); // Prevent memory leak
        this.calledNumber = calledNumber;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCallStateChanged(int state, String ignoredPhoneNumber) {
        super.onCallStateChanged(state, ignoredPhoneNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_OFFHOOK:
                wasCalling = true;
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                if (wasCalling) {
                    wasCalling = false;
                    Intent intent = new Intent(context, UserSosInfoActivity.class);
                    intent.putExtra("PhoneNum", calledNumber);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                break;
        }
    }
}
