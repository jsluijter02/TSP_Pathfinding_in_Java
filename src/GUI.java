import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//A class to start up the GUI for the TSP client
public class GUI{
    public TSP solver;

    public GUI(){
        JFrame frame = new JFrame("TSP");
        Clickable_Panel panel = new Clickable_Panel();
        JButton calc = new JButton("Calculate Shortest Route", null);
        solver  = new TSP(); //possibly add arguments from labels or menu options, and of course, the amount of dots on the screen

        frame.setResizable(false);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        panel.setBounds(10, 10, 780, 400);
        panel.setBackground(Color.lightGray);
        
        calc.setBounds(300, 460, 200, 50);
        calc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                solver.Solve(); 
            }
        });

        frame.add(panel);
        frame.add(calc);
    }

    public static void main(String args[]){
        new GUI();
    }
}