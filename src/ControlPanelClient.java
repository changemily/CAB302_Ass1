import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Control Panel class
 * Class contains methods for connecting to, receiving and sending info to a server over a port,
 * Hash Method from lecture 9 Q&A
 * @author Emily Chang
 * @version - under development
 */
public class ControlPanelClient {
    public static String username = "AdminUser";
    public static String sessionToken = "1234";

    /**
     * Sends requests to Server
     */
    public static void Run_Client(String [] user_inputs){
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

            //request given by button clicked on GUI screen
            String request = user_inputs[0];

            switch(request)
            {
                case "Login request":
                    //send username and password to server
                    loginRequest(oos,request);
                    break;

                case "Logout request":
                    //send sessionToken to the server
                    logoutRequest(oos, ois, request);
                    break;

                case "List billboards":
                    listBillboardDetails(oos, ois, request);
                    break;

                case "Get Billboard info":
                    getBillboardInfo(oos, request, user_inputs);
                    break;

                case "Create edit billboard":
                    //Send details of billboard wanting to be created to server
                    createEditBillboard(oos, request, user_inputs);
                    break;

                case "Delete billboard":
                    deleteBillboard(oos, request, user_inputs);
                    break;

                case "View schedule":
                    viewSchedule(oos,request,ois);
                    break;
                case "Schedule Billboard":
                    //Send details of billboard wanting to be scheduled to server
                    scheduleBillboard(oos, request, user_inputs);
                    break;
                case "Remove Schedule":
                    //Send details of billboard wanting to be scheduled to server
                    removeSchedule(oos, request, user_inputs);
                    break;

                case "List users":
                    listUsersScreen(oos, ois, user_inputs);
                    break;
                case "Delete User":
                    deleteUser(oos, request, user_inputs);
                    break;
                case "Create User":
                    createUser(oos, request, user_inputs);
                    break;
                case "Edit User":
                    editUser(oos, request, user_inputs);
            }

            //flush output stream
            oos.flush();

            /*//read response from server
            Object o = ois.readObject();

            //print what was received from server
            System.out.println("received from server: "+o);*/

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hashes byte array of password into hexadecimal code
     * @param password password in the form of array of bytes
     * @return return hashed password
     */
    private static String hash(byte[] password)
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
    private static void loginRequest(ObjectOutputStream oos, String buttonClicked) throws NoSuchAlgorithmException, IOException {
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
     * Sends logout request, session token if the user to server
     * @param oos Object output stream of client
     * @param ois Object Input Stream
     * @param buttonClicked Request given by Contol Panel GUI
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static void logoutRequest( ObjectOutputStream oos, ObjectInputStream ois, String buttonClicked) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        oos.writeObject(buttonClicked);
        //retrieve session token
        //String sessionToken = (String) ois.readObject();
        String sessionToken = "reee";
        System.out.println(sessionToken);
        //send username and hashed password to server
        oos.writeObject(sessionToken);
        //open login screen
        SwingUtilities.invokeLater(new ControlPanelGUILoginScreen());
    }

    /**
     * Sends List billboards request to the server
     * @param oos Object output stream
     * @param ois Object Input Stream
     * @param buttonClicked Request given by the control panel GUI
     * @throws IOException
     */
    private static void listBillboardDetails(ObjectOutputStream oos, ObjectInputStream ois, String buttonClicked) throws Exception {
        //Output clients request to the server
        oos.writeObject(buttonClicked);
        //Read billboard list from server
        HashMap<String, Billboard> BillboardList = (HashMap) ois.readObject();
        // Listing billboards also requires a current user details
        HashMap<String, User> userList = (HashMap) ois.readObject();
        ScheduleMultiMap schedule = (ScheduleMultiMap) ois.readObject();
        User currentUser = UserList.getUserInformation(userList, username);
        //Open the billboard control panel using username, session token and the billboard list
        SwingUtilities.invokeLater(new ControlPanelGUIBillboardControlPanel(username, sessionToken, BillboardList, currentUser, schedule));
    }

    /**
     * Sends a get info request to the server
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Contol Panel GUI
     * @param userInputs String array used for recieving information
     * @throws IOException
     */
    private static void createEditBillboard(ObjectOutputStream oos, String buttonClicked, String[] userInputs)throws IOException{
        //Store the billboard name in a String
        String  billboardName = userInputs[1];
        //Store the billboard creator name in a String
        String billboardCreator = userInputs[2];
        //Store the billboards xml in a String
        String xmlFile = userInputs[3];
        //Write the request to the server
        oos.writeObject(buttonClicked);
        //Write the details to the server
        oos.writeObject(billboardName);
        oos.writeObject(billboardCreator);
        oos.writeObject(xmlFile);
    }

    /**
     * Sends a get info request to the server
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Control Panel GUI
     * @throws IOException
     */
    private static void getBillboardInfo(ObjectOutputStream oos, String buttonClicked, String[] user_inputs)throws IOException{
        //Store the billboard name in a String
        String billboardName = user_inputs[1];
        //Write the request to the server
        oos.writeObject(buttonClicked);
        //Write the billboardName to the server
        oos.writeObject(billboardName);
    }

    /**
     * Sends a delete request to the server
     * @param oos Object output stream of the client
     * @param buttonClicked Request given by control panel GUI
     * @throws IOException
     */
    private static void deleteBillboard(ObjectOutputStream oos, String buttonClicked, String[] user_inputs)throws IOException{
        //Store the billboard name in a String
        String billboardName = user_inputs[1];
        //Write the request to the server
        oos.writeObject(buttonClicked);
        //Write the billboardName to the server
        oos.writeObject(billboardName);
    }

    /**
     * Sends view schedule request to Server and reads response
     * @param ois Object input stream of client
     * @throws IOException
     */
    private static void viewSchedule(ObjectOutputStream oos, String buttonClicked, ObjectInputStream ois) throws Exception {
        //Write the Client's request to the server
        oos.writeObject(buttonClicked);
        //read schedule sent by server
        MultiMap schedule = (MultiMap) ois.readObject();

        HashMap<String, User> userList = (HashMap<String, User>) ois.readObject();
        User userDetails = UserList.getUserInformation(userList, username);

        if(userDetails.Permissions.contains("Schedule Billboards")) {
            Frame[] allFrames = Frame.getFrames();
            for(Frame fr : allFrames){
                if((fr.getClass().getName().equals("ControlPanelGUI"))){
                    fr.dispose();
                    if((fr.getClass().getName().equals("ControlPanelGUI"))){
                        fr.dispose();
                    }
                }
            }
            SwingUtilities.invokeLater(new ControlPanelGUIBillboardSchedule(username, "1234", schedule));
        }
        else{
            JOptionPane.showMessageDialog(new JFrame(),
                    "User doesn't have Schedule Billboards permission");
        }
    }

    /**
     * Sends request to schedule billboard to server, and corresponding schedule information
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Control Panel GUI
     * @throws IOException
     */
    private static void scheduleBillboard(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException {

        String  billboardName= user_inputs[1];
        String startTime = user_inputs[2];
        String duration = user_inputs[3];
        String recurrence = user_inputs[4];

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
     * @throws IOException
     */
    private static void removeSchedule(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException {
        String billboardName= user_inputs[1];
        String startTime = user_inputs[2];
        String duration = user_inputs[3];
        String recurrence = user_inputs[4];


        //Write the Client's request to the server
        oos.writeObject(buttonClicked);

        //Write the variables of billboard being removed to server
        oos.writeObject(billboardName);
        oos.writeObject(startTime);
        oos.writeObject(duration);
        oos.writeObject(recurrence);
    }

//    /**
//     * checks if billboard exists in schedule
//     * @param oos Object output stream of client
//     * @return boolean value - true if billboard exists in schedule, false if not
//     * @throws IOException
//     */
//    private static boolean billboardScheduleCheck (ObjectOutputStream oos, ObjectInputStream ois, String[] user_inputs) throws IOException, ClassNotFoundException {
//        //send server request
//        oos.writeObject(user_inputs[0]);
//        oos.writeObject(user_inputs[1]);
//
//        //read server's response
//        String billboardExists = ois.readObject().toString();
//
//        //if billboard exists in schedule
//        if(billboardExists.equals("true"))
//        {
//            return true;
//        }
//
//        //if billboard does not exist in schedule
//        else
//        {
//            return false;
//        }
//    }

    private static void listUsersScreen(ObjectOutputStream oos, ObjectInputStream ois, String[] user_inputs) throws Exception {
        oos.writeObject(user_inputs[0]);
        HashMap<String, User> userList = (HashMap<String, User>) ois.readObject();
        User userDetails = UserList.getUserInformation(userList, username);
        if(!user_inputs[1].equals("Password")) {
            if (userDetails.Permissions.contains("Edit Users")) {
                Frame[] allFrames = Frame.getFrames();
                for(Frame fr : allFrames){
                    if((fr.getClass().getName().equals("ControlPanelGUI"))){
                        fr.dispose();
                        if((fr.getClass().getName().equals("ControlPanelGUI"))){
                            fr.dispose();
                        }
                    }
                }
                SwingUtilities.invokeLater(new ControlPanelGUIUserControlPanel(username, sessionToken, userList));
            } else {
                JOptionPane.showMessageDialog(new JFrame(),
                        "User doesn't have Edit Users permission");                
            }
        }
        else{
            SwingUtilities.invokeLater(new ControlPanelGUICreateEditUser(username, sessionToken, userDetails, false, true, userList));
        }
    }

    private static void deleteUser(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException{
        String deletedUsername = user_inputs[1];
        oos.writeObject(buttonClicked);
        oos.writeObject(deletedUsername);
    }

    private static void createUser(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException {
        String username = user_inputs[1];
        String password = user_inputs[2];
        String createBillboard = user_inputs[3];
        String scheduleBillboard = user_inputs[4];
        String editBillboard = user_inputs[5];
        String editUsers = user_inputs[6];

        oos.writeObject(buttonClicked);
        oos.writeObject(password);
        oos.writeObject(username);
        oos.writeObject(createBillboard);
        oos.writeObject(scheduleBillboard);
        oos.writeObject(editBillboard);
        oos.writeObject(editUsers);
    }

    private static void editUser(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException {
        String username = user_inputs[1];
        String password = user_inputs[2];
        String createBillboard = user_inputs[3];
        String scheduleBillboard = user_inputs[4];
        String editBillboard = user_inputs[5];
        String editUsers = user_inputs[6];

        oos.writeObject(buttonClicked);
        oos.writeObject(username);
        oos.writeObject(password);
        oos.writeObject(username);
        oos.writeObject(createBillboard);
        oos.writeObject(scheduleBillboard);
        oos.writeObject(editBillboard);
        oos.writeObject(editUsers);
    }
    /**
     * Runs client
     * @param args
     */
    public static void main(String args[]){
        SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
    }
}
