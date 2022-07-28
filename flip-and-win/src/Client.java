import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private String playerName;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Client(Socket socket, String playerName){
        try{
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.playerName = playerName;
        }
        catch (IOException e){
            closeEverything(socket, reader, writer);
        }
        
    }
        
    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer){
        try{
            if(reader != null){
                reader.close();
            }
            if(writer != null){
                writer.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
     
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Player Name: ");
        String playerName = scanner.nextLine();

        Socket socket = new Socket(host "localhost", port 1234);
        Client client = new Client(socket, playerName);

    }

    
}