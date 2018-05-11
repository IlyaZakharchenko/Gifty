package ru.kpfu.itis.gifty.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Ilya Zakharchenko on 05.05.2018.
 */
public class Keyboard {
    public static void hide(View v) {
        if (v.getContext() != null) {
            InputMethodManager manager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
            }
        }
    }
}
