/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.iwantbuyer.sever;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.appsflyer.AppsFlyerLib;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e("TAG_firebase", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("TAG_firebase", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("TAG_firebase", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        Log.e("TAG_firebase", remoteMessage.getNotification().getTitle() + "");
        Log.e("TAG_firebase", remoteMessage.getNotification().getTag() + "");
        Log.e("TAG_firebaseyangshuyu", remoteMessage.getData().get("event") + "");
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        if (remoteMessage.getData().get("event") != null && remoteMessage.getData().get("event").equals("user_won")) {
            winning(Integer.parseInt(remoteMessage.getData().get("order_id")));
        } else if (remoteMessage.getData().get("event") != null) {
            sendNotification(remoteMessage);
        }


//        winning(340672);

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
//    private void sendNotification(RemoteMessage remoteMessage) {
//        Intent intent = new Intent(this, SecondPagerActivity.class);
//        if(remoteMessage.getData().get("order_id")!= null) {
//            intent.putExtra("event", remoteMessage.getData().get("event"));
//            intent.putExtra("order_id", remoteMessage.getData().get("order_id"));
//        }else if(remoteMessage.getData().get("game_id")!= null) {
//            intent.putExtra("event", remoteMessage.getData().get("event"));
//            intent.putExtra("order_id", remoteMessage.getData().get("game_id"));
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
////                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(bm)
//                .setContentTitle(remoteMessage.getNotification().getTitle()+"")
//                .setContentText(remoteMessage.getNotification().getBody() + "")
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


//    }
    private void sendNotification(RemoteMessage remoteMessage) {
        Log.e("TAG_remote", remoteMessage.toString());
        Intent intent = new Intent(this, SecondPagerActivity.class);
        intent.putExtra("event", remoteMessage.getData().get("event"));
        if (remoteMessage.getData().get("order_id") != null) {
            intent.putExtra("order_id", remoteMessage.getData().get("order_id"));
        } else if (remoteMessage.getData().get("game_id") != null) {
            intent.putExtra("game_id", remoteMessage.getData().get("game_id"));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_small, 0)
                .setLargeIcon(bitmap)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentTitle(remoteMessage.getNotification().getTitle() + "")
                .setContentText(remoteMessage.getNotification().getBody() + "")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (remoteMessage.getData().get("order_id") != null) {
            notificationManager.notify(Integer.parseInt(remoteMessage.getData().get("order_id")) /* ID of notification */, notificationBuilder.build());
        } else if (remoteMessage.getData().get("game_id") != null) {
            notificationManager.notify(Integer.parseInt(remoteMessage.getData().get("game_id")) /* ID of notification */, notificationBuilder.build());
        } else if ("payment_succeeded".equals(remoteMessage.getData().get("event"))) {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        } else {
            notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
        }

    }


    //中奖的时候
    private void winning(int order_id) {
        String token = Utils.getSpData("token", this);
        String url = MyApplication.url + "/v1/game-orders/" + order_id + "?timezone=" + MyApplication.utc;
        Log.e("TAG——noti", url);
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(this) + "");
        //请求登陆接口
        final String finalToken = token;
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response,String link) {
                        Intent intent = new Intent();
                        intent.setAction("Message");
                        intent.putExtra("response", response);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void error(final int requestCode, final String message) {
                        Log.e("TAG——noti", requestCode + message);
                    }

                    @Override
                    public void failure(Exception exception) {
                    }
                }
        );
    }

    @Override
    public void zzm(Intent intent) {
        Log.e("TAG_zzm", intent.getExtras().toString() + "");
        int requestCode = 0;
        Intent launchIntent = null;
        if ("secondpager".equals(intent.getStringExtra("gcm.notification.click_action"))) {
            launchIntent = new Intent(this, SecondPagerActivity.class);

            if (intent.getStringExtra("event") != null && intent.getStringExtra("event").equals("user_won")) {
                winning(Integer.parseInt(intent.getStringExtra("order_id")));
            }

            launchIntent.putExtra("event", intent.getStringExtra("event"));
            if (intent.getStringExtra("order_id") != null) {
                launchIntent.putExtra("order_id", intent.getStringExtra("order_id"));
                requestCode = Integer.parseInt(intent.getStringExtra("order_id"));
            } else if (intent.getStringExtra("game_id") != null) {
                launchIntent.putExtra("game_id", intent.getStringExtra("game_id"));
                requestCode = Integer.parseInt(intent.getStringExtra("game_id"));
            }
        } else {
            launchIntent = new Intent(this, MainActivity.class);
        }

        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_small)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLargeIcon(rawBitmap)
                .setContentTitle(intent.getStringExtra("gcm.notification.title"))
                .setContentText(intent.getStringExtra("gcm.notification.body"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (intent.getStringExtra("game_id") != null && intent.getStringExtra("order_id") != null) {
            notificationManager.notify(Integer.parseInt(intent.getStringExtra("game_id")) /* ID of notification */, notificationBuilder.build());
        }else if(intent.getStringExtra("game_id") == null && intent.getStringExtra("order_id") != null) {
            notificationManager.notify(Integer.parseInt(intent.getStringExtra("order_id")) /* ID of notification */, notificationBuilder.build());
        }else if(intent.getStringExtra("game_id") != null && intent.getStringExtra("order_id") == null) {
            notificationManager.notify(Integer.parseInt(intent.getStringExtra("game_id")) /* ID of notification */, notificationBuilder.build());
        }else if ("payment_succeeded".equals(intent.getStringExtra("event"))) {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        } else {
            notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
        }

    }

}
