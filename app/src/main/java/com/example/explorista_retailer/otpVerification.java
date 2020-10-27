package com.example.explorista_retailer;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class otpVerification extends AppCompatActivity {

    private EditText otpInputET;
    private Button proceedToVerifyOtpB,loginOrRegisterWithOtherPhoneB;
    private TextView resendOtpTV;

    private Bundle extras;
    private Context mContext;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;

    private auxiliaryuseraccountmanager.actionType actionType;
    private String storeOwnerName,storeName,storeAddrLine1,storeAddrLine2,storeLandmark,storeCity,storeArea,storeLat,storeLong
            ,storeSector,storeGstin,storeOwnerPan,storeOwnerPhone,storeOwnerPassword;
    private LinearLayout otpManageLL;

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        if(savedInstanceState!=null){
            onRestoreInstanceState(savedInstanceState);
        }
        mContext=this;

        otpInputET=findViewById(R.id.otpInputET);
        proceedToVerifyOtpB=findViewById(R.id.proceedToVerifyOtpB);
        loginOrRegisterWithOtherPhoneB=findViewById(R.id.loginOrRegisterWithOtherPhoneB);
        resendOtpTV=findViewById(R.id.resendOtpTV);
        otpManageLL=findViewById(R.id.otpManageLL);
        loginOrRegisterWithOtherPhoneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otpVerificationToMainActivityIntent=new Intent(otpVerification.this,MainActivity.class);
                startActivity(otpVerificationToMainActivityIntent);
            }
        });

        extras=getIntent().getExtras();
        if(extras!=null){
            Log.i("bundle","extras is not null");
            actionType=(auxiliaryuseraccountmanager.actionType) extras.getSerializable(auxiliary.ACTION_TYPE);
            StringBuilder sb=new StringBuilder();
            storeOwnerName=extras.getString(auxiliary.STORE_OWNERNAME);
            storeName=extras.getString(auxiliary.STORE_NAME);
            storeAddrLine1=extras.getString(auxiliary.STORE_ADDRLINE1);
            storeAddrLine2=extras.getString(auxiliary.STORE_ADDRLINE2);
            storeLandmark=extras.getString(auxiliary.STORE_LANDMARK);
            storeCity=extras.getString(auxiliary.STORE_CITY);
            storeArea=extras.getString(auxiliary.STORE_AREA);
            storeLat=extras.getString(auxiliary.STORE_LAT);
            storeLong=extras.getString(auxiliary.STORE_LONG);
            storeSector=extras.getString(auxiliary.STORE_SECTOR);
            storeGstin=extras.getString(auxiliary.STORE_GSTIN);
            storeOwnerPan=extras.getString(auxiliary.STORE_OWNERPAN);
            storeOwnerPhone=extras.getString(auxiliary.STORE_OWNERPHONE);
            storeOwnerPassword=extras.getString(auxiliary.STORE_OWNERPASSWORD);
            sb.append(storeOwnerName+","+storeName+","+storeAddrLine1+","+storeAddrLine2+","+storeLandmark+","+
                    storeCity+","+storeArea+","+storeLat+","+storeLong+","+storeSector+","+storeGstin+","+
                    storeOwnerPan+","+storeOwnerPhone+","+storeOwnerPassword);
            Log.i("bundle","passed params -> "+sb.toString().trim());
        } else{
            Log.i("bundle","extras is null");
        }
        mAuth=FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.i("AUTH","inside onVerificationCompleted");
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                mVerificationInProgress = false;

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                //updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.i("AUTH","inside onVerificationFailed");
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                mVerificationInProgress = false;
                e.printStackTrace();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(mContext,"Invalid phone number",Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(mContext,"Quota exceeded",Toast.LENGTH_LONG).show();
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.i("AUTH","inside onCodeSent");
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                otpManageLL.setVisibility(View.VISIBLE);
            }
        };

        resendOtpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpInputET.setText("");
                resendVerificationCode(storeOwnerPhone,mResendToken);
            }
        });
        proceedToVerifyOtpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputted_otp=otpInputET.getText().toString().trim();
                if(inputted_otp.isEmpty()){
                    otpInputET.setError("Cannot be empty");
                } else{
                    verifyPhoneNumberWithCode(mVerificationId,inputted_otp);
                }
            }
        });
        startPhoneNumberVerification(storeOwnerPhone);
        //loginOrRegister();
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        Log.i("AUTH","inside startPhoneNumberVerification");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,              // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,       // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        Log.i("AUTH","inside verifyPhoneNumberWithCode");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,              // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,       // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        Log.i("AUTH","inside signInWithPhoneAuthCredential");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.i("AUTH","signin success");
                            FirebaseUser user=task.getResult().getUser();
                            if(user!=null){
                                loginOrRegister();
                            } else{
                                Log.i("AUTH","user is null");
                            }
                        } else{
                            Log.i("AUTH","task was not successful");
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                // [END_EXCLUDE]
                            }
                        }
                    }
                });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS,mVerificationInProgress);
    }

    private void loginOrRegister(){
        try{
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                //Log.i("FCM", "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            if(storeOwnerPhone!=null){
                                if(!storeOwnerPhone.equals("")){
                                    if(storeOwnerPhone.startsWith("+91")){
                                        storeOwnerPhone=storeOwnerPhone.substring(3);
                                    }
                                }
                            }
                            switch(actionType){
                                case LOGIN:
                                    if(auxiliaryuseraccountmanager.accountIsActive(
                                            auxiliary.SERVER_URL+"/store_userManagement.php"
                                            ,storeOwnerPhone)){
                                        // 1) fetch store's detail by store's phone
                                        // 2) save fetched details in SharedPrefs
                                        HashMap<String,String> fetched_fieldsHM=auxiliaryuseraccountmanager
                                                .fetchStoreFields(auxiliary.SERVER_URL+"/store_userManagement.php"
                                                        ,storeOwnerPhone);
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
                                        Toast.makeText(mContext,R.string.inactive_account,Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                case REGISTER:
                                    String fcmToken=task.getResult().getToken();
                                    Log.i("FCM","fcmToken : "+fcmToken);
                                    //Log.i("FCM","here fcmToken is : "+fcmToken);
                                    String sql_response=auxiliaryuseraccountmanager.addUserToDb(
                                            auxiliary.SERVER_URL+"/store_userManagement.php"
                                            ,storeOwnerName
                                            ,storeName
                                            ,storeAddrLine1
                                            ,storeAddrLine2
                                            ,storeLandmark
                                            ,storeCity
                                            ,storeArea
                                            ,storeLat
                                            ,storeLong
                                            ,storeSector
                                            ,storeGstin
                                            ,storeOwnerPan
                                            ,storeOwnerPhone
                                            ,storeOwnerPassword
                                            ,fcmToken);
                                    if(sql_response!=null){
                                        Log.i("DB","sql response -> "+sql_response);
                                    } else{
                                        Log.i("DB","sql response is null");
                                    }
                                    // 1) fetch store's detail by store's phone
                                    // 2) save fetched details in SharedPrefs
                                    HashMap<String,String> fetched_fieldsHM=auxiliaryuseraccountmanager
                                            .fetchStoreFields(auxiliary.SERVER_URL+"/store_userManagement.php"
                                                    ,storeOwnerPhone);
                                    StringBuilder sb=new StringBuilder();
                                    sb.append("store id - ").append(fetched_fieldsHM.get(auxiliary.STORE_ID));
                                    sb.append(", ").append("store name - ").append(fetched_fieldsHM.get(auxiliary.STORE_NAME));
                                    sb.append(", ").append("store owner phone - ").append(fetched_fieldsHM.get(auxiliary.STORE_OWNERPHONE));
                                    sb.append(", ").append("store name - ").append(fetched_fieldsHM.get(auxiliary.STORE_NAME));
                                    sb.append(", ").append("store rating - ").append(fetched_fieldsHM.get(auxiliary.STORE_RATING));
                                    Log.i("otpVerification","fetched fields -> "+sb.toString().trim());
                                    auxiliaryuseraccountmanager.saveStoreFieldsToSP(mContext
                                            ,fetched_fieldsHM.get(auxiliary.STORE_ID)
                                            ,fetched_fieldsHM.get(auxiliary.STORE_NAME)
                                            ,fetched_fieldsHM.get(auxiliary.STORE_OWNERPHONE)
                                            ,fetched_fieldsHM.get(auxiliary.STORE_OWNERNAME)
                                            ,null /* registration is done but timing is not set yet. */
                                            ,fetched_fieldsHM.get(auxiliary.STORE_RATING));
                                    proceedToSetTimingActivity();
                                    break;
                            }
                        }
                    });
        } catch (NullPointerException npe){
            npe.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void proceedToMainActivity(){
        Intent otpVerificationToMainActivityIntent=new Intent(otpVerification.this,MainActivity.class);
        startActivity(otpVerificationToMainActivityIntent);
        finish();
    }

    private void proceedToSetTimingActivity(){
        Intent otpVerificationToSetTimingIntent=new Intent(otpVerification.this,setTiming.class);
        startActivity(otpVerificationToSetTimingIntent);
        finish();
    }

}
