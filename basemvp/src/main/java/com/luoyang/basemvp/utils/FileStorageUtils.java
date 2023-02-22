package com.luoyang.basemvp.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * 手机内部和外部存储
 * 文件工具类
 *
 * @author luoyang
 * @date 2022/12/8
 */

public class FileStorageUtils {

    private final static String TAG = "FileStorageUtils";

    /**
     * 判断主共享/外部存储是否被挂载
     *
     * @return mounted
     */
    public static boolean isStorageMounted() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取主共享/外部存储的根目录
     *
     * @return 绝对根目录
     */
    public static String getStorageBaseDir() {
        if (isStorageMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取主共享/外部存储的完整空间大小
     *
     * @return 手机内存总量，返回MB
     */
    public static long getStorageSize() {
        if (isStorageMounted()) {
            StatFs fs = new StatFs(getStorageBaseDir());
            long count = fs.getBlockCountLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }


    /**
     * 获取主共享/外部存储的剩余空间大小
     *
     * @return 手机剩余内存，返回MB
     */
    public static long getStorageFreeSize() {
        if (isStorageMounted()) {
            StatFs fs = new StatFs(getStorageBaseDir());
            long count = fs.getFreeBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 获取主共享/外部存储的可用空间大小
     *
     * @return 剩余可用控件，返回MB
     */
    public static long getStorageAvailableSize() {
        if (isStorageMounted()) {
            StatFs fs = new StatFs(getStorageBaseDir());
            long count = fs.getAvailableBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }


    /**
     * 往外部存储-公有目录-保存文件
     *
     * @param data     数据
     * @param type     文件类型
     * @param fileName 文件名
     * @return 保存成功与否
     */
    public static boolean saveFileToStoragePublicDir(byte[] data, String type, String fileName) {
        BufferedOutputStream bos = null;
        if (isStorageMounted()) {
            File file = Environment.getExternalStoragePublicDirectory(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * 往外部存储的自定义目录下保存文件
     *
     * @param data     数据
     * @param dir      自定义目录
     * @param fileName 文件名
     * @return 保存成功与否
     */
    public static boolean saveFileToStorageCustomDir(byte[] data, String dir, String fileName) {
        BufferedOutputStream bos = null;
        if (isStorageMounted()) {
            File file = new File(getStorageBaseDir() + File.separator + dir);
            if (!file.exists()) {
                file.mkdirs();// 递归创建自定义目录
            }
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * 往外部存储的私有file目录下保存文件
     *
     * @param data     数据
     * @param type     类型
     * @param fileName 文件名
     * @param context  上下文
     * @return 保存成功与否
     */
    public static boolean saveFileToStoragePrivateFilesDir(byte[] data, String type, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isStorageMounted()) {
            File file = context.getExternalFilesDir(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 往外部存储的私有file目录下保存数据
     * 数据依次添加
     *
     * @param data     数据
     * @param type     类型
     * @param fileName 文件名
     * @param context  上下文
     * @return 保存成功与否
     */
    public static boolean saveDataToStoragePrivateFilesDir(String data, String type, String fileName, Context context) {
        if (isStorageMounted()) {
            File file = context.getExternalFilesDir(type);
            File realFile = new File(file, fileName);
            FileWriter fileWriter = null;
            BufferedWriter bufferedWriter = null;
            try {
                fileWriter = new FileWriter(realFile, true);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(data);
                bufferedWriter.newLine();
                return true;
            } catch (IOException e) {
                Log.d(TAG, " IOException1 :" + e.getMessage());
            } finally {
                try {
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    if (fileWriter != null) {
                        fileWriter.close();
                    }

                } catch (IOException e) {
                    Log.d(TAG, " IOException2 :" + e.getMessage());
                }

            }
        }
        return false;
    }

    /**
     * 删除日期前的文件
     * 清理日志
     *
     * @param date  日期
     * @param file  文件夹
     */
    public static void clearStoragePrivateFilesBeforeDate(Date date, File file) {
        if (isStorageMounted()) {
            if (null == file) {
                return;
            }
            File[] childFiles = file.listFiles();
            if (null == childFiles) {
                return;
            }
            long time = date.getTime();
            for (File childFile : childFiles) {
                if (childFile.isFile() && childFile.lastModified() < time) {
                    childFile.delete();
                }
                if (childFile.isDirectory()) {
                    clearStoragePrivateFilesBeforeDate(date, childFile);
                }
            }
        }
    }

    /**
     * 往外部存储的私有cache目录下保存文件
     *
     * @param data     数据
     * @param fileName 文件名
     * @param context  上下文
     * @return 保存成功与否
     */
    public static boolean saveFileToStoragePrivateCacheDir(byte[] data, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isStorageMounted()) {
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * 获取外部存储公有目录的路径
     *
     * @param type 文件类型
     * @return 公共类型文件目录
     */
    public static String getStoragePublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type).toString();
    }


    /**
     * 获取外部私有Cache目录的路径
     *
     * @param context 上下文
     * @return 私有cache文件目录
     */
    public static String getStoragePrivateCacheDir(Context context) {
        return context.getExternalCacheDir().getAbsolutePath();
    }


    /**
     * 获取外部存储私有Files目录的路径
     *
     * @param context 上下文
     * @param type    – 文件类型
     * @return 私有files文件目录
     */
    public static String getStoragePrivateFilesDir(Context context, String type) {
        return context.getExternalFilesDir(type).getAbsolutePath();
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.isFile();
    }


    /**
     * 从外部存储中删除文件
     *
     * @param filePath 文件名或文件路径
     * @return 移除成功与否
     */
    public static boolean removeFileFromStorage(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                file.delete();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}