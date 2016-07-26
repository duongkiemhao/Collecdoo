package com.collecdoo.config;


import com.collecdoo.MyApplicationContext;

/**
 * Created by kiemhao on 2/3/16.
 */
public class Config {

    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int SOCKET_TIMEOUT = 60000;
    //public static final String LOGFILE_PATH = "kct";
    public static String LOGFILE_NAME = "log.txt";


    public static int MAX_LOGFILE_KB = 100;

    public static final String LOG_NAME = "collecdoo";
    public static final String APP_DATA_DIR = MyApplicationContext
            .getInstance().getAppContext().getExternalCacheDir()
            + "/" + LOG_NAME;

    public static boolean FORCE_CLOSE_DIALOG = false;
    public static int SOCKET_PORT = 9000;
    public static String CLIENT_GATEWAY = "192.168.43.1";

    public static int TRYING_CONNECT_TIME = 1 * 60 * 1000;

    // public enum CommandEnum {"getPlaylist","addSong","updateSongIndex","play","pause","next","previous","voiceOn","voiceOff"};

    public static String AUTHEN_USER = "apikct";
    public static String AUTHEN_PASS = "tqKviry6a3yPMj";

    //    public static String TYPE_SONG = "1";
//    public static String TYPE_CATEGORY = "2";
//    public static String TYPE_COMPOSER = "3";
//    public static String TYPE_SINGER = "4";
//    public static String TYPE_PLAYLIST = "5";
//    public static String TYPE_COMPOSER_SINGER = "6";
    public static String DB_NAME = "mysql-db";
    public static int PAGE_SIZE = 10;


    public enum TypeSong {
        MV, Mp3, Midi
    }

    public enum Type {
        Song, Category, Composer, Singer, Album_Playlist, Album, Playlist
    }

    public static int ACTION_LOGOUT = 0;
    public static int ACTION_LOGIN = 1;
    public static int ACTION_WORKING= 2;

}
