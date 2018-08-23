package com.example.manhe.search.asynctask;

import android.os.AsyncTask;

import com.example.manhe.search.model.MarkerDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetDetailsOptionsFromCategoryId extends AsyncTask<String,Void,String> {
    public interface GetData {
        void  getData(ArrayList<MarkerDetails> data);
    }
    public GetData getdata;
    public GetDetailsOptionsFromCategoryId(GetData getdata) {
        this.getdata = getdata;
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
                        JSONObject j = jsonArray.getJSONObject(i);
                        list.add(new MarkerDetails(j.getString("category_id"),j.getString("lat"),j.getString("long"),
                                j.getString("name"),j.getString("name_vi"),j.getString("name_en"),j.getString("name_es"),j.getString("name_cn"),j.getString("name_ko"),
                                j.getString("address"),j.getString("address_vi"),j.getString("address_en"),j.getString("address_es"),j.getString("address_cn"),j.getString("address_ko"),
                                j.getString("id")));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            getdata.getData(list);
        }
        else{
            getdata.getData(list);
        }
        super.onPostExecute(s);

    }
}