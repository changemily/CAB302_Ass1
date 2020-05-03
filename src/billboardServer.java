import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Billboard server class
 * Class contains methods for connecting to, receiving and sending info to a database
 * @author Emily Chang
 * @version 1 - Skeleton
 */

public class billboardServer{

    public static void Setup_port(String properties_file){
    //setup port used to connect Server with viewer and control panel
    }

    public static void Start_up() throws IOException {

        Properties props = new Properties();
        FileInputStream fileIn = null;
        int portNumber;
        try {
            //read from network props file
            fileIn = new FileInputStream("./network.props");
            props.load(fileIn);
            fileIn.close();

            //convert port number to int and store in local variable
            String portNumberString = props.getProperty("portNumber");
            portNumber = Integer.parseInt(portNumberString);

            //Establish connection with client
            ServerSocket serverSocket = new ServerSocket(portNumber);

            for (;;) {
                Socket socket = serverSocket.accept();
                System.out.println("connected to"+ socket.getInetAddress());

                //Create Object input and output streams for server
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                //read object sent by client
                Object o = ois.readObject();

                //print what was received from client
                System.out.println("received from client: "+o);

                oos.close();
                ois.close();
                socket.close();
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void Add_table(){
        //Adds tables to database

    }

    public void Server_Connection(){
        //Connect server to billboard viewer and control panel
    }

    /*
    public String Login_request(String username, String password){
        // Sends back error or valid session token depending on outcome of login request
        return ;
    }
    */

    public static void main(String args[]) throws IOException {
        Start_up();
    }
}