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
        implements Preference.OnPreferenceClickListener {

    private static final String TAG = "SettingsFragment";

    private MainActivity mMain;
    private int iconSize;
    private int iconColor;

    private IInAppBillingService mService;

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
    private long mLastBuildVersionClicked;
    private long mHiddenCounter;


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

        int highlightColor = Setup.getTheme()
                             == 0 ? R.color.colorAccent
                                  : R.color.colorAccent_light;
        iconColor = Setup.getTheme()
                    == 0 ? R.color.primary_text_default_material_dark
                         : R.color.primary_text_default_material_light;
        iconSize = 24;

        if (Setup.getShowPremium()) {
            Preference getPremium = findPreference(getString(R.string.PR_KEY_GET_PREMIUM));
            getPremium.setOnPreferenceClickListener(this);
            getPremium.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_star, highlightColor, iconSize));
        } else hidePremiumCategory();

        Preference volSteps = findPreference(getString(R.string.PR_KEY_VOL_STEPS));
        volSteps.setOnPreferenceClickListener(this);
        volSteps.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_vector_point, iconColor, iconSize));

        Preference theme = findPreference(getString(R.string.PR_KEY_THEME));
        theme.setOnPreferenceClickListener(this);
        theme.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_brush, iconColor, iconSize));

        Preference detailInNavDrawer = findPreference(getString(R.string.PR_KEY_DETAIL_IN_NAV_DRAWER));
        detailInNavDrawer.setOnPreferenceClickListener(this);
        detailInNavDrawer.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_playlist_minus, iconColor, iconSize));

        Preference buildVersion = findPreference(getString(R.string.PR_KEY_BUILD_VERSION));
        buildVersion.setOnPreferenceClickListener(this);
        buildVersion.setIcon(Utils.getIconic(getActivity(), CommunityMaterial.Icon.cmd_leaf, iconColor, iconSize));

        initInAppBillings();

    }


    private void initInAppBillings() {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMain = (MainActivity) getActivity();
        mMain.setTitle(getString(R.string.settings));

        updateSummaries();

        AesPrefs.registerOnSharedPreferenceChangeListener(
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        if (key.equals(AesPrefs.getEncryptedKey("theme"))) {
                            mMain.recreate();
                        }
                    }
                });
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(getString(R.string.PR_KEY_GET_PREMIUM))) {
            if (Setup.getPremium()) {
                hidePremiumCategory();
                Setup.setShowPremium(false);
            } else new DialogGetPremium(mMain, SettingsFragment.this, mMain);
        } else if (preference.getKey().equals(getString(R.string.PR_KEY_VOL_STEPS))) {
            new DialogSelectVolumeSteps(mMain, this);
        } else if (preference.getKey().equals(getString(R.string.PR_KEY_THEME))) {
            new DialogSelectTheme(mMain, this);
        } else if (preference.getKey().equals(getString(R.string.PR_KEY_DETAIL_IN_NAV_DRAWER))) {
            new DialogSelectNavDrawerDetail(mMain, this);
        } else if (preference.getKey().equals(getString(R.string.PR_KEY_BUILD_VERSION))) {
            checkClickTimer();
        }
        return true;
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


    private void hidePremiumCategory() {
        PreferenceScreen screen = (PreferenceScreen) findPreference(getString(R.string.PR_SCREEN_KEY));
        PreferenceCategory premiumCategory = (PreferenceCategory) findPreference(getString(R.string.PR_CAT_KEY_GET_PREMIUM));
        screen.removePreference(premiumCategory);
    }


    public void updateSummaries() {

        if (Setup.getPremium() && Setup.getShowPremium()) {
            Preference getPremium = findPreference(getString(R.string.PR_KEY_GET_PREMIUM));
            getPremium.setIcon(
                    Utils.getIconic(mMain, CommunityMaterial.Icon.cmd_checkbox_marked_outline, iconColor, iconSize));

            getPremium.setTitle(getString(R.string.premium_unlocked_title));
            getPremium.setSummary(getString(R.string.premium_unlocked_summary));
        }

        Preference volSteps = findPreference(getString(R.string.PR_KEY_VOL_STEPS));
        int volStepPos = Setup.getVolumeStepsPos();
        String[] volStepsItems = getResources().getStringArray(R.array.dialog_items_select_vol_steps);
        volSteps.setSummary(getString(R.string.plus_minus) + "" + volStepsItems[volStepPos]);

        Preference theme = findPreference(getString(R.string.PR_KEY_THEME));
        int themePos = Setup.getTheme();
        theme.setSummary(themePos == 0 ? getString(R.string.dark) : getString(R.string.light));

        Preference detailInNavDrawer = findPreference(getString(R.string.PR_KEY_DETAIL_IN_NAV_DRAWER));
        String detailInfo = Setup.getNavDrawerDetail(mMain);
        detailInNavDrawer.setSummary(detailInfo);

        Preference buildVersion = findPreference(getString(R.string.PR_KEY_BUILD_VERSION));
        String bv = Utils.getAppVersionName(mMain);
        buildVersion.setSummary(bv);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            getActivity().unbindService(mServiceConn);
        }
    }


    public IInAppBillingService getIabService() {
        return mService;
    }

}