Android RxJava wrapper for Saving State

## RxSaveState

#### Setup
Add to root build.gradle

```groovy
    allprojects {
        repositories {
            jcenter()
        }
    }
```
In your module build.gradle file add to the dependencies block

```groovy
    dependencies {
        compile "com.twistedequations.rx:rx-savestate:1.0.0"
    }
```
If its giving an error about not finding a jar file use `com.twistedequations.rx:rx-savestate:1.0.0@aar` instead

## Usage

To obtain the previous state that was saved. The Observable emits one bundle event downstream and completes. If there is no
saved state then the observable just sends the onComplete event

```java
    RxSaveState.getSavedState(this)
    .map(new Func1<Bundle, CharSequence>() {
        @Override
        public CharSequence call(Bundle bundle) {
            return bundle.getCharSequence(STATE_KEY_TEXT);
        }
    }).subscribe(new Action1<CharSequence>() {
        @Override
        public void call(CharSequence charSequence) {
            Log.i(TAG, "State restored with string " + charSequence);
            Toast.makeText(RxStateActivity.this, "State restored for string " + charSequence, Toast.LENGTH_SHORT).show();
        }
    });
```

To save state you can use the `doOnNext` rx operator with the `RxSaveState.updateSaveState()` function to update the state to be saved. This can
be called many times and the new value will overwrite the previous one for a given key

```java
    RxView.clicks(button)
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
```

One very useful application is combine these functions with other data sources to easily combine 
restore state, disk cache and network loading

```java
    Observable savedStateObservable = RxSaveState.getSavedState(this) //save state and map to get the part of the state we need
                                .map(new Func1<Bundle, NetworkData>() {
                                    @Override
                                    public NetworkData call(Bundle bundle) {
                                        return bundle.getParceable(STATE_KEY);
                                    }
                                })
    
    Observable.concatDelayError(savedStateObservable, diskCacheObservable(), networkObservable()) //combine observables to run one after the next
       .doOnNext(new Action1<NetworkData>() {
           @Override
           public void call(final NetworkData data) {
               RxSaveState.updateSaveState(RxStateActivity.this,
                       new Action1<Bundle>() {
                           @Override
                           public void call(Bundle bundle) {
                               bundle.putParceable(STATE_KEY, data); //Update state with the lastest data
                           }
                       });
           }.subscribe(new Action1<NetworkData>() {
                @Override
                public void call(NetworkData data) {
                    view.setData(data);
                }
            }
```