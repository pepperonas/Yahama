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

import android.content.Context;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.utility.Const;
import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogDeviceInfo {

    public DialogDeviceInfo(Context ctx, String dbgMsg) {
        new MaterialDialog.Builder(ctx)
                .title(R.string.dialog_title_device_info)
                .icon(new IconicsDrawable(ctx, GoogleMaterial.Icon.gmd_info_outline)
                        .colorRes(Setup.getDialogIconColor()).sizeDp(Const.DIALOG_ICON_SIZE))
                .message(dbgMsg)
                .positiveText(R.string.ok)
                .show();
    }

}
