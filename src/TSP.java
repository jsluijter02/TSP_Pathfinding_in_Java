import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///A TSP solver, using ant colony optimization. To display the path and calculate the distances, we run A_star. ///
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TSP {

    public Map map;
    private int[][] pheromone;
    private int[][] distances;

    public TSP(Map m){
        this.map = m;
    }


    public void Solve(){

        if(map.num_locations < 2){
            return; 
        }

        else if (map.num_locations == 2){
            A_star A = new A_star(map, map.locations.get(0), map.locations.get(1));
            map.path = A.path; 
        }

        else{
            //return;
            AntColonyOptimization();
        }

    }

    //we use antcolony optimization to find the best combination of routes to take
    private void AntColonyOptimization(){
        
        //TODO: implement a Jframe so the user sees the algorithm is thinking, also give updates 
        JOptionPane.showMessageDialog(null, "Ant Optimization Algorithm in Progress... ", "Optimizer in Progress...", JOptionPane.DEFAULT_OPTION);

        int num_ants = 100;
        ArrayList<Ant> ants = new ArrayList<Ant>();
        boolean terminate = false;
        Point start_point = map.start_point;

        pheromone = new int[map.num_locations][map.num_locations];

        //1. pathfinding with A*
        //initialize a closed graph between each of the points, so the ants know how far each node is from another one.
        //then we can just run the regular ant colony program, treating the roads as if theyre straight. After the ants found the best path, we can draw the paths the A* algo found. 

        A_star[][] a_stars = new A_star[map.num_locations][map.num_locations]; 
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

                map.path.addAll(new_route.path); //TODO remove; this just shows all paths found by the Astar algo
            }
        }

        //2. initialize the ants
        for(int i = 0; i < num_ants; i++){
            ants.add(new Ant(map.start_point)); 
        }

        //3.Run ant optimization
        //now we have the paths' distances we run ant optimization to find the quickest paths
        // while(!terminate){
            
        // } 

        //3. Find quickest path and draw it to the screen
    }

    //a function to calculate the probability a given ant will relocate from point i to j
    private double probability_relocation(int i, int j, Ant ant){
        double alpha = 1;
        double beta = 5;

        double t_ij = pheromone[i][j];
        double eta_ij = 1/distances[i][j];
        double J_ik = 0;

        for(int k = 0; k<map.num_locations; k++){

            int dist = distances[i][k];
            if(dist != 0){
                J_ik += dist; 
            }
        }

        double P = (Math.pow(t_ij, alpha) * Math.pow(eta_ij, beta)) / J_ik;

        return P;
    }

    //a function that updates all pheromone trails of the ants when the tour is over
    private void pheromone_update(){
        //rate of evaporation
        double rho = 1; 

    }
}
