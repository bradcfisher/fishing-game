# Fishing

This is a simple graphical fishing game I created in Java as a school project for a graduate level user interface design class.

It demonstrates:

	- Various methods of providing users options for controlling the game environment, such as background music and sound effect volume levels.
	- Several levels of difficulty, each with their own user-configurable settings.
	- Animation and rendering utilizing background threads.

While the interface is Swing-based, the real grunt work is performed by a custom Flash-like graphical display list implementation.  Graphical elements, such as sprites, panels, and UI controls all inherit from a common base class `fishing.drawable.Drawable`

Several custom graphical UI controls are implemented within this project, including buttons, checkboxes, sliders, text input boxes, image-based selectors, and menus.

Game state and animation updates are performed at a consistent rate by timer threads, while display updates are performed in a dedicated rendering thread based on the current game state at the time of rendering.  This ensures that even if the host computer cannot ensure full framerate graphical rendering, the game state will still be updated in as close to real-time as possible.  While frame-skipping may occur on slower hardware, that is preferrable to having the game state lag behind real-time and ensures a more fluid user experience across a wider range of hardware.

# Building

## NetBeans

One of the simplest ways to build and run this project is using NetBeans.  Simply select `Open Project`, navigate to the path containing the project folder in the dialog, select the project folder and click `Open Project`.

Once the project is open, you can build and run it with `Run -> Run Project` or by hitting F6.  The project will then build and execute.

While the project will build and execute successfully using NetBeans 8, it was originally developed in NetBeans 7 and utilizes the deprecated Swing Application Framework.  Basically, the primary downside to this is that one cannot visually update any of the dynamically generated swing forms in NetBeans 8 or newer.  To modify those forms, please use the latest version of NetBeans 7.  The files affected by this limitation are `FishingView.java` and `FishingAboutBox.java`.

## Maven

Since this is a Maven project, it can also be built on the command line:

`mvn clean`

`mvn package`

`java -jar ./target/Fishing-1.0-SNAPSHOT-jar-with-dependencies.jar`

# License

The source code and any custom assets created for this project are licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International license (http://creativecommons.org/licenses/by-nc-sa/4.0/).

Some assets are provided by third party content creators and are utilized under their respective
licensing.  Those assets are attributed in separate README.md files in their containing directories.
Please do not reuse those assets in a manner inconsistent with their licensing.
