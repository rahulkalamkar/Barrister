# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\rahul.kalamkar\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn retrofit2.Platform$Java8
-dontwarn com.squareup.okhttp3.**
-dontwarn com.squareup.okhttp.**

-dontwarn okhttp3.**
-dontwarn okio.**

-keepclasseswithmembers public class * {
    @retrofit2.http.* <methods>;
}

-keep class retrofit2.* { *; }

-keep public class okhttp3.** { *; }
-keep public interface okhttp3.** { *; }
-keepattributes *Annotation*,Signature
