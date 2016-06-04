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

package com.pepperonas.yahama.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pepperonas.yahama.app.utility.Setup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class NotificationActivity extends Activity {

    private static final String TAG = "NotifRtSlt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Setup.getTheme() != 0) {
            setTheme(R.style.Transparent_Light);
        }

        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            String action = getIntent().getStringExtra("notification_filter");
            if (action.equalsIgnoreCase("launch")) {
                Log.i(TAG, "launch");
                launchApp();
            }
        }
    }


    private void launchApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}
