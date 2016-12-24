package com.collecdoo.fragment.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;

import com.collecdoo.Utility;
import com.collecdoo.fragment.home.profile.LanguageFragment;

/**
 * Created by kiemhao on 12/5/16.
 */

public class TextToSpeedManager {

    public static void startTTS(Fragment fragment,  int requestCode) {
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, LanguageFragment.isLanguageEn(fragment.getContext())? "en-US":"de-DE");
        try {
            fragment.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException a) {
            Utility.showMessage(fragment.getContext(),
                    "Opps! Your device doesn't support Speech to Text");
        }
    }
}
