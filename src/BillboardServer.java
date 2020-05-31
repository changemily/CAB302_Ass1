import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Timer;

/**
 * Billboard server class
 * Class contains methods for connecting to, receiving and sending info to a client over a port,
 * @author Emily Chang
 * @version 1 - under development
 */

public class BillboardServer {

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS Users (username varchar(255) PRIMARY KEY,password varchar(255), salt varchar(225), createBillboard INT, editBillboards INT, scheduleBillboards INT, editUsers INT)";

    private static final String CREATE_BILLBOARD_TABLE =
            "CREATE TABLE IF NOT EXISTS Billboards (billboardName varchar(255), billboardCreator varchar (255), xmlFile text);";

    private static final String CREATE_SCHEDULE_TABLE =
            "CREATE TABLE IF NOT EXISTS Schedule (billboardName varchar(255), startTimeScheduled varchar(50), " +
                    "Duration varchar (255), recurrenceDelay varchar (50), billboardCreator varchar (255));";

    private static final String ADD_DEFAULT_USER =
            "INSERT INTO Users (username, password, salt, createBillboard, editBillboards, scheduleBillboards, editUsers)"  +
            "VALUES(\""+"AdminUser"+"\",\""+"19513fdc9da4fb72a4a05eb66917548d3c90ff94d5419e1f2363eea89dfee1dd"+"\",\""+"Salt"+"\",\""+1+"\",\""+1+"\",\""+1+"\",\""+1+"\")" +
                    "ON DUPLICATE KEY UPDATE username = \""+"AdminUser"+"\";";

    //queue of billboard viewings - 2D array
    private static String [][] queue = new String [0][0];

    private static final String noViewingXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "    <message>There are no billboards displayed at this time</message>\n" +
            "</billboard>";

    //Setup another hashmap to store an id and hasmap of the token and its timer
    public static HashMap<Integer, Timer> SessionCombinedHashmap;
    static{
        SessionCombinedHashmap = new HashMap<>();
    }
    //Setup a hashmap to store each hasmap with a timer
    public static HashMap<Integer, String> SessionTokenListHashmap;
    static{
        SessionTokenListHashmap = new HashMap<>();
    }

    /**
     * Starts up Billboard server for connection to client
     * Sends and Receives information from client
     */
    private static void runServer() throws Exception {
        //create empty schedule, billboard list and user list
        ScheduleMultiMap billboardSchedule = new ScheduleMultiMap();
        BillboardList billboardList = new BillboardList();
        UserList userList = new UserList();

        //create DB connection
        Connection connection = null;

        connection = DBconnection.getInstance();

        //check if tables exist in DB, if not adds tables
        checkTables(connection);

        //populate schedule, billboard list and user list with data from database
        billboardSchedule.retrieveDBschedule(connection);
        billboardList.RetrieveDBbillboardList(connection);
        userList.retrieveUsersFromDB(connection);

        //populate queue with schedule
        populateQueue(connection);

        //Establish new properties file
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

                //read request sent by client
                String clientRequest = ois.readObject().toString();

                //print what was received from client in server command line
                System.out.println("request received from client: "+clientRequest);

                //save return message, based on what request was received from the client
                switch(clientRequest)
                {
                    case "Login request":
                        //retrieve username
                        //String username = ois.readObject().toString();
                        //retrieve hashed pwd from client
                        //String password = ois.readObject().toString();

                        //System.out.println("Username: " +username);
                        //System.out.println("Password: " +password);

                        saltAndCheckUserCredentials(ois, connection);

                        //retrieve salted pwd from DB
                        //unsalt pwd
                        //check if hashed pwd match
                        //if match return session token
                        //else returns error to client
                        break;
                    case "Logout request":
                        //retrieve sessionToken
                        //String sessionToken = ois.readObject().toString();
                        //System.out.println("Username: " +sessionToken);
                        logoutRequest(oos, ois);
                        break;
                    case "List billboards":
                        //write billboard list to client
                        billboardSchedule.retrieveDBschedule(connection);
                        billboardList.RetrieveDBbillboardList(connection);
                        userList.retrieveUsersFromDB(connection);
                        listBillboards(oos, billboardList, userList, billboardSchedule);
                        break;
                    case "Get Billboard info":
                        //write billboard info to client
                        getBillboardInfo(oos, ois, billboardList);
                        break;
                    case "Create edit billboard":
                        //Write the new billboard to the DB
                        createEditBillboard(ois, connection, billboardList);
                        break;
                    case "Delete billboard":
                        //Write the delete to the db
                        deleteBillboard(ois, connection, billboardList);
                        break;
                    case "View schedule":
                        billboardList.RetrieveDBbillboardList(connection);
                        userList.retrieveUsersFromDB(connection);
                        viewSchedule(oos,billboardSchedule, userList);
                        break;

                    case "Schedule Billboard":
                        //schedule billboard
                        scheduleBillboard(ois, connection, billboardList, billboardSchedule);
                        billboardSchedule.retrieveDBschedule(connection);
                        break;

                    case "Remove Schedule":
                        //remove viewing from schedule
                        removeSchedule(ois,connection,billboardSchedule, billboardList);
                        break;
                    case "List users":
                        userList.retrieveUsersFromDB(connection);
                        listUsers(oos, userList);
                        break;
                    case "Create User":
                        userList.retrieveUsersFromDB(connection);
                        createUser(ois, connection, userList);
                        break;
                    case "Delete User":
                        userList.retrieveUsersFromDB(connection);
                        deleteUser(ois, connection, userList);
                        break;
                    case "Edit User":
                        userList.retrieveUsersFromDB(connection);
                        deleteUser(ois, connection, userList);
                        createUser(ois, connection, userList);
                        break;
                    case "Edit User Keep Password":
                        userList.retrieveUsersFromDB(connection);
                        User userInformation = UserList.getUserInformation(userList.listUsers(), ois.readObject().toString());
                        String password = userInformation.Password;
                        String salt = userInformation.Salt;
                        deleteUser(ois, connection, userList);
                        createUserWithPassword(ois, connection, userList, password, salt);
                        break;
                    case "Run Billboard Viewer":
                        Connection finalConnection = connection;

                        //send details of currently displayed billboard to Viewer client
                        runViewer(oos, billboardList, billboardSchedule, finalConnection);

                    default:
                }

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

        //close connection to client
        connection.close();
    }

    public static MessageDigest messageDigester() throws NoSuchAlgorithmException {
        //Message digest for the password salting
        //Setup ready for hashing
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        return messageDigest;
    }


    /**
     * Creates tables if they do not exist in DB
     * @param connection Database connection
     */
    private static void checkTables(Connection connection) {
        //Adds tables to database if they do not exist

        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_BILLBOARD_TABLE);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_SCHEDULE_TABLE);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_USER_TABLE);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement st = connection.createStatement();
            st.execute(ADD_DEFAULT_USER);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives a users hashed password, salts it and then check it against the database to decide if the user is
     * authenticated or not. If valid assigns the user as the current user and opens control panel, if not returns an error message.
     * @param ois Object Input stream
     * @param connection connection to the db
     * @throws Exception
     */
    private static void saltAndCheckUserCredentials(ObjectInputStream ois, Connection connection) throws SQLException, IOException, ClassNotFoundException, NoSuchAlgorithmException {
        //Setup ready for hashing
        //MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        //Get the inputted username hashed password
        String userName = ois.readObject().toString();
        String hashedPassword = ois.readObject().toString();

        //SQL statement for retrieving user details
        final String SELECT = "select * from users where username = "+ "'" +userName+ "'" + ";";

        //create statement
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(SELECT);

        //Retrieve the Salt and Salted Hashed Password from the database
        String saltedPasswordDB = null;
        String saltString = null;
        if(rs.next()){
            saltedPasswordDB = rs.getString(2);
            saltString = rs.getString(3);
        }

        String createSalt = UserManager.createASalt();
        String[] userInfos = UserManager.hashPasswordAndSalt(hashedPassword, saltString, messageDigester());
        String[] userInfos2 = UserManager.hashPasswordAndSalt(hashedPassword, saltString, messageDigester());

        System.out.println("Database final product: "+saltedPasswordDB);
        System.out.println("Inputted password hashed: "+hashedPassword);
        System.out.println("Salt String from method: "+userInfos[1]);
        System.out.println("Inputted Final product: "+userInfos[0]);
        System.out.println("Salt String from method2: "+userInfos2[1]);
        System.out.println("Inputted Final product2: "+userInfos2[0]);



        //Add a salt to the user inputted hashed password
        //String inputtedPasswordSalted = (messageDigest.digest((hashedPassword + saltString).getBytes())).toString();
        String inputtedPasswordSalted = userInfos[0];
                //(messageDigest.digest((hashedPassword + saltString).getBytes())).toString();
        String userPass = inputtedPasswordSalted;



        //Compare the two salted and hashed passwords
        if(userInfos[0].equals(saltedPasswordDB)){
            String SessionToken = sessionToken("Valid");
            //If the user is valid set them as the default user in the control panel
            ControlPanelClient.username = userName;
            ControlPanelClient.sessionToken = SessionToken;
            System.out.println("The session token is: "+ SessionToken);
            //Create and return the user a valid session token
            SwingUtilities.invokeLater(new ControlPanelGUI(userName, SessionToken));
        }else{
            // Display an Error Message Dialog, alerting the user that the entered credentials are incorrect
            JOptionPane optionPane = new JOptionPane("The entered username or password is incorrect," +
                    " please try again.", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("User Error");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            SwingUtilities.invokeLater(new ControlPanelGUILoginScreen());
        }
    }

    /**
     * Uses the users session token to remove session token in the event the user logs out
     * @param oos Object Output stream of Server
     * @param ois ObjectInputStream
     * @throws IOException
     */
    private static void logoutRequest(ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        //Get the users token
        //String userToken = (String) ois.readObject();
        String userToken = "reee";
        System.out.println(userToken);

//        //Boolean for checking existance of session token
//        Boolean tokenExists = false;
//        //Check the user inputted token
//        for(Map.Entry<Integer, String> entry : SessionTokenListHashmap.entrySet()){
//            //If the user token exists in the hashmap then return a true value.
//            if(entry.getValue() == userToken) {
//                tokenExists = true;
//                SessionTokenListHashmap.remove(userToken);
//                oos.writeChars("Removed");
//            }else{
//                //Token doesn't exist in the hashmap
//                tokenExists = false;
//            }
//        }
//        //If the token wasn't found in the hashmap then it has expired
//        if(tokenExists == false){
//            oos.writeChars("Token has already expired.");
//            System.out.println("No session token existed, completed properly.");
//        }
    }

    /**
     * Sends a list of billboards to the client
     * @param oos Object output stream of the server
     * @param billboardList the list being sent to the client
     * @throws Exception
     */
    private static void listBillboards(ObjectOutputStream oos, BillboardList billboardList, UserList userList, ScheduleMultiMap billboardSchedule) throws Exception{
        //Output to client
        oos.writeObject(billboardList.listBillboards());
        oos.writeObject(userList.listUsers());
        oos.writeObject(billboardSchedule);

        //Print billboard list sent to client
        System.out.println("Sent to client:\n");
        System.out.println("billboard list: "+ billboardList.listBillboards());
    }

    /**
     * Sends billboard info to the client
     * @param oos Object output stream of client
     * @param ois Object Input stream of server
     * @param billboardList A list of billboards
     * @throws IOException
     */
    private static void getBillboardInfo(ObjectOutputStream oos, ObjectInputStream ois, BillboardList billboardList)throws Exception{
        //Read Parameters sent by client
        String billboardName = ois.readObject().toString();
        //Output results to the client
        oos.writeObject(billboardList.GetBillboardInfo(billboardName));

        Billboard BillboardInfo = billboardList.GetBillboardInfo(billboardName);

        //Print billboard variables received from client
        System.out.println("Sent to client:\n");
        System.out.println("billboard name: "+BillboardInfo.BillboardName + "\n");
        System.out.println("billboard creator: "+BillboardInfo.BillboardCreator + "\n");
        System.out.println("billboard xml: "+BillboardInfo.XMLFile + "\n");
    }

    /**
     * Sends a get info request to the server
     * @param ois Object input stream of server
     * @param connection connection to the db
     * @param billboardList
     * @throws IOException
     */
    private static void createEditBillboard(ObjectInputStream ois, Connection connection, BillboardList billboardList) throws Exception {
        //Read parameters sent by the client
        String billboardName = ois.readObject().toString();
        String billboardCreator = ois.readObject().toString();
        String xmlFile = ois.readObject().toString();

        //Print billboard variables received from client
        System.out.println("Received from client:\n");
        System.out.println("billboard name : "+ billboardName + "\n" +
                "billboard creator: "+billboardCreator+"\n"+
                "xml file: "+xmlFile+"\n");

        //Clear the db with the billboard information
        billboardList.ClearDBbillboardList(connection);

        //Create the billboard
        billboardList.createEditBillboard(billboardName, billboardCreator, xmlFile);

        //Write the new billboard to the DB
        billboardList.WriteToDBbillboard(connection);
    }

    /**
     * Sends a delete request to the DB
     * @param ois Object input stream of the server
     * @param connection connection to the db
     * @param billboardList the list of billboards
     * @throws IOException
     */
    private static void deleteBillboard(ObjectInputStream ois, Connection connection, BillboardList billboardList) throws Exception {
        //Read the parameters given by the client
        String billboardName = ois.readObject().toString();

        //Print name of billboard being deleted
        System.out.println("Received from client:\n");
        System.out.println("billboard name: "+ billboardName);

        //Clear the db with the billboard information
        billboardList.ClearDBbillboardList(connection);

        //Now that the db is empty remove the billboard from the billboard list
        billboardList.DeleteBillboard(billboardName);

        //Now that the billboard has been removed from the list of billboards
        //Write the updated list to the db
        billboardList.WriteToDBbillboard(connection);
    }

    /**
     * Sends schedule to client as a MultiMap
     * @param oos Object Output stream of Server
     * @param billboardSchedule schedule being sent to Client
     * @throws IOException
     */
    private static void viewSchedule(ObjectOutputStream oos, ScheduleMultiMap billboardSchedule, UserList userList) throws IOException {
        MultiMap<String, ScheduleInfo> schedule = billboardSchedule.viewSchedule();

        //send schedule to client
        oos.writeObject(schedule);
        oos.writeObject(userList.listUsers());

        //print schedule that was sent to client
        System.out.println("Sent to client:\n");
        System.out.println("Schedule: "+schedule+"\n");

    }

   /**
     * schedules Billboard sent by client and stores in DB
     * @param ois Object Input stream of Server
     * @param connection Database connection
     * @param billboardList list of created billboards
     * @param billboardSchedule schedule of billboard viewings
     * @throws Exception IOException, ClassNotFoundException, SQLException,
     * Exception - billboard does not exist, viewing does not exist
     */

   private static void scheduleBillboard(ObjectInputStream ois, Connection connection, BillboardList billboardList
            , ScheduleMultiMap billboardSchedule) throws Exception {
        //read parameters sent by client
        String billboardName = ois.readObject().toString();
        String startTime = ois.readObject().toString();
        String duration = ois.readObject().toString();
        String recurrenceDelay = ois.readObject().toString();

        //print schedule variables received from client
        System.out.println("Received to client:\n");
        System.out.println("Billboard Name: "+billboardName+"\n");
        System.out.println("Start Time: "+startTime+"\n");
        System.out.println("Duration (mins): "+duration+"\n");
        System.out.println("Recurrence delay (mins): "+recurrenceDelay+"\n");

        Billboard billboard = billboardList.GetBillboardInfo(billboardName);
        String billboardCreator = billboard.BillboardCreator;

        //Clear schedule table in DB
        billboardSchedule.clearDBschedule(connection);

        //schedule billboard with client input
        billboardSchedule.scheduleBillboard(billboardName,LocalDateTime.parse(startTime),
                Duration.ofMinutes(Integer.parseInt(duration)), Integer.parseInt(recurrenceDelay), billboardList.listBillboards(),billboardCreator);

        //write schedule to DB
        billboardSchedule.writeToDBschedule(connection);

        //update viewer queue
        populateQueue(connection);
    }


    /**
     * Removes viewing sent by client from schedule
     * @param ois Object input stream
     * @param connection Database Connection
     * @param billboardSchedule schedule of billboard viewings
     * @param billboardList list of created billboards
     * @throws Exception IOException, ClassNotFoundException, SQLException,
     * Exception - billboard does not exist, viewing does not exist, invalid duration, invalid recurrence delay
     */
    private static void removeSchedule (ObjectInputStream ois, Connection connection,
                                       ScheduleMultiMap billboardSchedule, BillboardList billboardList) throws Exception {
        //read parameters sent by client
        String billboardName = ois.readObject().toString();
        String startTime = ois.readObject().toString();
        String duration = ois.readObject().toString();
        String recurrenceDelay = ois.readObject().toString();

        //print schedule variables received from client
        System.out.println("Received to client:\n");
        System.out.println("Billboard Name: "+billboardName+"\n");
        System.out.println("Start Time: "+startTime+"\n");
        System.out.println("Duration (mins): "+duration+"\n");
        System.out.println("Recurrence delay (mins): "+recurrenceDelay+"\n");

        //retrieve billboard object
        Billboard billboard = billboardList.GetBillboardInfo(billboardName);

        //retrieve billboard creator
        String billboardCreator = billboard.BillboardCreator;

        //create schedule info object with client's input
        ScheduleInfo scheduleInfo = new ScheduleInfo(LocalDateTime.parse(startTime),
                Duration.ofMinutes(Integer.parseInt(duration)), Integer.parseInt(recurrenceDelay), billboardCreator);

        //Clear schedule table in DB
        billboardSchedule.clearDBschedule(connection);

        //remove viewing from schedule
        billboardSchedule.scheduleRemoveBillboard(billboardName,scheduleInfo);

        //write schedule to DB
        billboardSchedule.writeToDBschedule(connection);
    }
//
//
//    private static void billboardScheduleCheck(ObjectInputStream ois, ObjectOutputStream oos, ScheduleMultiMap billboardSchedule) throws IOException, ClassNotFoundException {
//        //read billboard name sent by client
//        String billboardName = ois.readObject().toString();
//
//        //print schedule variables received from client
//        System.out.println("Received to client:\n");
//        System.out.println("Billboard Name: "+billboardName+"\n");
//
//        //check if billboard exists in schedule
//        //try retrieve billboard's scheduling information
//        try {
//            billboardSchedule.getSchedule(billboardName);
//            //if viewings for billboard exist
//            //write true to DB
//            oos.writeObject("true");
//            System.out.println("true");
//            //flush output stream
//            oos.flush();
//        }
//        //if viewings for billboard do NOT exist
//        catch (Exception e) {
//            //write false to DB
//            oos.writeObject("false");
//            System.out.println("false");
//            //flush output stream
//            oos.flush();
//        }
//    }

    /**
     * populates queue with schedule from database
     * @param connection database connection
     * @throws SQLException invalid SQL query
     */
    private static void populateQueue (Connection connection) throws SQLException {

        //Read data from DB - sort rows in ascending order by start time of viewing
        final String SELECT = "SELECT * FROM schedule ORDER BY startTimeScheduled ASC;";
        final String GET_NUM_ROWS = "select count(*) from Schedule;";

        //create statement
        Statement st = connection.createStatement();

        ResultSet rs = st.executeQuery(SELECT);

        //retrieve number of rows from schedule table in DB
        ResultSet rowResultSet = st.executeQuery(GET_NUM_ROWS);

        int numDBRows;
        try{
            //store number of schedule table rows in local int
            rowResultSet.next();
            numDBRows = rowResultSet.getInt(1);
            System.out.println("rows in DB:" +numDBRows);
        }
        catch (Exception e)
        {
            //if no rows are in DB set numDBRows to 0
            numDBRows = 0;
            System.out.println("rows in DB:" +numDBRows);
        }
        //create 2D array that stores the contents of each row in the DB
        queue = new String[numDBRows][5];

        int rowNo = -1;

        //for every database entry
        while (rs.next())
        {
            rowNo++;

            //store database info in local variables
            String billboardName = rs.getString(1);
            String startTimeScheduled = rs.getString(2);
            String duration = rs.getString(3);
            String recurrence = rs.getString(4);
            String billboardCreator = rs.getString(5);

           //add to queue
            queue[rowNo][0] = billboardName;
            queue[rowNo][1] = startTimeScheduled;
            queue[rowNo][2] = duration;
            queue[rowNo][3] = recurrence;
            queue[rowNo][4] = billboardCreator;
        }

        //close ResultSet
        rs.close();
        //close statement
        st.close();

    }

    /**
     * Sends name of current billboard displayed to viewer
     * @param oos Object output stream
     * @param billboardList list of created billboards
     * @param billboardSchedule schedule of billboard viewings
     * @param connection Database connection
     * @throws Exception IOException, SQLException,
     * Exception - billboard does not exist, viewing does not exist, invalid duration, invalid recurrence delay
     **/
    private static void runViewer(ObjectOutputStream oos, BillboardList billboardList, ScheduleMultiMap billboardSchedule, Connection connection) throws Exception {
        //if billboards have been scheduled
        if(queue.length > 0)
        {   //store current time and time of next viewing in local variables
            LocalDateTime currentTime = LocalDateTime.now();

            //store schedule info in local variables
            String billboardName = queue[0][0];
            LocalDateTime nextViewingTime = LocalDateTime.parse(queue[0][1]);
            Duration duration = Duration.parse(queue[0][2]);
            int recurrenceDelay = Integer.parseInt(queue[0][3]);
            String billboardCreator = queue[0][4];

            //get schedule info of viewing that has been displayed
            ScheduleInfo displayedSchedule = new ScheduleInfo(nextViewingTime, duration,
                    recurrenceDelay, billboardCreator);

            LocalDateTime endTime = nextViewingTime.plus(duration);

            //if viewing duration has passed
            if(currentTime.isAfter(endTime))
            {
                //clear DB
                billboardSchedule.clearDBschedule(connection);

                //Remove viewing from schedule
                billboardSchedule.scheduleRemoveBillboard(billboardName, displayedSchedule);

                //Write schedule changes to DB
                billboardSchedule.writeToDBschedule(connection);

                //repopulate queue
                populateQueue(connection);

                //if there are remaining viewings
                if(queue.length > 0){
                    //update schedule info for next viewing
                    billboardName = queue[0][0];
                    nextViewingTime = LocalDateTime.parse(queue[0][1]);
                    duration = Duration.parse(queue[0][2]);
                    recurrenceDelay = Integer.parseInt(queue[0][3]);
                    billboardCreator = queue[0][4];
                }
            }

            //Check if the next viewing in the queue is before or equal to current time
            if((nextViewingTime.isBefore(currentTime) || nextViewingTime.isEqual(currentTime)) && queue.length > 0)
            {
                System.out.println("\n"+LocalDateTime.now());

                //retrieve information of currently displayed billboard
                Billboard billboard = billboardList.GetBillboardInfo(billboardName);

                //retrieve xml from billboard
                String xmlFile = billboard.XMLFile;

                //Send billboard xml to client
                oos.writeObject(xmlFile);
                oos.flush();;

                //print currently displaying billboard
                System.out.println("Currently displayed billboard: "+billboardName);


                //clear DB
                billboardSchedule.clearDBschedule(connection);

                //if billboard viewing recurs
                if(recurrenceDelay != 0)
                {
                    //retrieve all viewings of billboard
                    ArrayList<ScheduleInfo> billboardViewings = billboardSchedule.getSchedule(billboardName);

                    //convert recurrence delay to duration
                    Duration durationRecurrenceDelay = Duration.ofMinutes(recurrenceDelay);

                    //add delay to current time to calculate next start time
                    LocalDateTime newStartTime = nextViewingTime.plus(durationRecurrenceDelay);

                    //boolean variable to track whether billboard has been rescheduled
                    boolean rescheduled = false;

                    //for every viewing of currently displayed billboard
                    for(ScheduleInfo viewing : billboardViewings)
                    {
                        //if billboard has been rescheduled
                        if(viewing.startTimeScheduled.equals(newStartTime))
                        {
                            //set rescheduled to true
                            rescheduled = true;
                            break;
                        }
                    }

                    System.out.println("rescheduled: " + rescheduled);

                    //if billboard has not been rescheduled
                    if (rescheduled == false)
                    {
                        //Reschedule start time of viewing for next recurring
                        billboardSchedule.scheduleBillboard(billboardName,newStartTime,duration,recurrenceDelay,
                                billboardList.billboardHashMap, billboardCreator);
                    }
                }

                //Write schedule changes to DB
                billboardSchedule.writeToDBschedule(connection);
            }

            else
            {
                System.out.println("\n"+LocalDateTime.now());
                //Send error message to client
                System.out.println("There are no billboards scheduled for this time");

                //send details of "no billboard to display" xml
                oos.writeObject(noViewingXML);
                oos.flush();;
            }

        }

        else
        {
            System.out.println("\n"+LocalDateTime.now());

            //Send error message to client
            System.out.println("queue is empty");

            //send details of "no billboard to display" xml
            oos.writeObject(noViewingXML);
            oos.flush();;

        }
    }

    //Static int for counting which session has expired.
    private static int i = 0;
    //Inner class called when a timer expires.
    static class RemoveFromList extends TimerTask{
        public void run(){
            //Remove the session info from the hashmap for the session token that has expired.
            SessionTokenListHashmap.remove(i++);
        }
    }

    static int counter;

    /**
     * If the user is valid this creates a session token and sends it back to the control panel.
     * @throws Exception
     */
    private static String sessionToken(String validity) throws IOException, ClassNotFoundException {
        //Setup for the random token
        final SecureRandom secRand = new SecureRandom();
        final Base64.Encoder base64En = Base64.getUrlEncoder();

        //Check if the user was valid
        String Validity = validity;

        //If valid give a session token else return a message.
        if (Validity == "Valid"){
            counter++;
            //Create a random Session Token for the user
            byte[] randomBytes = new byte[24];
            secRand.nextBytes(randomBytes);
            String sessionToken = base64En.encodeToString(randomBytes);
            String thisSessionToken = sessionToken;
            //Send the randomised Session Token back to the control panel
            //oos.writeObject(thisSessionToken);

            //Create a new timer to be stored in an hashmap with the session token
            Timer timer = new Timer();
            TimerTask taskA = new RemoveFromList();
            //Set the timer to 24 hours, after which it will be removed from the hashmap
            timer.schedule(taskA, (long) 8.64e+7);

            //Store counter ID and session token
            try{
                SessionCombinedHashmap.put(counter, timer);
                //Then store the hasmpa of counter and token with a timer
                SessionTokenListHashmap.put(counter, thisSessionToken);
                System.out.println("The session token is: "+SessionTokenListHashmap.get(1));
            }catch(Exception e){

            }

            //Pass back the valid user session token
            return thisSessionToken;
        }else{
            return "User Invalid";
        }
    }

    /**
     * Checks the Hashmap of Tokens for the users token
     * @param oos Object Output stream of Server
     * @param oois ObjectInputStream
     * @throws IOException
     */
    private void checkToken(ObjectOutputStream oos, ObjectInputStream oois) throws IOException, ClassNotFoundException {
        //Get the user inputted token
        String userToken = oois.readObject().toString();

        //Boolean for checking existance of session token
        Boolean tokenExists = false;
        //Check the user inputted token
        for(Map.Entry<Integer, String> entry : SessionTokenListHashmap.entrySet()){
            //If the user token exists in the hashmap then return a true value.
            if(entry.getValue() == userToken) {
                tokenExists = true;
                oos.writeChars("Valid Token");
            }else{
                //Token doesn't exist in the hashmap
                tokenExists = false;
            }
        }
        //If the token wasn't found in the hashmap then it has expired
        if(tokenExists == false){
            oos.writeChars("Token has expired.");
        }
    }


    private static void createUser(ObjectInputStream ois, Connection connection, UserList userList) throws Exception {
        String createSalt = UserManager.createASalt();
        String[] userSet = UserManager.hashPasswordAndSalt(ois.readObject().toString(), createSalt, messageDigester());
        String username = ois.readObject().toString();
        String password = userSet[0];
        String salt = userSet[1];
        String createBillboard = ois.readObject().toString();
        String scheduleBillboard = ois.readObject().toString();
        String editBillboard = ois.readObject().toString();
        String editUsers = ois.readObject().toString();
        User newUser = new User(username, password, salt);
        if(createBillboard.equals("1")) {
            newUser.Permissions.add("Create Billboards");
        }
        if(scheduleBillboard.equals("1")) {
            newUser.Permissions.add("Schedule Billboards");
        }
        if(editBillboard.equals("1")) {
            newUser.Permissions.add("Edit All Billboards");
        }
        if(editUsers.equals("1")) {
            newUser.Permissions.add("Edit Users");
        }

        System.out.println("Received new user from client");
        System.out.println("username = " + username + ", password = " + password);

        //Clear the db with the user information
        UserList.clearUsersFromDB(userList.listUsers(), connection);

        //Write the new users to the list
        UserList.addUserToList(userList.listUsers(), newUser);

        //Write new user to db
        UserList.sendUsersToDB(userList.listUsers(), connection);
    }

    private static void createUserWithPassword(ObjectInputStream ois, Connection connection, UserList userList, String password, String salt) throws Exception {
        String username = ois.readObject().toString();
        String createBillboard = ois.readObject().toString();
        String scheduleBillboard = ois.readObject().toString();
        String editBillboard = ois.readObject().toString();
        String editUsers = ois.readObject().toString();
        User newUser = new User(username, password, salt);
        if(createBillboard.equals("1")) {
            newUser.Permissions.add("Create Billboards");
        }
        if(scheduleBillboard.equals("1")) {
            newUser.Permissions.add("Schedule Billboards");
        }
        if(editBillboard.equals("1")) {
            newUser.Permissions.add("Edit All Billboards");
        }
        if(editUsers.equals("1")) {
            newUser.Permissions.add("Edit Users");
        }

        System.out.println("Received new user from client");
        System.out.println("username = " + username + ", password = " + password);

        //Clear the db with the user information
        UserList.clearUsersFromDB(userList.listUsers(), connection);

        //Write the new users to the list
        UserList.addUserToList(userList.listUsers(), newUser);

        //Write new user to db
        UserList.sendUsersToDB(userList.listUsers(), connection);
    }

    private static void deleteUser(ObjectInputStream ois, Connection connection, UserList userList) throws Exception {
        //Read the parameters given by the client
        String username = ois.readObject().toString();
        User user = UserList.getUserInformation(userList.listUsers(), username);
        //Display the name of the user for ease of testing
        System.out.println("delete user: "+ username);

        //Clear the db with the user information
        UserList.clearUsersFromDB(userList.listUsers(), connection);

        //Now that the db is empty remove the user from the user list
        UserList.deleteUser(userList.listUsers(), user);

        //Now that the user has been removed from the list of user
        //Write the updated list to the db
        UserList.sendUsersToDB(userList.listUsers(), connection);
    }

    private static void listUsers(ObjectOutputStream oos, UserList userList) throws Exception {
        oos.writeObject(userList.listUsers());
    }

    /**
     * Runs server
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        runServer();
    }
}