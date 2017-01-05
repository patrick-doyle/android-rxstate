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
        if (state.isEmpty()) {
            //if the state in empty then return a null value,
            // this matches the way the bundle passed to onCreate
            return null;
        } else {
            return state;
        }
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
