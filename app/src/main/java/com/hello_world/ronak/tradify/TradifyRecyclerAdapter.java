package com.hello_world.ronak.tradify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        if(position>=0 && position<ProductLocalDB.PRODUCT_LOCAL_DB.size()) {
            Products product = ProductLocalDB.PRODUCT_LOCAL_DB.get(position);
            Log.d("Product_Id", product.getProductId());
            holder.bindProductData(product);
        }
    }

    public Products getItem(int position){
        Products product = new Products();
        Log.d("getItem PD count", String.valueOf(ProductLocalDB.PRODUCT_LOCAL_DB.size()));
        if(position>=0 && position<ProductLocalDB.PRODUCT_LOCAL_DB.size()) {
            product = ProductLocalDB.PRODUCT_LOCAL_DB.get(position);
        }
        return product;
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
        TextView posted_date;
        TextView product_address;
        public ViewHolder(final View itemView) {
            super(itemView);
            img_product = (ImageView) itemView.findViewById(R.id.productImage);
            txt_name = (TextView) itemView.findViewById(R.id.productName);
            txt_Desc = (TextView) itemView.findViewById(R.id.productDesc);
            txt_LOI = (TextView) itemView.findViewById(R.id.listofitems);
            posted_date = (TextView) itemView.findViewById(R.id.productPostedDate);
            product_address = (TextView) itemView.findViewById(R.id.productAddress);
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
            long date = products.getPostedDate();
            SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            String postedDate = format.format(date);
            posted_date.setText(postedDate);

            Geocoder geocoder;
            //List<Address> addresses;
            geocoder = new Geocoder(mContext, Locale.getDefault());
            String latlong = products.getLocation();
            String[] temp = latlong.split(",");
            double _lat =0,_long =0;
            try{
                _lat = Double.parseDouble(temp[0]);
                _long = Double.parseDouble(temp[1]);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            String address = "Address not available.";
            try {
                List<Address> addresses = geocoder.getFromLocation(_lat, _long, 1);
                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("");
                    for (int ia = 0; ia < returnedAddress.getMaxAddressLineIndex(); ia++)
                        strReturnedAddress.append(returnedAddress.getAddressLine(ia)).append(" , ");
                    address = strReturnedAddress.toString();
                    Log.d("My Current address", "" + strReturnedAddress.toString());
                } else {
                    Log.d("My Current address", "No Address returned!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("Exception LatLong", latlong);
            }

            product_address.setText(address);
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
