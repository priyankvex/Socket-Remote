import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by priyank on 9/1/15.
 * CLass to show a window when the server is running.
 */
public class ServerWindow {

    //View widgets
    private JFrame frame;
    private JPanel panel;
    private JLabel ip_address_label;
    private JLabel port_number_label;
    private JButton btn_start;
    private JButton btn_stop;

    //Thread that will run the server
    Thread serverThread;
    HostServer mHostServer;

    //global port number
    static int port_number;
    static String ip_address;


    public ServerWindow(){
        ServerWindow.ip_address =  "Not Available. Start the server";
    }


    void showWindow(){
        frame = new JFrame("Socket Remote Server");
        frame.setSize(475, 320);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        panel = new JPanel();
        panel.setSize(450, 300);
        panel.setLayout(null);

        ip_address_label = new JLabel();
        ip_address_label.setText("Server name : " + ip_address);
        ip_address_label.setBounds(20, 20, 500, 50);

        port_number_label = new JLabel();
        port_number_label.setText("Port number of the server : " + port_number);
        port_number_label.setBounds(20, 40, 500, 50);

        btn_start = new JButton("Start Server");
        btn_start.setBounds(100, 20, 200, 50);
        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mHostServer =  new HostServer(port_number);
                    serverThread = mHostServer;
                    serverThread.start();
                } catch (IOException ex) {

                    ip_address_label.setText("Error: Stop and Restart the server");
                    ex.printStackTrace();
                }

                String ip_address_server = mHostServer.getIp_address_server();
                if( ip_address_server != null)
                    ip_address_label.setText("Server name : "  + ip_address_server);
            }
        });

        btn_stop = new JButton("Stop Server");
        btn_stop.setBounds(100, 200, 200, 50);
        btn_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mHostServer.stopServer();
                serverThread.stop();
                serverThread = null;
                System.exit(0);
            }
        });


        ip_address_label.setLocation(20, 20);
        port_number_label.setLocation(20, 90);
        btn_start.setLocation(20, 160);
        btn_stop.setLocation(240, 160);

        panel.add(ip_address_label);
        panel.add(port_number_label);
        panel.add(btn_start);
        panel.add(btn_stop);

        frame.add(panel);

    }



    public static void main(String [] args){

        if(args.length != 0){
            port_number = Integer.parseInt(args[0]);
        }
        else{
            port_number = Integer.parseInt("5000");
        }

        ServerWindow mServerWindow = new ServerWindow();

        mServerWindow.showWindow();

    }

}
