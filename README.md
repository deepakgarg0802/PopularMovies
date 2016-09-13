# PopularMovies (Project 1 & 2)
An Android app which shows the list of Most Popular and Top Rated movies. This project is under Udacity Android Nanodegree Program.

## Rubric

### Stage 1 : Main Discovery Screen, A Details View, and Settings
* Upon launch, present the user with an grid arrangement of movie posters.
* Allow your user to change sort order via a setting:
* The sort order can be by most popular, or by top rated
* Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
  * original title
  * movie poster image thumbnail
  * A plot synopsis (called overview in the api)
  * user rating (called vote_average in the api)
  * release date

### Stage 2 : Trailers, Reviews, and Favorites
* Allow users to view and play trailers ( either in the youtube app or a web browser).
* Allow users to read reviews of a selected movie.
* Allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that you will maintain and does not require an API request*.
* Modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.
* Optimize your app experience for tablet.

## Getting Started
This sample uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project" in Android Studio.

This app uses [The Movie Database API](https://www.themoviedb.org/documentation/api) to retrieve movies. You must provide your own API key in order to build the app. When you get it, just paste it to `API_KEY` **variable** in [this](PopularMovies/app/src/main/java/com/example/deepakgarg/popularmovies/MainActivityFragment.java) file.
