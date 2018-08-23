package com.example.manhe.search.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.manhe.search.R;

import java.util.ArrayList;

public class TagDelAdapter extends RecyclerView.Adapter<TagDelAdapter.MyViewHolder> {


    Context context;
    ArrayList<Integer> list_tag;


    public TagDelAdapter(Context context, ArrayList<Integer> list_tag) {
        this.context = context;
        this.list_tag = list_tag;
    }


public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView img_del,img_tag;
    public MyViewHolder(View view) {
        super(view);
        img_tag = view.findViewById(R.id.imgtag);
        img_del = view.findViewById(R.id.img_del);
    }


}


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tagdel, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

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

        holder.img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_tag.remove(position);
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return list_tag.size();
    }
}
