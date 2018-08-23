package com.example.manhe.search.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manhe.search.activity.ConvenienceStoreCreateActivity;
import com.example.manhe.search.activity.ConvenienceStoreEditActivity;
import com.example.manhe.search.model.Service;
import com.example.manhe.search.R;

import java.util.ArrayList;

public class ServiceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Service> list_service;

    public ServiceAdapter(Context context, ArrayList<Service> list_service) {
        this.context = context;
        this.list_service = list_service;
    }


    @Override
    public int getCount() {
        return list_service.size();
    }

    @Override
    public Service getItem(int position) {
        return list_service.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView img;
        TextView txt;
        CheckBox cb;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder =null;
        if(convertView!=null)
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(context.getClass().equals(ConvenienceStoreCreateActivity.class) || context.getClass().equals(ConvenienceStoreEditActivity.class)){
                assert inflater != null;
                convertView = inflater.inflate(R.layout.service_cs,null);
                Log.e("dmmm","1");
            }
            else{
                assert inflater != null;
                convertView = inflater.inflate(R.layout.service_sm,null);
                Log.e("dmmm","2");
            }
            viewHolder = new ViewHolder();
            viewHolder.img = convertView.findViewById(R.id.imageView);
            viewHolder.txt = convertView.findViewById(R.id.textView);
            viewHolder.cb = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        }


        viewHolder.img.setImageResource(list_service.get(position).getIcon());
        viewHolder.txt.setText(list_service.get(position).getName());

        viewHolder.cb.setHighlightColor(Color.parseColor("#30a40d"));

        if(list_service.get(position).getIschecked()) viewHolder.cb.setChecked(true);
        else viewHolder.cb.setChecked(false);

        return convertView;
    }
}
