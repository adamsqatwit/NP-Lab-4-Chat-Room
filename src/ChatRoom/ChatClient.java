package ChatRoom;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    private static final String host = "localhost";
    private static final int portNumber = 1234;

    private String userName;
    private String serverHost;
    private int serverPort;
    private Scanner userInputScanner;

    public static void main(String[] args){
    
        String readName = null;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input username:");

        ChatClient client = new ChatClient(readName, host, portNumber);
        client.startClient(scan);
    }

    private ChatClient(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    private void startClient(Scanner scan){
        try{
            Socket socket = new Socket(serverHost, serverPort);

            ServerThread serverThread = new ServerThread(socket, userName);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive()){
                if(scan.hasNextLine()){
                    serverThread.addNextMessage(scan.nextLine());
                }
            }
        }catch(IOException ex){
            System.err.println("Bad Connection!");
        }
    }
}