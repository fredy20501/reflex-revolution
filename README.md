# Reflex Revolution
## Project Description
This project is an Android application for a game similar to the toy Bop-It! by Hasbro. The game consists of a series of random actions which you must complete within a limited amount of time. Example actions: tap, swipe, shake, tilt, turn, twist, freeze, and many more! The game continues until you complete an action incorrectly or fail to complete it in time. The time you have to complete each action is reduced as the game progresses, making it more and more difficult the longer your game lasts.

The game features 6 different game modes, each having a different set of actions for you to complete, as well as 3 difficulty levels. Your high scores for each game mode and difficulty level are automatically saved. A "How to Play" section is included in the app to further explain the rules of the game, as well as letting you practice any action using a dedicated "Practice Mode". The game also includes music and sound effects, as well as voice acting for each action. Settings are available to adjust the volume of all sounds, as well as reset your high scores stored on your local device.

This Android application was made as part of a project during a University-level course on mobile application development.

## Device Limitations
This game was made for Android devices with a minimum API level of 23 (i.e., Android Marshmallow). It was optimized for API level 31 (i.e., Android 12).

Your device must include hardware sensors for an accelerometer and gyroscope for our game to function properly. Those sensors are required for us to detect your phone's movement during the game for certain actions.

## How to Play
The application was written in Java using Android Studio. You can play it by opening it directly using Android Studio and either running it on an emulator within Android Studio or connecting an Android device and downloading it to it. Note the limitations mentioned above.

## Project Organization
The Java code for our application can be found under `app/src/main/java/ca/unb/mobiledev/reflexrevolution`. The code is split into directories to keep things organized.
- `activities` contain the Java classes for the main activities of our app.
- `detectors` contain the Java classes used for detecting the actions in the game.
- `instruction` contain the Java classes that define all the neceesary information for each action in our game.
- `utils` contain general classes and enums used throughout the app.

The assets for our application can be found under `app/src/main/res`. The main directories are listed below.
- `drawable` stores the icons and small custom shapes/components used in the UI.
- `font` stores the font information for the two fonts used in the app.
- `layout` stores the layout files that define the UI of the activities of the app.
- `mipmap` stores the game icon files and some images used in the app.
- `raw` stores the music, sound effects, and voice lines used in the game.
- `values` stores a lot of data related to the UI of the app.

## Contributors
- [Frederic Verret](https://github.com/fredy20501)
- [Carter Moore](https://github.com/carterjmoore)
- [Hailey Savoie](https://github.com/hsavoie)