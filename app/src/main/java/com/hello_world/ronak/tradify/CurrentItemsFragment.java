package com.hello_world.ronak.tradify;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */



public class CurrentItemsFragment extends Fragment {
    static Firebase ref = new Firebase("https://tradify.firebaseio.com/Products");
    static Firebase refTemp = new Firebase("https://tradify.firebaseio.com/Temp");
    /*Query _qref = ref;
    Query _qrefTemp = refTemp;*/
    static String currUserId;
    //TradifyFirebaseAdapter tfa;
    RecyclerView mrecyclerView;
    LinearLayoutManager mLayoutManagar;
    Products product;
    //ArrayList<Products> prodArray = new ArrayList<Products>();
    Users user;
    OnListItemSelectedListener mListner;
    TradifyRecyclerAdapter tra;
    static int tabPosition;

    public interface OnListItemSelectedListener{
        public void onListItemSelected(Products product, Users user);
    }

    public CurrentItemsFragment() {
        // Required empty public constructor
    }

    public static CurrentItemsFragment newInstance(String userId, int position) {
        CurrentItemsFragment fragment = new CurrentItemsFragment();
        Bundle args = new Bundle();
        //args.putString("userId",userId);
        currUserId = userId;
        tabPosition=position;
        fragment.setArguments(args);
        return fragment;
    }

   /* public static CurrentItemsFragment newInstance(Query ref){
        CurrentItemsFragment fragment = new CurrentItemsFragment();
        Bundle args = new Bundle();
        _qref = ref;
        fragment.setArguments(args);
        return fragment;
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_current_items, container, false);
        mrecyclerView = (RecyclerView) view.findViewById(R.id.cardListUserProf);
        mrecyclerView.setHasFixedSize(true);
        //mLayoutManagar = new LinearLayoutManager(getActivity());
        mLayoutManagar = new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mrecyclerView.setLayoutManager(mLayoutManagar);
        tra = new TradifyRecyclerAdapter(getContext());
//        mListner = (OnListItemSelectedListener) getActivity();
        try{
            new SetupAdapter(tra).execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        /*_qref =  _qref.getRef().orderByChild("UserID").equalTo(currUserId);
        _qref.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prodArray = new ArrayList<Products>();
                for (DataSnapshot prods : dataSnapshot.getChildren()) {
                    Products newProduct = new Products();
                    newProduct.setCategory((String) prods.child("Category").getValue());
                    newProduct.setDescription((String) prods.child("Description").getValue());
                    newProduct.setListOfItems((String) prods.child("ListOfItems").getValue());
                    newProduct.setLocation((String) prods.child("Location").getValue());
                    newProduct.setMode((String) prods.child("Mode").getValue());
                    newProduct.setPostedDate((Long) prods.child("PostedDate").getValue());
                    newProduct.setPrice(convertDouble(prods.child("Price").getValue()));
                    newProduct.setProductId((String) prods.child("ProductId").getValue());
                    newProduct.setProductImage((String) prods.child("ProductImage").getValue());
                    newProduct.setProductName((String) prods.child("ProductName").getValue());
                    newProduct.setSold((Boolean) prods.child("Sold").getValue());
                    newProduct.setUserID((String) prods.child("UserID").getValue());
                    prodArray.add(newProduct);
                }
            }*/

            /*@Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });*/
       /* try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        /*refTemp.removeValue();

        for(Products prod : prodArray) {
            refTemp.push().setValue(prod, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        Log.d("Data not saved. ", firebaseError.getMessage());
                    } else {
                        Log.d("Data saved.", "Data saved.");
                    }
                }
            });
        }*/
        //mListner = (OnListItemSelectedListener) getActivity();

        //_qrefTemp = _qrefTemp.getRef().orderByChild("Sold").equalTo(false);
        //_qref = _qref.getRef().orderByChild("Sold").startAt("f").endAt("e");
        /*tfa = new TradifyFirebaseAdapter(Products.class,R.layout.card_item_view,TradifyFirebaseAdapter.ProductsViewHolder.class,_qref,getActivity());
        tfa.SetOnItemClickListener(new TradifyFirebaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                try {
                    new getUserData(position).execute();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });*/
        mrecyclerView.setAdapter(tra);
        return view;
    }

    private class SetupAdapter extends AsyncTask<Void,Void,Void> {
        private final WeakReference<TradifyRecyclerAdapter> adapterRef;
        public  SetupAdapter(TradifyRecyclerAdapter adapter){
            adapterRef = new WeakReference<TradifyRecyclerAdapter>(adapter);
        }
        @Override
        protected Void doInBackground(Void... params) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ProductLocalDB.PRODUCT_LOCAL_DB.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Products product = ProductLocalDB.createProduct((HashMap) ds.getValue());
                        if (tabPosition == 0) {
                            if (currUserId.equals(product.getUserID()) && product.getSold() == false) {
                                ProductLocalDB.PRODUCT_LOCAL_DB.add(product);
                            }
                        } else if (tabPosition == 1) {
                            if (currUserId.equals(product.getUserID()) && product.getSold() == true) {
                                ProductLocalDB.PRODUCT_LOCAL_DB.add(product);
                            }
                        }
                    }
                    Log.d("Product DB count", String.valueOf(ProductLocalDB.PRODUCT_LOCAL_DB.size()));

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void voids ){
            Log.d("Post execute method", "called");
            final TradifyRecyclerAdapter tra = adapterRef.get();
            if(tra != null) {
                tra.SetOnItemClickListener(new TradifyRecyclerAdapter.OnItemClickListener_Recycler() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Products product = tra.getItem(position);
                        new getUserData(position).execute();
                        Log.d("Mode ", product.getMode());
                        /*Intent intent = new Intent(getContext(),Activity_HomeScreen.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userprofile",true);
                        bundle.putSerializable("product",product);
                        bundle.putSerializable("user",user);
                        intent.putExtras(bundle);
                        startActivity(intent);*/
                        //mListner.onListItemSelected(product, user);
                    }
                });
                tra.notifyItemChanged(0);
            }
        }
    }

    private class getUserData extends AsyncTask<Void,Void,Void> {
        int position;
        public  getUserData(int p){
            position = p;
        }

        @Override
        protected Void doInBackground(Void... params) {
            product = tra.getItem(position);
            user = getUserDetails(product.getUserID());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Log.d("User iD ", user.getUserId());
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            //mListner.onListItemSelected(product,user);
            Intent intent = new Intent(getContext(),Activity_HomeScreen.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("userprofile",true);
            bundle.putSerializable("product",product);
            bundle.putSerializable("user",user);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public Users getUserDetails(String userID){
        final Users user = new Users();
        final Firebase uref = new Firebase("https://tradify.firebaseio.com/Users/" + userID);
        uref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot md : dataSnapshot.getChildren()) {
                    if (md.getKey() == "Username")
                        user.setUsername(md.getValue().toString());
                    if (md.getKey() == "UserId")
                        user.setUserId(md.getValue().toString());
                    if (md.getKey() == "Email")
                        user.setEmail(md.getValue().toString());
                    if (md.getKey() == "UserImage")
                        user.setUserImage( md.getValue().toString());
                    if (md.getKey() == "ContactNumber")
                        user.setContactNumber(md.getValue().toString());
                    if (md.getKey() == "Address")
                        user.setAddress(md.getValue().toString());
                }
                Log.d("UserID", user.getUserId());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return  user;
    }

    static double convertDouble(Object longValue){
        double valueTwo = -1; // whatever to state invalid!

        if(longValue instanceof Long)
            valueTwo = ((Long) longValue).doubleValue();

        System.out.println(valueTwo);
        return valueTwo;
    }



}
