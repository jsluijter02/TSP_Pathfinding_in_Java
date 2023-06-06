import java.awt.Point;
import java.util.Objects;

public class Node implements Comparable<Node>{
    public Point location;
    public Node previous_node; 

    //Scores for the A_star Algorithm
    public double f_score = Double.POSITIVE_INFINITY; 
    public double g_score = Double.POSITIVE_INFINITY; 
    public double h_score = Double.POSITIVE_INFINITY;


    public Node(Point location){
        this.location = location; 
    }

    //for the comparing in A_star
    @Override
    public int compareTo(Node other) {
        return Double.compare(this.f_score, other.f_score); 
    }

    //So we can change the g_score and the algo won't get stuck in an infinite loop, because we changed the object.
    @Override
    public int hashCode() {
        return Objects.hash(location);
    }
}
