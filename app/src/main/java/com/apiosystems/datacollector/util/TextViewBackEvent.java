package com.apiosystems.datacollector.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.apiosystems.datacollector.ui.SensorActivity;

/**
 * Created by Akshayraj on 5/7/15.
 */
public class TextViewBackEvent extends TextView {

    private TextViewImeBackListener mOnImeBack;

    public TextViewBackEvent(Context context) {
        super(context);
    }

    public TextViewBackEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewBackEvent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                SensorActivity.mUserTexting = false;
                SensorActivity.mSensorLogger.writeDataToFile("EVENT:Texting:STOPPED" +
                        Helper.NEW_LINE);
                Log.i("onKeyPreIme", "TextingStopped" + keyCode + SensorActivity.mUserTexting);
            }

        return super.onKeyUp(keyCode, event);
    }

    public void setOnTextViewImeBackListener(TextViewImeBackListener listener) {
        mOnImeBack = listener;
    }

}
