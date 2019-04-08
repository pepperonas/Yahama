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

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.dialogs.DialogVolumeSlider;
import com.pepperonas.yahama.app.model.AmpYaRxV577;
import com.pepperonas.yahama.app.utility.Commands;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class AudioFragment
        extends Fragment
        implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "AudioFragment";

    private static final int ACTION_ICON_SIZE_MEDIUM = 32;

    private static final int ACTION_ICON_SIZE_LARGE = 40;

    private MainActivity mMain;

    private Handler mTitleWriterHandler = new Handler();

    private Runnable mTitleWriterRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMain != null && mMain.getLastSelection() != null) {
                mMain.setTitle(mMain.getLastSelection());
            }
        }
    };

    private SeekBar mInvisibleVolSlider;
    private DialogVolumeSlider mDialogVolumeSlider;

    public void setMain(MainActivity main) {
        this.mMain = main;
    }

    public static AudioFragment newInstance(int i) {
        AudioFragment fragment = new AudioFragment();

        Bundle args = new Bundle();
        args.putInt("the_id", i);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_audio, null, false);
        mMain = (MainActivity) getActivity();
        mMain.setTitle(getString(R.string.audio));
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");

        initLabelCards();

        drawIcons();

        initActionCards();

        initInvisibleVolumeSlider();

        mMain.runCtrlrTask(false, Commands.GET_INFO, Const.M_GET_INFO);

        ensureRemoveAds();
    }

    private void ensureRemoveAds() {
        if (getActivity() != null) {
            AdView adView = getActivity().findViewById(R.id.adView);
            if (getActivity().getResources().getBoolean(R.bool.build_without_ads)) {
                if (adView != null) {
                    adView.setVisibility(View.GONE);
                }
            }
            if (!Setup.getAdvertising()) {
                if (adView != null) {
                    adView.setVisibility(View.GONE);
                }
            } else if (adView != null) {
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
        }
    }

    private void initInvisibleVolumeSlider() {
        mInvisibleVolSlider = MainActivity.getInvisibleVolSeekBar();
        mInvisibleVolSlider.setMax((int) AmpYaRxV577.MAX_VOL_SLIDER);
        mInvisibleVolSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float vol = AmpYaRxV577.getVolume_dB(progress);
                String volumeMsg = MainActivity.getVolumeMessage(mMain, vol);

                mMain.setTemporaryTitleUpdate(volumeMsg);
                if (mDialogVolumeSlider != null) mDialogVolumeSlider.showSeekBarValue(volumeMsg);

                mMain.runCtrlrTask(
                        false,
                        Commands.SET_VOL((int) (vol * 10)),
                        Const.M_VOLUME_SET + String.valueOf((int) (vol * 10)));

                mTitleWriterHandler.removeCallbacks(mTitleWriterRunnable);
                mTitleWriterHandler.postDelayed(mTitleWriterRunnable, Const.DELAY_TITLE_RESET);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mMain.resetLastExecutionTimestamp();
                mTitleWriterHandler.removeCallbacks(mTitleWriterRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mTitleWriterHandler.postDelayed(mTitleWriterRunnable, Const.DELAY_TITLE_RESET);
            }
        });
    }

    private void initActionCards() {
        CardView cvPlayerStop = mMain.findViewById(R.id.cv_player_stop);
        CardView cvPlayerPause = mMain.findViewById(R.id.cv_player_pause);
        CardView cvPlayerPlay = mMain.findViewById(R.id.cv_player_play);

        CardView cvVolumeDown = mMain.findViewById(R.id.cv_volume_down);
        CardView cvVolumeUp = mMain.findViewById(R.id.cv_volume_up);
        CardView cvSlider = mMain.findViewById(R.id.cv_slider);

        CardView cvBassDown = mMain.findViewById(R.id.cv_bass_down);
        CardView cvBassUp = mMain.findViewById(R.id.cv_bass_up);

        CardView cvTrebleDown = mMain.findViewById(R.id.cv_treble_down);
        CardView cvTrebleUp = mMain.findViewById(R.id.cv_treble_up);

        setCardLevel
                (Setup.getTheme() == 0 ? Setup.COLOR_ACCENT
                                : Setup.COLOR_ACCENT_LIGHT,
                        cvPlayerStop, cvPlayerPause, cvVolumeDown, cvSlider, cvBassDown, cvTrebleDown);

        setCardLevel
                (Setup.getTheme() == 0 ? Setup.COLOR_ACCENT
                                : Setup.COLOR_ACCENT_LIGHT,
                        cvPlayerPlay, cvVolumeUp, cvSlider, cvBassUp, cvTrebleUp);

        setCardClickListener
                (cvPlayerStop, cvPlayerPause, cvPlayerPlay,
                        cvVolumeDown, cvVolumeUp,
                        cvSlider,
                        cvBassDown, cvBassUp,
                        cvTrebleDown, cvTrebleUp);
    }

    private void initLabelCards() {
        CardView cvPlayer = mMain.findViewById(R.id.cv_player);
        CardView cvVolume = mMain.findViewById(R.id.cv_volume);
        CardView cvBass = mMain.findViewById(R.id.cv_bass);
        CardView cvTreble = mMain.findViewById(R.id.cv_treble);

        colorizeLabelCard(cvPlayer, cvVolume, cvBass, cvTreble);
    }

    private void drawIcons() {

        ImageView ivPlayerStop = mMain.findViewById(R.id.iv_player_stop);
        ImageView ivPlayerPause = mMain.findViewById(R.id.iv_player_pause);
        ImageView ivPlayerPlay = mMain.findViewById(R.id.iv_player_play);

        ImageView ivVolumeDown = mMain.findViewById(R.id.iv_volume_down);
        ImageView ivVolumeSlider = mMain.findViewById(R.id.iv_volume_slider);
        ImageView ivVolumeUp = mMain.findViewById(R.id.iv_volume_up);

        ImageView ivBassDown = mMain.findViewById(R.id.iv_bass_down);
        ImageView ivBassUp = mMain.findViewById(R.id.iv_bass_up);

        ImageView ivTrebleDown = mMain.findViewById(R.id.iv_treble_down);
        ImageView ivTrebleUp = mMain.findViewById(R.id.iv_treble_up);

        ivPlayerStop.setImageDrawable(new IconicsDrawable(mMain, CommunityMaterial
                .Icon.cmd_close_circle_outline).colorRes(Setup.getButtonIconColor()).sizeDp(ACTION_ICON_SIZE_LARGE));

        ivPlayerPause.setImageDrawable(new IconicsDrawable(mMain, CommunityMaterial
                .Icon.cmd_pause_circle_outline).colorRes(Setup.getButtonIconColor()).sizeDp(ACTION_ICON_SIZE_LARGE));

        ivPlayerPlay.setImageDrawable(new IconicsDrawable(mMain, CommunityMaterial
                .Icon.cmd_play_circle_outline).colorRes(Setup.getButtonIconColor()).sizeDp(ACTION_ICON_SIZE_LARGE));

        ivVolumeDown.setImageDrawable(new IconicsDrawable(mMain, CommunityMaterial
                .Icon.cmd_menu_down).colorRes(Setup.getButtonIconColor()).sizeDp(ACTION_ICON_SIZE_LARGE));

        ivVolumeUp.setImageDrawable(new IconicsDrawable(mMain, CommunityMaterial
                .Icon.cmd_menu_up).colorRes(Setup.getButtonIconColor()).sizeDp(ACTION_ICON_SIZE_LARGE));

        ivVolumeSlider.setImageDrawable(new IconicsDrawable(mMain, CommunityMaterial
                .Icon.cmd_vector_point).colorRes(Setup.getButtonIconColor()).sizeDp(ACTION_ICON_SIZE_LARGE));

        ivBassDown.setImageDrawable(new IconicsDrawable(mMain, CommunityMaterial
                .Icon.cmd_menu_down).colorRes(Setup.getButtonIconColor()).sizeDp(ACTION_ICON_SIZE_MEDIUM));

        ivBassUp.setImageDrawable(new IconicsDrawable(mMain, CommunityMaterial
                .Icon.cmd_menu_up).colorRes(Setup.getButtonIconColor()).sizeDp(ACTION_ICON_SIZE_MEDIUM));

        ivTrebleDown.setImageDrawable(new IconicsDrawable(mMain, CommunityMaterial
                .Icon.cmd_menu_down).colorRes(Setup.getButtonIconColor()).sizeDp(ACTION_ICON_SIZE_MEDIUM));

        ivTrebleUp.setImageDrawable(new IconicsDrawable(mMain, CommunityMaterial
                .Icon.cmd_menu_up).colorRes(Setup.getButtonIconColor()).sizeDp(ACTION_ICON_SIZE_MEDIUM));
    }

    private void setCardClickListener(CardView... cards) {
        for (CardView c : cards) {
            c.setOnClickListener(this);
        }
    }

    private void setCardLevel(int level, CardView... cards) {
        for (CardView c : cards) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                c.getForeground().setLevel(level);
            }
        }
    }

    private void colorizeLabelCard(CardView... cards) {
        for (CardView c : cards) {
            c.setCardBackgroundColor(mMain.getResources().getColor(Setup.getLabelCardColor()));
        }
    }

    private void colorizeActionCard(CardView... cards) {
        for (CardView c : cards) {
            c.setCardBackgroundColor(mMain.getResources().getColor(Setup.getActionCardColor()));
        }
    }

    private void setActionBackground(TextView... tvs) {
        for (TextView t : tvs) {
            t.setBackgroundColor(mMain.getResources().getColor(Setup.getActionCardColor()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMain.setTemporaryTitleUpdate(mMain.getLastSelection());
        mTitleWriterHandler.removeCallbacks(mTitleWriterRunnable);
    }

    @Override
    public void onClick(View v) {
        String updateMsg = "", action = "";
        int volBass, volTreble;
        float displayedValue;

        switch (v.getId()) {
            case R.id.cv_volume_down:
                if (mInvisibleVolSlider != null) {
                    mInvisibleVolSlider.incrementProgressBy(Setup.getVolumeSteps() * -1);
                }
                return;

            case R.id.cv_volume_up:
                if (mInvisibleVolSlider != null) {
                    mInvisibleVolSlider.incrementProgressBy(Setup.getVolumeSteps());
                }
                return;

            case R.id.cv_slider:
                mDialogVolumeSlider = new DialogVolumeSlider(mMain);
                mDialogVolumeSlider.show();
                break;

            case R.id.cv_bass_down:
                volBass = MainActivity.getAmp().getBass();
                mMain.runCtrlrTask(
                        false,
                        Commands.SET_BASS_OR_TREBLE("Bass", (volBass) - 5),
                        Const.M_BASS_SET + String.valueOf((volBass - 5)));

                displayedValue = ((float) (volBass - 5) / 10);
                displayedValue = limitCheck(displayedValue);
                updateMsg = getString(R.string.bass) + ": " +
                        showPlusIfNeeded(displayedValue) +
                        String.valueOf(displayedValue + " dB");

                showActionInToolbar(mMain, updateMsg);
                return;

            case R.id.cv_bass_up:
                volBass = MainActivity.getAmp().getBass();
                mMain.runCtrlrTask(
                        false,
                        Commands.SET_BASS_OR_TREBLE("Bass", (volBass) + 5),
                        Const.M_BASS_SET + String.valueOf((volBass + 5)));

                displayedValue = ((float) (volBass + 5) / 10);
                displayedValue = limitCheck(displayedValue);
                updateMsg = getString(R.string.bass) + ": " +
                        showPlusIfNeeded(displayedValue) +
                        String.valueOf(displayedValue) + " dB";

                showActionInToolbar(mMain, updateMsg);
                return;

            case R.id.cv_treble_down:
                volTreble = MainActivity.getAmp().getTreble();
                mMain.runCtrlrTask(
                        false,
                        Commands.SET_BASS_OR_TREBLE("Treble", (volTreble) - 5),
                        Const.M_TREBLE_SET + String.valueOf((volTreble - 5)));

                displayedValue = ((float) (volTreble - 5) / 10);
                displayedValue = limitCheck(displayedValue);
                updateMsg = getString(R.string.treble) + ": " +
                        showPlusIfNeeded(displayedValue) +
                        String.valueOf(displayedValue + " dB");

                showActionInToolbar(mMain, updateMsg);
                return;

            case R.id.cv_treble_up:
                volTreble = MainActivity.getAmp().getTreble();
                mMain.runCtrlrTask(
                        false,
                        Commands.SET_BASS_OR_TREBLE("Treble", (volTreble) + 5),
                        Const.M_TREBLE_SET + String.valueOf((volTreble + 5)));

                displayedValue = ((float) (volTreble + 5) / 10);
                displayedValue = limitCheck(displayedValue);
                updateMsg = getString(R.string.treble) + ": " +
                        showPlusIfNeeded(displayedValue) +
                        String.valueOf(displayedValue + " dB");

                showActionInToolbar(mMain, updateMsg);
                return;

            case R.id.cv_player_stop:
                updateMsg = getString(R.string.stop);
                action = AmpYaRxV577.PC_STOP;
                break;

            case R.id.cv_player_pause:
                updateMsg = getString(R.string.pause);
                action = AmpYaRxV577.PC_PAUSE;
                break;

            case R.id.cv_player_play:
                updateMsg = getString(R.string.play);
                action = AmpYaRxV577.PC_PLAY;
                break;
        }

        mMain.runCtrlrTask(false, Commands.PLAYBACK_CONTROL(action));
        showActionInToolbar(mMain, updateMsg);
    }

    private float limitCheck(float displayedValue) {
        if (displayedValue > 6 && displayedValue > 0) return 6;
        if (displayedValue < -6 && displayedValue < 0) {
            return -6;
        } else {
            return displayedValue;
        }
    }

    private String showPlusIfNeeded(float volBass) {
        return ((volBass) >= 0 ? "+" : "");
    }

    private void showActionInToolbar(MainActivity main, String updateMsg) {
        main.setTemporaryTitleUpdate(updateMsg);
        mTitleWriterHandler.removeCallbacks(mTitleWriterRunnable);
        mTitleWriterHandler.postDelayed(mTitleWriterRunnable, Const.DELAY_TITLE_RESET);
    }

    public void setVolumeSlider(float volume) {
        Log.w(TAG, "volume: " + volume);

        if (mInvisibleVolSlider != null) {
            volume = volume * -1 * 10;
            mInvisibleVolSlider.setProgress((int) (800 - volume));
        }
    }

    public void updateSwitches(boolean isEnhancer, boolean sprkA, boolean sprkB) {
        Switch swEnhancer = mMain.findViewById(R.id.sw_enhancer);
        Switch swSpkrA = mMain.findViewById(R.id.sw_speaker_a);
        Switch swSpkrB = mMain.findViewById(R.id.sw_speaker_b);

        if (swEnhancer != null) {
            swEnhancer.setChecked(isEnhancer);
            swEnhancer.setOnCheckedChangeListener(this);
        }
        if (swSpkrA != null) {
            swSpkrA.setChecked(sprkA);
            swSpkrA.setOnCheckedChangeListener(this);
        }
        if (swSpkrB != null) {
            swSpkrB.setChecked(sprkB);
            swSpkrB.setOnCheckedChangeListener(this);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String action = "", msg = "", trailer = "";

        switch (buttonView.getId()) {

            case R.id.sw_enhancer:
                action = Commands.SET_ENHANCER("Enhancer", isChecked ? "On" : "Off");
                msg = getString(R.string.spec_enhancer) + " " +
                        (isChecked ? getString(R.string.active)
                                : getString(R.string.inactive));
                trailer = Const.M_ENHANCER_SET + (isChecked ? "true" : "false");
                break;

            case R.id.sw_speaker_a:
                action = Commands.SPEAKER_CONFIG(isChecked, MainActivity.getAmp().isSpeakerB());
                msg = getString(R.string.speaker_a) + " " +
                        (isChecked ? getString(R.string.active)
                                : getString(R.string.inactive));
                trailer = Const.M_SPKR_A_SET + (isChecked ? "true" : "false");
                break;

            case R.id.sw_speaker_b:
                action = Commands.SPEAKER_CONFIG(MainActivity.getAmp().isSpeakerA(), isChecked);
                msg = getString(R.string.speaker_b) + " " +
                        (isChecked ? getString(R.string.active)
                                : getString(R.string.inactive));
                trailer = Const.M_SPKR_B_SET + (isChecked ? "true" : "false");
                break;

        }

        showActionInToolbar(mMain, msg);

        mMain.runCtrlrTask(false, action, Const.DO_NOT_UPDATE, trailer);
    }

}