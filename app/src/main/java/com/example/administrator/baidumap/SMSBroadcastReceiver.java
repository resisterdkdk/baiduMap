package com.example.administrator.baidumap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.util.Date;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");    //接收数据
        for (Object p : pdus) {
            byte[] pdu = (byte[]) p;
            SmsMessage message = SmsMessage.createFromPdu(pdu);    //根据获得的byte[]封装成SmsMessage
            String body = message.getMessageBody();                //获得短信内容
            String date = new Date(message.getTimestampMillis()).toLocaleString();//获得发送时间
            String sender = message.getOriginatingAddress();    //短信发送方号码

                MainActivity ac =new MainActivity();
                SmsManager manager = SmsManager.getDefault();
                String mas ="Where are you?";
                if(body.compareTo(mas)==0)
                {
                    String location = String.valueOf(ac.locat_now.longitude)+"/"+String.valueOf(ac.locat_now.latitude);
                    manager.sendTextMessage(sender, null, location, null, null);
                }
                if(body.matches("\\d*.\\d*/\\d*.\\d*"))
                {
                    String[] strs = body.split("/");
                    float jing = Float.parseFloat(strs[0].substring(0,strs[0].length()));
                    float wei = Float.parseFloat(strs[1].substring(0,strs[1].length()));

                    ac.change(sender,jing,wei);
                }

            }
        }
}
