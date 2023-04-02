package com.luoyang.llyreader.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.content.PermissionChecker;

import com.luoyang.basemvp.utils.LogUtils;

/**
 * 权限工具
 *
 * @author luoyang
 * @date 2023/3/2
 */
public class PermissionCheck {
    public static Boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (getTargetSdkVersion(context) >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = context.checkSelfPermission(permission)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            result = PermissionChecker.checkSelfPermission(context, permission)
                    == PermissionChecker.PERMISSION_GRANTED;
        }
        return result;
    }

    private static int getTargetSdkVersion(Context context) {
        int version = 0;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            version = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static void requestPermissionSetting(Context from) {
        try {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", from.getPackageName(), null));
            from.startActivity(localIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 访问文件读写外部文件权限，需要去特定界面申请
     *
     * @param activity 界面
     */
    public static void requestFilePermission(Activity activity) {
        LogUtils.INSTANCE.e("requestPermission Build.VERSION.SDK_INT: " +Build.VERSION.SDK_INT);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse(String.format("package:%s", activity.getPackageName())));
                activity.startActivityForResult(intent, Activity.RESULT_CANCELED);
            }
        } catch (Exception e) {
            LogUtils.INSTANCE.e("requestPermission Exception: " + e.getMessage());
        }
    }
}
