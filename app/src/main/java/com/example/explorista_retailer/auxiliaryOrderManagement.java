package com.example.explorista_retailer;

import com.example.explorista_retailer.Models.OrderDataModel;
import com.example.explorista_retailer.Wrappers.OrderDataWrapper;

public class auxiliaryOrderManagement {

    //

    // Order methods

    private static int getOrderCount(String order_tab){
        return AppSingleton.getInstance()
                .getOrderDataWrapper(OrderDataWrapper.DATA_SOURCE.SPREF)
                .getOrderDataModel()
                .getOrderCountInTab(order_tab);
    }

    private static String getOrderId(String order_tab, int position){
        return AppSingleton.getInstance()
                .getOrderDataWrapper(OrderDataWrapper.DATA_SOURCE.SPREF)
                .getOrderDataModel()
                .getOrderIdByTabAndPosition(order_tab,position);
    }
    
    private static String getOrderProdCount(String order_tab, int position){
        return AppSingleton.getInstance()
                .getOrderDataWrapper(OrderDataWrapper.DATA_SOURCE.SPREF)
                .getOrderDataModel()
                .getDiffProdCountByTabAndPosition(order_tab,position);
    }
    
    private static String getOrderTotal(String order_tab, int position){
        return AppSingleton.getInstance()
                .getOrderDataWrapper(OrderDataWrapper.DATA_SOURCE.SPREF)
                .getOrderDataModel()
                .getOrderTotalByTabAndPosition(order_tab,position);
    }

    private static String getOrderRevenue(String order_tab, int position){
        return AppSingleton.getInstance()
                .getOrderDataWrapper(OrderDataWrapper.DATA_SOURCE.SPREF)
                .getOrderDataModel()
                .getOrderRevenueByTabAndPosition(order_tab,position);
    }

    private static String getPaymentStatus(String order_tab, int position){
        return AppSingleton.getInstance()
                .getOrderDataWrapper(OrderDataWrapper.DATA_SOURCE.SPREF)
                .getOrderDataModel()
                .getPaymentStatusByTabAndPosition(order_tab,position);
    }

    private static String getOrderStatus(String order_tab, int position){
        return AppSingleton.getInstance()
                .getOrderDataWrapper(OrderDataWrapper.DATA_SOURCE.SPREF)
                .getOrderDataModel()
                .getOrderStatusByTabAndPosition(order_tab,position);
    }

    private static String getOrderType(String order_tab, int position){
        return AppSingleton.getInstance()
                .getOrderDataWrapper(OrderDataWrapper.DATA_SOURCE.SPREF)
                .getOrderDataModel()
                .getOrderTypeByTabAndPosition(order_tab,position);
    }

    private static String getOrderDateTime(String order_tab, int position){
        return AppSingleton.getInstance()
                .getOrderDataWrapper(OrderDataWrapper.DATA_SOURCE.SPREF)
                .getOrderDataModel()
                .getOrderDateTimeByTabAndPosition(order_tab,position);
    }
    
    public static int getIncomingOrderCount(){
        return getOrderCount(auxiliary.ORDERTAB_INCOMING);
    }

    public static int getConfirmedOrderCount(){
        return getOrderCount(auxiliary.ORDERTAB_CONFIRMED);
    }

    public static int getDispatchedOrderCount(){
        return getOrderCount(auxiliary.ORDERTAB_DISPATCHED);
    }

    public static int getPendingOrderCount(){
        return getOrderCount(auxiliary.ORDERTAB_PENDING);
    }

    public static int getComplaintOrderCount(){
        return getOrderCount(auxiliary.ORDERTAB_COMPLAINT);
    }
    
    public static String getIncomingOrderId(int position){
        return getOrderId(auxiliary.ORDERTAB_INCOMING,position);
    }

    public static String getConfirmedOrderId(int position){
        return getOrderId(auxiliary.ORDERTAB_CONFIRMED,position);
    }

    public static String getDispatchedOrderId(int position){
        return getOrderId(auxiliary.ORDERTAB_DISPATCHED,position);
    }

    public static String getPendingOrderId(int position){
        return getOrderId(auxiliary.ORDERTAB_PENDING,position);
    }

    public static String getComplaintOrderId(int position){
        return getOrderId(auxiliary.ORDERTAB_COMPLAINT,position);
    }

    public static String getIncomingOrderProdCount(int position){
        return getOrderProdCount(auxiliary.ORDERTAB_INCOMING,position);
    }

    public static String getConfirmedOrderProdCount(int position){
        return getOrderProdCount(auxiliary.ORDERTAB_CONFIRMED,position);
    }

    public static String getDispatchedOrderProdCount(int position){
        return getOrderProdCount(auxiliary.ORDERTAB_DISPATCHED,position);
    }

    public static String getPendingOrderProdCount(int position){
        return getOrderProdCount(auxiliary.ORDERTAB_PENDING,position);
    }

    public static String getComplaintOrderProdCount(int position){
        return getOrderProdCount(auxiliary.ORDERTAB_COMPLAINT,position);
    }

    public static String getIncomingOrderTotal(int position){
        return getOrderTotal(auxiliary.ORDERTAB_INCOMING,position);
    }

    public static String getConfirmedOrderTotal(int position){
        return getOrderTotal(auxiliary.ORDERTAB_CONFIRMED,position);
    }

    public static String getDispatchedOrderTotal(int position){
        return getOrderTotal(auxiliary.ORDERTAB_DISPATCHED,position);
    }

    public static String getPendingOrderTotal(int position){
        return getOrderTotal(auxiliary.ORDERTAB_PENDING,position);
    }

    public static String getComplaintOrderTotal(int position){
        return getOrderTotal(auxiliary.ORDERTAB_COMPLAINT,position);
    }

    public static String getIncomingOrderRevenue(int position){
        return getOrderRevenue(auxiliary.ORDERTAB_INCOMING,position);
    }

    public static String getConfirmedOrderRevenue(int position){
        return getOrderRevenue(auxiliary.ORDERTAB_CONFIRMED,position);
    }

    public static String getDispatchedOrderRevenue(int position){
        return getOrderRevenue(auxiliary.ORDERTAB_DISPATCHED,position);
    }

    public static String getPendingOrderRevenue(int position){
        return getOrderRevenue(auxiliary.ORDERTAB_PENDING,position);
    }

    public static String getComplaintOrderRevenue(int position){
        return getOrderRevenue(auxiliary.ORDERTAB_COMPLAINT,position);
    }

    public static String getIncomingOrderPaymentStatus(int position){
        return getPaymentStatus(auxiliary.ORDERTAB_INCOMING,position);
    }

    public static String getConfirmedOrderPaymentStatus(int position){
        return getPaymentStatus(auxiliary.ORDERTAB_CONFIRMED,position);
    }

    public static String getDispatchedOrderPaymentStatus(int position){
        return getPaymentStatus(auxiliary.ORDERTAB_DISPATCHED,position);
    }

    public static String getPendingOrderPaymentStatus(int position){
        return getPaymentStatus(auxiliary.ORDERTAB_PENDING,position);
    }

    public static String getComplaintOrderPaymentStatus(int position){
        return getPaymentStatus(auxiliary.ORDERTAB_COMPLAINT,position);
    }

    public static String getIncomingOrderDateTime(int position){
        return getOrderDateTime(auxiliary.ORDERTAB_INCOMING,position);
    }

    public static String getConfirmedOrderDateTime(int position){
        return getOrderDateTime(auxiliary.ORDERTAB_CONFIRMED,position);
    }

    public static String getDispatchedOrderDateTime(int position){
        return getOrderDateTime(auxiliary.ORDERTAB_DISPATCHED,position);
    }

    public static String getPendingOrderDateTime(int position){
        return getOrderDateTime(auxiliary.ORDERTAB_PENDING,position);
    }

    public static String getComplaintOrderDateTime(int position){
        return getOrderDateTime(auxiliary.ORDERTAB_COMPLAINT,position);
    }

}
