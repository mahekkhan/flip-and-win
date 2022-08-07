//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;

class GridF {
    public static void main(String args[]) {

        //Creating the Frame
        JFrame frame = new JFrame();
        frame.setTitle("Grid");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 500);

        //Creating the MenuBar and adding components
       // JMenuBar mb = new JMenuBar();
       // JMenu m1 = new JMenu("Random number generator");
        //JMenu m2 = new JMenu("Help");
       // mb.add(m1);
        //mb.add(m2);
        //JMenuItem m11 = new JMenuItem("Open");
        //JMenuItem m22 = new JMenuItem("Save as");
       // m1.add(m11);
        //m1.add(m22);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(new GridLayout(4,4,10,10)); 

        for (int i=1;i<=16;i++){
            JButton button=new JButton("Button ");
            panel.add(button);
        }

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        //JLabel label = new JLabel("Lower Limit");
       // JTextField tf1 = new JTextField(100); // accepts upto 10 characters
        // JLabel label2 = new JLabel("Upper Limit");
        
        // JTextField tf2 = new JTextField(100); // accepts upto 10 characters
        // JButton generate = new JButton("Generate");
        // JButton clear = new JButton("Clear");
        // panel.add(label);
        // panel.add(tf1);
        // panel.add(label2); // Components Added using Flow Layout
        
        // panel.add(tf2);
        // panel.add(generate);
        // panel.add(clear);

        // // Text Area at the Center
        // JTextArea ta = new JTextArea();

        // //Adding Components to the frame.
        // frame.getContentPane().add(BorderLayout.CENTER, panel);
        // frame.getContentPane().add(BorderLayout.NORTH, mb);
        // frame.getContentPane().add(BorderLayout.SOUTH, ta);
        // frame.setVisible(true);
    }
}