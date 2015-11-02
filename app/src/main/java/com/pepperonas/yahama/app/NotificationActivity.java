/*
 * Copyright (c) 2015 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pepperonas.yahama.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.pepperonas.yahama.app.dialogs.DialogVolumeSlider;
import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class NotificationActivity extends Activity {

    private static final String TAG = "NotifRtSlt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Setup.getTheme() != 0) {
            setTheme(R.style.Transparent_Light);
        }

        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            String action = getIntent().getStringExtra("notification_filter");
            if (action.equalsIgnoreCase("launch")) {
                Log.i(TAG, "launch");
                launchApp();
            } else if (action.equalsIgnoreCase("volume")) {
                Log.i(TAG, "volume");
                DialogVolumeSlider dsv = new DialogVolumeSlider(this);
                dsv.show();
            } else if (action.equalsIgnoreCase("pause")) {
                Log.i(TAG, "pause");
                sendMessage("pause");
                finish();
            } else if (action.equalsIgnoreCase("play")) {
                Log.i(TAG, "play");
                sendMessage("play");
                finish();
            } else if (action.equalsIgnoreCase("mute")) {
                Log.i(TAG, "mute");
                sendMessage("mute");
                finish();
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    private void launchApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        //        PendingIntent intent = PendingIntent.getActivity(this, 0, intent, 0);
        //        notification.setLatestEventInfo(context, title, message, intent);
        //        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //        notificationManager.notify(0, notification);
    }


    private void sendMessage(String which) {
        Log.d(TAG, "Broadcasting message");
        Intent intent = new Intent("notification_action");
        intent.putExtra("button_clicked", which);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
