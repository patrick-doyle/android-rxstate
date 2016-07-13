package com.twistedequations.rxstate.internal;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Action3;
import rx.functions.Func1;

public class RxSaveStateFragment extends Fragment {

    private final Bundle state = new Bundle();
    private Bundle prevState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prevState = savedInstanceState;
        if(prevState != null) {
            //keep the old state for the next restoration
            //it wil be updated when (updateState()) is called for new values
            state.putAll(prevState);
        }
    }

    public Observable<Bundle> getPrevState() {
        if(prevState != null) {
            return Observable.just(prevState);
        } else {
            return Observable.empty();
        }
    }

    public void updateState(Action1<Bundle> updateStateAction) {
        updateStateAction.call(state);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(state);
    }
}
