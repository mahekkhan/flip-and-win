import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;

/*
 * Create client connection
 * Set up GUI and buttons
 * Run the game from server
 */
public class Client extends JFrame {
    private int width;
    private int height;
    private Container contentPane;
    private int id;
    private int turnsMade;
    private int maxTurns;
    private int[] values;
    private int myPoints;
    private boolean buttonEnabled;
    private int[] buttonClicked;
    private JButton[] grid;
    private int otherplayer_point;
    private ClientConnect cc;

    // Client class
    // Generate grid and buttons
    public Client(int w, int h) {
        width = w;
        height = h;
        contentPane = this.getContentPane();
        
        grid = new JButton[16];
        for( int i = 0; i < 16; i++) {
            String button_num = String.valueOf(i + 1);
            grid[i] = new JButton(button_num);
        }
        
        turnsMade = 0;
        myPoints = 0;
        values = new int[16];
        buttonClicked = new int[16];
    }

    // Set up gameboard interface 
    public void setUpGUI() {
        this.setSize(width, height);
        this.setTitle("Flip-and-Win Game" + " Player " + id);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(new GridLayout(4, 4, 10, 10));
        
        for( int i = 0; i < 16; i++) {
            contentPane.add(grid[i]);
        }

        if( id == 1)
            buttonEnabled = true;
        else {
            buttonEnabled = false;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    updateTurn();
                }
            });
            t.start();    
        }
        
        toggleButtons();
        this.setVisible(true);
    }

    // Create new player connection
    private void connect_Sever() {
        cc = new ClientConnect();
    }

    // Set up buttons on the gameboard and handle button's action
    public void setUpButtons() {
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JButton b = (JButton) ae.getSource();
                int bNUm = Integer.parseInt(b.getText());
                turnsMade++;
                System.out.println("Turns made: " + turnsMade);
                myPoints += values[bNUm -1];
                System.out.println("Points: " + myPoints);
                buttonEnabled = false;
                toggleButtons();
                cc.bnum_clicked(bNUm);
                String value = String.valueOf(values[bNUm - 1]);
                
                for(int i = 1; i<= 16; i++) {
                    if(bNUm == i) {
                        grid[i-1].setFont(new Font("Arial", Font.BOLD, 20));
                        grid[i-1].setText(value);
                    }
                }
                
                buttonClicked[bNUm - 1] = bNUm;
                if(id == 2 && maxTurns == turnsMade ) {
                    endGame_msg();
                } 
                else {
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            updateTurn();
                        }
                    });
                    t.start(); 
                }}
        };

        for( int i = 0; i < 16; i++) {
            grid[i].addActionListener(al);
        }
    }

    // Enable button when toggled
    public void toggleButtons() {
        for( int i = 0; i < 16; i++) {
            grid[i].setEnabled(buttonEnabled);
        }
    }

    // Print result on player's gameboard
    public void endGame_msg() {
        grid[4].setFont(new Font("Arial", Font.BOLD, 20));
        grid[5].setFont(new Font("Arial", Font.BOLD, 20));
        grid[6].setFont(new Font("Arial", Font.BOLD, 20));
        grid[7].setFont(new Font("Arial", Font.BOLD, 20));
        if(otherplayer_point < myPoints){  
            grid[4].setText("You");
            grid[5].setText("are");
            grid[6].setText("a");
            grid[7].setText("Winner");
        }
        if(otherplayer_point > myPoints) {
            grid[4].setText("Better");
            grid[5].setText("luck");
            grid[6].setText("next");
            grid[7].setText("time");
        }
    }

    // Update player's turn and lock buttons concurrently 
    public void updateTurn() {
        int n = cc.bnum_received();
        System.out.println("Other player clicked button #" + n + ". Your turn.");
        otherplayer_point += values[n-1];
        buttonEnabled = true;
        toggleButtons();
        for(int i = 0; i< 16; i++){
            if( buttonClicked[i] == i + 1)
                grid[i].setEnabled(false);
        }

        // Announce result to player
        if( id == 1 && maxTurns == turnsMade) {
            endGame_msg();
        }
    }

    // Set up client connection
    private class ClientConnect {
        private Socket s;
        private DataInputStream in;
        private DataOutputStream out;

        // Get inputStream and outputStream from Server to read from server.
        public ClientConnect() {
            try{
                s = new Socket("localhost", 2321);
                in = new DataInputStream(s.getInputStream());
                out = new DataOutputStream(s.getOutputStream());
                id = in.readInt();
                System.out.println("Player ID is: " + id);
                maxTurns = in.readInt()/2;
                for(int i = 0; i < 16; i++) {
                    values[i] = in.readInt();
                }
            } catch (IOException ex) {
                System.out.println("ClientConnect");
            }
        }

        // Get button number player clicked from the outputStrema
        public void bnum_clicked(int n) {
            try {
                out.writeInt(n);
                out.flush();
            } catch (IOException ex) {
                System.out.println("bnum_clicked");
            }
        }

        // Get button number the other player clicked from the inputStream
        public int bnum_received() {
            int n = -1;
            try {
                n = in.readInt();
                System.out.println("Another player clicked button: " + n);
                buttonClicked[n-1] = n;
            } catch (IOException ex) {
                System.out.println("bnum_received");
            }
            return n;
        }
    }

    // Main program
    public static void main(String[] args) {
        Client c = new Client(500, 500);
        c.connect_Sever();
        c.setUpGUI();
        c.setUpButtons();
    }
}
