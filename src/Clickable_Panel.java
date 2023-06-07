import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Clickable_Panel extends JPanel{
    
    public Map map;
    private BufferedImage renderimg;

    private Point og;
    private Point renderimgcoords; 

    public boolean solve = false; 

    public Clickable_Panel(Map map){

        this.map = map;

        //Get the part of the map that we want to render to the screen
        this.renderimgcoords= new Point(500, 1500);
        this.renderimg = map.map_image.getSubimage(renderimgcoords.x,renderimgcoords.y,780,400);


        //
        // get mouse commands, so we know when to add a location and when to scroll the map to a new map fragment
        //
        addMouseListener(new MouseAdapter() {
            //for the mouse dragged method, we need to know the og x and y, so we can calculate how much we change when dragging the map
            @Override
            public void mousePressed(MouseEvent m) {
                og = new Point(m.getPoint()); 
            }

            //when we click the panel we want it to generate a point, but only if we have less than the max points,
            //and the point is clicked on a road
            @Override
            public void mouseClicked(MouseEvent m){

                if(map.num_locations < map.max_locations)
                {
                    //calculate what the location is on the full map 
                    int locationx = renderimgcoords.x + m.getX();
                    int locationy = renderimgcoords.y + m.getY();

                    boolean is_road = map.isRoad(locationx, locationy);

                    //if the location clicked is a road, we add it to the list of locations
                    if(is_road){
                        Point new_location = new Point(locationx, locationy);
                        map.locations.add(new_location);

                        if(map.num_locations == 0){
                            map.start_point = new_location;
                        }

                        map.num_locations++; 
                        solve = false;
                        repaint();
                    }
                }
            } 
        });


        //when dragging the mouse, we want to show a different part of the map. 
        addMouseMotionListener(new MouseMotionAdapter() {
            
            //mousedragged changes the part of the map shown
            @Override 
            public void mouseDragged(MouseEvent m){

                //it gets the current point where the mouse is and checks against the original point how far the mouse has travelled
                Point current = m.getPoint();
                int xchange = og.x - current.x; 
                int ychange = og.y - current.y;
                
                //calculate the new image coords
                renderimgcoords.x = Math.max(0, Math.min(renderimgcoords.x+xchange,map.map_image.getWidth() - 780));
                renderimgcoords.y = Math.max(0, Math.min(renderimgcoords.y+ychange,map.map_image.getHeight() - 400));

                renderimg = map.map_image.getSubimage(renderimgcoords.x, renderimgcoords.y, 780,400);

                og = new Point(current.x, current.y);
                repaint(); 

            }

        });

    }


    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);
        g.drawImage(this.renderimg, 0, 0, null);


        if(solve){
            map.draw_path(g, Color.orange, renderimgcoords);
        }

        g.setColor(Color.black);

        //checks whether the point should be drawn on screen by checking its coordinates and then drawing the on screen 
        for ( Point loca : map.locations) {

            if(loca == map.start_point){
                g.setColor(Color.red);
            }
            else{
                g.setColor(Color.black);
            }
            //if they fall between the rendered image coordinates, we want to draw them to the screen
            if(insideRenderBounds(loca.x, loca.y)){

                g.fillOval(loca.x-renderimgcoords.x-5, loca.y-renderimgcoords.y-5, 10, 10);

            }
        }

    }

    //TODO remove this before final implementation
    //function that makes all the road pixels black
    private void highlightRoads(Graphics g, Color c){
        g.setColor(c);
        for(int i = 0; i < map.map_image.getWidth(); i++){

            for(int j = 0; j < map.map_image.getHeight(); j++){

                if(map.isRoad(i, j) && insideRenderBounds(i, j)){
                    g.fillRect(i - renderimgcoords.x, j-renderimgcoords.y, 1, 1);
                }
            }
        }
    }

    //a function that helps to decide wether coords are inside of the render box or not
    private boolean insideRenderBounds(int x, int y){
            
        return x >= renderimgcoords.x && 
            x <= renderimgcoords.x+780 && 
            y >= renderimgcoords.y && 
            y <= renderimgcoords.y + 400;

    }
}