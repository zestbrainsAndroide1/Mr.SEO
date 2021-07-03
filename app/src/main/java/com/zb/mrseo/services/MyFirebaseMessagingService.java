package com.zb.mrseo.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zb.mrseo.MainActivity;
import com.zb.mrseo.R;
import com.zb.mrseo.activity.ChatActivity;
import com.zb.mrseo.activity.ChatHistoryActivity;
import com.zb.mrseo.activity.HelpActivity;
import com.zb.mrseo.activity.SplashActivity;
import com.zb.mrseo.restapi.WebConstant;
import com.zb.mrseo.utility.AppConstants;
import com.zb.mrseo.utility.Prefs2;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String msg = "";
        String title = "";


//kryo but push nthi avtu
        Log.e("PUSHH : ", remoteMessage.getData().toString());

        title = remoteMessage.getData().get("push_title");
        /* title = "Get Up People";*/
        msg = remoteMessage.getData().get("push_message");

        if (remoteMessage.getData().size() > 0) {

            try {
                HashMap<String, String> dataMap = getDataHashMap(new JSONObject(remoteMessage.getData()));
                sendNotification(title, msg, dataMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification(String title, String notificationBody, HashMap<String, String> dataMap) {

        NotificationCompat.Builder notificationBuilder;

        int interval;
        long totalTime;
        Intent intent = null;
        String msg = "";

//        if(dataMap.get("push_type").equals("1") || dataMap.get("push_type").equals("2")){
//            msg=notificationBody;
//        }else {
        msg=dataMap.get("message");
        // }


        /*intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
*/

        if (AppConstants.ISCHAT_ACTVITY_OPEN && dataMap.get("push_type").equals("1") && AppConstants.OPEND_THREAD.equals(dataMap.get("object_id"))) {
            Intent intent1 = new Intent("chat");
            intent1.putExtra(WebConstant.ARGUMENT_ITEM_DATA, dataMap);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            return;
        } else if (dataMap.get("push_type").equals("1")) {

            intent = new Intent(this, ChatHistoryActivity.class);
            intent.putExtra("show", dataMap.get("option"));
            intent.putExtra("id", dataMap.get("object_id"));
            intent.putExtra("type", "notification");
            intent.putExtra("receiverId", dataMap.get("user_id"));


        } else if (AppConstants.ISCHAT_ACTVITY_OPEN && dataMap.get("push_type").equals("2") && AppConstants.OPEND_THREAD.equals(dataMap.get("object_id"))) {
            Intent intent1 = new Intent("chat");
            intent1.putExtra(WebConstant.ARGUMENT_ITEM_DATA, dataMap);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            return;
        } else if (dataMap.get("push_type").equals("2")) {

            intent = new Intent(this, ChatActivity.class);
            intent.putExtra("id", dataMap.get("object_id"));
            intent.putExtra("title", dataMap.get("push_message"));
            intent.putExtra("receiverId", dataMap.get("user_id"));
            intent.putExtra("type", dataMap.get("messageType"));



        }else{

            intent = new Intent(this, MainActivity.class);
            intent.putExtra("show", "content1");





        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        int notifId = (int) System.currentTimeMillis();


        long[] vibrateInterval;

        String CHANNEL_ID = "my_channel_01";

        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setColor(this.getResources().getColor(R.color.colorAccent));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        notificationBuilder.setContentTitle(title.replace("sent you new message", ""));
        notificationBuilder.setContentText(notificationBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);

        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        interval = 4;
        totalTime = 1000;

        vibrateInterval = new long[interval + 1];
        vibrateInterval[0] = totalTime / interval;
        for (int i = 1; i < interval + 1; i++) {
            vibrateInterval[i] = totalTime / interval;
        }
        notificationBuilder.setSound(defaultSoundUri);

        if (null != notificationManager) {
            notificationManager.notify(0, notificationBuilder.build());
        }


    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Prefs2.setValue(this, AppConstants.PUSH_TOKEN, s);
        Log.e("Push Token", s);

    }

    public HashMap<String, String> getDataHashMap(JSONObject jsonData) throws JSONException {
        HashMap<String, String> getData = new HashMap<>();
        Iterator<String> keysItr = jsonData.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            getData.put(key, jsonData.getString(key));
        }
        return getData;
    }

}

