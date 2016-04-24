package com.hello_world.ronak.tradify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ronak_000 on 4/22/2016.
 */
public class TradifyRecyclerAdapter extends RecyclerView.Adapter<TradifyRecyclerAdapter.ViewHolder> {
    static OnItemClickListener_Recycler mItemClickListener;
    private Context mContext;

    public TradifyRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setContext(Context context){
        mContext = context;
    }

    public static interface OnItemClickListener_Recycler{
        void onItemClick(View v, int position);
    }
    public void SetOnItemClickListener(OnItemClickListener_Recycler itemListner){
        mItemClickListener = itemListner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("View holder PD count", String.valueOf(ProductLocalDB.PRODUCT_LOCAL_DB.size()));
        Products product = ProductLocalDB.PRODUCT_LOCAL_DB.get(position);
        Log.d("Product_Id",product.getProductId());
        holder.bindProductData(product);

    }

    public Products getItem(int position){
        Log.d("getItem PD count", String.valueOf(ProductLocalDB.PRODUCT_LOCAL_DB.size()));
        Products product = ProductLocalDB.PRODUCT_LOCAL_DB.get(position);
        return  product;
    }

    @Override
    public int getItemCount() {
        return ProductLocalDB.PRODUCT_LOCAL_DB.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        ImageView img_product;
        TextView txt_name;
        TextView txt_Desc;
        TextView txt_LOI;
        public ViewHolder(final View itemView) {
            super(itemView);
            img_product = (ImageView) itemView.findViewById(R.id.productImage);
            txt_name = (TextView) itemView.findViewById(R.id.productName);
            txt_Desc = (TextView) itemView.findViewById(R.id.productDesc);
            txt_LOI = (TextView) itemView.findViewById(R.id.listofitems);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }

        public void bindProductData(Products products) {
            txt_name.setText(products.getProductName());
            txt_Desc.setText(products.getDescription());
            txt_LOI.setText(products.getListOfItems());
            img_product.setImageBitmap(StringToBitMap(products.getProductImage()));
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
