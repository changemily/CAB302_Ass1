import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * Control Panel class
 * Class contains methods for connecting to, receiving and sending info to a server over a port,
 * Hash Method from lecture 9 Q&A
 * @author Emily Chang
 * @version - under development
 */
public class ControlPanelClient {

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

            String request = "Schedule Billboard";
            //String request = "Run Billboard Viewer";
            //request given by user saved in local var request
            switch(request)
            {
                case "Login request":
                    //send username and password to server
                    loginRequest(oos,request);
                    break;

                case "List billboards":
                    listBillboards(oos, request);
                    break;

                case "Get Billboard info":
                    getBillboardInfo(oos, request, "Billboard_3");
                    break;

                case "Create edit billboard":
                    createEditBillboard(oos, request, "sun", "Hello", "#000000",
                            "No Image", "jarod");
                    break;

                case "Delete billboard":
                    deleteBillboard(oos, request, "Billboard_3");
                    break;

                case "View schedule":
                    viewScheduleWrite(oos,request);
                    viewScheduleRead(ois);
                    break;

                case "Schedule Billboard":
                    //Send details of billboard wanting to be scheduled to server

                    String time =LocalDateTime.now().plus(Duration.ofMinutes(1)).toString();
                    System.out.println("time scheduled: " +time);
                    scheduleBillboard(oos, request, "sun",
                            time, "5", "none");
                    break;

                case "Remove Schedule":
                    //Send details of billboard wanting to be scheduled to server
                    removeSchedule(oos,request,"monday",
                            "2020-05-25T10:00:00.00", "5", "none");
                    break;

                case "List users":
                    break;
                case "Create user":
                    break;
                case "Get user permissions":
                    break;
                case "Set user permissions":
                    break;
                case "Set user password":
                    break;
                case "Run Billboard Viewer":
                    //write request to server
                    oos.writeObject(request);
                    //retrieve info of currently displayed billboard
                    //display billboard on viewer
                    break;
            }

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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    /**
     * Hashes byte array of password into hexadecimal code
     * @param password password in the form of array of bytes
     * @return return hashed password
     */
    public static String hash(byte[] password)
    {
        StringBuffer sb = new StringBuffer();
        //for each byte of the password
        for (byte b : password)
        {
            sb.append(String.format("%02x",b &0xFF));
        }
        return sb.toString();
    }

    /**
     * Sends login request, username and password to server
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Contol Panel GUI
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void loginRequest(ObjectOutputStream oos, String buttonClicked) throws NoSuchAlgorithmException, IOException {
        oos.writeObject(buttonClicked);
        //retrieve username and password
        String username ="user";
        String pwd = "password";

        //turn password into bytes
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] passwordBytes = md.digest(pwd.getBytes());

        //hash password
        String hashedPassword = hash(passwordBytes);
        System.out.println("Hashed pwd : " + hashedPassword);

        //send username and hashed password to server
        oos.writeObject(username);
        oos.writeObject(hashedPassword);
    }

    /**
     * Sends List billboards request to the server
     * @param oos Object output stream
     * @param buttonClicked Request given by the control panel GUI
     * @throws IOException
     */
    public static void listBillboards(ObjectOutputStream oos, String buttonClicked)throws IOException{
        //Output clients request to the server
        oos.writeObject(buttonClicked);
    }

    /**
     * Sends a get info request to the server
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Contol Panel GUI
     * @param billboardName What the users wants to name the billboard
     * @param text The text that should be displayed on the billobard
     * @param bg_colour The background colour the billboard should be
     * @param image The string name of the image that will be displayed with the billboard
     * @throws IOException
     */
    public static void createEditBillboard(ObjectOutputStream oos, String buttonClicked, String billboardName, String text,
                                           String bg_colour, String image, String billboard_creator)throws IOException{
        //Write the request to the server
        oos.writeObject(buttonClicked);
        //Write the details to the server
        oos.writeObject(billboardName);
        oos.writeObject(text);
        oos.writeObject(bg_colour);
        oos.writeObject(image);
        oos.writeObject(billboard_creator);
    }

    /**
     * Sends a get info request to the server
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Contol Panel GUI
     * @param billboardName Name of the billboard info requested
     * @throws IOException
     */
    public static void getBillboardInfo(ObjectOutputStream oos, String buttonClicked, String billboardName)throws IOException{
        //Write the request to the server
        oos.writeObject(buttonClicked);
        //Write the billboardname to the server
        oos.writeObject(billboardName);
    }

    /**
     * Sends a delete request to the server
     * @param oos Object output stream of the client
     * @param buttonClicked Request given by control panel GUI
     * @param billboardName Name of the billboard to be deleted
     * @throws IOException
     */
    public static void deleteBillboard(ObjectOutputStream oos, String buttonClicked, String billboardName)throws IOException{
        //Write the request to the server
        oos.writeObject(buttonClicked);
        //Write the billboardName to the server
        oos.writeObject(billboardName);
    }

    /**
     * Sends view schedule request to Server
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Contol Panel GUI
     * @throws IOException
     */
    public static void viewScheduleWrite(ObjectOutputStream oos, String buttonClicked) throws IOException {
        //Write the Client's request to the server
        oos.writeObject(buttonClicked);
    }

    /**
     * Sends view schedule request to Server
     * @param ois Object input stream of client
     * @throws IOException
     */

    public static void viewScheduleRead(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        //read schedule sent by server
        MultiMap schedule = (MultiMap) ois.readObject();

        SwingUtilities.invokeLater(new ControlPanelGUIBillboardSchedule(schedule));
    }

    /**
     * Sends request to schedule billboard to server, and corresponding schedule information
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Control Panel GUI
     * @param billboardName Name of billboard being scheduled
     * @param startTime Start Time of billboard being scheduled
     * @param duration Duration of billboard being scheduled
     * @param recurrence Recurrence of billboard being scheduled
     * @throws IOException
     */
    public static void scheduleBillboard(ObjectOutputStream oos, String buttonClicked, String billboardName,
                       String startTime, String duration, String recurrence) throws IOException {
        //Write the Client's request to the server
        oos.writeObject(buttonClicked);

        //Write the variables of billboard being scheduled to server
        oos.writeObject(billboardName);
        oos.writeObject(startTime);
        oos.writeObject(duration);
        oos.writeObject(recurrence);
    }

    /**
     * Sends request to remove schedule to server, and corresponding schedule information
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Control Panel GUI
     * @param billboardName Name of billboard being removed from schedule
     * @param startTime Start Time of viewing being removed from schedule
     * @param duration Duration of viewing being removed from schedule
     * @param recurrence Recurrence of viewing being removed from schedule
     * @throws IOException
     */
    public static void removeSchedule(ObjectOutputStream oos, String buttonClicked, String billboardName,
                                      String startTime, String duration, String recurrence) throws IOException {
        //Write the Client's request to the server
        oos.writeObject(buttonClicked);

        //Write the variables of billboard being removed to server
        oos.writeObject(billboardName);
        oos.writeObject(startTime);
        oos.writeObject(duration);
        oos.writeObject(recurrence);
    }

    /**
     * Runs client
     * @param args
     */
    public static void main(String args[]) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IOException, IllegalAccessException {
        //SwingUtilities.invokeLater(new ControlPanelGUI());

        Run_Client();
    }
}
