package com.twistedequations.rx2state.internal;

import android.app.Fragment;
import android.os.Bundle;

import com.twistedequations.rx2state.BundleAction;

public class RxSaveStateFragment extends Fragment {

    private final Bundle state = new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            state.putAll(savedInstanceState);
        }
    }

    public Bundle getState() {
        return state;
    }

    public void updateState(BundleAction updateStateAction) {
        updateStateAction.call(state);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(state);
    }
}
