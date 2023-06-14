public class Ant {

    public int location; //location on index i of the arraylist of locations in Map class
    public int num_locations; 
    public int route_distance;

    private boolean[] visited; 
    private boolean[][] visited_edges; 


    public Ant(int start_point, int num_locations){
        this.location = start_point;

        this.visited = new boolean[num_locations]; 
        this.visited[this.location] = true; 

        this.visited_edges = new boolean[num_locations][num_locations]; 
    }

    //relocate handles the moving of the ant every iteration. 
    public void Relocate(int new_location, int distance){

        this.visited[new_location] = true; 
        this.visited_edges[location][new_location] = true; 
        
        this.location = new_location;

        this.route_distance += distance; 
        
    }


    //two visited functions, to check whether the ant has visited a certain city or edge
    //the visited is more for the main ACO algorithm and the visited_edge is meant for the pheromone update trails, 
    //to know which paths get pheromone added to them
    public boolean visited(int i) {
        return this.visited[i];
    }   

    public boolean visited_edge(int i, int j) {
       return visited_edges[i][j]; 
    }

}
