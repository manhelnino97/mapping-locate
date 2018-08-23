package com.example.manhe.search.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.manhe.search.Interface.ItemClick;
import com.example.manhe.search.R;
import com.example.manhe.search.adapter.BitmapAdapter;
import com.example.manhe.search.adapter.OptionsAdapter;
import com.example.manhe.search.adapter.TagDelAdapter;
import com.example.manhe.search.adapter.TimeDelAdapter;
import com.example.manhe.search.model.Image;
import com.example.manhe.search.model.MarkerDetails;
import com.example.manhe.search.model.Options;
import com.example.manhe.search.model.Time;
import com.example.manhe.search.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostBoxCreateActivity extends AppCompatActivity {

    private AlertDialog.Builder dialog;
    TextView tvTitle,timeWeekdayDuplicate,timeWeekendDuplicate;
    RecyclerView recyclerViewWeekday, recyclerViewWeekend;
    ArrayList<String> listTimeWd, listTimeWk;
    ArrayList<Time> listTimeWeekday,listTimeWeekend;
    ImageButton btnLeft, btnRight, btnTag,getLocation, btnAddimg;
    ViewPager viewPager;
    TextView btnAddWk, btnAddWd,tvtype;
    TimeDelAdapter timeAdapterWd, timeAdapterWk;
    String add_location,lang,lat,lng,token;
    String note,deviceId;
    EditText edtadd,edtpostname,edtpostcode,edtbranch,edtnote;
    BitmapAdapter bitmapAdapter;
    ArrayList<Image> images = new ArrayList<>();
    ArrayList<Image> listImg = new ArrayList<>();
    ArrayList<File> listFile = new ArrayList<>();
    RelativeLayout btnCancel, btnCreate;
    String vendingMachine = null, postBox = null, coffeeShop = null, convenienceStore = null, superMarket = null;
    ArrayList<Integer> listTag = new ArrayList<>();
    ArrayList<Integer> listTimeWeekdayDuplicate = new ArrayList<>();
    ArrayList<Integer> listTimeWeekendDuplicate = new ArrayList<>();
    ProgressBar progressBar;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pb_create);

        ScrollView sc = findViewById(R.id.scroll);
        sc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftKeyboard(PostBoxCreateActivity.this);
                return false;
            }
        });

        TextView tvprivacy =findViewById(R.id.privacy);
        tvprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPrivacy(PostBoxCreateActivity.this);
            }
        });

        getLocation = findViewById(R.id.getLocation);
        tvtype = findViewById(R.id.tvtype);
        edtadd = findViewById(R.id.edtadd);
        edtpostname = findViewById(R.id.edtpostname);
        edtpostcode = findViewById(R.id.edtpostcode);
        edtbranch = findViewById(R.id.edtbranch);
        edtnote = findViewById(R.id.edtnote);
        timeWeekdayDuplicate = findViewById(R.id.wd_noti);
        timeWeekendDuplicate = findViewById(R.id.wk_noti);
        progressBar = findViewById(R.id.progressbar);

        Bundle bundle = getIntent().getExtras();
        add_location = bundle.getString("addLocation");
        lang = bundle.getString("lang");
        lat = bundle.getString("latLocation");
        lng = bundle.getString("lngLocation");
        token = bundle.getString("token");
        deviceId = bundle.getString("deviceId");

        edtadd.setText(add_location);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtadd.setText(add_location);
            }
        });

        setTime();
        setActionBar();
        setImagePager();
        setAddTagDialog();

        btnAddimg = findViewById(R.id.btn_img);
        btnAddimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.optionSelectImage(PostBoxCreateActivity.this,view);
            }
        });

        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtadd.getText().toString().equals(add_location) && edtpostname.getText().toString().equals("") &&
                        edtpostcode.getText().toString().equals("") && edtbranch.getText().toString().equals("") &&
                        edtnote.getText().toString().equals("") && listTimeWd.size()==0 && listTimeWk.size()==0) {

                    onBackPressed();

                }
                else {
                   Utils.showAlertDialogCancelInputData(PostBoxCreateActivity.this);
                }
            }
        });

        btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#fb3680"), android.graphics.PorterDuff.Mode.MULTIPLY);
                if(Utils.isConnected(PostBoxCreateActivity.this)){
                    if(!edtadd.getText().toString().equals("")){
                        if(listTimeWeekdayDuplicate.size()==0 && listTimeWeekendDuplicate.size()==0){
                            new PostData(listFile, tvtype.getText().toString(), edtadd.getText().toString(), edtnote.getText().toString(),
                                    lat, lng, lang, edtbranch.getText().toString(), edtpostname.getText().toString(), edtpostcode.getText().toString(),
                                    listTimeWd, listTimeWk, listTag, deviceId, new PostData.GetData() {
                                @Override
                                public void getData(ArrayList<MarkerDetails> data) {
                                    if(data.size()==1){
                                        Bundle bundle = new Bundle();
                                        bundle.putString("token", token);
                                        bundle.putString("latLocation",data.get(0).getLat());
                                        bundle.putString("lngLocation",data.get(0).getLng());
                                        bundle.putString("idd",data.get(0).getId_d());
                                        bundle.putString("cate","2");
                                        Intent it = new Intent(PostBoxCreateActivity.this, MainActivity.class);
                                        it.putExtras(bundle);
                                        startActivity(it);
                                    }
                                }
                            }).execute("http://192.168.1.67:8080/api/createNewPostbox?token="+token);

                        }
                    }
                    else{
                        final AlertDialog alertDialog = new AlertDialog.Builder(PostBoxCreateActivity.this).create();
                        alertDialog.setMessage(getResources().getString(R.string.message_address));
                        alertDialog.show();
                        Thread bamgio=new Thread(){
                            public void run()
                            {
                                try {
                                    sleep(3000);
                                } catch (Exception e) {

                                }
                                finally
                                {
                                    alertDialog.dismiss();
                                }
                            }
                        };
                        bamgio.start();
                    }
                }
                else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(PostBoxCreateActivity.this).create();
                    alertDialog.setMessage(getResources().getString(R.string.message_internet));
                    alertDialog.show();
                    Thread bamgio=new Thread(){
                        public void run()
                        {
                            try {
                                sleep(3000);
                            } catch (Exception e) {

                            }
                            finally
                            {
                                alertDialog.dismiss();
                            }
                        }
                    };
                    bamgio.start();
                }
            }
        });



    }

    public void setDatatag(){
        vendingMachine = getResources().getText(R.string.vd).toString();
        postBox = getResources().getText(R.string.pb).toString();
        coffeeShop = getResources().getText(R.string.cf).toString();
        convenienceStore = getResources().getText(R.string.cs).toString();
        superMarket = getResources().getText(R.string.sm).toString();

    }

    public void setAddTagDialog(){
        setDatatag();
        final ArrayList<Options> list_op = new ArrayList<>();
        list_op.add(new Options(R.drawable.vending, vendingMachine,false));
        list_op.add(new Options(R.drawable.post, postBox,false));
        list_op.add(new Options(R.drawable.coffee, coffeeShop,false));
        list_op.add(new Options(R.drawable.store, convenienceStore,false));
        list_op.add(new Options(R.drawable.spmk, superMarket,false));

        final OptionsAdapter optionsAdapter = new OptionsAdapter(this,list_op);

        final RecyclerView addtag = findViewById(R.id.tag);
        final TagDelAdapter tagAdapter = new TagDelAdapter(this, listTag);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PostBoxCreateActivity.this, LinearLayoutManager.HORIZONTAL, false);
        addtag.setLayoutManager(horizontalLayoutManager);
        addtag.setAdapter(tagAdapter);

        btnTag = findViewById(R.id.btn_add_tag);
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(PostBoxCreateActivity.this);
                LayoutInflater inflater	= getLayoutInflater();
                View convertView =  inflater.inflate(R.layout.dialog_service_custom, null);
                dialog.setView(convertView);
                dialog.setTitle(getResources().getString(R.string.text_select));
                final AlertDialog alertDialog = dialog.create();
                alertDialog.show();

                TextView btn_cancel = convertView.findViewById(R.id.btn_cancel);
                btn_cancel.setVisibility(View.GONE);
                TextView btn_ok = convertView.findViewById(R.id.btn_ok);
                btn_ok.setVisibility(View.GONE);
                ListView lv = convertView.findViewById(R.id.list_item);
                lv.setAdapter(optionsAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int count=0;
                        if(listTag.size()>0){
                            for(int i = 0; i< listTag.size(); i++){
                                if(listTag.get(i)==position+1){
                                    count++;
                                }
                            }
                            if(count==0) {
                                listTag.add(position+1);
                                tagAdapter.notifyDataSetChanged();
                            }
                        }
                        else{
                            listTag.add(position+1);
                            tagAdapter.notifyDataSetChanged();
                        }

                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    public void setTime(){
        recyclerViewWeekday = findViewById(R.id.weekday);
        recyclerViewWeekend = findViewById(R.id.weekend);
        listTimeWd = new ArrayList<>();

        timeAdapterWd = new TimeDelAdapter(this, listTimeWd, listTimeWeekdayDuplicate);
        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewWeekday.setAdapter(timeAdapterWd);
        recyclerViewWeekday.setLayoutManager(layoutManager1);


        listTimeWk = new ArrayList<>();

        timeAdapterWk = new TimeDelAdapter(this, listTimeWk, listTimeWeekendDuplicate);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewWeekend.setAdapter(timeAdapterWk);
        recyclerViewWeekend.setLayoutManager(layoutManager);

        btnAddWd = findViewById(R.id.btn_add_wd);
        btnAddWd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(PostBoxCreateActivity.this);
                LayoutInflater inflater	= getLayoutInflater();
                final View convertView =  inflater.inflate(R.layout.layout_timepicker, null);
                dialog.setView(convertView);
                dialog.setTitle(getResources().getString(R.string.text_select));
                final AlertDialog alertDialog = dialog.create();
                alertDialog.show();

                TextView btn_cancel = convertView.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                TextView btn_ok = convertView.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePicker timePicker = convertView.findViewById(R.id.timePicker);
                        int hour = timePicker.getCurrentHour();
                        int minutes = timePicker.getCurrentMinute();
                        String str_min;
                        if(minutes<10){
                            str_min = "0"+String.valueOf(minutes);
                        }
                        else{
                            str_min = String.valueOf(minutes);
                        }
                        String str_hour;
                        if(hour<10){
                            str_hour = "0"+String.valueOf(hour);
                        }
                        else{
                            str_hour = String.valueOf(hour);
                        }
                        String time = str_hour+":"+str_min;
                        ArrayList<Integer> checkTime = new ArrayList<>();
                        if(listTimeWd.size()>0){
                            for(int i=0;i<listTimeWd.size();i++){
                                if(time.equals(listTimeWd.get(i))){
                                    checkTime.add(i);
                                }
                            }
                            listTimeWd.add(time);
                            if(checkTime.size()==1) {
                                listTimeWeekdayDuplicate.add(listTimeWd.size()-1);
                                listTimeWeekdayDuplicate.add(checkTime.get(0));
                            }
                            else if(checkTime.size()>1){
                                listTimeWeekdayDuplicate.add(listTimeWd.size()-1);
                            }
                        }
                        else {
                            listTimeWd.add(time);
                        }
                        timeAdapterWd.notifyDataSetChanged();
                        alertDialog.dismiss();
                        if(listTimeWeekdayDuplicate.size()>0){
                            timeWeekdayDuplicate.setVisibility(View.VISIBLE);
                        }
                    }
                });
                timeAdapterWd.setClickListener(new ItemClick() {
                    @Override
                    public void Click(View view, int position) {
                        String timeDuplicate = listTimeWd.get(position);
                        ArrayList<Integer> checkTime = new ArrayList<>();

                        for(int i=0;i<listTimeWd.size();i++){
                            if(timeDuplicate.equals(listTimeWd.get(i))){
                                checkTime.add(i);
                            }
                        }
                        if(checkTime.size()==2){
                            listTimeWeekdayDuplicate.clear();
                        }
                        else if(checkTime.size()>2){
                            for(int i = 0; i< listTimeWeekdayDuplicate.size(); i++){
                                if(listTimeWeekdayDuplicate.get(i)==position){
                                    listTimeWeekdayDuplicate.remove(i);
                                }
                            }
                            int[] arrTime = new int[listTimeWeekdayDuplicate.size()];
                            for(int i = 0; i< listTimeWeekdayDuplicate.size(); i++){
                                arrTime[i] = listTimeWeekdayDuplicate.get(i);
                            }
                            listTimeWeekdayDuplicate.clear();
                            for(int i=0;i<arrTime.length;i++){
                                if(arrTime[i]>position){
                                    arrTime[i] = arrTime[i]-1;
                                }
                            }
                            for (int anArrTime : arrTime) {
                                listTimeWeekdayDuplicate.add(anArrTime);
                            }
                        }
                        else {
                            checkTime.clear();
                        }
                        listTimeWd.remove(position);
                        timeAdapterWd.notifyDataSetChanged();
                        if(listTimeWeekdayDuplicate.size()==0){
                            timeWeekdayDuplicate.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        btnAddWk = findViewById(R.id.btn_add_wk);
        btnAddWk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(PostBoxCreateActivity.this);
                LayoutInflater inflater	= getLayoutInflater();
                final View convertView =  inflater.inflate(R.layout.layout_timepicker, null);
                dialog.setView(convertView);
                dialog.setTitle(getResources().getString(R.string.text_select));
                final AlertDialog alertDialog = dialog.create();
                alertDialog.show();

                TextView btn_cancel = convertView.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                TextView btn_ok = convertView.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePicker timePicker = convertView.findViewById(R.id.timePicker);
                        int hour = timePicker.getCurrentHour();
                        int minutes = timePicker.getCurrentMinute();
                        String str_min;
                        if(minutes<10){
                            str_min = "0"+String.valueOf(minutes);
                        }
                        else{
                            str_min = String.valueOf(minutes);
                        }
                        String str_hour;
                        if(hour<10){
                            str_hour = "0"+String.valueOf(hour);
                        }
                        else{
                            str_hour = String.valueOf(hour);
                        }
                        String time = str_hour+":"+str_min;
                        ArrayList<Integer> checkTime = new ArrayList<>();
                        if(listTimeWk.size()>0){
                            for(int i=0;i<listTimeWk.size();i++){
                                if(time.equals(listTimeWk.get(i))){
                                    checkTime.add(i);
                                }
                            }
                            listTimeWk.add(time);
                            if(checkTime.size()==1) {
                                listTimeWeekendDuplicate.add(listTimeWk.size()-1);
                                listTimeWeekendDuplicate.add(checkTime.get(0));
                            }
                            else if(checkTime.size()>1){
                                listTimeWeekendDuplicate.add(listTimeWk.size()-1);
                            }
                        }
                        else {
                            listTimeWk.add(time);
                        }
                        timeAdapterWk.notifyDataSetChanged();
                        alertDialog.dismiss();
                        if(listTimeWeekendDuplicate.size()>0){
                            timeWeekendDuplicate.setVisibility(View.VISIBLE);
                        }

                    }
                });
                timeAdapterWk.setClickListener(new ItemClick() {
                    @Override
                    public void Click(View view, int position) {
                        String timeDuplicate = listTimeWk.get(position);
                        ArrayList<Integer> checkTime = new ArrayList<>();

                        for(int i=0;i<listTimeWk.size();i++){
                            if(timeDuplicate.equals(listTimeWk.get(i))){
                                checkTime.add(i);
                            }
                        }
                        if(checkTime.size()==2){
                            listTimeWeekendDuplicate.clear();
                        }
                        else if(checkTime.size()>2){
                            for(int i = 0; i< listTimeWeekendDuplicate.size(); i++){
                                if(listTimeWeekendDuplicate.get(i)==position){
                                    listTimeWeekendDuplicate.remove(i);
                                }
                            }
                            int[] arrTime = new int[listTimeWeekendDuplicate.size()];
                            for(int i = 0; i< listTimeWeekendDuplicate.size(); i++){
                                arrTime[i] = listTimeWeekendDuplicate.get(i);
                            }
                            listTimeWeekendDuplicate.clear();
                            for(int i=0;i<arrTime.length;i++){
                                if(arrTime[i]>position){
                                    arrTime[i] = arrTime[i]-1;
                                }
                            }
                            for (int anArrTime : arrTime) {
                                listTimeWeekendDuplicate.add(anArrTime);
                            }
                        }
                        else {
                            checkTime.clear();
                        }
                        listTimeWk.remove(position);
                        timeAdapterWk.notifyDataSetChanged();
                        if(listTimeWeekendDuplicate.size()==0){
                            timeWeekendDuplicate.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar_home, null),
                new ActionBar.LayoutParams(
                        Gravity.CENTER
                )
        );
        tvTitle = findViewById(R.id.title);
        tvTitle.setText(getResources().getText(R.string.text_create));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fb3680")));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void setImagePager(){
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_image);
        listImg.add(new Image("","","",bitmap,true));
        viewPager = findViewById(R.id.viewPager);
        bitmapAdapter = new BitmapAdapter(this, listImg);
        viewPager.setAdapter(bitmapAdapter);
        btnLeft = findViewById(R.id.btnleft);
        btnRight = findViewById(R.id.btnright);


        if(viewPager.getCurrentItem()< bitmapAdapter.getCount()-1){
            btnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }
            });
        }
        if(viewPager.getCurrentItem()>0){
            btnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                }
            });
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(viewPager.getCurrentItem()< bitmapAdapter.getCount()-1){
                    btnRight.setImageResource(R.drawable.ic_right);
                    btnRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }
                else{
                    btnRight.setImageDrawable(null);
                }
                if(viewPager.getCurrentItem()>0){
                    btnLeft.setImageResource(R.drawable.ic_left);
                    btnLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                        }
                    });
                }
                else{
                    btnLeft.setImageDrawable(null);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK ) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                if(Objects.requireNonNull(Utils.persistImage(imageBitmap, "img" + System.currentTimeMillis(), this)).length()>10485760){
                    Utils.messageImageError(this);
                }
                else{
                    images.add(new Image("","","",imageBitmap,true));
                    listFile.add(Utils.persistImage(imageBitmap,"img"+System.currentTimeMillis(),this));
                    bitmapAdapter = new BitmapAdapter(this,images);
                    viewPager.setAdapter(bitmapAdapter);
                }

            }
        }
        else if (requestCode == 2000 && resultCode == RESULT_OK && null != data) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                if(Objects.requireNonNull(Utils.persistImage(selectedImage, "img" + System.currentTimeMillis(), this)).length()>10485760){
                    Utils.messageImageError(this);
                }
                else{
                    images.add(new Image("","","",selectedImage,true));
                    listFile.add(Utils.persistImage(selectedImage,"img"+System.currentTimeMillis(),this));
                    bitmapAdapter = new BitmapAdapter(this,images);
                    viewPager.setAdapter(bitmapAdapter);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

    }

    static class PostData extends AsyncTask<String,Void,String> {

        public interface GetData {
            void  getData(ArrayList<MarkerDetails> data);
        }
        public GetData getdata;
        private ArrayList<MarkerDetails> list = new ArrayList<>();

        ArrayList<File> list_file;
        String type;String add;String memo;String lat;String lng,lang,branch,postname,postcode,deviceId;
        ArrayList<String> list_time_wd,list_time_wk;
        ArrayList<Integer> listTag;

        public PostData(ArrayList<File> list_file, String type, String add, String memo, String lat, String lng, String lang,
                        String branch, String postname, String postcode, ArrayList<String> list_time_wd,  ArrayList<String> list_time_wk,
                        ArrayList<Integer> listTag,String deviceId,GetData getData) {
            this.list_file = list_file;
            this.type = type;
            this.add = add;
            this.memo = memo;
            this.lat = lat;
            this.lng = lng;
            this.lang = lang;
            this.branch = branch;
            this.postcode = postcode;
            this.postname = postname;
            this.list_time_wd = list_time_wd;
            this.list_time_wk = list_time_wk;
            this.listTag = listTag;
            this.deviceId = deviceId;
            this.getdata = getData;
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = null;
            JSONArray jsonArray_timewd = new JSONArray(list_time_wd);
            String time_wd = jsonArray_timewd.toString();

            JSONArray jsonArray_timewk = new JSONArray(list_time_wk);
            String time_wk = jsonArray_timewk.toString();

            String strTag = "";
            for(int i=0;i<listTag.size();i++){
                if(i==listTag.size()-1){
                    strTag = strTag + String.valueOf(listTag.get(i)+1);
                }
                else{
                    strTag = strTag + String.valueOf(listTag.get(i)+1) +".";
                }
            }
            Log.e("abcd",strTag);

            if(list_file.size()>0){
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM)
                        .addFormDataPart("name",type)
                        .addFormDataPart("address",add)
                        .addFormDataPart("memo",memo)
                        .addFormDataPart("lat",lat)
                        .addFormDataPart("long",lng)
                        .addFormDataPart("category_id","2")
                        .addFormDataPart("lang",lang)
                        .addFormDataPart("collection_branch",branch)
                        .addFormDataPart("name",postname)
                        .addFormDataPart("code",postcode)
                        .addFormDataPart("tags",strTag)
                        .addFormDataPart("created_by",deviceId)
                        .addFormDataPart("workingtime_weekday",time_wd)
                        .addFormDataPart("workingtime_saturday",time_wk);



                for(int i=0;i<list_file.size();i++){
                    builder.addFormDataPart("imagesAdd[]","VendingMachine_"+String.valueOf(i)+System.currentTimeMillis()+".png",RequestBody.create(MediaType.parse("image/png"),list_file.get(i)));
                }
                requestBody = builder.build();
            }
            else{
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("name",type)
                        .addFormDataPart("address",add)
                        .addFormDataPart("memo",memo)
                        .addFormDataPart("lat",lat)
                        .addFormDataPart("long",lng)
                        .addFormDataPart("category_id","2")
                        .addFormDataPart("lang",lang)
                        .addFormDataPart("collection_branch",branch)
                        .addFormDataPart("name",postname)
                        .addFormDataPart("code",postcode)
                        .addFormDataPart("tags",strTag)
                        .addFormDataPart("created_by",deviceId)
                        .addFormDataPart("workingtime_weekday",time_wd)
                        .addFormDataPart("workingtime_saturday",time_wk)
                        .build();
            }

            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("response",s);
            if(s!=null){
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.getString("code").equals("S200")){
                        JSONObject markDetails = new JSONObject(jsonObject.getString("body"));
                        list.add(new MarkerDetails(markDetails.getString("type"),markDetails.getString("lat"),markDetails.getString("long"),
                                markDetails.getString("name"),markDetails.getString("name_vi"),markDetails.getString("name_en"),markDetails.getString("name_es"),markDetails.getString("name_cn"),markDetails.getString("name_ko"),
                                markDetails.getString("address"),markDetails.getString("address_vi"),markDetails.getString("address_en"),markDetails.getString("address_es"),markDetails.getString("address_cn"),markDetails.getString("address_ko"),
                                markDetails.getString("id")));
                        getdata.getData(list);
                    }
                    else if(jsonObject.getString("code").equals("E404")){
                        getdata.getData(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else getdata.getData(null);
        }
    }
}
