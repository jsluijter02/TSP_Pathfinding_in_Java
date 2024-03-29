import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//A class to start up the GUI for the TSP client
public class GUI{

    public GUI(){

        JFrame frame = new JFrame("Los Santos Travelling Sales Person");
        Map map = new Map();
        
        Clickable_Panel panel = new Clickable_Panel(map);
        JButton calc = new JButton("Calculate Shortest Route", null);
        JButton wipe = new JButton("Delete all locations", null);
        TSP solver  = new TSP(map); 

        frame.setResizable(false);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        panel.setLayout(null);

        panel.setBounds(10, 10, 780, 400);
        
        //calculate shortest route button
        calc.setBounds(150, 460, 200, 50);

        calc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                panel.solve = true; 
                solver.Solve(); 
                panel.repaint();
            }
        });
        
        //wipe button
        wipe.setBounds(450, 460, 200, 50);

        wipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                panel.solve = false; 
                map.delete_locations();
                panel.repaint();
            }
        });

        frame.add(panel);
        frame.add(calc);
        frame.add(wipe);
        frame.setVisible(true);
    }

    public static void main(String args[]){
        new GUI();
    }
}