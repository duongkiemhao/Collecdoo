package com.collecdoo.config;


import com.collecdoo.MyApplicationContext;

/**
 * Created by kiemhao on 2/3/16.
 */
public class Config {

    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int SOCKET_TIMEOUT = 60000;
    public static final String LOG_NAME = "collecdoo";
    public static final String APP_DATA_DIR = MyApplicationContext
            .getInstance().getAppContext().getExternalCacheDir()
            + "/" + LOG_NAME;

    public static String LOGFILE_NAME = "log.txt";
    public static int MAX_LOGFILE_KB = 100;
    public static boolean FORCE_CLOSE_DIALOG = false;

    public static int ACTION_LOGOUT = 0;
    public static int ACTION_LOGIN = 1;
    public static int ACTION_WORKING = 2;

    public static String PHONE_NO="00491717522012";


}
