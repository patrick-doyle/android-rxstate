package com.twistedequations.rxsavestate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.twistedequations.rxsavestate.sample.R;
import com.twistedequations.rxstate.RxSaveState;

import org.jetbrains.anko.AnkoContextImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class RxStateActivity extends AppCompatActivity {

    private static final String TAG = "RxStateActivity";
    private static final String STATE_KEY_TEXT = "text_state_key";
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    @BindView(R.id.rx_state_button)
    Button button;

    @BindView(R.id.rx_state_edittext)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = findViewById(android.R.id.content);
        View layout = new RxStateActivityUI().createView(new AnkoContextImpl<>(this, view, false));
        setContentView(layout);

        ButterKnife.bind(this);

        compositeSubscription.add(getSavedState());
        compositeSubscription.add(saveState(button));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    private Subscription saveState(final TextView button) {
        return RxView.clicks(button)
            .map(new Func1<Void, CharSequence>() {
                @Override
                public CharSequence call(Void aVoid) {
                    return editText.getText();
                }
            }).doOnNext(new Action1<CharSequence>() {
                @Override
                public void call(final CharSequence charSequence) {
                    RxSaveState.updateSaveState(RxStateActivity.this,
                        new Action1<Bundle>() {
                            @Override
                            public void call(Bundle bundle) {
                                bundle.putCharSequence(STATE_KEY_TEXT, charSequence);
                            }
                        });
                }
            }).subscribe(new Action1<CharSequence>() {
                @Override
                public void call(CharSequence charSequence) {
                    Log.i(TAG, "State saved for string "+ charSequence);
                    Toast.makeText(RxStateActivity.this, "State saved for string "+ charSequence, Toast.LENGTH_SHORT).show();
                }
            });
    }

    private Subscription getSavedState() {
        return RxSaveState.getSavedState(this)
            .map(new Func1<Bundle, CharSequence>() {
                @Override
                public CharSequence call(Bundle bundle) {
                    return bundle.getCharSequence(STATE_KEY_TEXT);
                }
            }).subscribe(new Action1<CharSequence>() {
                @Override
                public void call(CharSequence charSequence) {
                    editText.setText(charSequence);
                    Log.i(TAG, "State restored with string " + charSequence);
                    Toast.makeText(RxStateActivity.this, "State restored for string " + charSequence, Toast.LENGTH_SHORT).show();
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {

                }
            }, new Action0() {
                @Override
                public void call() {
                    Log.i(TAG, "State restore completed");
                    Toast.makeText(RxStateActivity.this, "State restore completed", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
