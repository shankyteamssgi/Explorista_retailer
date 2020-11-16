package com.example.explorista_retailer.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.explorista_retailer.R;


public class dispatchedFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view;

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dispatched, container, false);
        return view;
    }
}