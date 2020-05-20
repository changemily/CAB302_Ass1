import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class BillboardViewerClient {

    /**
     * Sends requests to Server
     */
    public static void Run_Client(){
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

            String hostname = props.getProperty("host");

            Socket socket = new Socket(hostname,portNumber);

            //Create Object input and output streams for server
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            String request = "Billboard Viewer";

            //request viewer from server
            oos.writeObject(request);

            //flush output stream
            oos.flush();

            //read response from server
            Object o = ois.readObject();

            //print what was received from server
            System.out.println("received from server: "+o);

            //close streams and connection with server
            oos.close();
            ois.close();
            socket.close();

        } catch (
                FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (
                IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IOException, IllegalAccessException {
        //SwingUtilities.invokeLater(new ControlPanelGUI());

        Run_Client();
    }

}
