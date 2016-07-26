package com.collecdoo.control;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.collecdoo.R;


public class SimpleProgressDialog {
    ProgressDialog progressDialog;
    Context context;

    AsyncTask asyncTask;

    public SimpleProgressDialog(Context context) {

        this.context = context;
        progressDialog = new ProgressDialog(context, android.R.style.Theme_Translucent);

    }


    public void dismiss() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception exp) {
        }
    }

    public void showBox() {

        progressDialog.setTitle(null);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setIndeterminate(true);
        progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.custom_progress_background));
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress_dialog);
    }

    public Boolean isShowing() {
        return progressDialog.isShowing();
    }

}
