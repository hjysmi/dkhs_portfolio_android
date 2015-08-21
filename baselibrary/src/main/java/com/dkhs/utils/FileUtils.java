package com.dkhs.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zwm
 * @version 2.0
 * @ClassName FileUtils
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/21.
 */
public class FileUtils {


    public static final String TAG="FileUtils";

    /**
     * get the external storage file
     *
     * @return the file
     */
    public static File getExternalStorageDir() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * get the external storage file path
     *
     * @return the file path
     */
    public static String getExternalStoragePath() {
        return getExternalStorageDir().getAbsolutePath();
    }

    /**
     * get the external storage state
     *
     * @return
     */
    public static String getExternalStorageState() {
        return Environment.getExternalStorageState();
    }

    /**
     * check the usability of the external storage.
     *
     * @return enable -> true, disable->false
     */
    public static boolean isExternalStorageEnable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;

    }


    public  static boolean copyFile(String srcFileName, String destFileName, boolean reWrite)
    {
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        if(!srcFile.exists()) {
            Log.d(TAG, "copyFile, source file not exist.");
            return false;
        }
        if(!srcFile.isFile()) {
            Log.d(TAG, "copyFile, source file not a file.");
            return false;
        }
        if(!srcFile.canRead()) {
            Log.d(TAG, "copyFile, source file can't read.");
            return false;
        }
        if(destFile.exists() && reWrite){
            Log.d(TAG, "copyFile, before copy File, delete first.");
            destFile.delete();
        }
        if(!destFile.exists() ){
            destFile.getParentFile().mkdirs();
        }

        try {
            InputStream inStream = new FileInputStream(srcFile);
            FileOutputStream outStream = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int byteRead = 0;
            while ((byteRead = inStream.read(buf)) != -1) {
                outStream.write(buf, 0, byteRead);
            }
            outStream.flush();
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "copyFile, success");
        return true;
    }


}
