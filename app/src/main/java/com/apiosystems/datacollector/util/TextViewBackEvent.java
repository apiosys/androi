package com.apiosystems.datacollector.util;

import android.content.Context;
import android.location.LocationManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    public void onEditorAction(int actionCode) {
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            SensorActivity.mUserTexting = false;
            SensorActivity.mSensorLogger.writeDataToFile("EE:Texting" +
                    Helper.NEW_LINE);
            Log.i("onEditorAction", "DONE");
        }
        super.onEditorAction(actionCode);
    }

    public void setOnTextViewImeBackListener(TextViewImeBackListener listener) {
        mOnImeBack = listener;
    }

}
