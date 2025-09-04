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

# Gson rules to prevent TypeToken issues with R8
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Keep Gson classes
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep TypeToken and its generic signatures
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Keep fields with SerializedName annotation
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Keep generic signatures for reflection
-keepattributes Signature
-keep class * extends java.lang.reflect.Type

# Keep our data classes
-keep class com.example.stavropolplacesapp.places.Place { *; }
-keep class com.example.stavropolplacesapp.places.PlacesResponse { *; }
-keep class com.example.stavropolplacesapp.places.PlacesResponseList { *; }
-keep class com.example.stavropolplacesapp.eat.PlaceToEat { *; }
-keep class com.example.stavropolplacesapp.eat.Coordinates { *; }
-keep class com.example.stavropolplacesapp.eat.WorkingDays { *; }
-keep class com.example.stavropolplacesapp.famous_people.Person { *; }
-keep class com.example.stavropolplacesapp.region.Region { *; }

# Keep all data classes in our package
-keep class com.example.stavropolplacesapp.**.data.** { *; }
-keep class com.example.stavropolplacesapp.**.model.** { *; }