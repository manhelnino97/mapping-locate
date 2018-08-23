package com.example.manhe.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.manhe.search.R;
import com.example.manhe.search.model.Payment;

import java.util.ArrayList;

public class ListPaymentAdapter extends  RecyclerView.Adapter<ListPaymentAdapter.MyViewHolder> {

    Context context;
    ArrayList<Payment> paymentArrayList;


    public ListPaymentAdapter(Context context, ArrayList<Payment> paymentArrayList) {
        this.context = context;
        this.paymentArrayList =paymentArrayList;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        public MyViewHolder(View view) {
            super(view);
            img= view.findViewById(R.id.img_ic);


        }


    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_service, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if(paymentArrayList.get(position).getIschecked()){
            holder.img.setImageResource(paymentArrayList.get(position).getIcon());
        }

    }


    @Override
    public int getItemCount()
    {
        return paymentArrayList.size();
    }
}
