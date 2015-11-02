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

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.data.AmpYaRxV577;
import com.pepperonas.yahama.app.dialogs.DialogCinemaDsp3d7ch;
import com.pepperonas.yahama.app.utility.Commands;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;


/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DspFragment extends Fragment
        implements View.OnClickListener,
                   View.OnLongClickListener,
                   CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "DspFragment";

    private static final int ACTION_ICON_SIZE_LARGE = 40;
    private static final long DELAY_OFFSET = 50;

    private MainActivity mMain;

    private Handler mTitleWriterHandler = new Handler();
    private Runnable mTitleWriterRunnable = new Runnable() {
        @Override
        public void run() {
            mMain.setTitle(mMain.getLastSelection());
        }
    };


    public void setMain(MainActivity main) {
        this.mMain = main;
    }


    public static DspFragment newInstance(int i) {
        DspFragment fragment = new DspFragment();

        Bundle args = new Bundle();
        args.putInt("the_id", i);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated()");

        View v = inflater.inflate(R.layout.fragment_dsp, container, false);
        mMain = (MainActivity) getActivity();

        String input = MainActivity.getAmp().getActiveInput();
        if (input.equals(AmpYaRxV577.INPUT_NET_RADIO)) {
            input = getString(R.string.webradio);
        }
        mMain.setTitle(getString(R.string.dsp) + " " + getString(R.string.foR) + " " + input);
        mMain.runCtrlrTask(false, Commands.GET_INFO, Const.M_GET_INFO);
        setRetainInstance(true);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");

        initLabelCards();

        initActionCards();

        showInfoSnackbarIfNeeded();
    }


    private void showInfoSnackbarIfNeeded() {
        if (!Setup.getShowDspInfo()) return;
        CoordinatorLayout cl = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
        Snackbar sb = Snackbar.make(cl, getString(R.string.sb_hold_to_configure_dsp), Snackbar.LENGTH_LONG);
        sb.setActionTextColor(Color.RED);
        sb.setAction(getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setup.setShowDspInfo(false);
            }
        });
        View snackbarView = sb.getView();
        snackbarView.setBackgroundColor(Color.BLACK);
        sb.show();
    }


    @Override
    public void onPause() {
        super.onPause();
        mMain.setTemporaryTitleUpdate(mMain.getLastSelection());
        mTitleWriterHandler.removeCallbacks(mTitleWriterRunnable);
    }


    @Override
    public void onClick(View v) {
        Switch swStraight = (Switch) mMain.findViewById(R.id.sw_straight);
        String action = "";

        switch (v.getId()) {
            case R.id.cv_music_2ch:
                action = AmpYaRxV577.DSP_2CH_STEREO;
                swStraight.setChecked(false);
                break;
            case R.id.cv_music_7ch:
                action = AmpYaRxV577.DSP_7CH_STEREO;
                swStraight.setChecked(false);
                break;
            case R.id.cv_music_surround_decoder:
                action = AmpYaRxV577.DSP_SURROUND_DECODER;
                swStraight.setChecked(false);
                break;
            case R.id.cv_movie_actiongame:
                action = AmpYaRxV577.ACTION_GAME;
                swStraight.setChecked(false);
                break;
            case R.id.cv_movie_adventure:
                action = AmpYaRxV577.ADVENTURE;
                swStraight.setChecked(false);
                break;
            case R.id.cv_movie_drama:
                action = AmpYaRxV577.DRAMA;
                swStraight.setChecked(false);
                break;
            case R.id.cv_movie_monomovie:
                action = AmpYaRxV577.MONO_MOVIE;
                swStraight.setChecked(false);
                break;
            case R.id.cv_music_musicvideo:
                action = AmpYaRxV577.MUSIC_VIDEO;
                swStraight.setChecked(false);
                break;
            case R.id.cv_movie_roleplaygame:
                action = AmpYaRxV577.ROLEPLAYING_GAME;
                swStraight.setChecked(false);
                break;
            case R.id.cv_movie_sci_fi:
                action = AmpYaRxV577.SCIFI;
                swStraight.setChecked(false);
                break;
            case R.id.cv_movie_spectacle:
                action = AmpYaRxV577.SPECTACLE;
                swStraight.setChecked(false);
                break;
            case R.id.cv_movie_sports:
                action = AmpYaRxV577.SPORTS;
                swStraight.setChecked(false);
                break;
            case R.id.cv_movie_standard:
                action = AmpYaRxV577.STANDARD;
                swStraight.setChecked(false);
                break;
            case R.id.cv_music_cellarclub:
                action = AmpYaRxV577.CELLAR_CLUB;
                swStraight.setChecked(false);
                break;
            case R.id.cv_music_chamber:
                action = AmpYaRxV577.CHAMBER;
                swStraight.setChecked(false);
                break;
            case R.id.cv_music_hallinmunich:
                action = AmpYaRxV577.HALL_IN_MUNICH;
                swStraight.setChecked(false);
                break;
            case R.id.cv_music_hallinvienna:
                action = AmpYaRxV577.HALL_IN_VIENNA;
                swStraight.setChecked(false);
                break;
            case R.id.cv_music_thebottomline:
                action = AmpYaRxV577.THE_BOTTOM_LINE;
                swStraight.setChecked(false);
                break;
            case R.id.cv_music_theroxytheatre:
                action = AmpYaRxV577.THE_ROXY_THEATRE;
                swStraight.setChecked(false);
                break;

        }

        showActionInToolbar(action);
        mMain.runCtrlrTask(false, Commands.SET_DSP(action));
    }


    @Override
    public boolean onLongClick(final View v) {
        Handler handler = new Handler();

        switch (v.getId()) {

            case R.id.cv_music_7ch:
                v.callOnClick();
                new DialogCinemaDsp3d7ch(mMain);
                break;

            case R.id.cv_music_theroxytheatre:
            case R.id.cv_movie_monomovie:
            case R.id.cv_music_chamber:
                v.callOnClick();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMain.runCtrlrTask(
                                false,
                                Commands.GET_DSP_INFO(AmpYaRxV577.getCategory(getXmlName(v.getId()))),
                                Const.M_DIALOG_SHOW_DSP_CONFIG + v.getId() + "</");
                    }
                }, Const.DELAY_BETWEEN_COMMANDS + DELAY_OFFSET);

                break;

            case R.id.cv_music_hallinmunich:
            case R.id.cv_music_hallinvienna:
            case R.id.cv_music_cellarclub:
            case R.id.cv_music_thebottomline:
                v.callOnClick();
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMain.runCtrlrTask(
                                false,
                                Commands.GET_DSP_INFO(AmpYaRxV577.getCategory(getXmlName(v.getId()))),
                                Const.M_DIALOG_SHOW_DSP_CONFIG + v.getId() + "</");
                    }
                }, Const.DELAY_BETWEEN_COMMANDS + DELAY_OFFSET);

                break;

            case R.id.cv_movie_actiongame:
            case R.id.cv_movie_adventure:
            case R.id.cv_movie_drama:
            case R.id.cv_music_musicvideo:
            case R.id.cv_movie_roleplaygame:
            case R.id.cv_movie_sci_fi:
            case R.id.cv_movie_spectacle:
            case R.id.cv_movie_sports:
            case R.id.cv_movie_standard:
                v.callOnClick();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMain.runCtrlrTask(
                                false,
                                Commands.GET_DSP_INFO(AmpYaRxV577.getCategory(getXmlName(v.getId()))),
                                Const.M_DIALOG_SHOW_DSP_CONFIG + v.getId() + "</");
                    }
                }, Const.DELAY_BETWEEN_COMMANDS + DELAY_OFFSET);

                break;
        }

        return true;
    }


    public String getXmlName(int id) {
        switch (id) {
            case R.id.cv_movie_actiongame:
                return AmpYaRxV577.XML_ACTION_GAME;
            case R.id.cv_movie_adventure:
                return AmpYaRxV577.ADVENTURE;
            case R.id.cv_movie_drama:
                return AmpYaRxV577.DRAMA;
            case R.id.cv_music_musicvideo:
                return AmpYaRxV577.XML_MUSIC_VIDEO;
            case R.id.cv_movie_roleplaygame:
                return AmpYaRxV577.XML_ROLEPLAYING_GAME;
            case R.id.cv_movie_sci_fi:
                return AmpYaRxV577.XML_SCIFI;
            case R.id.cv_movie_spectacle:
                return AmpYaRxV577.SPECTACLE;
            case R.id.cv_movie_sports:
                return AmpYaRxV577.SPORTS;
            case R.id.cv_movie_standard:
                return AmpYaRxV577.STANDARD;
            case R.id.cv_movie_monomovie:
                return AmpYaRxV577.XML_MONO_MOVIE;
            case R.id.cv_music_theroxytheatre:
                return AmpYaRxV577.XML_THE_ROXY_THEATRE;
            case R.id.cv_music_chamber:
                return AmpYaRxV577.CHAMBER;
            case R.id.cv_music_thebottomline:
                return AmpYaRxV577.XML_THE_BOTTOM_LINE;
            case R.id.cv_music_hallinmunich:
                return AmpYaRxV577.XML_HALL_IN_MUNICH;
            case R.id.cv_music_hallinvienna:
                return AmpYaRxV577.XML_HALL_IN_VIENNA;
            case R.id.cv_music_cellarclub:
                return AmpYaRxV577.XML_CELLAR_CLUB;
        }
        return "";
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String action = "";

        switch (buttonView.getId()) {

            case R.id.sw_direct:
                action = Commands.SET_DIRECT(isChecked ? "On" : "Off");

                Switch swStraight = (Switch) mMain.findViewById(R.id.sw_straight);
                Switch swExtraBass = (Switch) mMain.findViewById(R.id.sw_extra_bass);
                Switch swAdaptiveDrc = (Switch) mMain.findViewById(R.id.sw_adaptive_drc);
                Switch sw_3dCinemaDrc = (Switch) mMain.findViewById(R.id.sw_3d_cinema_drc);

                swStraight.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                swExtraBass.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                swAdaptiveDrc.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                sw_3dCinemaDrc.setVisibility(isChecked ? View.GONE : View.VISIBLE);

                LinearLayout dspContainer = (LinearLayout) mMain.findViewById(R.id.dsp_container);
                dspContainer.setVisibility(isChecked ? View.GONE : View.VISIBLE);

                break;

            case R.id.sw_straight:
                action = Commands.SET_DSP_STRAIGHT(isChecked ? "On" : "Off");
                break;
            case R.id.sw_extra_bass:
                action = Commands.SET_EXTRA_BASS("Extra_Bass", isChecked ? "Auto" : "Off");
                break;
            case R.id.sw_adaptive_drc:
                action = Commands.SET_ADAPTIVE_DRC("Adaptive_DRC", isChecked ? "Auto" : "Off");
                break;
            case R.id.sw_3d_cinema_drc:
                action = Commands.SET_DRC("_3D_Cinema_DSP", isChecked ? "Auto" : "Off");
                break;
        }
        mMain.runCtrlrTask(false, action, Const.DO_NOT_UPDATE);
    }


    private void initActionCards() {
        CardView cvCh2Ch = (CardView) mMain.findViewById(R.id.cv_music_2ch);
        CardView cvCh7Ch = (CardView) mMain.findViewById(R.id.cv_music_7ch);
        CardView cvSurround = (CardView) mMain.findViewById(R.id.cv_music_surround_decoder);

        CardView cvCellar = (CardView) mMain.findViewById(R.id.cv_music_cellarclub);
        CardView cvChamber = (CardView) mMain.findViewById(R.id.cv_music_chamber);
        CardView cvMunich = (CardView) mMain.findViewById(R.id.cv_music_hallinmunich);
        CardView cvVienna = (CardView) mMain.findViewById(R.id.cv_music_hallinvienna);
        CardView cvBottom = (CardView) mMain.findViewById(R.id.cv_music_thebottomline);
        CardView cvRoxy = (CardView) mMain.findViewById(R.id.cv_music_theroxytheatre);
        CardView cvMusic = (CardView) mMain.findViewById(R.id.cv_music_musicvideo);

        CardView cvAction = (CardView) mMain.findViewById(R.id.cv_movie_actiongame);
        CardView cvAdventure = (CardView) mMain.findViewById(R.id.cv_movie_adventure);
        CardView cvDrama = (CardView) mMain.findViewById(R.id.cv_movie_drama);
        CardView cvMono = (CardView) mMain.findViewById(R.id.cv_movie_monomovie);
        CardView cvRole = (CardView) mMain.findViewById(R.id.cv_movie_roleplaygame);
        CardView cvSci = (CardView) mMain.findViewById(R.id.cv_movie_sci_fi);
        CardView cvSpec = (CardView) mMain.findViewById(R.id.cv_movie_spectacle);
        CardView cvSports = (CardView) mMain.findViewById(R.id.cv_movie_sports);
        CardView cvStan = (CardView) mMain.findViewById(R.id.cv_movie_standard);


        setCardLevel(Setup.getTheme()
                     == 0 ? Setup.COLOR_ACCENT
                          : Setup.COLOR_ACCENT_LIGHT,
                     cvCh2Ch, cvCh7Ch, cvSurround,
                     cvCellar, cvChamber, cvMunich, cvVienna, cvBottom, cvRoxy,
                     cvAction, cvAdventure, cvDrama, cvMono, cvMusic, cvRole, cvSci,
                     cvSpec, cvSports, cvStan);


        setCardClickListener(cvCh2Ch, cvCh7Ch, cvSurround,
                             cvCellar, cvChamber, cvMunich, cvVienna, cvBottom, cvRoxy,
                             cvAction, cvAdventure, cvDrama, cvMono, cvMusic, cvRole, cvSci,
                             cvSpec, cvSports, cvStan);

        setCardLongClickListener(
                cvCh7Ch,
                cvRoxy, cvMono, cvChamber,
                cvMunich, cvVienna, cvCellar, cvBottom,
                cvAction, cvAdventure, cvDrama, cvMusic, cvRole,
                cvSci, cvSpec, cvSports, cvStan);
    }


    private void initLabelCards() {
        CardView cvStraight = (CardView) mMain.findViewById(R.id.cv_direct_and_straight);
        CardView cvMusic = (CardView) mMain.findViewById(R.id.cv_music);
        CardView cvMovie = (CardView) mMain.findViewById(R.id.cv_movie);
        colorizeLabelCard(cvStraight, cvMusic, cvMovie);
    }


    private void setCardClickListener(CardView... cards) {
        for (CardView c : cards) c.setOnClickListener(this);
    }


    private void setCardLevel(int level, CardView... cards) {
        for (CardView c : cards) c.getForeground().setLevel(level);
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


    private void showActionInToolbar(String updateMsg) {
        mMain.setTemporaryTitleUpdate(updateMsg);
        mTitleWriterHandler.removeCallbacks(mTitleWriterRunnable);
        mTitleWriterHandler.postDelayed(mTitleWriterRunnable, Const.DELAY_TITLE_RESET);
    }


    public void updateSwitches(boolean isDirect,
                               boolean isStraight,
                               boolean isExtraBass,
                               boolean isAdaptiveDrc,
                               boolean is_3dCinemaDrc) {

        Switch swDirect = (Switch) mMain.findViewById(R.id.sw_direct);
        swDirect.setChecked(isDirect);
        swDirect.setOnCheckedChangeListener(this);

        Switch swStraight = (Switch) mMain.findViewById(R.id.sw_straight);
        Switch swExtraBass = (Switch) mMain.findViewById(R.id.sw_extra_bass);
        Switch swAdaptiveDrc = (Switch) mMain.findViewById(R.id.sw_adaptive_drc);
        Switch sw_3dCinemaDrc = (Switch) mMain.findViewById(R.id.sw_3d_cinema_drc);

        swStraight.setChecked(isStraight);
        swExtraBass.setChecked(isExtraBass);
        swAdaptiveDrc.setChecked(isAdaptiveDrc);
        sw_3dCinemaDrc.setChecked(is_3dCinemaDrc);
        swStraight.setOnCheckedChangeListener(this);
        swExtraBass.setOnCheckedChangeListener(this);
        swAdaptiveDrc.setOnCheckedChangeListener(this);
        sw_3dCinemaDrc.setOnCheckedChangeListener(this);
        swStraight.setVisibility(isDirect ? View.GONE : View.VISIBLE);
        swExtraBass.setVisibility(isDirect ? View.GONE : View.VISIBLE);
        swAdaptiveDrc.setVisibility(isDirect ? View.GONE : View.VISIBLE);
        sw_3dCinemaDrc.setVisibility(isDirect ? View.GONE : View.VISIBLE);

        LinearLayout dspContainer = (LinearLayout) mMain.findViewById(R.id.dsp_container);
        dspContainer.setVisibility(isDirect ? View.GONE : View.VISIBLE);
    }


    public void setCardLongClickListener(CardView... cards) {
        for (CardView card : cards) card.setOnLongClickListener(this);
    }
}