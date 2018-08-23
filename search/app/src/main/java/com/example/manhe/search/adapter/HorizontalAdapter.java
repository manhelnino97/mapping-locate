package com.example.manhe.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.manhe.search.Interface.MenuBarItemClick;
import com.example.manhe.search.model.Category;
import com.example.manhe.search.R;

import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    private ArrayList<ArrayList<Category>> list;
    private Context context;
    private String lg;
    private  int[] img1_active = {R.drawable.vending_na,R.drawable.post_na,R.drawable.coffee_na,R.drawable.store_na,R.drawable.spmk_na};
    private int[] img1_notactive = {R.drawable.vending,R.drawable.post,R.drawable.coffee,R.drawable.store,R.drawable.spmk};
    private MenuBarItemClick menuBarItemClick;
    public int[] status = {0,0,0,0,0,0};
    public int[] cate = {-1,-1,-1,-1,-1,-1};


    public HorizontalAdapter(Context context, ArrayList<ArrayList<Category>> list, String lg) {
        this.list = list;
        this.context = context;
        this.lg=lg;
    }





    public void setClickListener(MenuBarItemClick menuBarItemClickListener) {
        this.menuBarItemClick = menuBarItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageButton image1,image2,image3,image4,image5;
        TextView txt1,txt2,txt3,txt4,txt5;
        public MyViewHolder(View view) {
            super(view);
            image1= view.findViewById(R.id.btnops_1);
            txt1= view.findViewById(R.id.txtops_1);
            image2= view.findViewById(R.id.btnops_2);
            txt2= view.findViewById(R.id.txtops_2);
            image3= view.findViewById(R.id.btnops_3);
            txt3= view.findViewById(R.id.txtops_3);
            image4= view.findViewById(R.id.btnops_4);
            txt4= view.findViewById(R.id.txtops_4);
            image5= view.findViewById(R.id.btnops_5);
            txt5= view.findViewById(R.id.txtops_5);

            image1.setOnClickListener(this);
            image2.setOnClickListener(this);
            image3.setOnClickListener(this);
            image4.setOnClickListener(this);
            image5.setOnClickListener(this);

            txt1.setOnClickListener(this);
            txt2.setOnClickListener(this);
            txt3.setOnClickListener(this);
            txt4.setOnClickListener(this);
            txt5.setOnClickListener(this);



        }


        @Override
        public void onClick(View v) {

            if(getAdapterPosition()==0){
                if(v.getId()==R.id.btnops_1 || v.getId()==R.id.txtops_1){
                    if (menuBarItemClick != null) menuBarItemClick.Click(v, 0);
                }
                if(v.getId()==R.id.btnops_2 || v.getId()==R.id.txtops_2){
                    if (menuBarItemClick != null) menuBarItemClick.Click(v, 1 );
                }
                if(v.getId()==R.id.btnops_3 || v.getId()==R.id.txtops_3){
                    if (menuBarItemClick != null) menuBarItemClick.Click(v, 2 );
                }
                if(v.getId()==R.id.btnops_4 || v.getId()==R.id.txtops_4){
                    if (menuBarItemClick != null) menuBarItemClick.Click(v, 3 );
                }
                if(v.getId()==R.id.btnops_5 || v.getId()==R.id.txtops_5){
                    if (menuBarItemClick != null) menuBarItemClick.Click(v, 4 );
                }
            }

        }
    }





    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menubar, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

            if(cate[0] == 0 && status[0]==1) holder.image1.setImageResource(img1_notactive[0]);
            else holder.image1.setImageResource(img1_active[0]);

            if(cate[1] == 1 && status[1]==1) holder.image2.setImageResource(img1_notactive[1]);
            else holder.image2.setImageResource(img1_active[1]);

            if(cate[2] == 2 && status[2]==1) holder.image3.setImageResource(img1_notactive[2]);
            else holder.image3.setImageResource(img1_active[2]);

            if(cate[3] == 3 && status[3]==1) holder.image4.setImageResource(img1_notactive[3]);
            else holder.image4.setImageResource(img1_active[3]);

            if(cate[4] == 4 && status[4]==1) holder.image5.setImageResource(img1_notactive[4]);
            else holder.image5.setImageResource(img1_active[4]);



        switch (lg) {
            case "ja":
                holder.txt1.setText(list.get(position).get(0).getName());
                holder.txt2.setText(list.get(position).get(1).getName());
                holder.txt3.setText(list.get(position).get(2).getName());
                holder.txt4.setText(list.get(position).get(3).getName());
                holder.txt5.setText(list.get(position).get(4).getName());
                break;
            case "vi":
                holder.txt1.setText(list.get(position).get(0).getName_vi());
                holder.txt2.setText(list.get(position).get(1).getName_vi());
                holder.txt3.setText(list.get(position).get(2).getName_vi());
                holder.txt4.setText(list.get(position).get(3).getName_vi());
                holder.txt5.setText(list.get(position).get(4).getName_vi());
                break;
            case "en":
                holder.txt1.setText(list.get(position).get(0).getName_en());
                holder.txt2.setText(list.get(position).get(1).getName_en());
                holder.txt3.setText(list.get(position).get(2).getName_en());
                holder.txt4.setText(list.get(position).get(3).getName_en());
                holder.txt5.setText(list.get(position).get(4).getName_en());
                break;
            case "ko":
                holder.txt1.setText(list.get(position).get(0).getname_ko());
                holder.txt2.setText(list.get(position).get(1).getname_ko());
                holder.txt3.setText(list.get(position).get(2).getname_ko());
                holder.txt4.setText(list.get(position).get(3).getname_ko());
                holder.txt5.setText(list.get(position).get(4).getname_ko());
                break;
            case "zh":
                holder.txt1.setText(list.get(position).get(0).getName_cn());
                holder.txt2.setText(list.get(position).get(1).getName_cn());
                holder.txt3.setText(list.get(position).get(2).getName_cn());
                holder.txt4.setText(list.get(position).get(3).getName_cn());
                holder.txt5.setText(list.get(position).get(4).getName_cn());
                break;
            case "es":
                holder.txt1.setText(list.get(position).get(0).getName_es());
                holder.txt2.setText(list.get(position).get(1).getName_es());
                holder.txt3.setText(list.get(position).get(2).getName_es());
                holder.txt4.setText(list.get(position).get(3).getName_es());
                holder.txt5.setText(list.get(position).get(4).getName_es());
                break;
        }

    }


    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
