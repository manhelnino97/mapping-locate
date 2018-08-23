package com.example.manhe.search.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manhe.search.Interface.ItemClick;
import com.example.manhe.search.R;

import java.util.ArrayList;

public class TimeDelAdapter extends RecyclerView.Adapter<TimeDelAdapter.MyViewHolder> {


    Context context;
    private ArrayList<String> time;
    private ItemClick itemClick;
    private ArrayList<Integer> timeDuplicate;


    public TimeDelAdapter(Context context, ArrayList<String> time, ArrayList<Integer> timeDuplicate) {
        this.context = context;
        this.time=time;
        this.timeDuplicate = timeDuplicate;
    }

    public void setClickListener(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txt;
        ImageView img;
        public MyViewHolder(View view) {
            super(view);
            txt= view.findViewById(R.id.time);
            img= view.findViewById(R.id.img_del);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (itemClick != null) itemClick.Click(v, getAdapterPosition());
        }
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.working_time_del, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.txt.setBackground(context.getResources().getDrawable(R.drawable.pb_txt_time));
        holder.txt.setTextColor(Color.parseColor("#757575"));
        if(timeDuplicate.size()>0) {
            for (int i = 0; i < timeDuplicate.size(); i++) {
                if (position == timeDuplicate.get(i)) {
                    holder.txt.setBackground(context.getResources().getDrawable(R.drawable.pb_txt_timeduplicate));
                    holder.txt.setTextColor(Color.parseColor("#fc0320"));
                    break;
                }
            }
        }

        holder.txt.setText(time.get(position));
        holder.img.setImageResource(R.drawable.ic_del);

    }



    @Override
    public int getItemCount()
    {
        return time.size();
    }



}

