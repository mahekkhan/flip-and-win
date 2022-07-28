import java.io.*;
import java.net.*;

public class Client {

    private Socket socket;
    private int playerID;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    public Client(){
        System.out.println("----Client----");
        try{
            socket = new Socket("localhost", 51734);
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            playerID = dataIn.readInt();
            System.out.println("Connected to server as Player #" + playerID + ".");  
        }
        catch (IOException e){
            System.out.println("IO Exception");
            closeEverything(socket, dataIn, dataOut);
        }
        
    }
        
    public void closeEverything(Socket socket, DataInputStream reader, DataOutputStream writer){
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

    }

    
}