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

package com.pepperonas.yahama.app.fragments;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.pepperonas.yahama.app.utility.Const;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class RecyclerViewOnGestureListener implements GestureDetector.OnGestureListener {

    private static final String TAG = "RcyclrVwOnGstrLstnr";

    private WebradioFragment mWrf;

    private static final int SWIPE_THRESHOLD = 750;
    private static final int SWIPE_VELOCITY_THRESHOLD = 750;

    public RecyclerViewOnGestureListener(WebradioFragment wrf) {
        mWrf = wrf;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown  " + "");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG, "onShowPress  " + "");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp  " + "");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distX, float distY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG, "onLongPress  " + "");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "onFling  " + "");
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                }
                result = true;
            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom();
                } else {
                    onSwipeTop();
                }
            }
            result = true;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void onSwipeRight() {
        mWrf.onOverscrollDetected(Const.OVERSCROLL_LEFT);
    }

    public void onSwipeLeft() {
        mWrf.onOverscrollDetected(Const.OVERSCROLL_RIGHT);
    }

    public void onSwipeTop() {
        mWrf.onOverscrollDetected(Const.OVERSCROLL_BOTTOM);
    }

    public void onSwipeBottom() {
        mWrf.onOverscrollDetected(Const.OVERSCROLL_TOP);
    }


}
