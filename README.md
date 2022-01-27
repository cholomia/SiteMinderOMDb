# iTunes Movies

A demo application using: 
* MVVM and Clean Architecture
* Android Jetpack: LiveData, ViewModel, Navigation,etc.
* Uses OkHttp and Retrofit with Moshi (serialization) for handling REST API 
* For persistence library, uses Android Room for simplicity as this demo does not contain deep hierarchy of relationships.
* Paging3 - for handling list pagination with remote mediator and stale mechanism (not automatically refreshing every time user enters the screen if user have just reload)
* Android Hilt for dependency injection, a Dagger2 implementation for android components.
* Kotlin Coroutines and Flow for concurrency and reactive approach