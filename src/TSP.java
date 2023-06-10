
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///A TSP solver, using ant colony optimization. To display the path and calculate the distances, we run A_star. ///
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TSP {

    public Map map;

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
            Ant_Colony_Optimization ants = new Ant_Colony_Optimization(map);
            map.path = ants.path; 
        }

    }

}

