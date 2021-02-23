package com.example.explorista_retailer.DataProviders;

import android.os.AsyncTask;

import com.example.explorista_retailer.auxiliary;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class OrderDataDP {

    private String orderDataJson;

    public OrderDataDP() {
        initializeOrderDataJson();
    }

    private void initializeOrderDataJson(){
        orderDataJson=fetchOrderDataJsonFromDatabase(auxiliary.PPV_SAMPLEURLWEBSERVICE,auxiliary.PPV_SAMPLESTOREID);
    }

    public String getOrderDataJson(){
        if(orderDataJson==null){
            initializeOrderDataJson();
        }
        return orderDataJson;
    }

    public String getFreshOrderDataJson(){
        initializeOrderDataJson();
        return orderDataJson;
    }

    private String fetchOrderDataJsonFromDatabase(final String urlWebService,final String store_id){
        class FetchOrderDataJsonFromDatabase extends AsyncTask<Void,Void,Void>{

            private String order_data_json;

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
                            put(auxiliary.PPK_STOREID,store_id);
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
                    this.order_data_json=sb.toString().trim();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            private String getOrderDataJson(){
                return this.order_data_json;
            }
        }
        FetchOrderDataJsonFromDatabase fetchOrderDataJsonFromDatabase=new FetchOrderDataJsonFromDatabase();
        try {
            fetchOrderDataJsonFromDatabase.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return fetchOrderDataJsonFromDatabase.getOrderDataJson();
    }
}
