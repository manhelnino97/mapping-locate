package com.example.manhe.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manhe.search.R;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyViewHolder> {


    Context context;
    ArrayList<String> time;



    public TimeAdapter(Context context, ArrayList<String> time) {
        this.context = context;
        this.time=time;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt;
        public MyViewHolder(View view) {
            super(view);
            txt= view.findViewById(R.id.time);


        }


    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.working_time, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txt.setText(time.get(position));

    }


    @Override
    public int getItemCount()
    {
        return time.size();
    }
}

