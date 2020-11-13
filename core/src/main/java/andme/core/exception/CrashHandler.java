//package andme.core.exception;
//
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.os.Build;
//import android.util.Log;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.lang.Thread.UncaughtExceptionHandler;
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.Map;
//
//import andmex.core.util.AMDevice;
//import halo.android.integration.converter.FastJsonKt;
//import ucux.app.utils.AppUtil;
//import ucux.core.content.file.LogManager;
//import ucux.core.util.LogUtil;
//
///**
// * 未处理异常 处理类
// *
// * @author 罗超
// * @date 2015-8-11 下午2:43:09
// */
//public class CrashHandler implements UncaughtExceptionHandler {
//
//    public static final String TAG = "CrashHandler";
//
//    // CrashHandler 实例
//    private static CrashHandler INSTANCE = new CrashHandler();
//
//
//    // 用来存储设备信息和异常信息
//    private Map<String, String> infos = new HashMap<String, String>();
//
//    Context mContext;
//    UncaughtExceptionHandler mDefaultHandler;
//
//    /**
//     * 保证只有一个 CrashHandler 实例
//     */
//    private CrashHandler() {
//    }
//
//    /**
//     * 获取 CrashHandler 实例 ,单例模式
//     */
//    public static CrashHandler getInstance() {
//        return INSTANCE;
//    }
//
//    /**
//     * 初始化
//     *
//     * @param context
//     */
//    public void init(Context context) {
//        mContext = context;
//
//        // 获取系统默认的 UncaughtException 处理器
//        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
//
//        // 设置该 CrashHandler 为程序的默认处理器
//        Thread.setDefaultUncaughtExceptionHandler(this);
//    }
//
//    @Override
//    public void uncaughtException(Thread thread, Throwable ex) {
//        ex.printStackTrace();
//        if (!handleException(ex) && mDefaultHandler != null) {
//            // 如果用户没有处理则让系统默认的异常处理器来处理
//            mDefaultHandler.uncaughtException(thread, ex);
//        } else {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                Log.e(TAG, "error : ", e);
//            }
//
//            // 退出程序,注释下面的重启启动程序代码
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
//
////			// 重新启动程序，注释上面的退出程序
////			Intent intent = new Intent();
////			intent.setClass(mContext, GuidActivity.class);
////			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////			mContext.startActivity(intent);
////			android.os.Process.killProcess(android.os.Process.myPid());
//        }
//    }
//
//    /**
//     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
//     *
//     * @param ex
//     * @return true：如果处理了该异常信息；否则返回 false
//     */
//    private boolean handleException(Throwable ex) {
////
////        UXApp.instance().logoutForTokenInvalid(false, null);
//
//        //TODO 关掉数据库
//        //里面数据没有获取到的原因是因为获取到了默认数据库中的数据
//        if (ex == null) {
//            return false;
//        }
//
//        // 使用 Toast 来显示异常信息
//        new Thread() {
//            @Override
//            public void run() {
//                UXApp.instance().getHandler().post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        try {
//                            UXApp.mInstance.logout(false, null);
////                            Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出。", Toast.LENGTH_LONG)
////                                    .show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//            }
//        }.start();
//
//        // 收集设备参数信息
//        collectDeviceInfo(mContext);
//        // 保存日志文件
//        saveCrashInfo2File(ex);
//
//        if (AMDevice.isNetworkConnected(mContext) && infos.size() > 0) {//异常信息上传
//            AppUtil.uploadMsg(mContext, FastJsonKt.toJson(infos));
//        }
//        return true;
//    }
//
//    /**
//     * 收集设备参数信息
//     *
//     * @param ctx
//     */
//    public void collectDeviceInfo(Context ctx) {
//        try {
//            PackageManager pm = ctx.getPackageManager();
//            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
//                    PackageManager.GET_ACTIVITIES);
//
//            if (pi != null) {
//                String versionName = pi.versionName == null ? "null"
//                        : pi.versionName;
//                String versionCode = pi.versionCode + "";
//                infos.put("versionName", versionName);
//                infos.put("versionCode", versionCode);
//            }
//        } catch (NameNotFoundException e) {
//            Log.e(TAG, "an error occured when collect package info", e);
//        }
//
//        Field[] fields = Build.class.getDeclaredFields();
//        for (Field field : fields) {
//            try {
//                field.setAccessible(true);
//                infos.put(field.getName(), field.get(null).toString());
//                Log.d(TAG, field.getName() + " : " + field.get(null));
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e(TAG, "an error occured when collect crash info", e);
//            }
//        }
//    }
//
//    /**
//     * 保存错误信息到文件中 *
//     *
//     * @param ex
//     * @return 返回文件名称, 便于将文件传送到服务器
//     */
//    private String saveCrashInfo2File(Throwable ex) {
//        StringBuffer sb = new StringBuffer();
//        for (Map.Entry<String, String> entry : infos.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            sb.append(key + "=" + value + "\n");
//        }
//
//        Writer writer = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(writer);
//        ex.printStackTrace(printWriter);
//        Throwable cause = ex.getCause();
//        while (cause != null) {
//            cause.printStackTrace(printWriter);
//            cause = cause.getCause();
//        }
//        printWriter.close();
//
//        String result = writer.toString();
//        sb.append(result);
//        try {
//            String fileName = "";
//            File file = LogManager.createCrashLogFile();
////            if (Environment.getExternalStorageState().equals(
////                    Environment.MEDIA_MOUNTED)) {
////                File file = LogManager.createCrashLogFile();
//                FileOutputStream fos = null;
//                try {
//                    fos = new FileOutputStream(file.getAbsolutePath());
//                    fos.write(sb.toString().getBytes());
//                    fos.flush();
//                    LogUtil.i("程序崩溃异常", sb.toString());
//                } finally {
//                    if (fos != null)
//                        fos.close();
//                }
//                fileName = file.getName();
////            }
//
//            return fileName;
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "an error occured while writing file...", e);
//        }
//
//        return null;
//    }
//
//
//}
