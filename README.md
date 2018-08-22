# GithubFollowers

This sample app is a two-screen presentation, first of a selection of Github followers for the Github user entered in the dialog or search fields.
The second screen is a Material Design presentation of the selected follower details.

The requirements for this app were that no third-party libraries be used. Therefore, the network retrieval was implemented without Retrofit or RxJava, which normally would be used.

Android Data Binding and Material Design are the two main Android-specific highlights of the app. The screen animation from first to second uses an Activity animation, which is the reason for 2 Activities instead of an Activity and 2 Fragments.
