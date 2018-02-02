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

import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.fragments.SettingsFragment;
import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogPurchasePremium {

    public DialogPurchasePremium(final MainActivity main) {

        new MaterialDialog.Builder(main)
                .title(R.string.dialog_title_test_phase_expired)
                .message(R.string.dialog_content_test_phase_expired)
                .positiveText(R.string.dialog_learn_more)
                .neutralText(R.string.dialog_not_now)
                .negativeText(R.string.dialog_no_thanks)
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        main.selectNavViewItem(main.getNavView().getMenu().getItem(1).getSubMenu().getItem(0));

                        if (main.getActiveFragment() instanceof SettingsFragment) {
                            new DialogGetPremium(main, (SettingsFragment) main.getActiveFragment(), main);
                        }

                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        Setup.setShowDialogPurchasePremium(false);
                    }
                })
                .show();

    }
}
