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

package com.pepperonas.yahama.app.model;

import android.util.Log;

import com.pepperonas.yahama.app.model.music.CellarClub;
import com.pepperonas.yahama.app.model.music.TheBottomLine;
import com.pepperonas.yahama.app.model.music.TheRoxyTheatre;

/**
 * Created by martin on 10.08.2015.
 */
public class ConfigLiveClub {

    private static final String TAG = "ConfigLiveClub";

    private CellarClub cellarClub;
    private TheBottomLine theBottomLine;
    private TheRoxyTheatre theRoxyTheatre;


    public ConfigLiveClub() {
        cellarClub = new CellarClub();
        theBottomLine = new TheBottomLine();
        theRoxyTheatre = new TheRoxyTheatre();
    }


    public String dbgMsg() {
        String msg = "...\n" + cellarClub.dbgMsg() + "\n" + theBottomLine.dbgMsg() + "\n" + theRoxyTheatre.dbgMsg();

        Log.w(TAG, msg);

        return msg;
    }


    public CellarClub getCellarClub() {
        return cellarClub;
    }


    public void setCellarClub(CellarClub cellarClub) {
        this.cellarClub = cellarClub;
    }


    public TheRoxyTheatre getTheRoxyTheatre() {
        return theRoxyTheatre;
    }


    public void setTheRoxyTheatre(TheRoxyTheatre theRoxyTheatre) {
        this.theRoxyTheatre = theRoxyTheatre;
    }


    public TheBottomLine getTheBottomLine() {
        return theBottomLine;
    }


    public void setTheBottomLine(TheBottomLine theBottomLine) {
        this.theBottomLine = theBottomLine;
    }
}
