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

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;
import com.pepperonas.aesprefs.AesPrefs;
import com.pepperonas.andbasx.AndBasx;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class YaAmpApp extends Application {

    private Tracker mTracker;


    @Override
    public void onCreate() {
        super.onCreate();

        AesPrefs.init(this, "config", "myaespreferencepassword");

        AndBasx.init(this);

        //only required if you add a custom or generic font on your own
        Iconics.init(getApplicationContext());

        //register custom fonts like this (or also provide a font definition file)
        Iconics.registerFont(new CommunityMaterial());
        Iconics.registerFont(new GoogleMaterial());
    }


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.analytics_global_config);
        }
        return mTracker;
    }

}
