# Battlecode-Bot

This repo contains my final submission for [MIT's 2022 Battlecode Programming Competition](https://battlecode.org/). This readme presents a post-mortem of the project.

<div align="center">
<img src="https://user-images.githubusercontent.com/78166995/150421412-f986ad0c-d888-42f7-9374-cf5b90499239.PNG">
</div>


<h2 align="center">Results</h2>


| Sprint Tournament 1 Placement | Sprint Tournament 2 Placement |
| --- | --- |
| ![sprint tournament 1 placing](https://user-images.githubusercontent.com/78166995/150365276-7cd9527b-d321-4b22-87a0-74c605d1084b.PNG) | ![sprint tournament 2 final standings](https://user-images.githubusercontent.com/78166995/150365245-0cc16e88-3653-4ab5-821c-2823bfdd6470.PNG) |

| Rank going into Sprint 1 | Rank going into Sprint 2 |
| --- | --- |
| ![battlecode rankings week1 page](https://user-images.githubusercontent.com/78166995/150365351-fcfc6d2a-6050-4bb5-91e1-bb57833bb161.PNG) | ![Battlecode rank week 2 just before tournament](https://user-images.githubusercontent.com/78166995/150365310-0d2b7d59-c627-44d4-98b3-a018676cd275.PNG) |


# Technical Postmortem


Battlecode this year played a game called "Mutation." It was essentially a top-down RTS with the ability to upgrade buildings. Contestants program AI players that are then pitted against each other for cash prizes and glory.



There were four unit types that are self-explanatory:

1. Miners
2. Builders
3. Soldiers
4. Sages (can cast spells)

And three building types:

1. Archons - these are your main buildings that produce units. You win by destroying your enemy's archons. These also heal other units.
2. Watchtowers - defensive towers that shoot enemies.
3. Laboratories - these transmute lead into gold.


Basic gameplay is as follows: You build units by expending lead, the main resource found in the environment. Lead can be harvested by miners. Furthermore, lead can be transmuted into gold in laboratories and then used to produce advanced units and upgraded buildings. The goal is to use all tools at your disposal to destroy your enemy's archons. The environment is a variably sized tile map where tiles contain a preset amount of rubble which will slow down your units if you step on a high rubble tile.

<h3 align="center">Two of the many maps</h3>

| Nyancat | Snowflake |
| --- | --- |
| ![nyan-cat](https://user-images.githubusercontent.com/78166995/150367122-86b7231c-8430-4d51-b6a8-fdec2691ce44.PNG) | ![snowflake map](https://user-images.githubusercontent.com/78166995/150367130-2361efea-fd59-4fd5-afb6-738e8cb826a1.PNG) |

You control units through an API exposed by the game. Each unit runs in its own JVM so there is no shared state between units. The only way to communicate is through a shared array of 64 16-bit integers which can be accessed through the API. Additionally, each unit has a computation limit that it can expend each turn so you must keep your computation under this limit. The limit consists of a number of bytecode instructions, with bytecode being an intermediate representation during Java's compilation process.


I started out development by creating simple logic for various units and testing out how the game worked and then iterated every day. I will detail the development process for movement and communication, just two of many aspects of the game.

## Movement

The first approach I took was simple bugnav to get up and running.

<h3 align="center">Bugnav</h3>

<div align="center">
<img src="https://user-images.githubusercontent.com/78166995/150423177-bca86c6d-9055-4cf4-9bfe-42f2f68e939a.PNG">
</div>

1. Get the direction from the unit to its target.
2. If we can move in this direction, do so.
3. If not, turn 45 degrees in a direction and try to move again. The direction for rotation, either right or left, is chosen randomly at instantiation so half the units spin left and half spin right.
4. Continue this until we have moved successfully or spun around in a full 360.

Bug nav was ok as a first step but was really pretty bad. My implementation did not sense rubble on tiles or do any pathfinding other than spin, meaning units were slow and could get caught in pathological loops very easily. The benefit of bug nav is that it is really computationally cheap and easy to implement.

The next iteration of my movement strategy was to implement a more sophisticated pathfinding algorithm.

#### Next attempts

Each unit has a vision radius, meaning they could see a certain number of tiles away in every direction. I figured these tiles make a sort of graph and we can use a path finding algorithm to find the shortest weighted path toward our destination.

My intuition told me that A* would work well, so I implemented that using Java's built-in priority queue, hash table, and array list. I created a heuristic that took into account rubble amounts on tiles as weights and then distance to the target destination.

However, after I compeleted the algorithm I tried it and blew way past the computation limits! Soldiers, as an example, have a 10,000 bytecode limit per turn and a sight range of about 5. To keep A* under the bytecode limit, I had to decrease the search radius to 1 or 2 tiles, which is basically unusable.

After some profiling of bytecode costs, I realized the built-in java data structures used a lot of bytecode. A single hash table access, for example, cost something like 100 bytecode, which doesn't go very far when you only have 10,000 to spend and pathing is only one part of a bot's logic.

To remedy this, I decided to implement my own hash table, min heap, and list thinking it could be more bytecode efficient. However, after implementing these data structures, I still could only do A* in a 2 tile radius.

I thought that maybe A* was just too expensive so I changed my algorithm to be cheaper. I tried to use a greedier approach and implemented greedy best first search, meaning I would just select the best tile from each location and then repeat the process from the selected tile until I reached the end of my vision range and call that my path. However, even with this greedy algorithm the bytecode costs were just too high. After more profiling, it seemed my hand-written hash table, min heap, and list were too expensive even when using a cheaper, less effective algorithm. I needed another approach.

<h3 align="center">Recursive, 3-pronged approach</h3>


<div align="center">
<img src="https://user-images.githubusercontent.com/78166995/150423202-c55fb1eb-977e-4aec-a6c8-0b54af9198b2.PNG">
</div>


I decided I didn't really have to assess in 8 directions, maybe I could just look in three directions from each node. Basically, if my target destination was to the north, I would just consider the tiles that were immediately northeast, north, and northwest. I would then recurse on each of these selected tiles, and consider each of the three adjacent tiles from those. I would continue this recursive procedure, tracking path weights for each step and when I reached a predetermined level of recursion I would pass the weights back up and select the best direction to travel in from the origin - either straight, 45 degrees left, or 45 degrees right.

Here is a simple schematic of the tiles considered in the procedure for two recursive steps:

```
 
 |2| |2| |2| |2| |2| |2| |2|
    \ | /   \ | /   \ | /
     |1|     |1|     |1|
         \    |    /
             |0|
      

```

I found that you could go about 4 levels of recursion in the alotted 10,000 bytecode, meaning you could search up to 4 tiles in front of you. After profiling the different bytecode cost of the various levels of recursion, I then parameterized the movement function to take in the amount of bytecode remaining in the units turn and select the max level of recursion possible.

This algorithm was noticably more effective than bugnav, but not great. For instance, there would be situations where you could easily sidestep a massive rubble patch simply by moving perpendicularly to your target which this procedure didn't take into account. Additionally, the bytecode costs were still quite high! For one thing, we redundantly check the same tiles repeatedly, which is pretty bad. If we reach the 3rd level in our schematic above you'd notice massive redundancy. Also, the overhead of the recursive calls is not negligible.

On the bright side, we can implement this algorithm in 10-20 lines of code :)



<h3 align="center">Unrolled Breadth First Search</h3>


<div align="center">
<img src="https://user-images.githubusercontent.com/78166995/150423214-0a2e6e56-272a-4ea9-8be8-b8042e6b9c3d.PNG">
</div>


My final approach was an unrolled, modified breadth-first search that I 'adapted' from the team in first place. Since we know the sight range of our units at compile time, we know exactly how many tiles we will have to search. This means we can completely unroll our search algorithm. This saves a lot of bytecode, beacuse loop overhead for 1 iteration can cost over 10 bytecode. To perform BFS with a soldier's sight range we iterate over 75 tiles, meaning the loop overhead alone would be 750 bytecode, or 7.5% of our bytecode limit! Additionally, we can modify how we check tiles and make other tweaks for bytecode optimization - a combination of these strategies allowed the bot to search 5 tiles out for a cost of 4,700 bytecode, much better than the previous, recursive approach that only searched 4 tiles out in one direction for twice the cost.

The bad part of this is code length - while the previous recursive strategy could be implemented in 20 lines of code, manually unrolling made this function over 2000 lines long! You can meta program some of it, but it made tweaking or changing the function really not fun and made debugging it almost impossible.

Finally, I implemented a tiered dispatch based on the amount of bytecode left for the unit and made 3 tiers:

1. The max tier was a full search of all visible tiles.
2. The next tier down was just searching the 3/4 circle in front of the unit and ignoring tiles in a wedge directly behind it.
3. The cheapest tier was only searching the tiles that were closer or equidistant to the target, meaning units searched a half-circle of tiles in front of them.

The cheapest BFS strategy cost only 2,500 in the worst case and 1,800 in the usual case which was quite good!


## Communication

As mentioned previously, the only way that units could communicate with eachother was through a shared array containing 64 16-bit unisgned integers. It cost 2 bytecode to read one of the array entries, but 100 bytecode to write into an entry.

To keep things in perspective, the maps could be no smaller than 20x20 and no larger than 60x60, meaning you could specify a tile on the map using just 12 bits (two 6 bit coordinates). However, the largest possible map could contain up to 3600 tiles so there is no way that you could store an entire representation of the map in a 64 integer array - you only have 1024 bits to work with.

#### Ideas and Considerations

My initial thought was to use the array to set exploration targets. Each map is guaranteed to be symmetric, so by knowing where your archons started you can make reasonable assumptions about where the enemy's archons will be. You can place this information into the shared array and have your soldiers proceed to these locations. If the soldier reaches a possible enemy archon location and it is not there, they can remove the location from the array. Alternatively, if they find an enemy archon that is not in the array, they can add its location so all other units know where to go.

This idea generalizes to basically any other location on the map. Since we have 16 bits in each entry, we can use 12 bits for the map coordinates and then 4 bits for any other information we want. Perhaps we want to specify what is at the given location, or when we saw the location, or whether we should approach the location or stay away. 

Alternatively, if we just want to store locations alone without any other bit flags, we could chunk our array into 12 bit pieces and jam even more locations into it. Extending this idea, we can use the map dimensions to decrease the bit length of our coordinates - for instance to store coordinates of a 30x30 map we only require 10 bits instead of 12, so we can fit a few more locations in our 1024 bit array.

While these possibilities were stimulating to consider, I didn't actually use anything too extreme. If I had hard charging teammates and we implemented stuff at blazing speed maybe it would have been necessary to use the array more creatively, but I found that for each piece of information I stuck in the communication array, I spent a lot more time implementing complicated logic for the units to use it.

As an example, I ended up sticking enemy archon locations in the array, allied archons in the array, and spotted enemies in the array.

With this new information, it made targeting much more complex. Rather than simply iterating over enemies and allies in vision range, I now had to do the following in my targeting alrogithm:

1. Collect data - allied archons, enemy archons, known enemy locations from shared array, local enemies the unit can see.
2. Prioritize targets - is an allied archon in need of defense? Should I leave the enemy soldier I can see to run to the archon's defense? What if I see both an archon and an enemy, who should I target?
3. Update locations in the array - if an array location suggests there is an enemy at a location but we see that there is not, units must update this change to be reflected in the array. Alternatively, if we spot an enemy that is not in the array, we must add this location to the array.
4. Finally, we use this information to attack or move our unit.

This is all valuable information, but it increases several things:

1. Computation. Iterating over 64 locations in the array will take 1,000 bytecode (~10 for the loop overhead and ~5 for the array read). Writing up to 5 locations (a realistic assumption if we are both clearing array entries and adding array entries) costs another 500 bytecode. Then we might want to to see if any global targets are in vision range so we can focus fire with allied units. Niavely this makes our algorithm n * m where n is local targets and m is global targets and we check m for membership in n. A realistic assumption is 50 global targets and 10 local targets; now this costs 6,000 bytecode. And if we want to remove any of these 50 locations? 100 bytecode per removal. We can tighten this up by first checking for distance from the global target, which we will have to do anyways to prioritize where to move, but while this decreases our n * m membership check, now we add 500 bytecode to get distances for each global locaton, more for any logic we implement that uses the information. We also want to check the health of possible targets so we can attack the lowest HP enemy, but this adds another 10 bytecode / loop. A main challenge of battlecode this year seemed to be not processing all of the information available, rather you want to prioritize and process the most important information.
2. Lines of code. As the computation of your logic grows, it means you need to start doing bytecode optimizations. These are basically what the compiler normally does for you, but you have to do it by hand. A lot if this involves unrolling loops, creating loops that decrement to 0, passing around variables rather than creating objects, and more. The one thing most of these optimizations have in common is that they make the code unreadable, long, messy, and hard to maintain. Thus as you start to take account of more information you start to complicate your code and make it harder to add new features.

If I wasn't coding my bot alone I may have had the capacity to use the communication array more effectively. I found that I had a lot of ideas for how I wanted to use my shared array - I wanted to track enemy positions, archon positions, unit counts, lead positions, rubble pits, and message flags that could guide a broader macro strategy, but I only ever got around to implementing the first of these 2 ideas.

That being said, I did go through several iterations of how I used the array before reaching my final strategy. This process drove home the importance of abstraction, modularity, and creating an interface - it was much easier to create a function `getEnemyLocation` and hide the implementation than it was to go through the code and change it every time I changed directions for how I was using the array, a lesson I learned early on because at the start I just wrote and read from the array directly rather than using an interface.


You can see how lines of code grew over time - massive spikes were unrolling BFS and dramatic drops are big refactors.

![battlecode_loc](https://user-images.githubusercontent.com/78166995/150365268-575dc2eb-e011-460a-b92d-bfb09becf137.png)

# In Sum

To wrap up, Battlecode was a great experience and it was a lot of fun. I would do it again in the future and I'd encourage all active students to participate if they can over their winter breaks. I learned about 10x more than I detailed here, but this is getting pretty long at this point.
