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
import com.example.manhe.search.adapter.ListPaymentAdapter;
import com.example.manhe.search.adapter.ListServiceAdapter;
import com.example.manhe.search.adapter.PaymentAdapter;
import com.example.manhe.search.adapter.ServiceAdapter;
import com.example.manhe.search.adapter.TagAdapter;
import com.example.manhe.search.model.ConvenienceStore;
import com.example.manhe.search.model.Image;
import com.example.manhe.search.model.Payment;
import com.example.manhe.search.model.Service;
import com.example.manhe.search.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConvenienceStoreDetailActivity extends AppCompatActivity {
    ImageButton btnLeft, btnRight, btnTag;
    ViewPager viewPager;
    TextView tvTitle,tvtype,tvadd,tvname,tvphone,senditems,receiveitems,tvnote;
    String lang,add_location,lat,lng,token,idd,name,add,phone,note,send,receive, latLocation, lngLocation;
    RecyclerView listservice,listpayment;
    ArrayList<Service> listService = new ArrayList<>();
    ArrayList<Payment> listPayment = new ArrayList<>();
    ListServiceAdapter listServiceAdapter;
    ListPaymentAdapter listPaymentAdapter;
    ServiceAdapter serviceAdapter;
    PaymentAdapter paymentAdapter;
    RelativeLayout btnEdit, btnReport;
    ArrayList<Image> images = new ArrayList<>();
    ArrayList<Integer> listIdService,listIdPayment;
    BitmapAdapter bitmapAdapter;
    ArrayList<Integer> listTag = new ArrayList<>();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_details);

        tvtype = findViewById(R.id.tvtype);
        tvadd = findViewById(R.id.tvadd);
        tvname = findViewById(R.id.tvname);
        tvphone = findViewById(R.id.tvphone);
        tvnote = findViewById(R.id.tvnote);
        senditems = findViewById(R.id.tvsend);
        receiveitems = findViewById(R.id.tvreceive);
        listservice = findViewById(R.id.listservice);
        listpayment = findViewById(R.id.listpayment);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#30a40d"), android.graphics.PorterDuff.Mode.MULTIPLY);

        TextView tvprivacy =findViewById(R.id.privacy);
        tvprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPrivacy(ConvenienceStoreDetailActivity.this);
            }
        });

        Bundle bundle = getIntent().getExtras();
        add_location = bundle.getString("addLocation");
        lang = bundle.getString("lang");
        latLocation = bundle.getString("latLocation");
        lngLocation = bundle.getString("lngLocation");
        token = bundle.getString("token");
        idd = bundle.getString("idd");

        Log.e("abcd",idd);



        setActionBar();
        createServiceAndPayment();
        setListserviceAndPayment();

        new GetData(new GetData.Get_City() {
            @Override
            public void getCity(ArrayList<ConvenienceStore> data) {
                if(data.size()>0) {
                    progressBar.setVisibility(View.GONE);
                    lat = data.get(0).getLat();
                    lng = data.get(0).getLng();
                    phone = data.get(0).getPhonnumber();
                    send = data.get(0).getSenditems();
                    receive = data.get(0).getReceiveitems();

                    if (send.equals("1")) senditems.setVisibility(View.VISIBLE);
                    else senditems.setVisibility(View.GONE);
                    if (receive.equals("1")) receiveitems.setVisibility(View.VISIBLE);
                    else receiveitems.setVisibility(View.GONE);

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
                    images = data.get(0).getImageArrayList();
                    setImagePager();

                    if (!name.equals("null")) tvname.setText(name);
                    if (!add.equals("null")) tvadd.setText(add);
                    if (!phone.equals("null")) tvphone.setText(phone);
                    if (!note.equals("null")) tvnote.setText(note);

                    listIdService = data.get(0).getListIdService();
                    listIdPayment = data.get(0).getListIdPayment();
                    ArrayList<Service> serviceArrayList = new ArrayList<>();
                    for(int i=0;i<data.get(0).getListIdService().size();i++){
                        for(int j = 0; j< listService.size(); j++){
                            if(data.get(0).getListIdService().get(i)== listService.get(j).getId()){
                                serviceArrayList.add(listService.get(j));
                                serviceArrayList.get(i).setIschecked(true);
                            }
                        }
                    }
                    listServiceAdapter = new ListServiceAdapter(ConvenienceStoreDetailActivity.this, serviceArrayList);
                    LinearLayoutManager horizontalLayoutManager_1 = new LinearLayoutManager(ConvenienceStoreDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    listservice.setLayoutManager(horizontalLayoutManager_1);
                    listservice.setAdapter(listServiceAdapter);

                    ArrayList<Payment> paymentArrayList = new ArrayList<>();
                    for(int i=0;i<data.get(0).getListIdPayment().size();i++){
                        for(int j = 0; j< listPayment.size(); j++){
                            if(data.get(0).getListIdPayment().get(i)== listPayment.get(j).getId()){
                                paymentArrayList.add(listPayment.get(j));
                                paymentArrayList.get(i).setIschecked(true);
                            }
                        }
                    }

                    listPaymentAdapter = new ListPaymentAdapter(ConvenienceStoreDetailActivity.this, paymentArrayList);
                    LinearLayoutManager horizontalLayoutManager_2 = new LinearLayoutManager(ConvenienceStoreDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    listpayment.setLayoutManager(horizontalLayoutManager_2);
                    listpayment.setAdapter(listPaymentAdapter);

                    listTag = data.get(0).getListTag();
                    setTagLayout();

                    setOnClickButton();
                }
                else {
                    Utils.messageError(ConvenienceStoreDetailActivity.this);
                }

            }
        }).execute("http://192.168.1.67:8080/api/getDetailConvenienceStore/"+idd+"?token="+token);



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
                b.putString("send", send);
                b.putString("receive", receive);
                b.putIntegerArrayList("listidservice", listIdService);
                b.putIntegerArrayList("listidpayment", listIdPayment);
                b.putParcelableArrayList("bitmaps", images);
                b.putIntegerArrayList("listtag", listTag);

                Intent intent = new Intent(ConvenienceStoreDetailActivity.this, ConvenienceStoreEditActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btnReport = findViewById(R.id.btn_report);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showReportDialog(ConvenienceStoreDetailActivity.this,lat,lng,idd,token);
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


        listPayment.add(new Payment(1,R.drawable.androidpay,"Android Pay",false));
        listPayment.add(new Payment(2,R.drawable.applepay,"Apple Pay",false));
        listPayment.add(new Payment(3,R.drawable.googlepay,"Google Pay",false));
        listPayment.add(new Payment(4,R.drawable.androidpay,"Android Pay",false));
        listPayment.add(new Payment(5,R.drawable.applepay,"Apple Pay",false));
        listPayment.add(new Payment(6,R.drawable.googlepay,"Google Pay",false));

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



        paymentAdapter = new PaymentAdapter(this, listPayment);

        ArrayList<Payment> addPayment = new ArrayList<>();
        for(int i = 0; i< listPayment.size(); i++){
            if(listPayment.get(i).getIschecked()){
                addPayment.add(listPayment.get(i));
            }
        }

        listPaymentAdapter = new ListPaymentAdapter(this, addPayment);
        LinearLayoutManager horizontalLayoutManager_2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listpayment.setLayoutManager(horizontalLayoutManager_2);
        listpayment.setAdapter(listPaymentAdapter);
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
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#30a40d")));
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
            void  getCity(ArrayList<ConvenienceStore> data);
        }
        Get_City getcity = null;
        GetData(Get_City getcity) {
            this.getcity = getcity;
        }
        ArrayList<ConvenienceStore> list_cs = new ArrayList<>();
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
                        if(serviceArr.length()>0){
                            for(int i=0;i<serviceArr.length();i++){
                                JSONObject service = serviceArr.getJSONObject(i);
                                list_service.add(Integer.parseInt(service.getString("service_id")));
                            }

                        }

                        ArrayList<Integer> listTag = new ArrayList<>();
                        JSONArray arrTag = new JSONArray(jsonObject_body.getString("tags"));
                        for(int i=0;i<arrTag.length();i++){
                            JSONObject tag = arrTag.getJSONObject(i);
                            listTag.add(Integer.parseInt(tag.getString("id")));
                        }


                        JSONObject convenienceStores = new JSONObject(jsonObject_body.getString("convenienceStores"));

                        String paymentArr = convenienceStores.getString("payment_method");
                        ArrayList<Integer> list_payment = new ArrayList<>();
                        if(!paymentArr.equals("null")){
                            String[] payments = paymentArr.split("\\.");

                            for(int i=0;i<payments.length;i++){
                                list_payment.add(Integer.parseInt(payments[i]));
                            }
                        }


                        Log.e("abcd",String.valueOf(list_payment.size()));
                        list_cs.add(new ConvenienceStore(convenienceStores.getString("category_id"), convenienceStores.getString("utilities_id"),convenienceStores.getString("lat"),convenienceStores.getString("long"),
                                convenienceStores.getString("name"),convenienceStores.getString("name_vi"),convenienceStores.getString("name_en"),convenienceStores.getString("name_es"),convenienceStores.getString("name_cn"),convenienceStores.getString("name_ko"),
                                convenienceStores.getString("memo"),convenienceStores.getString("memo_vi"),convenienceStores.getString("memo_en"),convenienceStores.getString("memo_es"),convenienceStores.getString("memo_cn"),convenienceStores.getString("memo_ko"),
                                convenienceStores.getString("address"),convenienceStores.getString("address_vi"),convenienceStores.getString("address_en"),convenienceStores.getString("address_es"),convenienceStores.getString("address_cn"),convenienceStores.getString("address_ko"),
                                list_Img,list_service,list_payment,convenienceStores.getString("phone"),convenienceStores.getString("allow_send_items"),convenienceStores.getString("allow_receive_items"),listTag));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("abcd",String.valueOf(list_cs.size()));
            }
            getcity.getCity(list_cs);
            super.onPostExecute(s);
        }

    }

    public void setTagLayout(){

        final RecyclerView addtag = findViewById(R.id.tag);
        final TagAdapter tagAdapter = new TagAdapter(this, listTag);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ConvenienceStoreDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        addtag.setLayoutManager(horizontalLayoutManager);
        addtag.setAdapter(tagAdapter);

        tagAdapter.setClickListener(new ItemClick() {
            @Override
            public void Click(View view, int position) {
                Intent intent = new Intent(ConvenienceStoreDetailActivity.this,MainActivity.class);
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
