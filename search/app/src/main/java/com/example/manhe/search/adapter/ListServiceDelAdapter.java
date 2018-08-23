package com.example.manhe.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.manhe.search.Interface.ItemClick;
import com.example.manhe.search.R;
import com.example.manhe.search.model.Service;

import java.util.ArrayList;

public class ListServiceDelAdapter extends  RecyclerView.Adapter<ListServiceDelAdapter.MyViewHolder> {

    Context context;
    ArrayList<Service> serviceArrayList;
    ItemClick itemClick;


    public ListServiceDelAdapter(Context context, ArrayList<Service> serviceArrayList) {
        this.context = context;
        this.serviceArrayList=serviceArrayList;
    }


    public void setClickListener(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img;
        ImageView img_del;
        public MyViewHolder(View view) {
            super(view);
            img= view.findViewById(R.id.img_ic);
            img_del = view.findViewById(R.id.img_del);
            view.setOnClickListener(this);
            img.setOnClickListener(this);
            img_del.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (itemClick != null) itemClick.Click(v, getAdapterPosition());
        }
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_service_del, parent, false);

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

    public ArrayList<Service> getServiceArrayList() {
        return serviceArrayList;
    }
}

