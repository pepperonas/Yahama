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

package com.pepperonas.yahama.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pepperonas.yahama.app.model.AmpYaRxV577;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class VolumeSliderActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "VolumeSliderActivity";

    private TextView mTvVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Setup.getTheme() != 0) {
            setTheme(R.style.AppTheme_Dialog_Light);
        } else {
            setTheme(R.style.AppTheme_Dialog);
        }

        super.onCreate(savedInstanceState);

        //        DisplayMetrics metrics = getResources().getDisplayMetrics();
        //        int screenWidth = (int) (metrics.widthPixels * 0.8f);

        setContentView(R.layout.activity_volume_slider);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.67f;
        getWindow().setAttributes(layoutParams);

        //        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout container = findViewById(R.id.activity_dialog_volume_slider_container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        final Handler h = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!Setup.getCloseVolumeDialogAutomatically()) {
                    // only if active
                    return;
                }
                finish();
            }
        };
        mTvVolume = findViewById(R.id.activity_dialog_volume_textview);
        mTvVolume.setText(MainActivity.getVolumeMessage(this, AmpYaRxV577
                .getVolume_dB(MainActivity.getInvisibleVolSeekBar().getProgress())));

        SeekBar volumeSlider = findViewById(R.id.activity_dialog_volume_slider);
        volumeSlider.setMax((int) AmpYaRxV577.MAX_VOL_SLIDER);
        volumeSlider.setProgress(MainActivity.getInvisibleVolSeekBar().getProgress());

        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.getInvisibleVolSeekBar().setProgress(progress);
                mTvVolume.setText(MainActivity.getVolumeMessage(VolumeSliderActivity.this, AmpYaRxV577
                        .getVolume_dB(MainActivity.getInvisibleVolSeekBar().getProgress())));

                fireClosingTimer(h, runnable);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                MainActivity.getAmp().setMute(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        fireClosingTimer(h, runnable);
    }

    private void fireClosingTimer(Handler h, Runnable runnable) {
        h.removeCallbacks(runnable);
        h.postDelayed(runnable, Const.DELAY_DISMISS_VOLUME_DIALOG);
    }

}
