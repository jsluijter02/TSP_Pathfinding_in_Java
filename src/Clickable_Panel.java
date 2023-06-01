import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Clickable_Panel extends JPanel{
    
    public int max_locations = 10;
    public int num_locations = 0;
    public List<Point> locations; 

    public BufferedImage fullimg;
    private BufferedImage renderimg;

    private Point og;
    private Point renderimgcoords; 
    
    public Clickable_Panel(){
        this.max_locations = 10;
        this.num_locations = 0; 

        this.locations = new ArrayList<Point>();

        try{
            this.fullimg = ImageIO.read(new File("src/Map/GTAV_ATLUS_2048x2048.png"));
            this.renderimgcoords= new Point(500, 1500);
            this.renderimg = fullimg.getSubimage(renderimgcoords.x,renderimgcoords.y,780,400);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        addMouseListener(new MouseAdapter() {
            //for the mouse dragged method, we need to know the og x and y, so we can calculate how much we change when dragging the map
            @Override
            public void mousePressed(MouseEvent m) {
                og = new Point(m.getPoint()); 
            }

            //when we click the panel we want it to generate a point
            @Override
            public void mouseClicked(MouseEvent m){

                if(num_locations < max_locations)
                {
                    locations.add(new Point(m.getX(), m.getY()));
                    num_locations++; 
                    repaint();
                }
            } 
        });

        //when dragging the mouse, we want to show a different part of the map. 
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override 
            public void mouseDragged(MouseEvent m){

                Point current = m.getPoint();
                int xchange = og.x - current.x; 
                int ychange = og.y - current.y;
                
                renderimgcoords.x = Math.max(0, Math.min(renderimgcoords.x+xchange,fullimg.getWidth() - 780));
                renderimgcoords.y = Math.max(0, Math.min(renderimgcoords.y+ychange,fullimg.getHeight() - 400));

                renderimg = fullimg.getSubimage(renderimgcoords.x, renderimgcoords.y, 780,400);

                og = new Point(current.x, current.y);
                repaint(); 
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);
        g.drawImage(this.renderimg, 0, 0, null);
        g.setColor(Color.black);
        for ( Point loca : locations) {
            g.fillOval(loca.x-5, loca.y-5, 10, 10);
        }

    }

    public void WipePanel(){
        this.num_locations = 0;
        this.locations = new ArrayList<Point>();
        repaint();
    }

}