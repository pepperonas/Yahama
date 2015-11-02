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

package com.pepperonas.yahama.app.dialogs;

import android.content.DialogInterface;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogRadioStation {

    private static final String TAG = "DialogRadioStation";

    public DialogRadioStation(final MainActivity main) {
        new MaterialDialog.Builder(main)
                .title(R.string.dialog_title_device_info)
                .icon(new IconicsDrawable(main, GoogleMaterial.Icon.gmd_info_outline)
                              .colorRes(Setup.getDialogIconColor()).sizeDp(Const.DIALOG_ICON_SIZE))
                .customView(R.layout.dialog_radio_station, true)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        MaterialDialog d = (MaterialDialog) dialog;


                        ImageButton imgBtnFavorite = (ImageButton) d.findViewById(R.id.img_btn_dialog_station_favorite);
                        TextView tvStation = (TextView) d.findViewById(R.id.tv_dialog_station_station);
                        TextView tvArtist = (TextView) d.findViewById(R.id.tv_dialog_station_artist);
                        TextView tvTrack = (TextView) d.findViewById(R.id.tv_dialog_station_track);

                        imgBtnFavorite.setImageDrawable(
                                main.getResources().getDrawable(R.drawable.ic_launcher));

                        tvStation.setText(MainActivity.getAmp().getRadioStation().getStation());
                        tvArtist.setText(MainActivity.getAmp().getRadioStation().getSong());
                        tvTrack.setText("");

                        String song = MainActivity.getAmp().getRadioStation().getSong();
                        song = song.replaceAll("&(?!amp;)", "&amp;");

                        Log.d(TAG, "onShow  song=" + song);


                    }
                })
                .positiveText(R.string.ok)
                .build()
                .show();
    }


}
