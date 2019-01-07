# Foody [![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social&logo=twitter)](https://twitter.com/intent/tweet?text=Check%20out%20this%20amazing%20android%20application.%20It%20helps%20you%20to%20find%20restaurants%20near%20to%20your%20location%20or%20anywhere%20else%20for%20free%20&url=https://github.com/rahulmalhotra/Foody&via=rahulcoder&hashtags=androidnanodegree,capstoneproject,udacity,google)

Foody is an android application which helps you to find restaurants near to your location or anywhere else for free.

## Getting Started

Foody suggest you nearby restaurants based on your location and cuisine preferences. You can get the location details of each restaurant, it’s rating, average cost for two people, total number of votes, popularity, top cuisines and much more.

You can mark some restaurants favourite, which will be shown in home screen widget and also share the details of any restaurant. It helps the users to find a way to any restaurant from their current location by directing them to google maps.

### Prerequisites

You need to have basic knowledge of android development to understand this project. Exposure of external libraries used in the android application is a plus. If you want to download the code in your local system, you can do it by the executing the below command or downloading the code directly as a zip file.

```
git clone https://github.com/rahulmalhotra/Foody.git
```

### Installing

You need to add your own gradle.properties file as the api keys are defined there. There are mainly 3 apis used:-

1. Zomato API
2. Google Places API
3. Admob API

All the api keys are used in the defaultConfig section of [build.gradle](app/build.gradle) file.
In the [app manifest](app/src/main/AndroidManifest.xml) also, the api keys are used from gradle.properties file.

In your newly created **gradle.properties** file, you can add your api keys in the form of key value pairs.
For ex :-

```
GOOGLE_API_KEY=abcd123123123abcd
```
**Foody** is now ready for use.

## Deployment

You can deploy Foody android application directly to your device by installing the apk file. You can run the code in android studio and create an apk yourself or use the apk present in this repository. You can find the apk file [here](app/release/app-release.apk).

## Application Screenshots

### NearBy Restaurants Screen
<img src="App%20Screenshots/Nearby.png" alt="Nearby" width="40%" />

### Favorite Restaurants Screen
<img src="App%20Screenshots/Favorites.png" alt="Favorites" width="40%" />

### Restaurant Detail Screen
<img src="App%20Screenshots/Restaurant%20Detail.png" alt="Restaurant Detail" width="40%" />

### Change Location Screen
<img src="App%20Screenshots/Change%20Location.png" alt="Change Location" width="40%" />

### Cuisine Preferences Screen
<img src="App%20Screenshots/Cuisine%20Preferences.png" alt="Cuisine Preferences" width="40%" />

### Settings Screen
<img src="App%20Screenshots/Settings.png" alt="Settings" width="40%" />

## Tools and Softwares used

I used only android studio to build this application. You can download android studio for your system using the below link :-

* [Download Android Studio](https://developer.android.com/studio/) - Official IDE for Android Development

## Capstone Stage 1 Document

You can find the design document related to this application in this repository [here](Capstone_Stage1.pdf). It consists of the whole design and other information used to build this application.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on code of conduct and the process for submitting pull requests.

## Authors

* **Rahul Malhotra** - [@rahulcoder](https://twitter.com/rahulcoder)

## License

This project is licensed under the BSD 3-Clause License - see the [LICENSE.md](LICENSE.md) file for details.
