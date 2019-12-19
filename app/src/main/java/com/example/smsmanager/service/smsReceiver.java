package com.example.smsmanager.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.smsmanager.Activities.ScreenActivity;
import com.example.smsmanager.model.Message;


public class smsReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";
    //private Object Toast;
//    private static final String CHANNEL_ID_1 = "CHANNEL_ID_1";
//    private static final String CHANNEL_ID_2 = "CHANNEL_ID_2";

    Message message;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            String smsBody = "";
            String address = "";

            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                smsBody = smsMessage.getMessageBody().toString();
                address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
            //this will update the UI with message
            ScreenActivity inst = ScreenActivity.instance();
            inst.refreshSmsInbox();
            //  inst.finish();
            // inst.updateList(smsMessageStr);
        }
    }

//    public void createNotificationChannel(View view) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder();
//    }
}