# Stock App
An app that gives realtime updates on stocks

## Getting Started

Clone the repository. The repository contains a `server` folder for the server and an `android-app` folder for the Android application. 
Replace the `google-services.json` file in the `android-app/app/` directory with the one from your 
Firebase dashboard. Replace the key holders in the app with the keys from your Pusher Beams and Channels dashboard respectively.

Open the server folder install the node dependencies by running:

```
npm install
```

and run this this command to get your server running: 

```
node index.js
```

### Prerequisites

You need the following installed:

* [Android Studio](https://developer.android.com/studio/index)
* [Node](http://nodejs.org)


## Built With

* [Kotlin](https://kotlinlang.org/) - Used to build the Android client
* [Pusher](https://pusher.com/) - APIs to enable devs building realtime features
* [Node](http://nodejs.org) - Used to build the server

## Acknowledgments
