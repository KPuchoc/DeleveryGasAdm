package com.example.androidsportswearserver.ViewHolder;


import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsportswearserver.Common.Common;
import com.example.androidsportswearserver.Interface.ItemClickListener;
import com.example.androidsportswearserver.R;

public class SportswearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView sportswear_name;
    public ImageView sportswear_image;
    private ItemClickListener itemClickListener;


    public SportswearViewHolder(@NonNull View itemView) {
        super(itemView);
        sportswear_name= (TextView) itemView.findViewById(R.id.sportswear_name);
        sportswear_image= (ImageView) itemView.findViewById(R.id.sportswear_image);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Seleciónar Accion");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}
