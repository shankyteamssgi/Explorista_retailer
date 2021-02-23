package com.example.explorista_retailer.SharedPreferenceManagers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.explorista_retailer.AppSingleton;
import com.example.explorista_retailer.Models.OrderDataModel;
import com.google.gson.Gson;

public class OrderDataSPM {

    private static final String SPFILE_ORDERMANAGEMENT="spfile_ordermanagement";
    private static final String SPKEY_ORDERDATA="spkey_orderdata";

    public static void saveOrderDataModelObjectToSP(OrderDataModel orderDataModel){

        // Adds OrderDataModel object to SharedPreference

        SharedPreferences orderDataSP= AppSingleton.getContext().getSharedPreferences(SPFILE_ORDERMANAGEMENT,Context.MODE_PRIVATE);
        orderDataSP.edit()
                .putString(SPKEY_ORDERDATA,(new Gson()).toJson(orderDataModel))
                .apply();
    }

    public static OrderDataModel getOrderDataModelObjectFromSP(){

        // Gets OrderDataModelObject from SharedPreference

        SharedPreferences orderDataSP=AppSingleton.getContext().getSharedPreferences(SPFILE_ORDERMANAGEMENT,Context.MODE_PRIVATE);
        String orderDataModelObjectString=orderDataSP.getString(SPKEY_ORDERDATA,null);
        if(orderDataModelObjectString!=null){
            return (new Gson()).fromJson(orderDataModelObjectString,OrderDataModel.class);
        }
        return null;
    }

}
