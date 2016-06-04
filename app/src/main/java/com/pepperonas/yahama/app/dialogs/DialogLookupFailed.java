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

import com.afollestad.materialdialogs.MaterialDialog;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogLookupFailed {

    public DialogLookupFailed(final MainActivity main) {
        new MaterialDialog.Builder(main)
                .title(R.string.dialog_title_lookup_failed)
                .content(R.string.dialog_content_lookup_failed)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        main.recreate();
                    }


                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        main.finish();
                    }
                })
                .show();
    }
}
