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

package com.pepperonas.yahama.app.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Toast;

import com.pepperonas.yahama.app.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.WIFI_SERVICE;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class Utils {

    private static final String TAG = "Utils";

    public static final int TIME_STRING_NO_DIVIDER = 0, TIME_STRING_FILE = 1, TIME_STRING_GUI = 2;

    public static final int MORNING = 0, AFTERNOON = 1, EVENING = 2, NIGHT = 3;

    public static String getAppVersionCode(Context ctx) {
        try {
            return String.valueOf(ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAppVersionName(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAppInstallLocation(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                return String.valueOf(ctx.getPackageManager().getPackageInfo(
                        ctx.getPackageName(), 0).installLocation);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getAppLastUpdateTime(Context ctx) {
        try {
            return String.valueOf(ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0).lastUpdateTime);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAppPackageName(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean launchApp(Context ctx, String packageName) {
        PackageManager manager = ctx.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            ctx.startActivity(i);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public static boolean launchSpotify(Context ctx) {
        PackageManager manager = ctx.getPackageManager();
        boolean result = false;
        try {
            Intent i = manager.getLaunchIntentForPackage("com.spotify.music");
            if (i == null) {
                i = manager.getLaunchIntentForPackage("play.google.com/store/apps/details?id=com.spotify.music");
            } else {
                result = true;
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            ctx.startActivity(i);
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return result;
        }
    }

    public static void toastShort(Context ctx, int id) {
        Toast.makeText(ctx, ctx.getString(id), Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context ctx, String txt) {
        Toast.makeText(ctx, txt, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context ctx, int id) {
        Toast.makeText(ctx, ctx.getString(id), Toast.LENGTH_LONG).show();
    }

    public static float roundToHalf(float x) {
        return (float) (Math.ceil(x * 2) / 2);
    }

    public static String getValueFromXml(String xmlTag, String param) {
        return (param.split("<" + xmlTag + ">")[1]).split("</" + xmlTag)[0];
    }

    public static boolean isConnected(Context ctx) {
        ConnectivityManager connManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info.isConnected();
    }

    public static String getCurrentNetwork(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ip;
        try {
            ip = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e(TAG, "Unable to get host address.");
            ip = null;
        }

        int subnetLength = ip.lastIndexOf(".");
        String subnet = ip.substring(0, subnetLength) + ".";

        Log.i(TAG, "getCurrentNetwork: " + ip);

        return subnet;
    }

    public static boolean checkMd5(String md5, File updateFile) {
        if (TextUtils.isEmpty(md5) || updateFile == null) {
            Log.e(TAG, "MD5 string empty or updateFile null");
            return false;
        }

        String calculatedDigest = calculateMd5(updateFile);
        if (calculatedDigest == null) {
            Log.e(TAG, "calculatedDigest null");
            return false;
        }

        Log.v(TAG, "Calculated digest: " + calculatedDigest);
        Log.v(TAG, "Provided digest: " + md5);

        return calculatedDigest.equalsIgnoreCase(md5);
    }

    public static String calculateMd5(File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception while getting digest", e);
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Exception while getting FileInputStream", e);
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception on closing MD5 input stream", e);
            }
        }
    }

    public static Bitmap scaleDownBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth = width;
        int newHeight = height;

        if (maxWidth != 0 && newWidth > maxWidth) {
            newHeight = Math.round((float) maxWidth / newWidth * newHeight);
            newWidth = maxWidth;
        }

        if (maxHeight != 0 && newHeight > maxHeight) {
            newWidth = Math.round((float) maxHeight / newHeight * newWidth);
            newHeight = maxHeight;
        }

        return width != newWidth || height != newHeight ? resizeBitmap(bitmap, newWidth, newHeight)
                : bitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
    }

    public static String decodeString(String text) {
        try {
            return new String(Base64.decode(text, Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeString(String text) {
        try {
            return Base64.encodeToString(text.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void errorDialog(Context ctx, Exception e) {
        new AlertDialog.Builder(ctx)
                .setMessage(e.getMessage())
                .setNeutralButton(
                        ctx.getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
    }

    public static void circleAnimate(final View view, int cx, int cy) {
        if (view == null) return;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setVisibility(View.INVISIBLE);

                int finalRadius = Math.max(view.getWidth(), view.getHeight());
                Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                anim.setDuration(500);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                });
                anim.start();
            }
        } catch (IllegalStateException e) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static boolean existFile(String fileName) {
        return new RootFile(fileName).exists();
    }

    public static String readFile(String fileName) {
        return readFile(fileName, true);
    }

    public static String getProp(String key) {
        return RootUtilities.runCommand("getprop " + key);
    }

    public static boolean isPropActive(String key) {
        try {
            return RootUtilities.runCommand("getprop | grep " + key).split("]:")[1].contains("running");
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean hasProp(String key) {
        try {
            return RootUtilities.runCommand("getprop | grep " + key).split("]:").length > 1;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static void writeFile(String path, String text, boolean append) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(path, append);
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            Log.e(TAG, "Failed to write " + path);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String file, boolean root) {
        if (root) return new RootFile(file).readFile();

        StringBuilder s = null;
        FileReader fileReader = null;
        BufferedReader buf = null;
        try {
            fileReader = new FileReader(file);
            buf = new BufferedReader(fileReader);

            String line;
            s = new StringBuilder();
            while ((line = buf.readLine()) != null) s.append(line).append("\n");
        } catch (FileNotFoundException ignored) {
            Log.e(TAG, "File does not exist " + file);
        } catch (IOException e) {
            Log.e(TAG, "Failed to read " + file);
        } finally {
            try {
                if (fileReader != null) fileReader.close();
                if (buf != null) buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s == null ? null : s.toString().trim();
    }

    public static String getExternalStorage() {
        String path = RootUtilities.runCommand("echo ${SECONDARY_STORAGE%%:*}");
        return path.contains("/") ? path : null;
    }

    public static String getInternalStorage() {
        String dataPath = existFile("/data/media/0") ? "/data/media/0" : "/data/media";
        if (!new RootFile(dataPath).isEmpty()) return dataPath;
        if (existFile("/sdcard")) return "/sdcard";
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static void confirmDialog(String title, String msg, DialogInterface.OnClickListener onClickListener,
                                     Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        if (title != null) builder.setTitle(title);
        if (msg != null) builder.setMessage(msg);
        builder.setNegativeButton(ctx.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton(ctx.getString(R.string.ok), onClickListener).show();
    }

    public static String readAssetFile(Context ctx, String file) {
        InputStream input = null;
        BufferedReader buf = null;
        try {
            StringBuilder s = new StringBuilder();
            input = ctx.getAssets().open(file);
            buf = new BufferedReader(new InputStreamReader(input));

            String str;
            while ((str = buf.readLine()) != null) s.append(str).append("\n");
            return s.toString().trim();
        } catch (IOException e) {
            Log.e(TAG, "Unable to read " + file);
        } finally {
            try {
                if (input != null) input.close();
                if (buf != null) buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void setLocale(String lang, Context ctx) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ctx.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    public static String formatCelsius(double celsius) {
        return round(celsius, 2) + "°C";
    }

    public static String celsiusToFahrenheit(double celsius) {
        return round(celsius * 9 / 5 + 32, 2) + "°F";
    }

    public static String round(double value, int places) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < places; i++) stringBuilder.append("#");
        DecimalFormat df = new DecimalFormat("#." + stringBuilder.toString());
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(value);
    }

    public static long stringToLong(String string) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int stringToInt(String string) {
        try {
            return Math.round(Float.parseFloat(string));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void launchUrl(Context ctx, String link) {
        ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    public static int getActionBarHeight(Context ctx) {
        TypedArray ta = ctx.obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        int actionBarSize = ta.getDimensionPixelSize(0, 112);
        ta.recycle();
        return actionBarSize;
    }

    public static boolean isTV(Context ctx) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2
                && ((UiModeManager) ctx.getSystemService(
                Context.UI_MODE_SERVICE)).getCurrentModeType()
                == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    public static boolean isTablet(Context ctx) {
        return (ctx.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int getScreenOrientation(Context ctx) {
        return ctx.getResources().getDisplayMetrics().widthPixels <
                ctx.getResources().getDisplayMetrics().heightPixels ?
                Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;
    }

    public static int mkDiP(Context ctx, int pixel) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        return (int) (metrics.density * pixel);
    }

    public static float getDensity(Context ctx) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static int checkLimits(int max, int arg) {
        boolean neg = arg < 0;
        int abs = Math.abs(arg);
        abs = abs >= max ? max : abs;
        return (neg ? (abs * -1) : abs);
    }

    public static int checkMax(int arg, int maxVal) {
        arg = (arg > maxVal ? maxVal : arg);
        return arg;
    }

    public static double toPercent(double x, double maxX) {return x * (double) 100 / maxX;}

    private double toPercentScaled(double x, double maxX, int scaling) {return x * (double) scaling / maxX;}

    public static boolean stringEquals(String checkIt, String... params) {
        for (String s : params) if (s.equals(checkIt)) return true;
        return false;
    }

    public static boolean stringContains(String checkIt, String... params) {
        for (String s : params) if (s.contains(checkIt)) return true;
        return false;
    }

    public static int setMinMaxWhenOutOfRange(int min, int max, int checkIt) {
        if (checkIt <= min) return min;
        if (checkIt >= max) return max;
        return checkIt;
    }

    public void addValueToHashtable(Hashtable<String, Integer> countable, String s) {
        if (countable.containsKey(s)) {
            countable.put(s, countable.get(s) + 1);
        } else {
            countable.put(s, 1);
        }
    }

    private String resolveHashtableValues(Hashtable<String, Integer> countable) {
        String result = "";
        Iterator it = countable.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            result += (pair.getKey() + "(" + pair.getValue() + "), ");
            it.remove();
        }
        return result;
    }

    /**
     * @param mode: TIME_STRING_NO_DIVIDER for raw representation
     *              TIME_STRING_FILE for file representation
     *              TIME_STRING_GUI for a well looking representation
     */
    public static String timeToString(int mode) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        String d = gregorianCalendar.get(Calendar.YEAR)
                + String.format("%02d", gregorianCalendar.get(Calendar.MONTH))
                + String.format("%02d", gregorianCalendar.get(Calendar.DAY_OF_MONTH))
                + String.format("%02d", gregorianCalendar.get(Calendar.HOUR_OF_DAY))
                + String.format("%02d", gregorianCalendar.get(Calendar.MINUTE))
                + String.format("%02d", gregorianCalendar.get(Calendar.SECOND))
                + String.format("%04d", gregorianCalendar.get(Calendar.MILLISECOND));

        String month = d.substring(4, 6);
        if (month.startsWith("1")) {
            int _month = Integer.parseInt(month);
            _month++;
            month = String.valueOf(_month);
        } else {
            month = month.substring(1);
            int _month = Integer.parseInt(month);
            _month++;
            month = String.format("%02d", _month);
        }

        String divMonth = "", divDateTime = "", divTime = "";

        switch (mode) {
            case TIME_STRING_NO_DIVIDER:
                divMonth = "";
                divDateTime = "";
                divTime = "";
                break;
            case TIME_STRING_FILE:
                divMonth = "_";
                divDateTime = "_";
                divTime = "_";
                break;
            case TIME_STRING_GUI:
                divMonth = ".";
                divDateTime = " - ";
                divTime = ":";
                break;
        }

        return d.substring(6, 8) + divMonth + month + divMonth + d.substring(0, 4) + divDateTime +
                d.substring(8, 10) + divTime + d.substring(10, 12);
    }

    public static int getDayTime() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 6 && timeOfDay < 12) {
            return MORNING;
        } else if (timeOfDay >= 12 && timeOfDay < 18) {
            return AFTERNOON;
        } else if (timeOfDay >= 18 && timeOfDay < 22) return AFTERNOON;

        return NIGHT;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            // Single color bitmap will be created of 1x1 pixel
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //    private void waitForInteraction() {
    //final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Loading.. Wait..", true);
    //
    //// creating a thread to wait until the connection is established
    //Thread t = new Thread() {
    //    @Override
    //    public void run() {
    //        try {
    //            while (false) {
    //                Thread.sleep(300);
    //            }
    //            progressDialog.dismiss();
    //            // restarting main
    //        } catch (Exception e) {
    //            Log.e(TAG, "Error in run - Msg: " + e.getMessage());
    //        }
    //    }
    //};
    //t.start();
    //    }

    public static String buildSummary() {
        return "" +
                "VERSION.RELEASE {" + Build.VERSION.RELEASE + "}" + "\n" +
                "VERSION.INCREMENTAL {" + Build.VERSION.INCREMENTAL + "}" + "\n" +
                "VERSION.SDK {" + Build.VERSION.SDK + "}" + "\n" +
                "MANUFACTURER {" + Build.MANUFACTURER + "}" + "\n" +
                "BRAND {" + Build.BRAND + "}" + "\n" +
                "MODEL {" + Build.MODEL + "}" + "\n" +
                "DEVICE {" + Build.DEVICE + "}" + "\n" +
                "PRODUCT {" + Build.PRODUCT + "}" + "\n" +
                "BOARD {" + Build.BOARD + "}" + "\n" +
                "FINGERPRINT {" + Build.FINGERPRINT + "}" + "\n" +
                "DISPLAY {" + Build.DISPLAY + "}" + "\n" +
                "SERIAL {" + Build.SERIAL + "}" + "\n" +
                "HOST {" + Build.HOST + "}" + "\n" +
                "ID {" + Build.ID + "}";
    }

    /**
     * e.g. '5.1.1'
     */
    public static String getBuildVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * e.g. 'G920FXXU2BOG8'
     */
    public static String getBuildVersionIncremental() {
        return Build.VERSION.INCREMENTAL;
    }

    /**
     * e.g. '22'
     */
    public static String getBuildVersionSdk() {
        return Build.VERSION.SDK;
    }

    /**
     * e.g. 'samsung'
     */
    public static String getBuildManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * e.g. 'samsung'
     */
    public static String getBuildBrand() {
        return Build.BRAND;
    }

    /**
     * e.g. 'SM-G920F'
     */
    public static String getBuildModel() {
        return Build.MODEL;
    }

    /**
     * e.g. 'zeroflte'
     */
    public static String getBuildDevice() {
        return Build.DEVICE;
    }

    /**
     * e.g. 'zerofltexx'
     */
    public static String getBuildProduct() {
        return Build.PRODUCT;
    }

    /**
     * e.g. 'universal7420'
     */
    public static String getBuildBoard() {
        return Build.BOARD;
    }

    /**
     * e.g. 'samsung/zerofltexx/zeroflte:5.1.1/LMY47X/G920FXXU2BOG8:user/release-keys'
     */
    public static String getBuildFingerprint() {
        return Build.FINGERPRINT;
    }

    /**
     * e.g. 'LMY47X.G920FXXU2BOG8'
     */
    public static String getBuildSerialDisplay() {
        return Build.DISPLAY;
    }

    /**
     * e.g. '03157df39a58e10c'
     */
    public static String getBuildSerial() {
        return Build.SERIAL;
    }

    /**
     * e.g. 'SWDE2213'
     */
    public static String getBuildHost() {
        return Build.HOST;
    }

    /**
     * e.g. 'LMY47X'
     */
    public static String getBuildId() {
        return Build.ID;
    }

    public static boolean apiGreaterOrEqual(int target) {
        return Build.VERSION.SDK_INT >= target;
    }

}
