
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int e(...);
    public static int v(...);
    public static int w(...);
}

-dontwarn android.support.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class android.drm.** { *; }
-dontwarn android.drm.**
-keep class com.google.** { *; }
-dontwarn com.google.**

-keep class com.pepperonas.materialdialog.** { *; }
-dontwarn com.pepperonas.materialdialog.**
-keep class com.pepperonas.andbasx.** { *; }
-dontwarn com.pepperonas.andbasx.**
-keep class com.pepperonas.jbasx.** { *; }
-dontwarn com.pepperonas.jbasx.**
-keep class com.pepperonas.aespreferences.** { *; }
-dontwarn com.pepperonas.aespreferences.**