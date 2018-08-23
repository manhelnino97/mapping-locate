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
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.example.manhe.search.adapter.TagAdapter;
import com.example.manhe.search.adapter.TimeAdapter;
import com.example.manhe.search.model.Image;
import com.example.manhe.search.model.PostBox;
import com.example.manhe.search.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostBoxDetailActivity extends AppCompatActivity {
    TextView tvTitle;
    RecyclerView recyclerViewWeekday, recyclerViewWeekend;
    ArrayList<String> listTimeWd;
    ArrayList<String> listTimeWk;
    ImageButton btnLeft, btnRight;
    ViewPager viewPager;
    RelativeLayout btnEdit, btnReport;
    String lang,lat,lng,token,idd, latLocation, lngLocation;
    TextView tvtype,tvpostname,tvadd,tvpostcode,tvbranch,tvnote;
    String add,postname,postcode,branch,note, addLocation;
    TimeAdapter timeWd, timeWk;
    BitmapAdapter bitmapAdapter;
    ArrayList<Image> images = new ArrayList<>();
    ArrayList<Integer> listTag = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pb_details);

        tvtype = findViewById(R.id.tvtype);
        tvpostname = findViewById(R.id.tvpostname);
        tvadd = findViewById(R.id.tvadd);
        tvpostcode = findViewById(R.id.tvpostcode);
        tvbranch = findViewById(R.id.tvbranch);
        tvnote = findViewById(R.id.tvnote);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#fb3680"), android.graphics.PorterDuff.Mode.MULTIPLY);

        TextView tvprivacy =findViewById(R.id.privacy);
        tvprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPrivacy(PostBoxDetailActivity.this);

            }
        });

        Bundle bundle = getIntent().getExtras();
        addLocation = bundle.getString("addLocation");
        lang = bundle.getString("lang");
        latLocation = bundle.getString("latLocation");
        lngLocation = bundle.getString("lngLocation");
        token = bundle.getString("token");
        idd = bundle.getString("idd");

        setActionBar();

        new GetData(new GetData.Get_City() {
            @Override
           public void getCity(ArrayList<PostBox> data) {
                if(data.size()>0){
                    progressBar.setVisibility(View.GONE);
                    postcode = data.get(0).getCode();
                    branch = data.get(0).getCollection_branch();
                    lat = data.get(0).getLat();
                    lng = data.get(0).getLng();
                    switch (lang) {
                        case "vi":
                            postname = data.get(0).getName_vi();
                            add = data.get(0).getAdd_vi();
                            note = data.get(0).getMemo_vi();
                            break;
                        case "ja":
                            postname = data.get(0).getName();
                            add = data.get(0).getAdd();
                            note = data.get(0).getMemo();
                            break;
                        case "en":
                            postname = data.get(0).getName_en();
                            add = data.get(0).getAdd_en();
                            note = data.get(0).getMemo_en();
                            break;
                        case "es":
                            postname = data.get(0).getName_es();
                            add = data.get(0).getAdd_es();
                            note = data.get(0).getMemo_es();
                            break;
                        case "zh":
                            postname = data.get(0).getName_cn();
                            add = data.get(0).getAdd_cn();
                            note = data.get(0).getMemo_cn();
                            break;
                        case "ko":
                            postname = data.get(0).getname_ko();
                            add = data.get(0).getadd_ko();
                            note = data.get(0).getmemo_ko();
                            break;
                    }

                    if(!postname.equals("null")) tvpostname.setText(postname);
                    if(!add.equals("null")) tvadd.setText(add);
                    if(!postcode.equals("null")) tvpostcode.setText(postcode);
                    if(!branch.equals("null")) tvbranch.setText(branch);
                    if(!note.equals("null")) tvnote.setText(note);

                    listTimeWd = data.get(0).getWorkingtime_weekday();
                    listTimeWk = data.get(0).getWorkingtime_saturday();
                    setTime();

                    images = data.get(0).getImage();
                    Log.e("dmm",String.valueOf(images.size()));
                    setImagePager();

                    listTag = data.get(0).getListTag();
                    setTagLayout();
                }
                else {
                    Utils.messageError(PostBoxDetailActivity.this);
                }

                setOnClickButton();

            }
        }).execute("http://192.168.1.67:8080/api/getDetailPostBox/"+idd+"?token="+token);



    }

    public void setOnClickButton(){
        btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("addLocation", addLocation);
                b.putString("idd", idd);
                b.putString("token", token);
                b.putString("lang", lang);
                b.putString("latLocation", latLocation);
                b.putString("lngLocation", lngLocation);
                b.putString("lat", lat);
                b.putString("lng", lng);
                b.putString("addLocation", add);
                b.putString("postname", postname);
                b.putString("postcode", postcode);
                b.putString("branch", branch);
                b.putString("note", note);
                b.putStringArrayList("listTimeWd", listTimeWd);
                b.putStringArrayList("listTimeWk", listTimeWk);
                b.putParcelableArrayList("bitmaps", images);
                b.putIntegerArrayList("listtag", listTag);
                Intent it = new Intent(PostBoxDetailActivity.this, PostBoxEditActivity.class);
                it.putExtras(b);
                startActivity(it);
            }
        });

        btnReport = findViewById(R.id.btn_report);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showReportDialog(PostBoxDetailActivity.this,lat,lng,idd,token);
            }
        });
    }

    public void setTime(){
        recyclerViewWeekday = findViewById(R.id.weekday);
        recyclerViewWeekend = findViewById(R.id.weekend);

        timeWd = new TimeAdapter(this, listTimeWd);
        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewWeekday.setAdapter(timeWd);
        recyclerViewWeekday.setLayoutManager(layoutManager1);

        timeWk = new TimeAdapter(this, listTimeWk);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewWeekend.setAdapter(timeWk);
        recyclerViewWeekend.setLayoutManager(layoutManager);
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
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fb3680")));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void setImagePager(){
        viewPager = findViewById(R.id.viewPager);
        bitmapAdapter = new BitmapAdapter(this,images);
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

    static class GetData extends AsyncTask<String,Void,String> {
        interface Get_City{
            void  getCity(ArrayList<PostBox> data);
        }
        public Get_City getcity = null;
        public GetData(Get_City getcity) {
            this.getcity = getcity;
        }
        ArrayList<PostBox> list_postbox = new ArrayList<>();
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

                        ArrayList<Integer> listTag = new ArrayList<>();
                        JSONArray arrTag = new JSONArray(jsonObject_body.getString("tags"));
                        for(int i=0;i<arrTag.length();i++){
                            JSONObject tag = arrTag.getJSONObject(i);
                            listTag.add(Integer.parseInt(tag.getString("id")));
                        }

                        JSONObject postBoxes = new JSONObject(jsonObject_body.getString("postBoxes"));


                        JSONArray workingtime_weekday = null;
                        ArrayList<String> list_wd = new ArrayList<>();
                        if(!((String)postBoxes.getString("workingtime_weekday")).equals("null")) {
                            workingtime_weekday = new JSONArray(postBoxes.getString("workingtime_weekday"));
                            if(workingtime_weekday.length()>0){
                                for (int i=0;i<workingtime_weekday.length();i++){
                                    String str = (String) workingtime_weekday.get(i);
                                    list_wd.add(str);
                                    Log.e("ngaycmm",list_wd.get(i));
                                }
                            }
                        }

                        JSONArray workingtime_weekend = null;
                        ArrayList<String> list_wk = new ArrayList<>();
                        if(!((String)postBoxes.getString("workingtime_saturday")).equals("null")){
                            workingtime_weekend = new JSONArray(postBoxes.getString("workingtime_saturday"));
                            if(workingtime_weekend.length()>0){
                                for (int i=0;i<workingtime_weekend.length();i++){
                                    String str = (String) workingtime_weekend.get(i);
                                    list_wk.add(str);
                                    Log.e("ngaycmm",list_wk.get(i));
                                }
                            }
                        }


                        list_postbox.add(new PostBox(postBoxes.getString("category_id"),postBoxes.getString("lat"),postBoxes.getString("long"),
                                postBoxes.getString("name"),postBoxes.getString("name_vi"),postBoxes.getString("name_en"),postBoxes.getString("name_es"),postBoxes.getString("name_cn"),postBoxes.getString("name_ko"),
                                postBoxes.getString("address"),postBoxes.getString("address_vi"),postBoxes.getString("address_en"),postBoxes.getString("address_es"),postBoxes.getString("address_cn"),postBoxes.getString("address_ko"),
                                postBoxes.getString("memo"),postBoxes.getString("memo_vi"),postBoxes.getString("memo_en"),postBoxes.getString("memo_es"),postBoxes.getString("memo_cn"),postBoxes.getString("memo_ko"),
                                postBoxes.getString("utilities_id"),postBoxes.getString("code"),postBoxes.getString("collection_branch"),list_wd,list_wk,
                                list_Img,listTag));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            getcity.getCity(list_postbox);
            super.onPostExecute(s);

        }

    }

    public void setTagLayout(){

        final RecyclerView addtag = findViewById(R.id.tag);
        final TagAdapter tagAdapter = new TagAdapter(this, listTag);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PostBoxDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        addtag.setLayoutManager(horizontalLayoutManager);
        addtag.setAdapter(tagAdapter);

        tagAdapter.setClickListener(new ItemClick() {
            @Override
            public void Click(View view, int position) {
                Intent intent = new Intent(PostBoxDetailActivity.this,MainActivity.class);
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
