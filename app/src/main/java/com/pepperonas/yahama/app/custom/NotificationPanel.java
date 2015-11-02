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
import android.util.Log;
import android.widget.RemoteViews;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.NotificationActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;
import com.pepperonas.yahama.app.utils.Utils;

/**
 * Created by martin on 12.08.2015.
 * NotificationPanel
 */
public class NotificationPanel {

    private static final String TAG = "NtfctnPanel";

    private static final int DEFAULT_NOTIFICATION = 0;

    private Context mCtx;
    private NotificationManager mManager;

    private NotificationCompat.Builder mBuilder;
    private final RemoteViews mRemoteViews;
    private final int mBackgroundColor;
    private final int mIconColor;


    public NotificationPanel(Activity ctx) {
        this.mCtx = ctx;

        mBackgroundColor = Setup.getNotificationBackground();
        mIconColor = Setup.getNotificationIconColor();

        mBuilder = new NotificationCompat.Builder(ctx)
                .setContentTitle(ctx.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(Setup.getNotificationOngoing());

        mRemoteViews = new RemoteViews(ctx.getPackageName(), R.layout.notification_view);
        mRemoteViews.setInt(R.id.notification_layout, "setBackgroundColor", mBackgroundColor);

        initMute();
        initPause();
        initPlay();
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
        Log.d(TAG, "NOTIFICATION UPDATE!! ~~");
        initMute();
        mManager.notify(DEFAULT_NOTIFICATION, mBuilder.build());
    }


    public void initMute() {
        Intent mute = new Intent(mCtx, NotificationActivity.class);
        mute.putExtra("notification_filter", "mute");
        PendingIntent btnMute = PendingIntent.getActivity(mCtx, 0, mute, PendingIntent.FLAG_UPDATE_CURRENT);
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


    private void initPause() {
        Intent pause = new Intent(mCtx, NotificationActivity.class);
        pause.putExtra("notification_filter", "pause");
        PendingIntent btnPause = PendingIntent.getActivity(mCtx, 1, pause, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.ntfctn_btn_pause, btnPause);

        Drawable d = new IconicsDrawable(mCtx, CommunityMaterial
                .Icon.cmd_pause).colorRes(mIconColor).sizeDp(Const.NOTIFICATION_ICON_SIZE);

        Bitmap b = Utils.drawableToBitmap(d);
        mRemoteViews.setImageViewBitmap(R.id.ntfctn_btn_pause, b);
    }


    private void initPlay() {
        Intent play = new Intent(mCtx, NotificationActivity.class);
        play.putExtra("notification_filter", "play");
        PendingIntent btnPlay = PendingIntent.getActivity(mCtx, 2, play, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.ntfctn_btn_play, btnPlay);

        Drawable d = new IconicsDrawable(mCtx, CommunityMaterial
                .Icon.cmd_play).colorRes(mIconColor).sizeDp(Const.NOTIFICATION_ICON_SIZE);

        Bitmap b = Utils.drawableToBitmap(d);
        mRemoteViews.setImageViewBitmap(R.id.ntfctn_btn_play, b);
    }


    private void initVolume() {
        Intent volume = new Intent(mCtx, NotificationActivity.class);
        volume.putExtra("notification_filter", "volume");
        PendingIntent btnVolume = PendingIntent.getActivity(mCtx, 3, volume, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.ntfctn_btn_volume, btnVolume);

        Drawable d = new IconicsDrawable(mCtx, CommunityMaterial
                .Icon.cmd_vector_point).colorRes(mIconColor).sizeDp(Const.NOTIFICATION_ICON_SIZE);

        Bitmap b = Utils.drawableToBitmap(d);
        mRemoteViews.setImageViewBitmap(R.id.ntfctn_btn_volume, b);
    }


    private void initLauncher() {
        Intent launch = new Intent(mCtx, NotificationActivity.class);
        launch.putExtra("notification_filter", "launch");
        PendingIntent btnVolume = PendingIntent.getActivity(mCtx, 4, launch, PendingIntent.FLAG_NO_CREATE);
        mRemoteViews.setOnClickPendingIntent(R.id.ntfctn_btn_launcher, btnVolume);

        Drawable d = new IconicsDrawable(mCtx, CommunityMaterial
                .Icon.cmd_open_in_app).colorRes(mIconColor).sizeDp(Const.NOTIFICATION_ICON_SIZE);

        Bitmap b = Utils.drawableToBitmap(d);
        mRemoteViews.setImageViewBitmap(R.id.ntfctn_btn_launcher, b);
    }

}
