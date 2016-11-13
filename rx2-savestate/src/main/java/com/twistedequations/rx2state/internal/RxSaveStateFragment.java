package com.twistedequations.rx2state.internal;

import android.app.Fragment;
import android.os.Bundle;

import com.twistedequations.rx2state.BundleAction;

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

    public Bundle getPrevState() {
        return prevState;
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
