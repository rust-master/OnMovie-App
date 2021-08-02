package com.zaryab.omovie.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaryab.omovie.Interface.ItemClickListener;
import com.zaryab.omovie.R;


public class AnimatedMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtPlaceName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;


    public AnimatedMovieViewHolder(@NonNull View itemView) {
        super(itemView);

        txtPlaceName = (TextView) itemView.findViewById(R.id.movie_nameani);
        imageView = (ImageView) itemView.findViewById(R.id.movie_imageani);


        itemView.setOnClickListener(this);
        itemView.findViewById( R.id.fab ).setOnClickListener( this );
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(view, getAdapterPosition(),false);
    }
}
