package com.example.explorista_retailer;

import android.content.Context;
import android.content.Intent;
import android.icu.lang.UCharacter;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Context mContext;
    private String storeId;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView navBarStoreNameTV,navBarStoreOwnerNameTV,navBarStoreRatingTV,statusTextTV,
            ordersUnderManagementCountTV,revenueGeneratedTV,ordersFulfilledCountTV;
    private Button navBarLogoutB,proceedToManageOrdersB,detailedStatsB,orderHistoryB;
    private Switch statusSwitchS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("EXPORISTA STORE");
        mContext=this;
        storeId=auxiliaryuseraccountmanager.getStoreIdFromSP(mContext);

        statusTextTV=findViewById(R.id.statusTextTV);
        statusSwitchS=findViewById(R.id.statusSwitchS);
        ordersUnderManagementCountTV=findViewById(R.id.ordersUnderManagementCountTV);
        revenueGeneratedTV=findViewById(R.id.revenueGeneratedTV);
        ordersFulfilledCountTV=findViewById(R.id.ordersFulfilledCountTV);
        proceedToManageOrdersB=findViewById(R.id.proceedToManageOrdersB);
        detailedStatsB=findViewById(R.id.detailedStatsB);
        orderHistoryB=findViewById(R.id.orderHistoryB);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View header=navigationView.getHeaderView(0);
        navBarStoreNameTV=header.findViewById(R.id.navBarStoreNameTV);
        navBarStoreOwnerNameTV=header.findViewById(R.id.navBarStoreOwnerNameTV);
        navBarStoreRatingTV=header.findViewById(R.id.navBarStoreRatingTV);
        navBarLogoutB=header.findViewById(R.id.navBarLogoutB);

        navBarStoreNameTV.setText(auxiliaryuseraccountmanager.getStoreNameFromSP(mContext));
        navBarStoreOwnerNameTV.setText(auxiliaryuseraccountmanager.getStoreOwnerNameFromSP(mContext));
        navBarStoreRatingTV.setText(auxiliaryuseraccountmanager.getStoreRatingFromSP(mContext));
        navBarLogoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                Intent mainActivityToLoginOrRegisterIntent=new Intent(MainActivity.this,loginOrRegister.class);
                startActivity(mainActivityToLoginOrRegisterIntent);
                auxiliaryuseraccountmanager.logOut(mContext,auxiliary.SERVER_URL+"/store_userManagement.php",true);
            }
        });
        Log.i("MainActivity","store id is : "+storeId);
        if(auxiliaryuseraccountmanager
                .storeIsOnline(auxiliary.SERVER_URL+"/store_userManagement.php"
                        ,storeId)){
            //Log.i("MainActivity","store is online");
            statusTextTV.setText(auxiliary.STORE_ONLINESTR);
            statusSwitchS.setChecked(true);
        } else{
            //Log.i("MainActivity","store is offline");
            statusTextTV.setText(auxiliary.STORE_OFFLINESTR);
            statusSwitchS.setChecked(false);
        }
        statusSwitchS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    // Online
                    statusTextTV.setText(auxiliary.STORE_ONLINESTR);
                    auxiliaryuseraccountmanager.setStoreOnlineStatus(auxiliary.SERVER_URL+"/store_userManagement.php"
                            ,auxiliaryuseraccountmanager.getStoreIdFromSP(mContext),true);
                    //Log.i("switch","enabled");
                } else{
                    // Offline
                    statusTextTV.setText(auxiliary.STORE_OFFLINESTR);
                    auxiliaryuseraccountmanager.setStoreOnlineStatus(auxiliary.SERVER_URL+"/store_userManagement.php"
                            ,auxiliaryuseraccountmanager.getStoreIdFromSP(mContext),false);
                    //Log.i("switch","disabled");
                }
            }
        });
        String orders_under_management_count=fetchOrdersUnderManagementCount(auxiliary.SERVER_URL+"/orderManagement.php");
        String revenue_generated=fetchRevenueGenerated(auxiliary.SERVER_URL+"/orderManagement.php");
        String orders_fulfilled_count=fetchOrdersFulfilled(auxiliary.SERVER_URL+"/orderManagement.php");
        ordersUnderManagementCountTV.setText(orders_under_management_count);
        revenueGeneratedTV.setText(revenue_generated);
        ordersFulfilledCountTV.setText(orders_fulfilled_count);
        proceedToManageOrdersB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivityToOrderManagementIntent=new Intent(MainActivity.this,orderManagement.class);
                startActivity(mainActivityToOrderManagementIntent);
            }
        });
        detailedStatsB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent mainActivityToDetailedStatsIntent=new Intent(MainActivity.this,detailedStats.class);
                startActivity(mainActivityToDetailedStatsIntent);
            }
        });
        orderHistoryB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivityToOrderHistoryIntent=new Intent(MainActivity.this,orderHistory.class);
                startActivity(mainActivityToOrderHistoryIntent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int menu_item_id=item.getItemId();
        if(menu_item_id==R.id.nav_account){
            startActivity(new Intent(MainActivity.this,account.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(menu_item_id==R.id.nav_orderHistory){
            startActivity(new Intent(MainActivity.this,orderHistory.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(menu_item_id==R.id.nav_listedProducts){
            startActivity(new Intent(MainActivity.this,listedProducts.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(menu_item_id==R.id.nav_viewOrEditTimings){
            startActivity(new Intent(MainActivity.this,setTiming.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(menu_item_id==R.id.nav_orderManagement){
            startActivity(new Intent(MainActivity.this,orderManagement.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private String fetchOrdersUnderManagementCount(final String urlWebService){
        class FetchOrdersUnderManagementCount extends AsyncTask<Void,Void,Void>{

            StringBuilder orders_under_management_count;

            private FetchOrdersUnderManagementCount() {
                orders_under_management_count=new StringBuilder();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.connect();
                    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                    dos.writeBytes(auxiliary.postParamsToString(new HashMap<String, String>() {
                        {
                            put(auxiliary.PPK_INITIAL_CHECK,auxiliary.PPV_INITIAL_CHECK);
                            put(auxiliary.PPK_STOREID,storeId);
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_FETCHORDERSUNDERMANAGEMENTCOUNT);
                        }
                    }));
                    dos.flush();
                    dos.close();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json);
                    }
                    String server_response=sb.toString().trim();
                    Log.i("server response : ",server_response);
                    this.orders_under_management_count.append(auxiliary.hasOnlyNumbers(server_response)?server_response:"-");
                } catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            private String getOrdersUnderManagementCount(){
                return this.orders_under_management_count.toString();
            }
        }
        FetchOrdersUnderManagementCount fetchOrdersUnderManagementCount=new FetchOrdersUnderManagementCount();
        try {
            fetchOrdersUnderManagementCount.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return fetchOrdersUnderManagementCount.getOrdersUnderManagementCount();
    }

    private String fetchRevenueGenerated(final String urlWebService){
        class FetchRevenueGenerated extends AsyncTask<Void,Void,Void>{

            StringBuilder revenue_generated;

            private FetchRevenueGenerated() {
                revenue_generated=new StringBuilder();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.connect();
                    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                    dos.writeBytes(auxiliary.postParamsToString(new HashMap<String, String>() {
                        {
                            put(auxiliary.PPK_INITIAL_CHECK,auxiliary.PPV_INITIAL_CHECK);
                            put(auxiliary.PPK_STOREID,storeId);
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_FETCHREVENUEGENERATED);
                        }
                    }));
                    dos.flush();
                    dos.close();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json);
                    }
                    String server_response=sb.toString().trim();
                    Log.i("server response : ",server_response);
                    this.revenue_generated.append(auxiliary.hasOnlyNumbers(server_response)?server_response:"0");
                } catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            private String getRevenueGenerated(){
                return this.revenue_generated.toString();
            }
        }
        FetchRevenueGenerated fetchRevenueGenerated=new FetchRevenueGenerated();
        try {
            fetchRevenueGenerated.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return fetchRevenueGenerated.getRevenueGenerated();
    }

    private String fetchOrdersFulfilled(final String urlWebService){
        class FetchOrdersFulfilled extends AsyncTask<Void,Void,Void>{

            StringBuilder orders_fulfilled;

            private FetchOrdersFulfilled() {
                orders_fulfilled=new StringBuilder();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.connect();
                    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                    dos.writeBytes(auxiliary.postParamsToString(new HashMap<String, String>() {
                        {
                            put(auxiliary.PPK_INITIAL_CHECK,auxiliary.PPV_INITIAL_CHECK);
                            put(auxiliary.PPK_STOREID,storeId);
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_FETCHORDERSFULFILLED);
                        }
                    }));
                    dos.flush();
                    dos.close();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json);
                    }
                    String server_response=sb.toString().trim();
                    Log.i("server response : ",server_response);
                    this.orders_fulfilled.append(auxiliary.hasOnlyNumbers(server_response)?server_response:"0");
                } catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            private String getOrdersFulfilled(){
                return this.orders_fulfilled.toString();
            }
        }
        FetchOrdersFulfilled fetchOrdersFulfilled=new FetchOrdersFulfilled();
        try {
            fetchOrdersFulfilled.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return fetchOrdersFulfilled.getOrdersFulfilled();
    }

}
