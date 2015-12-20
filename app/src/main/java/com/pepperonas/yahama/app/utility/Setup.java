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

package com.pepperonas.yahama.app.utility;

import android.content.Context;

import com.pepperonas.aesprefs.AesPrefs;
import com.pepperonas.andbasx.base.Loader;
import com.pepperonas.yahama.app.R;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class Setup {

    public static int
            PINK_RIPPLE = 0,
            BLUE_RIPPLE = 1,
            GREEN_RIPPLE = 2,
            PURPLE_RIPPLE = 3,
            RED_RIPPLE = 4,
            ORANGE_RIPPLE = 5,
            YELLOW_RIPPLE = 6,
            COLOR_ACCENT = 7,
            COLOR_ACCENT_LIGHT = 8;
    private static boolean showDspInfo;


    public static int getButtonIconColor() {
        return AesPrefs.getInt("btn_icon_color", R.color.action_card_icon_color);
    }


    public static void setButtonIconColor(int colorId) {
        AesPrefs.putInt("btn_icon_color", colorId);
    }


    public static void setNotificationTheme(boolean colorId) {
        AesPrefs.putBoolean("dark_notification", colorId);
    }


    public static int getNotificationIconColor() {
        return AesPrefs.getBoolean("dark_notification", true) ? R.color.notification_icon_color_light
                                                              : R.color.notification_icon_color;
    }


    public static int getNotificationBackground() {
        return Loader.getColor(AesPrefs.getBoolean("dark_notification", true) ? R.color.notification_background_light
                                                                              : R.color.notification_background);
    }


    public static int getFabIconColor() {
        return AesPrefs.getInt("btn_fab_color", R.color.white);
    }


    public static void setFabIconColor(int colorId) {
        AesPrefs.putInt("btn_fab_color", colorId);
    }


    public static int getDialogIconColor() {
        return AesPrefs.getInt("btn_dialog_icon_color", getTheme() == 0 ? R.color.white : R.color.black);
    }


    public static int getLabelCardColor() {
        if (getTheme() == 0) return AesPrefs.getInt("label_card_color", R.color.primaryColor);
        return AesPrefs.getInt("label_card_color", R.color.primaryColor_light);
    }


    public static void setLabelCardColor(int colorId) {
        AesPrefs.putInt("label_card_color", colorId);
    }


    public static int getActionCardColor() {
        return AesPrefs.getInt("action_card_color", R.color.primaryColor);
    }


    public static void setActionCardColor(int colorId) {
        AesPrefs.putInt("action_card_color", colorId);
    }


    public static int getVolumeSteps() {
        return AesPrefs.getInt("vol_step", 5);
    }


    public static void setVolumeSteps(int volStep, int which) {
        AesPrefs.putInt("vol_step", volStep);
        AesPrefs.putInt("vol_step_pos", which);
    }


    public static int getVolumeStepsPos() {
        return AesPrefs.getInt("vol_step_pos", 0);
    }


    public static int getLastKnownByte() {
        return AesPrefs.getInt("last_device_ip", -1);
    }


    public static void setLastKnownByte(int deviceIp) {
        AesPrefs.putInt("last_device_ip", deviceIp);
    }


    public static int getTimeoutForLookup() {
        return AesPrefs.getInt("timeout_lookup", Const.DEFAULT_TIMEOUT_FOR_LOOKUP);
    }


    public static void setTimeoutForLookup(int timeout) {
        AesPrefs.putInt("timeout_lookup", timeout);
    }


    public static String getNavDrawerDetail(Context ctx) {
        return AesPrefs.get("nav_drawer_detail", ctx.getString(R.string.ip_address));
    }


    public static void setNavDrawerDetail(String detail, int which) {
        AesPrefs.put("nav_drawer_detail", detail);
        AesPrefs.putInt("nav_drawer_detail_pos", which);
    }


    public static int getNavDrawerDetailPos() {
        return AesPrefs.getInt("nav_drawer_detail_pos", 0);
    }


    public static int getTheme() {
        return AesPrefs.getInt("theme", 0);
    }


    public static void setTheme(int which) {
        AesPrefs.putInt("theme", which);
    }


    public static boolean getPremium() {
        return AesPrefs.getBoolean("premium_state", false);
    }


    public static void setPremium(boolean b) {
        AesPrefs.putBoolean("premium_state", b);
    }


    public static boolean getShowPremium() {
        return AesPrefs.getBoolean("show_premium", true);
    }


    public static void setShowPremium(boolean b) {
        AesPrefs.putBoolean("show_premium", b);
    }


    public static boolean getShowNotification() {
        return AesPrefs.getBoolean("show_notification", true);
    }


    public static void setShowNotification(boolean b) {
        AesPrefs.putBoolean("show_notification", b);
    }


    public static boolean getNotificationOngoing() { return AesPrefs.getBoolean("notification_ongoing", true); }


    public static void setNotificationOngoing(boolean b) {
        AesPrefs.putBoolean("notification_ongoing", b);
    }


    public static void setInstallationDate(long l) { AesPrefs.putLong("date_inst", l); }


    public static long getInstallationDate() {
        return AesPrefs.getLong("date_inst", -1);
    }


    public static void setAdvertising(boolean b) { AesPrefs.putBoolean("advertising", b); }


    public static boolean getAdvertising() { return AesPrefs.getBoolean("advertising", false); }


    public static void setShowDialogPurchasePremium(boolean b) {AesPrefs.putBoolean("dialog_purchase_premium", b); }


    public static boolean getShowDialogPurchasePremium() { return AesPrefs.getBoolean("dialog_purchase_premium", true); }


    public static void setShowDspInfo(boolean showDspInfo) {
        AesPrefs.putBoolean("show_dsp_info", showDspInfo);
    }


    public static boolean getShowDspInfo() {
        return AesPrefs.getBoolean("show_dsp_info", true);
    }


    public static void setCloseVolumeDialogAutomatically(boolean b) {
        AesPrefs.putBoolean(R.string.PR_KEY_CLOSE_VOL_DIALOG_AUTOMATICALLY, b);
    }


    public static boolean getCloseVolumeDialogAutomatically() {
        return AesPrefs.getBoolean(R.string.PR_KEY_CLOSE_VOL_DIALOG_AUTOMATICALLY, true);
    }
}
