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

package com.pepperonas.yahama.app.model.movie.movie;

import com.pepperonas.yahama.app.model.AmpYaRxV577;
import com.pepperonas.yahama.app.model.entity.BaseEntity;

/**
 * Created by martin on 10.08.2015.
 */
public class Standard extends BaseEntity {

    // TODO: Defaults..
    private int dspLvl = -2;
    private int surInitDly = 15;
    private int surRoomSize = 10;
    private int surLiveness = 5;
    private int surBackInitDly = 10;
    private int surBackRoomSize = 9;
    private int surBackLiveness = 9;


    public String getXML_NAME() { return AmpYaRxV577.STANDARD; }


    public String dbgMsg() {
        return getXML_NAME() + ": dspLvl= " + getDspLvl() + ", surInitDly=" + getSurInitDly() +
               ", surRoomSize=" + getSurRoomSize() + ", surLiveness=" + getSurLiveness() +
               ", surBackInitDly=" + getSurBackInitDly() + ", surBackRoomSize=" + getSurBackRoomSize() +
               ", surBackLiveness=" + getSurBackLiveness();
    }


    public int getDspLvl() {
        return dspLvl;
    }


    public void setDspLvl(int dspLvl) {
        this.dspLvl = dspLvl;
    }


    public int getSurInitDly() {
        return surInitDly;
    }


    public void setSurInitDly(int surInitDly) {
        this.surInitDly = surInitDly;
    }


    public int getSurRoomSize() {
        return surRoomSize;
    }


    public void setSurRoomSize(int surRoomSize) {
        this.surRoomSize = surRoomSize;
    }


    public int getSurLiveness() {
        return surLiveness;
    }


    public void setSurLiveness(int surLiveness) {
        this.surLiveness = surLiveness;
    }


    public int getSurBackInitDly() {
        return surBackInitDly;
    }


    public void setSurBackInitDly(int surBackInitDly) {
        this.surBackInitDly = surBackInitDly;
    }


    public int getSurBackRoomSize() {
        return surBackRoomSize;
    }


    public void setSurBackRoomSize(int surBackRoomSize) {
        this.surBackRoomSize = surBackRoomSize;
    }


    public int getSurBackLiveness() {
        return surBackLiveness;
    }


    public void setSurBackLiveness(int surBackLiveness) {
        this.surBackLiveness = surBackLiveness;
    }



}
