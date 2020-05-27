import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
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
            String billboardName = ois.readObject().toString();

            //print what was received from server
            System.out.println("billboard being displayed: "+ billboardName);

            //get xml file name for billboard
            File file = new File("./"+billboardName+".xml");

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

    /**
     *
     * @param billboardGUI
     * @param billboardPanel
     * @param file
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static void ViewerGUI(JFrame billboardGUI, JPanel billboardPanel, File file) throws IOException, SAXException, ParserConfigurationException {
        if(billboardPanel != null){
            billboardGUI.remove(billboardPanel);
        }
        // Find screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Create new billboard
        //BillboardViewer billboard = new BillboardViewer(file, screenSize);

        // Get Panel
        //billboardPanel = billboard.getSizedBillboard();

        // Add Panel
        billboardGUI.add(billboardPanel);
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
