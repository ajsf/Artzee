# Artzee

An Android app written in Kotlin to browse the [Artsy](https://www.artsy.net/) public API, and set the phone wallpaper. This was a project to experiment with some of the newer Android libraries, such as Anko, Android Architecture Components, Android Paging Library for continuous scrolling, as well as Retrofit for networking and a light tough of RxJava/RxKotlin.

![Screenshot](./.github/screenshot1.png?raw=true)&nbsp;&nbsp;&nbsp;&nbsp;![Screenshot](./.github/screenshot2.png?raw=true)

## Getting Started

To run the app, you will need to sign up with Artsy for a client ID and client secret. Instructions for this can be found [here](https://developers.artsy.net/start).

Once you have the ID and secret, copy the gradle.properties.example file to gradle.properties, and enter them in the gradle.properties file: 

```
artsyClientId=Enter_Client_ID
artsyClientSecret=Enter_Client_Secret
```

## Built With

* [Kotlin](https://kotlinlang.org/) - Kotlin programing language
* [Anko](https://github.com/Kotlin/anko) - Kotlin extensions and utilities for Android
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html) - For managing Android UI lifecycle
* [Android Paging Library](https://developer.android.com/topic/libraries/architecture/paging.html) - For continuous scrolling and paging of network requests
* [RxJava](https://github.com/ReactiveX/RxJava) - Reactive Extensions for the JVM
* [RxKotlin](https://github.com/ReactiveX/RxKotlin) - Kotlin Extensions for RxJava
* [Picasso](http://square.github.io/picasso/) - Image downloading and caching
* [Retrofit](http://square.github.io/retrofit/) - Type-safe HTTP client

## Acknowledgments

* [Android paging library with RxJava and REST API](https://medium.com/@Ahmed.AbdElmeged/android-paging-library-with-rxjava-and-rest-api-e5c229fd70ba) - This article and the code in it really helped me wrap my head around the Android Paging Library