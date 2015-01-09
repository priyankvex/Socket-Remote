/**
 * Created by priyankvex on 7/1/15.
 * Class to serve as the server for the app.
 * Usage : Compile and run the file giving port number for the server as CLA
 */
import java.net.*;
import java.io.*;

public class HostServer extends Thread
{

    //ServerSocket object. Has the accept() which waits for the incoming client connection.
    private ServerSocket serverSocket;
    Socket server = null;

    static RunShellCommandFromJava terminal;
    String ip_address_server;

    public HostServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        //Timeout time for the server's socket
        terminal = new RunShellCommandFromJava();
        ip_address_server = InetAddress.getLocalHost().getCanonicalHostName();
    }

    public String getIp_address_server(){
        return ip_address_server;
    }

    //Continuous running thread to check for client connections.
    public void run()
    {
        while(true)
        {
            try
            {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

                //Server object. Stores the reference to the socket object returned by the accept()
                //The returned socket object contains established connection to the client.
                server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                //Data input stream for the server. It is connected to the output stream of the client socket's
                //output stream.
                DataInputStream in = new DataInputStream(server.getInputStream());

                String msg = in.readUTF();

                System.out.println("Client says : " + msg);

                //Executing the terminal command
                terminal.execute(msg);

                //Data output stream to send messages to the client.
                //It is connected to the input stream of the client socket.
                DataOutputStream out = new DataOutputStream(server.getOutputStream());

                //Sending the message
                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");

                //closing the server socket. No connection now.
                server.close();

            }
            catch(IOException e){

                e.printStackTrace();
            }
        }
    }

    public void stopServer(){
        if( server != null){
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


/**
 * Class to execute terminals commands received from client on the terminal of the server
 * Only one process allowed at a time. Others will be queued.
 */
class RunShellCommandFromJava {

    public void execute(String command){

        System.out.println("Executing");

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();

        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        try {
            while((line = reader.readLine()) != null) {
                System.out.print(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
