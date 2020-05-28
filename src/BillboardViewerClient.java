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
 * Control Panel class
 * Class contains methods for connecting to, sending a request for the name of the
 * currently displayed billboard and retrieving the name of the
 * currently displayed billboard from the server over a port,
 * @author Emily Chang
 * @version - under development
 */
public class BillboardViewerClient {
    private static boolean firstTime = true;
    private static JFrame billboardGUI = new JFrame();
    private static JPanel billboardPanel = new JPanel();
    private final static String serverErrorXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "<message>The viewer cannot connect to the server</message>\n" +
            "</billboard>\n";


    private final static String TestFIle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "<message>Billboard displaying</message>\n" +
            "</billboard>\n";
    /**
     * Sends request to server for currently displayed billboard
     */
    public static void runClient(){
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

            //try connecting to server
            try{
                //establish connection with server
                Socket socket = new Socket(hostname,portNumber);
                //Create Object input and output streams for server
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());//String request sent to server
                String request = "Run Billboard Viewer";

                //request billboard currently displayed from server
                oos.writeObject(request);
                //flush output stream
                oos.flush();

                //retrieve name of currently displayed billboard
                String xmlFile = ois.readObject().toString();

                //print what was received from server
                System.out.println("billboard xml: "+ xmlFile);

                //display billboard on viewer
                ViewerGUI(billboardGUI, billboardPanel, xmlFile);
                billboardGUI.pack();
                billboardGUI.setVisible(true);

                //close streams and connection with server
                oos.close();
                ois.close();
                socket.close();
            }
            catch(ConnectException e)
            {
                //display "unable to connect to server" error screen
                ViewerGUI(billboardGUI, billboardPanel, serverErrorXML);
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
     *
     * @param billboardGUI
     * @param billboardPanel
     * @param xmlString
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static void ViewerGUI(JFrame billboardGUI, JPanel billboardPanel, String xmlString) throws IOException, SAXException, ParserConfigurationException {
        if(billboardPanel != null){
            billboardGUI.remove(billboardPanel);
        }
        // Find screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Create new billboard
        BillboardViewer billboard = new BillboardViewer(xmlString, screenSize);

        // Get Panel
        billboardPanel = billboard.getSizedBillboard();

        // Add Panel
        billboardGUI.add(billboardPanel);

        //add listener to detect if a key is pressed on the keyboard
        billboardGUI.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //close viewer
                System.exit(0);
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

        //request current billboard every 15 seconds & update viewer
        timer.schedule(new runViewer(), 0, 15000);
    }
}
