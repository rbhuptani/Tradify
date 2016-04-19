package com.hello_world.ronak.tradify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;

import java.util.HashMap;


public class Fragement_HomeScreen extends Fragment {
    static Firebase ref = new Firebase("https://tradify.firebaseio.com/Products");
    TradifyFirebaseAdapter tfa;
    RecyclerView mrecyclerView;
    LinearLayoutManager mLayoutManagar;
    OnListItemSelectedListener mListner;
    public interface OnListItemSelectedListener{
        public void onListItemSelected(Products product,Users user);
    }
    public Fragement_HomeScreen() {
        // Required empty public constructor
    }


    public static Fragement_HomeScreen newInstance() {
        Fragement_HomeScreen fragment = new Fragement_HomeScreen();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment__home_screen,container,false);
        mListner = (OnListItemSelectedListener) getActivity();
        mrecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mrecyclerView.setHasFixedSize(true);
        mLayoutManagar = new LinearLayoutManager(getActivity());
        mrecyclerView.setLayoutManager(mLayoutManagar);
        tfa = new TradifyFirebaseAdapter(Products.class,R.layout.card_item_view,TradifyFirebaseAdapter.ProductsViewHolder.class,ref,getActivity());
        tfa.SetOnItemClickListener(new TradifyFirebaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //Toast.makeText(getActivity(),"Single Click" + Integer.toString(position), Toast.LENGTH_LONG).show();
                Products product = tfa.getItem(position);
                Users user = getUserDetails(product.getUserID());
                Log.d("Mode ",product.getMode());
                mListner.onListItemSelected(product,user);
            }
        });
        mrecyclerView.setAdapter(tfa);
        return rootView;
    }
    public Users getUserDetails(String userID){
        final Users user = new Users();
        final Firebase uref = new Firebase("https://tradify.firebaseio.com/Users");
        uref.child(userID).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                for (MutableData md : mutableData.getChildren()) {
                    if (md.getKey() == "Username")
                        user.setUsername(md.getValue().toString());
                    if (md.getKey() == "UserId")
                        user.setUserId(md.getValue().toString());
                    if (md.getKey() == "Email")
                        user.setEmail(md.getValue().toString());
                    if (md.getKey() == "UserImage")
                        user.setUserImage(md.getValue().toString());
                    if (md.getKey() == "ContactNumber")
                        user.setContactNumber(Integer.valueOf(md.getValue().toString()));
                    if (md.getKey() == "Address")
                        user.setAddress(md.getValue().toString());
                }
                return Transaction.abort();
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d("on Complete", "called");
            }
        });
        return  user;
    }

}
