package com.hello_world.ronak.tradify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by ronak_000 on 4/15/2016.
 */
public class TradifyFirebaseAdapter extends FirebaseRecyclerAdapter<Products,TradifyFirebaseAdapter.ProductsViewHolder> {
    private Context mContext ;
    static OnItemClickListener mItemClickListener;
    public static interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    public void SetOnItemClickListener(OnItemClickListener itemListner){
        mItemClickListener = itemListner;
    }
    public TradifyFirebaseAdapter(Class<Products> modelClass, int modelLayout,
                                    Class<ProductsViewHolder> holder, Query ref,Context context) {
        super(modelClass,modelLayout,holder,ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(ProductsViewHolder productsViewHolder, Products products, int i) {
        productsViewHolder.txt_name.setText(products.getProductName());
        productsViewHolder.txt_Desc.setText(products.getDescription());
        productsViewHolder.txt_LOI.setText(products.getListOfItems());
        productsViewHolder.img_product.setImageBitmap(StringToBitMap(products.getProductImage()));
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView txt_name;
        TextView txt_Desc;
        TextView txt_LOI;
        public ProductsViewHolder(final View itemView) {
            super(itemView);
            img_product = (ImageView) itemView.findViewById(R.id.productImage);
            txt_name = (TextView) itemView.findViewById(R.id.productName);
            txt_Desc = (TextView) itemView.findViewById(R.id.productDesc);
            txt_LOI = (TextView) itemView.findViewById(R.id.listofitems);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mItemClickListener != null)
                        mItemClickListener.onItemClick(itemView,getAdapterPosition());
                }
            });
        }
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
