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

import android.app.AlertDialog;
import android.content.Intent;
import android.provider.Settings;

import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.utility.Const;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogWifiDisabled {

    boolean mIsShown = false;
    private final MaterialDialog mDialog;

    public DialogWifiDisabled(final MainActivity main) {
        mDialog = new MaterialDialog.Builder(main)
                .title(R.string.dialog_title_check_settings)
                .message(R.string.dialog_content_wifi_disabled)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        main.startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), Const.REQ_CODE_ENABLE_WIFI);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        main.finish();
                    }
                })
                .showListener(new MaterialDialog.ShowListener() {
                    @Override
                    public void onShow(AlertDialog dialog) {
                        super.onShow(dialog);
                        mIsShown = true;
                    }
                })
                .dismissListener(new MaterialDialog.DismissListener() {
                    @Override
                    public void onDismiss() {
                        super.onDismiss();
                        mIsShown = false;
                    }
                }).build();
    }

    public void showDialog() {
        if (!mIsShown) mDialog.show();
    }

}
