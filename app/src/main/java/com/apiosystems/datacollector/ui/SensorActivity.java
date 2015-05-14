package com.apiosystems.datacollector.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.apiosystems.datacollector.Logger.SensorLogger;
import com.apiosystems.datacollector.util.Helper;
import com.apiosystems.datacollector.util.TextViewBackEvent;

import java.util.Timer;
import datacollector.apiosystems.com.datacollector.R;

public class SensorActivity extends Activity {

    private static TextViewBackEvent mTextView;
    private static Button mPhoneCallBtn;
    private static Button mGeneralHandlingBtn;
    private static Button mStartButton;
    private static Button mEndButton;
    private static Switch mSwitchButton;
    public static Timer timer ;
    public static final String LOG_TAG = "SENSOR_ACTIVITY";
    private static int BLUE ;
    public static int LTBLUE;
    public static int GREY;
    public static int WHITE;

    public static SensorLogger mSensorLogger;
    public static boolean isDriver = false;
    public static boolean mStartLog = false;
    public static boolean mPhoneCallStarted = false;
    public static boolean mGeneralHandlingStarted = false;
    public static boolean mUserTexting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.data_capture);
        mTextView = (TextViewBackEvent) findViewById(R.id.textView);//Custom layout component
        mPhoneCallBtn = (Button) findViewById(R.id.phonecall_btn);
        mGeneralHandlingBtn = (Button) findViewById(R.id.generalhandling_btn);
        mStartButton = (Button) findViewById(R.id.StartButton);
        mEndButton   = (Button) findViewById(R.id.EndButton);
        mSwitchButton = (Switch) findViewById(R.id.switch_button);
        LTBLUE = getResources().getColor(R.color.LTBLUE);
        BLUE = getResources().getColor(R.color.BLUE);
        GREY = Color.DKGRAY;
        WHITE = Color.WHITE;

        disableView();

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mUserTexting) {
                    mUserTexting = true;
                    mTextView.setText("");
                    mSensorLogger.writeDataToFile("SE:Texting" +
                            Helper.NEW_LINE);
                    Log.i(LOG_TAG, "TEXTVIEW_CLICKED" + mUserTexting);
                }
            }
        });

        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    isDriver = true;
                    mSwitchButton.setText("DRIVER");
                    mSensorLogger.writeDataToFile(Helper.DRIVER
                            + Helper.NEW_LINE);
                    Log.i(LOG_TAG,"DRIVER");
                }else{
                    isDriver = false;
                    mSwitchButton.setText("PASSENGER");
                    mSensorLogger.writeDataToFile(Helper.PASSENGER
                            + Helper.NEW_LINE);
                    Log.i(LOG_TAG, "PASSENGER");
                }
            }
        });

        mPhoneCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPhoneCallStarted) {
                    mPhoneCallStarted = true;
                    mPhoneCallBtn.setBackgroundColor(BLUE);
                    mPhoneCallBtn.setText("STOP PHONE CALL");
                    mSensorLogger.writeDataToFile("SE:PhoneCall"
                            + Helper.NEW_LINE);
                } else {
                    mPhoneCallStarted = false;
                    mPhoneCallBtn.setBackgroundColor(LTBLUE);
                    mPhoneCallBtn.setText("START PHONE CALL");
                    mSensorLogger.writeDataToFile("EE:PhoneCall"
                            + Helper.NEW_LINE);
                }
            }
        });

        mGeneralHandlingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGeneralHandlingStarted) {
                    mGeneralHandlingStarted = true;
                    mGeneralHandlingBtn.setBackgroundColor(BLUE);
                    mGeneralHandlingBtn.setText("STOP GENERAL HANDLING");
                    mSensorLogger.writeDataToFile("SE:GeneralHandling"
                            + Helper.NEW_LINE);
                } else {
                    mGeneralHandlingStarted = false;
                    mGeneralHandlingBtn.setBackgroundColor(LTBLUE);
                    mGeneralHandlingBtn.setText("START GENERAL HANDLING");
                    mSensorLogger.writeDataToFile("EE:GeneralHandling"
                            + Helper.NEW_LINE);
                }
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartLog = true;
                enableView();
                mSensorLogger = new SensorLogger(getApplicationContext(), SensorActivity.this);
                mSensorLogger.startLogging(Helper.getPhoneName());
                timer = new Timer();
                timer.schedule(mSensorLogger, Helper.TIMER_DELAY, Helper.TIMER_PERIOD);
            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableView();
                mSensorLogger.stopLogging();
                timer.cancel();
                timer.purge();
            }
        });
    }

    private void enableView() {
        mTextView.setBackgroundColor(WHITE);
        mPhoneCallBtn.setBackgroundColor(LTBLUE);
        mGeneralHandlingBtn.setBackgroundColor(LTBLUE);
        mStartButton.setBackgroundColor(LTBLUE);
        mEndButton.setBackgroundColor(LTBLUE);

        mTextView.setEnabled(true);
        mSwitchButton.setEnabled(true);
        mPhoneCallBtn.setEnabled(true);
        mGeneralHandlingBtn.setEnabled(true);
        mStartButton.setEnabled(false);
        mEndButton.setEnabled(true);
    }

    private void disableView(){
        mTextView.setBackgroundColor(Color.GRAY);
        mPhoneCallBtn.setBackgroundColor(GREY);
        mGeneralHandlingBtn.setBackgroundColor(GREY);
        mStartButton.setBackgroundColor(GREY);
        mEndButton.setBackgroundColor(GREY);

        mTextView.setEnabled(false);
        mSwitchButton.setChecked(false);
        mSwitchButton.setEnabled(false);
        mPhoneCallBtn.setEnabled(false);
        mGeneralHandlingBtn.setEnabled(false);
        mStartButton.setEnabled(true);
        mEndButton.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mStartLog){
            mStartLog = false;
            mSensorLogger.stopLogging();
            timer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}