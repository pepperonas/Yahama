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

import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.fragments.SettingsFragment;
import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogSelectNavDrawerDetail {

    public DialogSelectNavDrawerDetail(final MainActivity main, final SettingsFragment sf) {
        new MaterialDialog.Builder(main)
                .title(R.string.dialog_title_select_nav_drawer_detail)
                .items(R.array.dialog_items_select_nav_drawer_detail)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice
                        (Setup.getNavDrawerDetailPos(),
                         new MaterialDialog.ListCallbackSingleChoice() {
                             @Override
                             public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                 Setup.setNavDrawerDetail(text.toString(), which);
                                 if (MainActivity.getAmp().isOn()) main.runConnectionTask(false);
                                 sf.updateSummaries();
                                 return true;
                             }
                         })
                .show();
    }
}
