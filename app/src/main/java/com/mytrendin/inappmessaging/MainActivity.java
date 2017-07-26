package com.mytrendin.inappmessaging;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends Activity {

    private static MainActivity inst;
    Boolean permission = false;
    public static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 99;
    public EditText setPass;
    public Button submit;
    public String password;
    SharedPreferences sharedPreferences;
    boolean b = false;
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;

    IntentFilter filter;

    SmsBroadcastReceiver receiver=new SmsBroadcastReceiver();

    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

            setPass = (EditText) findViewById(R.id.setPassEditId);
            submit = (Button) findViewById(R.id.submitButtonId);
            sharedPreferences = getApplicationContext().getSharedPreferences("Pritom", Context.MODE_PRIVATE);
                ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password = setPass.getText().toString();
                    // if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    //   permission = checkSmsPermission();
                    //if (permission) {
                    savePreferences(password);

                    //}
                    sharedPreferences = getApplicationContext().getSharedPreferences("Pritom", Context.MODE_PRIVATE);
                    Map<String, ?> keys = sharedPreferences.getAll();
                    for (Map.Entry<String, ?> entry : keys.entrySet()) {
                        Toast.makeText(getApplicationContext(), entry.getKey() + ":" + entry.getValue().toString(), Toast.LENGTH_LONG).show();
                    }


                }
            });



    }

    public void register(){
        this.registerReceiver(receiver,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }


    public void savePreferences(String pass) {
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("password",pass);
        editor.commit();
        Toast.makeText(getApplicationContext(), "Password saved successfully" + password, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
        }
    }

    /*public boolean checkSmsPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
            return false;
        } else {
            return true;
        }

    }*/
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
                        //refreshSmsInbox("");
                        register();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }







}