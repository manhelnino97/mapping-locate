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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhe.search.Interface.ItemClick;
import com.example.manhe.search.R;
import com.example.manhe.search.adapter.BitmapAdapter;
import com.example.manhe.search.adapter.ListPaymentDelAdapter;
import com.example.manhe.search.adapter.ListServiceDelAdapter;
import com.example.manhe.search.adapter.OptionsAdapter;
import com.example.manhe.search.adapter.PaymentAdapter;
import com.example.manhe.search.adapter.ServiceAdapter;
import com.example.manhe.search.adapter.TagDelAdapter;
import com.example.manhe.search.model.Image;
import com.example.manhe.search.model.Options;
import com.example.manhe.search.model.Payment;
import com.example.manhe.search.model.Service;
import com.example.manhe.search.utils.Utils;

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

public class ConvenienceStoreEditActivity extends AppCompatActivity {

    private AlertDialog.Builder dialog;
    ImageButton btnLeft, btnRight, btnTag;
    ViewPager viewPager;
    TextView tvTitle,tvtype;
    ImageButton getLocation,btn_addimg;
    String lang,add_location,lat,lng, latLocation, lngLocation,token,idd,name,add,phone,note,send,receive,lastChar="";
    RecyclerView listservice,listpayment;
    ImageView btnAddservice, btnAddpayment;
    ServiceAdapter serviceAdapter;
    PaymentAdapter paymentAdapter;
    ArrayList<Service> listService = new ArrayList<>();
    ArrayList<Payment> listPayment = new ArrayList<>();
    ListServiceDelAdapter listServiceAdapter;
    ListPaymentDelAdapter listPaymentAdapter;
    BitmapAdapter bitmapAdapter;
    ArrayList<Image> images = new ArrayList<>();
    ArrayList<File> listFile = new ArrayList<>();
    RelativeLayout btnCancel, btnEdit;
    ArrayList<Integer> listIdService,listIdPayment;
    EditText edtname,edtadd,edtnote,edtphone;
    CheckBox cbSend, cbReceive;
    String vendingMachine = null, postBox = null, coffeeShop = null, convenienceStore = null, superMarket = null;
    RecyclerView addtag;
    TagDelAdapter tagAdapter;
    ArrayList<Integer> listTag = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_edit);


        ScrollView sc = findViewById(R.id.scroll);
        sc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftKeyboard(ConvenienceStoreEditActivity.this);
                return false;
            }
        });

        TextView tvprivacy =findViewById(R.id.privacy);
        tvprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPrivacy(ConvenienceStoreEditActivity.this);
            }
        });

        tvtype = findViewById(R.id.tvtype);
        edtadd = findViewById(R.id.edtadd);
        edtname = findViewById(R.id.edtname);
        edtphone = findViewById(R.id.edtphone);
        edtnote = findViewById(R.id.edtnote);
        cbSend = findViewById(R.id.send);
        cbReceive = findViewById(R.id.receive);
        addtag = findViewById(R.id.tag);

        getLocation = findViewById(R.id.getLocation);
        listservice = findViewById(R.id.listservice);
        listpayment = findViewById(R.id.listpayment);
        btnAddservice = findViewById(R.id.btn_addservice);
        btnAddpayment = findViewById(R.id.btn_addpayment);

        Bundle bundle = getIntent().getExtras();
        add_location = bundle.getString("addLocation");
        lang = bundle.getString("lang");
        lat = bundle.getString("lat");
        lng = bundle.getString("lng");
        latLocation = bundle.getString("latLocation");
        lngLocation = bundle.getString("lngLocation");
        token = bundle.getString("token");
        idd = bundle.getString("idd");
        add = bundle.getString("addLocation");
        phone = bundle.getString("phone");
        name = bundle.getString("name");
        note = bundle.getString("note");
        send = bundle.getString("send");
        receive = bundle.getString("receive");
        listIdService = bundle.getIntegerArrayList("listidservice");
        listIdPayment = bundle.getIntegerArrayList("listidpayment");
        images = bundle.getParcelableArrayList("bitmaps");
        listTag = bundle.getIntegerArrayList("listtag");

        if (!name.equals("null")) edtname.setText(name);
        if (!add.equals("null")) edtadd.setText(add);
        if (!phone.equals("null")) edtphone.setText(phone);
        if (!note.equals("null")) edtnote.setText(note);
        if(send.equals("1")) cbSend.setChecked(true);
        if(receive.equals("1")) cbReceive.setChecked(true);

        tagAdapter = new TagDelAdapter(this, listTag);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ConvenienceStoreEditActivity.this, LinearLayoutManager.HORIZONTAL, false);
        addtag.setLayoutManager(horizontalLayoutManager);
        addtag.setAdapter(tagAdapter);


        edtphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = edtphone.getText().toString().length();
                if (digits > 1)
                    lastChar = edtphone.getText().toString().substring(digits-1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = edtphone.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!lastChar.equals("-")) {
                    if (digits == 3 || digits == 7) {
                        edtphone.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtadd.setText(add_location);
            }
        });


        setActionBar();
        setImagePager();
        setAddTagDialog();
        setListserviceAndPayment();

        btn_addimg = findViewById(R.id.btn_img);
        btn_addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.optionSelectImage(ConvenienceStoreEditActivity.this,view);
            }
        });

        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtadd.getText().toString().equals(add) && edtname.getText().toString().equals(name) && edtphone.getText().toString().equals(phone)
                        && edtnote.getText().toString().equals(note)){
                    onBackPressed();
                }
                else{
                    Utils.showAlertDialogCancelInputData(ConvenienceStoreEditActivity.this);
                }
            }
        });


        btnEdit = findViewById(R.id.btn_edit);
        if(Math.abs(Double.valueOf(lat)-Double.valueOf(latLocation))<=0.0018086 && Math.abs(Double.valueOf(lng)-Double.valueOf(lngLocation))<=0.0018018){
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String service_listid = "";
                    ArrayList<Service> serviceArrayListTemp = listServiceAdapter.getServiceArrayList();

                    for(int i=0;i<serviceArrayListTemp.size();i++){
                        if(serviceArrayListTemp.get(i).getIschecked()){
                            if(i==serviceArrayListTemp.size()-1){
                                service_listid= service_listid + String.valueOf(serviceArrayListTemp.get(i).getId());
                            }
                            else{
                                service_listid= service_listid + String.valueOf(serviceArrayListTemp.get(i).getId())+".";
                            }

                        }
                    }

                    Log.e("cmm",service_listid);


                    String payment_listid = "";
                    ArrayList<Payment> paymentArrayListTemp = listPaymentAdapter.getPaymentArrayList();

                    for(int i=0;i<paymentArrayListTemp.size();i++){
                        if(paymentArrayListTemp.get(i).getIschecked()){
                            if(i==paymentArrayListTemp.size()-1){
                                payment_listid= payment_listid + String.valueOf(paymentArrayListTemp.get(i).getId());
                            }
                            else{
                                payment_listid= payment_listid + String.valueOf(paymentArrayListTemp.get(i).getId())+".";
                            }

                        }
                    }
                    Log.e("cmm",payment_listid);

                    if(cbSend.isChecked()) send="1"; else send="0";
                    if(cbReceive.isChecked()) receive="1"; else receive="0";


                    if(Utils.isConnected(ConvenienceStoreEditActivity.this)){

                        if(!edtadd.getText().toString().equals("")){

                            new PostData(listFile,tvtype.getText().toString(),edtadd.getText().toString(),edtnote.getText().toString(),lat,lng,lang,
                                    edtphone.getText().toString(),edtname.getText().toString(),send,receive,service_listid,payment_listid, listTag)
                                    .execute("http://192.168.1.67:8080/api/editConveninceStore/"+idd+"?token="+token);

                            Bundle b = new Bundle();
                            b.putString("token",token);
                            b.putString("lang",lang);
                            b.putString("latLocation", latLocation);
                            b.putString("lngLocation", lngLocation);
                            b.putString("idd",idd);
                            b.putString("addLocation",add_location);

                            Intent intent = new Intent(ConvenienceStoreEditActivity.this,ConvenienceStoreDetailActivity.class);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                        else {
                            final AlertDialog alertDialog = new AlertDialog.Builder(ConvenienceStoreEditActivity.this).create();
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
                        final AlertDialog alertDialog = new AlertDialog.Builder(ConvenienceStoreEditActivity.this).create();
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
        else {
            btnEdit.setBackground(getResources().getDrawable(R.drawable.btn_cancel));
        }

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

        createServiceAndPayment();

        serviceAdapter = new ServiceAdapter(this, listService);

        final ArrayList<Service> addService = new ArrayList<>();
        for(int i=0; i< listIdService.size();i++){
            for(int j = 0; j< listService.size(); j++){
                if(listService.get(j).getId()==listIdService.get(i)){
                    addService.add(listService.get(j));
                    addService.get(i).setIschecked(true);
                }
            }
        }
        listServiceAdapter = new ListServiceDelAdapter(this, addService);
        LinearLayoutManager horizontalLayoutManager_1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listservice.setLayoutManager(horizontalLayoutManager_1);
        listservice.setAdapter(listServiceAdapter);


        btnAddservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(ConvenienceStoreEditActivity.this);
                LayoutInflater inflater	= getLayoutInflater();
                View convertView =  inflater.inflate(R.layout.dialog_service_custom, null);
                dialog.setTitle(getResources().getString(R.string.service));
                dialog.setView(convertView);
                dialog.setCancelable(false);
                final AlertDialog alertDialog = dialog.create();



                ListView lv = convertView.findViewById(R.id.list_item);
                lv.setAdapter(serviceAdapter);

                final ArrayList<Boolean> list_check = new ArrayList<>();
                for(int i = 0; i< listService.size(); i++){
                    if(listService.get(i).getIschecked()){
                        list_check.add(true);
                    }
                    else{
                        list_check.add(false);
                    }
                }
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(listService.get(position).getIschecked()) listService.get(position).setIschecked(false);
                        else listService.get(position).setIschecked(true);
                        serviceAdapter.notifyDataSetChanged();
                    }
                });

                TextView btn_cancel = convertView.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        for(int i = 0; i< listService.size(); i++){
                            listService.get(i).setIschecked(list_check.get(i));
                        }

                    }
                });

                TextView btn_ok = convertView.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        final ArrayList<Service> add_Service = new ArrayList<>();
                        for(int i = 0; i< listService.size(); i++){
                            if(listService.get(i).getIschecked()){
                                add_Service.add(listService.get(i));
                            }
                        }
                        listServiceAdapter = new ListServiceDelAdapter(ConvenienceStoreEditActivity.this, add_Service);
                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ConvenienceStoreEditActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        listservice.setLayoutManager(horizontalLayoutManager);
                        listservice.setAdapter(listServiceAdapter);

                        listServiceAdapter.setClickListener(new ItemClick() {
                            @Override
                            public void Click(View view, int position) {
                                listService.get(add_Service.get(position).getId()-1).setIschecked(false);
                                add_Service.remove(position);
                                listServiceAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });


        listServiceAdapter.setClickListener(new ItemClick() {
            @Override
            public void Click(View view, int position) {
                listService.get(addService.get(position).getId()-1).setIschecked(false);
                addService.remove(position);
                listServiceAdapter.notifyDataSetChanged();
            }
        });




        paymentAdapter = new PaymentAdapter(this, listPayment);

        final ArrayList<Payment> addPayment = new ArrayList<>();
        for(int i=0; i< listIdPayment.size();i++){
            for(int j = 0; j< listPayment.size(); j++){
                if(listPayment.get(j).getId()==listIdPayment.get(i)){
                    addPayment.add(listPayment.get(j));
                    addPayment.get(i).setIschecked(true);
                }
            }
        }

        listPaymentAdapter = new ListPaymentDelAdapter(this, addPayment);
        LinearLayoutManager horizontalLayoutManager_2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listpayment.setLayoutManager(horizontalLayoutManager_2);
        listpayment.setAdapter(listPaymentAdapter);

        btnAddpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(ConvenienceStoreEditActivity.this);
                LayoutInflater inflater	= getLayoutInflater();
                View convertView =  inflater.inflate(R.layout.dialog_service_custom, null);
                dialog.setTitle(getResources().getString(R.string.payment));
                dialog.setView(convertView);
                dialog.setCancelable(false);
                final AlertDialog alertDialog = dialog.create();



                ListView lv = convertView.findViewById(R.id.list_item);
                lv.setAdapter(paymentAdapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(listPayment.get(position).getIschecked()) listPayment.get(position).setIschecked(false);
                        else listPayment.get(position).setIschecked(true);
                        paymentAdapter.notifyDataSetChanged();

                    }
                });

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
                        alertDialog.dismiss();

                        final ArrayList<Payment> add_Payment = new ArrayList<>();
                        for(int i = 0; i< listPayment.size(); i++){
                            if(listPayment.get(i).getIschecked()){
                                add_Payment.add(listPayment.get(i));
                            }
                        }
                        listPaymentAdapter = new ListPaymentDelAdapter(ConvenienceStoreEditActivity.this, add_Payment);
                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ConvenienceStoreEditActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        listpayment.setLayoutManager(horizontalLayoutManager);
                        listpayment.setAdapter(listPaymentAdapter);

                        listPaymentAdapter.setClickListener(new ItemClick() {
                            @Override
                            public void Click(View view, int position) {
                                listPayment.get(add_Payment.get(position).getId()-1).setIschecked(false);
                                add_Payment.remove(position);
                                listPaymentAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                });
                alertDialog.show();
            }
        });

        listPaymentAdapter.setClickListener(new ItemClick() {
            @Override
            public void Click(View view, int position) {
                listPayment.get(addPayment.get(position).getId()-1).setIschecked(false);
                addPayment.remove(position);
                listPaymentAdapter.notifyDataSetChanged();
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
        tvTitle.setText(getResources().getText(R.string.text_edit));
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

    public void setDatatag(){
        vendingMachine = getResources().getText(R.string.vd).toString();
        postBox = getResources().getText(R.string.pb).toString();
        coffeeShop = getResources().getText(R.string.cf).toString();
        convenienceStore = getResources().getText(R.string.cs).toString();
        superMarket = getResources().getText(R.string.sm).toString();

    }

    public void setAddTagDialog(){
        setDatatag();

        ArrayList<Options> list_op = new ArrayList<>();
        list_op.add(new Options(R.drawable.vending, vendingMachine,false));
        list_op.add(new Options(R.drawable.post, postBox,false));
        list_op.add(new Options(R.drawable.coffee, coffeeShop,false));
        list_op.add(new Options(R.drawable.store, convenienceStore,false));
        list_op.add(new Options(R.drawable.spmk, superMarket,false));

        final OptionsAdapter optionsAdapter = new OptionsAdapter(this,list_op);

        btnTag = findViewById(R.id.btn_add_tag);
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(ConvenienceStoreEditActivity.this);
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

    static class PostData extends AsyncTask<String,Void,String> {

        ArrayList<File> list_file;
        String type;String add;String memo;String lat;String lng,lang,phone,name;
        String send,receive,service,payment;
        ArrayList<Integer> listTag;

        public PostData(ArrayList<File> list_file, String type, String add, String memo, String lat, String lng, String lang,
                        String phone, String name, String send, String receive, String service, String payment,ArrayList<Integer> listTag) {
            this.list_file = list_file;
            this.type = type;
            this.add = add;
            this.memo = memo;
            this.lat = lat;
            this.lng = lng;
            this.lang = lang;
            this.phone = phone;
            this.send =send;
            this.receive = receive;
            this.service = service;
            this.payment = payment;
            this.name = name;
            this.listTag = listTag;
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = null;

            String strTag = "";
            for(int i=0;i<listTag.size();i++){
                if(i==listTag.size()-1){
                    strTag = strTag + String.valueOf(listTag.get(i));
                }
                else{
                    strTag = strTag + String.valueOf(listTag.get(i)) +".";
                }
            }

            if(list_file.size()>0){
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM)
                        .addFormDataPart("name",type)
                        .addFormDataPart("address",add)
                        .addFormDataPart("memo",memo)
                        .addFormDataPart("lat",lat)
                        .addFormDataPart("long",lng)
                        .addFormDataPart("category_id","4")
                        .addFormDataPart("lang",lang)
                        .addFormDataPart("phone",phone)
                        .addFormDataPart("name",name)
                        .addFormDataPart("allow_send_items",send)
                        .addFormDataPart("allow_receive_items",receive)
                        .addFormDataPart("paymentMethod",payment)
                        .addFormDataPart("tags",strTag)
                        .addFormDataPart("businessHour","")
                        .addFormDataPart("services",service);




                for(int i=0;i<list_file.size();i++){
                    builder.addFormDataPart("imagesAdd[]","ConvenienceStore_"+String.valueOf(i)+System.currentTimeMillis()+".png",RequestBody.create(MediaType.parse("image/png"),list_file.get(i)));
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
                        .addFormDataPart("category_id","4")
                        .addFormDataPart("lang",lang)
                        .addFormDataPart("phone",phone)
                        .addFormDataPart("name",name)
                        .addFormDataPart("allow_send_items",send)
                        .addFormDataPart("allow_receive_items",receive)
                        .addFormDataPart("paymentMethod",payment)
                        .addFormDataPart("services",service)
                        .addFormDataPart("businessHour","")
                        .addFormDataPart("tags",strTag)
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
        }
    }

}
