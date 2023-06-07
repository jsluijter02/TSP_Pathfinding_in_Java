import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Ant {

    public int location; //location on index i of the arraylist of locations in Map class
    public ArrayList<Edge> route;
    public int route_distance;

    public HashSet<Integer> visited_nodes = new HashSet<Integer>();
    public HashSet<Edge> visited_edges = new HashSet<Edge>();


    public Ant(int start_point){
        this.location = start_point;
        this.route = new ArrayList<Edge>();
    }

    public void Relocate(int new_location, int distance){

        Edge edge  = new Edge(location, new_location, distance); 
        this.route.add(edge);
        this.visited_edges.add(edge);

        this.visited_nodes.add(new_location); 
        this.location = new_location;

        this.route_distance += distance; 
    }


    //two visited functions, to check whether the ant has visited a certain city or edge
    //the visited is more for the main ACO algorithm and the visited_edge is meant for the pheromone update trails, 
    //to know which paths get pheromone added to them
    public boolean visited(int i) {
        return this.visited_nodes.contains(i);
    }   

    public boolean visited_edge(int i, int j, int distance) {
        return visited_edges.contains(new Edge(i, j, distance)); 
    }


    //small internal class to represent an edge of the graph
    public class Edge{
        public int i; 
        public int j; 
        public int distance; 

        public Edge(int i, int j, int distance){
            this.i = i;
            this.j = j;
            this.distance = distance; 
        }

        @Override 
        public int hashCode(){
            return Objects.hash(i,j);
        }
    }



}
