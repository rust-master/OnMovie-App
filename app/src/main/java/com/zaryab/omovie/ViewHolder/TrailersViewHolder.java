package com.zaryab.omovie.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaryab.omovie.Interface.ItemClickListener;

public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtPlaceName;
//    public ImageView imageView;

    private ItemClickListener itemClickListener;


    public TrailersViewHolder(@NonNull View itemView) {
        super( itemView );

       // txtPlaceName = (TextView) itemView.findViewById( R.id.TrailerTitle);
//      imageView = (ImageView) itemView.findViewById(R.id.trailerCover);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.OnClick(view, getAdapterPosition(),false);

    }
}
