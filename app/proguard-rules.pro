# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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


# Keep rules for Gson data models in the correct package
# Make sure this path matches ALL your data model classes used by Gson
-keep class dev.bmg.vyasmahabharat.data.model.** { *; }
-keepclassmembers class dev.bmg.vyasmahabharat.data.model.** { *; }

# --- Add these recommended rules too for robustness ---

# Keep default constructors for classes used with Gson (often needed)
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
  <init>(); # Keep default constructor
}

# Keep specific Gson annotations if you use them directly
# -keep @com.google.gson.annotations.SerializedName class *

# General rule for libraries using reflection
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses

# Add rules for other libraries if needed (e.g., Compose, Navigation usually handled by defaults or library artifacts)
