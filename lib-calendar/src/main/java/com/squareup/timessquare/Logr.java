package com.squareup.timessquare;

import android.util.Log;

/**
 * Log utility class to handle the log tag and DEBUG-only logging.
 */
final class Logr {
    public static void d(String message) {
        if (true) {
            Log.e("TimesSquare", message);
        }
    }

    public static void d(String message, Object... args) {
        if (true) {
            d(String.format(message, args));
        }
    }
}
