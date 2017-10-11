package com.mytrendin.inappmessaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String KEY_FORMAT = "format";
    private static final String SMS_BUNDLE = "pdus";
    private String smsBody, smsMessage;

    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Bundle intentExtras = intent.getExtras();
        String format = intentExtras.getString(KEY_FORMAT);
        Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
        String storedPassword = sharedPreferences.getString(MainActivity.KEY_PASSWORD, null);
        for (Object sm : sms) {

            SmsMessage mSmsMessage;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mSmsMessage = SmsMessage.createFromPdu((byte[]) sm, format);
            } else {
                mSmsMessage = SmsMessage.createFromPdu((byte[]) sm);
            }
            smsBody = mSmsMessage.getMessageBody();
            String address = mSmsMessage.getOriginatingAddress();

            smsMessage = "SMS From: " + address + "\n" + smsBody + "\n";

            String[] smsSplit = smsBody.split(" ");
            if (smsSplit[1].equals(storedPassword)) {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                switch (smsSplit[0]) {
                    case "@normal":
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        break;
                    case "@vibrate":
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        break;
                    case "@silent":
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        break;
                }
            }
        }
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, "SMS : " + smsMessage + storedPassword, Toast.LENGTH_SHORT).show();
            Log.d("Message:", smsMessage + storedPassword);
        }
    }
}