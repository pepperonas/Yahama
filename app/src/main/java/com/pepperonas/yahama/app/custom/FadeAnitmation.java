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

package com.pepperonas.yahama.app.custom;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class FadeAnitmation {

    private View view;
    private float maxBrightness = 1.0f;
    private float minBrightness = 0.1f;
    private long duration = 300L;
    private long startOffset = 0L;


    public FadeAnitmation(View view, float maxBrightness, float minBrightness, long duration, long startOffset) {
        this.view = view;
        this.maxBrightness = maxBrightness;
        this.minBrightness = minBrightness;
        this.duration = duration;
        this.startOffset = startOffset;
    }


    public void fadeOut() {
        Animation anim = new AlphaAnimation(maxBrightness, minBrightness);
        anim.setDuration(duration);
        anim.setStartOffset(startOffset);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }


    public void fadeIn() {
        Animation anim = new AlphaAnimation(minBrightness, maxBrightness);
        anim.setDuration(duration);
        anim.setStartOffset(startOffset);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

}
