package com.example.manhe.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.manhe.search.R;
import com.example.manhe.search.model.Service;

import java.util.ArrayList;

public class ListServiceAdapter extends  RecyclerView.Adapter<ListServiceAdapter.MyViewHolder> {

     Context context;
     ArrayList<Service> serviceArrayList;


    public ListServiceAdapter(Context context, ArrayList<Service> serviceArrayList) {
        this.context = context;
        this.serviceArrayList=serviceArrayList;
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

        if(serviceArrayList.get(position).getIschecked()){
            holder.img.setImageResource(serviceArrayList.get(position).getIcon());
        }

    }


    @Override
    public int getItemCount()
    {
        return serviceArrayList.size();
    }
}
