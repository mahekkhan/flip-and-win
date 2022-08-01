import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Server {
    private ServerSocket ss;
    private int player_num;
    private ServerConnect player1;
    private ServerConnect player2;
    private ServerConnect player3;
    private ServerConnect player4;
    private int turnsMade;
    private int maxTurns;
    private int[] values;
    private int p1_point;
    private int p2_point;
    private int p3_point;
    private int p4_point;
    private int p1_btn;
    private int p2_btn;
    private int p3_btn;
    private int p4_btn;

    public Server() {
        player_num = 0;
        turnsMade = 0;
        maxTurns = 4;
        values = new int[4];

        for (int i = 0; i < 4; i++) {
            values[i] = (int) Math.ceil(Math.random() * 100);
            System.out.println("Values: " + (i+1) + " is " + values[i]);
        }

        try {
            ss = new ServerSocket(2321);
        } catch (IOException ex) {
            System.out.println("Server");
        }
    }

    public void connect_acc() {
        try {
            System.out.println("waiting...");
            while(player_num < 4) {
                Socket s = ss.accept();
                player_num++;
                System.out.println("Player " + player_num + " has joined");
                ServerConnect sc = new ServerConnect(s, player_num);
                if (player_num == 1)
                    player1 = sc;
                else if (player_num == 2)
                    player2 = sc;
                else if (player_num == 3)
                    player3 = sc;
                else 
                    player4 = sc;
                
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

        public void run() {
            try {
                out.writeInt(playerID);
                out.writeInt(maxTurns);
                out.writeInt(values[0]);
                out.writeInt(values[1]);
                out.writeInt(values[2]);
                out.writeInt(values[3]);
                out.flush();
                while (true) {
                    if(playerID == 1) {
                        p1_btn = in.readInt();
                        System.out.println("p1 clicked button: " + p1_btn);
                        player2.bnum_clicked(p1_btn);
                        player3.bnum_clicked(p1_btn);
                        player4.bnum_clicked(p1_btn);
                        turnsMade++;
                        p1_point += values[p1_btn -1];
                    } else if(playerID == 2) {
                        p2_btn = in.readInt();
                        System.out.println("p2 clicked button: " + p2_btn);
                        player1.bnum_clicked(p2_btn);
                        player3.bnum_clicked(p2_btn);
                        player4.bnum_clicked(p2_btn);
                        turnsMade++;
                        p2_point += values[p2_btn -1];
                    } else if(playerID == 3) {
                        p3_btn = in.readInt();
                        System.out.println("p3 clicked button: " + p3_btn);
                        player2.bnum_clicked(p3_btn);
                        player1.bnum_clicked(p3_btn);
                        player4.bnum_clicked(p3_btn);
                        turnsMade++;
                        p3_point += values[p3_btn -1];
                    } else if(playerID == 4) {
                        p4_btn = in.readInt();
                        System.out.println("p4 clicked button: " + p4_btn);
                        player2.bnum_clicked(p4_btn);
                        player3.bnum_clicked(p4_btn);
                        player1.bnum_clicked(p4_btn);
                        turnsMade++;
                        p4_point += values[p4_btn -1];
                    }
                    if(turnsMade == maxTurns) {
                        Integer[] points = {p1_point, p2_point, p3_point, p4_point};
                        int max = Collections.max(Arrays.asList(points));
                        for(int i = 0; i< 4; i++){
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

    public static void main(String[] args) {
        Server s = new Server();
        s.connect_acc();
    }
}
