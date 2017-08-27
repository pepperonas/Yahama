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

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.RemoteException;

import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.fragments.SettingsFragment;
import com.pepperonas.yahama.app.utility.Const;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogGetPremium {

    public DialogGetPremium(final MainActivity main, final SettingsFragment sf, final Context ctx) {
        new MaterialDialog.Builder(ctx)
                .title(R.string.dialog_title_get_premium)
                .message(R.string.dialog_get_premium_msg)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        try {
                            Bundle buyIntentBundle = sf.getIabService().getBuyIntent(
                                    3,
                                    main.getPackageName(),
                                    "premium",
                                    "inapp",
                                    "bGoa+V7g/yqDXvKRqq+JThzv+zcvlzTlz546tvc§zHGztfrr/khu");

                            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

                            main.startIntentSenderForResult(
                                    pendingIntent.getIntentSender(),
                                    Const.REQ_CODE_PREMIUM_PURCHASE,
                                    new Intent(), 0, 0, 0);

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                })
                .show();

    }
}
