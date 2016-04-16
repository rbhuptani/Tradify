package com.hello_world.ronak.tradify;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;


public class Fragement_HomeScreen extends Fragment {
    static Firebase ref = new Firebase("https://tradify.firebaseio.com/Products");
    TradifyFirebaseAdapter tfa;
    RecyclerView mrecyclerView;
    LinearLayoutManager mLayoutManagar;

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
        mrecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mrecyclerView.setHasFixedSize(true);
        mLayoutManagar = new LinearLayoutManager(getActivity());
        mrecyclerView.setLayoutManager(mLayoutManagar);
        tfa = new TradifyFirebaseAdapter(Products.class,R.layout.card_item_view,TradifyFirebaseAdapter.ProductsViewHolder.class,ref,getActivity());
        mrecyclerView.setAdapter(tfa);
        return rootView;
    }

}
