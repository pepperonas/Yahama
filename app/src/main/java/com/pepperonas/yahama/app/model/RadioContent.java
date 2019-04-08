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

import android.graphics.drawable.Drawable;

/**
 * Created by martin on 13.08.2015.
 */
public class RadioContent {

    private Drawable icon;
    private boolean isFavorite = false;
    private String name = "";

    public RadioContent(String name) {
        this.name = name;
    }

    public RadioContent(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public RadioContent(String name, boolean isFavorite, Drawable icon) {
        this.name = name;
        this.isFavorite = isFavorite;
        this.icon = icon;
    }


    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
