package com.twistedequations.rxstate;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.twistedequations.rxstate.internal.PreConditions;
import com.twistedequations.rxstate.internal.RxSaveStateFragment;

import rx.Observable;
import rx.functions.Action1;

public class RxSaveState {

    private static final String FRAGMENT_TAG = "RxSaveStateFragment";

    private RxSaveState() {
    }

    /**
     * Gets the current state as an observable. The Observable will emit a single bundle if there is a previous state
     * or if the state is missing it will emit no events and call the onComplete event
     */
    @NonNull
    public static Observable<Bundle> getSavedState(@NonNull Activity activity) {
        PreConditions.throwIfNotOnMainThread();
        Bundle prevState = getSavedStateDirect(activity);
        if(prevState != null) {
            return Observable.just(prevState);
        } else {
            return Observable.empty();
        }
    }

    /**
     * Gets the current state as a bundle
     * @return the saved state or null if there is no saved state
     */
    @Nullable
    public static Bundle getSavedStateDirect(@NonNull Activity activity) {
        PreConditions.throwIfNotOnMainThread();
        return getFragment(activity).getPrevState();
    }

    /**
     * Update the state to save. This state will be available on the activity config change (eg screen rotation)
     * via the {@link #getSavedState(Activity)} method.
     * @param updateStateAction the action that will be called to save the state
     */
    public static void updateSaveState(@NonNull Activity activity, @NonNull Action1<Bundle> updateStateAction) {
        PreConditions.throwIfNotOnMainThread();
        getFragment(activity).updateState(updateStateAction);
    }

    private static RxSaveStateFragment getFragment(Activity activity) {
        final FragmentManager fragmentManager = activity.getFragmentManager();
        final RxSaveStateFragment intentFragment;
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if(fragment == null) {
            intentFragment = new RxSaveStateFragment();
            fragmentManager.beginTransaction().add(intentFragment, FRAGMENT_TAG).commitAllowingStateLoss();
        } else {
            intentFragment = (RxSaveStateFragment) fragment;
        }
        return intentFragment;
    }
}
