import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import java.awt.*;

import static java.awt.Frame.MAXIMIZED_BOTH;

public class BillboardViewerClient {
    private static boolean firstTime = true;
    private static String[] billboardArray = {"./1.xml", "./2.xml", "./3.xml", "./4.xml", "./5.xml"}; // only for test
    private static int arrayNo = 0; // only for test
    private static JFrame billboardGUI = new JFrame();
    private static JPanel billboardPanel = new JPanel();
    /**
     * Sends requests to Server
     */
    public static void Run_Client(){
        Properties props = new Properties();
        FileInputStream fileIn = null;
        int portNumber;
        try {
            if(firstTime){
                billboardGUI.setUndecorated(true);
                firstTime = false;
            }
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

            //String request sent to server
            String request = "Run Billboard Viewer";

            //request billboard currently displayed from server
            oos.writeObject(request);
            //flush output stream
            oos.flush();

            //retrieve name of currently displayed billboard
            String billboard_name = ois.readObject().toString();

            //print what was received from server
            System.out.println("billboard being displayed: "+ billboard_name);

            //get xml file name for billboard
            File file = new File("./"+billboard_name+".xml");

            //display billboard on viewer
            ViewerGUI(billboardGUI, billboardPanel, file);
            billboardGUI.pack();
            billboardGUI.setVisible(true);

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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public static void ViewerGUI(JFrame billboardGUI, JPanel billboardPanel, File file) throws IOException, SAXException, ParserConfigurationException {
        if(billboardPanel != null){
            billboardGUI.remove(billboardPanel);
        }
        // Find screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Create new billboard
        BillboardViewer billboard = new BillboardViewer(file, screenSize);

        // Get Panel
        billboardPanel = billboard.getSizedBillboard();

        // Add Panel
        billboardGUI.add(billboardPanel);
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
