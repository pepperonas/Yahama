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
import com.pepperonas.aesprefs.AesPrefs;
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
public class SettingsFragment
        extends com.github.machinarius.preferencefragment.PreferenceFragment
        implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";

    private MainActivity mMain;

    private static final int ICON_SIZE = 24;
    private int mIconColor;

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

        int highlightColor = Setup.getTheme() == 0 ? R.color.colorAccent
                                                   : R.color.colorAccent_light;

        mIconColor = Setup.getTheme() == 0 ? R.color.primary_text_default_material_dark_failsafe
                                           : R.color.primary_text_default_material_light_failsafe;

        if (Setup.getShowPremium()) {
            Preference getPremium = findPreference(getString(R.string.PR_KEY_GET_PREMIUM));
            getPremium.setOnPreferenceClickListener(this);
            getPremium.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_star, highlightColor, ICON_SIZE));
        } else hidePremiumCategory();

        /**
         * Preferences
         * */
        Preference p = findPreference(getString(R.string.PR_KEY_VOL_STEPS));
        p.setOnPreferenceClickListener(this);
        p.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_vector_point, mIconColor, ICON_SIZE));

        p = findPreference(getString(R.string.PR_KEY_THEME));
        p.setOnPreferenceClickListener(this);
        p.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_brush, mIconColor, ICON_SIZE));

        p = findPreference(getString(R.string.PR_KEY_DETAIL_IN_NAV_DRAWER));
        p.setOnPreferenceClickListener(this);
        p.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_playlist_minus, mIconColor, ICON_SIZE));

        p = findPreference(getString(R.string.PR_KEY_BUILD_VERSION));
        p.setOnPreferenceClickListener(this);
        p.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_leaf, mIconColor, ICON_SIZE));

        /**
         * CheckBoxPreference
         * */
        CheckBoxPreference cbxp = (CheckBoxPreference) findPreference(getString(R.string.PR_KEY_CLOSE_VOL_DIALOG_AUTOMATICALLY));
        cbxp.setOnPreferenceChangeListener(this);
        cbxp.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_message_text_outline, mIconColor, ICON_SIZE));

        initInAppBillings();

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMain = (MainActivity) getActivity();
        mMain.setTitle(getString(R.string.settings));

        updateSummaries();
        AesPrefs.registerOnSharedPreferenceChangeListener(mChangeListener);
    }


    @Override
    public void onDestroy() {
        if (mService != null) {
            getActivity().unbindService(mServiceConn);
        }

        AesPrefs.unregisterOnSharedPreferenceChangeListener(mChangeListener);

        super.onDestroy();
    }


    @Override
    public boolean onPreferenceClick(Preference p) {
        if (p.getKey().equals(getString(R.string.PR_KEY_GET_PREMIUM))) {
            if (Setup.getPremium()) {
                hidePremiumCategory();
                Setup.setShowPremium(false);
            } else new DialogGetPremium(mMain, SettingsFragment.this, mMain);
        } else if (p.getKey().equals(getString(R.string.PR_KEY_VOL_STEPS))) {
            new DialogSelectVolumeSteps(mMain, this);
        } else if (p.getKey().equals(getString(R.string.PR_KEY_THEME))) {
            new DialogSelectTheme(mMain);
        } else if (p.getKey().equals(getString(R.string.PR_KEY_DETAIL_IN_NAV_DRAWER))) {
            new DialogSelectNavDrawerDetail(mMain, this);
        } else if (p.getKey().equals(getString(R.string.PR_KEY_BUILD_VERSION))) {
            checkClickTimer();
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
            getPremium.setIcon(Utils.getIconic(mMain, CommunityMaterial.Icon.cmd_checkbox_marked_outline, mIconColor, ICON_SIZE));

            getPremium.setTitle(getString(R.string.premium_unlocked_title));
            getPremium.setSummary(getString(R.string.premium_unlocked_summary));
        }

        /**
         * Preferences
         * */
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
        p.setSummary(bv);

        /**
         * CheckBoxPreference
         * */
        CheckBoxPreference cbxp = (CheckBoxPreference) findPreference(getString(R.string.PR_KEY_CLOSE_VOL_DIALOG_AUTOMATICALLY));
        cbxp.setChecked(Setup.getCloseVolumeDialogAutomatically());
    }


    private void initInAppBillings() {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }


    private void checkClickTimer() {

        if ((mLastBuildVersionClicked + 550) > System.currentTimeMillis() || mLastBuildVersionClicked == 0) {
            mHiddenCounter++;
        } else mHiddenCounter = 0;

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

}