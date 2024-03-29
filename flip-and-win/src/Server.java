import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;

/*
 * Server class to handle multiple clients connecting
 */
public class Server {
    private ServerSocket ss;
    private int player_num;
    private ServerConnect player1;
    private ServerConnect player2;
    private int turnsMade;
    private int maxTurns;
    private int[] values;
    private int p1_point;
    private int p2_point;
    private int p1_btn;
    private int p2_btn;

    //Server class constructor
    public Server() {
        player_num = 0;
        turnsMade = 0;
        maxTurns = 16;
        values = new int[16];

        //Initialize the grid with random values and display the values in terminal
        for (int i = 0; i < 16; i++) {
            values[i] = (int) Math.ceil(Math.random() * 100);
            System.out.println("Values: " + (i+1) + " is " + values[i]);
        }

        try {
            ss = new ServerSocket(2321);
        } catch (IOException ex) {
            System.out.println("Server");
        }
    }

    /*
     * Function to connect clients
     * Displays in terminal which player has joined
     */
    public void connect_acc() {
        try {
            System.out.println("waiting...");
            while(player_num < 2) {
                Socket s = ss.accept();
                player_num++;
                System.out.println("Player " + player_num + " has joined");
                ServerConnect sc = new ServerConnect(s, player_num);
                if (player_num == 1)
                    player1 = sc;
                else
                    player2 = sc;
                
                Thread t = new Thread(sc);
                t.start();
            }
            System.out.println("Max players joined.");
        } catch (IOException ex) {
            System.out.println("connect_acc");
        }
    }

    private class ServerConnect implements Runnable {
        private Socket s;
        private DataInputStream in;
        private DataOutputStream out;
        private int playerID;

        public ServerConnect(Socket socket, int id) {
            s = socket;
            playerID = id;
            try{
                in = new DataInputStream(s.getInputStream());
                out = new DataOutputStream(s.getOutputStream());
            } catch (IOException ex) {
                System.out.println("ServerConnect");
            }
        }

        /* 
         * Server class run function
         */
        public void run() {
            try {
                out.writeInt(playerID);
                out.writeInt(maxTurns);
                for(int i = 0; i < 16; i++) {
                    out.writeInt(values[i]);
                }
                out.flush();

                //Handle multiple players
                while (true) {
                    if(playerID == 1) {
                        p1_btn = in.readInt();
                        System.out.println("p1 clicked button: " + p1_btn);
                        player2.bnum_clicked(p1_btn);
                        turnsMade++;
                        p1_point += values[p1_btn -1];
                    } else {
                        p2_btn = in.readInt();
                        System.out.println("p2 clicked button: " + p2_btn);
                        player1.bnum_clicked(p2_btn);
                        turnsMade++;
                        p2_point += values[p2_btn -1];
                    } 

                    //Show winner in the terminal and winner's points
                    if(turnsMade == maxTurns) {
                        Integer[] points = {p1_point, p2_point};
                        int max = Collections.max(Arrays.asList(points));
                        for(int i = 0; i< 2; i++){
                            if(max == points[i]) {
                                System.out.println("Player " + (i + 1) + " wins with " + max + " point!");
                                break;
                            }
                        }

                    }
                }
            } catch (IOException ex) {
                System.out.println("run");
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
    }

    //Main function
    public static void main(String[] args) {
        Server s = new Server();
        s.connect_acc();
    }
}
