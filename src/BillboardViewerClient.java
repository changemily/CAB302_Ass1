import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
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

            String request = "Run Billboard Viewer";

            //request billboard currently displayed from server
            oos.writeObject(request);

            //flush output stream
            oos.flush();

            //retrieve name of currently displayed billboard
            String billboard_name = ois.readObject().toString();

            //print what was received from server
            System.out.println("billboard being displayed: "+ billboard_name);

            /*//get xml file name for billboard
            File file = new File("./"+billboard_name+".xml");


            //display billboard on viewer
            BillboardViewer BV = new BillboardViewer(file, true);
*/
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
        } /*catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }*/

    }

    public static void main(String args[]) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IOException, IllegalAccessException {
        //SwingUtilities.invokeLater(new ControlPanelGUI());

        //create new timer
        Timer timer = new Timer();

        //create timer task that requests current billboard from server
        class runViewer extends TimerTask {
            public void run() {
                try {
                    Run_Client();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //request current billboard every 15 seconds & update viewer
        timer.schedule(new runViewer(), 0, 15000);

    }

}
