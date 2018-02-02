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

import com.pepperonas.yahama.app.model.movie.movie.Adventure;
import com.pepperonas.yahama.app.model.movie.movie.Drama;
import com.pepperonas.yahama.app.model.movie.movie.MonoMovie;
import com.pepperonas.yahama.app.model.movie.movie.SciFi;
import com.pepperonas.yahama.app.model.movie.movie.Spectacle;
import com.pepperonas.yahama.app.model.movie.movie.Standard;

/**
 * Created by martin on 10.08.2015.
 */
public class ConfigMovie {

    private static final String TAG = "ConfigMovie";

    private Adventure adventure;
    private Drama drama;
    private MonoMovie monoMovie;
    private SciFi sciFi;
    private Spectacle spectacle;
    private Standard standard;


    public ConfigMovie() {
        adventure = new Adventure();
        drama = new Drama();
        monoMovie = new MonoMovie();
        sciFi = new SciFi();
        spectacle = new Spectacle();
        standard = new Standard();
    }


    public String dbgMsg() {
        String msg = "...\n" + adventure.dbgMsg() + "\n" + drama.dbgMsg() + "\n" + monoMovie.dbgMsg() +
                     "\n" + sciFi.dbgMsg() + "\n" + spectacle.dbgMsg() + "\n" + standard.dbgMsg();

        Log.w(TAG, msg);

        return msg;
    }


    public Adventure getAdventure() {
        return adventure;
    }


    public void setAdventure(Adventure adventure) {
        this.adventure = adventure;
    }


    public Drama getDrama() {
        return drama;
    }


    public void setDrama(Drama drama) {
        this.drama = drama;
    }


    public MonoMovie getMonoMovie() {
        return monoMovie;
    }


    public void setMonoMovie(MonoMovie monoMovie) {
        this.monoMovie = monoMovie;
    }


    public SciFi getSciFi() {
        return sciFi;
    }


    public void setSciFi(SciFi sciFi) {
        this.sciFi = sciFi;
    }


    public Spectacle getSpectacle() {
        return spectacle;
    }


    public void setSpectacle(Spectacle spectacle) {
        this.spectacle = spectacle;
    }


    public Standard getStandard() {
        return standard;
    }


    public void setStandard(Standard standard) {
        this.standard = standard;
    }
}
