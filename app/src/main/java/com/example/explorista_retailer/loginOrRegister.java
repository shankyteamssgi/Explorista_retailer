package com.example.explorista_retailer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class loginOrRegister extends AppCompatActivity {

    Context mContext;
    enum userPermissionResponse{LOCATION_NOT_ENABLED,PERMISSION_DENIED,PERMISSION_DENIED_WITH_NEVERASKAGAIN};
    enum loginWith{OTP,PASSWORD,NONE};

    // Progress bar
    ProgressBar locationPB;

    // Heading views
    private TextView loginTV,registerTV;

    // Login views
    private LinearLayout phoneLoginLL;
    private EditText phoneNoWithOtpET,phoneNoWithPasswordET,passwordLoginET;
    private Button phoneLoginWithOtpB,phoneLoginWithPasswordB;

    // Register views
    private LinearLayout registrationDetailsLL,touConfirmationLL;
    private EditText storeOwnerNameET,storeNameET,storeAddrLine1ET,storeAddrLine2ET,storeLanmarkET
            ,storeGstinET,storeOwnerPanET,storeOwnerPhoneET,passwordET,confirmPasswordET;
    private Spinner storeCitySpinner,storeAreaSpinner,storeSectorSpinner;
    private CheckBox touConfirmationCB;
    private TextView touConfirmationTV;
    private Button proceedToRegisterB;

    // Register variables
    private String store_ownerName;
    private String store_name;
    private String store_addrLine1;
    private String store_addrLine2;
    private String store_landmark;
    private String store_lat;
    private String store_long;
    private String store_city;
    private String store_area;
    private String store_sector;
    private String store_gstin;
    private String store_ownerPan;
    private String store_ownerPhone;
    private String store_ownerPassword;
    private String store_ownerPasswordConfirm;

    // Login variables
    private String phoneNoForOtpLogin,phoneNoForPasswordLogin;
    private String passwordLogin;

    // Location
    private static final int REQUEST_CODE_LOCATION_PERMISSION=1;
    private static final int REQUEST_CHECK_SETTINGS=2;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // If (user is logged in & time is set) -> proceed to MainActivity.
        // If (user is logged in & time is not set) -> proceed to setTiming activity.
        mContext=this;
        if(auxiliaryuseraccountmanager.userLoggedIn(mContext)){
            if(auxiliaryuseraccountmanager.storeTimeIsSet(mContext)){
                proceedToMainActivity();
            } else{
                proceedToSetTimingActivity();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        Log.i("LOGINORREGISTER","inside oncreate");

        // Progress bar
        locationPB=findViewById(R.id.locationPB);

        // Heading views
        loginTV=findViewById(R.id.loginTV);
        registerTV=findViewById(R.id.registerTV);

        // Login views
        phoneLoginLL=findViewById(R.id.phoneLoginLL);
        phoneNoWithOtpET=findViewById(R.id.phoneNoWithOtpET);
        phoneNoWithPasswordET=findViewById(R.id.phoneNoWithPasswordET);
        passwordLoginET=findViewById(R.id.passwordLoginET);
        phoneLoginWithOtpB=findViewById(R.id.phoneLoginWithOtpB);
        phoneLoginWithPasswordB=findViewById(R.id.phoneLoginWithPasswordB);

        // Register views
        registrationDetailsLL=findViewById(R.id.registrationDetailsLL);
        touConfirmationLL=findViewById(R.id.touConfirmationLL);
        storeOwnerNameET=findViewById(R.id.storeOwnerNameET);
        storeNameET=findViewById(R.id.storeNameET);
        storeAddrLine1ET=findViewById(R.id.storeAddrLine1ET);
        storeAddrLine2ET=findViewById(R.id.storeAddrLine2ET);
        storeLanmarkET=findViewById(R.id.storeLanmarkET);
        storeGstinET=findViewById(R.id.storeGstinET);
        storeOwnerPanET=findViewById(R.id.storeOwnerPanET);
        storeOwnerPhoneET=findViewById(R.id.storeOwnerPhoneET);
        passwordET=findViewById(R.id.passwordET);
        confirmPasswordET=findViewById(R.id.confirmPasswordET);
        storeCitySpinner=findViewById(R.id.storeCitySpinner);
        storeAreaSpinner=findViewById(R.id.storeAreaSpinner);
        storeSectorSpinner=findViewById(R.id.storeSectorSpinner);
        touConfirmationCB=findViewById(R.id.touConfirmationCB);
        touConfirmationTV=findViewById(R.id.touConfirmationTV);
        proceedToRegisterB=findViewById(R.id.proceedToRegisterB);

        storeCitySpinner.setAdapter(new ArrayAdapter<String>(mContext
                ,android.R.layout.simple_spinner_dropdown_item
                ,auxiliaryLoginOrRegister.fetchCities(auxiliary.SERVER_URL+"/fetchCities.php").split("_")));
        storeCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==0){
                    storeAreaSpinner.setAdapter(new ArrayAdapter<String>(mContext
                            ,android.R.layout.simple_spinner_dropdown_item
                            ,new String[]{auxiliary.SPINNER_UNSELECTED_AREA}));
                } else{
                    storeAreaSpinner.setAdapter(
                            new ArrayAdapter<String>(
                                    mContext
                                    ,android.R.layout.simple_spinner_dropdown_item
                                    ,auxiliaryLoginOrRegister.fetchAreasByCity(auxiliary.SERVER_URL+"/fetchAreasByCity.php"
                                    ,adapterView.getItemAtPosition(position).toString().trim()).split("_")));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                storeAreaSpinner.setAdapter(null);
            }
        });
        storeAreaSpinner.setAdapter(new ArrayAdapter<String>(mContext
                ,android.R.layout.simple_spinner_dropdown_item
                ,new String[]{auxiliary.SPINNER_UNSELECTED_AREA}));
        storeSectorSpinner.setAdapter(new ArrayAdapter<String>(mContext
                ,android.R.layout.simple_spinner_dropdown_item
                ,auxiliaryLoginOrRegister.fetchSectors(auxiliary.SERVER_URL+"/fetchSectors.php").split("_")));

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTV.setBackgroundColor(getResources().getColor(R.color.colorActionBarBackground));
                loginTV.setTextColor(getResources().getColor(R.color.colorActionBarTitleText));
                loginTV.setTextSize(18);
                registerTV.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                registerTV.setTextColor(getResources().getColor(R.color.colorProductBrand));
                registerTV.setTextSize(14);
                registrationDetailsLL.setVisibility(View.GONE);
                phoneLoginLL.setVisibility(View.VISIBLE);
            }
        });
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerTV.setBackgroundColor(getResources().getColor(R.color.colorActionBarBackground));
                registerTV.setTextColor(getResources().getColor(R.color.colorActionBarTitleText));
                registerTV.setTextSize(18);
                loginTV.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                loginTV.setTextColor(getResources().getColor(R.color.colorProductBrand));
                loginTV.setTextSize(14);
                phoneLoginLL.setVisibility(View.GONE);
                registrationDetailsLL.setVisibility(View.VISIBLE);
            }
        });
        phoneLoginWithPasswordB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignViews(auxiliaryuseraccountmanager.actionType.LOGIN,loginWith.PASSWORD);
                if(fieldCheckOk(auxiliaryuseraccountmanager.actionType.LOGIN,loginWith.PASSWORD)){
                    if(auxiliaryuseraccountmanager
                            .userWithThisPhoneExistsInDb(auxiliary.SERVER_URL+"/store_userManagement.php"
                                    ,phoneNoForPasswordLogin)){
                        if(auxiliaryuseraccountmanager.accountIsActive(auxiliary.SERVER_URL+"/store_userManagement.php"
                                ,phoneNoForPasswordLogin)){
                            if(auxiliaryuseraccountmanager.validCredentials(auxiliary.SERVER_URL+"/store_userManagement.php"
                                    ,phoneNoForPasswordLogin,passwordLogin)){
                                //proceedToMainActivity();
                                // 1) fetch store's detail by store's phone
                                // 2) save fetched details in SharedPrefs
                                HashMap<String,String> fetched_fieldsHM=auxiliaryuseraccountmanager
                                        .fetchStoreFields(auxiliary.SERVER_URL+"/store_userManagement.php"
                                                ,phoneNoForPasswordLogin);
                                auxiliaryuseraccountmanager.saveStoreFieldsToSP(mContext
                                        ,fetched_fieldsHM.get(auxiliary.STORE_ID)
                                        ,fetched_fieldsHM.get(auxiliary.STORE_NAME)
                                        ,fetched_fieldsHM.get(auxiliary.STORE_OWNERPHONE)
                                        ,fetched_fieldsHM.get(auxiliary.STORE_OWNERNAME)
                                        ,fetched_fieldsHM.get(auxiliary.STORE_TIMING)
                                        ,fetched_fieldsHM.get(auxiliary.STORE_RATING));
                                auxiliaryuseraccountmanager
                                        .setStoreLoginStatus(auxiliary.SERVER_URL+"/store_userManagement.php"
                                                ,fetched_fieldsHM.get(auxiliary.STORE_OWNERPHONE)
                                                ,true);
                                if(auxiliaryuseraccountmanager.storeTimeIsSet(mContext)){
                                    proceedToMainActivity();
                                } else{
                                    proceedToSetTimingActivity();
                                }
                            } else{
                                Toast.makeText(mContext,R.string.incorrect_password,Toast.LENGTH_LONG).show();
                            }
                        } else{
                            Toast.makeText(mContext,R.string.inactive_account,Toast.LENGTH_LONG).show();
                            locationPB.setVisibility(View.GONE);
                        }
                    } else{
                        Toast.makeText(mContext,R.string.phone_does_not_exist,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        phoneLoginWithOtpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignViews(auxiliaryuseraccountmanager.actionType.LOGIN,loginWith.OTP);
                if(fieldCheckOk(auxiliaryuseraccountmanager.actionType.LOGIN,loginWith.OTP)){
                    if(auxiliaryuseraccountmanager
                            .userWithThisPhoneExistsInDb(auxiliary.SERVER_URL+"/store_userManagement.php"
                                    ,phoneNoForOtpLogin)){
                        if(auxiliaryuseraccountmanager.accountIsActive(auxiliary.SERVER_URL+"/store_userManagement.php"
                                ,phoneNoForOtpLogin)){
                            proceedToOtpVerification(auxiliaryuseraccountmanager.actionType.LOGIN);
                        } else{
                            Toast.makeText(mContext,R.string.inactive_account,Toast.LENGTH_LONG).show();
                        }
                    } else{
                        Toast.makeText(mContext,R.string.phone_does_not_exist,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        setLocationRequest();
        proceedToRegisterB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                proceedToOtpVerificationInputBypass(auxiliaryuseraccountmanager.actionType.REGISTER); For testing*/
                assignViews(auxiliaryuseraccountmanager.actionType.REGISTER,loginWith.NONE);
                if(fieldCheckOk(auxiliaryuseraccountmanager.actionType.REGISTER,loginWith.NONE)){
                    if(auxiliaryLoginOrRegister.gpsEnabled(mContext)){
                        //Log.i("LOCATION","Gps is enabled");
                        // Call below code only when location access is granted and lat, long is fetched
                        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                                ==PackageManager.PERMISSION_GRANTED){
                            Log.i("LOCATION","Permission already granted");
                            //getCurrentLocation();
                            if(!auxiliaryuseraccountmanager
                                    .userWithThisPhoneExistsInDb(auxiliary.SERVER_URL+"/store_userManagement.php"
                                            ,store_ownerPhone)){
                                proceedToOtpVerification(auxiliaryuseraccountmanager.actionType.REGISTER);
                            } else{
                                Toast.makeText(mContext,"user with this phone already exists",Toast.LENGTH_LONG).show();
                            }
                        } else{
                            Log.i("LOCATION","Permission not granted. Requesting permission.");
                            ActivityCompat.requestPermissions(loginOrRegister.this
                                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                    ,REQUEST_CODE_LOCATION_PERMISSION);
                        }
                    } else{
                        Log.i("LOCATION","Gps is disabled.");
                        setLocationSettings();
                    }
                }
            }
        });
        loginTV.performClick();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*
        if(requestCode==REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
                proceedToOtpVerification(auxiliaryuseraccountmanager.actionType.REGISTER);
            } else{
                Toast.makeText(mContext,"Permission denied!",Toast.LENGTH_LONG).show();
            }
        }*/
        for(String permission : permissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,permission)){
                Log.i("LOCATION","Permission denied");
                msgExplanation(userPermissionResponse.PERMISSION_DENIED);
            } else{
                if(ActivityCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_GRANTED){
                    //getCurrentLocation();
                    if(!auxiliaryuseraccountmanager
                            .userWithThisPhoneExistsInDb(auxiliary.SERVER_URL+"/store_userManagement.php"
                                    ,store_ownerPhone)){
                        proceedToOtpVerification(auxiliaryuseraccountmanager.actionType.REGISTER);
                    } else{
                        Toast.makeText(mContext,"user already exists",Toast.LENGTH_LONG).show();
                    }
                } else{
                    Log.i("LOCATION","set to never ask again");
                    msgExplanation(userPermissionResponse.PERMISSION_DENIED_WITH_NEVERASKAGAIN);
                }
            }
        }
    }

    private void proceedToMainActivity(){
        Intent loginOrRegisterToMainActivityIntent=new Intent(loginOrRegister.this,MainActivity.class);
        startActivity(loginOrRegisterToMainActivityIntent);
        finish();
    }

    private void proceedToSetTimingActivity(){
        Intent loginOrRegisterToSetTimingIntent=new Intent(loginOrRegister.this,setTiming.class);
        startActivity(loginOrRegisterToSetTimingIntent);
        finish();
    }

    private void proceedToOtpVerification(final auxiliaryuseraccountmanager.actionType action_type){
        Log.i("MSG","inside proceedToOtpVerification");
        switch (action_type) {
            case REGISTER:
                locationPB.setVisibility(View.VISIBLE);
                LocationServices.getFusedLocationProviderClient(loginOrRegister.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                LocationServices.getFusedLocationProviderClient(loginOrRegister.this)
                                        .removeLocationUpdates(this);
                                if (locationResult != null) {
                                    if (locationResult.getLocations().size() > 0) {
                                        Log.i("lor", "locationResult size > 0");
                                        int latestLocationIndex = locationResult.getLocations().size() - 1;
                                        Intent loginOrRegisterToOtpVerificationIntent = new Intent(
                                                loginOrRegister.this,
                                                otpVerification.class);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.ACTION_TYPE, action_type);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERNAME, store_ownerName);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_NAME, store_name);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_ADDRLINE1, store_addrLine1);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_ADDRLINE2, store_addrLine2);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_LANDMARK, store_landmark);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_LAT
                                                , Double.toString(locationResult.getLocations().get(latestLocationIndex).getLatitude()));
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_LONG
                                                , Double.toString(locationResult.getLocations().get(latestLocationIndex).getLongitude()));
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_CITY, store_city);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_AREA, store_area);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_SECTOR, store_sector);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_GSTIN, store_gstin);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERPAN, store_ownerPan);
                                        loginOrRegisterToOtpVerificationIntent
                                                .putExtra(auxiliary.STORE_OWNERPHONE, "+91" + store_ownerPhone);
                                        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERPASSWORD, store_ownerPassword);
                                        startActivity(loginOrRegisterToOtpVerificationIntent);
                                    } else {
                                        Log.i("LOCATION", "getLocations().size() not greater than 0");
                                    }
                                } else {
                                    Log.i("LOCATION", "locationResult is null");
                                }
                                locationPB.setVisibility(View.GONE);
                            }
                        }, Looper.getMainLooper());
        /*
        Intent loginOrRegisterToOtpVerificationIntent=new Intent(loginOrRegister.this,otpVerification.class);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.ACTION_TYPE,action_type);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERNAME,store_ownerName);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_NAME,store_name);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_ADDRLINE1,store_addrLine1);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_ADDRLINE2,store_addrLine2);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_LANDMARK,store_landmark);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_LAT,store_lat);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_LONG,store_long);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_CITY,store_city);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_AREA,store_area);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_SECTOR,store_sector);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_GSTIN,store_gstin);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERPAN,store_ownerPan);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERPHONE,"+91"+store_ownerPhone);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERPASSWORD,store_ownerPassword);
        startActivity(loginOrRegisterToOtpVerificationIntent);*/
                break;
            case LOGIN:
                Intent loginOrRegisterToOtpVerificationIntent = new Intent(loginOrRegister.this, otpVerification.class);
                loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.ACTION_TYPE, action_type);
                loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERPHONE,"+91"+phoneNoForOtpLogin);
                startActivity(loginOrRegisterToOtpVerificationIntent);
                break;
        }
        finish();
    }

    private void proceedToOtpVerificationInputBypass(auxiliaryuseraccountmanager.actionType action_type){
        // Bypasses edittext input
        Log.i("MSG","inside proceedToOtpVerification");
        Intent loginOrRegisterToOtpVerificationIntent=new Intent(loginOrRegister.this,otpVerification.class);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.ACTION_TYPE,action_type);
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERNAME,"aman");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_NAME,"aman kirana");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_ADDRLINE1,"shop no 45");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_ADDRLINE2,"baskerville");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_LANDMARK,"city school");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_LAT,"23.345678");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_LONG,"24.56789");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_CITY,"bhilai");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_AREA,"chauhan town");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_SECTOR,"grocery");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_GSTIN,"sjfkasfg");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERPAN,"sfassf");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERPHONE,"+918770415342");
        loginOrRegisterToOtpVerificationIntent.putExtra(auxiliary.STORE_OWNERPASSWORD,"hello12");
        startActivity(loginOrRegisterToOtpVerificationIntent);
    }

    private void assignViews(auxiliaryuseraccountmanager.actionType action_type,loginWith login_with){
        //Log.i("LOGINORREGISTER","inside assignViews");
        switch (action_type) {
            case REGISTER:
                store_ownerName = storeOwnerNameET.getText().toString().trim();
                store_name = storeNameET.getText().toString().trim();
                store_addrLine1 = storeAddrLine1ET.getText().toString().trim();
                store_addrLine2 = storeAddrLine2ET.getText().toString().trim();
                store_landmark = storeLanmarkET.getText().toString().trim();
                store_landmark = store_landmark.isEmpty()?null:store_landmark;
                store_city = storeCitySpinner.getSelectedItem().toString().trim();
                store_area = storeAreaSpinner.getSelectedItem().toString().trim();
                store_sector = storeSectorSpinner.getSelectedItem().toString().trim();
                store_gstin = storeGstinET.getText().toString().trim();
                store_ownerPan = storeOwnerPanET.getText().toString().trim();
                store_ownerPhone = storeOwnerPhoneET.getText().toString().trim();
                store_ownerPassword = passwordET.getText().toString().trim();
                store_ownerPasswordConfirm = confirmPasswordET.getText().toString().trim();
                break;
            case LOGIN:
                switch (login_with){
                    case OTP:
                        phoneNoForOtpLogin=phoneNoWithOtpET.getText().toString().trim();
                        //Log.i("LOGINORREGISTER","inside login with otp");
                        break;
                    case PASSWORD:
                        Log.i("LOGINORREGISTER","inside login with password");
                        phoneNoForPasswordLogin=phoneNoWithPasswordET.getText().toString().trim();
                        passwordLogin=passwordLoginET.getText().toString().trim();
                        break;
                }
                break;
        }
    }

    private boolean fieldCheckOk(auxiliaryuseraccountmanager.actionType action_type,loginWith login_with){
        Log.i("LOGINORREGISTER","inside fieldCheckOk");
        boolean fieldsCheckResult=true;

        switch(action_type){
            case LOGIN:
                //Log.i("LOGINORREGISTER","fieldCheckOk case LOGIN");
                switch (login_with){
                    case OTP:
                        //Log.i("LOGINORREGISTER","phoneNoForOtpLogin : "+phoneNoForOtpLogin);
                        if(phoneNoForOtpLogin.length()!=10){
                            phoneNoWithOtpET.setError("Invalid phone number");
                            fieldsCheckResult=false;
                        }
                        break;
                    case PASSWORD:
                        if(phoneNoForPasswordLogin.length()!=10){
                            phoneNoWithPasswordET.setError("Invalid phone number");
                            fieldsCheckResult=false;
                        }
                        if(passwordLogin.isEmpty()){
                            passwordLoginET.setError("Cannot be empty");
                        } else{
                            if(passwordLogin.length()<auxiliary.PASSWORD_MINLENGTH){
                                passwordLoginET.setError("Minimum length required is "+Integer.toString(auxiliary.PASSWORD_MINLENGTH));
                            }
                        }
                        break;
                }
                break;
            case REGISTER:
                StringBuilder toastSb=new StringBuilder();
                if(store_ownerName.isEmpty()){
                    storeOwnerNameET.setError("Invalid name");
                    fieldsCheckResult=false;
                }
                if(store_name.isEmpty()){
                    storeNameET.setError("Invalid store name");
                    fieldsCheckResult=false;
                }
                if(store_addrLine1.isEmpty()){
                    storeAddrLine1ET.setError("Invalid address");
                    fieldsCheckResult=false;
                }
                if(store_addrLine2.isEmpty()){
                    storeAddrLine2ET.setError("Invalid address");
                    fieldsCheckResult=false;
                }
                if(store_city.equals(auxiliary.SPINNER_UNSELECTED_CITY)){
                    toastSb.append("Select City");
                    fieldsCheckResult=false;
                }
                if(store_area.equals(auxiliary.SPINNER_UNSELECTED_AREA)){
                    toastSb.append(toastSb.length()==0?"Select Area":", Select Area");
                    fieldsCheckResult=false;
                }
                if(store_sector.equals(auxiliary.SPINNER_UNSELECTED_SECTOR)){
                    toastSb.append(toastSb.length()==0?"Select Sector":", Select Sector");
                    fieldsCheckResult=false;
                }
                if(store_gstin.isEmpty()){
                    storeGstinET.setError("Invalid gstin");
                    fieldsCheckResult=false;
                }
                if(store_ownerPan.isEmpty()){
                    storeOwnerPanET.setError("Invalid PAN");
                    fieldsCheckResult=false;
                }
                if(store_ownerPhone.length()!=10){
                    storeOwnerPhoneET.setError("Invalid phone");
                    fieldsCheckResult=false;
                }
                if(store_ownerPassword.isEmpty()){
                    passwordET.setError("Cannot be empty");
                    fieldsCheckResult=false;
                }
                if(store_ownerPasswordConfirm.isEmpty()){
                    confirmPasswordET.setError("Cannot be empty");
                    fieldsCheckResult=false;
                }
                if(!store_ownerPassword.isEmpty() &&
                        !store_ownerPasswordConfirm.isEmpty() &&
                        !store_ownerPassword.equals(store_ownerPasswordConfirm)){
                    confirmPasswordET.setError("Passwords do not match");
                    fieldsCheckResult=false;
                }
                if(!touConfirmationCB.isChecked()){
                    toastSb.append(toastSb.length()==0?"Check terms of use box":", Check terms of use box");
                    fieldsCheckResult=false;
                }
                Toast.makeText(mContext,toastSb,Toast.LENGTH_LONG).show();
                break;
        }
        return fieldsCheckResult;
    }

    private void getCurrentLocation(){
        locationPB.setVisibility(View.VISIBLE);
        LocationServices.getFusedLocationProviderClient(loginOrRegister.this)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(loginOrRegister.this)
                                .removeLocationUpdates(this);
                        if(locationResult!=null){
                            if(locationResult.getLocations().size()>0){
                                int latestLocationIndex=locationResult.getLocations().size()-1;
                                store_lat=Double.toString(locationResult.getLocations().get(latestLocationIndex).getLatitude());
                                store_long=Double.toString(locationResult.getLocations().get(latestLocationIndex).getLongitude());
                                Log.i("LOCATION","Latitude : "+store_lat);
                                Log.i("LOCATION","Longitude : "+store_long);
                            } else{
                                Log.i("LOCATION","getLocations().size() not greater than 0");
                            }
                        }
                        else{
                            Log.i("LOCATION","locationResult is null");
                        }
                        locationPB.setVisibility(View.GONE);
                    }
                }, Looper.getMainLooper());
    }

    private void setLocationRequest(){
        if(locationRequest==null){
            Log.i("LOCATION","locationRequest was null");
            locationRequest=new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
        } else{
            Log.i("LOCATION","locationRequest was already set");
        }
    }

    private void setLocationSettings(){
        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result=LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                            ==PackageManager.PERMISSION_GRANTED){
                        //getCurrentLocation();
                        if(!auxiliaryuseraccountmanager
                                .userWithThisPhoneExistsInDb(auxiliary.SERVER_URL+"/store_userManagement.php"
                                        ,store_ownerPhone)){
                            proceedToOtpVerification(auxiliaryuseraccountmanager.actionType.REGISTER);
                        } else{
                            Toast.makeText(mContext,"user already exists",Toast.LENGTH_LONG).show();
                        }
                    } else{
                        ActivityCompat.requestPermissions(loginOrRegister.this
                                ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                ,REQUEST_CODE_LOCATION_PERMISSION);
                    }
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        loginOrRegister.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                                Log.i("LOCATION","SendIntentException");
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                                Log.i("LOCATION","ClassCastException");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            Log.i("LOCATION","Settings change unavailable");
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                                ==PackageManager.PERMISSION_GRANTED){
                            //getCurrentLocation();
                            if(!auxiliaryuseraccountmanager
                                    .userWithThisPhoneExistsInDb(auxiliary.SERVER_URL+"/store_userManagement.php"
                                            ,store_ownerPhone)){
                                proceedToOtpVerification(auxiliaryuseraccountmanager.actionType.REGISTER);
                            } else{
                                Toast.makeText(mContext,"user already exists",Toast.LENGTH_LONG).show();
                            }
                        } else{
                            ActivityCompat.requestPermissions(loginOrRegister.this
                                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                    ,REQUEST_CODE_LOCATION_PERMISSION);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // Display an informative message here as to why is fetching location required.
                        Log.i("LOCATION","result cancelled");
                        msgExplanation(userPermissionResponse.LOCATION_NOT_ENABLED);
                        break;
                    default:
                        Log.i("LOCATION","default branch entered");
                        break;
                }
                break;
        }
    }

    private void msgExplanation(userPermissionResponse permission_response){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        switch (permission_response){
            case LOCATION_NOT_ENABLED:
                builder.setTitle("Location not enabled!")
                        .setMessage("Location should be enabled to locate your store on the map")
                        .setCancelable(false)
                        .setPositiveButton("Enable location", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setLocationSettings();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                break;
            case PERMISSION_DENIED:
                builder.setTitle("Permission denied!")
                        .setMessage("This permission is required to locate your store on the map. Give location permission ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(loginOrRegister.this
                                        ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                        ,REQUEST_CODE_LOCATION_PERMISSION);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                break;
            case PERMISSION_DENIED_WITH_NEVERASKAGAIN:
                builder.setTitle("Permission denied with never ask again!")
                        .setMessage("This permission is required to locate your store on the map. Go to Settings -> App permissions")
                        .setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                break;
        }
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}
