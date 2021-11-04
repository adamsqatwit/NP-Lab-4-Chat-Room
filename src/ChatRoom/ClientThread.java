package ChatRoom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    
    //Constructor method to set server object and socket
    public ClientThread(Socket socket){
        this.socket = socket;
    }

    public void run() {
        try{

            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());


            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    PrintWriter thatClientOut = clientOut;
                    if(thatClientOut != null){
                        thatClientOut.write(input + "\r\n");
                        thatClientOut.flush();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Bad Connection!");
        }
    }
}