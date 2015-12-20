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
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.utility.Commands;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;
import com.pepperonas.yahama.app.utils.Utils;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogSleeptimer {

    public DialogSleeptimer(final MainActivity main) {
        new MaterialDialog.Builder(main)
                .title(R.string.dialog_title_sleeptimer)
                .items(R.array.dialog_items_sleeptimer)
                .icon(new IconicsDrawable(main, CommunityMaterial.Icon.cmd_timer)
                              .colorRes(Setup.getDialogIconColor()).sizeDp(Const.DIALOG_ICON_SIZE))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        int mins;
                        switch (which) {
                            case 0:
                                mins = 30;
                                break;
                            case 1:
                                mins = 60;
                                break;
                            case 2:
                                mins = 90;
                                break;
                            case 3:
                                mins = 120;
                                break;
                            case 4:
                                mins = 0;
                                break;
                            default:
                                mins = 0;
                        }

                        String msg;
                        if (mins != 0) {
                            msg = main.getString(R.string.sleep_timer_set_start) + " " +
                                  mins + " " +
                                  main.getString(R.string.sleep_timer_set_end);
                        } else msg = main.getString(R.string.sleep_timer_disabled);

                        main.runCtrlrTask(false, Commands.SET_SLEEPTIMER(mins));
                        CoordinatorLayout cl = (CoordinatorLayout) main.findViewById(R.id.coordinator_layout);
                        Snackbar sb = Snackbar.make(cl, msg, Snackbar.LENGTH_LONG);
                        sb.setActionTextColor(Color.RED);
                        if (mins != 0) {
                            sb.setAction(main.getString(R.string.undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    main.runCtrlrTask(false, Commands.SET_SLEEPTIMER(0));
                                    Utils.toastShort(main, R.string.sleep_timer_disabled);
                                }
                            });
                        }
                        View snackbarView = sb.getView();
                        snackbarView.setBackgroundColor(Color.BLACK);
                        sb.show();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                })
                .negativeText(R.string.cancel)
                .show();
    }
}
