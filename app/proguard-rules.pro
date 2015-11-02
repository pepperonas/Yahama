# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:/AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.mikepenz.iconics.** { *; }
-keep class com.mikepenz.community_material_typeface_library.CommunityMaterial
-keep class com.mikepenz.fontawesome_typeface_library.FontAwesome
-keep class com.mikepenz.google_material_typeface_library.GoogleMaterial
-keep class com.mikepenz.meteocons_typeface_library.Meteoconcs
-keep class com.mikepenz.octicons_typeface_library.Octicons
-keep class com.google.android.gms** { *; }
-dontwarn com.google.android.gms.**