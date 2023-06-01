import java.awt.Point;
import java.util.List;

public class TSP {

    public Clickable_Panel panel; 
    public Map map;
    //panel object? so it can see which things we need

    public TSP(Clickable_Panel c, Map m){
        this.panel = c; 
        this.map = m;
    }

    //TODO implement solving modes
    public void Solve(){
        //get all the locations
        List<Point> locations = map.locations;
        boolean[][] roads = map.roads;
        //TODO check which pixels on the map are road and initialize an array 

    }

    //all 8 directions a pixel can travel to
    private int[][] directions = new int[][]{
        {-1, -1},
        { -1, 0 },
        { -1, 1 },
        { 0, -1},
        { 0, 1 },
        { 1, -1},
        {  1, 0 },
        {  1, 1 }
        };

    //heuristic distance for the points, euclidian distance
    private double distance(Point p1, Point p2){
        return Math.sqrt(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
    }
}
