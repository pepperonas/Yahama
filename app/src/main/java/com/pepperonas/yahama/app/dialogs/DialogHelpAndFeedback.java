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

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.yahama.app.R;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class DialogHelpAndFeedback {

    public DialogHelpAndFeedback(final Context ctx) {
        new MaterialDialog.Builder(ctx)
                .title(R.string.u_dialog_title_help_and_feedback)
                .customView(R.layout.dialog_help_and_feedback)
                .positiveText(R.string.ok)
                .showListener(new MaterialDialog.ShowListener() {
                    @Override
                    public void onShow(AlertDialog dialog) {
                        super.onShow(dialog);
                        TextView msg1 = dialog.findViewById(R.id.dialog_help_and_feedback_tv_01);
                        TextView msg2 = dialog.findViewById(R.id.dialog_help_and_feedback_tv_02);
                        TextView msg3 = dialog.findViewById(R.id.dialog_help_and_feedback_tv_03);
                        TextView msg4 = dialog.findViewById(R.id.dialog_help_and_feedback_tv_04);

                        msg1.setText(Html.fromHtml(makeBold(ctx.getString(R.string.app_name)) + " " +
                                ctx.getString(R.string.app_description)));
                        msg1.setMovementMethod(LinkMovementMethod.getInstance());

                        msg2.setText(Html.fromHtml(ctx.getString(R.string.web_presentation_info)));
                        msg2.setMovementMethod(LinkMovementMethod.getInstance());

                        msg3.setText(Html.fromHtml(ctx.getString(R.string.translate_native_info)));
                        msg3.setMovementMethod(LinkMovementMethod.getInstance());

                        msg4.setText(Html.fromHtml(ctx.getString(R.string.copyright)));
                        msg4.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                })
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private String makeBold(String text) {
        return "<b>" + text + "</b>";
    }
}
