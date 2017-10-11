package com.mytrendin.inappmessaging;


import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 99;
    private SharedPreferences mSharedPreferences;
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;

    SmsBroadcastReceiver receiver = new SmsBroadcastReceiver();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextInputLayout passwordTextInputLayout = findViewById(R.id.set_password_text_input_layout);
        AppCompatButton submitButton = findViewById(R.id.submit_button);

        mSharedPreferences = getApplicationContext().getSharedPreferences("Pritom", Context.MODE_PRIVATE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordTextInputLayout.getEditText().getText().toString();
                savePreferences(password);
                mSharedPreferences = getApplicationContext().getSharedPreferences("Pritom", Context.MODE_PRIVATE);
                Map<String, ?> keys = mSharedPreferences.getAll();
                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                    Toast.makeText(getApplicationContext(), entry.getKey() + ":" + entry.getValue().toString(), Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    public void register() {
        this.registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }


    public void savePreferences(String password) {
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("password", password);
            editor.commit();
            Toast.makeText(getApplicationContext(), "Password saved successfully" + password, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.RECEIVE_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                        register();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "permission denied",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}