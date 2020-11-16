package com.example.explorista_retailer;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.explorista_retailer.Fragments.complaintFragment;
import com.example.explorista_retailer.Fragments.confirmedFragment;
import com.example.explorista_retailer.Fragments.dispatchedFragment;
import com.example.explorista_retailer.Fragments.incomingFragment;
import com.example.explorista_retailer.Fragments.pendingFragment;

import java.util.ArrayList;

public class orderManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        setTitle("Order management");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Toolbar tool = findViewById(R.id.toolbar);
//        setSupportActionBar(tool);
//        getSupportActionBar().setTitle("Order management");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());



        viewpagerAdapter.addFragment(new incomingFragment(),"Incoming");
        viewpagerAdapter.addFragment(new confirmedFragment(),"Confirmed");
        viewpagerAdapter.addFragment(new dispatchedFragment(),"Dispatched");
        viewpagerAdapter.addFragment(new complaintFragment(),"Complaint");
        viewpagerAdapter.addFragment(new pendingFragment(),"Pending");


        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    class ViewpagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewpagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments= new ArrayList<>();
            this.titles = new ArrayList<>();

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }


    }
}