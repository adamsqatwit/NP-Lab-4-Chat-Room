package ChatRoom;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable {
    private Socket socket;
    private String userName;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;

    //constructor method to set socket and userName
    //Also instantiates the LinkedList used to store messages to send
    public ServerThread(Socket socket, String userName){
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<String>();
    }

    //Adds new messages to the linkedList
    public void addNextMessage(String message){
        synchronized (messagesToSend){
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

    //Handles the work of writing all messages in as well as pushing all messages out
    public void run(){
        System.out.printf("Welcome %s\n",userName);


        try{
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);

            //Loop runs constantly while the server is active
            while(!socket.isClosed()){
            
            
                //Writes incoming messages
                if(serverInStream.available() > 0){
                    if(serverIn.hasNextLine()){
                        System.out.println(serverIn.nextLine());
                    }
                }
                
                //Sends all outgoing messages
                if(hasMessages){
                    String message = "";
                    synchronized(messagesToSend){
                        message = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    serverOut.printf("%s: %s\n", userName, message);
                    serverOut.flush();
                }
            }
        }
        catch(IOException ex){
            System.err.println("Bad Connection!");
        }

    }
}