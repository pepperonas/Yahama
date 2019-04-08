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

import android.view.View;

import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.fragments.SettingsFragment;
import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogSelectVolumeSteps {

    public DialogSelectVolumeSteps(final MainActivity main, final SettingsFragment sf) {
        new MaterialDialog.Builder(main)
                .title(null)
                .listItems(true, main.getResources().getStringArray(R.array.dialog_items_select_vol_steps))
                .itemClickListener(new MaterialDialog.ItemClickListener() {
                    @Override
                    public void onClick(View v, int position, long id) {
                        super.onClick(v, position, id);
                        storeValue(position, main.getResources().getStringArray(R.array.dialog_items_select_vol_steps)[position], main);
                        sf.updateSummaries();
                    }
                })
                .show();
    }

    private void storeValue(int which, CharSequence text, MainActivity main) {
        Setup.setVolumeSteps((int) (Float.valueOf(text.toString().replace(" " + main.getString(R.string.dB), "")) * 10), which);
    }
}
