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

package com.pepperonas.yahama.app.custom;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pepperonas.jbasx.log.Log;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.NotificationActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.VolumeSliderActivity;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;
import com.pepperonas.yahama.app.utils.Utils;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class NotificationPanel {

    private static final String TAG = "NotificationPanel";

    private static final int DEFAULT_NOTIFICATION = 0;

    private Context mCtx;
    private NotificationManager mManager;

    private NotificationCompat.Builder mBuilder;
    private final RemoteViews mRemoteViews;
    private final int mIconColor;


    public NotificationPanel(Activity ctx) {
        this.mCtx = ctx;

        mIconColor = Setup.getNotificationIconColor();

        mBuilder = new NotificationCompat.Builder(ctx)
                .setContentTitle(ctx.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.ic_notification)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(Setup.getNotificationOngoing());

        mRemoteViews = new RemoteViews(ctx.getPackageName(), R.layout.notification_view);
        mRemoteViews.setInt(R.id.notification_layout, "setBackgroundColor", Setup.getNotificationBackground());

        initMute();
        initClose();
        initVolume();
        initLauncher();

        mBuilder.setContent(mRemoteViews);

        mManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.notify(DEFAULT_NOTIFICATION, mBuilder.build());
    }


    public void cancel() {
        mManager.cancel(DEFAULT_NOTIFICATION);
    }


    public void update() {
        Log.d(TAG, "NOTIFICATION UPDATE!");
        initMute();
        mManager.notify(DEFAULT_NOTIFICATION, mBuilder.build());
    }


    /**
     * Handle in {@link MainActivity}, see {@link MainActivity#mNotificationReceiver}.
     */
    public void initMute() {
        Intent mute = new Intent("mute");
        PendingIntent btnMute = PendingIntent.getBroadcast(mCtx, 0, mute, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.ntfctn_btn_mute, btnMute);

        Drawable d;
        if (MainActivity.getAmp().isMute()) {
            d = new IconicsDrawable(
                    mCtx, GoogleMaterial.Icon.gmd_volume_up).colorRes(mIconColor).sizeDp(Const.NOTIFICATION_ICON_SIZE);
        } else {
            d = new IconicsDrawable(
                    mCtx, GoogleMaterial.Icon.gmd_volume_off).colorRes(mIconColor).sizeDp(Const.NOTIFICATION_ICON_SIZE);
        }

        Bitmap icMute = Utils.drawableToBitmap(d);

        mRemoteViews.setImageViewBitmap(R.id.ntfctn_btn_mute, icMute);
    }


    /**
     * Handle in {@link VolumeSliderActivity}.
     */
    private void initVolume() {
        Intent volume = new Intent(mCtx, VolumeSliderActivity.class);

        volume.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent btnVolume = PendingIntent.getActivity(mCtx, 3, volume, 0);

        mRemoteViews.setOnClickPendingIntent(R.id.ntfctn_btn_volume, btnVolume);

        Drawable d = new IconicsDrawable(mCtx, CommunityMaterial
                .Icon.cmd_vector_point).colorRes(mIconColor).sizeDp(Const.NOTIFICATION_ICON_SIZE);

        Bitmap b = Utils.drawableToBitmap(d);
        mRemoteViews.setImageViewBitmap(R.id.ntfctn_btn_volume, b);
    }


    /**
     * Handle in {@link NotificationActivity}, see {@link NotificationActivity#launchApp}.
     */
    private void initLauncher() {
        Intent launch = new Intent(mCtx, NotificationActivity.class);
        launch.putExtra("notification_filter", "launch");
        launch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent btnLaunch = PendingIntent.getActivity(mCtx, 4, launch, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.ntfctn_btn_launcher, btnLaunch);

        Drawable d = new IconicsDrawable(mCtx, CommunityMaterial
                .Icon.cmd_open_in_app).colorRes(mIconColor).sizeDp(Const.NOTIFICATION_ICON_SIZE);

        Bitmap b = Utils.drawableToBitmap(d);
        mRemoteViews.setImageViewBitmap(R.id.ntfctn_btn_launcher, b);
    }


    /**
     * Handle in {@link MainActivity}, see {@link MainActivity#mNotificationReceiver}.
     */
    private void initClose() {
        Intent close = new Intent("close");
        PendingIntent btnClose = PendingIntent.getBroadcast(mCtx, 1, close, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.ntfctn_btn_close, btnClose);

        Drawable d = new IconicsDrawable(mCtx, GoogleMaterial
                .Icon.gmd_close).colorRes(mIconColor).sizeDp(Const.NOTIFICATION_ICON_SIZE);

        Bitmap b = Utils.drawableToBitmap(d);
        mRemoteViews.setImageViewBitmap(R.id.ntfctn_btn_close, b);
    }

}
