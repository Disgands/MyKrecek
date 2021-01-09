package com.tika.mykrecek.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tika.mykrecek.Interface.ItemClickListener;
import com.tika.mykrecek.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public TextView txtProductName, txtProductPrice,txtProductQuantity;
private ItemClickListener itemClickListener;

public CartViewHolder(@NonNull View itemView) {

        super(itemView);

        txtProductName = itemView.findViewById(R.id.nama_produk);
        txtProductPrice = itemView.findViewById(R.id.harga_produk);
        txtProductQuantity = itemView.findViewById(R.id.jumlah_produk);
        }

@Override
public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

        }

public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        }
        }

