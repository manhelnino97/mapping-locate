package com.example.manhe.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.manhe.search.model.Country;
import com.example.manhe.search.activity.MainActivity;
import com.example.manhe.search.R;

import java.util.List;

public class CountryAdapter extends ArrayAdapter<Country> {

    private Context context;
    int layout;
    List<Country> src;

    public CountryAdapter(MainActivity context, int layout , List<Country> src){
        super(context, layout,src);
        this.context =context;
        this.layout =layout;
        this.src = src;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        return getcustomView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position,View convertView, ViewGroup parent){
        return getcustomView(position,convertView,parent);
    }
    private View getcustomView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.country_spinner_layout,null);}

        ImageView img = convertView.findViewById(R.id.img);
        img.setImageResource(src.get(position).getSrc());
        return convertView;
    }

}
