/*
 * Copyright (c) 2019 Martin Pfeffer
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

package com.pepperonas.yahama.app.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import com.android.vending.billing.IInAppBillingService;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pepperonas.aesprefs.AesPrefs;
import com.pepperonas.andbasx.system.UsabilityUtils;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.dialogs.DialogGetPremium;
import com.pepperonas.yahama.app.dialogs.DialogPromotion;
import com.pepperonas.yahama.app.dialogs.DialogSelectNavDrawerDetail;
import com.pepperonas.yahama.app.dialogs.DialogSelectTheme;
import com.pepperonas.yahama.app.dialogs.DialogSelectVolumeSteps;
import com.pepperonas.yahama.app.utility.Setup;
import com.pepperonas.yahama.app.utils.Utils;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class SettingsFragment extends com.github.machinarius.preferencefragment.PreferenceFragment
        implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    @SuppressWarnings("unused")
    private static final String TAG = "SettingsFragment";

    private MainActivity mMain;

    private static final int ICON_SIZE = 24;
    private int mIconColor;

    //    private Tracker mTracker;

    private IInAppBillingService mService;

    private long mLastBuildVersionClicked;
    private long mHiddenCounter;

    SharedPreferences.OnSharedPreferenceChangeListener mChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(AesPrefs.getEncryptedKey("theme"))) {
                if (mMain != null && mMain.getIntent() != null) {
                    Intent relaunch = mMain.getIntent();
                    mMain.finish();
                    relaunch.putExtra("theme_changed", true);
                    mMain.startActivity(relaunch);
                    mMain.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        }
    };

    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    public static SettingsFragment newInstance(int i) {
        SettingsFragment fragment = new SettingsFragment();

        Bundle args = new Bundle();
        args.putInt("the_id", i);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.fragment_preference);

        int highlightColor = Setup.getTheme() == 0 ? R.color.colorAccent : R.color.colorAccent_light;

        mIconColor = Setup.getTheme() == 0 ? R.color.settings_icons_light : R.color.settings_icons_dark;

        if (Setup.getShowPremium()) {
            Preference getPremium = findPreference(getString(R.string.PR_KEY_GET_PREMIUM));
            getPremium.setOnPreferenceClickListener(this);
            if (getContext() != null) {
                getPremium.setIcon(new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_star).colorRes(highlightColor).sizeDp(ICON_SIZE));
            }
        } else {
            hidePremiumCategory();
        }

        Preference p = findPreference(getString(R.string.PR_KEY_VOL_STEPS));
        p.setOnPreferenceClickListener(this);
        p.setIcon(new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_vector_point).colorRes(mIconColor).sizeDp(ICON_SIZE));

        p = findPreference(getString(R.string.PR_KEY_THEME));
        p.setOnPreferenceClickListener(this);
        p.setIcon(new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_brush).colorRes(mIconColor).sizeDp(ICON_SIZE));

        p = findPreference(getString(R.string.PR_KEY_DETAIL_IN_NAV_DRAWER));
        p.setOnPreferenceClickListener(this);
        p.setIcon(new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_lightbulb_outline).colorRes(mIconColor).sizeDp(ICON_SIZE));

        p = findPreference(getString(R.string.RATE_APP));
        p.setOnPreferenceClickListener(this);
        p.setIcon(new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_star).colorRes(mIconColor).sizeDp(ICON_SIZE));

        p = findPreference(getString(R.string.SHARE_APP));
        p.setOnPreferenceClickListener(this);
        p.setIcon(new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_tag_faces).colorRes(mIconColor).sizeDp(ICON_SIZE));

        p = findPreference(getString(R.string.PR_KEY_BUILD_VERSION));
        p.setOnPreferenceClickListener(this);
        p.setIcon(new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_leaf).colorRes(mIconColor).sizeDp(ICON_SIZE));

        CheckBoxPreference cbxp = (CheckBoxPreference) findPreference(getString(R.string.PR_KEY_CLOSE_VOL_DIALOG_AUTOMATICALLY));
        cbxp.setOnPreferenceChangeListener(this);
        cbxp.setIcon(new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_message_text_outline).colorRes(mIconColor).sizeDp(ICON_SIZE));

        initInAppBillings();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMain = (MainActivity) getActivity();
        mMain.setTitle(getString(R.string.settings));

        //        initAnalytics();

        updateSummaries();
        AesPrefs.registerOnSharedPreferenceChangeListener(mChangeListener);
    }

    @Override
    public void onDestroy() {
        if (mService != null && getActivity() != null) {
            getActivity().unbindService(mServiceConn);
        }

        //        doAnalyticsOnLifecycle();

        AesPrefs.unregisterOnSharedPreferenceChangeListener(mChangeListener);

        super.onDestroy();
    }

    @Override
    public boolean onPreferenceClick(Preference p) {
        if (p.getKey().equals(getString(R.string.PR_KEY_GET_PREMIUM))) {
            if (getContext() != null) {
                if (Setup.getPremium() || getContext().getResources().getBoolean(R.bool.app_unlocked)) {
                    hidePremiumCategory();
                    Setup.setShowPremium(false);
                } else {
                    new DialogGetPremium(mMain, SettingsFragment.this, mMain);
                }
            }
        } else if (p.getKey().equals(getString(R.string.PR_KEY_VOL_STEPS))) {
            new DialogSelectVolumeSteps(mMain, this);
        } else if (p.getKey().equals(getString(R.string.PR_KEY_THEME))) {
            new DialogSelectTheme(mMain);
        } else if (p.getKey().equals(getString(R.string.PR_KEY_DETAIL_IN_NAV_DRAWER))) {
            new DialogSelectNavDrawerDetail(mMain, this);
        } else if (p.getKey().equals(getString(R.string.PR_KEY_BUILD_VERSION))) {
            checkClickTimer();
        } else if (p.getKey().equals(getString(R.string.RATE_APP))) {
            onRate();
        } else if (p.getKey().equals(getString(R.string.SHARE_APP))) {
            onShare();
        }

        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(getString(R.string.PR_KEY_CLOSE_VOL_DIALOG_AUTOMATICALLY))) {
            Setup.setCloseVolumeDialogAutomatically((Boolean) newValue);
        }

        return true;
    }

    public void updateSummaries() {

        if (Setup.getPremium() && Setup.getShowPremium()) {
            Preference getPremium = findPreference(getString(R.string.PR_KEY_GET_PREMIUM));
            if (getContext() != null) {
                getPremium.setIcon(new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_checkbox_marked_outline)
                        .colorRes(mIconColor).sizeDp(ICON_SIZE));
            }

            getPremium.setTitle(getString(R.string.premium_unlocked_title));
            getPremium.setSummary(getString(R.string.premium_unlocked_summary));
        }

        Preference p = findPreference(getString(R.string.PR_KEY_VOL_STEPS));
        int volStepPos = Setup.getVolumeStepsPos();
        String[] volStepsItems = getResources().getStringArray(R.array.dialog_items_select_vol_steps);
        p.setSummary(getString(R.string.plus_minus) + "" + volStepsItems[volStepPos]);

        p = findPreference(getString(R.string.PR_KEY_THEME));
        int themePos = Setup.getTheme();
        p.setSummary(themePos == 0 ? getString(R.string.dark) : getString(R.string.light));

        p = findPreference(getString(R.string.PR_KEY_DETAIL_IN_NAV_DRAWER));
        String detailInfo = Setup.getNavDrawerDetail(mMain);
        p.setSummary(detailInfo);

        p = findPreference(getString(R.string.PR_KEY_BUILD_VERSION));
        String bv = Utils.getAppVersionName(mMain);
        String additionalInfo = (Setup.getPremium() || getResources().getBoolean(R.bool.app_unlocked)) ? ("-" + getString(R.string.pro)) : "";
        p.setSummary(bv + additionalInfo);

        CheckBoxPreference cbxp = (CheckBoxPreference) findPreference(getString(R.string.PR_KEY_CLOSE_VOL_DIALOG_AUTOMATICALLY));
        cbxp.setChecked(Setup.getCloseVolumeDialogAutomatically());
    }

    private void initInAppBillings() {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        if (getActivity() != null) {
            getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        }
    }

    private void checkClickTimer() {

        if ((mLastBuildVersionClicked + 550) > System.currentTimeMillis() || mLastBuildVersionClicked == 0) {
            mHiddenCounter++;
        } else {
            mHiddenCounter = 0;
        }

        if (mHiddenCounter == 6) {
            new DialogPromotion(this, getActivity());
        }

        mHiddenCounter = mHiddenCounter > 6 ? 0 : mHiddenCounter;

        mLastBuildVersionClicked = System.currentTimeMillis();
    }

    public IInAppBillingService getIabService() {
        return mService;
    }

    private void hidePremiumCategory() {
        PreferenceScreen screen = (PreferenceScreen) findPreference(getString(R.string.PR_SCREEN_KEY));
        PreferenceCategory category = (PreferenceCategory) findPreference(getString(R.string.PR_CAT_KEY_GET_PREMIUM));
        screen.removePreference(category);
    }

    /**
     * On rate.
     */
    private void onRate() {
        if (getActivity() != null) {
            UsabilityUtils.launchAppStore(getActivity(), "com.pepperonas.yahama.app");
            //            doAnalyticsOnAction("onRate");
        }
    }

    /**
     * On share.
     */
    private void onShare() {
        if (getActivity() != null) {
            UsabilityUtils.launchShareAppIntent(getActivity(), "com.pepperonas.yahama.app", getString(R.string.share_app_intro_text));
            //            doAnalyticsOnAction("onShare");
        }
    }

    /**
     * Init analytics.
     */
    //    private void initAnalytics() {
    //        if (getActivity() != null) {
    //            YaAmpApp application = (YaAmpApp) getActivity().getApplication();
    //            mTracker = application.getDefaultTracker();
    //            if (mTracker != null) {
    //                mTracker.setScreenName("FragmentSettings");
    //                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    //            }
    //        }
    //    }

    //    /**
    //     * Do analytics on action.
    //     *
    //     * @param action the action
    //     */
    //    private void doAnalyticsOnAction(String action) {
    //        mTracker.send(new HitBuilders.EventBuilder()
    //                .setCategory("Action")
    //                .setCustomDimension(Analyst.ANDROID_ID, SystemUtils.getAndroidId())
    //                .setAction(action)
    //                .build());
    //    }
    //
    //    /**
    //     * Do analytics on lifecycle.
    //     */
    //    private void doAnalyticsOnLifecycle() {
    //        mTracker.send(new HitBuilders.EventBuilder()
    //                .setCategory("Lifecycle")
    //                .setLabel("onDestroy")
    //                .build());
    //    }
}