package com.collecdoo;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.collecdoo.config.Config;
import com.collecdoo.handler.MyUncaughtExceptionHandler;
import com.collecdoo.helper.DateHelper;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.appender.FileAppender;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.IOException;


public class MyApplicationContext extends Application {
    private Context context;
    private static Logger logger = LoggerFactory.getLogger();

    private static MyApplicationContext instance;
    private RequestQueue mRequestQueue;
    // private HashMap<String, Object> hashmap;

    public static final String TAG = MyApplicationContext.class.getSimpleName();


    public MyApplicationContext() {
        // TODO Auto-generated constructor stub
        super();

        // this.indexVersion=indexVersion;

    }

    public static synchronized MyApplicationContext getInstance() {
        // TODO Auto-generated method stub
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;
        init();
        // hashmap = new HashMap<String, Object>();
    }

    // public Object getFromHashmap(String key) {
    // return hashmap.get(key);
    // }
    //
    // public void setToHashmap(String key, Object value) {
    // this.hashmap.put(key, value);
    // }

    void init() {
        // init image loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
        // init logger
        File dir = new File(Config.APP_DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // initDB();
        initLogger();
        // init sqlite

    }

    public DisplayImageOptions getImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()

                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)

                .cacheInMemory(true)
                .cacheOnDisk(true).build();
        return options;
    }

    private static void createLogFileIfNeed() {

        File file = new File(Environment.getExternalStorageDirectory() + "/"
                + Config.LOG_NAME + "/" + Config.LOGFILE_NAME);
        try {
            if (!file.exists())
                file.createNewFile();
            else if (file.length() / 1024 > Config.MAX_LOGFILE_KB) {
                file.delete();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initLogger() {
        new File(Environment.getExternalStorageDirectory() + "/"
                + Config.LOG_NAME).mkdir();
        createLogFileIfNeed();
        PropertyConfigurator.getConfigurator(this).configure();
        FileAppender appender = new FileAppender();
        appender.setFileName(Config.LOG_NAME + "/" + Config.LOGFILE_NAME);
        appender.setAppend(true);
        logger.addAppender(appender);
    }

    public Context getAppContext() {
        return context;
    }

    public static void log(String msg) {
        // createLogFileIfNeed();

        logger.debug(DateHelper.getcurrentDateString() + "-----" + msg);
    }

    public static void log(Exception exp) {
        logger.debug(DateHelper.getcurrentDateString()
                + Log.getStackTraceString(exp));
    }


    // volley

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty

        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void cancelAllPendingRequests() {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    public static void setEnableUncatchException(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(
                context));

    }


}
