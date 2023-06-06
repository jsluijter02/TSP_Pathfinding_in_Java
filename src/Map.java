import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Map {

    public BufferedImage map_image; 
    public boolean[][] roads;
    public int max_locations = 5;
    public int num_locations = 0;
    
    public ArrayList<Point> locations; 
    public Point start_point; 

    public ArrayList<Point> path = new ArrayList<Point>(); 

    public Map(){

        //get the maps image
        try{
            this.map_image = ImageIO.read(new File("src/Map/GTAV_ATLUS_2048x2048.png"));
        }

        catch(IOException e){
            e.printStackTrace();
        }

        //initialize locations and roads
        this.locations = new ArrayList<Point>();
        setRoads();
    }

    private void setRoads(){
        //first lets initialize the road array
        roads = new boolean[map_image.getWidth()][map_image.getHeight()];

        for(int i = 0; i < map_image.getWidth(); i++){
            for(int j = 0; j < map_image.getHeight(); j++){

                int color = map_image.getRGB(i, j);
                int[] rgb = getRGBvalues(color);
                roads[i][j] =  isRoad(rgb[0],rgb[1],rgb[2]);

            }
        }
    }

    //a function that takes a img.getrgb int value and calculates the rgb values
    private int[] getRGBvalues(int color){

        int red = (color >> 16) & 0xff;
        int green = (color >> 8) & 0xff;
        int blue = color & 0xff;   
        int[] colors = {red, green, blue};

        return colors; 
    }

    //two functions to decide wether a pixel is a road, the first one checks the array and is for outside of the map class,
    //the second is for within the class, when the map is first initialized
    //TODO!! check if the road is a tunnel, or if it is going underneath another road, but this may be out of the scope for this project right now
    public boolean isRoad(int x, int y)
    {
        return roads[x][y];
    }

    private boolean isRoad(int red, int green, int blue){

        //TODO: find the perfect rgb values, some parts seem to be closed off
        boolean regular = red > 235 && green > 235 && blue > 235;
        boolean highway = (236 <= red && red <= 255) && (231 <= green && green <= 255) && (195 <= blue && blue <= 230); 
        boolean dirtroad = (175 <= red && red <= 207) && (140 <= green && green <= 180) && (90 <= blue && blue <= 130);

        return regular || highway || dirtroad;
    }

    //deletes all the points on the panel
    //TODO remove Astar paths as well.
    public void delete_locations(){
        num_locations = 0;
        locations = new ArrayList<Point>();
        path = new ArrayList<Point>(); 
    }

    //displays path on the screen, if it fits within the coordinates
    public void draw_path(Graphics g, Color c, Point renderimgcoords){
        
        g.setColor(c);
        for(Point p : this.path){
            if(p.x >= renderimgcoords.x && 
            p.x <= renderimgcoords.x+780 && 
            p.y >= renderimgcoords.y && 
            p.y <= renderimgcoords.y + 400)

            g.fillOval(p.x - renderimgcoords.x - 1, p.y - renderimgcoords.y - 1,5, 5);
        }

    }
}
