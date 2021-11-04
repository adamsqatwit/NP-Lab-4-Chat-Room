package ChatRoom;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//Chat server object is instantiated by the server thread
public class ChatServer {

    private static final int portNum = 1234;
    private int serverPort;
    private List<ClientThread> clientList; 
    public int numClients;

    
    //Assign server object with provided port
    public ChatServer(int portNumber){
        this.serverPort = portNumber;
    }
    
    //Returns client list to other classes
    public List<ClientThread> getClients(){
        return this.clientList;
    }
    
    
    //Used by the main to initialize the server socket as well as client list
    private void startServer(){
        ServerSocket serverSocket = null;
        clientList = new ArrayList<ClientThread>();
        numClients = 0;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } 
        catch (IOException e){
            System.err.printf("Could  not listen on port: %d\n", serverPort);
        }
    }

    //Accepts clients and starts a thread for them, also adds them to the arrayList
    private void acceptClients(ServerSocket serverSocket){
        System.out.printf("Accepting clients on port: %d\n", serverPort);
        while(true){
            try{
                Socket socket = serverSocket.accept();
                ClientThread client = new ClientThread(socket);
                Thread thread = new Thread(client);
                thread.start();
                this.clientList.add(client);
                numClients++;
            } 
            catch (IOException ex){
                System.out.printf("Failed port: %d ",serverPort);
            }
        }
    }

    //Main method creates server and starts the thread when run
    //Always sets the port to the final int declared in this class
    public static void main(String[] args){
        ChatServer server = new ChatServer(portNum);
        server.startServer();
    }
}
   