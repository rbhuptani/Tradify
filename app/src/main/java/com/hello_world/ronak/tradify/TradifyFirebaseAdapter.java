package com.hello_world.ronak.tradify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        long date = products.getPostedDate();
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        String postedDate = format.format(date);
        productsViewHolder.posted_date.setText(postedDate);

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
        String address = "Not able to receive address from database.";
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

        productsViewHolder.product_address.setText(address);
        productsViewHolder.img_product.setImageBitmap(StringToBitMap(products.getProductImage()));
        String transName = products.getProductId();
        productsViewHolder.img_product.setTransitionName(transName);
        Log.d("SharedElement_tfa : ", productsViewHolder.img_product.getTransitionName());
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView txt_name;
        TextView txt_Desc;
        TextView txt_LOI;
        TextView posted_date;
        TextView product_address;
        public ProductsViewHolder(final View itemView) {
            super(itemView);
            img_product = (ImageView) itemView.findViewById(R.id.productImage);
            txt_name = (TextView) itemView.findViewById(R.id.productName);
            txt_Desc = (TextView) itemView.findViewById(R.id.productDesc);
            txt_LOI = (TextView) itemView.findViewById(R.id.listofitems);
            posted_date = (TextView) itemView.findViewById(R.id.productPostedDate);
            product_address = (TextView) itemView.findViewById(R.id.productAddress);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mItemClickListener != null)
                        mItemClickListener.onItemClick(img_product,getAdapterPosition());
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
