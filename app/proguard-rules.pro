# WebView support
-keepclassmembers class * extends android.webkit.WebView {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
}

# Keep SMSReceiver and ForegroundService
-keep class com.lucifer.smsforwarder.** { *; }

# Avoid common warnings
-dontwarn android.webkit.**
