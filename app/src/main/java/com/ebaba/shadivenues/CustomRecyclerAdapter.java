package com.ebaba.shadivenues;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    private List<Venues> blogList;

    //the context object
    private Context mCtx;

    public CustomRecyclerAdapter(Context mCtx, List<Venues> blogList) {
        this.mCtx = mCtx;
        this.blogList = blogList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.venuelist, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(blogList.get(position));
        Venues blog = blogList.get(position);
        Uri myUri = Uri.parse(blog.getImgurl());
        holder.venueTitle.setText(blog.getTitle());
         holder.venueCap.setText(blog.getCapacity());
         holder.venueAddress.setText(blog.getAddress());

//holder.venueImage.setImageResource(R.drawable.demoimg);
        Picasso.get()
                .load(blog.getImgurl())
                .placeholder(R.drawable.ic_home_black_24dp)
                .into(holder.venueImage);
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView venueTitle;
        public ImageView venueImage;
        public TextView   venueAddress;
        public  TextView venueCap;


        public ViewHolder(View itemView) {
            super(itemView);

            venueTitle = (TextView) itemView.findViewById(R.id.venueTitle);
            venueImage =  itemView.findViewById(R.id.venueImg);
            venueAddress =itemView.findViewById(R.id.venueaddress);
            venueCap =itemView.findViewById(R.id.venuecap);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 Venues cpu = (Venues) view.getTag();
                    String  postID = cpu.getId();
//
//                    // Toast.makeText(getApplicationContext(), cpu.getPostId(), Toast.LENGTH_LONG).show();
              Intent blogin=new Intent(mCtx,Details.class);
              blogin.putExtra("postid", postID);
//                    blogin.putExtra("type", 1);
                   mCtx.startActivity(blogin);

                }
            });

        }
    }

}