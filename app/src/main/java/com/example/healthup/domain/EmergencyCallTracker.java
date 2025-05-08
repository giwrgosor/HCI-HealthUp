package com.example.healthup.domain;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.healthup.Sos.UserSosInfoActivity;

public class EmergencyCallTracker extends PhoneStateListener {
    private final Context context;
    private boolean wasCalling = false;

    public EmergencyCallTracker(Context context) {
        this.context = context.getApplicationContext(); // Avoid memory leaks
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        super.onCallStateChanged(state, phoneNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_OFFHOOK:
                wasCalling = true;
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                if (wasCalling) {
                    wasCalling = false;
                    Intent intent = new Intent(context, UserSosInfoActivity.class);
                    intent.putExtra("PhoneNum",phoneNumber);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                break;
        }
    }
}
