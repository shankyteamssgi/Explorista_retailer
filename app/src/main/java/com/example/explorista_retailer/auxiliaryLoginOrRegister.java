package com.example.explorista_retailer;

import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

class auxiliaryLoginOrRegister {

    static boolean gpsEnabled(Context context){
        boolean gps_enabled=false;
        LocationManager locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try{
            if(locationManager!=null){
                gps_enabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } else{
                Log.i("LOCATION","locationManager is null");
            }
        } catch (Exception e){
            Log.i("LOCATION","Exception occurred");
            e.printStackTrace();
        }
        return gps_enabled;
    }

    static String fetchCities(final String urlWebService){
        class FetchCities extends AsyncTask<Void,Void,Void> {

            private StringBuilder city_names;

            private FetchCities() {
                this.city_names=new StringBuilder();
                this.city_names.append(auxiliary.SPINNER_UNSELECTED_CITY);
                this.city_names.append("_");
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
                    this.city_names.append(sb.toString().trim());
                } catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            private String getCityNames(){
                return this.city_names.toString().trim();
            }
        }
        FetchCities fetchCities=new FetchCities();
        try {
            fetchCities.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return fetchCities.getCityNames();
    }

    static String fetchSectors(final String urlWebService){
        class FetchSectors extends AsyncTask<Void,Void,Void>{

            private StringBuilder sector_names;

            private FetchSectors() {
                this.sector_names=new StringBuilder();
                this.sector_names.append(auxiliary.SPINNER_UNSELECTED_SECTOR);
                this.sector_names.append("_");
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
                    this.sector_names.append(sb.toString().trim());
                } catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            private String getSectorNames(){
                return this.sector_names.toString().trim();
            }
        }
        FetchSectors fetchSectors=new FetchSectors();
        try {
            fetchSectors.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return fetchSectors.getSectorNames();
    }

    static String fetchAreasByCity(final String urlWebService,
                                    final String city_name){
        class FetchAreasByCity extends AsyncTask<Void,Void,Void>{

            private StringBuilder area_names;

            private FetchAreasByCity() {
                this.area_names=new StringBuilder();
                this.area_names.append(auxiliary.SPINNER_UNSELECTED_AREA);
                this.area_names.append("_");
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
                            put(auxiliary.PPK_CITYNAME,city_name);
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
                    this.area_names.append(sb.toString().trim());
                } catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            private String getAreaNames(){
                return this.area_names.toString().trim();
            }
        }
        FetchAreasByCity fetchAreasByCity=new FetchAreasByCity();
        try {
            fetchAreasByCity.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return fetchAreasByCity.getAreaNames();
    }

}
