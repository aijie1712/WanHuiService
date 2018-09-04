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
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
# ument
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.edu.online.onlineeducationapp.R$*{
public static final int *;
}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }
# 分享
-dontusemixedcaseclassnames
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
     -keepattributes SourceFile,LineNumberTable

     -keep public interface com.facebook.**
     -keep public interface com.tencent.**
     -keep public interface com.umeng.socialize.**
     -keep public interface com.umeng.socialize.sensor.**
     -keep public interface com.umeng.scrshot.**
     -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
     -keep public class com.umeng.socialize.* {*;}


     -keep class com.facebook.**
     -keep class com.facebook.** { *; }
     -keep class com.umeng.scrshot.**
     -keep public class com.tencent.** {*;}
     -keep class com.umeng.socialize.sensor.**
     -keep class com.umeng.socialize.handler.**
     -keep class com.umeng.socialize.handler.*
     -keep class com.umeng.weixin.handler.**
     -keep class com.umeng.weixin.handler.*
     -keep class com.umeng.qq.handler.**
     -keep class com.umeng.qq.handler.*
     -keep class UMMoreHandler{*;}
     -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
     -keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
     -keep class im.yixin.sdk.api.YXMessage {*;}
     -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
     -keep class com.tencent.mm.sdk.** {
      *;
     }
     -keep class com.tencent.mm.opensdk.** {
    *;
     }
     -keep class com.tencent.wxop.** {
     *;
     }
     -dontwarn twitter4j.**
     -keep class twitter4j.** { *; }

     -keep class com.tencent.** {*;}
     -dontwarn com.tencent.**
     -keep public class com.umeng.com.umeng.soexample.R$*{
     public static final int *;
     }
-keep public class com.linkedin.android.mobilesdk.R$*{
public static final int *;
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keepattributes Signature


#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

# butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keep public class * extends javax.annotation.processing.AbstractProcessor
-keep public class * extends com.sun.tools.javac.tree.TreeScanner
-keep public class javax.annotation.processing.AbstractProcessor
-keep public class com.sun.tools.javac.tree.TreeScanner
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#Fresco
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#litepal
-keep class org.litepal.** {
    *;
}

-keep class * extends org.litepal.crud.DataSupport {
    *;
}

-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# 其他
-ignorewarnings
-keep class * {
    public private *;
}
# 高德定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#高德搜索
-keep class com.amap.api.services.**{*;}
#聊天部分
-keep class intelligent.cmeplaza.com.socket.bean.**{*;}
-keep class intelligent.cmeplaza.com.socket.bean.response.**{*;}
# Arouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider

#华为推送
#-ignorewarning

-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

-keep class com.huawei.hms.**{*;}

#OnePush
-dontoptimize
-dontpreverify
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn com.peng.one.push.**
-dontwarn com.igexin.**
-dontwarn cn.jpush.**
-dontwarn cn.jiguang.**
-keepattributes *Annotation*

-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-keep class cn.jiguang.** { *; }
-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class com.hianalytics.android.** {*;}
-keep class com.meizu.cloud.**{*;}
-keep class org.apache.thrift.** {*;}
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }
-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }

 # OnePush的混淆
-keep class * extends com.peng.one.push.core.IPushClient

# AgentWeb
-keep class com.just.agentweb.** {
    *;
}
-dontwarn com.just.agentweb.**
-keepclassmembers class com.just.agentweb.sample.common.AndroidInterface{ *; }
-keepclassmembers class com.cme.coreuimodule.base.web.AndroidInterface{ *; }