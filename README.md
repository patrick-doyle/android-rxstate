Android RxJava wrapper for updating and retrieving the saved state as any point in an activity/fragment or RxJava chain

## RxSaveState

#### Setup
Add `jcenter()` to the projects repositories build.gradle

```groovy
    repositories {
        jcenter()
    }
```
In your module build.gradle file add the dependency

Current Version is in the gradle.properties file [Here](gradle.properties) 
```groovy
    dependencies {
        compile "com.twistedequations.rx:rx-savestate:{{current version}}"
    }
```
If its giving an error about not finding a jar file use `com.twistedequations.rx:rx-savestate:{{current version}}@aar` instead

## Usage

To obtain the previous state call the `RxSaveState.getSavedState(activity)` method, the observable returned emits the saved state as a bundle
downstream and completes. If there is no saved state then the observable just sends the onComplete event acting the same as Observable.empty()

```java
    RxSaveState.getSavedState(activity)
    .map(new Func1<Bundle, CharSequence>() {
        @Override
        public CharSequence call(Bundle bundle) {
            return bundle.getCharSequence(STATE_KEY_TEXT);
        }
    })
    .subscribe(new Action1<CharSequence>() {
        @Override
        public void call(CharSequence charSequence) {
            Log.i(TAG, "State restored with string " + charSequence);
            Toast.makeText(RxStateActivity.this, "State restored for string " + charSequence, Toast.LENGTH_SHORT).show();
        }
    });
```

To save state you can use the `RxSaveState.updateSaveState()` function to update the state and add any new data. This can be called many times and any new values 
will overwrite the previous one for a given key

```java
    RxView.clicks(button)
    .map(new Func1<Void, CharSequence>() {
        @Override
        public CharSequence call(Void aVoid) {
            return editText.getText();
        }
    })
    .doOnNext(new Action1<CharSequence>() {
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
    })
    .subscribe(new Action1<CharSequence>() {
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
        .map(new Func1<Bundle, List<Items>>() {
            @Override
            public List<Items> call(Bundle bundle) {
                return bundle.getParceable(STATE_KEY);
            }
        })
        .switdhIfEmpty(networkObservable()) //Fail over the network observable if the saved data obervable is empty
        .doOnNext(new Action1<List<Items>>() {
           @Override
            public void call(final List<Items> data) {
                RxSaveState.updateSaveState(RxStateActivity.this,
                        new Action1<Bundle>() {
                            @Override
                            public void call(Bundle bundle) {
                                bundle.putParceable(STATE_KEY, data); //Update state with the lastest data
                            }
                        });
            }.subscribe(new Action1<List<Items>>() {
                 @Override
                 public void call(List<Items> data) {
                     view.setData(data);
                 }
            }
```