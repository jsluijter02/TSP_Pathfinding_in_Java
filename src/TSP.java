import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///A TSP solver, using ant colony optimization. To display the path and calculate the distance, we run A_star. ///
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TSP {

    public Map map;

    public TSP(Map m){
        this.map = m;
    }

    //TODO implement solving modes? do i want different solvers
    public void Solve(){
        //TODO do i need this? 
        List<Point> locations = map.locations;
        boolean[][] roads = map.roads;

        //AntColonyOptimization();
    }

    //we use antcolony optimization to find the best combination of routes to take
    private void AntColonyOptimization(){
        int num_ants = 100;
        boolean terminate = false;
        Point start_point = map.start_point;

        int[][] pheromone = new int[map.map_image.getWidth()][map.map_image.getHeight()];

        //1. pathfinding with A*
        //initialize a closed graph between each of the points, so the ants know how far each node is from another one.
        //then we can just run the regular ant colony program, treating the roads as if theyre straight. After the ants found the best path, we can draw the paths the A* algo found. 

        A_star[] distances = new A_star[map.num_locations*map.num_locations - map.num_locations]; //we calculate all the distances between all points, except between same points, because that distance is 0
        
        //2.Run ant optimization
        //loop for the ant algorithm
        while(!terminate){
            
        } 
    }
}
