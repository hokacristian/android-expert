# Existing rules
-keep class com.hoka.** { *; }
-keepattributes *Annotation*

# Keep the Resource class and its inner classes
-keepclassmembers class com.hoka.core.data.source.Resource {
    *;
}

# Alternatively, keep all inner classes of Resource
-keep,allowobfuscation class com.hoka.core.data.Resource$* {
    *;
}

-keep class kotlin.** { *; }
-keepclassmembers class kotlin.** { *; }
-dontwarn kotlin.**

-keep class androidx.viewbinding.** { *; }
-keepclassmembers class androidx.viewbinding.** { *; }
-dontwarn androidx.viewbinding.**

