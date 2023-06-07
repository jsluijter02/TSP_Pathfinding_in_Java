import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JOptionPane;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///A TSP solver, using ant colony optimization. To display the path and calculate the distances, we run A_star. ///
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TSP {

    public Map map;
    private double[][] pheromone;
    private int[][] distances;

    public TSP(Map m){
        this.map = m;
    }

    //public function to determine whether we can run an algorithm to find the quickest path
    //shortest path between 2 points: run A_star
    //shortest path between more points: run ACO
    public void Solve(){

        if(map.num_locations < 2){
            return; 
        }

        else if (map.num_locations == 2){
            A_star A = new A_star(map, map.locations.get(0), map.locations.get(1));
            map.path = A.path; 
        }

        else{
            AntColonyOptimization();
        }

    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///Ant colony Optimization algorithm, using A_star for the pathfinding/ distances                               ///
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void AntColonyOptimization(){
        
        JOptionPane.showMessageDialog(null, "Ant Optimization Algorithm in Progress... ", "Optimizer in Progress...", JOptionPane.DEFAULT_OPTION);

        int num_ants = 100;
        ArrayList<Ant> ants = new ArrayList<Ant>();
        boolean terminate = false;
        A_star[][] a_stars; 

        pheromone = new double[map.num_locations][map.num_locations];

        //1. pathfinding with A*
        //initialize a closed graph between each of the points, so the ants know how far each node is from another one.
        //then we can just run the regular ant colony program, treating the roads as if theyre straight. After the ants found the best path, we can draw the paths the A* algo found. 

        a_stars = new A_star[map.num_locations][map.num_locations]; 
        distances = new int[map.num_locations][map.num_locations];

        for(int  i = 0; i < map.locations.size(); i++){
            for(int j = i + 1; j < map.locations.size(); j++){

                if (i  == j){
                    distances[i][j] = 0; //if a path is 0, i.e. its the same point or we cant reach it, we skip over it
                    continue; 
                }

                A_star new_route = new A_star(map, map.locations.get(i), map.locations.get(j));

                a_stars[i][j] = new_route;  
                a_stars[j][i] = new_route; 
                distances[i][j] = new_route.path.size();  
                distances[j][i] = new_route.path.size();

            }
        }

        //2. initialize the ants
        for(int i = 0; i < num_ants; i++){
            ants.add(new Ant(0)); 
        }


        //3.Run ant optimization
        //now we have the paths' distances we run ant optimization to find the quickest paths        
        int num_iterations = 0;
        int max_iterations = 5;
        int max_noImprovement = 10; 
        int noImprovement_iterations = 0; 
        while(!terminate){
            
            System.out.println("Iteration: " + num_iterations);
            System.out.println("No improvement count: " + noImprovement_iterations);

            //check whether we should terminate
            //TODO implement the noImprovement clauses
            //TODO implement the update every x iterations to the screen 
            if(num_iterations >= max_iterations || noImprovement_iterations >= max_noImprovement){
                terminate = true; 
                break; 
            }


            //make a complete tour for all the ants
            int visited_locations = 0;
            while(visited_locations < map.num_locations){

                System.out.println("Visited locations: " + visited_locations);

                //for all ants, calculate the next location to go to
                for(Ant ant : ants){

                    int best_relocation = 0; 
                    double best_relocation_prob= Double.NEGATIVE_INFINITY;

                    //make a loop to check which location should be visited next by the ant
                    for(int k = 0; k < map.num_locations; k++){

                        //if the ant has not been to that specific node yet, and it is reachable, we check if we should relocate to that location
                        if(!ant.visited(k) && distances[ant.location][k] != 0){
                            double relocation_probability = probability_relocation(ant.location, k, ant); 
                        
                            if (relocation_probability > best_relocation_prob){
                                best_relocation = k; 
                                best_relocation_prob = relocation_probability; 
                            }

                        }

                    }
                    
                    //update the ants location and route distance
                    if(best_relocation != Double.NEGATIVE_INFINITY){
                        ant.Relocate(best_relocation, distances[ant.location][best_relocation]);
                    }

                }
                
                visited_locations += 1; 
            
            }

            //update pheromone trails
            pheromone_update(ants);

            num_iterations += 1;
        } 

        //4. Find quickest path and draw it to the screen
        quickest_path(a_stars);
    }

    //function that wipes the maps path and concatenates all the quickest found paths by the ants
    private void quickest_path(A_star[][] a_stars){
        map.path = new ArrayList<Point>(); 
        //go from city 0
        int current_city = 0;
        int new_city = 0; 

        //check which city has the highest pheromone concentration from that city, add that index to the path
        int visited_locations = 0;
        HashSet<Integer> visitedSet = new HashSet<Integer>();
        visitedSet.add(0);

        while(visited_locations < map.num_locations){

            double max_pheromone = Double.NEGATIVE_INFINITY; 
            for(int k = 0; k < map.num_locations; k++){

                if(!visitedSet.contains(k) && pheromone[current_city][k] > max_pheromone){
                    new_city = k; 
                }

            }

            System.out.println("Current city: " + current_city);
            System.out.println("New city: " + new_city);

            //then concatenate all the a_stars[i][j]'s together to create the shortest found path
            if(!visitedSet.contains(new_city)){
                map.path.addAll(a_stars[current_city][new_city].path);
                visitedSet.add(new_city);
                current_city = new_city; 
            }

            visited_locations += 1;

        }

        
    }

    //a function to calculate the probability a given ant will relocate from point i to j
    private double probability_relocation(int i, int j, Ant ant){

        double alpha = 1;
        double beta = 5;

        double t_ij = pheromone[i][j];
        double eta_ij = 1/distances[i][j];
        double J_ik = 0;

        for(int k = 0; k<map.num_locations; k++){

            if(!ant.visited(k) && distances[i][k] != 0){
                double t_ik = pheromone[i][k];
                double eta_ik = 1/distances[i][k];
                J_ik += Math.pow(t_ik, alpha) * Math.pow(eta_ik, beta);
            }
        }

        double P = (Math.pow(t_ij, alpha) * Math.pow(eta_ij, beta)) / J_ik;

        System.out.println("for relocation from city "+ i + " to " + j + " :");
        System.out.println("Pheromone: " + t_ij);
        System.out.println("1/Distance: " + eta_ij);
        System.out.println("Sum of feasible moves: " + J_ik);
        System.out.println("Probability: " + P);

        return P;
    }

    //a function that updates all pheromone trails of the ants when the tour is over
    private void pheromone_update(ArrayList<Ant> ants){
        //rate of evaporation
        double rho = 0.5; 

        for (int i = 0; i < map.num_locations; i++){
            for(int j = i+1; j< map.num_locations; j++){
                
                //check how much pheromone to add to a specific edge,
                //this is done by looping over the ants and if they took that edge,
                //how far was their tour?
                double add_pheromone = 0.0;
                for(Ant ant : ants){

                    //TODO is the visited functions with hashcodes causing strange behavior? 
                    if(ant.visited_edge(i,j, distances[i][j])){
                        add_pheromone += 1/ant.route_distance; 
                        System.out.println("Ant " + ant + " visited edge (" + i + ", " + j + "), distance = " + ant.route_distance + ", add_pheromone = " + add_pheromone);
                    } else {
                        System.out.println("Ant " + ant + " did not visit edge (" + i + ", " + j + ")");
                    }
                }

                pheromone[i][j] = (1-rho)*pheromone[i][j]+add_pheromone;

                System.out.println("Updated pheromone for edge (" + i + ", " + j + "): " + pheromone[i][j]);

            }
        }
    }
}
