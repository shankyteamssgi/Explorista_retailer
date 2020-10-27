package com.example.explorista_retailer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

class auxiliaryuseraccountmanager {

    private final static String TRUE="true";
    private final static String FALSE="false";

    private static final String SPFILE_STOREACCOUNTFIELDS="spfile_storeaccountfields";
    private static final String SPKEY_STOREID="spkey_storeid";
    private static final String SPKEY_STORENAME="spkey_storename";
    private static final String SPKEY_STOREPHONE="spkey_storephone";
    private static final String SPKEY_STOREOWNERNAME="spkey_storeownername";
    private static final String SPKEY_STORETIMINGS="spkey_storetimings";
    private static final String SPKEY_STORERATING="spkey_storerating";
    private static final String SPKEY_LOGINSTATUS="spkey_loginstatus";

    enum actionType{LOGIN,REGISTER};

    static String addUserToDb(final String urlWebService,
                            final String store_ownerName,
                            final String store_name,
                            final String store_addrLine1,
                            final String store_addrLine2,
                            final String store_landmark,
                            final String store_city,
                            final String store_area,
                            final String store_lat,
                            final String store_long,
                            final String store_sector,
                            final String store_gstin,
                            final String store_ownerPan,
                            final String store_ownerPhone,
                            final String store_ownerPassword,
                            final String store_fcmToken){
        class AddUserToDb extends AsyncTask<Void,Void,Void>{

            private String sql_response;

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.connect();
                    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                    dos.writeBytes(auxiliary.postParamsToString(new HashMap<String, String>() {
                        {
                            put(auxiliary.PPK_INITIAL_CHECK,auxiliary.PPV_INITIAL_CHECK);
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_USERADDITION);
                            put(auxiliary.PPK_STOREOWNERNAME,store_ownerName==null?"":store_ownerName);
                            put(auxiliary.PPK_STORENAME,store_name==null?"":store_name);
                            put(auxiliary.PPK_STOREADDRLINE1,store_addrLine1==null?"":store_addrLine1);
                            put(auxiliary.PPK_STOREADDRLINE2,store_addrLine2==null?"":store_addrLine2);
                            put(auxiliary.PPK_STORELANDMARK,store_landmark==null?"":store_landmark);
                            put(auxiliary.PPK_STORECITY,store_city==null?"":store_city);
                            put(auxiliary.PPK_STOREAREA,store_area==null?"":store_area);
                            put(auxiliary.PPK_STORELAT,store_lat==null?"":store_lat);
                            put(auxiliary.PPK_STORELONG,store_long==null?"":store_long);
                            put(auxiliary.PPK_STORESECTOR,store_sector==null?"":store_sector);
                            put(auxiliary.PPK_STOREGSTIN,store_gstin==null?"":store_gstin);
                            put(auxiliary.PPK_STOREOWNERPAN,store_ownerPan==null?"":store_ownerPan);
                            put(auxiliary.PPK_STOREOWNERPHONE,store_ownerPhone==null?"":store_ownerPhone);
                            put(auxiliary.PPK_STOREOWNERPASSWORD,store_ownerPassword==null?"":store_ownerPassword);
                            put(auxiliary.PPK_STOREFCMTOKEN,store_fcmToken==null?"":store_fcmToken);
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
                    this.sql_response=sb.toString().trim();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        AddUserToDb addUserToDb=new AddUserToDb();
        try {
            addUserToDb.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return addUserToDb.sql_response;
    }

    static boolean userWithThisPhoneExistsInDb(final String urlWebService,
                                               final String store_ownerPhone) {
        class UserWithThisPhoneExistsInDb extends AsyncTask<Void,Void,Void>{

            private boolean user_exists;

            private UserWithThisPhoneExistsInDb() {
                this.user_exists=false;
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
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_USEREXISTENCECHECKBYPHONE);
                            put(auxiliary.PPK_STOREOWNERPHONE,store_ownerPhone);
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
                    this.user_exists=sb.toString().trim().equalsIgnoreCase(auxiliaryuseraccountmanager.TRUE);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        UserWithThisPhoneExistsInDb userWithThisPhoneExistsInDb=new UserWithThisPhoneExistsInDb();
        try {
            userWithThisPhoneExistsInDb.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return userWithThisPhoneExistsInDb.user_exists;
    }

    static boolean validCredentials(final String urlWebService,
                                    final String store_ownerPhone,
                                    final String store_ownerPassword){
        class ValidCredentials extends AsyncTask<Void,Void,Void>{

            private boolean credential_validity;

            private ValidCredentials() {
                this.credential_validity=false;
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
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_CREDENTIALVERIFY);
                            put(auxiliary.PPK_STOREOWNERPHONE,store_ownerPhone);
                            put(auxiliary.PPK_STOREOWNERPASSWORD,store_ownerPassword);
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
                    //Log.i("auam","server response : "+sb.toString().trim());
                    this.credential_validity=sb.toString().trim().equalsIgnoreCase(auxiliaryuseraccountmanager.TRUE);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        ValidCredentials validCredentials=new ValidCredentials();
        try {
            validCredentials.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return validCredentials.credential_validity;
    }

    static boolean accountIsActive(final String urlWebService,
                                        final String store_ownerPhone){
        class AccountIsActive extends AsyncTask<Void,Void,Void>{

            private boolean activation_status;

            private AccountIsActive() {
                this.activation_status = false;
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
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_ACTIVATIONSTATUSCHECK);
                            put(auxiliary.PPK_STOREOWNERPHONE,store_ownerPhone);
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
                    Log.i("auam","accountIsActive phone received : "+store_ownerPhone);
                    Log.i("auam","accountIsActive status response : "+sb.toString().trim());
                    this.activation_status=sb.toString().trim().equalsIgnoreCase(TRUE);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        AccountIsActive accountIsActive=new AccountIsActive();
        try {
            accountIsActive.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return accountIsActive.activation_status;
    }

    static void setStoreTimings(final String urlWebService, final String formatted_time){
        class SetStoreTimings extends AsyncTask<Void,Void,Void>{
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
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_STORETIMESET);
                            put(auxiliary.PPK_STORETIMINGS,formatted_time);
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
                    Log.i("auam","setStoreTimings server response -> "+sb.toString().trim());
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        SetStoreTimings setStoreTimings=new SetStoreTimings();
        try {
            setStoreTimings.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static void setStoreLoginStatus(final String urlWebService, final String store_ownerPhone, final boolean login_status){
        class SetStoreLoginStatus extends AsyncTask<Void,Void,Void>{
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
                            put(auxiliary.PPK_REQUESTTYPE
                                    ,login_status
                                            ?auxiliary.PPV_REQUESTTYPE_SETLOGINSTATUSPOSITIVE
                                            :auxiliary.PPV_REQUESTTYPE_SETLOGINSTATUSNEGATIVE);
                            put(auxiliary.PPK_STOREOWNERPHONE,store_ownerPhone);
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
                    Log.i("auam","setStoreLoginStatus response ->"+sb.toString().trim());
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        SetStoreLoginStatus setStoreLoginStatus=new SetStoreLoginStatus();
        try {
            setStoreLoginStatus.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static HashMap<String,String> fetchStoreFields(final String urlWebService, final String store_ownerPhone){
        class FetchStoreFields extends AsyncTask<Void,Void,Void>{

            private HashMap<String,String> fetched_fieldsHM;

            private FetchStoreFields() {
                this.fetched_fieldsHM = new HashMap<String,String>();
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
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_FETCHSTOREFIELDS);
                            put(auxiliary.PPK_STOREOWNERPHONE,store_ownerPhone);
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
                    Log.i("auam","fetchStoreFields server response : "+sb.toString().trim());
                    JSONArray jsonArray=new JSONArray(sb.toString().trim());
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String store_id=jsonObject.getString(auxiliary.STORE_ID);
                    String store_name=jsonObject.getString(auxiliary.STORE_NAME);
                    String store_phone=jsonObject.getString(auxiliary.STORE_OWNERPHONE);
                    String store_retailerName=jsonObject.getString(auxiliary.STORE_OWNERNAME);
                    String store_rating=jsonObject.getString(auxiliary.STORE_RATING);
                    String store_openTimeMon=jsonObject.getString(auxiliary.STORE_OPENTIMEMON);
                    String store_closeTimeMon=jsonObject.getString(auxiliary.STORE_CLOSETIMEMON);
                    String store_openTimeTue=jsonObject.getString(auxiliary.STORE_OPENTIMETUE);
                    String store_closeTimeTue=jsonObject.getString(auxiliary.STORE_CLOSETIMETUE);
                    String store_openTimeWed=jsonObject.getString(auxiliary.STORE_OPENTIMEWED);
                    String store_closeTimeWed=jsonObject.getString(auxiliary.STORE_CLOSETIMEWED);
                    String store_openTimeThu=jsonObject.getString(auxiliary.STORE_OPENTIMETHU);
                    String store_closeTimeThu=jsonObject.getString(auxiliary.STORE_CLOSETIMETHU);
                    String store_openTimeFri=jsonObject.getString(auxiliary.STORE_OPENTIMEFRI);
                    String store_closeTimeFri=jsonObject.getString(auxiliary.STORE_CLOSETIMEFRI);
                    String store_openTimeSat=jsonObject.getString(auxiliary.STORE_OPENTIMESAT);
                    String store_closeTimeSat=jsonObject.getString(auxiliary.STORE_CLOSETIMESAT);
                    String store_openTimeSun=jsonObject.getString(auxiliary.STORE_OPENTIMESUN);
                    String store_closeTimeSun=jsonObject.getString(auxiliary.STORE_CLOSETIMESUN);

                    fetched_fieldsHM.put(auxiliary.STORE_ID,store_id);
                    fetched_fieldsHM.put(auxiliary.STORE_NAME,store_name);
                    fetched_fieldsHM.put(auxiliary.STORE_OWNERPHONE,store_phone);
                    fetched_fieldsHM.put(auxiliary.STORE_OWNERNAME,store_retailerName);
                    fetched_fieldsHM.put(auxiliary.STORE_RATING,store_rating);

                    StringBuilder store_timing=new StringBuilder();
                    store_timing.append(store_openTimeMon);
                    store_timing.append("_").append(store_closeTimeMon);
                    store_timing.append("_").append(store_openTimeTue);
                    store_timing.append("_").append(store_closeTimeTue);
                    store_timing.append("_").append(store_openTimeWed);
                    store_timing.append("_").append(store_closeTimeWed);
                    store_timing.append("_").append(store_openTimeThu);
                    store_timing.append("_").append(store_closeTimeThu);
                    store_timing.append("_").append(store_openTimeFri);
                    store_timing.append("_").append(store_closeTimeFri);
                    store_timing.append("_").append(store_openTimeSat);
                    store_timing.append("_").append(store_closeTimeSat);
                    store_timing.append("_").append(store_openTimeSun);
                    store_timing.append("_").append(store_closeTimeSun);
                    Log.i("auam"," store timing : "+store_timing.toString().trim());

                    fetched_fieldsHM.put(auxiliary.STORE_TIMING,store_timing.toString().trim());
                } catch (JSONException je){
                    je.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        FetchStoreFields fetchStoreFields=new FetchStoreFields();
        try {
            fetchStoreFields.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return fetchStoreFields.fetched_fieldsHM;
    }

    static void saveStoreFieldsToSP(Context context,
                                    String store_id,
                                    String store_name,
                                    String store_phone,
                                    String store_ownerName,
                                    String store_timings,
                                    String store_rating){
        SharedPreferences storeFieldsSP=context
                .getSharedPreferences(SPFILE_STOREACCOUNTFIELDS,Context.MODE_PRIVATE);
        storeFieldsSP.edit()
                .putString(SPKEY_STOREID,store_id)
                .putString(SPKEY_STORENAME,store_name)
                .putString(SPKEY_STOREPHONE,store_phone)
                .putString(SPKEY_STOREOWNERNAME,store_ownerName)
                .putString(SPKEY_STORETIMINGS,store_timings)
                .putString(SPKEY_STORERATING,store_rating)
                .putBoolean(SPKEY_LOGINSTATUS,true)
                .apply();
    }

    static void clearStoreFieldsFromSP(Context context){
        SharedPreferences storeFieldsSP=context
                .getSharedPreferences(SPFILE_STOREACCOUNTFIELDS,Context.MODE_PRIVATE);
        storeFieldsSP.edit()
                .putString(SPKEY_STOREID,null)
                .putString(SPKEY_STORENAME,null)
                .putString(SPKEY_STOREPHONE,null)
                .putString(SPKEY_STOREOWNERNAME,null)
                .putString(SPKEY_STORETIMINGS,null)
                .putString(SPKEY_STORERATING,null)
                .putBoolean(SPKEY_LOGINSTATUS,false)
                .apply();
    }

    static boolean userLoggedIn(Context context){
        SharedPreferences storeFieldsSP=context
                .getSharedPreferences(SPFILE_STOREACCOUNTFIELDS,Context.MODE_PRIVATE);
        return storeFieldsSP.getBoolean(SPKEY_LOGINSTATUS,false);
    }

    static void saveStoreTimingsToSP(Context context,
                                    String store_timings){
        SharedPreferences storeFieldsSP=context
                .getSharedPreferences(SPFILE_STOREACCOUNTFIELDS,Context.MODE_PRIVATE);
        storeFieldsSP.edit()
                .putString(SPKEY_STORETIMINGS,store_timings)
                .apply();
    }

    static String getStoreIdFromSP(Context context){
        SharedPreferences storeFieldsSP=context
                .getSharedPreferences(SPFILE_STOREACCOUNTFIELDS,Context.MODE_PRIVATE);
        return storeFieldsSP.getString(SPKEY_STOREID,null);
    }

    static String getStoreNameFromSP(Context context){
        SharedPreferences storeFieldsSP=context
                .getSharedPreferences(SPFILE_STOREACCOUNTFIELDS,Context.MODE_PRIVATE);
        return storeFieldsSP.getString(SPKEY_STORENAME,null);
    }

    static String getStorePhoneFromSP(Context context){
        SharedPreferences storeFieldsSP=context
                .getSharedPreferences(SPFILE_STOREACCOUNTFIELDS,Context.MODE_PRIVATE);
        return storeFieldsSP.getString(SPKEY_STOREPHONE,null);
    }

    static String getStoreOwnerNameFromSP(Context context){
        SharedPreferences storeFieldsSP=context
                .getSharedPreferences(SPFILE_STOREACCOUNTFIELDS,Context.MODE_PRIVATE);
        return storeFieldsSP.getString(SPKEY_STOREOWNERNAME,null);
    }

    static String getStoreTimingsFromSP(Context context){
        SharedPreferences storeFieldsSP=context
                .getSharedPreferences(SPFILE_STOREACCOUNTFIELDS,Context.MODE_PRIVATE);
        return storeFieldsSP.getString(SPKEY_STORETIMINGS,null);
    }

    static String getStoreRatingFromSP(Context context){
        SharedPreferences storeFieldsSP=context
                .getSharedPreferences(SPFILE_STOREACCOUNTFIELDS,Context.MODE_PRIVATE);
        return storeFieldsSP.getString(SPKEY_STORERATING,null);
    }

    static boolean storeTimeIsSet(Context context) {
        String store_timings = auxiliaryuseraccountmanager.getStoreTimingsFromSP(context);
        return store_timings != null && store_timings.contains(":");
    }

    static void logOut(Context context,String urlWebService,boolean finish_flag){
        /* 1) signs user out from firebase
        *  2) sets store's login status to false
        *  3) clears store's fields from SharedPrefs */

        try{
            Log.i("auam","entered logOut");
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            mAuth.signOut();
            String store_ownerPhone=getStorePhoneFromSP(context);
            Log.i("auam","logOut store phone : "+store_ownerPhone);
            auxiliaryuseraccountmanager.setStoreLoginStatus(auxiliary.SERVER_URL+"/store_userManagement.php"
                    ,store_ownerPhone
                    ,false);
            auxiliaryuseraccountmanager.clearStoreFieldsFromSP(context);
            if(finish_flag){
                ((Activity)context).finish();
            }
            Log.i("auam","exited logOut");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    static boolean storeIsOnline(final String urlWebService, final String store_id){
        class StoreIsOnline extends AsyncTask<Void,Void,Void>{

            private boolean store_is_online;

            private StoreIsOnline() {
                this.store_is_online = false;
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
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_FETCHSTOREONLINESTATUS);
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
                    this.store_is_online=sb.toString().trim().equalsIgnoreCase(auxiliaryuseraccountmanager.TRUE);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        StoreIsOnline storeIsOnline=new StoreIsOnline();
        try {
            storeIsOnline.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return storeIsOnline.store_is_online;
    }

    static void setStoreOnlineStatus(final String urlWebService, final String store_id, final boolean online_status){
        class SetStoreOnlineStatus extends AsyncTask<Void,Void,Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.connect();
                    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                    String _online_status=online_status
                            ?auxiliaryuseraccountmanager.TRUE
                            :auxiliaryuseraccountmanager.FALSE;
                    Log.i("auam","setStoreOnlineStatus online status : "+_online_status);
                    dos.writeBytes(auxiliary.postParamsToString(new HashMap<String, String>() {
                        {
                            put(auxiliary.PPK_INITIAL_CHECK,auxiliary.PPV_INITIAL_CHECK);
                            put(auxiliary.PPK_REQUESTTYPE,auxiliary.PPV_REQUESTTYPE_SETSTOREONLINESTATUS);
                            put(auxiliary.PPK_STOREID,store_id);
                            put(auxiliary.PPK_STOREONLINESTATUS,online_status
                                    ?auxiliaryuseraccountmanager.TRUE
                                    :auxiliaryuseraccountmanager.FALSE);
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
                    //Log.i("auam","setStoreOnlineStatus response : "+sb.toString().trim());
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        SetStoreOnlineStatus setStoreStatus=new SetStoreOnlineStatus();
        try {
            setStoreStatus.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
