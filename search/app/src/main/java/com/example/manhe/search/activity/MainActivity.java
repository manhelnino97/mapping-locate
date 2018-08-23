package com.example.manhe.search.activity;

import android.Manifest;
import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.content.res.Configuration;
        import android.content.res.Resources;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.PorterDuff;
        import android.graphics.drawable.Drawable;
        import android.location.Address;
        import android.location.Geocoder;
import android.location.Location;
        import android.location.LocationManager;
import android.os.Bundle;
        import android.support.annotation.DrawableRes;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.os.ConfigurationCompat;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.PopupMenu;
        import android.support.v7.widget.RecyclerView;
import android.util.Log;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
        import android.widget.AutoCompleteTextView;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.provider.Settings.Secure;

import com.example.manhe.search.Interface.MenuBarItemClick;
import com.example.manhe.search.SQLiteDatabase.MyDatabase;
import com.example.manhe.search.R;
import com.example.manhe.search.adapter.HorizontalAdapter;
import com.example.manhe.search.adapter.OptionsAdapter;
import com.example.manhe.search.adapter.PlaceAutocompleteAdapter;
import com.example.manhe.search.asynctask.GetOptions;
import com.example.manhe.search.asynctask.GetDetailsOptionsFromCategoryId;
import com.example.manhe.search.asynctask.GetDetailsOptionsFromCity;
import com.example.manhe.search.asynctask.GetDetailsOptionsFromLocation;
import com.example.manhe.search.model.MarkerDetails;
import com.example.manhe.search.model.Category;
import com.example.manhe.search.model.Options;
import com.example.manhe.search.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.PendingResult;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.location.places.AutocompletePrediction;
        import com.google.android.gms.location.places.Place;
        import com.google.android.gms.location.places.PlaceBuffer;
        import com.google.android.gms.location.places.Places;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;

        import java.io.IOException;
        import java.lang.reflect.Field;
        import java.lang.reflect.Method;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {


    private static final String TAG = "MainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 14f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));



    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageButton mGps,mAdd;
    RecyclerView twoWayView;
    ImageView btnClear, imgCountry;
    RelativeLayout layout_parent;
    LinearLayout popupCountryFlag;

    //vars
    int searchStatus =0;
    String deviceId;
    String token, latLocation, lngLocation, categotyId,addLocation,idd;
    ArrayList<MarkerDetails> detailsArrayList1 = new ArrayList<>();
    ArrayList<MarkerDetails> detailsArrayList2 = new ArrayList<>();
    boolean GpsStatus;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    HorizontalAdapter horizontalAdapter;
    ArrayList<MarkerDetails> detailsArrayList;
    MyDatabase myDatabase = new MyDatabase(this);
    Double latFind,lngFind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnClear = findViewById(R.id.btn_clear);
        popupCountryFlag = findViewById(R.id.spinner);
        imgCountry = findViewById(R.id.img_country);
        twoWayView = findViewById(R.id.include);
        mSearchText = findViewById(R.id.input_search);
        mGps = findViewById(R.id.ic_gps);
        mAdd = findViewById(R.id.ic_add);


        Locale current = ConfigurationCompat.getLocales(getResources().getConfiguration()).get(0);
        switch (current.toString()) {
            case "vi_VN":
                imgCountry.setImageResource(R.drawable.ic_vi);
                break;
            case "ja_JP":
                imgCountry.setImageResource(R.drawable.ic_jp);
                break;
            case "en_GB":
                imgCountry.setImageResource(R.drawable.ic_en);
                break;
            case "zh_CN":
                imgCountry.setImageResource(R.drawable.ic_china);
                break;
            case "ko_KR":
                imgCountry.setImageResource(R.drawable.ic_kr);
                break;
            case "es_ES":
                imgCountry.setImageResource(R.drawable.ic_sp);
                break;
        }
        deviceId = Secure.getString(getContentResolver(),Secure.ANDROID_ID);

        if(myDatabase.getAllMarker().size()>0) myDatabase.deleteAll();
        if(myDatabase.getAllSearchHistory().size()>0) myDatabase.deleteAllSH();


        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        token = bundle.getString("token");
        latLocation = bundle.getString("latLocation");
        lngLocation = bundle.getString("lngLocation");
        categotyId = bundle.getString("cate");
        idd = bundle.getString("idd");


        if(categotyId !=null && latLocation!=null && lngLocation!=null){
            if(idd!=null){
                new GetDetailsOptionsFromCategoryId(new GetDetailsOptionsFromCategoryId.GetData() {
                    @Override
                    public void getData(ArrayList<MarkerDetails> data) {
                        if (data.size() > 0) {
                            markOptions(Double.valueOf(latLocation), Double.valueOf(lngLocation), Integer.parseInt(categotyId) - 1, idd, "", "");
                        } else {
                            Utils.messageError(MainActivity.this);
                        }
                    }
                }).execute("http://192.168.1.67:8080/api/searchUtilitiesWithLocation/"+ latLocation +"/"+ lngLocation +"/"+ categotyId +"?token="+token);
            }
            else{
                new GetDetailsOptionsFromCategoryId(new GetDetailsOptionsFromCategoryId.GetData() {
                    @Override
                    public void getData(ArrayList<MarkerDetails> data) {
                        if(data.size()>0){
                            for(int i=0;i<data.size();i++){
                                markOptions(Double.valueOf(data.get(i).getLat()),Double.valueOf(data.get(i).getLng()),Integer.parseInt(data.get(i).getCate())-1,data.get(i).getId_d(),data.get(i).getName_vi(),data.get(i).getAdd_vi());
                                myDatabase.addMarker(data.get(i));
                            }
                        }
                        else {
                            Utils.messageError(MainActivity.this);
                        }
                    }
                }).execute("http://192.168.1.67:8080/api/searchUtilitiesWithLocation/"+ latLocation +"/"+ lngLocation +"/"+ categotyId +"?token="+token);
            }
        }
        detailsArrayList = myDatabase.getAllMarker();
        Log.e("cmm",String.valueOf(detailsArrayList.size()));



        mSearchText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchText.setCursorVisible(true);
                    btnClear.setImageResource(R.drawable.ic_clear);
                    ArrayList<String> list_searchHistory = myDatabase.getAllSearchHistory();
                    if(list_searchHistory.size()>0){
                        PopupMenu pm = new PopupMenu(MainActivity.this, v);
                        if(list_searchHistory.size()<6){
                            for(int i=0;i<list_searchHistory.size();i++){
                                pm.getMenu().add(list_searchHistory.get(i));
                            }
                        }
                        else{
                            for(int i=0;i<5;i++){
                                pm.getMenu().add(list_searchHistory.get(list_searchHistory.size()-1-i));
                            }
                        }
                        pm.getMenuInflater().inflate(R.menu.menu_popup, pm.getMenu());
                        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                mSearchText.setText(item.getTitle());
                                return false;
                            }
                        });
                        pm.show();
                    }

                }
        });

        GPSStatus();
        if (GpsStatus&& Utils.isConnected(MainActivity.this))
        {
            getLocationPermission();
            getDataFromServer();
        }
        else {
            showAlertDialogExit();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocationStart();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mSearchText.setCursorVisible(false);
                    btnClear.setImageDrawable(null);
                    Utils.hideSoftKeyboard(MainActivity.this);
                }
            });

        }
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId,String name,String add) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_layout, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.img_marker);
        TextView tv1 = customMarkerView.findViewById(R.id.txt_name);
        TextView tv2 = customMarkerView.findViewById(R.id.txt_add);
        //tv1.setText(name);
        // tv2.setText(addLocation);
        if(resId == R.drawable.icon_vd){
            tv1.setTextColor(Color.parseColor("#ff9000"));
            tv2.setTextColor(Color.parseColor("#ff9000"));
        }
        else if(resId == R.drawable.icon_pb){
            tv1.setTextColor(Color.parseColor("#fb3680"));
            tv2.setTextColor(Color.parseColor("#fb3680"));
        }
        else if(resId == R.drawable.icon_cf){
            tv1.setTextColor(Color.parseColor("#6c4109"));
            tv2.setTextColor(Color.parseColor("#6c4109"));
        }
        else if(resId == R.drawable.icon_cs){
            tv1.setTextColor(Color.parseColor("#30a40d"));
            tv2.setTextColor(Color.parseColor("#30a40d"));
        }
        else if(resId == R.drawable.icon_sm){
            tv1.setTextColor(Color.parseColor("#015d46"));
            tv2.setTextColor(Color.parseColor("#015d46"));
        }

        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public void markOptions(double lat, double lng, int p, String idd, String name, String add) {
        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions option = new MarkerOptions();
        option.position(latLng);
        switch (p){
            case 0:{
                option.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.icon_vd,name,add)));
                option.snippet("vd"+idd);
                break;
            }
            case 1:{
                option.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.icon_pb,name,add)));
                option.snippet("pb"+idd);
                break;
            }
            case 2:{
                option.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.icon_cf,name,add)));
                option.snippet("cf"+idd);
                break;
            }
            case 3:{
                option.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.icon_cs,name,add)));
                option.snippet("cs"+idd);
                break;
            }
            case 4:{
                option.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.icon_sm,name,add)));
                option.snippet("sm"+idd);
                break;
            }
        }
        Marker currentMarker = mMap.addMarker(option);
        currentMarker.showInfoWindow();
    }

    public void showAlertDialogExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.internet_and_gps));
        builder.setMessage(getResources().getString(R.string.restart_app));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this,LoadingActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void getDataFromServer(){
        new GetOptions(new GetOptions.interfaceGetOptions() {

            @Override
            public void getData(final ArrayList<Category> data) {
                if (data.size() > 0) {
                    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            final Location currentLocation = (Location) task.getResult();
                            String lat_f = "";
                            String lng_f = "";
                            if(currentLocation!=null){
                                lat_f = String.valueOf(currentLocation.getLatitude());
                                lng_f = String.valueOf(currentLocation.getLongitude());
                            }
                            else{
                                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                            }
                            final String lang = Locale.getDefault().toString().substring(0, 2);

                            ArrayList<ArrayList<Category>> list_category = new ArrayList<ArrayList<Category>>();
                            ArrayList<Category> list_temp = new ArrayList<>();
                            for (int i = 0; i < data.size(); i++) {
                                list_temp.add(data.get(i));
                                if (list_temp.size() == 5) {
                                    list_category.add(list_temp);
                                }
                            }

                            horizontalAdapter = new HorizontalAdapter(MainActivity.this, list_category, lang);
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            twoWayView.setLayoutManager(horizontalLayoutManager);
                            twoWayView.setAdapter(horizontalAdapter);

                            final int[] searchInCityStatus = new int[data.size()];
                            final int[] searchFromAdrressStatus = new int[data.size()];
                            final int[] searchFromYourLocationStatus = new int[data.size()];

                            for (int i = 0; i < data.size(); i++) {
                                searchInCityStatus[i] = 0;
                                searchFromAdrressStatus[i] = 0;
                                searchFromYourLocationStatus[i] = 0;
                            }


                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String city = "";
                            String add = "";
                            if(addresses!=null){
                                city = addresses.get(0).getAdminArea();
                                add = addresses.get(0).getAddressLine(0);
                                addLocation = add;
                            }
                            else {
                                Utils.messageGeocoderError(MainActivity.this);
                            }
                            final String finalCity = city;
                            final String finalLat_f = lat_f;
                            final String finalLng_f = lng_f;
                            horizontalAdapter.setClickListener(new MenuBarItemClick() {
                                @Override
                                public void Click(View view, final int position) {
                                    Log.e("abcd",finalLat_f);
                                    mSearchText.setCursorVisible(false);
                                    btnClear.setImageDrawable(null);
                                    if (searchStatus == 0) {
                                        getDeviceLocationStart();
                                        if (searchInCityStatus[position] == 0) {
                                            horizontalAdapter.status[position] = 1;
                                            horizontalAdapter.cate[position] = position;
                                            horizontalAdapter.notifyDataSetChanged();
                                            final String lang = Locale.getDefault().toString().substring(0, 2);
                                            new GetDetailsOptionsFromCity(new GetDetailsOptionsFromCity.getDataFromCity() {
                                                @Override
                                                public void getData(ArrayList<MarkerDetails> data_city) {
                                                    if(data_city!=null) {
                                                        if (data_city.size() > 0) {
                                                            if (detailsArrayList.size() > 0) {
                                                                for (int i = 0; i < data_city.size(); i++) {
                                                                    for (int j = 0; j < detailsArrayList.size(); j++) {
                                                                        if (!data_city.get(i).getId_d().equals(detailsArrayList.get(j).getId_d())) {
                                                                            markOptions(Double.valueOf(data_city.get(i).getLat()), Double.valueOf(data_city.get(i).getLng()), position, data_city.get(i).getId_d(), "", "");
                                                                            myDatabase.addMarker(data_city.get(i));
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int i = 0; i < data_city.size(); i++) {
                                                                    Log.e("cmm", "cmm");
                                                                    markOptions(Double.valueOf(data_city.get(i).getLat()), Double.valueOf(data_city.get(i).getLng()), position, data_city.get(i).getId_d(), "", "");
                                                                    myDatabase.addMarker(data_city.get(i));
                                                                }
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        Utils.messageError(MainActivity.this);
                                                    }
                                                }
                                            }).execute("http://192.168.1.67:8080/api/searchUtilitiesInCity/" + finalCity + "/" + String.valueOf(position + 1) + "/" + lang + "/" + finalLat_f + "/" + finalLng_f + "/" + deviceId + "/?token=" + token);
                                            searchInCityStatus[position] = 1;
                                            Log.e("ghenha", String.valueOf(myDatabase.getAllMarker().size()));
                                        } else {
                                            mMap.clear();
                                            horizontalAdapter.status[position] = 0;
                                            horizontalAdapter.cate[position] = -1;
                                            horizontalAdapter.notifyDataSetChanged();
                                            Log.e("ghenha", String.valueOf(myDatabase.getAllMarker().size()));
                                            ArrayList<MarkerDetails> list_marker = myDatabase.getAllMarker();
                                            for (int i = 0; i < list_marker.size(); i++) {
                                                if (!list_marker.get(i).getCate().equals(String.valueOf(position + 1))) {
                                                    markOptions(Double.valueOf(list_marker.get(i).getLat()), Double.valueOf(list_marker.get(i).getLng()), Integer.parseInt(list_marker.get(i).getCate()) - 1, list_marker.get(i).getId_d(), list_marker.get(i).getName_vi(), list_marker.get(i).getAdd_vi());
                                                }
                                            }
                                            myDatabase.deleteCate(position + 1);
                                            Log.e("ghenha", String.valueOf(myDatabase.getAllMarker().size()));
                                            searchInCityStatus[position] = 0;
                                        }
                                    } else if (searchStatus == 1) {

                                        if (searchFromAdrressStatus[position] == 0) {
                                            mMap.clear();
                                            LatLng latLng = new LatLng(latFind,lngFind);
                                            MarkerOptions options = new MarkerOptions()
                                                    .position(latLng)
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                                            mMap.addMarker(options);
                                            horizontalAdapter.status[position] = 0;
                                            horizontalAdapter.cate[position] = -1;
                                            horizontalAdapter.notifyDataSetChanged();
                                            ArrayList<MarkerDetails> list_marker = myDatabase.getAllMarker();
                                            for (int i = 0; i < list_marker.size(); i++) {
                                                if (!list_marker.get(i).getCate().equals(String.valueOf(position + 1))) {
                                                    markOptions(Double.valueOf(list_marker.get(i).getLat()), Double.valueOf(list_marker.get(i).getLng()), Integer.parseInt(list_marker.get(i).getCate()) - 1, list_marker.get(i).getId_d(), list_marker.get(i).getName_vi(), list_marker.get(i).getAdd_vi());
                                                } else {
                                                    detailsArrayList2.add(list_marker.get(i));
                                                }

                                            }
                                            myDatabase.deleteCate(position + 1);
                                            searchFromAdrressStatus[position] = 1;
                                        } else {
                                            horizontalAdapter.status[position] = 1;
                                            horizontalAdapter.cate[position] = position;
                                            horizontalAdapter.notifyDataSetChanged();
                                            for (int i = 0; i < detailsArrayList2.size(); i++) {
                                                if (Integer.parseInt(detailsArrayList2.get(i).getCate()) - 1 == position) {
                                                    markOptions(Double.valueOf(detailsArrayList2.get(i).getLat()), Double.valueOf(detailsArrayList2.get(i).getLng()), position, detailsArrayList2.get(i).getId_d(), detailsArrayList2.get(i).getName_vi(), detailsArrayList2.get(i).getAdd_vi());
                                                }
                                            }
                                            int i = detailsArrayList2.size() - 1;
                                            while (i > -1) {
                                                if (detailsArrayList2.get(i).getCate().equals(String.valueOf(position + 1))) {
                                                    myDatabase.addMarker(detailsArrayList2.get(i));
                                                    detailsArrayList2.remove(i);
                                                    Log.e("cmm", "abc");
                                                }
                                                i--;
                                            }
                                            searchFromAdrressStatus[position] = 0;
                                        }
                                    } else if (searchStatus == 2) {
                                        if (searchFromYourLocationStatus[position] == 0) {
                                            mMap.clear();
                                            horizontalAdapter.status[position] = 0;
                                            horizontalAdapter.cate[position] = -1;
                                            horizontalAdapter.notifyDataSetChanged();
                                            ArrayList<MarkerDetails> list_marker = myDatabase.getAllMarker();
                                            for (int i = 0; i < list_marker.size(); i++) {
                                                if (!list_marker.get(i).getCate().equals(String.valueOf(position + 1))) {
                                                    markOptions(Double.valueOf(list_marker.get(i).getLat()), Double.valueOf(list_marker.get(i).getLng()), Integer.parseInt(list_marker.get(i).getCate()) - 1, list_marker.get(i).getId_d(), list_marker.get(i).getName_vi(), list_marker.get(i).getAdd_vi());
                                                } else {
                                                    detailsArrayList1.add(list_marker.get(i));
                                                }

                                            }
                                            myDatabase.deleteCate(position + 1);
                                            Log.e("cmm", detailsArrayList1.toString());
                                            searchFromYourLocationStatus[position] = 1;
                                        } else {
                                            horizontalAdapter.status[position] = 1;
                                            horizontalAdapter.cate[position] = position;
                                            horizontalAdapter.notifyDataSetChanged();
                                            for (int i = 0; i < detailsArrayList1.size(); i++) {
                                                if (Integer.parseInt(detailsArrayList1.get(i).getCate()) - 1 == position) {
                                                    markOptions(Double.valueOf(detailsArrayList1.get(i).getLat()), Double.valueOf(detailsArrayList1.get(i).getLng()), position, detailsArrayList1.get(i).getId_d(), detailsArrayList1.get(i).getName_vi(), detailsArrayList1.get(i).getAdd_vi());
                                                }
                                            }
                                            int i = detailsArrayList1.size() - 1;
                                            while (i > -1) {
                                                if (detailsArrayList1.get(i).getCate().equals(String.valueOf(position + 1))) {
                                                    myDatabase.addMarker(detailsArrayList1.get(i));
                                                    detailsArrayList1.remove(i);
                                                    Log.e("cmm", "abc");
                                                }
                                                i--;
                                            }
                                            Log.e("cmm", detailsArrayList1.toString());
                                            searchFromYourLocationStatus[position] = 0;
                                        }
                                    }
                                }
                            });
                            final String finalAdd = add;
                            final String finalLat_f1 = lat_f;
                            final String finalLng_f1 = lng_f;
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    if (!marker.getSnippet().equals("find")) {
                                        String op = marker.getSnippet().substring(0, 2);
                                        String idd = marker.getSnippet().substring(2, marker.getSnippet().length());
                                        Bundle bundle = new Bundle();
                                        bundle.putString("idd", idd);
                                        bundle.putString("token", token);
                                        bundle.putString("lang", lang);
                                        bundle.putString("latLocation", finalLat_f1);
                                        bundle.putString("lngLocation", finalLng_f1);
                                        bundle.putString("addLocation", finalAdd);
                                        bundle.putString("deviceId",deviceId);
                                        Intent intent = null;
                                        switch (op) {
                                            case "vd":
                                                intent = new Intent(MainActivity.this, VendingMachineDetailActivity.class);
                                                break;
                                            case "pb":
                                                intent = new Intent(MainActivity.this, PostBoxDetailActivity.class);
                                                break;
                                            case "cf":
                                                intent = new Intent(MainActivity.this, CoffeeShopDetailActivity.class);
                                                break;
                                            case "cs":
                                                intent = new Intent(MainActivity.this, ConvenienceStoreDetailActivity.class);
                                                break;
                                            case "sm":
                                                intent = new Intent(MainActivity.this, SuperMarketDetailActivity.class);
                                                break;
                                        }
                                        assert intent != null;
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                    return true;
                                }
                            });

                            popupCountryFlag.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                                    popupMenu.getMenuInflater().inflate(R.menu.menu_country, popupMenu.getMenu());

                                    try {
                                        Field[] fields = popupMenu.getClass().getDeclaredFields();
                                        for (Field field : fields) {
                                            if ("mPopup".equals(field.getName())) {
                                                field.setAccessible(true);
                                                Object menuPopupHelper = field.get(popupMenu);
                                                Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                                                Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                                                setForceIcons.invoke(menuPopupHelper, true);
                                                break;
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    popupMenu.show();
                                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            if (item.getItemId() == R.id.vi) {
                                                Locale locale = new Locale("vi", "VN");
                                                setLocale(locale);
                                                Bundle b = new Bundle();
                                                b.putString("token", token);
                                                Intent it = new Intent(MainActivity.this, MainActivity.class);
                                                it.putExtras(b);
                                                startActivity(it);
                                            } else if (item.getItemId() == R.id.ja) {
                                                Locale locale = new Locale("ja", "JP");
                                                setLocale(locale);
                                                Bundle b = new Bundle();
                                                b.putString("token", token);
                                                Intent it = new Intent(MainActivity.this, MainActivity.class);
                                                it.putExtras(b);
                                                startActivity(it);
                                            } else if (item.getItemId() == R.id.en) {
                                                Locale locale = new Locale("en", "GB");
                                                setLocale(locale);
                                                Bundle b = new Bundle();
                                                b.putString("token", token);
                                                Intent it = new Intent(MainActivity.this, MainActivity.class);
                                                it.putExtras(b);
                                                startActivity(it);
                                            } else if (item.getItemId() == R.id.sp) {
                                                Locale locale = new Locale("es", "ES");
                                                setLocale(locale);
                                                Bundle b = new Bundle();
                                                b.putString("token", token);
                                                Intent it = new Intent(MainActivity.this, MainActivity.class);
                                                it.putExtras(b);
                                                startActivity(it);
                                            } else if (item.getItemId() == R.id.kr) {
                                                Locale locale = new Locale("ko", "KR");
                                                setLocale(locale);
                                                Bundle b = new Bundle();
                                                b.putString("token", token);
                                                Intent it = new Intent(MainActivity.this, MainActivity.class);
                                                it.putExtras(b);
                                                startActivity(it);
                                            } else if (item.getItemId() == R.id.cn) {
                                                Locale locale = new Locale("zh", "CN");
                                                setLocale(locale);
                                                Bundle b = new Bundle();
                                                b.putString("token", token);
                                                Intent it = new Intent(MainActivity.this, MainActivity.class);
                                                it.putExtras(b);
                                                startActivity(it);

                                            }
                                            return true;
                                        }
                                    });
                                }
                            });


                            final String lat = String.valueOf(currentLocation.getLatitude());
                            final String lng = String.valueOf(currentLocation.getLongitude());

                            int[] img = {R.drawable.vending, R.drawable.post, R.drawable.coffee, R.drawable.store, R.drawable.spmk};
                            ArrayList<Options> list_op = new ArrayList<>();
                            list_op.add(new Options(img[0], getResources().getString(R.string.vd), false));
                            list_op.add(new Options(img[1], getResources().getString(R.string.pb), false));
                            list_op.add(new Options(img[2], getResources().getString(R.string.cf), false));
                            list_op.add(new Options(img[3], getResources().getString(R.string.cs), false));
                            list_op.add(new Options(img[4], getResources().getString(R.string.sm), false));
                            final OptionsAdapter optionsAdapter = new OptionsAdapter(MainActivity.this, list_op);
                            final String finalAdd1 = add;
                            mAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder dialog;
                                    mSearchText.setCursorVisible(false);
                                    btnClear.setImageDrawable(null);
                                    Utils.hideSoftKeyboard(MainActivity.this);

                                    dialog = new AlertDialog.Builder(MainActivity.this);
                                    LayoutInflater inflater = getLayoutInflater();
                                    View convertView = inflater.inflate(R.layout.dialog_option_custom, null);
                                    dialog.setView(convertView);
                                    dialog.setTitle(getResources().getString(R.string.text_create));
                                    final AlertDialog alertDialog = dialog.create();
                                    alertDialog.show();
                                    ListView lv = convertView.findViewById(R.id.list_item);
                                    lv.setAdapter(optionsAdapter);
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("addLocation", finalAdd1);
                                            bundle.putString("lang", lang);
                                            bundle.putString("latLocation", lat);
                                            bundle.putString("lngLocation", lng);
                                            bundle.putString("token", token);
                                            bundle.putString("deviceId",deviceId);
                                            Intent intent = null;
                                            switch (position) {
                                                case 0:
                                                    intent = new Intent(MainActivity.this, VendingMachineCreateActivity.class);
                                                    break;
                                                case 1:
                                                    intent = new Intent(MainActivity.this, PostBoxCreateActivity.class);
                                                    break;
                                                case 2:
                                                    intent = new Intent(MainActivity.this, CoffeeShopCreateActivity.class);
                                                    break;
                                                case 3:
                                                    intent = new Intent(MainActivity.this, ConvenienceStoreCreateActivity.class);
                                                    break;
                                                case 4:
                                                    intent = new Intent(MainActivity.this, SuperMarketCreateActivity.class);
                                                    break;
                                            }
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            alertDialog.dismiss();
                                        }
                                    });

                                }
                            });

                        }
                    });
                } else{
                    Utils.messageExit(MainActivity.this);
                }
            }
        }).execute("http://192.168.1.67:8080/api/getAllCate?token="+token);
    }

    public void setLocale(Locale mLocale) {
        Locale.setDefault(mLocale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = mLocale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private void init(){
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });


        ImageView clear = findViewById(R.id.btn_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText.setText("");
                mMap.clear();
                for(int i=0;i<5;i++){
                    horizontalAdapter.status[i]=0;
                    horizontalAdapter.cate[i]=-1;
                }
                horizontalAdapter.notifyDataSetChanged();
                myDatabase.deleteAll();
                searchStatus =0;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchText.setCursorVisible(false);
                btnClear.setImageDrawable(null);
                mSearchText.setText(addLocation);
                Utils.hideSoftKeyboard(MainActivity.this);
                    mMap.clear();
                    getDeviceLocation();
            }
        });

    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            Log.d("gogogo", "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            //moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
            // address.getAddressLine(0));
        }
    }

    public void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location","");
                            String lat_f = String.valueOf(currentLocation.getLatitude());
                            String lng_f = String.valueOf(currentLocation.getLongitude());
                            searchStatus =2;
                            myDatabase.deleteAll();
                            Bundle b = getIntent().getExtras();
                            final String token = b.getString("token");
                            mMap.clear();

                            new GetDetailsOptionsFromLocation(new GetDetailsOptionsFromLocation.GetData() {
                                @Override
                                public void getData(final ArrayList<MarkerDetails> data) {
                                    if(data==null){
                                        Utils.messageError(MainActivity.this);
                                    }else{
                                        if(data.size()>0){
                                            for(int i=0;i<data.size();i++){
                                                markOptions(Double.valueOf(data.get(i).getLat()),Double.valueOf(data.get(i).getLng()),Integer.parseInt(data.get(i).getCate())-1,data.get(i).getId_d(),data.get(i).getName_vi(),data.get(i).getAdd_vi());
                                                myDatabase.addMarker(data.get(i));
                                            }
                                            for(int i=0;i<5;i++){
                                                horizontalAdapter.cate[i]=i;
                                                horizontalAdapter.status[i]=1;
                                            }
                                            horizontalAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }).execute("http://192.168.1.67:8080/api/getUtiilities/"+lat_f+"/"+lng_f+"?token="+token);
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    public void GPSStatus(){
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void getDeviceLocationStart(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    12,
                                    "My Location","");


                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title,String sinnpet){
        Log.d(TAG, "moveCamera: moving the camera to: latLocation: " + latLng.latitude + ", lngLocation: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .snippet(sinnpet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            mMap.addMarker(options);
        }



    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    /*
        --------------------------- google places API autocomplete suggestions -----------------
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Utils.hideSoftKeyboard(MainActivity.this);

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    public ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            String searchString = mSearchText.getText().toString();
            ArrayList<String> list_sh = myDatabase.getAllSearchHistory();
            int count = 0;
            if(list_sh.size()>0){
                for(int i=0;i<list_sh.size();i++){
                    if(searchString.equals(list_sh.get(i))){
                        count++;
                    }
                }
                if(count==0) myDatabase.addSH(searchString);
            }
            else myDatabase.addSH(searchString);
            final Place place = places.get(0);
            mMap.clear();

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, "","find");
            String lat_f, lng_f;
            lat_f = String.valueOf(place.getViewport().getCenter().latitude);
            lng_f = String.valueOf(place.getViewport().getCenter().longitude);
            latFind = place.getViewport().getCenter().latitude;
            lngFind = place.getViewport().getCenter().longitude;
            places.release();
            searchStatus =1;
            myDatabase.deleteAll();
            Bundle b = getIntent().getExtras();
            assert b != null;
            final String token = b.getString("token");
            new GetDetailsOptionsFromLocation(new GetDetailsOptionsFromLocation.GetData() {
                @Override
                public void getData(final ArrayList<MarkerDetails> data) {
                    if(data==null){
                        Utils.messageError(MainActivity.this);
                    }
                    else {
                        if (data.size() > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                markOptions(Double.valueOf(data.get(i).getLat()), Double.valueOf(data.get(i).getLng()), Integer.parseInt(data.get(i).getCate()) - 1, data.get(i).getId_d(), data.get(i).getName_vi(), data.get(i).getAdd_vi());
                                myDatabase.addMarker(data.get(i));
                            }
                            for (int i = 0; i < 5; i++) {
                                horizontalAdapter.cate[i] = i;
                                horizontalAdapter.status[i] = 1;
                            }
                            horizontalAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }).execute("http://192.168.1.67:8080/api/getUtiilities/"+lat_f+"/"+lng_f+"?token="+token);
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

}
