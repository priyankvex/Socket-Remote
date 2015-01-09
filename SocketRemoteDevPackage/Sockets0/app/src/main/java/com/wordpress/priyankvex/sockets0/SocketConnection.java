package com.wordpress.priyankvex.sockets0;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by priyank on 7/1/15.
 * Class to connect to the client to the server.
 */
public class SocketConnection {


    public int client(String address, int port, String command){

        //Storing the server name and port number of the server

        try{

            //instantiating the socket object for the client
            Socket client = new Socket(address, port);

            //Prinitng the server's socket address
            Log.d("SocketConnection", "Just connected to " + client.getRemoteSocketAddress());

            //Output stream for the client socket. Will be used to send messages to the server
            //This is connected to the input stream of the server socket.
            OutputStream outToServer = client.getOutputStream();

            //A data output stream lets an application write primitive Java data types to an output stream in a portable way.
            // An application can then use a data input stream to read the data back in.
            DataOutputStream out = new DataOutputStream(outToServer);

            //command = "gedit\n";

            //Message to be send to server
            out.writeUTF(command);

            //Input stream for the client socket. Will be used to get the messages from the server
            //It is connected to the server socket's output server.
            InputStream inFromServer = client.getInputStream();

            //Just like DataOutputStream
            DataInputStream in = new DataInputStream(inFromServer);

            //Printing the incoming message from the server
            Log.d("SocketConnection", "Server says " + in.readUTF());

            //Closing the client's socket. No connections after this.
            client.close();

        }
        catch(IOException e){

            e.printStackTrace();

            return -1;
        }

        return 0;
    }

}
