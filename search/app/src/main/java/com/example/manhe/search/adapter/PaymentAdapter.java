package com.example.manhe.search.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manhe.search.R;
import com.example.manhe.search.model.Payment;

import java.util.ArrayList;

public class PaymentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Payment> list_payment;

    public PaymentAdapter(Context context, ArrayList<Payment> list_service) {
        this.context = context;
        this.list_payment = list_service;
    }


    @Override
    public int getCount() {
        return list_payment.size();
    }

    @Override
    public Payment getItem(int position) {
        return list_payment.get(position);
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
            convertView = inflater.inflate(R.layout.service_cs,null);
            viewHolder = new ViewHolder();
            viewHolder.img = convertView.findViewById(R.id.imageView);
            viewHolder.txt = convertView.findViewById(R.id.textView);
            viewHolder.cb = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        }


        viewHolder.img.setImageResource(list_payment.get(position).getIcon());
        viewHolder.txt.setText(list_payment.get(position).getName());

        if(list_payment.get(position).getIschecked()) viewHolder.cb.setChecked(true);
        else viewHolder.cb.setChecked(false);

        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_payment.get(position).getIschecked()) list_payment.get(position).setIschecked(false);
                else list_payment.get(position).setIschecked(true);
            }
        });




        return convertView;
    }
}