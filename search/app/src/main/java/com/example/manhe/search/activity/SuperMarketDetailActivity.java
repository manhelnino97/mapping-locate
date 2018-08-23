package com.example.manhe.search.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.manhe.search.Interface.ItemClick;
import com.example.manhe.search.R;
import com.example.manhe.search.adapter.BitmapAdapter;
import com.example.manhe.search.adapter.ListServiceAdapter;
import com.example.manhe.search.adapter.ServiceAdapter;
import com.example.manhe.search.adapter.TagAdapter;
import com.example.manhe.search.model.Image;
import com.example.manhe.search.model.Service;
import com.example.manhe.search.model.Supermarket;
import com.example.manhe.search.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SuperMarketDetailActivity extends AppCompatActivity {

    ImageButton btnLeft, btnRight;
    ViewPager viewPager;
    TextView tvTitle,tvtype,tvadd,tvname,tvphone,tvroadguide,tvnote,tvtimeopen,tvtimeclose,style;
    String lang,add_location,lat,lng,token,idd,name,add,phone,note,timeopen,timeclose,roadguide, latLocation, lngLocation,smstyle;
    RecyclerView listservice;
    ArrayList<Service> listService = new ArrayList<>();
    ListServiceAdapter listServiceAdapter;
    ArrayList<Integer> listIdService;
    ServiceAdapter serviceAdapter;
    RelativeLayout btnEdit, btnReport;
    ArrayList<Image> images = new ArrayList<>();
    ArrayList<Integer> listTag = new ArrayList<>();
    BitmapAdapter bitmapAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sm_details);

        tvtype = findViewById(R.id.tvtype);
        tvadd = findViewById(R.id.tvadd);
        tvname = findViewById(R.id.tvname);
        tvnote = findViewById(R.id.tvnote);
        tvphone = findViewById(R.id.tvphone);
        tvroadguide = findViewById(R.id.tvroadguide);
        tvtimeopen = findViewById(R.id.tvtime_open);
        tvtimeclose = findViewById(R.id.tvtime_close);
        style = findViewById(R.id.style);

        tvroadguide = findViewById(R.id.tvroadguide);
        listservice = findViewById(R.id.listservice);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#015d46"), android.graphics.PorterDuff.Mode.MULTIPLY);

        TextView tvprivacy =findViewById(R.id.privacy);
        tvprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPrivacy(SuperMarketDetailActivity.this);
            }
        });

        Bundle bundle = getIntent().getExtras();
        add_location = bundle.getString("addLocation");
        lang = bundle.getString("lang");
        latLocation = bundle.getString("latLocation");
        lngLocation = bundle.getString("lngLocation");
        token = bundle.getString("token");
        idd = bundle.getString("idd");


        setActionBar();
        createServiceAndPayment();
        setListserviceAndPayment();


        new GetData(new GetData.Get_City() {
            @Override
            public void getCity(ArrayList<Supermarket> data) {
                if(data.size()>0){
                    progressBar.setVisibility(View.GONE);
                    lat = data.get(0).getLat();
                    lng = data.get(0).getLng();
                    phone = data.get(0).getPhonnumber();
                    roadguide = data.get(0).getRoadguide();
                    timeopen = data.get(0).getTime_open();
                    timeclose = data.get(0).getTime_close();
                    smstyle = data.get(0).getStyle();

                    switch (lang) {
                        case "vi":
                            name = data.get(0).getName_vi();
                            add = data.get(0).getAdd_vi();
                            note = data.get(0).getMemo_vi();
                            break;
                        case "ja":
                            name = data.get(0).getName();
                            add = data.get(0).getAdd();
                            note = data.get(0).getMemo();
                            break;
                        case "en":
                            name = data.get(0).getName_en();
                            add = data.get(0).getAdd_en();
                            note = data.get(0).getMemo_en();
                            break;
                        case "es":
                            name = data.get(0).getName_es();
                            add = data.get(0).getAdd_es();
                            note = data.get(0).getMemo_es();
                            break;
                        case "zh":
                            name = data.get(0).getName_cn();
                            add = data.get(0).getAdd_cn();
                            note = data.get(0).getMemo_cn();
                            break;
                        case "ko":
                            name = data.get(0).getName_ko();
                            add = data.get(0).getAdd_ko();
                            note = data.get(0).getMemo_ko();
                            break;
                    }

                    if (!name.equals("null")) tvname.setText(name);
                    if (!add.equals("null")) tvadd.setText(add);
                    if (!phone.equals("null")) tvphone.setText(phone);
                    if (!note.equals("null")) tvnote.setText(note);
                    if (!roadguide.equals("null")) tvroadguide.setText(roadguide);
                    if(!timeopen.equals("null") && !timeclose.equals("")){
                        tvtimeopen.setText(timeopen);
                        tvtimeclose.setText(timeclose);
                    }
                    if(!smstyle.equals("null")){
                        switch (smstyle) {
                            case "1":
                                style.setText(getResources().getText(R.string.coop));
                                break;
                            case "2":
                                style.setText(getResources().getText(R.string.discount_shop));
                                break;
                            case "3":
                                style.setText(getResources().getText(R.string.super_cheap_market));
                                break;
                            case "4":
                                style.setText(getResources().getText(R.string.general_merchandise_store));
                                break;
                            case "5":
                                style.setText(getResources().getText(R.string.high_end_supermarket));
                                break;
                            case "6":
                                style.setText(getResources().getText(R.string.minimarket));
                                break;
                            case "7":
                                style.setText(getResources().getText(R.string.grocery_store));
                                break;
                        }
                    }

                    images = data.get(0).getImageArrayList();
                    setImagePager();

                    listIdService = data.get(0).getListIdService();
                    ArrayList<Service> serviceArrayList = new ArrayList<>();
                    for(int i=0;i<data.get(0).getListIdService().size();i++){
                        for(int j = 0; j< listService.size(); j++){
                            if(data.get(0).getListIdService().get(i)== listService.get(j).getId()){
                                serviceArrayList.add(listService.get(j));
                                serviceArrayList.get(i).setIschecked(true);
                            }
                        }
                    }
                    listServiceAdapter = new ListServiceAdapter(SuperMarketDetailActivity.this, serviceArrayList);
                    LinearLayoutManager horizontalLayoutManager_1 = new LinearLayoutManager(SuperMarketDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    listservice.setLayoutManager(horizontalLayoutManager_1);
                    listservice.setAdapter(listServiceAdapter);

                    listTag = data.get(0).getListTag();
                    setTagLayout();
                }
                else {
                    Utils.messageError(SuperMarketDetailActivity.this);
                }

                setOnClickButton();

            }
        }).execute("http://192.168.1.67:8080/api/getDetailSupermarket/"+idd+"?token="+token);




    }

    public void setOnClickButton(){
        btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("token", token);
                b.putString("lang", lang);
                b.putString("lat", lat);
                b.putString("lng", lng);
                b.putString("latLocation", latLocation);
                b.putString("lngLocation", lngLocation);
                b.putString("idd", idd);
                b.putString("addLocation", add_location);
                b.putString("addLocation", add);
                b.putString("name", name);
                b.putString("phone", phone);
                b.putString("note", note);
                b.putString("guide", roadguide);
                b.putString("timeopen", timeopen);
                b.putString("timeclose", timeclose);
                b.putIntegerArrayList("listidservice", listIdService);
                b.putParcelableArrayList("bitmaps", images);
                b.putIntegerArrayList("listtag", listTag);
                b.putString("style",smstyle);
                Intent intent = new Intent(SuperMarketDetailActivity.this, SuperMarketEditActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btnReport = findViewById(R.id.btn_report);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showReportDialog(SuperMarketDetailActivity.this,lat,lng,idd,token);
            }
        });
    }

    public void createServiceAndPayment(){
        listService.add(new Service(1,R.drawable.wifi,"WIFI",false));
        listService.add(new Service(2,R.drawable.atm,"ATM",false));
        listService.add(new Service(3,R.drawable.wifi,"WIFI",false));
        listService.add(new Service(4,R.drawable.atm,"ATM",false));
        listService.add(new Service(5,R.drawable.wifi,"WIFI",false));
        listService.add(new Service(6,R.drawable.atm,"ATM",false));

    }

    public void setListserviceAndPayment(){


        serviceAdapter = new ServiceAdapter(this, listService);

        ArrayList<Service> addService = new ArrayList<>();
        for(int i = 0; i< listService.size(); i++){
            if(listService.get(i).getIschecked()){
                addService.add(listService.get(i));
            }
        }
        listServiceAdapter = new ListServiceAdapter(this, addService);
        LinearLayoutManager horizontalLayoutManager_1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listservice.setLayoutManager(horizontalLayoutManager_1);
        listservice.setAdapter(listServiceAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
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
        tvTitle.setText(getResources().getText(R.string.text_detail));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#015d46")));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void setImagePager(){
        viewPager = findViewById(R.id.viewPager);
        bitmapAdapter = new BitmapAdapter(this,images);
        viewPager.setAdapter(bitmapAdapter);
        btnLeft = findViewById(R.id.btnleft);
        btnRight = findViewById(R.id.btnright);


        if(viewPager.getCurrentItem()<bitmapAdapter.getCount()-1){
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
                if(viewPager.getCurrentItem()<bitmapAdapter.getCount()-1){
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

    static class GetData extends AsyncTask<String,Void,String> {
        interface Get_City{
            void  getCity(ArrayList<Supermarket> data);
        }
        Get_City getcity = null;
        GetData(Get_City getcity) {
            this.getcity = getcity;
        }
        ArrayList<Supermarket> list_sm = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        @Override
        protected String doInBackground(String... strings) {


            Request request = new Request.Builder()
                    .url(strings[0])
                    .get()
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
            if(s!=null){
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.getString("code").equals("S200")){
                        JSONObject jsonObject_body = new JSONObject(jsonObject.getString("body"));
                        JSONArray imageArr = new JSONArray(jsonObject_body.getString("images"));
                        ArrayList<Image> list_Img = new ArrayList<>();
                        for(int i=0;i<imageArr.length();i++){
                            JSONObject image = imageArr.getJSONObject(i);
                            list_Img.add(new Image(image.getString("id"),image.getString("url"),image.getString("utilities_id"),null,false));
                        }

                        JSONArray serviceArr = new JSONArray(jsonObject_body.getString("services"));
                        ArrayList<Integer> list_service = new ArrayList<>();
                        for(int i=0;i<serviceArr.length();i++){
                            JSONObject service = serviceArr.getJSONObject(i);
                            list_service.add(Integer.parseInt(service.getString("service_id")));
                        }

                        ArrayList<Integer> listTag = new ArrayList<>();
                        JSONArray arrTag = new JSONArray(jsonObject_body.getString("tags"));
                        for(int i=0;i<arrTag.length();i++){
                            JSONObject tag = arrTag.getJSONObject(i);
                            listTag.add(Integer.parseInt(tag.getString("id")));
                        }


                        JSONObject superMarket = new JSONObject(jsonObject_body.getString("supermarket"));

                        String check = superMarket.getString("business_hour");
                        Log.e("cmm",check);
                        ArrayList<String> list_wd = new ArrayList<>();
                        if(!check.equals("") && !check.equals("null")) {
                            JSONArray workingtime = new JSONArray(superMarket.getString("business_hour"));
                            if(workingtime.length()>0){
                                for (int i=0;i<workingtime.length();i++){
                                    String str = (String) workingtime.get(i);
                                    list_wd.add(str);
                                    Log.e("ngaycmm",list_wd.get(i));
                                }}
                        }
                        else {
                            list_wd.add("");
                            list_wd.add("");
                        }


                        list_sm.add(new Supermarket(superMarket.getString("category_id"), superMarket.getString("utilities_id"),superMarket.getString("lat"),superMarket.getString("long"),
                                superMarket.getString("name"),superMarket.getString("name_vi"),superMarket.getString("name_en"),superMarket.getString("name_es"),superMarket.getString("name_cn"),superMarket.getString("name_ko"),
                                superMarket.getString("memo"),superMarket.getString("memo_vi"),superMarket.getString("memo_en"),superMarket.getString("memo_es"),superMarket.getString("memo_cn"),superMarket.getString("memo_ko"),
                                superMarket.getString("address"),superMarket.getString("address_vi"),superMarket.getString("address_en"),superMarket.getString("address_es"),superMarket.getString("address_cn"),superMarket.getString("address_ko"),
                                list_Img,list_service,superMarket.getString("tel"),superMarket.getString("road_goal"),list_wd.get(0),list_wd.get(1),listTag,superMarket.getString("store_type")));
                        Log.e("abcd",String.valueOf(list_sm.size()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            getcity.getCity(list_sm);

            super.onPostExecute(s);

        }

    }

    public void setTagLayout(){

        final RecyclerView addtag = findViewById(R.id.tag);
        final TagAdapter tagAdapter = new TagAdapter(this, listTag);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(SuperMarketDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        addtag.setLayoutManager(horizontalLayoutManager);
        addtag.setAdapter(tagAdapter);

        tagAdapter.setClickListener(new ItemClick() {
            @Override
            public void Click(View view, int position) {
                Intent intent = new Intent(SuperMarketDetailActivity.this,MainActivity.class);
                Bundle b = new Bundle();
                b.putString("token",token);
                b.putString("latLocation", latLocation);
                b.putString("lngLocation", lngLocation);
                b.putString("cate", listTag.get(position).toString());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }
}
