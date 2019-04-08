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

package com.pepperonas.yahama.app.model.entity;

/**
 * Created by martin on 10.08.2015.
 */
public abstract class AudioRoom extends AudioEntity {

    private int roomSize = 9;


    public int getRoomSize() {
        return roomSize;
    }


    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }
}
