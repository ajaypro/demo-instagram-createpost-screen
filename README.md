## Container classes for profile, image and home

* This project helps you understand creating containers classes for your data to be loaded such as
   * home fragment
   * photo fragment
   * profile fragment
* We create a `MainActivity` which keeps replacing the fragments such as photo, home, profile based on what is been selected 
  at the bottom navigation bar. 
* `MainActivity` only sends the information of which option was clicked to `MainViewmodel` based on `bottomNavigation`
* In the `MainViewmodel` we use the `LiveData` for each option of `bottomNavigation` and we observe them in `MainActivity`
* From `MainActivity` we change the fragment updated by `LiveData`, we also check if current fragment is option that the user clicked 
  then we retain the same fragment.
* To avoid the recreation of fragment on multiple clicks by user we retain the instance of fragment when its loaded for first time
   and displayed the same.
