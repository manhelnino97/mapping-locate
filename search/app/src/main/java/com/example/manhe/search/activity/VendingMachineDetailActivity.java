package com.example.manhe.search.activity;

import android.annotation.SuppressLint;
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
import com.example.manhe.search.adapter.TagAdapter;
import com.example.manhe.search.model.Image;
import com.example.manhe.search.model.VendingMachine;
import com.example.manhe.search.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VendingMachineDetailActivity extends AppCompatActivity{

    TextView tvTitle;
    Integer RQC = 123;
    RelativeLayout btnEdit;
    ArrayList<Image> images = new ArrayList<Image>();
    BitmapAdapter bitmapAdapter;
    ArrayList<Integer> listTag = new ArrayList<>();
    String token, latLocation, lngLocation,idd,lang,lat,lng,add_location;
    ImageButton btn_left, btn_right;
    ViewPager viewPager;
    String name,add,memo;
    ProgressBar progressBar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vm_detail);



        TextView tvprivacy =findViewById(R.id.privacy);
        tvprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPrivacy(VendingMachineDetailActivity.this);
            }
        });

        Bundle bundle = getIntent().getExtras();
        idd = bundle.getString("idd");
        token = bundle.getString("token");
        lang = bundle.getString("lang");
        latLocation = bundle.getString("latLocation");
        lngLocation = bundle.getString("lngLocation");
        add_location = bundle.getString("addLocation");


        final TextView txttype = findViewById(R.id.tvtype);
        final TextView txtadd = findViewById(R.id.tvadd);
        final TextView txtnote = findViewById(R.id.tvnote);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ff9000"), android.graphics.PorterDuff.Mode.MULTIPLY);

        setActionBar();
        btnEdit = findViewById(R.id.btn_edit);
        new GetData(new GetData.Get_City() {
            @Override
            public void getCity(final ArrayList<VendingMachine> data) {
                if(data.size()>0) {
                    progressBar.setVisibility(View.GONE);
                    lat = data.get(0).getLat();
                    lng = data.get(0).getLng();
                    images = data.get(0).getImage();
                    setImagePager();


                    Log.e("dkm",data.toString());
                    switch (lang) {
                        case "vi":
                            name = data.get(0).getName_vi();
                            add = data.get(0).getAdd_vi();
                             memo = data.get(0).getMemo_vi();
                            break;
                        case "ja":
                            name = data.get(0).getName();
                            add = data.get(0).getAdd();
                             memo = data.get(0).getMemo();
                            break;
                        case "en":
                            name = data.get(0).getName_en();
                            add = data.get(0).getAdd_en();
                             memo = data.get(0).getMemo_en();
                            break;
                        case "es":
                            name = data.get(0).getName_es();
                            add = data.get(0).getAdd_es();
                            memo = data.get(0).getMemo_es();
                            break;
                        case "zh":
                            name = data.get(0).getName_cn();
                            add = data.get(0).getAdd_cn();
                            memo = data.get(0).getMemo_cn();
                            break;
                        case "ko":
                            name = data.get(0).getname_ko();
                            add = data.get(0).getadd_ko();
                            memo = data.get(0).getmemo_ko();
                            break;
                    }



                    txttype.setText(name);
                    txtadd.setText(add);
                    if(!memo.equals("null")) txtnote.setText(memo);
                    listTag = data.get(0).getListTag();
                    setTagLayout();

                    btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                           Bundle bundle1 = new Bundle();
                           bundle1.putString("type", name);
                           bundle1.putString("add", add);
                           bundle1.putString("addLocation",add_location);
                           bundle1.putString("note", memo);
                           bundle1.putString("lang",lang);
                           bundle1.putString("token",token);
                           bundle1.putString("idd",idd);
                           bundle1.putString("lat",lat);
                           bundle1.putString("lng",lng);
                           bundle1.putString("latLocation", latLocation);
                           bundle1.putString("lngLocation", lngLocation);
                           bundle1.putParcelableArrayList("bitmaps",images);
                           bundle1.putIntegerArrayList("listtag", listTag);
                           Intent intent = new Intent(VendingMachineDetailActivity.this,VendingMachineEditAcitvity.class);
                           intent.putExtras(bundle1);
                           startActivity(intent);
                            }
                        });

                        RelativeLayout btnReport;
                        btnReport = findViewById(R.id.btn_report);
                        btnReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.showReportDialog(VendingMachineDetailActivity.this,lat,lng,idd,token);
                            }
                        });

                }
                else {
                    Utils.messageError(VendingMachineDetailActivity.this);
                }
            }
        }).execute("http://192.168.1.67:8080/api/getDetailVendingMachine/"+idd+"?token="+token);



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
            void  getCity(ArrayList<VendingMachine> data);
        }
        Get_City getcity = null;
        GetData(Get_City getcity) {
            this.getcity = getcity;
        }
        ArrayList<VendingMachine> list = new ArrayList<>();
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
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("body"));
                        JSONObject vendingMachine = jsonArray.getJSONObject(0);


                        JSONArray imageArr = new JSONArray(vendingMachine.getString("images"));
                        ArrayList<Image> list_Img = new ArrayList<>();
                        if(imageArr.length()>0){
                            for(int i=0;i<imageArr.length();i++){
                                JSONObject image = imageArr.getJSONObject(i);
                                list_Img.add(new Image(image.getString("id"),image.getString("url"),image.getString("utilities_id"),null,false));
                            }
                        }

                        ArrayList<Integer> listTag = new ArrayList<>();
                        JSONArray arrTag = new JSONArray(vendingMachine.getString("cates"));
                        for(int i=0;i<arrTag.length();i++){
                            JSONObject tag = arrTag.getJSONObject(i);
                            listTag.add(Integer.parseInt(tag.getString("id")));
                        }

                        list.add(new VendingMachine(vendingMachine.getString("category_id"),vendingMachine.getString("lat"),vendingMachine.getString("long"),
                                vendingMachine.getString("name"),vendingMachine.getString("name_vi"),vendingMachine.getString("name_en"), vendingMachine.getString("name_es"),vendingMachine.getString("name_cn"),vendingMachine.getString("name_ko"),
                                vendingMachine.getString("address"),vendingMachine.getString("address_vi"),vendingMachine.getString("address_en"),  vendingMachine.getString("address_es"),vendingMachine.getString("address_cn"),vendingMachine.getString("address_ko"),
                                vendingMachine.getString("memo"),vendingMachine.getString("memo_vi"),vendingMachine.getString("memo_en"),vendingMachine.getString("memo_es"),vendingMachine.getString("memo_cn"),vendingMachine.getString("memo_ko"),
                                vendingMachine.getString("id"),list_Img,listTag));
                        Log.e("abcd",list.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            getcity.getCity(list);
            super.onPostExecute(s);

        }
    }

    public void setTagLayout(){

        final RecyclerView addtag = findViewById(R.id.tag);
        final TagAdapter tagAdapter = new TagAdapter(this, listTag);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(VendingMachineDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        addtag.setLayoutManager(horizontalLayoutManager);
        addtag.setAdapter(tagAdapter);

        tagAdapter.setClickListener(new ItemClick() {
            @Override
            public void Click(View view, int position) {
                Intent intent = new Intent(VendingMachineDetailActivity.this,MainActivity.class);
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

    public void setImagePager(){
        viewPager = findViewById(R.id.viewPager);
        bitmapAdapter = new BitmapAdapter(this,images);
        viewPager.setAdapter(bitmapAdapter);
        btn_left = findViewById(R.id.btnleft);
        btn_right = findViewById(R.id.btnright);


        if(viewPager.getCurrentItem()<bitmapAdapter.getCount()-1){
            btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }
            });
        }
        if(viewPager.getCurrentItem()>0){
            btn_left.setOnClickListener(new View.OnClickListener() {
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
                    btn_right.setImageResource(R.drawable.ic_right);
                    btn_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }
                else{
                    btn_right.setImageDrawable(null);
                }
                if(viewPager.getCurrentItem()>0){
                    btn_left.setImageResource(R.drawable.ic_left);
                    btn_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                        }
                    });
                }
                else{
                    btn_left.setImageDrawable(null);
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

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9000")));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
