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

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder> {


    Context context;
    ArrayList<Integer> list_tag;
    ItemClick itemClick;



    public TagAdapter(Context context, ArrayList<Integer> list_tag) {
        this.context = context;
        this.list_tag = list_tag;
    }

    public void setClickListener(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img_tag;

        public MyViewHolder(View view) {
            super(view);
            img_tag = view.findViewById(R.id.imgtag);
            img_tag.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (itemClick != null) itemClick.Click(v, getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        switch (list_tag.get(position)) {
            case 1:
                holder.img_tag.setImageResource(R.drawable.vending);
                break;
            case 2:
                holder.img_tag.setImageResource(R.drawable.post);
                break;
            case 3:
                holder.img_tag.setImageResource(R.drawable.coffee);
                break;
            case 4:
                holder.img_tag.setImageResource(R.drawable.store);
                break;
            case 5:
                holder.img_tag.setImageResource(R.drawable.spmk);
                break;
        }

    }


    @Override
    public int getItemCount() {
        return list_tag.size();
    }
}