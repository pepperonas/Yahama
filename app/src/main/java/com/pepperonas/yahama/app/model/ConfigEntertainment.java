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

package com.pepperonas.yahama.app.model;

import android.util.Log;

import com.pepperonas.yahama.app.model.movie.entertainment.ActionGame;
import com.pepperonas.yahama.app.model.movie.entertainment.MusicVideo;
import com.pepperonas.yahama.app.model.movie.entertainment.RoleplayingGame;
import com.pepperonas.yahama.app.model.movie.entertainment.Sports;

/**
 * Created by martin on 10.08.2015.
 */
public class ConfigEntertainment {

    private static final String TAG = "ConfigEntertainment";

    private ActionGame actionGame;
    private MusicVideo musicVideo;
    private RoleplayingGame roleplayingGame;
    private Sports sports;


    public ConfigEntertainment() {
        actionGame = new ActionGame();
        musicVideo = new MusicVideo();
        roleplayingGame = new RoleplayingGame();
        sports = new Sports();
    }


    public String dbgMsg() {
        String msg = "...\n" + actionGame.dbgMsg() + "\n" + musicVideo.dbgMsg() + "\n" + roleplayingGame.dbgMsg() + "\n" + sports.dbgMsg();

        Log.w(TAG, msg);

        return msg;
    }


    public ActionGame getActionGame() {
        return actionGame;
    }


    public void setActionGame(ActionGame actionGame) {
        this.actionGame = actionGame;
    }


    public MusicVideo getMusicVideo() {
        return musicVideo;
    }


    public void setMusicVideo(MusicVideo musicVideo) {
        this.musicVideo = musicVideo;
    }


    public RoleplayingGame getRoleplayingGame() {
        return roleplayingGame;
    }


    public void setRoleplayingGame(RoleplayingGame roleplayingGame) {
        this.roleplayingGame = roleplayingGame;
    }


    public Sports getSports() {
        return sports;
    }


    public void setSports(Sports sports) {
        this.sports = sports;
    }
}
