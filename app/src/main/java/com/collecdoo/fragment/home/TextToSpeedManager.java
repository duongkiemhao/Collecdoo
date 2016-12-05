package com.collecdoo.fragment.home;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.widget.EditText;

import com.collecdoo.Utility;

/**
 * Created by kiemhao on 12/5/16.
 */

public class TextToSpeedManager {

    public static void startTTS(Fragment fragment, EditText editText,int requestCode){
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            fragment.startActivityForResult(intent, requestCode);
            editText.setText("");
        } catch (ActivityNotFoundException a) {
            Utility.showMessage(fragment.getContext(),
                    "Opps! Your device doesn't support Speech to Text");
        }
    }
}
