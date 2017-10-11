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
    public AudioManager audioManager;
    SharedPreferences sharedPreferences;
    boolean b = false;
    String smsBody;
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static final String SMS_BUNDLE = "pdus";

    SmsMessage smsMessage;

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(), "Toast Long", Toast.LENGTH_LONG).show();


        sharedPreferences = context.getApplicationContext().getSharedPreferences("Pritom", context.MODE_PRIVATE);
        Bundle intentExtras = intent.getExtras();
        String format = intentExtras.getString("format");
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            String pass3 = sharedPreferences.getString("password", null);
            for (int i = 0; i < sms.length; ++i) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);
                } else {
                    smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                }
                smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }
            if (smsBody.equals("@general" + pass3)) {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
            }
            Toast.makeText(context, "SMS : " + smsBody + pass3, Toast.LENGTH_SHORT).show();
            Log.d("Message:", smsBody + pass3);


        }
    }
}