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

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.data.AmpYaRxV577;
import com.pepperonas.yahama.app.utility.Commands;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class InputFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "InputFragment";

    private Handler mTitleWriterHandler = new Handler();
    private Runnable mTitleWriterRunnable = new Runnable() {
        @Override
        public void run() {
            MainActivity a = (MainActivity) getActivity();
            a.setTitle(a.getLastSelection());
        }
    };


    public static InputFragment newInstance(int i) {
        InputFragment fragment = new InputFragment();

        Bundle args = new Bundle();
        args.putInt("the_id", i);
        fragment.setArguments(args);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setTitle(getString(R.string.input));
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");

        initLabelCards();

        initActionCards();
    }


    private void initActionCards() {
        CardView cvAv1 = (CardView) getActivity().findViewById(R.id.cv_av_av1);
        CardView cvAv2 = (CardView) getActivity().findViewById(R.id.cv_av_av2);
        CardView cvAv3 = (CardView) getActivity().findViewById(R.id.cv_av_av3);

        CardView cvAv4 = (CardView) getActivity().findViewById(R.id.cv_av_av4);
        CardView cvAv5 = (CardView) getActivity().findViewById(R.id.cv_av_av5);
        CardView cvAv6 = (CardView) getActivity().findViewById(R.id.cv_av_av6);


        CardView cvHdmi1 = (CardView) getActivity().findViewById(R.id.cv_hdmi_hdmi1);
        CardView cvHdmi2 = (CardView) getActivity().findViewById(R.id.cv_hdmi_hdmi2);
        CardView cvHdmi3 = (CardView) getActivity().findViewById(R.id.cv_hdmi_hdmi3);

        CardView cvHdmi4 = (CardView) getActivity().findViewById(R.id.cv_hdmi_hdmi4);
        CardView cvHdmi5 = (CardView) getActivity().findViewById(R.id.cv_hdmi_hdmi5);
        CardView cvHdmi6 = (CardView) getActivity().findViewById(R.id.cv_hdmi_hdmi6);


        CardView cvTuner = (CardView) getActivity().findViewById(R.id.cv_others_tuner);
        CardView cvAux = (CardView) getActivity().findViewById(R.id.cv_others_aux);
        CardView cvUsb = (CardView) getActivity().findViewById(R.id.cv_others_usb);

        setCardClickListener(
                cvAv1, cvAv2, cvAv3, cvAv4, cvAv5, cvAv6,
                cvHdmi1, cvHdmi2, cvHdmi3, cvHdmi4, cvHdmi5, cvHdmi5, cvHdmi6,
                cvTuner, cvAux, cvUsb);

        setCardLevel(Setup.getTheme() == 0 ? Setup.COLOR_ACCENT
                                           : Setup.COLOR_ACCENT_LIGHT,
                     cvAv1, cvAv2, cvAv3, cvAv4, cvAv5, cvAv6,
                     cvHdmi1, cvHdmi2, cvHdmi3, cvHdmi4, cvHdmi5, cvHdmi5, cvHdmi6,
                     cvTuner, cvAux, cvUsb);
    }


    private void setCardClickListener(CardView... cards) {
        for (CardView c : cards) c.setOnClickListener(this);
    }


    private void setCardLevel(int level, CardView... cards) {
        for (CardView c : cards) c.getForeground().setLevel(level);
    }


    private void initLabelCards() {
        CardView cvAv = (CardView) getActivity().findViewById(R.id.cv_av);
        CardView cvHdmi = (CardView) getActivity().findViewById(R.id.cv_hdmi);
        CardView cvOthers = (CardView) getActivity().findViewById(R.id.cv_others);
        colorizeLabelCard(getActivity(), cvAv, cvHdmi, cvOthers);
    }


    private void colorizeLabelCard(Activity a, CardView... cards) {
        for (CardView c : cards) {
            c.setCardBackgroundColor(a.getResources().getColor(Setup.getLabelCardColor()));
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setTemporaryTitleUpdate(mainActivity.getLastSelection());
        mTitleWriterHandler.removeCallbacks(mTitleWriterRunnable);
    }


    @Override
    public void onClick(View v) {
        MainActivity mainActivity = (MainActivity) getActivity();

        String updateMsg = "", input = "";

        switch (v.getId()) {
            case R.id.cv_av_av1:
                updateMsg = getString(R.string.av_1);
                input = AmpYaRxV577.INPUT_AV(1);
                break;
            case R.id.cv_av_av2:
                updateMsg = getString(R.string.av_2);
                input = AmpYaRxV577.INPUT_AV(2);
                break;
            case R.id.cv_av_av3:
                updateMsg = getString(R.string.av_3);
                input = AmpYaRxV577.INPUT_AV(3);
                break;
            case R.id.cv_av_av4:
                updateMsg = getString(R.string.av_4);
                input = AmpYaRxV577.INPUT_AV(4);
                break;
            case R.id.cv_av_av5:
                updateMsg = getString(R.string.av_5);
                input = AmpYaRxV577.INPUT_AV(5);
                break;
            case R.id.cv_av_av6:
                updateMsg = getString(R.string.av_6);
                input = AmpYaRxV577.INPUT_AV(6);
                break;
            case R.id.cv_hdmi_hdmi1:
                updateMsg = getString(R.string.hdmi_1);
                input = AmpYaRxV577.INPUT_HDMI(1);
                break;
            case R.id.cv_hdmi_hdmi2:
                updateMsg = getString(R.string.hdmi_2);
                input = AmpYaRxV577.INPUT_HDMI(2);
                break;
            case R.id.cv_hdmi_hdmi3:
                updateMsg = getString(R.string.hdmi_3);
                input = AmpYaRxV577.INPUT_HDMI(3);
                break;
            case R.id.cv_hdmi_hdmi4:
                updateMsg = getString(R.string.hdmi_4);
                input = AmpYaRxV577.INPUT_HDMI(4);
                break;
            case R.id.cv_hdmi_hdmi5:
                updateMsg = getString(R.string.hdmi_5);
                input = AmpYaRxV577.INPUT_HDMI(5);
                break;
            case R.id.cv_hdmi_hdmi6:
                updateMsg = getString(R.string.hdmi_6);
                input = AmpYaRxV577.INPUT_HDMI(6);
                break;
            case R.id.cv_others_tuner:
                updateMsg = getString(R.string.tuner);
                input = AmpYaRxV577.INPUT_TUNER;
                break;
            case R.id.cv_others_aux:
                updateMsg = getString(R.string.aux);
                input = AmpYaRxV577.INPUT_AUX;
                break;
            case R.id.cv_others_usb:
                updateMsg = getString(R.string.usb);
                input = AmpYaRxV577.INPUT_USB;
                break;
        }

        mainActivity.runCtrlrTask(false, Commands.SELECT_INPUT(input), Const.M_INPUT_SET, input);

        mainActivity.setTemporaryTitleUpdate(updateMsg);
        mTitleWriterHandler.removeCallbacks(mTitleWriterRunnable);
        mTitleWriterHandler.postDelayed(mTitleWriterRunnable, Const.DELAY_TITLE_RESET);
    }

}