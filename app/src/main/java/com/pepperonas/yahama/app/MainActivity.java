/*
 * Copyright (c) 2016 Martin Pfeffer
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.vending.billing.IInAppBillingService;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pepperonas.aesprefs.AesPrefs;
import com.pepperonas.andbasx.animation.FadeAnimation;
import com.pepperonas.yahama.app.custom.NotificationPanel;
import com.pepperonas.yahama.app.dialogs.DialogCinemaDsp3d;
import com.pepperonas.yahama.app.dialogs.DialogDeviceInfo;
import com.pepperonas.yahama.app.dialogs.DialogError;
import com.pepperonas.yahama.app.dialogs.DialogHelpAndFeedback;
import com.pepperonas.yahama.app.dialogs.DialogLookupFailed;
import com.pepperonas.yahama.app.dialogs.DialogPurchasePremium;
import com.pepperonas.yahama.app.dialogs.DialogSleeptimer;
import com.pepperonas.yahama.app.dialogs.DialogThx;
import com.pepperonas.yahama.app.dialogs.DialogWifiDisabled;
import com.pepperonas.yahama.app.fragments.AudioFragment;
import com.pepperonas.yahama.app.fragments.DspFragment;
import com.pepperonas.yahama.app.fragments.InputFragment;
import com.pepperonas.yahama.app.fragments.SettingsFragment;
import com.pepperonas.yahama.app.fragments.WebradioFragment;
import com.pepperonas.yahama.app.inab.IabHelper;
import com.pepperonas.yahama.app.inab.IabResult;
import com.pepperonas.yahama.app.inab.Inventory;
import com.pepperonas.yahama.app.inab.Purchase;
import com.pepperonas.yahama.app.model.AmpYaRxV577;
import com.pepperonas.yahama.app.model.entity.AudioEntityAdvanced;
import com.pepperonas.yahama.app.model.entity.AudioRoom;
import com.pepperonas.yahama.app.model.entity.AudioRoomAdvanced;
import com.pepperonas.yahama.app.model.entity.Entertainment;
import com.pepperonas.yahama.app.model.entity.SurroundEntity;
import com.pepperonas.yahama.app.model.movie.movie.Standard;
import com.pepperonas.yahama.app.utility.Commands;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;
import com.pepperonas.yahama.app.utils.Constants;
import com.pepperonas.yahama.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private NavigationView mNavView;
    private int mLastSelectedNavItemPos = 0;

    private Toolbar mToolbar;
    private String mLastSelection = "";
    private TextView mTvNavViewTitle, mTvNavViewSubtitle;

    private FloatingActionMenu mFabMenu;
    private FloatingActionButton mFabPower, mFabMute;

    private FrameLayout mMainFrame;
    private Fragment mFragment;

    private Timer mDriverTimer = null;

    private Handler mHandler = new Handler();
    private Handler mTitleWriterHandler = new Handler();

    private long mLastExecution = 0L;
    private long mDeltaConnection = 0L;

    private static AmpYaRxV577 mAmp;

    private static MaterialDialog mProgressDialog;

    private static NotificationPanel mNotificationPanel;

    private static SeekBar mInvisibleVolSlider;

    private boolean mStartedSpotify = false;

    private FloatingActionButton mFabSleeptimer;
    private FloatingActionButton mFabDeviceInfo;

    private IabHelper mHelper;

    private Runnable mTitleWriterRunnable = new Runnable() {
        @Override
        public void run() {
            if (getLastSelection() != null) {
                setTitle(getLastSelection());
            }
        }
    };

    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("mute")) {
                Log.d(TAG, "onReceive  " + "mute");
                mFabMute.callOnClick();
                mNotificationPanel.update();
            } else if (intent.getAction().equals("close")) {
                Log.d(TAG, "onReceive  " + "close");
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setup.getTheme() != 0) {
            setTheme(R.style.AppTheme_Light);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.primaryColor_light));
            }
        } else {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.primaryColor));
            }
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mLastSelection = getString(R.string.audio);

        mTvNavViewTitle = (TextView) findViewById(R.id.nav_view_header_title);
        mTvNavViewSubtitle = (TextView) findViewById(R.id.nav_view_header_subtitle);
        setNavViewSubtitle(getString(R.string.not_connected));

        mAmp = new AmpYaRxV577();

        mInvisibleVolSlider = new SeekBar(this);

        if (Utils.isConnected(MainActivity.this)) {
            startApp();
        } else new DialogWifiDisabled(MainActivity.this).showDialog();


        ensureAdvertising();

        storeCurrentVersion();

        loadIabHelper();

        doAnalytics("MainActivity", "Starting...");


        IntentFilter filter = new IntentFilter();
        filter.addAction("mute");
        filter.addAction("close");
        // Add other actions as needed

        registerReceiver(mNotificationReceiver, filter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Setup.getShowNotification()) {
            mNotificationPanel = new NotificationPanel(this);
        }

        // a proper connection is mandatory
        if (!Utils.isConnected(MainActivity.this)) return;

        mDriverTimer = new Timer();
        mDriverTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(mDriverRunnable);
            }
        }, 0, Const.DRIVER_REFRESH_RATE);
    }


    @Override
    protected void onPause() {
        if (mStartedSpotify) {
            mStartedSpotify = false;
            selectNavViewItem(mNavView.getMenu().getItem(0).getSubMenu().getItem(mLastSelectedNavItemPos));
        }

        try {

            stopDriver();

            dismissProgressDialog();

        } catch (Exception e) {
            Log.e(TAG, "Error in onPause - Msg: " + e.getMessage());
        }

        super.onPause();
    }


    @Override
    protected void onDestroy() {
        if (mNotificationPanel != null) {
            mNotificationPanel.cancel();
        }

        if (mNotificationReceiver != null) {
            unregisterReceiver(mNotificationReceiver);
        }

        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHandler.removeCallbacks(mDriverRunnable);
        outState.putInt("selection", mLastSelectedNavItemPos);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLastSelectedNavItemPos = savedInstanceState.getInt("selection");

        if (mNavView == null) {
            mNavView = (NavigationView) findViewById(R.id.navigation_view);
        }

        if (mNavView != null && mNavView.getMenu() != null) {
            if (mLastSelectedNavItemPos < 5) {
                selectNavViewItem(mNavView.getMenu().getItem(0).getSubMenu().getItem(mLastSelectedNavItemPos));
            } else if (mLastSelectedNavItemPos == 5) {
                selectNavViewItem(mNavView.getMenu().getItem(1).getSubMenu().getItem(0));
            } else {
                selectNavViewItem(mNavView.getMenu().getItem(0).getSubMenu().getItem(0));
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navDrawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();

        switch (event.getKeyCode()) {

            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    mInvisibleVolSlider.incrementProgressBy(Setup.getVolumeSteps());
                }
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    mInvisibleVolSlider.incrementProgressBy(Setup.getVolumeSteps() * -1);
                }
                return true;
        }
        return super.dispatchKeyEvent(event);
    }


    private void storeCurrentVersion() {
        AesPrefs.put("current_version", Utils.getAppVersionName(this));
    }


    private void ensureAdvertising() {
        if (Setup.getInstallationDate() == -1) Setup.setInstallationDate(System.currentTimeMillis());

        else if (!Setup.getPremium()) {
            long delta = System.currentTimeMillis() - Setup.getInstallationDate();
            if (delta > (Constants.WEEK_IN_MS)) {

                Setup.setAdvertising(true);

                if (Setup.getShowDialogPurchasePremium()) {
                    new DialogPurchasePremium(MainActivity.this);
                }
            }
        } else Setup.setAdvertising(false);
    }


    private void doAnalytics(String screenName, String action) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        Tracker tracker = analytics.newTracker(Const.ANALYTICS_ID);

        // All subsequent hits will be send with screen name = "main screen"
        tracker.setScreenName(screenName);

        Log.d(TAG, "doAnalytics tracker: " + tracker.toString());

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("General")
                .setAction(action)
                .setLabel("x")
                .build());
    }


    private void startApp() {

        runConnectionTask(true);

        initToolbar();

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        initFab();

        initNavView();

        initNavDrawer();

        if (getIntent() != null) {
            if (getIntent().getBooleanExtra("theme_changed", false)) {
                makeFragmentTransaction(SettingsFragment.newInstance(0));
                return;
            }
        }
        makeFragmentTransaction(AudioFragment.newInstance(0));
    }


    /**
     * if enable-wifi-request is successfull, we will proceed here
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Const.REQ_CODE_ENABLE_WIFI:
                waitForWifi();
                break;
            case Const.REQ_CODE_PREMIUM_PURCHASE:
                processPurchase(resultCode, data);
                break;
            case Const.REQ_CODE_SELECT_PICTURE:
                storeIcon(data);
                break;
        }
    }


    private void storeIcon(Intent data) {
        Log.d(TAG, "onActivityResult  " + "icon selected...");
        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("icon", Context.MODE_PRIVATE);
            File path = new File(directory, "custom_app_icon.png");

            InputStream is = MainActivity.this.getContentResolver().openInputStream(data.getData());
            FileOutputStream fos = new FileOutputStream(path);

            int read;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                fos.write(bytes, 0, read);
            }
            fos.close();
            is.close();

            AesPrefs.put("custom_icon_path", path.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadIconFromStorage();
    }


    private void loadIconFromStorage() {
        ImageView iv = (ImageView) findViewById(R.id.iv_logo);
        try {
            File f = new File(AesPrefs.get("custom_icon_path", ""));
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            iv.setImageBitmap(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void processPurchase(int resultCode, Intent data) {
        int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
        String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
        String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

        if (resultCode == RESULT_OK) {
            try {
                JSONObject jo = new JSONObject(purchaseData);
                String sku = jo.getString("productId");

                Setup.setPremium(true);
                if (mFragment instanceof SettingsFragment) {
                    SettingsFragment sf = (SettingsFragment) mFragment;
                    sf.updateSummaries();
                }

                new DialogThx(MainActivity.this);
            } catch (JSONException e) {
                new DialogError(MainActivity.this);
            }
        }
    }


    private void waitForWifi() {
        mProgressDialog = new MaterialDialog.Builder(MainActivity.this)
                .title(R.string.progress_dialog_title_waiting_for_wifi)
                .content(R.string.progress_dialog_content_waiting_for_wifi)
                .progress(true, 0)
                .show();

        // creating a thread to wait until the connection is established
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!Utils.isConnected(MainActivity.this)) {
                        Thread.sleep(300);
                    }
                    mProgressDialog.dismiss();
                    // restarting main
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    MainActivity.this.finish();
                    MainActivity.this.startActivity(i);
                } catch (Exception e) {
                    Log.e(TAG, "Error in run - Msg: " + e.getMessage());
                }
            }
        };
        t.start();
    }


    public void runConnectionTask(boolean showDialog) {
        new ConnectionTask(showDialog).execute(Utils.getCurrentNetwork());
    }


    public void runCtrlrTask(boolean showProgress, String... s) {
        if (System.currentTimeMillis() > (mLastExecution + Const.DELAY_BETWEEN_COMMANDS)) {
            mLastExecution = System.currentTimeMillis();
            new ControllerTask(showProgress).execute(s);
        }
    }


    public class ConnectionTask extends AsyncTask<String, Void, String> {

        boolean showDialog;


        public ConnectionTask(boolean showDialog) {
            this.showDialog = showDialog;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDeltaConnection = System.currentTimeMillis();
            if (showDialog) {
                mProgressDialog = new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.progress_dialog_title_lookup)
                        .content(R.string.progress_dialog_content_lookup)
                        .progress(true, 0)
                        .show();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String networkAddress = params[0];
            String[] deviceInfo = {"", "", ""};

            int lastKnownByte = Setup.getLastKnownByte();

            if (lastKnownByte != -1) {
                deviceInfo = checkIp(networkAddress, lastKnownByte);
            }

            if (!deviceInfo[0].isEmpty() && !deviceInfo[1].isEmpty()) {
                mAmp.setIp(deviceInfo[0] + deviceInfo[1]);
                Setup.setLastKnownByte(Integer.parseInt(deviceInfo[1]));
                mAmp.setDeviceName(deviceInfo[2]);
                return "passed";
            }

            int lastByte = 0;
            while (deviceInfo[0].isEmpty()
                    && deviceInfo[1].isEmpty()
                    && lastByte < 256) {
                deviceInfo = checkIp(networkAddress, lastByte);

                if (!deviceInfo[0].isEmpty() && !deviceInfo[1].isEmpty()) {
                    mAmp.setIp(deviceInfo[0] + deviceInfo[1]);
                    Setup.setLastKnownByte(Integer.parseInt(deviceInfo[1]));
                    mAmp.setDeviceName(deviceInfo[2]);
                    return "passed";
                }

                lastByte++;
            }
            return "failed";
        }


        /**
         * @return deviceInfo:
         * 0=networkAddress,
         * 1=lastByte,
         * 2=deviceName
         */
        private String[] checkIp(String networkAddress, int lastByte) {

            String deviceInfo[] = {"", "", ""};

            String _deviceIp = networkAddress + String.valueOf(lastByte);

            try {
                URL url = new URL("http://" + _deviceIp + AmpYaRxV577.PORT + AmpYaRxV577.AV_SPECS);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Accept", "application/xml");
                con.setDoOutput(true);
                con.setConnectTimeout(Setup.getTimeoutForLookup());

                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                dos.writeBytes(Commands.GET_INFO);
                dos.flush();
                dos.close();

                if (con.getResponseCode() == 200) {
                    Log.i(TAG, "PASS! " + con.getResponseMessage() + " (" + con.getResponseCode() + ")");
                    deviceInfo[0] = networkAddress;
                    deviceInfo[1] = String.valueOf(lastByte);
                    deviceInfo[2] = extractDeviceName(con);
                    return deviceInfo;
                }

                InputStream is = checkConnection(con);

                if (is == null) return deviceInfo;

                mAmp.dbgMsg();
            } catch (Exception e) {
                Log.e(TAG, "Error in checkIp - Msg: " + e.getMessage());
            }

            return deviceInfo;
        }


        private String extractDeviceName(HttpURLConnection con) {
            if (con.getHeaderField(0) != null) {
                String _deviceName = con.getHeaderField(0);
                if (_deviceName.contains("(") && _deviceName.contains(")")) {
                    return _deviceName.split("\\(")[1].replace(")", "");
                }
            }
            return "";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mProgressDialog.dismiss();

            if (s.equals("passed")) {
                onConnectPassed();
            } else onConnectFailed();

            mDeltaConnection = 0;
        }


        private void onConnectFailed() {
            Log.e(TAG, "AmpConnectorTask FAILED: " + mAmp.getDeviceName() + " / " + mAmp.getIp() + "\n" +
                    "t.o.e.: " + (System.currentTimeMillis() - mDeltaConnection));

            setNavViewSubtitle(getString(R.string.not_connected));
            new DialogLookupFailed(MainActivity.this);
        }


        private void onConnectPassed() {
            Log.d(TAG, "AmpConnectorTask PASSED: " + mAmp.getDeviceName() + " / " + mAmp.getIp() + "\n" +
                    "t.o.e.: " + (System.currentTimeMillis() - mDeltaConnection));

            if (!mAmp.getDeviceName().isEmpty()) setNavViewTitle(mAmp.getDeviceName());

            setNavViewSubtitle(getString(R.string.connected) +
                    getString(R.string.DETAIL_DIVIDER) +
                    getNavDrawerDetail());
        }

    }


    final Runnable mDriverRunnable = new Runnable() {
        @Override
        public void run() {

            if (mFragment instanceof AudioFragment || mFragment instanceof DspFragment) {
                runCtrlrTask(false, Commands.GET_INFO, Const.M_GET_INFO);
            }

            if (mFragment instanceof WebradioFragment) {
                runCtrlrTask(false, Commands.LIST_INFO(AmpYaRxV577.XML_INPUT_NET_RADIO));
            }

            if (mAmp.getIp().isEmpty()) return;

            if (!mAmp.isOn()) {
                setNavViewSubtitle(getString(R.string.powered_off) +
                        getString(R.string.DETAIL_DIVIDER) +
                        mAmp.getIp());
            } else {
                setNavViewSubtitle(getString(R.string.connected) +
                        getString(R.string.DETAIL_DIVIDER) +
                        getNavDrawerDetail());
            }

        }
    };


    public class ControllerTask extends AsyncTask<String, Void, String> {

        boolean showDialog;
        long sTime;


        public ControllerTask(boolean showDialog) {
            this.showDialog = showDialog;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sTime = System.currentTimeMillis();

            if (showDialog) {
                mProgressDialog = new MaterialDialog.Builder(
                        MainActivity.this).progress(true, 0).show();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection con;
            StringBuilder resp = null;
            try {
                con = initHttpUrlConnection();
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                dos.writeBytes(params[0]);
                dos.flush();
                dos.close();
                InputStream is = checkConnection(con);
                resp = collectData(con, is);
            } catch (IOException e) {
                Log.e(TAG, "Error in doInBackground - Msg: " + e.getMessage());
            }

            if (nullCheck(resp)) return "";

            if (resp != null) {
                String s = resp.toString();

                for (String param : params) s += param;
                return s;
            }
            return "error";
        }


        private boolean nullCheck(StringBuilder resp) {
            if (resp == null) {
                Log.e(TAG, "Error in doInBackground - Msg: NULL");
                return true;
            }
            return false;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "onPostExecute  " + s);

            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (s.isEmpty()) return;

            if (s.contains(AmpYaRxV577.RESPONSE_SUCCESS)
                    && !s.contains("<List_Info>")) {

                initAudioSetup(s);

                initSurroundPrograms(s);

                if (!s.contains(Const.DO_NOT_UPDATE)) {
                    updateSwitches();
                }

            }

            reinitFab();

            Log.wtf(TAG, "Controller t.o.e.: " + (System.currentTimeMillis() - sTime) + " ms.");


            if (s.contains("<" + AmpYaRxV577.XML_INPUT_NET_RADIO + "><List_Info>")) {
                initRadioList(s);
            }

            if (s.contains("<NET_RADIO><Play_Info><Feature_Availability>Ready")) {
                initRadioTrackInfo(s);
            }

        }
    }


    private void initRadioList(String info) {

        List<String> content = mAmp.collectListInfo(info);
        if (mFragment instanceof WebradioFragment) {
            WebradioFragment wrf = (WebradioFragment) mFragment;
            if (info.contains("<Max_Line>")) {
                int maxLine = Integer.parseInt(info.split("<Max_Line>")[1].split("</")[0]);
                Log.w(TAG, "initRadioList MAXLINE = " + maxLine);
                wrf.initRadioList(maxLine, content, info);
            }
        }

        /*
        * Here goes the fun part!
        * */
        runCtrlrTask(false, Commands.PLAY_INFO(AmpYaRxV577.XML_INPUT_NET_RADIO));
    }


    private void initRadioTrackInfo(String info) {
        String[] params = info.split("<Meta_Info>");
        Log.d(TAG, "INIT RADIO TRACK INFO");
        String station = Utils.getValueFromXml(AmpYaRxV577.STATION, params[1]);
        String song = Utils.getValueFromXml(AmpYaRxV577.SONG, params[1]);
        String album = Utils.getValueFromXml(AmpYaRxV577.ALBUM, params[1]);
        getAmp().getRadioStation().setSong(song);
        getAmp().getRadioStation().setStation(station);
        getAmp().getRadioStation().setAlbum(album);
    }


    private void initSpotifyTrackInfo(String info) {

        String[] params = info.split("<Meta_Info>");
        String station = "", song = "", album = "", artist = "", track = "";

        if (getAmp().getInputAsXml().equals(AmpYaRxV577.XML_INPUT_SPOTIFY)) {
            artist = Utils.getValueFromXml(AmpYaRxV577.ARTIST, params[1]);
            track = Utils.getValueFromXml(AmpYaRxV577.TRACK, params[1]);
            album = Utils.getValueFromXml(AmpYaRxV577.ALBUM, params[1]);

        }

        Log.d(TAG, "initTrackInfo  " + station + " | " + song + " | " + artist + " | " + track + " | " + album);
    }


    private void initAudioSetup(String s) {
        if (s.contains(Const.M_GET_INFO)) {
            mAmp.initAmpVars(MainActivity.this, s);
        }

        if (s.contains(Const.M_VOLUME_SET)) {
            float volume = mAmp.getVolumeFromTrailer(s.split(Const.M_VOLUME_SET)[1]);
            updateVolumeSlider(volume);
        }

        if (s.contains(Const.M_BASS_SET)) {
            int bass = mAmp.getBassFromTrailer(s.split(Const.M_BASS_SET)[1]);
            mAmp.setBass(bass);
        }

        if (s.contains(Const.M_TREBLE_SET)) {
            int treble = mAmp.getTrebleFromTrailer(s.split(Const.M_TREBLE_SET)[1]);
            mAmp.setTreble(treble);
        }

        if (s.contains(Const.M_INPUT_SET)) {
            String input = s.split(Const.M_INPUT_SET)[1];
            mAmp.setActiveInput(input);
        }

        if (s.contains(Const.M_MUTE)) {
            boolean mute = s.split(Const.M_MUTE)[1].equals("true");
            mAmp.setMute(mute);
        }

        if (s.contains(Const.M_GET_INFO)) {
            if (s.contains("<Power_Control><Power>On")) mAmp.setOn(true);
            else mAmp.setOn(false);
        }

        if (s.contains(Const.M_ENHANCER_SET)) {
            boolean enhancer = (s.split(Const.M_ENHANCER_SET)[1]).equals("true");
            mAmp.setEnhancer(enhancer);
        }

        Log.d(TAG, "initAmpVars s: " + s);
        if (s.contains(Const.M_SPKR_A_SET)) {
            boolean spkrAset = (s.split(Const.M_SPKR_A_SET)[1]).equals("true");
            mAmp.setSpeakerA(spkrAset);
        }

        if (s.contains(Const.M_SPKR_B_SET)) {
            boolean spkrBset = (s.split(Const.M_SPKR_B_SET)[1]).equals("true");
            mAmp.setSpeakerB(spkrBset);
        }

    }


    private void initSurroundPrograms(String s) {
        if (s.contains("<Sound_Program_Param><CLASSICAL>")) {
            initConfigClassical(s);
        }

        if (s.contains("<Surround><Sound_Program_Param><LIVE_CLUB>")) {
            initConfigLiveClub(s);
        }

        if (s.contains("<Surround><Sound_Program_Param><MOVIE>")) {
            initConfigMovie(s);
        }

        if (s.contains("<Surround><Sound_Program_Param><ENTERTAINMENT>")) {
            initConfigEntertainment(s);
        }
    }


    private void initConfigEntertainment(String s) {
        if (s.contains("<" + AmpYaRxV577.SPORTS + ">")) {
            String sports = s.split("<" + AmpYaRxV577.SPORTS + ">")[1].split("</" + AmpYaRxV577.SPORTS)[0];
            initEntertainment(mAmp.getConfigEntertainment().getSports(), sports);
        }

        if (s.contains("<" + AmpYaRxV577.XML_ACTION_GAME + ">")) {
            String action = s.split("<" + AmpYaRxV577.XML_ACTION_GAME + ">")[1].split("</" + AmpYaRxV577.XML_ACTION_GAME)[0];
            initEntertainment(mAmp.getConfigEntertainment().getActionGame(), action);
        }

        if (s.contains("<" + AmpYaRxV577.XML_ROLEPLAYING_GAME + ">")) {
            String roleplay = s.split("<" + AmpYaRxV577.XML_ROLEPLAYING_GAME + ">")[1].split("</" + AmpYaRxV577
                    .XML_ROLEPLAYING_GAME)[0];
            initEntertainment(mAmp.getConfigEntertainment().getRoleplayingGame(), roleplay);
        }

        if (s.contains("<" + AmpYaRxV577.XML_MUSIC_VIDEO + ">")) {
            String musicVideo = s.split("<" + AmpYaRxV577.XML_MUSIC_VIDEO + ">")[1].split("</" + AmpYaRxV577.XML_MUSIC_VIDEO)[0];
            initEntertainment(mAmp.getConfigEntertainment().getMusicVideo(), musicVideo);
        }

        mAmp.getConfigEntertainment().dbgMsg();

        if (s.contains(Const.M_DIALOG_SHOW_DSP_CONFIG)) {
            new DialogCinemaDsp3d(this, Integer.parseInt(s.split(Const.M_DIALOG_SHOW_DSP_CONFIG)[1].split("</")[0]));
        }
    }


    private void initEntertainment(Entertainment e, String arg) {
        try {
            e.setDspLvl(Integer.parseInt(arg.split("<DSP_Lvl><Val>")[1].split("</")[0]));
            e.setRoomSize(Integer.parseInt(arg.split("<Room_Size><Val>")[1].split("</")[0]));
            e.setInitDly(Integer.parseInt(arg.split("<Init_Dly><Val>")[1].split("</")[0]));
            e.setSurRoomSize(Integer.parseInt(arg.split("<Sur_Room_Size><Val>")[1].split("</")[0]));
            e.setSurInitDly(Integer.parseInt(arg.split("<Sur_Init_Dly><Val>")[1].split("</")[0]));
            e.setSurBackInitDly(Integer.parseInt(arg.split("<Sur_Back_Init_Dly><Val>")[1].split("</")[0]));
            e.setSurBackRoomSize(Integer.parseInt(arg.split("<Sur_Back_Room_Size><Val>")[1].split("</")[0]));
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.e(TAG, "initEntertainment ArrayIndexOutOfBoundsException " + ex.getMessage());
        }
    }


    private void initConfigClassical(String s) {
        if (s.contains("<" + AmpYaRxV577.XML_HALL_IN_MUNICH + ">")) {
            String munich = s.split("<" + AmpYaRxV577.XML_HALL_IN_MUNICH + ">")[1].split("</" + AmpYaRxV577.XML_HALL_IN_MUNICH)
                    [0];
            initAudioRoom(mAmp.getConfigClassical().getHallInMunich(), munich);
        }

        if (s.contains("<" + AmpYaRxV577.XML_HALL_IN_VIENNA + ">")) {
            String vienna = s.split("<" + AmpYaRxV577.XML_HALL_IN_VIENNA + ">")[1].split("</" + AmpYaRxV577.XML_HALL_IN_VIENNA)
                    [0];
            initAudioRoom(mAmp.getConfigClassical().getHallInVienna(), vienna);
        }

        if (s.contains("<" + AmpYaRxV577.CHAMBER + ">")) {
            String chamber = s.split("<" + AmpYaRxV577.CHAMBER + ">")[1].split("</" + AmpYaRxV577.CHAMBER)[0];
            initAudioAdvanced(mAmp.getConfigClassical().getChamber(), chamber);
        }

        mAmp.getConfigClassical().dbgMsg();

        if (s.contains(Const.M_DIALOG_SHOW_DSP_CONFIG)) {
            new DialogCinemaDsp3d(this, Integer.parseInt(s.split(Const.M_DIALOG_SHOW_DSP_CONFIG)[1].split("</")[0]));
        }
    }


    private void initConfigLiveClub(String s) {
        if (s.contains("<" + AmpYaRxV577.XML_CELLAR_CLUB + ">")) {
            String cellar = s.split("<" + AmpYaRxV577.XML_CELLAR_CLUB + ">")[1]
                    .split("</" + AmpYaRxV577.XML_CELLAR_CLUB)[0];

            initAudioRoom(mAmp.getConfigLiveClub().getCellarClub(), cellar);
        }

        if (s.contains("<" + AmpYaRxV577.XML_THE_BOTTOM_LINE + ">")) {
            String bottom = s.split("<" + AmpYaRxV577.XML_THE_BOTTOM_LINE + ">")[1]
                    .split("</" + AmpYaRxV577.XML_THE_BOTTOM_LINE)[0];

            initAudioRoom(mAmp.getConfigLiveClub().getTheBottomLine(), bottom);
        }

        if (s.contains("<" + AmpYaRxV577.XML_THE_ROXY_THEATRE + ">")) {
            String roxy = s.split("<" + AmpYaRxV577.XML_THE_ROXY_THEATRE + ">")[1]
                    .split("</" + AmpYaRxV577.XML_THE_ROXY_THEATRE)[0];

            initAudioRoomAdvanced(mAmp.getConfigLiveClub().getTheRoxyTheatre(), roxy);
        }

        mAmp.getConfigLiveClub().dbgMsg();

        if (s.contains(Const.M_DIALOG_SHOW_DSP_CONFIG)) {
            new DialogCinemaDsp3d(this, Integer
                    .parseInt(s.split(Const.M_DIALOG_SHOW_DSP_CONFIG)[1].split("</")[0]));
        }
    }


    private void initConfigMovie(String s) {

        if (s.contains("<" + AmpYaRxV577.STANDARD + ">")) {
            String standard = s.split("<" + AmpYaRxV577.STANDARD + ">")[1].split("</" + AmpYaRxV577.STANDARD)[0];
            initStandard(mAmp.getConfigMovie().getStandard(), standard);
        }

        if (s.contains("<" + AmpYaRxV577.SPECTACLE + ">")) {
            String spectacle = s.split("<" + AmpYaRxV577.SPECTACLE + ">")[1].split("</" + AmpYaRxV577.SPECTACLE)[0];
            initSurroundEnitiy(mAmp.getConfigMovie().getSpectacle(), spectacle);
        }

        if (s.contains("<" + AmpYaRxV577.XML_SCIFI + ">")) {
            String scifi = s.split("<" + AmpYaRxV577.XML_SCIFI + ">")[1].split("</" + AmpYaRxV577.XML_SCIFI)[0];
            initSurroundEnitiy(mAmp.getConfigMovie().getSciFi(), scifi);
        }


        if (s.contains("<" + AmpYaRxV577.ADVENTURE + ">")) {
            String adventure = s.split("<" + AmpYaRxV577.ADVENTURE + ">")[1].split("</" + AmpYaRxV577.ADVENTURE)[0];
            initSurroundEnitiy(mAmp.getConfigMovie().getAdventure(), adventure);
        }

        if (s.contains("<" + AmpYaRxV577.DRAMA + ">")) {
            String drama = s.split("<" + AmpYaRxV577.DRAMA + ">")[1].split("</" + AmpYaRxV577.DRAMA)[0];
            initSurroundEnitiy(mAmp.getConfigMovie().getDrama(), drama);
        }

        if (s.contains("<" + AmpYaRxV577.XML_MONO_MOVIE + ">")) {
            String monomovie = s.split("<" + AmpYaRxV577.XML_MONO_MOVIE + ">")[1].split("</" + AmpYaRxV577.XML_MONO_MOVIE)[0];
            initAudioRoomAdvanced(mAmp.getConfigMovie().getMonoMovie(), monomovie);
        }

        mAmp.getConfigMovie().dbgMsg();

        if (s.contains(Const.M_DIALOG_SHOW_DSP_CONFIG)) {
            new DialogCinemaDsp3d(this, Integer.parseInt(s.split(Const.M_DIALOG_SHOW_DSP_CONFIG)[1].split("</")[0]));
        }
    }


    private void initStandard(Standard standard, String arg) {
        try {
            standard.setDspLvl(Integer.parseInt(arg.split("<DSP_Lvl><Val>")[1].split("</")[0]));
            standard.setSurInitDly(Integer.parseInt(arg.split("<Sur_Init_Dly><Val>")[1].split("</")[0]));
            standard.setSurRoomSize(Integer.parseInt(arg.split("<Sur_Room_Size><Val>")[1].split("</")[0]));
            standard.setSurLiveness(Integer.parseInt(arg.split("<Sur_Liveness><Val>")[1].split("</")[0]));
            standard.setSurBackInitDly(Integer.parseInt(arg.split("<Sur_Back_Init_Dly><Val>")[1].split("</")[0]));
            standard.setSurBackRoomSize(Integer.parseInt(arg.split("<Sur_Back_Room_Size><Val>")[1].split("</")[0]));
            standard.setSurBackLiveness(Integer.parseInt(arg.split("<Sur_Back_Liveness><Val>")[1].split("</")[0]));
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.e(TAG, "initEntertainment ArrayIndexOutOfBoundsException " + ex.getMessage());
        }
    }


    private void initAudioAdvanced(AudioEntityAdvanced aea, String arg) {
        try {
            aea.setDspLvl(Integer.parseInt(arg.split("<DSP_Lvl><Val>")[1].split("</")[0]));
            aea.setInitDly(Integer.parseInt(arg.split("<Init_Dly><Val>")[1].split("</")[0]));
            aea.setLiveness(Integer.parseInt(arg.split("<Liveness><Val>")[1].split("</")[0]));
            aea.setRevTime(Integer.parseInt(arg.split("<Rev_Time><Val>")[1].split("</")[0]));
            aea.setRevDly(Integer.parseInt(arg.split("<Rev_Dly><Val>")[1].split("</")[0]));
            aea.setRevLvl(Integer.parseInt(arg.split("<Rev_Lvl><Val>")[1].split("</")[0]));
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.e(TAG, "initEntertainment ArrayIndexOutOfBoundsException " + ex.getMessage());
        }
    }


    private void initSurroundEnitiy(SurroundEntity se, String arg) {
        try {
            se.setDspLvl(Integer.parseInt(arg.split("<DSP_Lvl><Val>")[1].split("</")[0]));
            se.setInitDly(Integer.parseInt(arg.split("<Init_Dly><Val>")[1].split("</")[0]));
            se.setRoomSize(Integer.parseInt(arg.split("<Room_Size><Val>")[1].split("</")[0]));
            se.setSurInitDly(Integer.parseInt(arg.split("<Sur_Init_Dly><Val>")[1].split("</")[0]));
            se.setSurRoomSize(Integer.parseInt(arg.split("<Sur_Room_Size><Val>")[1].split("</")[0]));
            se.setSurBackInitDly(Integer.parseInt(arg.split("<Sur_Back_Init_Dly><Val>")[1].split("</")[0]));
            se.setSurBackRoomSize(Integer.parseInt(arg.split("<Sur_Back_Room_Size><Val>")[1].split("</")[0]));
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.e(TAG, "initEntertainment ArrayIndexOutOfBoundsException " + ex.getMessage());
        }
    }


    private void initAudioRoom(AudioRoom ar, String arg) {
        try {
            ar.setDspLvl(Integer.parseInt(arg.split("<DSP_Lvl><Val>")[1].split("</")[0]));
            ar.setInitDly(Integer.parseInt(arg.split("<Init_Dly><Val>")[1].split("</")[0]));
            ar.setRoomSize(Integer.parseInt(arg.split("<Room_Size><Val>")[1].split("</")[0]));
            ar.setLiveness(Integer.parseInt(arg.split("<Liveness><Val>")[1].split("</")[0]));
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.e(TAG, "initEntertainment ArrayIndexOutOfBoundsException " + ex.getMessage());
        }
    }


    private void initAudioRoomAdvanced(AudioRoomAdvanced ar, String arg) {
        try {
            ar.setDspLvl(Integer.parseInt(arg.split("<DSP_Lvl><Val>")[1].split("</")[0]));
            ar.setInitDly(Integer.parseInt(arg.split("<Init_Dly><Val>")[1].split("</")[0]));
            ar.setRoomSize(Integer.parseInt(arg.split("<Room_Size><Val>")[1].split("</")[0]));
            ar.setLiveness(Integer.parseInt(arg.split("<Liveness><Val>")[1].split("</")[0]));
            ar.setRevDly(Integer.parseInt(arg.split("<Rev_Dly><Val>")[1].split("</")[0]));
            ar.setRevTime(Integer.parseInt(arg.split("<Rev_Time><Val>")[1].split("</")[0]));
            ar.setRevLvl(Integer.parseInt(arg.split("<Rev_Lvl><Val>")[1].split("</")[0]));
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.e(TAG, "initEntertainment ArrayIndexOutOfBoundsException " + ex.getMessage());
        }
    }


    public void updateSwitches() {
        if (mFragment instanceof AudioFragment) {
            AudioFragment af = (AudioFragment) mFragment;
            af.setMain(MainActivity.this);
            af.updateSwitches(
                    mAmp.isEnhancer(),
                    mAmp.isSpeakerA(),
                    mAmp.isSpeakerB());
        }

        if (mFragment instanceof DspFragment) {
            DspFragment dspf = (DspFragment) mFragment;
            dspf.setMain(MainActivity.this);
            dspf.updateSwitches(
                    mAmp.isDirect(),
                    mAmp.isStraight(),
                    mAmp.isExtraBass(),
                    mAmp.isAdaptiveDrc(),
                    mAmp.is_3dCinemaDrc());
        }
    }


    public void updateVolumeSlider(float volume) {
        if (mFragment instanceof AudioFragment) {
            AudioFragment af = (AudioFragment) mFragment;
            af.setMain(MainActivity.this);
            af.setVolumeSlider(volume);
        }
    }


    private void initNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navDrawerLayout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                if (!AesPrefs.get("custom_icon_path", "").isEmpty()) {
                    loadIconFromStorage();
                }
            }


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                ImageView iv = (ImageView) findViewById(R.id.iv_logo);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media
                                .EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");

                        Intent chooserIntent = Intent.createChooser(MainActivity.this.getIntent(), getString(R.string
                                .select_icon));
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                        startActivityForResult(chooserIntent, Const.REQ_CODE_SELECT_PICTURE);
                    }
                });

            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }


    private void initNavView() {
        mNavView = (NavigationView) findViewById(R.id.navigation_view);

        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(menuItem.isChecked());
                mDrawerLayout.closeDrawers();

                // return if the selected fragment is shown
                return selectNavViewItem(menuItem);

            }
        });
    }


    public boolean selectNavViewItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.audio:
                mLastSelection = getString(R.string.audio);
                if (mFragment instanceof AudioFragment) return true;
                makeFragmentTransaction(AudioFragment.newInstance(0));
                mLastSelectedNavItemPos = 0;
                return true;

            case R.id.dsp:
                mLastSelection = getString(R.string.dsp);
                if (mFragment instanceof DspFragment) return true;
                makeFragmentTransaction(DspFragment.newInstance(1));
                mLastSelectedNavItemPos = 1;
                return true;

            case R.id.input:
                mLastSelection = getString(R.string.input);
                if (mFragment instanceof InputFragment) return true;
                makeFragmentTransaction(InputFragment.newInstance(2));
                mLastSelectedNavItemPos = 2;
                return true;

            case R.id.webradio:
                mLastSelection = getString(R.string.webradio);
                if (mFragment instanceof WebradioFragment) return true;
                makeFragmentTransaction(WebradioFragment.newInstance(3));
                mLastSelectedNavItemPos = 3;
                return true;

            case R.id.spotify:
                mLastSelection = getString(R.string.spotify);
                mStartedSpotify = true;
                mLastSelectedNavItemPos = 0;
                setTitle(getString(R.string.spotify));
                runCtrlrTask(false, Commands.SELECT_INPUT(AmpYaRxV577.XML_INPUT_SPOTIFY));
                Utils.launchSpotify(MainActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runCtrlrTask(false, Commands.PLAYBACK_CONTROL(AmpYaRxV577.PC_PLAY));
                    }
                }, 300);
                return true;

            case R.id.settings:
                mLastSelection = getString(R.string.settings);
                if (mFragment instanceof SettingsFragment) return true;
                makeFragmentTransaction(SettingsFragment.newInstance(5));
                mLastSelectedNavItemPos = 5;
                return true;

            case R.id.help_and_feedback:
                new DialogHelpAndFeedback(MainActivity.this);
                return true;

        }
        return false;
    }


    private void makeFragmentTransaction(Fragment fragment) {
        mFragment = fragment;
        android.support.v4.app.FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (mFragment instanceof SettingsFragment) {
            fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
        }

        fragmentTransaction.replace(R.id.main_frame, mFragment);
        fragmentTransaction.commit();
    }


    private void initFab() {
        FrameLayout interceptorFrame = (FrameLayout) findViewById(R.id.fl_interceptor);
        interceptorFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch  " + "");
                if (mFabMenu.isOpened()) {
                    mFabMenu.close(true);
                    return true;
                }
                return false;
            }
        });

        mFabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        mFabMenu.setMenuButtonColorNormal(getResources().getColor
                (Setup.getTheme() != 0 ? R.color.fabMenuBgColor_light
                        : R.color.fabMenuBgColor));

        mFabPower = (FloatingActionButton) findViewById(R.id.fab_action_power);
        mFabMute = (FloatingActionButton) findViewById(R.id.fab_action_mute);
        mFabSleeptimer = (FloatingActionButton) findViewById(R.id.fab_action_sleeptimer);
        mFabDeviceInfo = (FloatingActionButton) findViewById(R.id.fab_action_device_info);

        final FadeAnimation fadeAnitmation =
                new FadeAnimation(mMainFrame, 1.0f, 0.1f, 400L, 0);

        mFabMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    reinitFab();
                    fadeAnitmation.fadeOut();
                } else {
                    reinitFab();
                    fadeAnitmation.fadeIn();
                }
            }
        });

        mFabMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ControllerTask(false).execute
                        (Commands.TOGGLE_MUTE
                                        (mAmp.isMute() ? AmpYaRxV577.OFF
                                                : AmpYaRxV577.ON),
                                Const.M_MUTE,
                                mAmp.isMute() ? "false"
                                        : "true");

                setTemporaryTitleUpdate(mAmp.isMute() ? getString(R.string.toolbar_info_unmute)
                        : getString(R.string.toolbar_info_mute));

                mFabMenu.close(true);

                mTitleWriterHandler.removeCallbacks(mTitleWriterRunnable);
                mTitleWriterHandler.postDelayed(mTitleWriterRunnable, Const.DELAY_TITLE_RESET);

            }
        });

        mFabPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAmp.isOn()) closeAppDelayed();
                else mFabMenu.close(true);

                new ControllerTask(false).execute
                        (Commands.SET_POWER(mAmp.isOn() ? AmpYaRxV577.STANDBY : AmpYaRxV577.ON),
                                mAmp.isOn() ? Const.M_POWER_OFF
                                        : Const.M_POWER_ON);
            }


            private void closeAppDelayed() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.finish();
                    }
                }, 400);
            }
        });


        mFabSleeptimer.setImageDrawable(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_timer)
                .colorRes(Setup.getFabIconColor()).sizeDp(Const.FAB_ICON_SIZE));

        mFabSleeptimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogSleeptimer(MainActivity.this);
                closeFabMenuDelayed();
            }
        });


        mFabDeviceInfo.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_info_outline)
                .colorRes(Setup.getFabIconColor()).sizeDp(Const.FAB_ICON_SIZE));

        mFabDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogDeviceInfo(MainActivity.this, mAmp.dbgMsg());
                closeFabMenuDelayed();
            }
        });

        reinitFab();
    }


    private void closeFabMenuDelayed() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFabMenu.close(true);
            }
        }, 400);
    }


    private void reinitFab() {
        mFabPower.setImageDrawable(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_power)
                .colorRes(Setup.getFabIconColor()).sizeDp(Const.FAB_ICON_SIZE));

        mFabPower.setColorNormal(getResources().getColor(mAmp.isOn() ? R.color.fabPowerRed
                : R.color.fabPowerGreen));

        mFabPower.setLabelText(mAmp.isOn() ? getString(R.string.fab_action_power_off)
                : getString(R.string.fab_action_power_on));


        mFabMute.setImageDrawable(new IconicsDrawable(this, mAmp.isMute() ? GoogleMaterial.Icon.gmd_volume_up
                : GoogleMaterial.Icon.gmd_volume_off)
                .colorRes(Setup.getFabIconColor()).sizeDp(Const.FAB_ICON_SIZE));

        mFabMute.setLabelText(mAmp.isMute() ? getString(R.string.fab_action_unmute)
                : getString(R.string.fab_action_mute));

        int normalFabBgColor = getResources().getColor
                (Setup.getTheme() != 0 ? R.color.fabBgColor_light
                        : R.color.fabBgColor);

        mFabMute.setColorNormal(normalFabBgColor);
        mFabDeviceInfo.setColorNormal(normalFabBgColor);
        mFabSleeptimer.setColorNormal(normalFabBgColor);
    }


    private InputStream checkConnection(HttpURLConnection con) throws IOException {
        InputStream is = null;
        if (con.getResponseCode() < 300) is = con.getInputStream();
        else if (con.getResponseCode() < 400) is = con.getInputStream();
        else if (con.getResponseCode() >= 400) is = con.getErrorStream();
        return is;
    }


    private StringBuilder collectData(HttpURLConnection con, InputStream is) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) response.append(line);

        rd.close();
        con.disconnect();
        return response;
    }


    private HttpURLConnection initHttpUrlConnection() throws IOException {
        URL url = new URL("http://" + mAmp.getIp() + AmpYaRxV577.PORT + AmpYaRxV577.AV_SPECS);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Accept", "application/xml");
        con.setDoOutput(true);
        return con;
    }


    private void stopDriver() {
        mHandler.removeCallbacks(mDriverRunnable);
        mTitleWriterHandler.removeCallbacks(mTitleWriterRunnable);
        if (mDriverTimer != null) mDriverTimer.cancel();
    }


    private void dismissProgressDialog() {
        if (mProgressDialog != null
                && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }


    public void setTitle(String title) {
        if (mToolbar == null) initToolbar();

        if (title.contains("android.support")) {
            title = mLastSelection;
        }

        mToolbar.setTitle(title);
        mLastSelection = title;
    }


    public void setTemporaryTitleUpdate(String title) {
        mToolbar.setTitle(title);
    }


    public void setNavViewTitle(String title) {
        if (mTvNavViewTitle != null) {
            mTvNavViewTitle.setText(title);
        } else if (mNavView != null) {
            mTvNavViewTitle = (TextView) mNavView.findViewById(R.id.nav_view_header_title);
        }
    }


    public void setNavViewSubtitle(String title) {
        if (mTvNavViewSubtitle != null) {
            mTvNavViewSubtitle.setText(title);
        } else if (mNavView != null) {
            mTvNavViewSubtitle = (TextView) mNavView.findViewById(R.id.nav_view_header_subtitle);
        }
    }


    public String getLastSelection() {
        return mLastSelection;
    }


    public void resetLastExecutionTimestamp() {
        mLastExecution = 0;
    }


    public static AmpYaRxV577 getAmp() {
        return mAmp;
    }


    public static SeekBar getInvisibleVolSeekBar() {
        return mInvisibleVolSlider;
    }


    public static String getVolumeMessage(Context ctx, float vol) {
        return ctx.getString(R.string.vol) + " " + vol + " " + ctx.getString(R.string.dB);
    }


    public String getNavDrawerDetail() {
        if (Setup.getNavDrawerDetail(this).equals(getString(R.string.input))) {
            return getAmp().getActiveInput();
        }
        if (Setup.getNavDrawerDetail(this).equals(getString(R.string.volume))) {
            return getString(R.string.vol) + " " + String.valueOf(getAmp().getVolume());
        }
        return getAmp().getIp();
    }


    public NavigationView getNavView() {
        return mNavView;
    }


    public Fragment getActiveFragment() {
        return mFragment;
    }


    private void loadIabHelper() {
        mHelper = new IabHelper(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxr6JxgVWhboWdXpOP+2ocNPGtjW7HweEyy" +
                "/z2w4lBRrJVR2HZ6ra8zP/ZSNodIZN128+xTmXeEXHsdY8JFCTsuNmzHTDHXbSm+n1+Eu8YP377BEPAZZd26Lu6kGp6KhwTt2OS8w" +
                "+zrApRIlIQm8yLRvdmNCdWwYYjixgq9472tOWghM40aT5gSMqt568PImZsRRsX8jt4BDG+bm7aDLmt0A4OrLIYqC5i8IoE1" +
                "/4oJCF0TKyvd2RQECIDFbaQvHgpZafhrpawISqoSvw2o2BHWqgzsrGRDJklYXwVOP1KocIgop+ofoWl0Vo" +
                "/DYW66CM7Jh1RPRz2XZzH8lcWyiHmQIDAQAB");

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {

            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "onIabSetupFinished: Setup fertig.");

                if (!result.isSuccess()) {
                    Log.e(TAG, "onIabSetupFinished fehlgeschlagen.");
                    return;
                }

                // wenn Verbindung als disponiert gekennzeichnet -> beenden
                if (mHelper == null) return;

                // IAB erfolgreich erstellt -> Produkte suchen und anzeigen
                Log.d(TAG, "onIabSetupFinished: Einrichten von IAB erfolgreich. Beziehe Produkte...");

                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }


    /**
     * Listener wird aufgerufen, wenn Produktsuche erfolgt ist.
     * Ebenso werden hier bereits erfasste Käufe festgehalten.
     */
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, " Produktsuche abgeschlossen.");

            // wenn Verbindung als disponiert gekennzeichnet -> beenden
            if (mHelper == null) return;

            // gab es einen Fehler?
            if (result.isFailure()) {
                Log.e(TAG, "onQueryInventoryFinished fehlgeschlagen: " + result);
                return;
            }

            Log.d(TAG, "Produktsuche erfolgreich abgeschlossen.");

            // haben wir das Premium-Upgrade? (einmaliger Kauf)
            Purchase premiumPurchase = inventory.getPurchase("premium");
            if (premiumPurchase != null
                    && premiumPurchase.getDeveloperPayload().equals("bGoa+V7g/yqDXvKRqq+JThzv+zcvlzTlz546tvc§zHGztfrr/khu")) {
                Setup.setPremium(true);
            }
            // Käufe ermitteln
            IInAppBillingService appBillingService = mHelper.getmService();
            Bundle ownedItems = null;
            try {
                ownedItems = appBillingService.getPurchases(3, getApplicationContext().getPackageName(), "inapp", null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            assert ownedItems != null;
            Iterable<String> ownedSkus = ownedItems.getStringArrayList(IabHelper.RESPONSE_INAPP_ITEM_LIST);
            for (String inapp : ownedSkus) {
                if (inapp.equals("premium")) {
                    Setup.setPremium(true);
                }

                Log.d(TAG, "Gekauft: " + inapp);
            }

            Log.d(TAG, "Die Produktsuche wurde abgeschlossen.");

        }
    };

}