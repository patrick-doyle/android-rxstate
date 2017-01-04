2.0.1
-----

- Fixed an issue where the current state was not available until after a rotation
- Updated RxJava 2 to 2.0.3
- Updated RxJava to 1.2.4
- Updated Support library to 25.0.1

-----

- Added Rxjava 2 support with the `com.twistedequations.rx2:rx2-savestate:2.0.0` artifact
- Added maybe support to the `getSavedState(activity)` method
- Changed the Action1 parameter (`updateSaveState(Activity, Action1<Bundle>)`) to BundleAction (`updateSaveState(Activity, BundleAction)`) for updateSaveState

1.1.0
-----

- Added Ability to get state without an observable wih the`getSavedStateDirect(activity)` method
