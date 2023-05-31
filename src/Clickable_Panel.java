import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Clickable_Panel extends JPanel{
    
    public int max_locations = 10;
    public int num_locations = 0;
    public List<Point> locations; 
    
    public Clickable_Panel(){
        this.max_locations = 10;
        this.num_locations = 0; 


        this.locations = new ArrayList<Point>();

        this.setBackground(Color.lightGray);
        addMouseListener(new MouseAdapter() {
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
    }

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);
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