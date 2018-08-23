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
import com.example.manhe.search.adapter.TagAdapter;
import com.example.manhe.search.model.CoffeeShop;
import com.example.manhe.search.model.Image;
import com.example.manhe.search.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CoffeeShopDetailActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageButton btnLeft, btnRight, btnTag;
    ViewPager viewPager;
    String lang,lat,lng,token,idd;
    String add,name,phonenumber,website,note, addLocation, latLocation, lngLocation;
    String timeOpen, timeClose;
    TextView tvtype,tvname,tvadd,tvphone,tvweb,tvnote, tvTimeopen, tvTimeclose,tvprivacy;
    BitmapAdapter bitmapAdapter;
    ArrayList<Image> images = new ArrayList<>();
    RelativeLayout btn_edit,btn_report;
    String vd = null,pb = null,cf = null,cs= null,sm = null;
    ArrayList<Integer> listTag = new ArrayList<>();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cf_details);

        tvtype = findViewById(R.id.tvtype);
        tvname = findViewById(R.id.tvname);
        tvadd = findViewById(R.id.tvadd);
        tvphone = findViewById(R.id.tvphone);
        tvweb = findViewById(R.id.tvweb);
        tvnote = findViewById(R.id.tvnote);
        tvTimeopen = findViewById(R.id.tvtime_open);
        tvTimeclose = findViewById(R.id.tvtime_close);
        tvprivacy =findViewById(R.id.privacy);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#6c4109"), android.graphics.PorterDuff.Mode.MULTIPLY);


        Bundle bundle = getIntent().getExtras();
        addLocation = bundle.getString("addLocation");
        latLocation = bundle.getString("latLocation");
        lngLocation = bundle.getString("lngLocation");
        lang = bundle.getString("lang");
        token = bundle.getString("token");
        idd = bundle.getString("idd");

        setActionBar();

        tvprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPrivacy(CoffeeShopDetailActivity.this);
            }
        });

        new GetData(new GetData.Get_City() {
            @Override
            public void getCity(ArrayList<CoffeeShop> data) {
                if(data.size()>0){
                    progressBar.setVisibility(View.GONE);
                    lat = data.get(0).getLat();
                    lng = data.get(0).getLng();
                    phonenumber = data.get(0).getPhonenumber();
                    website = data.get(0).getWebsite();
                    timeOpen = data.get(0).getTime_open();
                    timeClose = data.get(0).getTime_close();

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
                            name = data.get(0).getname_ko();
                            add = data.get(0).getadd_ko();
                            note = data.get(0).getmemo_ko();
                            break;
                    }
                    images = data.get(0).getImageArrayList();
                    setImagePager();
                    if(!name.equals("null")) tvname.setText(name);
                    if(!add.equals("null")) tvadd.setText(add);
                    if(!phonenumber.equals("null")) tvphone.setText(phonenumber);
                    if(!website.equals("null")) tvweb.setText(website);
                    if(!note.equals("null")) tvnote.setText(note);
                    if(!timeOpen.equals("") && !timeClose.equals("")){
                        tvTimeopen.setText(timeOpen);
                        tvTimeclose.setText(timeClose);
                    }
                    else{
                        tvTimeopen.setText("00:00 AM");
                        tvTimeclose.setText("00:00 PM");
                    }

                    listTag = data.get(0).getListTag();
                    setTagLayout();
                }
                else {
                    Utils.messageError(CoffeeShopDetailActivity.this);
                }


                setOnClickButton();
            }
        }).execute("http://192.168.1.67:8080/api/getDetailCafeShop/"+idd+"?token="+token);



    }

    public void setOnClickButton(){
        btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putString("addLocation", addLocation);
                b.putString("idd", idd);
                b.putString("token", token);
                b.putString("lang", lang);
                b.putString("lat", lat);
                b.putString("lng", lng);
                b.putString("latLocation", latLocation);
                b.putString("lngLocation", lngLocation);
                b.putString("add", add);
                b.putString("phone", phonenumber);
                b.putString("web", website);
                b.putString("name", name);
                b.putString("note", note);
                b.putString("timeopen", timeOpen);
                b.putString("timeclose", timeClose);
                b.putParcelableArrayList("bitmaps", images);
                b.putIntegerArrayList("listtag", listTag);
                Intent it = new Intent(CoffeeShopDetailActivity.this, CoffeeShopEditActivity.class);
                it.putExtras(b);
                startActivity(it);
            }
        });

        btn_report = findViewById(R.id.btn_report);
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showReportDialog(CoffeeShopDetailActivity.this,lat,lng,idd,token);
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
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6c4109")));
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
            void  getCity(ArrayList<CoffeeShop> data);
        }
        public Get_City getcity = null;
        public GetData(Get_City getcity) {
            this.getcity = getcity;
        }
        ArrayList<CoffeeShop> list_coffee = new ArrayList<>();
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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
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

                        ArrayList<Integer> listTag = new ArrayList<>();
                        JSONArray arrTag = new JSONArray(jsonObject_body.getString("tags"));
                        for(int i=0;i<arrTag.length();i++){
                            JSONObject tag = arrTag.getJSONObject(i);
                            listTag.add(Integer.parseInt(tag.getString("id")));
                        }

                        JSONObject cafeShop = new JSONObject(jsonObject_body.getString("cafeShop"));

                        JSONArray workingtime_weekday = null;
                        ArrayList<String> list_wd = new ArrayList<>();
                        if(!((String)cafeShop.getString("workingtime_weekday")).equals("null")) {
                            workingtime_weekday = new JSONArray(cafeShop.getString("workingtime_weekday"));
                            if(workingtime_weekday.length()>0){
                                for (int i=0;i<workingtime_weekday.length();i++){
                                    String str = (String) workingtime_weekday.get(i);
                                    list_wd.add(str);
                                }
                            }
                        }
                        else{
                            list_wd.add("");
                            list_wd.add("");
                        }


                        list_coffee.add(new CoffeeShop(cafeShop.getString("category_id"),cafeShop.getString("lat"),cafeShop.getString("long"),
                                cafeShop.getString("name"),cafeShop.getString("name_vi"),cafeShop.getString("name_en"),cafeShop.getString("name_es"),cafeShop.getString("name_cn"),cafeShop.getString("name_ko"),
                                cafeShop.getString("address"),cafeShop.getString("address_vi"),cafeShop.getString("address_en"),cafeShop.getString("address_es"),cafeShop.getString("address_cn"),cafeShop.getString("address_ko"),
                                cafeShop.getString("memo"),cafeShop.getString("memo_vi"),cafeShop.getString("memo_en"),cafeShop.getString("memo_es"),cafeShop.getString("memo_cn"),cafeShop.getString("memo_ko"),
                                cafeShop.getString("utilities_id"),cafeShop.getString("phone"),cafeShop.getString("website"),list_wd.get(0),list_wd.get(1),
                                list_Img,listTag));



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            getcity.getCity(list_coffee);
            super.onPostExecute(s);
        }
    }

    public void setTagLayout(){

        final RecyclerView addtag = findViewById(R.id.tag);
        final TagAdapter tagAdapter = new TagAdapter(this, listTag);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(CoffeeShopDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        addtag.setLayoutManager(horizontalLayoutManager);
        addtag.setAdapter(tagAdapter);

        tagAdapter.setClickListener(new ItemClick() {
            @Override
            public void Click(View view, int position) {
                Intent intent = new Intent(CoffeeShopDetailActivity.this,MainActivity.class);
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
