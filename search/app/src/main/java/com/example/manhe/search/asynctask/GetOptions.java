package com.example.manhe.search.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.manhe.search.R;
import com.example.manhe.search.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetOptions extends AsyncTask<String,Void,String> {
        public interface interfaceGetOptions {
            void  getData(ArrayList<Category> data);
        }
        public interfaceGetOptions getData;
        public GetOptions(interfaceGetOptions getOptions) {
            this.getData = getOptions;
        }
        private ArrayList<Category> listop = new ArrayList<>();
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
            Log.e("cmma",s);
            if(s!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.getString("code").equals("S200")){
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("body"));
                        for (int i =0;i<jsonArray.length();i++) {
                            JSONObject category = jsonArray.getJSONObject(i);
                            if(category.getString("type").equals("0")){
                                Category op = new Category(R.drawable.ic_home_active,category.getString("id"),"",category.getString("name"),category.getString("name_vi"),category.getString("name_en"),category.getString("name_cn"),category.getString("name_es"),category.getString("name_ko"),category.getString("type"));
                                listop.add(op);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getData.getData(listop);
            }
            else {
                getData.getData(listop);
            }
            super.onPostExecute(s);
        }
}
