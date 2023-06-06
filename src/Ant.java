import java.awt.Point;
import java.util.ArrayList;

public class Ant {
    public Point location;
    public ArrayList<Point> route;

    public Ant(Point start_point){
        this.location = start_point;
        this.route = new ArrayList<Point>();
        this.route.add(start_point);
    }

    public void Relocate(Point new_location){
        this.location = new_location;
        this.route.add(new_location);
    }
}
