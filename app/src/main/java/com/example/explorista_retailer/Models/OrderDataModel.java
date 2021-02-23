package com.example.explorista_retailer.Models;

import com.example.explorista_retailer.auxiliary;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDataModel {

    /* An instance of this object will be stored in SharedPreference.
       This object will store all orders under management in orderManagement activity.
    */
    
    private HashMap<String,HashMap<String,String>> basicOrderDataHM;
    private HashMap<String,HashMap<String,HashMap<String,String>>> prodDataHM;
    private HashMap<String,ArrayList<String>> prodIdsByOrderIdHM;
    //private HashMap<String,ArrayList<String>> orderIdsByOrderStatusHM;
    private HashMap<String,ArrayList<String>> orderIdsByOrderTabHM;
    private HashMap<String,Integer> orderIdIndexHM;

    private final HashMap<String,String> orderStatusToOrderTabMappingHM=initializeOrderStatusToOrderTabMapping();

    /*

    Basic description of private fields

    1) basicOrderDataHM
    It is a HashMap where key is order_id and value is order data.
    The order data is of type HashMap<String,String>.
    The key in order data HashMap is order attribute name such as its status, type, date and time, number of products in order, total amount, etc.
    The value in order data is value of that order attribute.

    2) prodDataHM
    It is a HashMap where key is order_id and value is HashMap of products in that order.
    HashMap of products has prod_id as key and value is HashMap of that product's data such as product name, required quantity, available quantity, etc.

    3) prodIdsByOrderIdHM
    It is a HashMap where key is order_id and value is ArrayList of ids of all products present in that order.
    It is used to maintain sequence of products in an order in RecyclerView.

    4) orderIdsByOrderStatusHM (ignore this / no longer in use)
    It is a HashMap where key is order status and value is ArrayList of order ids which have that order status.
    It is used to maintain sequence of orders in RecyclerView for a certain order status.

    5) orderIdsByOrderTabHM
    It is a HashMap where key is order tab and value is ArrayList of order ids which have that order tab.
    It is used to maintain sequence of orders in RecyclerView for a certain order tab.

    6) orderIdIndexHM
    It is a HashMap where key is order id and value is index of that order id in orderIdsByOrderTabHM (refer 5).

     */

    // initialization of final fields

    private HashMap<String,String> initializeOrderStatusToOrderTabMapping(){
        return new HashMap<String,String>(){
            {
                put(auxiliary.ORDERSTATUS_PLACED,auxiliary.ORDERTAB_INCOMING);
                put(auxiliary.ORDERSTATUS_ACCEPTED,auxiliary.ORDERTAB_INCOMING);
                put(auxiliary.ORDERSTATUS_CONFIRMED,auxiliary.ORDERTAB_CONFIRMED);
                put(auxiliary.ORDERSTATUS_PACKAGED,auxiliary.ORDERTAB_CONFIRMED);
                put(auxiliary.ORDERSTATUS_PENDING,auxiliary.ORDERTAB_PENDING);
                put(auxiliary.ORDERSTATUS_DISPATCHED,auxiliary.ORDERTAB_DISPATCHED);
                put(auxiliary.ORDERSTATUS_COMPLAINT,auxiliary.ORDERTAB_COMPLAINT);
            }
        };
    }

    // Getters

    private HashMap<String,String> getBasicOrderData(String order_id){
        return basicOrderDataHM.get(order_id);
    }

    public HashMap<String,HashMap<String,String>> getProdData(String order_id){
        return prodDataHM.get(order_id);
    }

    public ArrayList<String> getProdIds(String order_id){
        return prodIdsByOrderIdHM.get(order_id);
    }

    /*
    public ArrayList<String> getOrderIdsByOrderStatus(String order_status){
        return orderIdsByOrderStatusHM.get(order_status);
    }*/

    public ArrayList<String> getOrderIdsByOrderTab(String order_tab){
        return orderIdsByOrderTabHM.get(order_tab);
    }

    public String getOrderIdByTabAndPosition(String order_tab, int position){
        return getOrderIdsByOrderTab(order_tab).get(position);
    }

    public String getOrderTabByOrderId(String order_id){
        return orderStatusToOrderTabMappingHM.get(getOrderStatus(order_id));
    }

    public HashMap<String,HashMap<String,String>> getAvailableProdData(String order_id){
        HashMap<String,HashMap<String,String>> available_prod_data=new HashMap<String,HashMap<String,String>>();
        HashMap<String,HashMap<String,String>> prod_data=getProdData(order_id);
        for(HashMap.Entry<String,HashMap<String,String>> entry : prod_data.entrySet()){
            String prod_id=entry.getKey();
            HashMap<String,String> prod_attrs=entry.getValue();
            int prod_available_quantity=Integer.parseInt(prod_attrs.get(auxiliary.ORDER_PRODAVAILABLEQUANTITY));
            int prod_quantity=Integer.parseInt(prod_attrs.get(auxiliary.ORDER_PRODQUANTITY).trim());
            if(prod_available_quantity==prod_quantity){
                available_prod_data.put(prod_id,prod_attrs);
            }
        }
        return available_prod_data;
    }

    public HashMap<String,HashMap<String,String>> getPartiallyAvailableProdData(String order_id){
        HashMap<String,HashMap<String,String>> partiallyavailable_prod_data=new HashMap<String,HashMap<String,String>>();
        HashMap<String,HashMap<String,String>> prod_data=getProdData(order_id);
        for(HashMap.Entry<String,HashMap<String,String>> entry : prod_data.entrySet()){
            String prod_id=entry.getKey();
            HashMap<String,String> prod_attrs=entry.getValue();
            int prod_available_quantity=Integer.parseInt(prod_attrs.get(auxiliary.ORDER_PRODAVAILABLEQUANTITY));
            int prod_quantity=Integer.parseInt(prod_attrs.get(auxiliary.ORDER_PRODQUANTITY).trim());
            if(prod_available_quantity>0 && prod_available_quantity<prod_quantity){
                partiallyavailable_prod_data.put(prod_id,prod_attrs);
            }
        }
        return partiallyavailable_prod_data;
    }

    public HashMap<String,HashMap<String,String>> getUnvailableProdData(String order_id){
        HashMap<String,HashMap<String,String>> unavailable_prod_data=new HashMap<String,HashMap<String,String>>();
        HashMap<String,HashMap<String,String>> prod_data=getProdData(order_id);
        for(HashMap.Entry<String,HashMap<String,String>> entry : prod_data.entrySet()){
            String prod_id=entry.getKey();
            HashMap<String,String> prod_attrs=entry.getValue();
            int prod_available_quantity=Integer.parseInt(prod_attrs.get(auxiliary.ORDER_PRODAVAILABLEQUANTITY));
            if(prod_available_quantity==0){
                unavailable_prod_data.put(prod_id,prod_attrs);
            }
        }
        return unavailable_prod_data;
    }

    public String getDiffProdCount(String order_id){
        return basicOrderDataHM.get(order_id).get(auxiliary.ORDER_DIFFPRODCOUNT);
    }

    public String getDiffProdCountByTabAndPosition(String order_tab, int position){
        return getDiffProdCount(getOrderIdByTabAndPosition(order_tab,position));
    }

    public String getOrderTotal(String order_id){
        return basicOrderDataHM.get(order_id).get(auxiliary.ORDER_TOTAL);
    }

    public String getOrderTotalByTabAndPosition(String order_tab, int position){
        return getOrderTotal(getOrderIdByTabAndPosition(order_tab,position));
    }

    public String getOrderRevenue(String order_id){
        return basicOrderDataHM.get(order_id).get(auxiliary.ORDER_REVENUE);
    }

    public String getOrderRevenueByTabAndPosition(String order_tab, int position){
        return getOrderRevenue(getOrderIdByTabAndPosition(order_tab,position));
    }

    public String getPaymentStatus(String order_id){
        return basicOrderDataHM.get(order_id).get(auxiliary.ORDER_PAYMENTSTATUS);
    }

    public String getPaymentStatusByTabAndPosition(String order_tab, int position){
        return getPaymentStatus(getOrderIdByTabAndPosition(order_tab,position));
    }

    public String getOrderStatus(String order_id){
        return basicOrderDataHM.get(order_id).get(auxiliary.ORDER_STATUS);
    }

    public String getOrderStatusByTabAndPosition(String order_tab, int position){
        return getOrderStatus(getOrderIdByTabAndPosition(order_tab,position));
    }

    public String getOrderType(String order_id){
        return basicOrderDataHM.get(order_id).get(auxiliary.ORDER_TYPE);
    }

    public String getOrderTypeByTabAndPosition(String order_tab, int position){
        return getOrderType(getOrderIdByTabAndPosition(order_tab,position));
    }

    public String getOrderDateTime(String order_id){
        return basicOrderDataHM.get(order_id).get(auxiliary.ORDER_DATEANDTIME);
    }

    public String getOrderDateTimeByTabAndPosition(String order_tab, int position){
        return getOrderDateTime(getOrderIdByTabAndPosition(order_tab,position));
    }

    public int getOrderCountInTab(String order_tab){
        return orderIdsByOrderTabHM.get(order_tab).size();
    }

    // Changers and Markers

    public void markProductAvailable(String order_id,String prod_id){
        HashMap<String,String> prod_attrs=getProdData(order_id).get(prod_id);
        String prod_quantity=prod_attrs.get(auxiliary.ORDER_PRODQUANTITY);
        prod_attrs.put(auxiliary.ORDER_PRODAVAILABLEQUANTITY,prod_quantity);
    }

    public void markProductPartiallyAvailable(String order_id,String prod_id,String prod_availableQuantity){
        HashMap<String,String> prod_attrs=getProdData(order_id).get(prod_id);
        prod_attrs.put(auxiliary.ORDER_PRODAVAILABLEQUANTITY,prod_availableQuantity);
    }

    public void markProductUnavailable(String order_id,String prod_id){
        HashMap<String,String> prod_attrs=getProdData(order_id).get(prod_id);
        prod_attrs.put(auxiliary.ORDER_PRODAVAILABLEQUANTITY,"0");
    }

    public void changePaymentStatus(String order_id,String payment_status){
        basicOrderDataHM.get(order_id).put(auxiliary.ORDER_PAYMENTSTATUS,payment_status);
    }

    public void changeOrderStatus(String order_id,String new_order_status){
        String current_order_status=basicOrderDataHM.get(order_id).get(auxiliary.ORDER_STATUS);
        String current_order_tab=orderStatusToOrderTabMappingHM.get(current_order_status);
        String new_order_tab=orderStatusToOrderTabMappingHM.get(new_order_status);
        int current_order_index=(int)orderIdIndexHM.get(order_id);
        if(!current_order_tab.equals(new_order_tab)){
            changeOrderTab(order_id,new_order_tab);
        }
        basicOrderDataHM.get(order_id).put(auxiliary.ORDER_STATUS,new_order_status);
    }

    public void changeOrderTab(String order_id,String new_order_tab){
        String current_order_status=basicOrderDataHM.get(order_id).get(auxiliary.ORDER_STATUS);
        String current_order_tab=orderStatusToOrderTabMappingHM.get(current_order_status);
        int current_order_index=(int)orderIdIndexHM.get(order_id);
        orderIdsByOrderTabHM.get(current_order_tab).remove(current_order_index);
        orderIdsByOrderTabHM.get(new_order_tab).add(order_id);
        Integer new_order_index=orderIdsByOrderTabHM.get(new_order_tab).size()-1;
        orderIdIndexHM.put(order_id,new_order_index);
    }

    // Setters / Putters

    public void populateOrderData(String orderDataJSON){

    }

}
