package cn.javava.shenma.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;


import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.javava.shenma.app.App;

/**
 * Description: 系统工具类
 * Creator: Li'O
 * date: 2016/9/7 10:16
 */
public class SystemUtil {

    private static final String TAG = SystemUtil.class.getSimpleName();


//    public static String Encrypt(String sSrc) throws Exception {
//        SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
//        byte[] ivbytes =ivStr.getBytes();
//        IvParameterSpec iv = new IvParameterSpec(ivbytes);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
//        byte[] data3 = new byte[ivbytes.length+encrypted.length];
//        System.arraycopy(ivbytes,0,data3,0,ivbytes.length);
//        System.arraycopy(encrypted,0,data3,ivbytes.length,encrypted.length);
//        return new String(Base64.encode(data3, Base64.DEFAULT),"utf-8");//此处使用BASE64做转码功能，同时能起到2次加密的作用。
//    }

    /**
     * 跳转到WIFI设置
     *
     * @param context
     */
    public static void intentWifiSetting(Context context) {
        if (Build.VERSION.SDK_INT > 10) {
            // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
            context.startActivity(new Intent(Settings.ACTION_SETTINGS));
        } else {
            context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    public static boolean isAppForeground() {
        ActivityManager activityManager = (ActivityManager) App.getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(App.getInstance().getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * 移动网络开关
     */
    public static void toggleMobileData(Context context, boolean enabled) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> conMgrClass = null; // ConnectivityManager类
        Field iConMgrField = null; // ConnectivityManager类中的字段
        Object iConMgr = null; // IConnectivityManager类的引用
        Class<?> iConMgrClass = null; // IConnectivityManager类
        Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法
        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.getClass().getName());
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass.getDeclaredField("mService");
            // 设置mService可访问
            iConMgrField.setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(conMgr);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            // 设置setMobileDataEnabled方法可访问
            setMobileDataEnabledMethod.setAccessible(true);
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * GPS开关 当前若关则打，当前若开则关
     */
    public static void toggleGPS(Context context) {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }

    }

    /**
     * 调节系统音量
     *
     * @param context
     */
    public static void holdSystemAudio(Context context) {
        AudioManager audiomanage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 获取系统最大音量
        // int maxVolume =
        // audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取当前音量
        int currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_RING);
        // 设置音量
        audiomanage.setStreamVolume(AudioManager.STREAM_SYSTEM, currentVolume, AudioManager.FLAG_PLAY_SOUND);

        // 调节音量
        // ADJUST_RAISE 增大音量，与音量键功能相同
        // ADJUST_LOWER 降低音量
        audiomanage.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

    }

    /**
     * 设置亮度（每30递增）
     *
     * @param activity
     */
    public static void setBrightness(Activity activity) {
        ContentResolver resolver = activity.getContentResolver();
        Uri uri = Settings.System.getUriFor("screen_brightness");
        int nowScreenBri = getScreenBrightness(activity);
        nowScreenBri = nowScreenBri <= 225 ? nowScreenBri + 30 : 30;
        System.out.println("nowScreenBri==" + nowScreenBri);
        Settings.System.putInt(resolver, "screen_brightness", nowScreenBri);
        resolver.notifyChange(uri, null);
    }

    /**
     * 获取屏幕的亮度
     *
     * @param activity
     * @return
     */
    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * 跳转到系统设置
     *
     * @param context
     */
    public static void intentSetting(Context context) {
        String pkg = "com.android.settings";
        String cls = "com.android.settings.Settings";

        ComponentName component = new ComponentName(pkg, cls);
        Intent intent = new Intent();
        intent.setComponent(component);

        context.startActivity(intent);
    }

    /**
     * 获取文件夹下所以的文件
     *
     * @param path
     * @return
     */
    public static ArrayList<File> getFilesArray(String path) {
        File file = new File(path);
        File files[] = file.listFiles();
        ArrayList<File> listFile = new ArrayList<File>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    listFile.add(files[i]);
                }
                if (files[i].isDirectory()) {
                    listFile.addAll(getFilesArray(files[i].toString()));
                }
            }
        }
        return listFile;
    }

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的，这样会节省内存
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        // System.out.println("w"+bitmap.getWidth());
        // System.out.println("h"+bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 打开视频文件
     *
     * @param context
     * @param file    视频文件
     */
    public static void intentVideo(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "video/*";
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    /**
     * 判断网络是否可用
     * <p>
     * This method requires the caller to hold the permission
     * {@link android.Manifest.permission#ACCESS_NETWORK_STATE}.
     *
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /**
     * 网络连接类型
     *
     * @param context
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String getAPN(Context context) {
        String apn = "";
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null) {
            if (ConnectivityManager.TYPE_WIFI == info.getType()) {
                apn = info.getTypeName();
                if (apn == null) {
                    apn = "wifi";
                }
            } else {
                apn = info.getExtraInfo().toLowerCase();
                if (apn == null) {
                    apn = "mobileNo";
                }
            }
        }
        return apn;
    }

    public static String getModel(Context context) {
        return Build.MODEL;
    }

    /**
     * 获取制造商
     *
     * @param context
     * @return
     */
    public static String getManufacturer(Context context) {
        return Build.MANUFACTURER;
    }

    /**
     * 获取固件版本
     *
     * @param context
     * @return
     */
    public static String getFirmware(Context context) {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取SDK版本
     *
     * @return
     */
    public static String getSDKVer() {
        return Integer.valueOf(Build.VERSION.SDK_INT).toString();
    }

    /**
     * 获取当前语言
     *
     * @return
     */
    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        String languageCode = locale.getLanguage();
        if (TextUtils.isEmpty(languageCode)) {
            languageCode = "";
        }
        return languageCode;
    }

    /**
     * 获取国家代码
     *
     * @return
     */
    public static String getCountry() {
        Locale locale = Locale.getDefault();
        String countryCode = locale.getCountry();
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = "";
        }
        return countryCode;
    }

    /**
     * 获取imei
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        if (TextUtils.isEmpty(imei) || imei.equals("000000000000000")) {
            imei = "0";
        }
        return imei;
    }

    /**
     * getIMSI</br> Requires Permission:
     * {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        if (TextUtils.isEmpty(imsi)) {
            return "0";
        } else {
            return imsi;
        }
    }

    /**
     * 获取当前网络类型
     *
     * @param context
     * @return
     */
    public static String getMcnc(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String mcnc = tm.getNetworkOperator();
        if (TextUtils.isEmpty(mcnc)) {
            return "0";
        } else {
            return mcnc;
        }
    }


    /**
     * 获取<meta-data>元素的数据
     *
     * @param context
     * @param keyName
     * @return
     */
    public static Object getMetaData(Context context, String keyName) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = info.metaData;
            Object value = bundle.get(keyName);
            return value;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取App版本
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = pi.versionName;
            return versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取注册号码
     *
     * @param context
     * @return
     */
    public static String getSerialNumber(Context context) {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
            if (serial == null || serial.trim().length() <= 0) {
                TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                serial = tManager.getDeviceId();
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return serial;
    }

    /**
     * 判断SD卡是否存
     *
     * @return
     */
    public static boolean getSDcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取屏幕像素
     *
     * @param activity
     * @return
     */
    public static String screenSize(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int W = mDisplayMetrics.widthPixels;
        int H = mDisplayMetrics.heightPixels;
        return H + "x" + W;
    }

    /**
     * 获取当前网络连接类型
     *
     * @param mContext
     * @return
     */
    public static String GetNetworkType(Context mContext) {
        String strNetworkType = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by
                        // 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by
                        // 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by
                        // 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by
                        // 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by
                        // 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA")
                                || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
            }
        }
        return strNetworkType;
    }

    /**
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 获取系统版本x.x.x
     * @param context
     * @return
     */
    public static String getVersionName(Context context){
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo=context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            versionName=packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    /**
     * 获取系统版本z
     * @param context
     * @return
     */
    public static int getVersionCode(Context context){
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo=context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            versionCode=packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getDeviceId(Context context){
        TelephonyManager telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei=telephonyManager.getDeviceId();
//        imei=MD5Util.getMD5Code(imei);
//        Log.e("lzh2017","imei="+imei);
        return imei;
    }

    public static String getMacAddress(){
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth0");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "020000000002";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "020000000002";
        }
        Log.e("lzh2017","macAddress="+macAddress);
        return macAddress.replace(":","");
    }
    public static String getAndroidId(Context context){
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);

//        androidId=MD5Util.getMD5Code(androidId);
//        Log.e("lzh2017","ANDROID_ID="+androidId);
        return androidId;
    }
}
