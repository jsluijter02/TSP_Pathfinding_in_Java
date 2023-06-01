import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

public class TSP {

    public Clickable_Panel panel; 
    //panel object? so it can see which things we need

    public TSP(Clickable_Panel c){
        this.panel = c; 
    }

    public void Solve(){
        //get all the locations
        List<Point> locations = panel.locations;
        BufferedImage map = panel.fullimg; 
        
    }

}
