package com.collecdoo.handler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;

import com.collecdoo.MyApplicationContext;
import com.collecdoo.config.Config;


public class MyUncaughtExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private Context context;
    private Thread.UncaughtExceptionHandler defaultUEH;

    public MyUncaughtExceptionHandler(Context context) {
        this.context = context;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread thread, Throwable exception) {

        if (Config.FORCE_CLOSE_DIALOG == false) {

            MyApplicationContext.getInstance().log(
                    Log.getStackTraceString(exception));
            this.defaultUEH.uncaughtException(thread, exception);
        } else {
            String fullMessage = Log.getStackTraceString(exception);
            MyApplicationContext.getInstance().log(fullMessage);

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    builder.setTitle("Sorry...!");
                    builder.create();

                    builder.setPositiveButton("View log",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(
                                            Intent.ACTION_VIEW);
                                    intent.setDataAndType(
                                            Uri.parse("file:///sdcard/"
                                                    + Config.LOG_NAME + "/"
                                                    + Config.LOGFILE_NAME),
                                            "text/plain");
                                    context.startActivity(intent);
                                    System.exit(0);

                                }
                            });
                    builder.setNegativeButton("Exit",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    System.exit(0);
                                }
                            });
                    builder.setMessage("Unfortunately,This application has stopped");
                    builder.show();
                    Looper.loop();
                }
            }.start();

        }
    }

}