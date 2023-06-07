import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

////////////////////////////////////////////////////////////////////////////////////
/// A* algorithm class for pathfinding
////////////////////////////////////////////////////////////////////////////////////

public class A_star {
    Node start; 
    Node end; 

    ArrayList<Point> path; 
    Map map; 

    //representation of the map as nodes, so we can keep track of which node is where? 
    Node[][] nodes; 
    
    public A_star(Map map, Point start, Point end){
        this.map = map; 

        this.start = new Node(start);
        this.start.g_score = 0; 
        this.end = new Node(end);

        this.nodes = new Node[map.map_image.getWidth()][map.map_image.getHeight()];

        this.nodes[start.x][start.y] = this.start;
        this.nodes[end.x][end.y] = this.end; 

        this.path = find_path();
    }

    //the meat of the algorithm, processes the actual loop from 1 path to the other
    public ArrayList<Point> find_path(){

        PriorityQueue<Node> next_up = new PriorityQueue<Node>(); 
        next_up.add(this.start);

        HashSet<Node> visited = new HashSet<Node>();
        
        // Loop for the A* algorithm until we either find the path,
        // or we return nothing if no path is found
        while(!next_up.isEmpty()){

            //1. select (with lowest f(n)), done with compareTo in Node class
            //get node from priority queue
            Node next  = next_up.poll();

            //if the node we selected is the goal node, we return the path, so we can draw it
            if(next.location.equals(this.end.location)){

                System.out.println("End node found!");
                return extract_path(this.end); 

            }

            //if we already processed this node we don't bother
            if(visited.contains(next)){
                continue;
            }

            //move current node to the visited set
            visited.add(next);
            

            //2. expand
            //we check all directions next to the pixel whether we can go there
            for(int[] dir : directions){

                int x = next.location.x + dir[0];
                int y = next.location.y + dir[1];

                //if that direction is a road we check whether we can add it to the queue and what its g,
                //h and f scores would be. If it is not in visited we add it to the queue. 
                if(map.isRoad(x, y)){
                    
                    Node new_node;

                    //construct new nodes and add to adjacent nodes 
                    if(node_empty(x, y)){
                        new_node = new Node(new Point(x,y)); 

                        //calculate f,g and h scores:
                        new_node.g_score = next.g_score + 1; 
                        new_node.h_score = distance(new_node.location, this.end.location);
                        new_node.f_score = new_node.g_score + new_node.h_score; 
                        new_node.previous_node = next; 

                        nodes[x][y] = new_node; 
                    }

                    else{
                        //if we have already seen the node, we get it fron the nodes[x][y] entry, 
                        //and we check if we found a better route
                        new_node = nodes[x][y];

                        double new_g_score = next.g_score + 1; 

                        //if we find a shorter route to the node, we update the g and f scores
                        if(new_g_score < new_node.g_score){
                            new_node.previous_node = next; 
                            new_node.g_score = new_g_score;
                            new_node.f_score = new_node.g_score + new_node.h_score; 
                        }
                    }

                    //we add this new found node to the queue if we haven't 
                    if(!visited.contains(new_node)){
                        next_up.add(new_node);
                    }

                }
            }
        }

        System.out.println("no path found");
        return new ArrayList<Point>();
    }

    //extract_path unravels the path we have taken to get from the start node to the end node
    private ArrayList<Point> extract_path(Node node){
        ArrayList<Point> path = new ArrayList<Point>();

        while(node != null){
            path.add(node.location);
            node = node.previous_node; 
        }

        return path; 
    }

    //a function to check wether we have already seen this node before 
    private boolean node_empty(int x, int y){
        return nodes[x][y] == null; 
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
