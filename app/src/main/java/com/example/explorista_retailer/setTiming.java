package com.example.explorista_retailer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class setTiming extends AppCompatActivity {

    private ImageView leftArrowIV,rightArrowIV;
    private LinearLayout allDayTimingsLL;
    private LinearLayout mondayOpenTimeLL,mondayCloseTimeLL,tuesdayOpenTimeLL,tuesdayCloseTimeLL
            ,wednesdayOpenTimeLL,wednesdayCloseTimeLL,thursdayOpenTimeLL,thursdayCloseTimeLL
            ,fridayOpenTimeLL,fridayCloseTimeLL,saturdayOpenTimeLL,saturdayCloseTimeLL
            ,sundayOpenTimeLL,sundayCloseTimeLL;
    private TimePicker mondayOpenTimeTP,mondayCloseTimeTP,tuesdayOpenTimeTP,tuesdayCloseTimeTP
            ,wednesdayOpenTimeTP,wednesdayCloseTimeTP,thursdayOpenTimeTP,thursdayCloseTimeTP
            ,fridayOpenTimeTP,fridayCloseTimeTP,saturdayOpenTimeTP,saturdayCloseTimeTP
            ,sundayOpenTimeTP,sundayCloseTimeTP;
    private Button nextB,openCloseTodayB;
    private TextView openCloseTodayMsgTV;
    private LinearLayout[] timingLLArr;
    private TimePicker[] timingTPArr;

    private int currentTimingArrIndex;
    private int[] timePickerHour,timePickerMinute;
    private boolean[] openStatus;

    private LinearLayout reviewTimingsLL,editTimingsLL;
    private TextView mondayTimeReviewTV,tuesdayTimeReviewTV,wednesdayTimeReviewTV
            ,thursdayTimeReviewTV,fridayTimeReviewTV,saturdayTimeReviewTV,sundayTimeReviewTV;
    private Button proceedToMainB;

    private TextView[] reviewTimingTVArr;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timing);
        setTitle("Set your store timings");

        mContext=this;

        currentTimingArrIndex=0;
        timePickerHour=new int[auxiliary.DAYSINWEEKX2];
        timePickerMinute=new int[auxiliary.DAYSINWEEKX2];
        openStatus=new boolean[]{true,true,true,true,true,true,true};

        leftArrowIV=findViewById(R.id.leftArrowIV);
        rightArrowIV=findViewById(R.id.rightArrowIV);

        allDayTimingsLL=findViewById(R.id.allDayTimingsLL);

        mondayOpenTimeLL=findViewById(R.id.mondayOpenTimeLL);
        mondayCloseTimeLL=findViewById(R.id.mondayCloseTimeLL);
        tuesdayOpenTimeLL=findViewById(R.id.tuesdayOpenTimeLL);
        tuesdayCloseTimeLL=findViewById(R.id.tuesdayCloseTimeLL);
        wednesdayOpenTimeLL=findViewById(R.id.wednesdayOpenTimeLL);
        wednesdayCloseTimeLL=findViewById(R.id.wednesdayCloseTimeLL);
        thursdayOpenTimeLL=findViewById(R.id.thursdayOpenTimeLL);
        thursdayCloseTimeLL=findViewById(R.id.thursdayCloseTimeLL);
        fridayOpenTimeLL=findViewById(R.id.fridayOpenTimeLL);
        fridayCloseTimeLL=findViewById(R.id.fridayCloseTimeLL);
        saturdayOpenTimeLL=findViewById(R.id.saturdayOpenTimeLL);
        saturdayCloseTimeLL=findViewById(R.id.saturdayCloseTimeLL);
        sundayOpenTimeLL=findViewById(R.id.sundayOpenTimeLL);
        sundayCloseTimeLL=findViewById(R.id.sundayCloseTimeLL);

        mondayOpenTimeTP=findViewById(R.id.mondayOpenTimeTP);
        mondayCloseTimeTP=findViewById(R.id.mondayCloseTimeTP);
        tuesdayOpenTimeTP=findViewById(R.id.tuesdayOpenTimeTP);
        tuesdayCloseTimeTP=findViewById(R.id.tuesdayCloseTimeTP);
        wednesdayOpenTimeTP=findViewById(R.id.wednesdayOpenTimeTP);
        wednesdayCloseTimeTP=findViewById(R.id.wednesdayCloseTimeTP);
        thursdayOpenTimeTP=findViewById(R.id.thursdayOpenTimeTP);
        thursdayCloseTimeTP=findViewById(R.id.thursdayCloseTimeTP);
        fridayOpenTimeTP=findViewById(R.id.fridayOpenTimeTP);
        fridayCloseTimeTP=findViewById(R.id.fridayCloseTimeTP);
        saturdayOpenTimeTP=findViewById(R.id.saturdayOpenTimeTP);
        saturdayCloseTimeTP=findViewById(R.id.saturdayCloseTimeTP);
        sundayOpenTimeTP=findViewById(R.id.sundayOpenTimeTP);
        sundayCloseTimeTP=findViewById(R.id.sundayCloseTimeTP);

        nextB=findViewById(R.id.nextB);
        openCloseTodayB=findViewById(R.id.openCloseTodayB);
        openCloseTodayMsgTV=findViewById(R.id.openCloseTodayMsgTV);

        timingLLArr=new LinearLayout[]{mondayOpenTimeLL,mondayCloseTimeLL,tuesdayOpenTimeLL,tuesdayCloseTimeLL
                ,wednesdayOpenTimeLL,wednesdayCloseTimeLL,thursdayOpenTimeLL,thursdayCloseTimeLL
                ,fridayOpenTimeLL,fridayCloseTimeLL,saturdayOpenTimeLL,saturdayCloseTimeLL
                ,sundayOpenTimeLL,sundayCloseTimeLL};
        timingTPArr=new TimePicker[]{mondayOpenTimeTP,mondayCloseTimeTP,tuesdayOpenTimeTP,tuesdayCloseTimeTP
                ,wednesdayOpenTimeTP,wednesdayCloseTimeTP,thursdayOpenTimeTP,thursdayCloseTimeTP
                ,fridayOpenTimeTP,fridayCloseTimeTP,saturdayOpenTimeTP,saturdayCloseTimeTP
                ,sundayOpenTimeTP,sundayCloseTimeTP};

        reviewTimingsLL=findViewById(R.id.reviewTimingsLL);
        editTimingsLL=findViewById(R.id.editTimingsLL);
        mondayTimeReviewTV=findViewById(R.id.mondayTimeReviewTV);
        tuesdayTimeReviewTV=findViewById(R.id.tuesdayTimeReviewTV);
        wednesdayTimeReviewTV=findViewById(R.id.wednesdayTimeReviewTV);
        thursdayTimeReviewTV=findViewById(R.id.thursdayTimeReviewTV);
        fridayTimeReviewTV=findViewById(R.id.fridayTimeReviewTV);
        saturdayTimeReviewTV=findViewById(R.id.saturdayTimeReviewTV);
        sundayTimeReviewTV=findViewById(R.id.sundayTimeReviewTV);
        proceedToMainB=findViewById(R.id.proceedToMainB);

        reviewTimingTVArr=new TextView[]{mondayTimeReviewTV,tuesdayTimeReviewTV,wednesdayTimeReviewTV
                ,thursdayTimeReviewTV,fridayTimeReviewTV,saturdayTimeReviewTV,sundayTimeReviewTV};

        if(openStatus[currentTimingArrIndex]){
            timingLLArr[currentTimingArrIndex].setVisibility(View.VISIBLE);
            openCloseTodayB.setText("close today");
        } else{
            timingLLArr[currentTimingArrIndex].setVisibility(View.GONE);
            openCloseTodayMsgTV.setVisibility(View.VISIBLE);
            openCloseTodayMsgTV.setText(new StringBuilder().append("Store shut on ").append(auxiliary
                    .DAYSOFWEEK[currentTimingArrIndex/2])
                    .toString());
            openCloseTodayB.setText("open today");
        }

        leftArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentTimingArrIndex>=1 && currentTimingArrIndex<=13){
                    if(currentTimingArrIndex==1){
                        leftArrowIV.setImageResource(R.drawable.leftarrow_light);
                    } else if(currentTimingArrIndex==13){
                        rightArrowIV.setImageResource(R.drawable.rightarrow_dark);
                        nextB.setVisibility(View.GONE);
                    }
                    if(currentTimingArrIndex%2==0){
                        if(openStatus[currentTimingArrIndex/2]){
                            timePickerHour[currentTimingArrIndex]=timingTPArr[currentTimingArrIndex].getCurrentHour();
                            timePickerMinute[currentTimingArrIndex]=timingTPArr[currentTimingArrIndex].getCurrentMinute();
                        } else{
                            if(currentTimingArrIndex==12){
                                nextB.setVisibility(View.GONE);
                                rightArrowIV.setImageResource(R.drawable.rightarrow_dark);
                            }
                        }
                        timingLLArr[currentTimingArrIndex].setVisibility(View.GONE);
                        if(openStatus[(currentTimingArrIndex-2)/2]){
                            currentTimingArrIndex-=1;
                            timingLLArr[currentTimingArrIndex].setVisibility(View.VISIBLE);

                            timingTPArr[currentTimingArrIndex].setCurrentHour(timePickerHour[currentTimingArrIndex]);
                            timingTPArr[currentTimingArrIndex].setCurrentMinute(timePickerMinute[currentTimingArrIndex]);

                            openCloseTodayB.setVisibility(View.GONE);
                            openCloseTodayMsgTV.setVisibility(View.GONE);
                        } else{
                            currentTimingArrIndex-=2;
                            if(currentTimingArrIndex==0){
                                leftArrowIV.setImageResource(R.drawable.leftarrow_light);
                            }
                            timingLLArr[currentTimingArrIndex].setVisibility(View.GONE);
                            openCloseTodayB.setVisibility(View.VISIBLE);
                            openCloseTodayB.setText("open today");
                            openCloseTodayMsgTV.setVisibility(View.VISIBLE);
                            openCloseTodayMsgTV.setText(new StringBuilder().append("Store shut on ").append(auxiliary
                                    .DAYSOFWEEK[currentTimingArrIndex/2])
                                    .toString());
                        }
                    } else{
                        timingLLArr[currentTimingArrIndex].setVisibility(View.GONE);
                        currentTimingArrIndex-=1;
                        timingLLArr[currentTimingArrIndex].setVisibility(View.VISIBLE);

                        timingTPArr[currentTimingArrIndex].setCurrentHour(timePickerHour[currentTimingArrIndex]);
                        timingTPArr[currentTimingArrIndex].setCurrentMinute(timePickerMinute[currentTimingArrIndex]);

                        openCloseTodayB.setVisibility(View.VISIBLE);
                        openCloseTodayB.setText("close today");
                        openCloseTodayMsgTV.setVisibility(View.GONE);
                    }
                }
            }
        });
        rightArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentTimingArrIndex>=0 && currentTimingArrIndex<=12){
                    if(currentTimingArrIndex==0){
                        leftArrowIV.setImageResource(R.drawable.leftarrow_dark);
                    } else if(currentTimingArrIndex==12){
                        rightArrowIV.setImageResource(R.drawable.rightarrow_light);
                        nextB.setVisibility(View.VISIBLE);
                    }
                    if(currentTimingArrIndex%2==0){
                        if(openStatus[currentTimingArrIndex/2]){
                            timePickerHour[currentTimingArrIndex]=timingTPArr[currentTimingArrIndex].getCurrentHour();
                            timePickerMinute[currentTimingArrIndex]=timingTPArr[currentTimingArrIndex].getCurrentMinute();
                            openCloseTodayB.setVisibility(View.GONE);
                            openCloseTodayMsgTV.setVisibility(View.GONE);
                            timingLLArr[currentTimingArrIndex].setVisibility(View.GONE);
                            currentTimingArrIndex+=1;
                            timingLLArr[currentTimingArrIndex].setVisibility(View.VISIBLE);
                        } else{
                            if(currentTimingArrIndex!=12){
                                currentTimingArrIndex=Math.min(currentTimingArrIndex+2,auxiliary.DAYSINWEEKX2-1);
                                if(currentTimingArrIndex==12 && !openStatus[currentTimingArrIndex/2]){
                                    rightArrowIV.setImageResource(R.drawable.rightarrow_light);
                                    nextB.setVisibility(View.VISIBLE);
                                }
                                if(currentTimingArrIndex!=13){
                                    if(openStatus[currentTimingArrIndex/2]){
                                        openCloseTodayB.setVisibility(View.VISIBLE);
                                        openCloseTodayB.setText("close today");
                                        openCloseTodayMsgTV.setVisibility(View.GONE);
                                        timingLLArr[currentTimingArrIndex].setVisibility(View.VISIBLE);
                                    } else{
                                        openCloseTodayB.setVisibility(View.VISIBLE);
                                        openCloseTodayB.setText("open today");
                                        openCloseTodayMsgTV.setVisibility(View.VISIBLE);
                                        openCloseTodayMsgTV.setText(new StringBuilder().append("Store shut on ").append(auxiliary
                                                .DAYSOFWEEK[currentTimingArrIndex/2])
                                                .toString());
                                    }
                                }
                            }
                        }
                    } else{
                        timePickerHour[currentTimingArrIndex]=timingTPArr[currentTimingArrIndex].getCurrentHour();
                        timePickerMinute[currentTimingArrIndex]=timingTPArr[currentTimingArrIndex].getCurrentMinute();
                        timingLLArr[currentTimingArrIndex].setVisibility(View.GONE);
                        currentTimingArrIndex+=1;
                        if(openStatus[currentTimingArrIndex/2]){
                            openCloseTodayB.setVisibility(View.VISIBLE);
                            openCloseTodayB.setText("close today");
                            timingLLArr[currentTimingArrIndex].setVisibility(View.VISIBLE);
                        } else{
                            openCloseTodayB.setVisibility(View.VISIBLE);
                            openCloseTodayB.setText("open today");
                            openCloseTodayMsgTV.setVisibility(View.VISIBLE);
                            openCloseTodayMsgTV.setText(new StringBuilder().append("Store shut on ").append(auxiliary
                                    .DAYSOFWEEK[currentTimingArrIndex/2])
                                    .toString());
                            timingLLArr[currentTimingArrIndex].setVisibility(View.GONE);
                            if(currentTimingArrIndex==12){
                                rightArrowIV.setImageResource(R.drawable.rightarrow_light);
                                nextB.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });
        openCloseTodayB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(openStatus[currentTimingArrIndex/2]){
                    openStatus[currentTimingArrIndex/2]=false;
                    timingLLArr[currentTimingArrIndex].setVisibility(View.GONE);
                    openCloseTodayMsgTV.setVisibility(View.VISIBLE);
                    openCloseTodayMsgTV.setText(new StringBuilder().append("Store shut on ").append(auxiliary
                            .DAYSOFWEEK[currentTimingArrIndex/2])
                            .toString());
                    openCloseTodayB.setText("open today");
                    if(currentTimingArrIndex==12){
                        rightArrowIV.setImageResource(R.drawable.rightarrow_light);
                        nextB.setVisibility(View.VISIBLE);
                    }
                } else{
                    openStatus[currentTimingArrIndex/2]=true;
                    openCloseTodayMsgTV.setVisibility(View.GONE);
                    timingLLArr[currentTimingArrIndex].setVisibility(View.VISIBLE);
                    openCloseTodayB.setText("close today");
                    if(currentTimingArrIndex==12){
                        rightArrowIV.setImageResource(R.drawable.rightarrow_dark);
                        nextB.setVisibility(View.GONE);
                    }
                }
            }
        });
        nextB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(openStatus[currentTimingArrIndex/2]){
                        timePickerHour[currentTimingArrIndex]=timingTPArr[currentTimingArrIndex].getCurrentHour();
                        timePickerMinute[currentTimingArrIndex]=timingTPArr[currentTimingArrIndex].getCurrentMinute();
                    }
                    StringBuilder sbLog=new StringBuilder();
                    for(int i=0;i<auxiliary.DAYSINWEEKX2;i++){
                        if(openStatus[i/2]){
                            sbLog.append(auxiliary.DAYSOFWEEK[i/2]).append(" ");
                            sbLog.append("hour : ").append(timePickerHour[i]).append(" ");
                            sbLog.append("minute : ").append(timePickerMinute[i]).append(" ");
                        } else{
                            sbLog.append("closed ");
                        }
                        if(i%2==0){
                            sbLog.append(" ; ");
                        } else{
                            sbLog.append(" | ");
                        }
                    }
                    Log.i("setTiming","Timings -> "+sbLog.toString().trim());
                    allDayTimingsLL.setVisibility(View.GONE);
                    leftArrowIV.setVisibility(View.GONE);
                    rightArrowIV.setVisibility(View.GONE);
                    openCloseTodayB.setVisibility(View.GONE);
                    openCloseTodayMsgTV.setVisibility(View.GONE);
                    nextB.setVisibility(View.GONE);

                    reviewTimingsLL.setVisibility(View.VISIBLE);
                    for(int i=0;i<auxiliary.DAYSINWEEKX2/2;i++){
                        StringBuilder sb=new StringBuilder();
                        sb.append(auxiliary.DAYSOFWEEK[i]).append(" : ");
                        if(openStatus[i]){
                            sb.append(getTimeRangeText(i));
                        } else{
                            sb.append("closed");
                        }
                        reviewTimingTVArr[i].setText(sb.toString().trim());
                    }
                } catch (ArrayIndexOutOfBoundsException aioobe){
                    Log.i("setTiming","ArrayIndexOutOfBoundsException occurred");
                    aioobe.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        editTimingsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewTimingsLL.setVisibility(View.GONE);

                allDayTimingsLL.setVisibility(View.VISIBLE);
                leftArrowIV.setVisibility(View.VISIBLE);
                leftArrowIV.setImageResource(R.drawable.leftarrow_light);
                rightArrowIV.setVisibility(View.VISIBLE);
                rightArrowIV.setImageResource(R.drawable.rightarrow_dark);
                openCloseTodayB.setVisibility(View.VISIBLE);
                timingLLArr[currentTimingArrIndex].setVisibility(View.GONE);
                currentTimingArrIndex=0;
                if(openStatus[currentTimingArrIndex]){
                    timingLLArr[currentTimingArrIndex].setVisibility(View.VISIBLE);
                    openCloseTodayB.setText("close today");
                } else{
                    timingLLArr[currentTimingArrIndex].setVisibility(View.GONE);
                    openCloseTodayMsgTV.setVisibility(View.VISIBLE);
                    openCloseTodayMsgTV.setText(new StringBuilder().append("Store shut on ").append(auxiliary
                            .DAYSOFWEEK[currentTimingArrIndex/2])
                            .toString());
                    openCloseTodayB.setText("open today");
                }
            }
        });
        proceedToMainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formatted_timings=formatTime(timePickerHour,timePickerMinute,openStatus);
                auxiliaryuseraccountmanager.setStoreTimings(auxiliary.SERVER_URL+"/store_userManagement.php"
                        ,formatted_timings);
                auxiliaryuseraccountmanager.saveStoreTimingsToSP(mContext,formatted_timings);
                String store_ownerPhone=auxiliaryuseraccountmanager.getStorePhoneFromSP(mContext);
                if(store_ownerPhone!=null){
                    auxiliaryuseraccountmanager
                            .setStoreLoginStatus(auxiliary.SERVER_URL+"/store_userManagement.php"
                                    ,store_ownerPhone
                                    ,true);
                    proceedToMainActivity();
                } else{
                    Toast.makeText(mContext,"store owner phone in SP is null",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getTimeRangeText(int i){
        StringBuilder sb=new StringBuilder();
        int[] indexes_in_TP=new int[]{i*2,i*2+1};
        for(int j=0;j<indexes_in_TP.length;j++){
            if(timePickerHour[indexes_in_TP[j]]>12){
                sb.append(timePickerHour[indexes_in_TP[j]]-12)
                        .append(":");
                if(timePickerMinute[indexes_in_TP[j]]<10){
                    sb.append("0");
                }
                sb.append(timePickerMinute[indexes_in_TP[j]]).append(" PM");
            } else{
                if(timePickerHour[indexes_in_TP[j]]==0){
                    sb.append("12:");
                } else{
                    if(timePickerHour[indexes_in_TP[j]]<10){
                        sb.append("0");
                    }
                    sb.append(timePickerHour[indexes_in_TP[j]])
                            .append(":");
                }
                if(timePickerMinute[indexes_in_TP[j]]<10){
                    sb.append("0");
                }
                sb.append(timePickerMinute[indexes_in_TP[j]]).append(" AM");
            }
            if(j==0){
                sb.append(" - ");
            }
        }
        return sb.toString().trim();
    }

    private String formatTime(int[] timeInHour,int[] timeInMin,boolean[] open_status){
        Log.i("setTiming","inside formatTime");
        StringBuilder sb=new StringBuilder();
        if(open_status[0/2]){
            if(timeInHour[0]<10){
                sb.append("0");
            }
            sb.append(timeInHour[0]).append(":");
            if(timeInMin[0]<10){
                sb.append("0");
            }
            sb.append(timeInMin[0]).append(":00");
        } else{
            sb.append("null");
        }
        for(int i=1;i<timeInHour.length;i++){
            sb.append("_");
            if(open_status[i/2]){
                if(timeInHour[i]<10){
                    sb.append("0");
                }
                sb.append(timeInHour[i]).append(":");
                if(timeInMin[i]<10){
                    sb.append("0");
                }
                sb.append(timeInMin[i]).append(":00");
            } else{
                sb.append("null");
            }
        }
        Log.i("setTiming","return val -> "+sb.toString().trim());
        return sb.toString().trim();
    }

    private void proceedToMainActivity(){
        Intent setTimingToMainActivityIntent=new Intent(setTiming.this,MainActivity.class);
        startActivity(setTimingToMainActivityIntent);
        finish();
    }

}
