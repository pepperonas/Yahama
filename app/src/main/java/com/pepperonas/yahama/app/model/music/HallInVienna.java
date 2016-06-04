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

package com.pepperonas.yahama.app.model.music;

import com.pepperonas.yahama.app.model.AmpYaRxV577;
import com.pepperonas.yahama.app.model.entity.AudioRoom;

/**
 * Created by martin on 10.08.2015.
 */
public class HallInVienna extends AudioRoom {


    @Override
    public String getXML_NAME() {
        return AmpYaRxV577.HALL_IN_VIENNA;
    }


    @Override
    public String dbgMsg() {
        return getXML_NAME() + ": dspLvl= " + getDspLvl() + ", initDly=" + getInitDly() + ", roomSize=" + getRoomSize() + ", liveness=" + getLiveness();
    }

}
