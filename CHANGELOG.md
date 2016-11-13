2.0.0
-----

- Added Rxjava 2 support with the `com.twistedequations.rx2:rx2-savestate:1.2.0` artifact
- Added maybe support to the `getSavedState(activity)` method
- Changed the Action1 parameter (`updateSaveState(Activity, Action1<Bundle>)`) to BundleAction (`updateSaveState(Activity, BundleAction)`) for updateSaveState

1.1.0
-----

- Added Ability to get state without an observable wih the`getSavedStateDirect(activity)` method
