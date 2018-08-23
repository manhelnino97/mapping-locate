package com.example.manhe.search.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.manhe.search.R;
import com.example.manhe.search.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.Toast.*;

public  class LoadingActivity extends AppCompatActivity {
    boolean GpsStatus ;
    String deviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        deviceId = Secure.getString(getContentResolver(),Secure.ANDROID_ID);
        GPSStatus();
            if (Utils.isConnected(LoadingActivity.this) && GpsStatus)
            {
                CountDown();
            }
            else{
                showAlertDialogPermission();
            }

    }
    public void CountDown(){
        new PostLogin("thang@gmail.com","111111", deviceId, getDeviceName(),new PostLogin.Get_Data() {
            @Override
            public void getData(final String data) {
                if(data!=null){
                    Log.e("cmm", data);
                    String token = "";
                    try {
                        JSONObject j = new JSONObject(data);
                        token = j.getString("body");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String finalToken = token;
                    Thread bamgio=new Thread(){
                        public void run()
                        {
                            try {
                                sleep(2000);
                            } catch (Exception e) {

                            }
                            finally
                            {
                                Intent ac =new Intent(LoadingActivity.this,MainActivity.class);
                                Bundle b = new Bundle();
                                b.putString("token", finalToken);
                                ac.putExtras(b);
                                startActivity(ac);
                            }
                        }
                    };
                    bamgio.start();
                }
                else{
                    Utils.messageExit(LoadingActivity.this);
                }

            }
        }).execute("http://192.168.1.67:8080/api/auth/login");

    }

    public void GPSStatus(){
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void showAlertDialogPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.request));
        builder.setMessage(getResources().getString(R.string.question));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Utils.isConnected(LoadingActivity.this)&&GpsStatus)
                {
                    CountDown();
                }
                else{
                    Thread bamgio=new Thread(){
                        public void run()
                        {
                            try {
                                sleep(2000);
                            } catch (Exception e) {

                            }
                            finally
                            {
                                Intent ac =new Intent(LoadingActivity.this,MainActivity.class);
                                Bundle b = new Bundle();
                                b.putString("token","");
                                ac.putExtras(b);
                                startActivity(ac);
                            }
                        }
                    };
                    bamgio.start();
                }
            }
        });
        builder.setNegativeButton(getResources().getText(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startActivity(startMain);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    static class PostLogin extends AsyncTask<String,Void,String> {
        interface Get_Data{
            void  getData(String data);
        }
        public Get_Data getdata;
        String resonse;
        String deviceName;
        PostLogin(String email, String pass, String deice_id,String deviceName, Get_Data getdata) {
            this.getdata = getdata;
            this.email = email;
            this.pass = pass;
            this.device_id = deice_id;
            this.deviceName = deviceName;
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        String email,pass,device_id;
        @Override
        protected String doInBackground(String... strings) {

            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("email",email)
                    .addFormDataPart("password",pass)
                    .addFormDataPart("device_code",device_id)
                    .addFormDataPart("username", deviceName)
                    .setType(MultipartBody.FORM)
                    .build();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();
            try {
                okHttpClient.retryOnConnectionFailure();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resonse;
        }

        @Override
        protected void onPostExecute(String s) {
            getdata.getData(s);
        }
    }

}


