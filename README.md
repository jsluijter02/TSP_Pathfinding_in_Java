## A project in works for a TSP visualization/ Pathfinding in Java

This project is a unique combination of A* pathfinding algorithm and Ant Colony Optimization (ACO) to navigate through the map of Los Santos from Grand Theft Auto V. It aims to find the most efficient route between up to five different locations.

## How does it work? 

The program begins by interpreting the Los Santos map's RGB values, to find out which pixels are roads. These are stored in a boolean array to be used by A*. 

There are two different solving modes:
1. if just two locations are selected, only A* pathfinding will be deployed independently, as ACO will have little use. 
2. if more than two locations are selected, the algorithm is more complicated. 

It starts off with A* pathfinding to determine the initial feasible paths between all the points, and getting their distances. This way, the roads can be treated as straight lines, so the ants have a simpler graph to traverse. The distances between each location are calculated based on these  A* paths.

Then, Ant Colony Optimization (ACO) is used to optimize the path selection. It simulates the behavior of a colony of ants traversing the map. Each ant independently selects a path based on two factors: the amount of pheromones, which is deposited according to the routes success, and the distance to that location. Over several iterations, this process finds a (close to) optimal path.

By combining these two algorithms, the program can determine the shortest route between various locations in Los Santos. 

## Improvements

Some improvements could be made to the program, to speed things up and to make the end result look and feel better. 

For instance, 
1. A-star efficiency. Right now, the program treats all the pixels as separate nodes, determined by their RGB value. The map image is 2048x2048, so as you can imagine this can get a bit more slow for the path finding process. A solution to this would be to treat the intersections of the roads as nodes and calculate the distance between those nodes via A-star, rather than between all the pixels. I tried to get my hands on the path.xml file in the GTAV native program, but I could not access it, due to the necessary mod being taken down. This would also make the implementation of traffic rules possible. 
2. Dynamic path updates, update the best path every x amount of iterations.
3. Multi-path support, let the user choose their favorite of a few paths, like in Google Maps. 
4. Improved stopping criterion, now it just checks the amount of iterations, but maybe employ early stopping if there are no improvements.

