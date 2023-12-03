package com.stream.straw.advertise_plugin.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.SoftReference;

public final class TToast {
    private static SoftReference<Toast> sToast;

    public static void show(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String msg, int duration) {
        Toast toast = getToast(context);
        if (toast != null) {
            toast.setDuration(duration);
            toast.setText(String.valueOf(msg));
            toast.show();
        } else {
            Log.i("TToast", "toast msg: " + String.valueOf(msg));
        }
    }

    @SuppressLint("ShowToast")
    private static Toast getToast(Context context) {
        if (context == null) {
            return sToast != null ? sToast.get() : null;
        }
//        if (sToast == null) {
//            synchronized (TToast.class) {
//                if (sToast == null) {
        sToast = new SoftReference<>(Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT));
//                }
//            }
//        }
        return sToast.get();
    }

    public static void reset() {
        sToast = null;
    }

}