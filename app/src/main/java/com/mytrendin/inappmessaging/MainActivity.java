package com.mytrendin.inappmessaging;


import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_PASSWORD = "password";
    private static final int RECEIVE_SMS_REQUEST_ID = 99;
    SmsBroadcastReceiver receiver = new SmsBroadcastReceiver();
    private SharedPreferences mSharedPreferences;
    private View mView;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvInstructions = findViewById(R.id.tvInstructions);
        tvInstructions.setMovementMethod(new ScrollingMovementMethod());

        final TextInputLayout passwordTextInputLayout = findViewById(R.id.set_password_text_input_layout);
        AppCompatButton submitButton = findViewById(R.id.submit_button);
        mView = passwordTextInputLayout;

        mSharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                RECEIVE_SMS_REQUEST_ID);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordTextInputLayout.getEditText().getText().toString();
                savePreferences(password);
                mSharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                Map<String, ?> keys = mSharedPreferences.getAll();
                if (BuildConfig.DEBUG) {
                    for (Map.Entry<String, ?> entry : keys.entrySet()) {
                        Snackbar.make(passwordTextInputLayout, entry.getKey() + " : " + entry.getValue().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    public void register() {
        this.registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }


    public void savePreferences(String password) {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(KEY_PASSWORD, password);
            editor.commit();
            Snackbar.make(mView, getString(R.string.password_saved), Toast.LENGTH_LONG).show();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {

                Snackbar.make(mView, "Permission to read SMS is required in order to successfully change the ringer profile" + password, Toast.LENGTH_LONG)
                        .setAction(R.string.grant, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, RECEIVE_SMS_REQUEST_ID);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, RECEIVE_SMS_REQUEST_ID);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECEIVE_SMS_REQUEST_ID: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.RECEIVE_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                        register();

                    }
                } else {
                    Snackbar.make(mView, getString(R.string.permission_denied),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void onHowToUseClick(View mView) {
        LinearLayout llHowToUse = findViewById(R.id.llHowToUse);
        LinearLayout llSetPassword = findViewById(R.id.llSetPassword);
        TextView tvHowToUse = findViewById(R.id.tvHowToUse);
        TextView tvSetPassword = findViewById(R.id.tvSetPassword);
        tvSetPassword.setTextColor(Color.BLACK);
        tvHowToUse.setTextColor(Color.parseColor("#ff669900"));
        llHowToUse.setVisibility(View.VISIBLE);
        llSetPassword.setVisibility(View.GONE);

    }

    public void onSetPasswordClick(View mView) {
        LinearLayout llHowToUse = findViewById(R.id.llHowToUse);
        LinearLayout llSetPassword = findViewById(R.id.llSetPassword);
        TextView tvHowToUse = findViewById(R.id.tvHowToUse);
        TextView tvSetPassword = findViewById(R.id.tvSetPassword);
        tvSetPassword.setTextColor(Color.parseColor("#ff669900"));
        tvHowToUse.setTextColor(Color.BLACK);
        llHowToUse.setVisibility(View.GONE);
        llSetPassword.setVisibility(View.VISIBLE);

    }
}