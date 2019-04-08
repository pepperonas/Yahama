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

package com.pepperonas.yahama.app.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.NotificationActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.model.AmpYaRxV577;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogVolumeSlider {

    private TextView mTvVolume;
    private MaterialDialog mDialog;

    public DialogVolumeSlider(final Activity act) {

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!Setup.getCloseVolumeDialogAutomatically()) {
                    // only if active
                    return;
                }
                mDialog.dismiss();
            }
        };

        mDialog = new MaterialDialog.Builder(act)
                .customView(R.layout.dialog_volume_slider)
                .dismissListener(new MaterialDialog.DismissListener() {
                    @Override
                    public void onDismiss() {
                        super.onDismiss();
                        if (act instanceof NotificationActivity) {

                            // disabled background fading when dialog is shown
                            mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                            act.finish();
                        }
                    }
                })
                .showListener(new MaterialDialog.ShowListener() {
                    @Override
                    public void onShow(AlertDialog dialog) {
                        super.onShow(dialog);
                        fireClosingTimer(handler, runnable);
                    }
                })
                .build();

        mDialog.show();

        mTvVolume = mDialog.findViewById(R.id.dialog_volume_textview);
        if (mTvVolume != null) {
            mTvVolume.setText(MainActivity.getVolumeMessage(act, AmpYaRxV577
                    .getVolume_dB(MainActivity.getInvisibleVolSeekBar().getProgress())));
        }

        SeekBar volumeSlider = mDialog.findViewById(R.id.dialog_volume_slider);
        volumeSlider.setMax((int) AmpYaRxV577.MAX_VOL_SLIDER);
        volumeSlider.setProgress(MainActivity.getInvisibleVolSeekBar().getProgress());

        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.getInvisibleVolSeekBar().setProgress(progress);
                mTvVolume.setText(MainActivity.getVolumeMessage(act, AmpYaRxV577
                        .getVolume_dB(MainActivity.getInvisibleVolSeekBar().getProgress())));

                fireClosingTimer(handler, runnable);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                MainActivity.getAmp().setMute(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        fireClosingTimer(handler, runnable);

    }

    private void fireClosingTimer(Handler h, Runnable runnable) {
        h.removeCallbacks(runnable);
        h.postDelayed(runnable, Const.DELAY_DISMISS_VOLUME_DIALOG);
    }

    public void show() { mDialog.show(); }

    public void showSeekBarValue(String msg) {
        if (mTvVolume != null) mTvVolume.setText(msg);
    }

}
