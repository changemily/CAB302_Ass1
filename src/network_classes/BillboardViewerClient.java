package network_classes;

import billboard_classes.BillboardViewer;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * billboard_classes.Billboard Viewer Client
 * Class contains methods for running the viewer. These include connecting to the server, sending a request for the name of the
 * currently displayed billboard and retrieving the name of the
 * currently displayed billboard from the server over a port.
 * @author Emily Chang & Harry Estreich
 * @version - complete
 */
public class BillboardViewerClient {
    private static boolean firstTime = true;
    private static JFrame billboardGUI = new JFrame();
    private static JPanel billboardPanel = new JPanel();
    //define xml string for unable to connect to server error
    private final static String serverErrorXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "<message>The viewer cannot connect to the server</message>\n" +
            "</billboard>\n";

    /**
     * Sends request to server for currently displayed billboard, retrieves it and displays the billboard
     */
    private static void runClient(){
        Properties props = new Properties();
        FileInputStream fileIn = null;
        int portNumber;
        try {
            //if initial running of viewer
            if(firstTime){
                //set frame with no decorations
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

            //try connecting to server
            try{
                //establish connection with server
                Socket socket = new Socket(hostname,portNumber);
                //Create Object input and output streams for server
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                //String request sent to server
                String request = "Run billboard_classes.Billboard Viewer";

                //request billboard currently displayed from server
                oos.writeObject(request);
                //flush output stream
                oos.flush();

                //retrieve xml file of currently displayed billboard
                String xmlFile = ois.readObject().toString();

                //display billboard on viewer
                viewerGUI(billboardGUI, billboardPanel, xmlFile);
                billboardGUI.pack();
                billboardGUI.setVisible(true);

                //close streams and connection with server
                oos.close();
                ois.close();
                socket.close();
            }
            catch(ConnectException e) //unable to establish connection with server
            {
                //display "unable to connect to server" error screen
                viewerGUI(billboardGUI, billboardPanel, serverErrorXML);
                billboardGUI.pack();
                billboardGUI.setVisible(true);
            }

        } catch (
                FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (
                IOException | ClassNotFoundException | SAXException | ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * populates JPanel with JFrame of billboard xml
     * @param billboardGUI JFrame billboard_classes.Billboard Viewer frame
     * @param billboardPanel JPanel that holds xml display
     * @param xmlString xml string being displayed
     * @throws IOException Thrown if xml string is invalid
     * @throws SAXException Thrown if xml string is invalid
     * @throws ParserConfigurationException Thrown if xml string is invalid
     */
    private static void viewerGUI(JFrame billboardGUI, JPanel billboardPanel, String xmlString) throws IOException, SAXException, ParserConfigurationException {
        //if panel contains a display
        if(billboardPanel != null){
            //clear panel
            billboardGUI.remove(billboardPanel);
        }
        // Find screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Create new billboard viewer of xml
        BillboardViewer billboard = new BillboardViewer(xmlString, screenSize);

        // Get Panel
        billboardPanel = billboard.getSizedBillboard();

        // Add Panel
        billboardGUI.add(billboardPanel);

        //add listener to detect which key is pressed on the keyboard
        billboardGUI.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent actionEvent) {
                //Get key that has been pressed - event source
                int keyPressed = actionEvent.getKeyCode();

                //if escape key is hit
                if(keyPressed == KeyEvent.VK_ESCAPE)
                {
                    //close viewer
                    System.exit(0);
                }
            }
        });

        //add listener to detect mouse clicks
        billboardGUI.addMouseListener(new MouseListener() {
            //when mouse is clicked
            @Override
            public void mouseClicked(MouseEvent e) {
                //close viewer
                System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * Runs viewer request to server every 15 seconds, displays current billboard on viewer GUI
     * @param args
     */
    public static void main(String args[]){
        //create new timer
        Timer timer = new Timer();

        //create timer task that requests current billboard from server and displays it on the viewer
        class runViewer extends TimerTask {
            public void run() {
                try {
                    runClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //request current billboard every 15 seconds & updates viewer
        timer.schedule(new runViewer(), 0, 15000);
    }
}
