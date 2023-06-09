# VidarVoyager
Vidar Voyager is a top-down 2d puzzle game that I am currently developing. I am using Libgdx framework and Gradle build tool.

# How to Build
This project will require an android sdk
Please follow the android guide to downloading an android sdk
[https://android-doc.github.io/sdk/installing/index.html](https://developer.android.com/studio/install)

Once you have downloaded android studios you will have to change the sdk to Android 31.

![alt text](https://github.com/thunderPumaFalconBird/VidarVoyager/blob/master/AndroidSDK.png?raw=true)

![alt text](https://github.com/thunderPumaFalconBird/VidarVoyager/blob/master/AndroidPlatform.png?raw=true)

Please follow the libgdx setup guide to import and edit the Desktop Launcher build and run configuration 
https://libgdx.com/wiki/start/import-and-running

The build should look like this when you are done

![alt text](https://github.com/thunderPumaFalconBird/VidarVoyager/blob/master/BuildConfig.png?raw=true)

There is also a basic SQL database that can be set up for this game. The initialization script is located in 
the sql directory. There is a Database class in the core/src/com/vv/game/utils directory that allows users 
to Sign Up and Sign in. There is no other functionality at this time. Installing PostgreSQL is not required,
but you can set up the database by following the Getting Started tutorial
https://www.postgresql.org/docs/current/tutorial-start.html and running initVidarVoyagerDB.sql. Then you will need 
to change the variables in the Database class to match the database that you set up.

# How to Play
There is a jar file located in the out directory. Simply download and double click the file. 
There currently is no puzzle on the main menu. You can click anywhere to continue to the game.
There are no puzzles implemented in the game yet either. Check back soon.
