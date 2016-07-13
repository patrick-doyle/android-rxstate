package com.twistedequations.rxstate.internal;

import android.os.Looper;


public class PreConditions {

    public static void throwIfNotOnMainThread() {
        if(Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalArgumentException("Cant call RxSaveState from a background thread, move it to the main thread");
        }
    }
}
