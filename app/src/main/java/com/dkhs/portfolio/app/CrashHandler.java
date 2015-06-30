/**
 * @Title: CrashHandler.java
 * @Package com.naerju.application
 * @Description: TODO(用一句话描述该文件做什么)
 * @author think4
 * @date 2014-4-25 下午1:47:26
 * @version V1.0
 */
package com.dkhs.portfolio.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhoujunzhou
 * @version 1.0
 * @ClassName: CrashHandler
 * @Description: 全局异常处理
 * @date 2014-4-25 下午1:47:26
 */
public class CrashHandler implements UncaughtExceptionHandler {
    /**
     * Debug Log tag
     */
    public static final String TAG = "CrashHandler";
    private Context mContext;
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // private static final String ErrorReportName = "crash-";
    private static String ErrorReportName = "crash-";
    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".txt";
    /**
     * 错误报告的文件长度 500K
     */
    private static final long FILELEGTH = 1024 * 500;

    /**
     * 保存设备的信息和错误堆栈信息
     */
    private static String VERSION_NAME = "versionName: ";
    private static String VERSION_CODE = "versionCode: ";
    private static String STACK_TRACE = "STACK_TRACE: ";
    private static String PACKAGENAME = "packageName: ";
    /**
     * 基带
     */
    private static String BASEBAND = "baseBand: ";
    /**
     * 設備名
     */
    private static String MODEL = "model: ";
    /**
     * 设备厂商名
     */
    private static String BRAND = "brand: ";
    /**
     * 设备的SDK版本号
     */
    private static String SDKVERSION = "sdkversion: ";

    private static StringBuffer crashinfo;

    private volatile static CrashHandler crashhandler;

    private CrashHandler() {
    }

    ;

    public static CrashHandler getInstance(Context ctx) {

        if (crashhandler == null) {
            synchronized (CrashHandler.class) {
                if (crashhandler == null) {
                    crashhandler = new CrashHandler();
                    crashhandler.init(ctx);
                }
            }
        }
        return crashhandler;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        crashinfo = new StringBuffer();
        makeRootDirectory(errorLogFile);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.i("CrashHandler", "come into uncaughtException");
        if (!handleException(ex) && null != mDefaultHandler) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                // Thread.sleep(3000)；来让线程停止一会是为了显示Toast信息给用户，然后Kill。
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // Logger.e(TAG, "Error : "+getStackTraceString(e));
            }
            mDefaultHandler.uncaughtException(thread, ex);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);

        }
    }

    /**
     * @param @param ex
     * @Title: handleException
     * @Description: 自定义异常处理方法
     */
    private boolean handleException(final Throwable ex) {
        Log.i("CrashHandler", "come into handleException");

        if (null == ex) {
            return true;
        }
        final String msg = ex.getLocalizedMessage();
        // 使用Toast来显示异常信息
        // 除了Toast外，还可以选择使用Notification来显示错误内容并让用户选择是否提交错误报告而不是自动提交。
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出错了：" + msg, Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            ;
        }.start();

        // 收集设备信息
        collectCrashDeviceInfo(mContext);
        // 保存错误报告文件
        saveCrashInfoToFile(ex);

        // 发送错误报告到服务器
        sendCrashReportsToServer(mContext);

        return true;
    }

    /**
     * 把错误报告发送给服务器.
     *
     * @param ctx
     */
    public void sendCrashReportsToServer(Context ctx) {

        Log.i("CrashHandler", "come into sendCrashReportToServer");
        String filePath = Environment.getExternalStorageDirectory().getPath() + "portfolio/";

        final File cr = new File(filePath, ErrorReportName);
        if (cr.length() >= FILELEGTH) {
            Log.i("CrashHandler", "sendCrashReportToServer");
            new Thread() {
                public void run() {
                    postReport(cr);
                    // cr.delete(); // 删除已发送的报告
                }

                ;
            }.start();
        }

    }

    private void postReport(File file) {

        /*
         * DefaultHttpClient httpClient = new DefaultHttpClient();
         * HttpPost httpPost = new HttpPost(G.URL);
         * List<NameValuePair> nvps = new ArrayList<NameValuePair>();
         * nvps.add(new BasicNameValuePair("package_name", G.APP_PACKAGE));
         * nvps.add(new BasicNameValuePair("package_version", version));
         * nvps.add(new BasicNameValuePair("phone_model", phoneModel));
         * nvps.add(new BasicNameValuePair("android_version", androidVersion));
         * nvps.add(new BasicNameValuePair("stacktrace", stacktrace));
         * httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
         * // We don't care about the response, so we just hope it went
         * // well and on with it
         * httpClient.execute(httpPost);
         */
    }

    /**
     * 获取错误报告文件名
     *
     * @param ctx
     * @return
     */
    private String[] getCrashReportFiles(Context ctx) {
        File filesDir = ctx.getFilesDir();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(ErrorReportName);
            }
        };
        return filesDir.list(filter);
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */

    private final static String errorLogFile = Environment.getExternalStorageDirectory() + "/CrashLog/";

    private void saveCrashInfoToFile(Throwable ex) {

        Log.i("CrashHandler", "saveCrashInfoToFile");
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        printWriter.close();
        crashinfo.append(STACK_TRACE).append(result).append("\n \n");
        Log.i("STACK_TRACE", result);
        try {
            // long timestamp = System.currentTimeMillis();
            // String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;
            // FileOutputStream trace = mContext.openFileOutput(ErrorReportName, Context.MODE_APPEND);
            // trace.write(crashinfo.toString().getBytes());
            // trace.flush();
            // trace.close();
            writeToFile(crashinfo);

        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
        }
    }

    public static void writeToFile(StringBuffer pData) throws IOException {

        // getFilePath(errorLogFile, ErrorReportName);
        long timestamp = System.currentTimeMillis();
        ErrorReportName = ErrorReportName + timestamp + CRASH_REPORTER_EXTENSION;
        BufferedWriter out = new BufferedWriter(new FileWriter(getFilePath(errorLogFile, ErrorReportName).toString()));
        out.write(pData.toString());
        out.flush();
        out.close();
    }

    public static File getFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx) {
        Log.i("CrashHandler", "come into collectCrashDeviceInfo");
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                crashinfo.append(VERSION_NAME).append(pi.versionName == null ? "not set" : pi.versionName).append("\n");
                crashinfo.append(VERSION_CODE).append(String.valueOf(pi.versionCode)).append("\n");
                crashinfo.append(PACKAGENAME).append(pi.packageName).append("\n");
                crashinfo.append(BRAND).append(Build.BRAND == null ? "unknown" : Build.BRAND).append("\n");
                crashinfo.append(MODEL).append(Build.MODEL == null ? "unknown" : Build.MODEL).append("\n");
                crashinfo.append(BASEBAND).append(getBaseBand()).append("\n");
                crashinfo.append(SDKVERSION).append(Build.VERSION.RELEASE == null ? "unknown" : Build.VERSION.RELEASE).append("\n");
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
        }
    }

    /**
     * 获取手机基带信息
     *
     * @return String 基带信息
     */
    private static String getBaseBand() {
        String baseband = "unknow";
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Object invoker = clazz.newInstance();
            Method method = clazz.getMethod("get", new Class[]{String.class, String.class});
            Object result = method.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            baseband = (String) result;
            // Toast.makeText(this, (String)result, Toast.LENGTH_LONG).show();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return baseband;
    }
}
