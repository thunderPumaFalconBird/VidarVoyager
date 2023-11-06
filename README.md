# VidarVoyager
Vidar Voyager is a top-down 2d puzzle game that I developed to learn the Libgdx framework and Gradle build 
tool. This game is just a Demo with one level. However, I developed it with scalability in mind and followed 
Object-Oriented Programming design principles. So, multiple levels can easily be added to this game. 


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


# SQL Database
There is also a basic SQL database that can be set up for this game. The initialization script is located in 
the sql directory. There is a DatabaseInterface class in the core/src/com/vv/game/utils directory that allows 
users to Sign Up and Log In. User's passwords are stored in hashed values that utilize salt. The database also keeps
track of successful log in events and failed log in events.

This database is not necessary for the game to function,
but you can set up the database by following the Getting Started tutorial
https://www.postgresql.org/docs/current/tutorial-start.html and running initVidarVoyagerDB.sql. Then you will need 
to change the variables such as, DATABASE_NAME, DATABASE_URL, etc., in the Database class to match the database that 
you set up.


# How to Play
If you do not want to build and run the program there is a jar file located in the out directory. 
Simply download and double-click the file. 

The goal of the first level is to collect one of every color teddy bear and take them to the life support system. 
Be sure to keep an eye on your oxygen level and refill your tank at an oxygen station when needed. The first level
has a basic game of minesweeper as the puzzle to unlock doors. 
