import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import java.awt.Point;


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///Ant colony Optimization algorithm, using A_star for the pathfinding/ distances                               ///
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Ant_Colony_Optimization {
    
    private double[][] pheromone;
    private int[][] distances;
    private int num_ants = 100;
    private ArrayList<Ant> ants = new ArrayList<Ant>();

    private A_star[][] a_stars; 
    private Map map; 
    public ArrayList<Point> path; 

    public Ant_Colony_Optimization(Map m){

        JOptionPane.showMessageDialog(null, "Ant Optimization Algorithm in Progress... ", "Optimizer in Progress...", JOptionPane.DEFAULT_OPTION);
        this.map = m; 
        this.pheromone =  new double[this.map.num_locations][this.map.num_locations];
        this.distances = new int[this.map.num_locations][this.map.num_locations];
        

        get_A_stars();

        initialize_ants();

        ant_optimizer();
    }

    //1. pathfinding with A*
    //initialize a graph between all of the points, so the ants know how far each node is from another one.
    //then we can just run the regular ant colony program, treating the roads as if theyre straight. After the ants found the best path, we can draw the intricate paths the A* algo found. 
    private void get_A_stars(){

        this.a_stars = new A_star[this.map.num_locations][this.map.num_locations];

        //initialize a loop for all indices that arent the same, ie, the distance is more than 0. 
        //for these get an A_star object, so we have the stored path and we caan get the distances from these paths 
        for(int  i = 0; i < map.locations.size(); i++){
            for(int j = i + 1; j < map.locations.size(); j++){

                A_star new_route = new A_star(map, map.locations.get(i), map.locations.get(j));

                a_stars[i][j] = new_route;  
                a_stars[j][i] = new_route; 
                distances[i][j] = new_route.path.size();  
                System.out.println("Distances i: " +i+ " and j: " + j + " is: " + distances[i][j]);

                distances[j][i] = new_route.path.size();

            }
        }

        //initialize the distances between the same nodes as 0. 
        for(int i = 0; i<map.locations.size(); i++){
            distances[i][i] = 0;
        }

    }

    //2. initialize the ants
    private void initialize_ants(){

        for(int i = 0; i < num_ants; i++){
            ants.add(new Ant(0, map.num_locations)); 
        }

    }

    //3. run the optimization loop
    private void ant_optimizer(){
        int num_iterations = 0;
        int max_iterations = 100;
        int max_noImprovement = 10; 
        int noImprovement_iterations = 0; 
        boolean terminate = false;

        while(!terminate){

            //check if the loop needs to be cut off
            if(num_iterations == max_iterations){
                terminate = true;
                break;
            }

            //have all the ants make a full tour
            int num_locations_visited = 0;
            while(num_locations_visited < map.num_locations){

                for(Ant ant : ants){
                    select_nextLocation(ant);
                }

                num_locations_visited += 1; 
            }

            //After all the ants made a tour, we need to know what the best paths were and how much pheromone to deposit
            pheromone_update();

            num_iterations++; 
        }

        //after we ran the algorithm we want to print the path we found:
        quickest_path();
    }

    //these are extra functions, to help with the optimization and conciseness:
    private void select_nextLocation(Ant ant){

        int best_location = ant.location; 
        double best_relocation_prob = Double.NEGATIVE_INFINITY; 
        ArrayList<Integer> unvisited = new ArrayList<Integer>();

        for(int j = 0; j < map.num_locations; j++){

            if(!ant.visited(j) && distances[ant.location][j] != 0){
                unvisited.add(j);
                double candidate_locationProb = relocation_probability(ant.location, j, ant);

                if(candidate_locationProb > best_relocation_prob){
                    best_location = j; 
                    best_relocation_prob = candidate_locationProb; 
                }
            }
        }

        if(best_relocation_prob == Double.NEGATIVE_INFINITY && unvisited.size() > 0){ //at the beginning all the pheromone = 0
            best_location = unvisited.get(new Random().nextInt(unvisited.size())); 
        }

        //System.out.println("New location: " + best_location);

        ant.Relocate(best_location, distances[ant.location][best_location]);
    }

    //a function to calculate what the probability is an ant will move from location i to location j
    private double relocation_probability(int i, int j, Ant ant){

        double alpha = 1;
        double beta = 5;

        double t_ij = pheromone[i][j];
        double eta_ij = 1/distances[i][j];
        double J_ik = 0;

        for(int k = 0; k<map.num_locations; k++){

            if(distances[i][k] != 0){
                double t_ik = pheromone[i][k];
                double eta_ik = 1/distances[i][k];
                J_ik += Math.pow(t_ik, alpha) * Math.pow(eta_ik, beta);
            }

        }

        double P = (Math.pow(t_ij, alpha) * Math.pow(eta_ij, beta)) / J_ik;

        // System.out.println("for relocation from city "+ i + " to " + j + " :");
        // System.out.println("Pheromone: " + t_ij);
        // System.out.println("1/Distance: " + eta_ij);
        // System.out.println("Sum of feasible moves: " + J_ik);
        // System.out.println("Probability: " + P);

        return P;
    }


    //4. Recalculate how much pheromone to add to each path
    private void pheromone_update(){

        double evaporation_rate = 0.5; 

        for (int i = 0; i < map.num_locations; i++){
            for(int j = i+1; j < map.num_locations; j++){
            
                //check how much pheromone to add to a specific edge,
                //this is done by looping over the ants and if they took that edge,
                //how far was their tour?
                double add_pheromone = 0.0;
                for(Ant ant : ants){
                    if(ant.visited_edge(i,j)){
                        add_pheromone = add_pheromone +  1.0/ant.route_distance; 
                        //System.out.println("Ant " + ant + " visited edge (" + i + ", " + j + "), distance = " + ant.route_distance + ", add_pheromone = " + add_pheromone);
                    } else {
                       // System.out.println("Ant " + ant + " did not visit edge (" + i + ", " + j + ")");
                    }
                }

                pheromone[i][j] = (1-evaporation_rate)*pheromone[i][j]+add_pheromone;

               //System.out.println("Updated pheromone for edge (" + i + ", " + j + "): " + pheromone[i][j]);
            }

        }

    }

    //5. Extract the quickest path from the pheromone trail, so that we can draw that to the screen: 
    private void quickest_path(){
        //initialize the path
        this.path = new ArrayList<Point>();

        //we start at the location the ants started at
        int current_location = 0;
        int new_location = 0;

        boolean[] visited_locations = new boolean[map.num_locations];
        visited_locations[0] = true; 
        int num_locations_visited = 0;

        while(num_locations_visited < map.num_locations){

            double max_pheromone = Double.NEGATIVE_INFINITY; 

            for(int k = 0; k < map.num_locations; k++){
                System.out.println("pheromone from edge: " + current_location + ", " + k + " = " + pheromone[current_location][k]);
                if(!visited_locations[k] && pheromone[current_location][k] > max_pheromone){
                    new_location = k; 
                    max_pheromone = pheromone[current_location][k]; 
                }

            }

            visited_locations[new_location] = true; 
            num_locations_visited ++;

            if(distances[current_location][new_location] != 0){

                A_star A = a_stars[current_location][new_location];

                System.out.println("Going from location: " + current_location + " to: " + new_location);

                current_location = new_location; 

                this.path.addAll(A.path);
            }

        }

    }  
   
}

