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

package com.pepperonas.yahama.app.dialogs;

import android.content.DialogInterface;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.model.AmpYaRxV577;
import com.pepperonas.yahama.app.model.entity.BaseEntity;
import com.pepperonas.yahama.app.model.movie.entertainment.ActionGame;
import com.pepperonas.yahama.app.model.movie.entertainment.MusicVideo;
import com.pepperonas.yahama.app.model.movie.entertainment.RoleplayingGame;
import com.pepperonas.yahama.app.model.movie.entertainment.Sports;
import com.pepperonas.yahama.app.model.movie.movie.Adventure;
import com.pepperonas.yahama.app.model.movie.movie.Drama;
import com.pepperonas.yahama.app.model.movie.movie.MonoMovie;
import com.pepperonas.yahama.app.model.movie.movie.SciFi;
import com.pepperonas.yahama.app.model.movie.movie.Spectacle;
import com.pepperonas.yahama.app.model.movie.movie.Standard;
import com.pepperonas.yahama.app.model.music.CellarClub;
import com.pepperonas.yahama.app.model.music.Chamber;
import com.pepperonas.yahama.app.model.music.HallInMunich;
import com.pepperonas.yahama.app.model.music.HallInVienna;
import com.pepperonas.yahama.app.model.music.TheBottomLine;
import com.pepperonas.yahama.app.model.music.TheRoxyTheatre;
import com.pepperonas.yahama.app.utility.Commands;
import com.pepperonas.yahama.app.utils.Utils;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogCinemaDsp3d {

    private static final String TAG = "Dsp3d_MOVIE";

    // negative
    private static final int DSP_LVL_OFFSET = 6;
    private static final int REV_TIME_OFFSET = 10;

    private MainActivity mMain;

    private int mViewId;

    private TextView mTvInfo;
    private final String mXmlName;
    private final String mCategory;

    private int m0 = 0, m1 = 0, m2 = 0, m3 = 0, m4 = 0, m5 = 0, m6 = 0;

    private boolean mIsShown = false;


    public DialogCinemaDsp3d(MainActivity main, int viewId) {

        Log.i(TAG, "DialogCinemaDsp3dMovie ");

        mMain = main;

        mViewId = viewId;

        mXmlName = getXmlName();

        mCategory = AmpYaRxV577.getCategory(mXmlName);

        MaterialDialog dlg = new MaterialDialog.Builder(mMain)
                .customView(R.layout.dialog_cinema_dsp3d, true)
                .positiveText(R.string.ok)
                .neutralText(R.string.reset)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                    }


                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);

                        mMain.runCtrlrTask(false, Commands.RESET_3D_DSP(mCategory, getXmlName()));
                    }

                }).showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        MaterialDialog d = (MaterialDialog) dialog;

                        mTvInfo = (TextView) d.findViewById(R.id.dialog_movie_textview);
                        mTvInfo.setText(getCardName());

                        LinearLayout layout = (LinearLayout) d.findViewById(R.id.dsp3d_frame);

                        BaseEntity be = getEntityFromName();

                        if (isStandard()) initStandardLayout(be, layout);

                        else if (isAudioRoom()) initAudioRoomLayout(be, layout);

                        else if (isAudioAdvanced()) initAudioAdvancedLayout(be, layout);

                        else if (isAudioRoomAdvanced()) initAudioRoomAdvancedLayout(be, layout);

                        else if (isSurround()) initSurroundLayout(be, layout);

                    }
                })
                .build();
        dlg.show();
    }


    private void initStandardLayout(BaseEntity be, LinearLayout layout) {
        int _dspLvl = 0, _surInitDly = 0, _surRoomSize = 0,
                _surLiveness = 0, _surBackInitDly = 0, _surBackRoomSize = 0,
                _surBackLiveness = 0;

        if (be instanceof Standard) {
            _dspLvl = ((Standard) be).getDspLvl();
            _surInitDly = ((Standard) be).getSurInitDly();
            _surRoomSize = ((Standard) be).getSurRoomSize();
            _surLiveness = ((Standard) be).getSurLiveness();
            _surBackInitDly = ((Standard) be).getSurBackInitDly();
            _surBackRoomSize = ((Standard) be).getSurBackRoomSize();
            _surBackLiveness = ((Standard) be).getSurBackLiveness();

        }

        for (int i = 0; i < 7; i++) {
            final TextView tv = new TextView(mMain);
            final SeekBar sb = new SeekBar(mMain);
            layout.addView(tv);
            layout.addView(sb);

            final int ii = i;
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (!mIsShown) return;


                    switch (ii) {

                        case 0:
                            m0 = progress;
                            mMain.runCtrlrTask(false, Commands.SET_3D_DSP_STANDARD((progress - DSP_LVL_OFFSET), m1, m2, m3, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.dsp_lvl), progress - DSP_LVL_OFFSET));
                            break;

                        case 1:
                            m1 = progress;
                            mMain.runCtrlrTask(false, Commands.SET_3D_DSP_STANDARD(m0, progress, m2, m3, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_init_dly), progress));
                            break;

                        case 2:
                            m2 = progress;
                            mMain.runCtrlrTask(false, Commands.SET_3D_DSP_STANDARD(m0, m1, progress, m3, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_room_size), progress));
                            break;

                        case 3:
                            m3 = progress;
                            mMain.runCtrlrTask(false, Commands.SET_3D_DSP_STANDARD(m0, m1, m2, progress, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_liveness), progress));
                            break;

                        case 4:
                            m4 = progress;
                            mMain.runCtrlrTask(false, Commands.SET_3D_DSP_STANDARD(m0, m1, m2, m3, progress, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_back_init_dly), progress));
                            break;

                        case 5:
                            m5 = progress;
                            mMain.runCtrlrTask(false, Commands.SET_3D_DSP_STANDARD(m0, m1, m2, m3, m4, progress, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_back_room_size), progress));
                            break;

                        case 6:
                            m6 = progress;
                            mMain.runCtrlrTask(false, Commands.SET_3D_DSP_STANDARD(m0, m1, m2, m3, m4, m5, progress));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_back_liveness), progress));
                            break;

                    }
                }


                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}


                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

        }

        int x = 0;


        for (int i = 0; i < layout.getChildCount(); i += 2) {
            if (layout.getChildAt(i) instanceof TextView) {
                TextView tv = (TextView) layout.getChildAt(i);
                SeekBar sb = (SeekBar) layout.getChildAt(i + 1);

                switch (x) {

                    case 0:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.dsp_lvl), _dspLvl));
                        sb.setMax(9);
                        sb.setProgress(_dspLvl + DSP_LVL_OFFSET);
                        m0 = _dspLvl;
                        break;

                    case 1:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_init_dly), _surInitDly));
                        sb.setMax(49);
                        sb.setProgress(_surInitDly);
                        m1 = _surInitDly;
                        break;

                    case 2:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_room_size), _surRoomSize));
                        sb.setMax(20);
                        sb.setProgress(_surRoomSize);
                        m2 = _surRoomSize;
                        break;

                    case 3:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_liveness), _surLiveness));
                        sb.setMax(10);
                        sb.setProgress(_surLiveness);
                        m3 = _surLiveness;
                        break;

                    case 4:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_back_init_dly), _surBackInitDly));
                        sb.setMax(49);
                        sb.setProgress(_surBackInitDly);
                        m4 = _surBackInitDly;
                        break;

                    case 5:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_back_room_size), _surBackRoomSize));
                        sb.setMax(20);
                        sb.setProgress(_surBackRoomSize);
                        m5 = _surBackRoomSize;
                        break;

                    case 6:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_back_liveness), _surBackLiveness));
                        sb.setMax(10);
                        sb.setProgress(_surBackLiveness);
                        m6 = _surBackLiveness;
                        break;
                }

                x++;
            }
        }

        // now the slider will be active
        mIsShown = true;
    }


    private void initSurroundLayout(BaseEntity be, LinearLayout layout) {

        int _dspLvl = 0, _initDly = 0, _roomSize = 0,
                _surInitDly = 0, _surRoomSize = 0, _surBackInitDly = 0,
                _surBackRoomSize = 0;

        if (be instanceof Adventure) {
            _dspLvl = ((Adventure) be).getDspLvl();
            _initDly = ((Adventure) be).getInitDly();
            _roomSize = ((Adventure) be).getRoomSize();
            _surInitDly = ((Adventure) be).getSurInitDly();
            _surRoomSize = ((Adventure) be).getSurRoomSize();
            _surBackInitDly = ((Adventure) be).getSurBackInitDly();
            _surBackRoomSize = ((Adventure) be).getSurBackRoomSize();

        } else if (be instanceof Drama) {
            _dspLvl = ((Drama) be).getDspLvl();
            _initDly = ((Drama) be).getInitDly();
            _roomSize = ((Drama) be).getRoomSize();
            _surInitDly = ((Drama) be).getSurInitDly();
            _surRoomSize = ((Drama) be).getSurRoomSize();
            _surBackInitDly = ((Drama) be).getSurBackInitDly();
            _surBackRoomSize = ((Drama) be).getSurBackRoomSize();

        } else if (be instanceof SciFi) {
            _dspLvl = ((SciFi) be).getDspLvl();
            _initDly = ((SciFi) be).getInitDly();
            _roomSize = ((SciFi) be).getRoomSize();
            _surInitDly = ((SciFi) be).getSurInitDly();
            _surRoomSize = ((SciFi) be).getSurRoomSize();
            _surBackInitDly = ((SciFi) be).getSurBackInitDly();
            _surBackRoomSize = ((SciFi) be).getSurBackRoomSize();

        } else if (be instanceof MusicVideo) {
            _dspLvl = ((MusicVideo) be).getDspLvl();
            _initDly = ((MusicVideo) be).getInitDly();
            _roomSize = ((MusicVideo) be).getRoomSize();
            _surInitDly = ((MusicVideo) be).getSurInitDly();
            _surRoomSize = ((MusicVideo) be).getSurRoomSize();
            _surBackInitDly = ((MusicVideo) be).getSurBackInitDly();
            _surBackRoomSize = ((MusicVideo) be).getSurBackRoomSize();

        } else if (be instanceof RoleplayingGame) {
            _dspLvl = ((RoleplayingGame) be).getDspLvl();
            _initDly = ((RoleplayingGame) be).getInitDly();
            _roomSize = ((RoleplayingGame) be).getRoomSize();
            _surInitDly = ((RoleplayingGame) be).getSurInitDly();
            _surRoomSize = ((RoleplayingGame) be).getSurRoomSize();
            _surBackInitDly = ((RoleplayingGame) be).getSurBackInitDly();
            _surBackRoomSize = ((RoleplayingGame) be).getSurBackRoomSize();

        } else if (be instanceof Spectacle) {
            _dspLvl = ((Spectacle) be).getDspLvl();
            _initDly = ((Spectacle) be).getInitDly();
            _roomSize = ((Spectacle) be).getRoomSize();
            _surInitDly = ((Spectacle) be).getSurInitDly();
            _surRoomSize = ((Spectacle) be).getSurRoomSize();
            _surBackInitDly = ((Spectacle) be).getSurBackInitDly();
            _surBackRoomSize = ((Spectacle) be).getSurBackRoomSize();

        } else if (be instanceof Sports) {
            _dspLvl = ((Sports) be).getDspLvl();
            _initDly = ((Sports) be).getInitDly();
            _roomSize = ((Sports) be).getRoomSize();
            _surInitDly = ((Sports) be).getSurInitDly();
            _surRoomSize = ((Sports) be).getSurRoomSize();
            _surBackInitDly = ((Sports) be).getSurBackInitDly();
            _surBackRoomSize = ((Sports) be).getSurBackRoomSize();

        } else if (be instanceof ActionGame) {
            _dspLvl = ((ActionGame) be).getDspLvl();
            _initDly = ((ActionGame) be).getInitDly();
            _roomSize = ((ActionGame) be).getRoomSize();
            _surInitDly = ((ActionGame) be).getSurInitDly();
            _surRoomSize = ((ActionGame) be).getSurRoomSize();
            _surBackInitDly = ((ActionGame) be).getSurBackInitDly();
            _surBackRoomSize = ((ActionGame) be).getSurBackRoomSize();

        }

        for (int i = 0; i < 7; i++) {
            final TextView tv = new TextView(mMain);
            final SeekBar sb = new SeekBar(mMain);
            layout.addView(tv);
            layout.addView(sb);

            final int ii = i;
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (!mIsShown) return;


                    switch (ii) {

                        case 0:
                            m0 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MOVIE(getXmlName(), (progress - DSP_LVL_OFFSET), m1, m2, m3, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.dsp_lvl), progress - DSP_LVL_OFFSET));
                            break;

                        case 1:
                            m1 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MOVIE(getXmlName(), m0, progress, m2, m3, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.init_dly), progress));
                            break;

                        case 2:
                            m2 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MOVIE(getXmlName(), m0, m1, progress, m3, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.room_size), progress));
                            break;

                        case 3:
                            m3 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MOVIE(getXmlName(), m0, m1, m2, progress, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_init_dly), progress));
                            break;

                        case 4:
                            m4 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MOVIE(getXmlName(), m0, m1, m2, m3, progress, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_room_size), progress));
                            break;

                        case 5:
                            m5 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MOVIE(getXmlName(), m0, m1, m2, m3, m4, progress, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_back_init_dly), progress));
                            break;

                        case 6:
                            m6 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MOVIE(getXmlName(), m0, m1, m2, m3, m4, m5, progress));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_back_room_size), progress));
                            break;

                    }
                }


                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}


                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

        }

        int x = 0;

        for (int i = 0; i < layout.getChildCount(); i += 2) {
            if (layout.getChildAt(i) instanceof TextView) {
                TextView tv = (TextView) layout.getChildAt(i);
                SeekBar sb = (SeekBar) layout.getChildAt(i + 1);

                switch (x) {

                    case 0:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.dsp_lvl), _dspLvl));
                        sb.setMax(9);
                        sb.setProgress(_dspLvl + DSP_LVL_OFFSET);
                        m0 = _dspLvl;
                        break;

                    case 1:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.init_dly), _initDly));
                        sb.setMax(99);
                        sb.setProgress(_initDly);
                        m1 = _initDly;
                        break;

                    case 2:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.room_size), _roomSize));
                        sb.setMax(20);
                        sb.setProgress(_roomSize);
                        m2 = _roomSize;
                        break;

                    case 3:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_init_dly), _surInitDly));
                        sb.setMax(49);
                        sb.setProgress(_surInitDly);
                        m3 = _surInitDly;
                        break;

                    case 4:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_room_size), _surRoomSize));
                        sb.setMax(20);
                        sb.setProgress(_surRoomSize);
                        m4 = _surRoomSize;
                        break;

                    case 5:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_back_init_dly), _surBackInitDly));
                        sb.setMax(49);
                        sb.setProgress(_surBackInitDly);
                        m5 = _surBackInitDly;
                        break;

                    case 6:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.sur_back_room_size), _surBackRoomSize));
                        sb.setMax(20);
                        sb.setProgress(_surBackRoomSize);
                        m6 = _surBackRoomSize;
                        break;
                }

                x++;
            }
        }

        // now the slider will be active
        mIsShown = true;
    }


    private void initAudioRoomLayout(final BaseEntity be, LinearLayout layout) {

        int _dspLvl = 0, _initDly = 0, _roomSize = 0,
                _liveness = 0;

        if (be instanceof HallInMunich) {
            _dspLvl = ((HallInMunich) be).getDspLvl();
            _initDly = ((HallInMunich) be).getInitDly();
            _roomSize = ((HallInMunich) be).getRoomSize();
            _liveness = ((HallInMunich) be).getLiveness();

        } else if (be instanceof HallInVienna) {
            _dspLvl = ((HallInVienna) be).getDspLvl();
            _initDly = ((HallInVienna) be).getInitDly();
            _roomSize = ((HallInVienna) be).getRoomSize();
            _liveness = ((HallInVienna) be).getLiveness();

        } else if (be instanceof CellarClub) {
            _dspLvl = ((CellarClub) be).getDspLvl();
            _initDly = ((CellarClub) be).getInitDly();
            _roomSize = ((CellarClub) be).getRoomSize();
            _liveness = ((CellarClub) be).getLiveness();

        } else if (be instanceof TheBottomLine) {
            _dspLvl = ((TheBottomLine) be).getDspLvl();
            _initDly = ((TheBottomLine) be).getInitDly();
            _roomSize = ((TheBottomLine) be).getRoomSize();
            _liveness = ((TheBottomLine) be).getLiveness();

        }

        for (int i = 0; i < 4; i++) {
            final TextView tv = new TextView(mMain);
            final SeekBar sb = new SeekBar(mMain);
            layout.addView(tv);
            layout.addView(sb);

            final int ii = i;
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (!mIsShown) return;

                    switch (ii) {

                        case 0:
                            m0 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC(getXmlName(), (progress - DSP_LVL_OFFSET), m1, m2, m3));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.dsp_lvl), progress - DSP_LVL_OFFSET));
                            break;

                        case 1:
                            m1 = progress;
                            mMain.runCtrlrTask(false, Commands.SET_3D_DSP_MUSIC(getXmlName(), m0, progress, m2, m3));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.init_dly), progress));
                            break;

                        case 2:
                            m2 = progress;
                            mMain.runCtrlrTask(false, Commands.SET_3D_DSP_MUSIC(getXmlName(), m0, m1, progress, m3));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.room_size), progress));
                            break;

                        case 3:
                            m3 = progress;
                            mMain.runCtrlrTask(false, Commands.SET_3D_DSP_MUSIC(getXmlName(), m0, m1, m2, progress));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.liveness), progress));
                            break;

                    }
                }


                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}


                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }


        int x = 0;

        for (int i = 0; i < layout.getChildCount(); i += 2) {
            if (layout.getChildAt(i) instanceof TextView) {
                TextView tv = (TextView) layout.getChildAt(i);
                SeekBar sb = (SeekBar) layout.getChildAt(i + 1);
                switch (x) {

                    case 0:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.dsp_lvl), _dspLvl));
                        sb.setMax(9);
                        sb.setProgress(_dspLvl + DSP_LVL_OFFSET);
                        m0 = _dspLvl;
                        break;

                    case 1:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.init_dly), _initDly));
                        sb.setMax(99);
                        sb.setProgress(_initDly);
                        m1 = _initDly;
                        break;

                    case 2:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.room_size), _roomSize));
                        sb.setMax(20);
                        sb.setProgress(_roomSize);
                        m2 = _roomSize;
                        break;

                    case 3:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.liveness), _liveness));
                        sb.setMax(10);
                        sb.setProgress(_liveness);
                        m3 = _liveness;
                        break;
                }

                x++;
            }
        }

        // now the slider will be active
        mIsShown = true;
    }


    private void initAudioRoomAdvancedLayout(BaseEntity be, LinearLayout layout) {

        int _dspLvl = 0, _initDly = 0, _roomSize = 0,
                _liveness = 0, _revTime = 0, _revDly = 0, _revLvl = 0;

        if (be instanceof MonoMovie) {
            _dspLvl = ((MonoMovie) be).getDspLvl();
            _initDly = ((MonoMovie) be).getInitDly();
            _roomSize = ((MonoMovie) be).getRoomSize();
            _liveness = ((MonoMovie) be).getLiveness();
            _revTime = ((MonoMovie) be).getRevTime();
            _revDly = ((MonoMovie) be).getRevDly();
            _revLvl = ((MonoMovie) be).getRevLvl();

        } else if (be instanceof TheRoxyTheatre) {
            _dspLvl = ((TheRoxyTheatre) be).getDspLvl();
            _initDly = ((TheRoxyTheatre) be).getInitDly();
            _roomSize = ((TheRoxyTheatre) be).getRoomSize();
            _liveness = ((TheRoxyTheatre) be).getLiveness();
            _revTime = ((TheRoxyTheatre) be).getRevTime();
            _revDly = ((TheRoxyTheatre) be).getRevDly();
            _revLvl = ((TheRoxyTheatre) be).getRevLvl();

        }


        for (int i = 0; i < 7; i++) {
            final TextView tv = new TextView(mMain);
            final SeekBar sb = new SeekBar(mMain);
            layout.addView(tv);
            layout.addView(sb);

            final int ii = i;
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (!mIsShown) return;

                    switch (ii) {

                        case 0:
                            m0 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), (progress - DSP_LVL_OFFSET), m1, m2, m3, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.dsp_lvl), progress - DSP_LVL_OFFSET));
                            break;

                        case 1:
                            m1 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, progress, m2, m3, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.init_dly), progress));
                            break;

                        case 2:
                            m2 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, m1, progress, m3, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.room_size), progress));
                            break;

                        case 3:
                            m3 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, m1, m2, progress, m4, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.liveness), progress));
                            break;

                        case 4:
                            m4 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, m1, m2, m3, progress + REV_TIME_OFFSET, m5, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.revTime), progress + REV_TIME_OFFSET));
                            break;

                        case 5:
                            m5 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, m1, m2, m3, m4, progress, m6));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.revDly), progress));
                            break;

                        case 6:
                            m6 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, m1, m2, m3, m4, m5, progress));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.revLvl), progress));
                            break;

                    }
                }


                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}


                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

        }


        int x = 0;

        for (int i = 0; i < layout.getChildCount(); i += 2) {
            if (layout.getChildAt(i) instanceof TextView) {
                TextView tv = (TextView) layout.getChildAt(i);
                SeekBar sb = (SeekBar) layout.getChildAt(i + 1);

                switch (x) {

                    case 0:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.dsp_lvl), _dspLvl));
                        sb.setMax(9);
                        sb.setProgress(_dspLvl + DSP_LVL_OFFSET);
                        m0 = _dspLvl;
                        break;

                    case 1:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.init_dly), _initDly));
                        sb.setMax(99);
                        sb.setProgress(_initDly);
                        m1 = _initDly;
                        break;

                    case 2:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.room_size), _roomSize));
                        sb.setMax(20);
                        sb.setProgress(_roomSize);
                        m2 = _roomSize;
                        break;

                    case 3:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.liveness), _liveness));
                        sb.setMax(10);
                        sb.setProgress(_liveness);
                        m3 = _liveness;
                        break;

                    case 4:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.revTime), _revTime));
                        sb.setMax(40);
                        sb.setProgress(_revTime - REV_TIME_OFFSET);
                        m4 = _revTime;
                        break;

                    case 5:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.revDly), _revDly));
                        sb.setMax(250);
                        sb.setProgress(_revDly);
                        m5 = _revDly;
                        break;

                    case 6:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.revLvl), _revLvl));
                        sb.setMax(100);
                        sb.setProgress(_revLvl);
                        m6 = _revLvl;
                        break;
                }

                x++;
            }
        }

        // now the slider will be active
        mIsShown = true;
    }


    /**
     * Do not copy-paste from here!
     */
    private void initAudioAdvancedLayout(BaseEntity be, LinearLayout layout) {

        int _dspLvl = 0, _initDly = 0, _liveness = 0, _revTime = 0, _revDly = 0, _revLvl = 0;

        if (be instanceof Chamber) {
            _dspLvl = ((Chamber) be).getDspLvl();
            _initDly = ((Chamber) be).getInitDly();
            _liveness = ((Chamber) be).getLiveness();
            _revTime = ((Chamber) be).getRevTime();
            _revDly = ((Chamber) be).getRevDly();
            _revLvl = ((Chamber) be).getRevLvl();
        }

        for (int i = 0; i < 6; i++) {
            final TextView tv = new TextView(mMain);
            final SeekBar sb = new SeekBar(mMain);
            layout.addView(tv);
            layout.addView(sb);

            final int ii = i;
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (!mIsShown) return;

                    // modified order (m1, -> m2 <-, m3, ...)
                    switch (ii) {

                        case 0:
                            m0 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), (progress - DSP_LVL_OFFSET), m1, -1, m2, m3, m4, m5));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.dsp_lvl), progress - DSP_LVL_OFFSET));
                            break;

                        case 1:
                            m1 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, progress, -1, m2, m3, m4, m5));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.init_dly), progress));
                            break;

                        case 2:
                            m2 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, m1, -1, progress, m3, m4, m5));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.liveness), progress));
                            break;

                        case 3:
                            m3 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, m1, -1, m2, progress + REV_TIME_OFFSET, m4, m5));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.revTime), progress + REV_TIME_OFFSET));
                            break;

                        case 4:
                            m4 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, m1, -1, m2, m3, progress, m5));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.revDly), progress));
                            break;

                        case 5:
                            m5 = progress;
                            mMain.runCtrlrTask
                                    (false, Commands.SET_3D_DSP_MUSIC_ADVANCED(getXmlName(), m0, m1, -1, m2, m3, m4, progress));
                            tv.setText(String.format("%s: %d", mMain.getString(R.string.revLvl), progress));
                            break;

                    }
                }


                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}


                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

        }


        int x = 0;

        for (int i = 0; i < layout.getChildCount(); i += 2) {
            if (layout.getChildAt(i) instanceof TextView) {
                TextView tv = (TextView) layout.getChildAt(i);
                SeekBar sb = (SeekBar) layout.getChildAt(i + 1);
                switch (x) {

                    case 0:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.dsp_lvl), _dspLvl));
                        sb.setMax(9);
                        sb.setProgress(_dspLvl + DSP_LVL_OFFSET);
                        m0 = _dspLvl;
                        break;

                    case 1:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.init_dly), _initDly));
                        sb.setMax(99);
                        sb.setProgress(_initDly);
                        m1 = _initDly;
                        break;

                    case 2:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.liveness), _liveness));
                        sb.setMax(10);
                        sb.setProgress(_liveness);
                        m2 = _liveness;
                        break;

                    case 3:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.revTime), _revTime));
                        sb.setMax(40);
                        sb.setProgress(_revTime - REV_TIME_OFFSET);
                        m3 = _revTime;
                        break;

                    case 4:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.revDly), _revDly));
                        sb.setMax(250);
                        sb.setProgress(_revDly);
                        m4 = _revDly;
                        break;

                    case 5:
                        tv.setText(String.format("%s: %d", mMain.getString(R.string.revLvl), _revLvl));
                        sb.setMax(100);
                        sb.setProgress(_revLvl);
                        m5 = _revLvl;
                        break;
                }

                x++;
            }
        }

        // now the slider will be active
        mIsShown = true;
    }


    private boolean isStandard() {
        return Utils.stringEquals(mXmlName, AmpYaRxV577.STANDARD);
    }


    private boolean isAudioAdvanced() {
        return Utils.stringEquals(mXmlName, AmpYaRxV577.CHAMBER);
    }


    private boolean isSurround() {
        return Utils.stringEquals
                (mXmlName, AmpYaRxV577.SPECTACLE, AmpYaRxV577.XML_SCIFI,
                 AmpYaRxV577.DRAMA, AmpYaRxV577.ADVENTURE,
                 AmpYaRxV577.SPORTS, AmpYaRxV577.XML_MUSIC_VIDEO,
                 AmpYaRxV577.XML_ACTION_GAME, AmpYaRxV577.XML_ROLEPLAYING_GAME);
    }


    private boolean isAudioRoom() {
        return Utils.stringEquals
                (mXmlName, AmpYaRxV577.XML_CELLAR_CLUB, AmpYaRxV577.XML_THE_BOTTOM_LINE,
                 AmpYaRxV577.XML_HALL_IN_MUNICH, AmpYaRxV577.XML_HALL_IN_VIENNA);
    }


    private boolean isAudioRoomAdvanced() {
        return Utils.stringEquals(mXmlName, AmpYaRxV577.XML_MONO_MOVIE, AmpYaRxV577.XML_THE_ROXY_THEATRE);
    }


    public String getCardName() {
        switch (mViewId) {
            case R.id.cv_movie_actiongame: return mMain.getString(R.string.actiongame);
            case R.id.cv_movie_adventure: return mMain.getString(R.string.adventure);
            case R.id.cv_movie_drama: return mMain.getString(R.string.drama);
            case R.id.cv_music_musicvideo: return mMain.getString(R.string.music_video);
            case R.id.cv_movie_roleplaygame: return mMain.getString(R.string.role_play_game);
            case R.id.cv_movie_sci_fi: return mMain.getString(R.string.sci_fi);
            case R.id.cv_movie_spectacle: return mMain.getString(R.string.spectacle);
            case R.id.cv_movie_sports: return mMain.getString(R.string.sports);
            case R.id.cv_movie_standard: return mMain.getString(R.string.standard);
            case R.id.cv_movie_monomovie: return mMain.getString(R.string.mono_movie);
            case R.id.cv_music_theroxytheatre: return mMain.getString(R.string.the_roxy_theatre);
            case R.id.cv_music_chamber: return mMain.getString(R.string.chamber);
            case R.id.cv_music_thebottomline: return mMain.getString(R.string.the_bottom_line);
            case R.id.cv_music_hallinmunich: return mMain.getString(R.string.hall_in_munich);
            case R.id.cv_music_hallinvienna: return mMain.getString(R.string.hall_in_vienna);
            case R.id.cv_music_cellarclub: return mMain.getString(R.string.cellar_club);
        }
        Log.w(TAG, "getCardName NO VALUE FOUND! ID=" + mViewId);
        return "";
    }


    public String getXmlName() {
        switch (mViewId) {
            case R.id.cv_movie_actiongame: return AmpYaRxV577.XML_ACTION_GAME;
            case R.id.cv_movie_adventure: return AmpYaRxV577.ADVENTURE;
            case R.id.cv_movie_drama: return AmpYaRxV577.DRAMA;
            case R.id.cv_music_musicvideo: return AmpYaRxV577.XML_MUSIC_VIDEO;
            case R.id.cv_movie_roleplaygame: return AmpYaRxV577.XML_ROLEPLAYING_GAME;
            case R.id.cv_movie_sci_fi: return AmpYaRxV577.XML_SCIFI;
            case R.id.cv_movie_spectacle: return AmpYaRxV577.SPECTACLE;
            case R.id.cv_movie_sports: return AmpYaRxV577.SPORTS;
            case R.id.cv_movie_standard: return AmpYaRxV577.STANDARD;
            case R.id.cv_movie_monomovie: return AmpYaRxV577.XML_MONO_MOVIE;
            case R.id.cv_music_theroxytheatre: return AmpYaRxV577.XML_THE_ROXY_THEATRE;
            case R.id.cv_music_chamber: return AmpYaRxV577.CHAMBER;
            case R.id.cv_music_thebottomline: return AmpYaRxV577.XML_THE_BOTTOM_LINE;
            case R.id.cv_music_hallinmunich: return AmpYaRxV577.XML_HALL_IN_MUNICH;
            case R.id.cv_music_hallinvienna: return AmpYaRxV577.XML_HALL_IN_VIENNA;
            case R.id.cv_music_cellarclub: return AmpYaRxV577.XML_CELLAR_CLUB;
        }
        return "";
    }


    public BaseEntity getEntityFromName() {
        switch (mViewId) {
            case R.id.cv_movie_actiongame: return MainActivity.getAmp().getConfigEntertainment().getActionGame();
            case R.id.cv_movie_adventure: return MainActivity.getAmp().getConfigMovie().getAdventure();
            case R.id.cv_movie_drama: return MainActivity.getAmp().getConfigMovie().getDrama();
            case R.id.cv_music_musicvideo: return MainActivity.getAmp().getConfigEntertainment().getMusicVideo();
            case R.id.cv_movie_roleplaygame: return MainActivity.getAmp().getConfigEntertainment().getRoleplayingGame();
            case R.id.cv_movie_sci_fi: return MainActivity.getAmp().getConfigMovie().getSciFi();
            case R.id.cv_movie_spectacle: return MainActivity.getAmp().getConfigMovie().getSpectacle();
            case R.id.cv_movie_sports: return MainActivity.getAmp().getConfigEntertainment().getSports();
            case R.id.cv_movie_standard: return MainActivity.getAmp().getConfigMovie().getStandard();
            case R.id.cv_movie_monomovie: return MainActivity.getAmp().getConfigMovie().getMonoMovie();
            case R.id.cv_music_theroxytheatre: return MainActivity.getAmp().getConfigLiveClub().getTheRoxyTheatre();
            case R.id.cv_music_chamber: return MainActivity.getAmp().getConfigClassical().getChamber();
            case R.id.cv_music_thebottomline: return MainActivity.getAmp().getConfigLiveClub().getTheBottomLine();
            case R.id.cv_music_hallinmunich: return MainActivity.getAmp().getConfigClassical().getHallInMunich();
            case R.id.cv_music_hallinvienna: return MainActivity.getAmp().getConfigClassical().getHallInVienna();
            case R.id.cv_music_cellarclub: return MainActivity.getAmp().getConfigLiveClub().getCellarClub();
        }
        return null;
    }

}
