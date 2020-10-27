package com.example.explorista_retailer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

class auxiliary {

    final static String SERVER_URL="http://3.16.188.152/app";
    final static int LOCATIONREQUEST_INTERVAL=10000;

    final static int PASSWORD_MINLENGTH=8;
    static final String[] DAYSOFWEEK=new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    final static int DAYSINWEEKX2=14;

    // setTiming.java
    final static int MON_INT=0;
    final static int TUE_INT=1;
    final static int WED_INT=2;
    final static int THU_INT=3;
    final static int FRI_INT=4;
    final static int SAT_INT=5;
    final static int SUN_INT=6;

    // Request validation from client
    // PPK -> POST parameter keys
    // PPV -> POST parameter values
    static String PPK_INITIAL_CHECK="PPK_INITIAL_CHECK";
    static String PPK_INITIAL_CHECK_NOT_SET="PPK_INITIAL_CHECK not set";
    static String PPK_INITIAL_CHECK_FAIL="PPK_INITIAL_CHECK failed";
    static String PPV_INITIAL_CHECK="EXPLORISTA";

    // Firebase cloud messaging (FCM)
    final static String PPK_STOREFCMTOKEN="storeFcmToken";
    final static String FCM_RECEIVEDMESSAGE="fcm_receivedMessage";

    // Intent (loginOrRegister -> otpVerification)
    final static String ACTION_TYPE="action_type";
    final static String STORE_ID="store_id";
    final static String STORE_OWNERNAME="store_retailerName";
    final static String STORE_NAME="store_name";
    final static String STORE_RATING="store_rating";
    final static String STORE_ADDRLINE1="store_addrLine1";
    final static String STORE_ADDRLINE2="store_addrLine2";
    final static String STORE_LANDMARK="store_landmark";
    final static String STORE_LAT="store_lat";
    final static String STORE_LONG="store_long";
    final static String STORE_CITY="store_city";
    final static String STORE_AREA="store_area";
    final static String STORE_SECTOR="store_sector";
    final static String STORE_GSTIN="store_gstin";
    final static String STORE_OWNERPAN="store_ownerPan";
    final static String STORE_OWNERPHONE="store_phone";
    final static String STORE_OWNERPASSWORD="store_ownerPassword";
    final static String STORE_TIMING="store_timing";
    final static String STORE_OPENTIMEMON="store_openTimeMon";
    final static String STORE_CLOSETIMEMON="store_closeTimeMon";
    final static String STORE_OPENTIMETUE="store_openTimeTue";
    final static String STORE_CLOSETIMETUE="store_closeTimeTue";
    final static String STORE_OPENTIMEWED="store_openTimeWed";
    final static String STORE_CLOSETIMEWED="store_closeTimeWed";
    final static String STORE_OPENTIMETHU="store_openTimeThu";
    final static String STORE_CLOSETIMETHU="store_closeTimeThu";
    final static String STORE_OPENTIMEFRI="store_openTimeFri";
    final static String STORE_CLOSETIMEFRI="store_closeTimeFri";
    final static String STORE_OPENTIMESAT="store_openTimeSat";
    final static String STORE_CLOSETIMESAT="store_closeTimeSat";
    final static String STORE_OPENTIMESUN="store_openTimeSun";
    final static String STORE_CLOSETIMESUN="store_closeTimeSun";
    final static String STORE_ONLINESTR="Online";
    final static String STORE_OFFLINESTR="Offline";

    // PPK (auxiliaryLoginOrRegister)
    final static String PPK_CITYNAME="cityName";

    // PPK (auxiliaryuseraccountmanager)
    final static String PPK_REQUESTTYPE="requestType";
    final static String PPV_REQUESTTYPE_USEREXISTENCECHECKBYPHONE="0";
    final static String PPV_REQUESTTYPE_USERADDITION="1";
    final static String PPV_REQUESTTYPE_CREDENTIALVERIFY="2";
    final static String PPV_REQUESTTYPE_ACTIVATIONSTATUSCHECK="3";
    final static String PPV_REQUESTTYPE_STORETIMESET="4";
    final static String PPV_REQUESTTYPE_SETLOGINSTATUSPOSITIVE="5";
    final static String PPV_REQUESTTYPE_SETLOGINSTATUSNEGATIVE="6";
    final static String PPV_REQUESTTYPE_FETCHSTOREFIELDS="7";
    final static String PPV_REQUESTTYPE_FETCHSTOREONLINESTATUS="8";
    final static String PPV_REQUESTTYPE_SETSTOREONLINESTATUS="9";
    final static String PPK_STOREID="storeId";
    final static String PPK_STOREOWNERNAME="storeOwnerName";
    final static String PPK_STORENAME="storeName";
    final static String PPK_STOREADDRLINE1="storeAddrLine1";
    final static String PPK_STOREADDRLINE2="storeAddrLine2";
    final static String PPK_STORELANDMARK="storeLandmark";
    final static String PPK_STORELAT="storeLat";
    final static String PPK_STORELONG="storeLong";
    final static String PPK_STORECITY="storeCity";
    final static String PPK_STOREAREA="storeArea";
    final static String PPK_STORESECTOR="storeSector";
    final static String PPK_STOREGSTIN="storeGstin";
    final static String PPK_STOREOWNERPAN="storeOwnerPan";
    final static String PPK_STOREOWNERPHONE="storeOwnerPhone";
    final static String PPK_STOREOWNERPASSWORD="storeOwnerPassword";
    final static String PPK_STOREOPENTIMEMON="storeOpenTimeMon";
    final static String PPK_STORECLOSETIMEMON="storeCloseTimeMon";
    final static String PPK_STOREOPENTIMETUE="storeOpenTimeTue";
    final static String PPK_STORECLOSETIMETUE="storeCloseTimeTue";
    final static String PPK_STOREOPENTIMEWED="storeOpenTimeWed";
    final static String PPK_STORECLOSETIMEWED="storeCloseTimeWed";
    final static String PPK_STOREOPENTIMETHU="storeOpenTimeThu";
    final static String PPK_STORECLOSETIMETHU="storeCloseTimeThu";
    final static String PPK_STOREOPENTIMEFRI="storeOpenTimeFri";
    final static String PPK_STORECLOSETIMEFRI="storeCloseTimeFri";
    final static String PPK_STOREOPENTIMESAT="storeOpenTimeSat";
    final static String PPK_STORECLOSETIMESAT="storeCloseTimeSat";
    final static String PPK_STOREOPENTIMESUN="storeOpenTimeSun";
    final static String PPK_STORECLOSETIMESUN="storeCloseTimeSun";
    final static String PPK_STOREONLINESTATUS="storeOnlineStatus";

    final static String PPK_STORETIMINGS="storeTimings";
    //final static String PPK_STOREFCMTOKEN="storeFcmToken";
    //final static String PPK_FCMTOKEN="fcmToken";

    // loginOrRegister.java
    final static String SPINNER_UNSELECTED_CITY="Select City";
    final static String SPINNER_UNSELECTED_SECTOR="Select Sector";
    final static String SPINNER_UNSELECTED_AREA="Select Area";

    static String postParamsToString(HashMap<String,String> params){
        /*
        Takes POST parameters in a HashMap<String, String>
        and returns POST string to be passed to OutputStream of HttpURLConnection
        */
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0){
                    sb.append("&");
                }
                sb.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                //e.printStackTrace();
            }
            i++;
        }
        return sb.toString().trim();
    }

}
