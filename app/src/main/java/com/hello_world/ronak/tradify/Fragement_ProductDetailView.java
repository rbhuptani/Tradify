package com.hello_world.ronak.tradify;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by ronak_000 on 4/18/2016.
 */
public class Fragement_ProductDetailView extends Fragment {
    public static String ARG_PRODUCT = "product_obj";
    public static Products _product;
    TextView txt_name,txt_details,txt_mode,txt_username,txt_loi;
    ImageView img_product;
    public static Fragement_ProductDetailView newInstance(Products product) {
        Fragement_ProductDetailView fragment = new Fragement_ProductDetailView();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragement_ProductDetailView() {

    }


    @Override
    public  void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragement_productdetailview,container,false);
        txt_name = (TextView)rootView.findViewById(R.id.txt_dv_productName);
        txt_details = (TextView)rootView.findViewById(R.id.txt_dv_productDetails);
        txt_mode = (TextView)rootView.findViewById(R.id.txt_dv_transactionMode);
        txt_username= (TextView)rootView.findViewById(R.id.txt_dv_userName);
        txt_loi = (TextView)rootView.findViewById(R.id.txt_dv_LOI);
        img_product = (ImageView) rootView.findViewById(R.id.img_dv_product);
        Products product = (Products)getArguments().getSerializable(ARG_PRODUCT);
        txt_name.setText(product.getProductName());
        txt_details.setText(product.getDescription());
        txt_mode.setText(product.getMode());
        txt_username.setText(product.getUserID());
        txt_loi.setText(product.getListOfItems());
        img_product.setImageBitmap(StringToBitMap(product.getProductImage()));
        return rootView;
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

