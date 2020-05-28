package com.ebaba.shadivenues;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

public class MyTesimonialAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> clients;
    ArrayList<String> comments;
    LayoutInflater layoutInflater;


    public MyTesimonialAdapter(Context context, ArrayList<String> clients,ArrayList<String> comments) {
        this.context = context;
        this.comments= comments;
        this.clients=clients;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return clients.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.textslider, container, false);

        TextView comment = itemView.findViewById(R.id.client_comment);
        TextView name=itemView.findViewById(R.id.clinet_name);

        comment.setText(comments.get(position));
        name.setText(clients.get(position));

        container.addView(itemView);

//        //listening to image click
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
//
//            }
//        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}