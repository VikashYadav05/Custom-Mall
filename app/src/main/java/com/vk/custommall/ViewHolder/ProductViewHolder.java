package com.vk.custommall.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vk.custommall.Interface.ItemClickListner;
import com.vk.custommall.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView=(ImageView) itemView.findViewById(R.id.product_image_home_layout);
        txtProductDescription=(TextView) itemView.findViewById((R.id.product_description_home_layout));
        txtProductName=(TextView) itemView.findViewById(R.id.product_name_home_layout);
        txtProductPrice=(TextView) itemView.findViewById(R.id.product_price_home_layout);
    }
    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner=listner;
    }

    @Override
    public void onClick(View view)
    {
      listner.onClick(view,getAdapterPosition(),false);
    }
}
