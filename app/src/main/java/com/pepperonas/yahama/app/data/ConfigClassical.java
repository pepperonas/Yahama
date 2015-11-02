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

package com.pepperonas.yahama.app.data;

import android.util.Log;

import com.pepperonas.yahama.app.data.music.Chamber;
import com.pepperonas.yahama.app.data.music.HallInMunich;
import com.pepperonas.yahama.app.data.music.HallInVienna;

/**
 * Created by martin on 10.08.2015.
 */
public class ConfigClassical {

    private static final String TAG = "ConfigClassical";

    private HallInMunich hallInMunich;
    private HallInVienna hallInVienna;
    private Chamber chamber;


    public ConfigClassical() {
        hallInMunich = new HallInMunich();
        hallInVienna = new HallInVienna();
        chamber = new Chamber();
    }


    public String dbgMsg() {
        String msg = "...\n" + hallInMunich.dbgMsg() + "\n" + hallInVienna.dbgMsg() + "\n" + chamber.dbgMsg();

        Log.w(TAG, msg);

        return msg;
    }


    public HallInMunich getHallInMunich() {
        return hallInMunich;
    }


    public void setHallInMunich(HallInMunich hallInMunich) {
        this.hallInMunich = hallInMunich;
    }


    public HallInVienna getHallInVienna() {
        return hallInVienna;
    }


    public void setHallInVienna(HallInVienna hallInVienna) {
        this.hallInVienna = hallInVienna;
    }


    public Chamber getChamber() {
        return chamber;
    }


    public void setChamber(Chamber chamber) {
        this.chamber = chamber;
    }
}
