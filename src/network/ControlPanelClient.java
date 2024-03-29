package network;

import billboard.Billboard;
import guis.*;
import schedule.MultiMap;
import schedule.ScheduleMultiMap;
import users.User;
import users.UserList;
import users.UserManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Control Panel class
 * Class contains methods for connecting to, receiving and sending info to a server over a port,
 * Hash Method from lecture 9 Q&A
 * @author Emily Chang (Schedule), Jarod Evans (Billboard, Log In / Log Out), Harry Estreich (Users)
 * @version - Final
 */
public class ControlPanelClient {
    public static String username;
    public static String sessionToken;

    /**
     * Sends requests to Server
     */
    public static void runClient(String [] userInputs){
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
            String request = userInputs[0];
            //print control panel message
            System.out.println("Request sent to server: "+request +"\n");

            switch(request)
            {
                case "Login request":
                    //send username and password to server
                    loginRequest(ois, oos, userInputs, request);
                    break;

                case "Logout request":
                    //send sessionToken to the server
                    logoutRequest(oos, userInputs, request);
                    break;

                case "List billboards":
                    listBillboardDetails(oos, ois, request, userInputs);
                    break;

                case "Get Billboard info":
                    getBillboardInfo(oos, request, userInputs);
                    break;

                case "Create edit billboard":
                    //Send details of billboard wanting to be created to server
                    createEditBillboard(oos, request, userInputs);
                    break;

                case "Delete billboard":
                    deleteBillboard(oos, request, userInputs);
                    break;

                case "View schedule":
                    viewSchedule(oos,request,ois,userInputs);
                    break;
                case "Schedule Billboard":
                    //Send details of billboard wanting to be scheduled to server
                    scheduleBillboard(oos, request, userInputs);
                    break;
                case "Remove Schedule":
                    //Send details of billboard wanting to be scheduled to server
                    removeSchedule(oos, request, userInputs);
                    break;

                case "List users":
                    listUsersScreen(oos, ois, userInputs);
                    break;
                case "Delete User":
                    deleteUser(oos, request, userInputs);
                    break;
                case "Create User":
                    createUser(oos, request, userInputs);
                    break;
                case "Edit User":
                    editUser(oos, request, userInputs);
                case "Edit User Keep Password":
                    editUserKeepPassword(oos, request, userInputs);
            }

            //flush output stream
            oos.flush();

            //close streams and connection with server
            oos.close();
            ois.close();
            socket.close();

        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (Exception ex) {
            JOptionPane optionPane = new JOptionPane("Could not connect to Server. Please try again.", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Connection Error");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            SwingUtilities.invokeLater(new GUILoginScreen());
        }
    }

    /**
     * Sends login request, username and password to server
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Control Panel GUI
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static void loginRequest(ObjectInputStream ois, ObjectOutputStream oos, String[] userInputs, String buttonClicked) throws NoSuchAlgorithmException, IOException, ClassNotFoundException, SQLException {
        oos.writeObject(buttonClicked);
        //retrieve username and password
        String username = userInputs[1];
        String pwd = userInputs[2];
        String hashedPassword = UserManager.hashPassword(pwd);

        //send username and hashed password to server
        oos.writeObject(username);
        oos.writeObject(hashedPassword);
        oos.writeObject(userInputs);

        //print control panel message
        System.out.println("Sent to server: \n");
        System.out.println("Username: " +username +"\n");
        System.out.println("Hashed Password: " +hashedPassword +"\n");

        Boolean validUser = ois.readBoolean();
        String SessionToken = (String) ois.readObject();

        //print control panel message
        System.out.println("Retrieved from server: \n");
        System.out.println("Session Token: " +SessionToken +"\n");

        if(validUser == true){
            //If the user is valid set them as the default user in the control panel
            ControlPanelClient.username = username;
            ControlPanelClient.sessionToken = SessionToken;
            //Create and return the user a valid session token
            SwingUtilities.invokeLater(new GUIMainMenu(username, sessionToken));
        }else{
            SwingUtilities.invokeLater(new GUILoginScreen());
        }

    }

    /**
     * Sends logout request, session token if the user to server
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Control Panel GUI
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static void logoutRequest( ObjectOutputStream oos, String[] userInputs, String buttonClicked) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        oos.writeObject(buttonClicked);
        //retrieve session token
        String sessionToken = userInputs[1];
        //send username and hashed password to server
        oos.writeObject(sessionToken);

        //print control panel message
        System.out.println("Sent to server: \n");
        System.out.println("Session Token removed: " +sessionToken +"\n");

        //open login screen
        SwingUtilities.invokeLater(new GUILoginScreen());
    }

    /**
     * Sends List billboards request to the server
     * @param oos Object output stream
     * @param ois Object Input Stream
     * @param buttonClicked Request given by the control panel GUI
     * @throws IOException
     */
    private static void listBillboardDetails(ObjectOutputStream oos, ObjectInputStream ois, String buttonClicked, String[] userInputs) throws Exception {
        //Get the users session token to send to the server
        String sessionToken = userInputs[1];
        //Output clients request to the server
        oos.writeObject(buttonClicked);
        oos.writeObject(sessionToken);

        //print control panel message
        System.out.println("Sent to server: \n");
        System.out.println("Session Token: " +sessionToken +"\n");

        Boolean validSession = ois.readBoolean();
        if(validSession == true){
            //Read billboard list from server
            HashMap<String, Billboard> BillboardList = (HashMap) ois.readObject();
            // Listing billboards also requires a current user details
            HashMap<String, User> userList = (HashMap) ois.readObject();
            ScheduleMultiMap schedule = (ScheduleMultiMap) ois.readObject();

            //print control panel message
            System.out.println("Retrieved from server: \n");
            System.out.println("Billboard List: "+BillboardList +"\n");
            System.out.println("User List: "+userList +"\n");
            System.out.println("Schedule: " +schedule +"\n");

            User currentUser = UserList.getUserInformation(userList, username);
            //Open the billboard control panel using username, session token and the billboard list
            SwingUtilities.invokeLater(new GUIBillboardControlPanel(username, sessionToken, BillboardList, currentUser, schedule));
        }else{
            JOptionPane optionPane = new JOptionPane("Your session has expired," +
                    " please login and try again.", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Session Expired");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            Frame[] allFrames = Frame.getFrames();
            for(Frame fr : allFrames){
                if((fr.getClass().getName().equals("guis.GUIMainMenu"))){
                    fr.dispose();
                }
            }
            SwingUtilities.invokeLater(new GUILoginScreen());
        }
    }

    /**
     * Sends a get info request to the server
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Control Panel GUI
     * @param userInputs String array used for receiving information
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

        //print control panel message
        System.out.println("Sent to server: \n");
        System.out.println("Billboard Name: " +billboardName +"\n");
        System.out.println("Billboard Creator: " +billboardCreator +"\n");
        System.out.println("Xml File: " +xmlFile +"\n");
    }

    /**
     * Sends a get info request to the server
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Control Panel GUI
     * @throws IOException io error
     */
    private static void getBillboardInfo(ObjectOutputStream oos, String buttonClicked, String[] user_inputs)throws IOException{
        //Store the billboard name in a String
        String billboardName = user_inputs[1];
        //Write the request to the server
        oos.writeObject(buttonClicked);
        //Write the billboardName to the server
        oos.writeObject(billboardName);

        //print control panel message
        System.out.println("Sent to server: \n");
        System.out.println("Billboard Name: " +billboardName +"\n");
    }

    /**
     * Sends a delete request to the server
     * @param oos Object output stream of the client
     * @param buttonClicked Request given by control panel GUI
     * @throws IOException io error
     */
    private static void deleteBillboard(ObjectOutputStream oos, String buttonClicked, String[] user_inputs)throws IOException{
        //Store the billboard name in a String
        String billboardName = user_inputs[1];
        //Write the request to the server
        oos.writeObject(buttonClicked);
        //Write the billboardName to the server
        oos.writeObject(billboardName);

        //print control panel message
        System.out.println("Sent to server: \n");
        System.out.println("Billboard Name: " +billboardName +"\n");
    }

    /**
     * Sends view schedule request to Server and reads response
     * @param ois Object input stream of client
     * @throws IOException io error
     */
    private static void viewSchedule(ObjectOutputStream oos, String buttonClicked,
                                     ObjectInputStream ois, String[] userInputs) throws Exception {
        String sessionToken = userInputs[1];
        //Write the Client's request to the server
        oos.writeObject(buttonClicked);
        oos.writeObject(sessionToken);

        //print control panel message
        System.out.println("Sent to server: \n");
        System.out.println("Session Token: " +sessionToken +"\n");

        Boolean validSession = ois.readBoolean();
        // check valid session
        if(validSession){
            //read schedule sent by server
            MultiMap schedule = (MultiMap) ois.readObject();
            HashMap<String, User> userList = (HashMap<String, User>) ois.readObject();
            User userDetails = UserList.getUserInformation(userList, username);

            //print control panel message
            System.out.println("Retrieved from server: \n");
            System.out.println("User List: "+userList +"\n");
            System.out.println("Schedule: " +schedule +"\n");

            // if user has permission to view schedule
            if(userDetails.permissions.contains("Schedule Billboards")) {
                Frame[] allFrames = Frame.getFrames();
                for(Frame fr : allFrames){
                    if((fr.getClass().getName().equals("guis.GUIMainMenu"))){
                        fr.dispose();
                    }
                }
                SwingUtilities.invokeLater(new GUIBillboardSchedule(username, sessionToken, schedule));
            }
            else{ // fail, error
                JOptionPane.showMessageDialog(new JFrame(),
                        "User doesn't have Schedule Billboards permission");
            }
        }else{ // invalid session
            JOptionPane optionPane = new JOptionPane("Your session has expired," +
                    " please login and try again.", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Session Expired");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            // reset to login screen
            Frame[] allFrames = Frame.getFrames();
            for(Frame fr : allFrames){
                if((fr.getClass().getName().equals("guis.GUIMainMenu"))){
                    fr.dispose();
                }
            }
            SwingUtilities.invokeLater(new GUILoginScreen());
        }
    }

    /**
     * Sends request to schedule billboard to server, and corresponding schedule information
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Control Panel GUI
     * @throws IOException io error
     */
    private static void scheduleBillboard(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException {
        // read billboard details
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

        //print control panel message
        System.out.println("Sent to server: \n");
        System.out.println("Billboard Name: " +billboardName +"\n");
        System.out.println("Start Time: " +startTime +"\n");
        System.out.println("Duration: " +duration +"\n");
        System.out.println("Recurrence delay: " +recurrence +"\n");
    }

    /**
     * Sends request to remove schedule to server, and corresponding schedule information
     * @param oos Object output stream of client
     * @param buttonClicked Request given by Control Panel GUI
     * @throws IOException io error
     */
    private static void removeSchedule(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException {
        // read schedule details
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

        //print control panel message
        System.out.println("Sent to server: \n");
        System.out.println("Billboard Name: " +billboardName +"\n");
        System.out.println("Start Time: " +startTime +"\n");
        System.out.println("Duration: " +duration +"\n");
        System.out.println("Recurrence delay: " +recurrence +"\n");
    }

    /**
     * Lists all users and produces a new GUI depending on inputs, either user control panel
     * or edit password screen
     * @param oos Object output stream of client
     * @param ois Object input stream of server
     * @param userInputs user inputs
     * @throws Exception error
     */
    private static void listUsersScreen(ObjectOutputStream oos, ObjectInputStream ois, String[] userInputs) throws Exception {
        oos.writeObject(userInputs[0]);
        //Get the users session token to send to the server
        String sessionToken = userInputs[2];
        //Output clients request to the server
        oos.writeObject(sessionToken);

        //print control panel message
        System.out.println("Sent to server: \n");
        System.out.println("Session Token: " +sessionToken +"\n");

        // if valid session
        Boolean validSession = ois.readBoolean();
        if(validSession){
            // read userList
            HashMap<String, User> userList = (HashMap<String, User>) ois.readObject();

            //print control panel message
            System.out.println("Retrieved from server: \n");
            System.out.println("User List: "+userList +"\n");

            // get details of current user
            User userDetails = UserList.getUserInformation(userList, username);
            if(!userInputs[1].equals("Password")) { // user presses edit users
                if (userDetails.permissions.contains("Edit Users")) { // checks that user can edit users
                    Frame[] allFrames = Frame.getFrames(); // remove main menu
                    for(Frame fr : allFrames){
                        if((fr.getClass().getName().equals("guis.GUIMainMenu"))){
                            fr.dispose();
                        }
                    }
                    // open control panel
                    SwingUtilities.invokeLater(new GUIUserControlPanel(username, sessionToken, userList));
                } else {  // fails permission check
                    JOptionPane.showMessageDialog(new JFrame(),
                            "User doesn't have Edit Users permission");
                }
            }
            else{ // users press change password, no permission check
                SwingUtilities.invokeLater(new GUICreateEditUser(username, sessionToken, userDetails, false, true, userList));
            }
        }else{
            // Fails session check
            // Create error
            JOptionPane optionPane = new JOptionPane("Users session has expired, please log in again.", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Session Token Expired");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            // Reset to login page
            Frame[] allFrames = Frame.getFrames();
            for(Frame fr : allFrames){
                fr.dispose();
            }
            SwingUtilities.invokeLater(new GUILoginScreen());
        }
    }

    /**
     * Method that deletes a user
     * @param oos Object output stream of client
     * @param buttonClicked "Delete User"
     * @param user_inputs username
     * @throws IOException IO error
     */
    private static void deleteUser(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException{
        String deletedUsername = user_inputs[1];
        oos.writeObject(buttonClicked);
        oos.writeObject(deletedUsername);

        System.out.println("Sent to server: \n");
        System.out.println("Username: " + deletedUsername +"\n");

    }

    /**
     * Method for creating a current user
     * @param oos Object output steam of client
     * @param buttonClicked "Create User"
     * @param user_inputs user details
     * @throws IOException IO error
     */
    private static void createUser(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException {
        // user details
        String username = user_inputs[1];
        String password = user_inputs[2];
        String createBillboard = user_inputs[3];
        String scheduleBillboard = user_inputs[4];
        String editBillboard = user_inputs[5];
        String editUsers = user_inputs[6];

        // find server method
        oos.writeObject(buttonClicked);

        // write new details
        oos.writeObject(password);
        oos.writeObject(username);
        oos.writeObject(createBillboard);
        oos.writeObject(scheduleBillboard);
        oos.writeObject(editBillboard);
        oos.writeObject(editUsers);

        System.out.println("Sent to server: \n");
        System.out.println("Username: " + username +"\n");
        System.out.println("Password: " + password +"\n");
        System.out.println("Permissions - 1 if granted, 0 if not\n");
        System.out.println("Create Billboard Permission: " + createBillboard +"\n");
        System.out.println("Schedule Billboard Permission: " + scheduleBillboard +"\n");
        System.out.println("Edit Billboard Permission: " + editBillboard +"\n");
        System.out.println("Edit Users Permission: " + editUsers +"\n");

    }

    /**
     * Method for editing a current user
     * @param oos Object output steam of client
     * @param buttonClicked "Edit User"
     * @param user_inputs user details
     * @throws IOException IO error
     */
    private static void editUser(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException {
        // user details
        String username = user_inputs[1];
        String password = user_inputs[2];
        String createBillboard = user_inputs[3];
        String scheduleBillboard = user_inputs[4];
        String editBillboard = user_inputs[5];
        String editUsers = user_inputs[6];

        // find server method
        oos.writeObject(buttonClicked);

        //write new details
        oos.writeObject(username);
        oos.writeObject(password);
        oos.writeObject(username);
        oos.writeObject(createBillboard);
        oos.writeObject(scheduleBillboard);
        oos.writeObject(editBillboard);
        oos.writeObject(editUsers);

        System.out.println("Sent to server: \n");
        System.out.println("Username: " + username +"\n");
        System.out.println("Password: " + password +"\n");
        System.out.println("Permissions - 1 if granted, 0 if not\n");
        System.out.println("Create Billboard Permission: " + createBillboard +"\n");
        System.out.println("Schedule Billboard Permission: " + scheduleBillboard +"\n");
        System.out.println("Edit Billboard Permission: " + editBillboard +"\n");
        System.out.println("Edit Users Permission: " + editUsers +"\n");
    }

    /**
     * Method for editing a current user but keeps their password
     * @param oos Object output steam of client
     * @param buttonClicked "Edit User Keep Password"
     * @param user_inputs user details
     * @throws IOException IO error
     */
    private static void editUserKeepPassword(ObjectOutputStream oos, String buttonClicked, String[] user_inputs) throws IOException {
        // retrieve inputs for user
        String username = user_inputs[1];
        String createBillboard = user_inputs[2];
        String scheduleBillboard = user_inputs[3];
        String editBillboard = user_inputs[4];
        String editUsers = user_inputs[5];

        // find server method
        oos.writeObject(buttonClicked);

        // delete user
        oos.writeObject(username);

        // record user password
        oos.writeObject(username);

        // write new details
        oos.writeObject(username);
        oos.writeObject(createBillboard);
        oos.writeObject(scheduleBillboard);
        oos.writeObject(editBillboard);
        oos.writeObject(editUsers);

        System.out.println("Sent to server: \n");
        System.out.println("Username: " + username +"\n");
        System.out.println("Permissions - 1 if granted, 0 if not\n");
        System.out.println("Create Billboard Permission: " + createBillboard +"\n");
        System.out.println("Schedule Billboard Permission: " + scheduleBillboard +"\n");
        System.out.println("Edit Billboard Permission: " + editBillboard +"\n");
        System.out.println("Edit Users Permission: " + editUsers +"\n");

    }
    /**
     * Runs client
     * @param args
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new GUILoginScreen());
    }
}
