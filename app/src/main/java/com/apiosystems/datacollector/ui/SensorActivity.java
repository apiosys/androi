package com.apiosystems.datacollector.ui;

import android.app.Activity;
import android.app.Service;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.apiosystems.datacollector.Logger.SensorLogger;
import com.apiosystems.datacollector.util.Helper;
import com.apiosystems.datacollector.util.TextViewBackEvent;

import java.util.Timer;
import datacollector.apiosystems.com.datacollector.R;

public class SensorActivity extends Activity {

    private static TextViewBackEvent mTextView;
    private static Button mAtStopBtn;
    private static Button mStartedMoveBtn;
    private static Button mPhoneCallBtn;
    private static Button mGeneralHandlingBtn;
    private static Button mStartButton;
    private static Button mEndButton;
    public static Timer timer ;
    public static final String LOG_TAG = "SENSOR_ACTIVITY";
    public static int BLUE;
    public static int GREY;
    public static int WHITE;

    public static SensorLogger mSensorLogger;
    public static boolean mStartLog = false;
    private static boolean mPhoneCallStarted = false;
    private static boolean mGeneralHandlingStarted = false;
    public static boolean mUserTexting = false;
    private static InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.data_capture);
        mTextView = (TextViewBackEvent) findViewById(R.id.textView);//Custom layout component
        mAtStopBtn = (Button) findViewById(R.id.atstop);
        mStartedMoveBtn = (Button) findViewById(R.id.startedmove);
        mPhoneCallBtn = (Button) findViewById(R.id.phonecall_btn);
        mGeneralHandlingBtn = (Button) findViewById(R.id.generalhandling_btn);
        mStartButton = (Button) findViewById(R.id.StartButton);
        mEndButton   = (Button) findViewById(R.id.EndButton);

         imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);

        BLUE = getResources().getColor(R.color.BLUE);
        GREY = Color.DKGRAY;
        WHITE = Color.WHITE;

        disableView();

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mUserTexting) {
                    mUserTexting = true;
                    mSensorLogger.writeDataToFile("EVENT:Texting:STARTED" +
                            Helper.NEW_LINE);
                    Log.i(LOG_TAG, "TEXTVIEW_CLICKED" + mUserTexting);
                }
            }
        });

        mAtStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorLogger.writeDataToFile("EVENT:Moving:STOPPED"
                            + Helper.NEW_LINE);
                Log.i(LOG_TAG, "AT_STOP");
            }
        });

        mStartedMoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorLogger.writeDataToFile("EVENT:Moving:STARTED"
                        + Helper.NEW_LINE);
                Log.i(LOG_TAG, "STARTED_MOVE");
            }
        });

        mPhoneCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPhoneCallStarted) {
                    mPhoneCallStarted = true;
                    mPhoneCallBtn.setText("STOP PHONE CALL");
                    mSensorLogger.writeDataToFile("EVENT:PhoneCall:STARTED"
                            + Helper.NEW_LINE);
                } else {
                    mPhoneCallStarted = false;
                    mPhoneCallBtn.setText("START PHONE CALL");
                    mSensorLogger.writeDataToFile("EVENT:PhoneCall:STOPPED"
                            + Helper.NEW_LINE);
                }
            }
        });

        mGeneralHandlingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGeneralHandlingStarted) {
                    mGeneralHandlingStarted = true;
                    mGeneralHandlingBtn.setText("STOP GENERAL HANDLING");
                    mSensorLogger.writeDataToFile("EVENT:GeneralHandling:STARTED"
                            + Helper.NEW_LINE);
                } else {
                    mGeneralHandlingStarted = false;
                    mGeneralHandlingBtn.setText("START GENERAL HANDLING");
                    mSensorLogger.writeDataToFile("EVENT:GeneralHandling:STOPPED"
                            + Helper.NEW_LINE);
                }
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartLog = true;
                enableView();
                Toast.makeText(getApplicationContext(), Helper.CAPTURE + " STARTED :-) ", Toast.LENGTH_SHORT).show();
                mSensorLogger = new SensorLogger(getApplicationContext(), SensorActivity.this);
                mSensorLogger.startLogging("");
                timer = new Timer();
                timer.schedule(mSensorLogger, Helper.TIMER_DELAY, Helper.TIMER_PERIOD);
            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableView();
                Toast.makeText(getApplicationContext(), Helper.CAPTURE + " STOPPED :-( ", Toast.LENGTH_SHORT).show();
                mSensorLogger.stopLogging();
                timer.cancel();
                timer.purge();
            }
        });
    }

    private void enableView() {
        mTextView.setBackgroundColor(WHITE);
        mAtStopBtn.setBackgroundColor(BLUE);
        mStartedMoveBtn.setBackgroundColor(BLUE);
        mPhoneCallBtn.setBackgroundColor(BLUE);
        mGeneralHandlingBtn.setBackgroundColor(BLUE);
        mStartButton.setBackgroundColor(BLUE);
        mEndButton.setBackgroundColor(BLUE);

        mTextView.setEnabled(true);
        mAtStopBtn.setEnabled(true);
        mStartedMoveBtn.setEnabled(true);
        mPhoneCallBtn.setEnabled(true);
        mGeneralHandlingBtn.setEnabled(true);
        mStartButton.setEnabled(false);
        mEndButton.setEnabled(true);
    }

    private void disableView(){
        mTextView.setBackgroundColor(Color.GRAY);
        mAtStopBtn.setBackgroundColor(GREY);
        mStartedMoveBtn.setBackgroundColor(GREY);
        mPhoneCallBtn.setBackgroundColor(GREY);
        mGeneralHandlingBtn.setBackgroundColor(GREY);
        mStartButton.setBackgroundColor(GREY);
        mEndButton.setBackgroundColor(GREY);

        mTextView.setEnabled(false);
        mAtStopBtn.setEnabled(false);
        mStartedMoveBtn.setEnabled(false);
        mPhoneCallBtn.setEnabled(false);
        mGeneralHandlingBtn.setEnabled(false);
        mStartButton.setEnabled(true);
        mEndButton.setEnabled(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
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