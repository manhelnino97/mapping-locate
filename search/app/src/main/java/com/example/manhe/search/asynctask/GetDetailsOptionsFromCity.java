package com.example.manhe.search.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.manhe.search.model.MarkerDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetDetailsOptionsFromCity extends AsyncTask<String,Void,String> {
    public interface getDataFromCity {
        void getData(ArrayList<MarkerDetails> data);
    }
    public getDataFromCity getData;
    public GetDetailsOptionsFromCity(getDataFromCity getcity) {
        this.getData = getcity;
    }
    private ArrayList<MarkerDetails> list = new ArrayList<>();
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().build();


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
                    for (int i =0;i<jsonArray.length();i++) {
                        JSONObject markDetails = jsonArray.getJSONObject(i);
                        Log.e("cmm",markDetails.toString());
                        list.add(new MarkerDetails(markDetails.getString("type"),markDetails.getString("lat"),markDetails.getString("long"),
                                markDetails.getString("name"),markDetails.getString("name_vi"),markDetails.getString("name_en"),markDetails.getString("name_es"),markDetails.getString("name_cn"),markDetails.getString("name_ko"),
                                markDetails.getString("address"),markDetails.getString("address_vi"),markDetails.getString("address_en"),markDetails.getString("address_es"),markDetails.getString("address_cn"),markDetails.getString("address_ko"),
                                markDetails.getString("id")));
                    }
                    getData.getData(list);
                }
                else if(jsonObject.getString("code").equals("E404")){
                    getData.getData(list);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else getData.getData(null);

        super.onPostExecute(s);
    }
}
