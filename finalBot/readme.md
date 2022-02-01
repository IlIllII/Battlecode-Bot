# Final Bot

The is the final state of my Battlecode bot. It's basically a cleaned up version of the spaghetti that resides in src.

`RobotPlayer.java` runs the bot by dispatching different strategies based on the unit type being operated. These strategies can be found in the different `Strategy` files in this folder. There are also some files that provide utlities that are shared between all of the strategies, namely `Comms`, the various BFS files, `Movement`, `Targeting`, and `Statics`.