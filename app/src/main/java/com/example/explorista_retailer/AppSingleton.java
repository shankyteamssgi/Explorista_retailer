package com.example.explorista_retailer;

import android.app.Application;
import android.content.Context;

import com.example.explorista_retailer.Wrappers.OrderDataWrapper;

public class AppSingleton extends Application {

    private static AppSingleton instance;

    private OrderDataWrapper orderDataWrapper;

    public static AppSingleton getInstance(){
        if(instance==null){
            instance=new AppSingleton();
        }
        return instance;
    }

    public static Context getContext(){
        return getInstance().getApplicationContext();
    }

    public OrderDataWrapper getOrderDataWrapper(OrderDataWrapper.DATA_SOURCE data_source){
        if(!orderDataWrapperInitialized()){
            orderDataWrapper=new OrderDataWrapper(data_source);
        }
        return orderDataWrapper;
    }

    public boolean orderDataWrapperInitialized(){
        return orderDataWrapper!=null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
}
