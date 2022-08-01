import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
//import javax.swing.JFrame.*;

public class Client extends JFrame {
    private int width;
    private int height;
    private Container contentPane;
    private JTextArea message;
    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton b4;
    private int id;
    private int turnsMade;
    private int maxTurns;
    private int[] values;
    private int myPoints;
    private boolean buttonEnabled;

    private ClientConnect cc;

    public Client(int w, int h) {
        width = w;
        height = h;
        contentPane = this.getContentPane();
        message = new JTextArea();
        b1 = new JButton("1");
        b2 = new JButton("2");
        b3 = new JButton("3");
        b4 = new JButton("4");
        turnsMade = 0;
        myPoints = 0;
        values = new int[4];
    }

    public void setUpGUI() {
        this.setSize(width, height);
        this.setTitle("Turn-based Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(new GridLayout(1, 5));
        contentPane.add(message);
        message.setText("Player " + id);
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);
        contentPane.add(b1);
        contentPane.add(b2);
        contentPane.add(b3);
        contentPane.add(b4);

        if (id == 1)
            buttonEnabled = true;
        else if (id == 2) {
            buttonEnabled = false;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    updateTurn();
                }
            });
            t.start();
        } else if (id == 3) {
            buttonEnabled = false;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    updateTurn();
                }
            });
            t.start();
        } else if (id == 4) {
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

    private void connect_Sever() {
        cc = new ClientConnect();
    }

    public void setUpButtons() {
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JButton b = (JButton) ae.getSource();
                int bNUm = Integer.parseInt(b.getText());
                turnsMade++;
                System.out.println("Turns made: " + turnsMade);
                myPoints += values[bNUm - 1];
                System.out.println("Points: " + myPoints);
                buttonEnabled = false;
                toggleButtons();
                cc.bnum_clicked(bNUm);

                Thread t = new Thread(new Runnable() {
                    public void run() {
                        updateTurn();
                    }
                });
                t.start();
            }
        };

        b1.addActionListener(al);
        b2.addActionListener(al);
        b3.addActionListener(al);
        b4.addActionListener(al);

    }

    public void toggleButtons() {
        b1.setEnabled(buttonEnabled);
        b2.setEnabled(buttonEnabled);
        b3.setEnabled(buttonEnabled);
        b4.setEnabled(buttonEnabled);
    }

    public void updateTurn() {
        int n = cc.bnum_received();
        System.out.println("your turn.");
        buttonEnabled = true;
        toggleButtons();
    }

    private class ClientConnect {
        private Socket s;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientConnect() {
            try {
                s = new Socket("localhost", 2322);
                in = new DataInputStream(s.getInputStream());
                out = new DataOutputStream(s.getOutputStream());
                id = in.readInt();
                System.out.println("Player ID is: " + id);
                maxTurns = in.readInt() / 4;
                values[0] = in.readInt();
                values[1] = in.readInt();
                values[2] = in.readInt();
                values[3] = in.readInt();
            } catch (IOException ex) {
                System.out.println("ClientConnect");
            }
        }

        public void bnum_clicked(int n) {
            try {
                out.writeInt(n);
                out.flush();
            } catch (IOException ex) {
                System.out.println("bnum_clicked");
            }
        }

        public int bnum_received() {
            int n = -1;
            try {
                n = in.readInt();
                System.out.println("Another player clicked button: " + n);
            } catch (IOException ex) {
                System.out.println("bnum_received");
            }
            return n;
        }
    }

    public static void main(String[] args) {
        Client c = new Client(500, 100);
        c.connect_Sever();
        c.setUpGUI();
        c.setUpButtons();
    }
}
