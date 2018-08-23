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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhe.search.R;
import com.example.manhe.search.adapter.BitmapAdapter;
import com.example.manhe.search.adapter.OptionsAdapter;
import com.example.manhe.search.adapter.TagDelAdapter;
import com.example.manhe.search.model.Image;
import com.example.manhe.search.model.Options;
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

public class VendingMachineEditAcitvity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    TextView tvTitle, tvtype;
    EditText edtnote,edtadd;
    RelativeLayout btnEdit;
    String type,note,lang,idd,token,lat,lng, addLocation,add, latLocation, lngLocation;
    ArrayList<Image> images = new ArrayList<>();
    ArrayList<File> listFile = new ArrayList<>();
    BitmapAdapter bitmapAdapter;
    ImageButton btnAddimg,btn_tag,btn_left,btn_right;
    private AlertDialog.Builder dialog;
    String vendingMachine = null, postBox = null, coffeeShop = null, convenienceStore = null, superMarket = null;
    RecyclerView addtag;
    TagDelAdapter tagAdapter;
    ArrayList<Integer> listTag = new ArrayList<>();
    ViewPager viewPager;
    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vm_edit);

        btnEdit = findViewById(R.id.btn_edit);
        addtag = findViewById(R.id.tag);
        tvtype = findViewById(R.id.tvtype);
        edtadd = findViewById(R.id.edtadd);
        edtnote = findViewById(R.id.memo);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        addLocation = bundle.getString("addLocation");
        add = bundle.getString("add");
        note = bundle.getString("note");
        lang = bundle.getString("lang");
        idd = bundle.getString("idd");
        token = bundle.getString("token");
        lat = bundle.getString("lat");
        lng = bundle.getString("lng");
        latLocation = bundle.getString("latLocation");
        lngLocation = bundle.getString("lngLocation");
        images = bundle.getParcelableArrayList("bitmaps");
        listTag = bundle.getIntegerArrayList("listtag");

        setImagePager();
        setActionBar();

        TextView tvprivacy =findViewById(R.id.privacy);
        tvprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPrivacy(VendingMachineEditAcitvity.this);

            }
        });



        btnAddimg = findViewById(R.id.btn_img);
        btnAddimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.optionSelectImage(VendingMachineEditAcitvity.this,view);
            }
        });


        tagAdapter = new TagDelAdapter(this, listTag);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(VendingMachineEditAcitvity.this, LinearLayoutManager.HORIZONTAL, false);
        addtag.setLayoutManager(horizontalLayoutManager);
        addtag.setAdapter(tagAdapter);


        edtadd.setText(add);
        if(!note.equals("null")) edtnote.setText(note);




        RelativeLayout btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add.equals(edtadd.getText().toString()) && note.equals(edtnote.getText().toString())){
                    onBackPressed();
                }
                else {
                    Utils.showAlertDialogCancelInputData(VendingMachineEditAcitvity.this);
                }
            }
        });

        if(Math.abs(Double.valueOf(lat)-Double.valueOf(latLocation))<=0.0018086 && Math.abs(Double.valueOf(lng)-Double.valueOf(lngLocation))<=0.0018018){
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.isConnected(VendingMachineEditAcitvity.this)){
                        if(!edtadd.getText().toString().equals("")){
                            new PostData(listFile, tvtype.getText().toString(),edtadd.getText().toString(),
                                    edtnote.getText().toString(),lat,lng,lang, listTag).execute("http://192.168.1.67:8080/api/editVendingMachine/"+idd+"?token="+token);
                            Intent intent = new Intent(VendingMachineEditAcitvity.this,VendingMachineDetailActivity.class);
                            Bundle b = new Bundle();
                            b.putString("token",token);
                            b.putString("idd",idd);
                            b.putString("lang",lang);
                            b.putString("latLocation", latLocation);
                            b.putString("lngLocation", lngLocation);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                        else{
                            final AlertDialog alertDialog = new AlertDialog.Builder(VendingMachineEditAcitvity.this).create();
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
                        final AlertDialog alertDialog = new AlertDialog.Builder(VendingMachineEditAcitvity.this).create();
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

        ImageButton btnGetLocation = findViewById(R.id.getLocation);
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtadd.setText(addLocation);
            }
        });


        ScrollView sc = findViewById(R.id.scroll);
        sc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftKeyboard(VendingMachineEditAcitvity.this);
                return false;
            }
        });

        setAddTagDialog();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
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

    static class PostData extends AsyncTask<String,Void,String> {

        ArrayList<File> list_file;
        String type;String add;String memo;String lat;String lng,lang;
        ArrayList<Integer> listTag;

        public PostData(ArrayList<File> list_file, String type, String add, String memo, String lat, String lng, String lang,
         ArrayList<Integer> listTag) {
            this.list_file = list_file;
            this.type = type;
            this.add = add;
            this.memo = memo;
            this.lat = lat;
            this.lng = lng;
            this.lang = lang;
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
                        .addFormDataPart("category_id","1")
                        .addFormDataPart("tags",strTag)
                        .addFormDataPart("lang",lang);

                        for(int i=0;i<list_file.size();i++){
                            builder.addFormDataPart("imagesAdd[]","VendingMachine_"+String.valueOf(i)+System.currentTimeMillis()+".png",RequestBody.create(MediaType.parse("image/png"),list_file.get(i)));
                        }
                        requestBody = builder.build();
            }
            else{
                requestBody = new MultipartBody.Builder()
                        .addFormDataPart("name",type)
                        .addFormDataPart("address",add)
                        .addFormDataPart("memo",memo)
                        .addFormDataPart("lat",lat)
                        .addFormDataPart("long",lng)
                        .addFormDataPart("category_id","1")
                        .addFormDataPart("lang",lang)
                        .addFormDataPart("tags",strTag)
                        .setType(MultipartBody.FORM)
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

        btn_tag = findViewById(R.id.btn_add_tag);
        btn_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(VendingMachineEditAcitvity.this);
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
        tvTitle.setText(getResources().getText(R.string.text_edit));

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9000")));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
