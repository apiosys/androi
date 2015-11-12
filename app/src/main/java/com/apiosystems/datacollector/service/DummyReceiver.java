package com.apiosystems.datacollector.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * dummyBroadcastReceiver used to increase the process priority
 * of this app
 */
public class DummyReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = DummyReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, ApioActivityRecognitionService.class);
        context.startService(background);
    }
}
