package com.example.manhe.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manhe.search.model.Options;
import com.example.manhe.search.R;

import java.util.ArrayList;

public class OptionsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Options> list_op;
    private int category;


    public OptionsAdapter(Context context, ArrayList<Options> list_op) {
        this.context = context;
        this.list_op = list_op;
    }

    @Override
    public int getCount() {
        return list_op.size();
    }

    @Override
    public Options getItem(int position) {
        return list_op.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView img;
        TextView txt;
        LinearLayout linearLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder =null;
        if(convertView!=null)
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_options,null);
            viewHolder = new ViewHolder();
            viewHolder.img = convertView.findViewById(R.id.imageView);
            viewHolder.txt = convertView.findViewById(R.id.textView);
            viewHolder.linearLayout = convertView.findViewById(R.id.linear);
            convertView.setTag(viewHolder);
        }
        viewHolder.txt.setText(list_op.get(position).getName());
        viewHolder.img.setImageResource(list_op.get(position).getIcon());

        return convertView;
    }
}

