package com.example.explorista_retailer;

import android.content.Context;
import android.content.Intent;
import android.icu.lang.UCharacter;
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

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Context mContext;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView navBarStoreNameTV,navBarStoreOwnerNameTV,navBarStoreRatingTV,statusTextTV;
    private Button navBarLogoutB;
    private Switch statusSwitchS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("EXPORISTA STORE");
        mContext=this;

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
        statusTextTV=findViewById(R.id.statusTextTV);
        statusSwitchS=findViewById(R.id.statusSwitchS);
        String store_id=auxiliaryuseraccountmanager.getStoreIdFromSP(mContext);
        Log.i("MainActivity","store id is : "+store_id);
        if(auxiliaryuseraccountmanager
                .storeIsOnline(auxiliary.SERVER_URL+"/store_userManagement.php"
                        ,store_id)){
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
}
