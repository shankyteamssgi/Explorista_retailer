package com.example.explorista_retailer.Wrappers;

import android.content.Context;

import com.example.explorista_retailer.DataProviders.OrderDataDP;
import com.example.explorista_retailer.Models.OrderDataModel;
import com.example.explorista_retailer.SharedPreferenceManagers.OrderDataSPM;

public class OrderDataWrapper {

    private OrderDataDP orderDataDP;
    private OrderDataModel orderDataModel;
    public enum DATA_SOURCE{SPREF,SERVER};

    public OrderDataWrapper(DATA_SOURCE data_source) {
        switch (data_source){
            case SPREF:
                assignOrderDataModelFromSP();
                break;
            case SERVER:
                initializeOrderDataDP();
                initializeOrderDataModel();
                populateOrderDataModel();
                saveOrderDataModelToSP(orderDataModel);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + data_source);
        }
    }

    private void initializeOrderDataDP(){
        orderDataDP=new OrderDataDP();
    }

    private void initializeOrderDataModel(){
        orderDataModel=new OrderDataModel();
    }

    private void populateOrderDataModel(){
        orderDataModel.populateOrderData(orderDataDP.getOrderDataJson());
    }

    private OrderDataDP getOrderDataDP() {
        if(orderDataDP==null){
            initializeOrderDataDP();
        }
        return orderDataDP;
    }

    public OrderDataModel getOrderDataModel() {
        if(!orderDatModelInitialized()){
            if(orderDataDP==null){
                initializeOrderDataDP();
            }
            initializeOrderDataModel();
            populateOrderDataModel();
        }
        return orderDataModel;
    }

    public boolean orderDatModelInitialized(){
        return orderDataModel!=null;
    }

    public void saveOrderDataModelToSP(OrderDataModel orderDataModel){
        OrderDataSPM.saveOrderDataModelObjectToSP(orderDataModel);
    }

    private void assignOrderDataModelFromSP(){
        orderDataModel=OrderDataSPM.getOrderDataModelObjectFromSP();
    }

}
