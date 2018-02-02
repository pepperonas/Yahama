/*
 * Copyright (c) 2018 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pepperonas.yahama.app.dialogs;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.fragments.SettingsFragment;
import com.pepperonas.yahama.app.utility.Setup;
import com.pepperonas.yahama.app.utils.Utils;

import java.util.Calendar;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogPromotion {

    private static final String TAG = "DialogPromotion";

    public DialogPromotion(final SettingsFragment sf, final Context ctx) {
        new MaterialDialog.Builder(ctx)
                .title(R.string.dialog_title_promotion)
                .customView(R.layout.dialog_promo)
                .cancelable(false)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        EditText etPromoCode = dialog.findViewById(R.id.et_promo);
                        checkPromoCode(sf, etPromoCode, dialog);
                        super.onPositive(dialog);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void checkPromoCode(SettingsFragment sf, EditText etCode, MaterialDialog matDia) {

        if (etCode.getText().toString().isEmpty()) {
            matDia.dismiss();
            return;
        }

        Log.i(TAG, "checkPromoCode: " + etCode.getText());

        String input = etCode.getText().toString(), xtra = "", code;

        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        switch (dayOfYear % 7) {
            case 0:
                xtra = "_mof";
                break;
            case 1:
                xtra = "_xda";
                break;
            case 2:
                xtra = "_tro";
                break;
            case 3:
                xtra = "_alp";
                break;
            case 4:
                xtra = "_yps";
                break;
            case 5:
                xtra = "_nyc";
                break;
            case 6:
                xtra = "_gto";
                break;
        }

        code = String.valueOf((String.valueOf((dayOfYear * 299)) + xtra).hashCode()).replace("-", "");
        if (input.equals(code)) {
            // freischalten
            Setup.setPremium(true);
            Utils.toastLong(sf.getActivity(), R.string.premium_unlocked);
            sf.updateSummaries();
            matDia.dismiss();

        } else {
            matDia.dismiss();
        }
    }

}
