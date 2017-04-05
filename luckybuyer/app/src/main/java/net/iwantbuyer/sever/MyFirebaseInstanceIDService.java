/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.iwantbuyer.sever;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.bean.FCMBean;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.MyBase64;
import net.iwantbuyer.utils.Utils;

import java.util.HashMap;
import java.util.Map;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("TAG_firebase", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        Utils.setSpData("refreshedToken",token,this);
        String mToken = Utils.getSpData("token", this);
        if(mToken == null || "".equals(mToken) || token == null) {
            return;
        }
        String lang = Utils.getSpData("locale", this);
        if(lang != null && !lang.equals("")) {
            lang = lang.split("-")[0] + "";
        }
        String url = MyApplication.url + "/v1/fcm/registrations/?timezone=" + MyApplication.utc;
        FCMBean fcm = new FCMBean(lang,"android",token);
        String json = fcm.toString();

        Map map = new HashMap();
        map.put("Authorization", "Bearer " + mToken);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(this) + "");
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
            }

            @Override
            public void error(final int code, final String message) {
            }

            @Override
            public void failure(Exception exception) {
            }
        });
    }
}
