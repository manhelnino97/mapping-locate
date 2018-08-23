package com.example.manhe.search.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.provider.Settings.Secure;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.manhe.search.R;
import com.example.manhe.search.activity.CoffeeShopCreateActivity;
import com.example.manhe.search.activity.CoffeeShopDetailActivity;
import com.example.manhe.search.activity.LoadingActivity;
import com.example.manhe.search.activity.VendingMachineEditAcitvity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utils {

    private static int hour_open=0,hour_close=24,minutes_open=0,minutes_close=0;
    //Kiểm tra kết nối internet
    public static boolean isConnected(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    //Ẩn bàn phím
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
    }

    //Chuyển bitmap->file
    public static File persistImage(Bitmap bitmap, String name, Activity activity) {
        File filesDir = activity.getFilesDir();
        File imageFile = new File(filesDir, name + ".png");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e(activity.getClass().getSimpleName(), "Error writing bitmapAdapter", e);
        }
        return null;
    }

    public static void showAlertDialogCancelInputData(final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setMessage(activity.getResources().getString(R.string.message_cancel));
        builder.setPositiveButton(activity.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.onBackPressed();
            }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public static void setTime(final TextView tvTime, final TextView tvError, final Activity activity, final String lang){
        final AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater	= activity.getLayoutInflater();
        View convertView =  inflater.inflate(R.layout.layout_timepicker, null);
        dialog.setView(convertView);
        dialog.setTitle(activity.getResources().getString(R.string.text_select));
        final AlertDialog alertDialog = dialog.create();

        final TimePicker timePicker = convertView.findViewById(R.id.timePicker);

        TextView btn_cancel = convertView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        TextView btn_ok = convertView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int minutes = timePicker.getCurrentMinute();
                Log.e("gio",String.valueOf(hour)+String.valueOf(minutes));
                showTime(hour,minutes,tvTime,lang);
                alertDialog.dismiss();

                if(tvTime.getId()==R.id.tvtime_open){
                    hour_open = hour;
                    minutes_open = minutes;
                    TextView time_close = activity.findViewById(R.id.tvtime_close);
                    if(hour>hour_close || hour==hour_close && minutes>=minutes_close){
                        tvError.setVisibility(View.VISIBLE);
                        time_close.setTextColor(Color.parseColor("#fc0320"));
                        time_close.setBackground(activity.getResources().getDrawable(R.drawable.pb_txt_timeduplicate));
                    }
                    else{
                        tvError.setVisibility(View.GONE);
                        time_close.setTextColor(Color.parseColor("#757575"));
                        time_close.setBackground(activity.getResources().getDrawable(R.drawable.pb_txt_time));
                    }
                    Log.e("gio",String.valueOf(hour_close)+String.valueOf(minutes_close));
                }


                else if(tvTime.getId()==R.id.tvtime_close){
                    hour_close = hour;
                    minutes_close = minutes;
                    if(hour<hour_open || hour == hour_open && minutes<=minutes_open){
                        tvError.setVisibility(View.VISIBLE);
                        tvTime.setBackground(activity.getResources().getDrawable(R.drawable.pb_txt_timeduplicate));
                        tvTime.setTextColor(Color.parseColor("#fc0320"));
                    }
                    else{
                        tvError.setVisibility(View.GONE);
                        tvTime.setBackground(activity.getResources().getDrawable(R.drawable.pb_txt_time));
                        tvTime.setTextColor(Color.parseColor("#757575"));
                    }
                    Log.e("gio",String.valueOf(hour_open)+String.valueOf(minutes_open));
                }

            }
        });


        alertDialog.show();
    }

    private static void showTime(int hour, int min, TextView tv, String lang) {
        String str_min,str_hour;
        String format = "";
        if (hour == 0) {
            str_hour = String.valueOf(hour+12);
            format = "AM";
        }
        else if (hour == 12) {
            str_hour = String.valueOf(hour);
            format = "PM";
        } else if (hour > 12) {
            if(hour-12<10){
                str_hour = "0"+String.valueOf(hour-12);
                format = "PM";
            }
            else{
                str_hour = String.valueOf(hour-12);
                format = "PM";
            }

        } else if(hour>=10){
            str_hour = String.valueOf(hour);
            format = "AM";
        }
        else{
            str_hour = "0"+String.valueOf(hour);
            format = "AM";
        }

        if(min<10) {
            str_min= "0"+String.valueOf(min);
        }
        else {
            str_min = String.valueOf(min);
        }

        if(lang.equals("ja")){
            tv.setText(format+"  "+str_hour+":"+str_min);
        }
        else {
            tv.setText(str_hour+":"+str_min+"  "+format);
        }

    }

    public static void resetTime(){
        hour_open=0;hour_close=24;minutes_open=0;minutes_close=0;
    }

    public static void optionSelectImage(final Activity activity, View view){
        PopupMenu pm = new PopupMenu(activity, view);
        pm.getMenuInflater().inflate(R.menu.menu_action, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.camera: {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activity.startActivityForResult(intent, 1000);
                        return true;
                    }
                    case R.id.gallery:
                    {
                        Intent lib = new Intent(Intent.ACTION_PICK);
                        File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
                        String picS = pic.getPath();
                        Uri data = Uri.parse(picS);

                        lib.setDataAndType(data,"image/");
                        activity.startActivityForResult(lib,2000);
                        return true;
                    }
                    default: return false;
                }
            }
        });
        pm.show();

    }

    public static void showPrivacy(Activity activity){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.privacy_policy));
        builder.setMessage(activity.getResources().getString(R.string.privacy));
        builder.setCancelable(false);
        builder.setNegativeButton(activity.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void messageError(final Activity activity){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.connect_error));
        alertDialog.setNegativeButton(activity.getResources().getText(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton(activity.getResources().getText(R.string.tryagain), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.recreate();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    public static void messageExit(final Activity activity){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.connect_error));
        alertDialog.setNegativeButton(activity.getResources().getText(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
            }
        });
        alertDialog.setPositiveButton(activity.getResources().getText(R.string.tryagain), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.recreate();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    public static void messageDataNull(Activity activity){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.connect_error));
        alertDialog.setNegativeButton(activity.getResources().getText(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    public static void messageGeocoderError(final Activity activity){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage(activity.getResources().getString(R.string.geocoder));
        alertDialog.setNegativeButton(activity.getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    public static void messageInternetError(final Context activity){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage(activity.getResources().getString(R.string.message_internet));
        alertDialog.setNegativeButton(activity.getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    public static void messageImageError(final Activity activity){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage(activity.getResources().getString(R.string.upload));
        alertDialog.setNegativeButton(activity.getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    public static void showReportDialog(final Activity activity, final String lat,
                                        final String lng, final String idd, final String token){

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater	= activity.getLayoutInflater();
        View convertView =  inflater.inflate(R.layout.layout_report, null);
        dialog.setView(convertView);
        dialog.setCancelable(false);
        final AlertDialog alertDialog = dialog.create();

        TextView btn_cancel = convertView.findViewById(R.id.btn_cancel);
        final EditText edt_content = convertView.findViewById(R.id.rpcontent);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        @SuppressLint("HardwareIds") final String device_id = Secure.getString(activity.getContentResolver(),Secure.ANDROID_ID);
        TextView btn_ok = convertView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                RequestBody requestBody;
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM)
                        .addFormDataPart("latLocation",lat)
                        .addFormDataPart("long",lng)
                        .addFormDataPart("deviceCode",device_id)
                        .addFormDataPart("mailRevice","info.ominext.com")
                        .addFormDataPart("contents",edt_content.getText().toString());
                requestBody = builder.build();
                final Request request = new Request.Builder()
                        .url("http://192.168.1.67:8080/api/sendMailReport/"+idd+"?token="+token)
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                    }
                });
                alertDialog.dismiss();
            }

        });
        alertDialog.show();

    }

}
