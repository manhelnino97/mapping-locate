package com.example.manhe.search.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.manhe.search.model.Image;
import com.example.manhe.search.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BitmapAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Image> images;

    public BitmapAdapter(Context context, ArrayList<Image> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        if(images.get(position).check)
        {
            imageView.setImageBitmap(images.get(position).getBitmap());
        }
        else
        {
            Picasso.with(context)
                    .load("http://192.168.1.67:8080/upload/"+images.get(position).getUrl())
                    .into(imageView);
        }


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}