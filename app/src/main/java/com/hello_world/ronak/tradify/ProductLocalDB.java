package com.hello_world.ronak.tradify;

import android.util.Log;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ronak_000 on 4/22/2016.
 */
public class ProductLocalDB {
    static List<Products> PRODUCT_LOCAL_DB= new ArrayList<Products>();


    static Products createProduct(HashMap dataSnapshot){
        Products product = new Products();
        Iterator it = dataSnapshot.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry ds = (Map.Entry)it.next();
            if(ds.getKey() == "Category")
                product.setCategory(ds.getValue().toString());
            if(ds.getKey() == "Description")
                product.setDescription(ds.getValue().toString());
            if(ds.getKey() == "ListOfItems")
                product.setListOfItems(ds.getValue().toString());
            if(ds.getKey() == "Location")
                product.setLocation(ds.getValue().toString());
            if(ds.getKey() == "Mode")
                product.setMode(ds.getValue().toString());
            if(ds.getKey() == "PostedDate")
                product.setPostedDate(Long.parseLong(ds.getValue().toString()));
            if(ds.getKey() == "Price")
                product.setPrice(Double.parseDouble(ds.getValue().toString()));
            if(ds.getKey() == "ProductId")
                product.setProductId(ds.getValue().toString());
            if(ds.getKey() == "ProductImage")
                product.setProductImage(ds.getValue().toString());
            if(ds.getKey() == "ProductName")
                product.setProductName(ds.getValue().toString());
            if(ds.getKey() == "Sold")
                product.setSold(Boolean.parseBoolean(ds.getValue().toString()));
            if(ds.getKey() == "UserID")
                product.setUserID(ds.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return  product;
    }
}
