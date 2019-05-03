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

import android.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.utility.Commands;
import com.pepperonas.yahama.app.utils.Utils;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogCinemaDsp3d7ch {

    @SuppressWarnings("unused")
    private static final String TAG = "DialogCinemaDsp3d7ch";

    private TextView tvRange;
    private ImageView touchFrame;
    private ImageView visualizer;

    public DialogCinemaDsp3d7ch(final MainActivity main) {
        MaterialDialog dlg = new MaterialDialog.Builder(main)
                .customView(R.layout.dialog_cinema_dsp3d_7ch)
                .positiveText(R.string.ok)
                .neutralText(R.string.reset)
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                        main.runCtrlrTask(false, Commands.RESET_SURROUND_3D_CONFIG());
                    }
                })
                .showListener(new MaterialDialog.ShowListener() {
                    @Override
                    public void onShow(AlertDialog dialog) {
                        super.onShow(dialog);
                        MaterialDialog dlg = (MaterialDialog) dialog;

                        tvRange = dlg.findViewById(R.id.tv_range);
                        touchFrame = dlg.findViewById(R.id.touch_frame);
                        visualizer = dlg.findViewById(R.id.visualizer);

                        makeStartConfig(main);

                        touchFrame.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return trackInput(v, event);
                            }

                            private boolean trackInput(View v, MotionEvent event) {
                                int x = (int) event.getX() - (v.getWidth() / 2);
                                int y = (int) event.getY() - (v.getHeight() / 2);
                                int maxX = v.getWidth();
                                int maxY = v.getHeight();

                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                    case MotionEvent.ACTION_MOVE:
                                    case MotionEvent.ACTION_UP:

                                        x = Utils.checkMax(x, maxX);
                                        y = Utils.checkMax(y, maxY);

                                        visualizer.setX((x + v.getWidth() / 2) - visualizer.getWidth() / 2);
                                        visualizer.setY((y + v.getHeight() / 2) - visualizer.getHeight() / 2);

                                        checkBounds(v);

                                        double pX = Utils.toPercent(x, maxX);
                                        double pY = Utils.toPercent(y, maxY);

                                        int pXi = (int) Math.ceil(pX / 10d);
                                        int pYi = (int) Math.ceil(pY / -10d);

                                        String leftRight = main.getString(R.string.left_right) + " " + Utils.checkLimits(5, pXi);
                                        String frontRear = main.getString(R.string.front_rear) + " " + Utils.checkLimits(5, pYi);
                                        tvRange.setText(String.format("%s\n%s", leftRight, frontRear));

                                        main.runCtrlrTask(false, Commands.SURROUND_DSP_3D_CONFIG(
                                                Utils.checkLimits(5, pXi),
                                                Utils.checkLimits(5, pYi)));

                                }
                                return true;
                            }

                            private void checkBounds(View v) {
                                if (visualizer.getX() < 0) visualizer.setX(0);
                                if (visualizer.getY() < 0) visualizer.setY(0);

                                if ((visualizer.getX() + visualizer.getWidth()) > v.getWidth()) {
                                    visualizer.setX(v.getWidth() - visualizer.getWidth());
                                }

                                if ((visualizer.getY() + visualizer.getHeight()) > v.getHeight()) {
                                    visualizer.setY(v.getHeight() - visualizer.getHeight());
                                }
                            }
                        });
                    }
                }).build();

        dlg.show();
    }

    private void makeStartConfig(MainActivity main) {
        visualizer.setX(touchFrame.getWidth() / 2 - visualizer.getWidth() / 2);
        visualizer.setY(touchFrame.getHeight() / 2 - visualizer.getHeight() / 2);
        String leftRight = main.getString(R.string.left_right) + " 0";
        String frontRear = main.getString(R.string.front_rear) + " 0";
        tvRange.setText(String.format("%s\n%s", leftRight, frontRear));
    }

}
