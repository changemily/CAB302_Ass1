import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
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

    public static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS Users (username varchar(255),password varchar(255));";

    public static final String CREATE_BILLBOARD_TABLE =
            "CREATE TABLE IF NOT EXISTS Billboards (billboard_name varchar(255)," +
                    "text varchar(1000),bg_colour varchar (255)," +
                    "image_file varchar(255), billboard_creator varchar(255));";

    public static final String CREATE_SCHEDULE_TABLE =
            "CREATE TABLE IF NOT EXISTS Schedule (billboard_name varchar(255), Start_TimeScheduled varchar(50), " +
                    "Duration varchar (255), recurrence varchar (50), time_scheduled varchar (50), billboard_creator varchar (255));";

    //Setup another hashmap to store an id and hasmap of the token and its timer
    HashMap<Integer, Timer> SessionCombinedHashmap;
    //Setup a hashmap to store each hasmap with a timer
    HashMap<Integer, String> SessionTokenListHashmap;

    /**
     * Starts up Billboard server for connection to client
     * Sends and Receives information from client
     */
    public static void Run_Server() throws Exception {
        //Setup a default user.
        User DefaultUser = new User("DefaultUserName", "DefaultPassword",
                "Create Billboards", "Edit All Billboards", "Schedule Billboards", "Edit Users");



        //create empty schedule, billboard list and user list
        ScheduleMultiMap billboard_schedule = new ScheduleMultiMap();

        BillboardList billboard_list = new BillboardList();

        //create DB connection
        Connection connection = null;

        //A while loop that attempts to connect to the database
        //Every 15 seconds until a connection is made.
        boolean connectionMade = false;
        while(connectionMade == false){
            connection = DBconnection.getInstance();
            if(connection != null){
                connectionMade = true;
                System.out.println("Connection made, resuming.");
            }
            else {
                System.out.println("Connection cannot be made. Attempting connection again in 15 seconds...");
                Thread.sleep(15000);
            }
        }

        //checks if tables exist in DB, if not adds tables
        Check_tables(connection);

        //populate schedule, billboard list and user list with data from database
        billboard_schedule.RetrieveDBschedule(connection);
        billboard_list.RetrieveDBbillboardList(connection);

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
                Object clientRequest = ois.readObject();

                //print what was received from client
                System.out.println("received from client: "+clientRequest);

                String return_message;

                //save return message, based on what request was received from the client
                switch(clientRequest.toString())
                {
                    case "Login request":
                        return_message = "Login request";
                        //retrieve username
                        String username = ois.readObject().toString();
                        //retrieve hashed pwd from client
                        String password = ois.readObject().toString();

                        System.out.println("Username: " +username);
                        System.out.println("Password: " +password);

                        //retrieve salted pwd from DB
                        //unsalt pwd
                        //check if hashed pwd match
                        //if match return session token
                        //else returns error to client
                        break;
                    case "List billboards":
                        return_message = "returned List of billboards";
                        //write billboard list to client
                        listBillboards(oos, billboard_list);
                        break;
                    case "Get Billboard info":
                        return_message = "returned Billboard info";
                        //write billboard info to client
                        getBillboardInfo(oos, ois, billboard_list);
                        break;
                    case "Create edit billboard":
                        return_message = "Created/edited billboard";
                        //Write the new billboard to the DB
                        createEditBillboard(ois, connection, billboard_list);
                        break;
                    case "Delete billboard":
                        return_message = "billboard has been deleted";
                        //Write the delete to the db
                        deleteBillboard(ois, connection, billboard_list);
                        break;
                    case "View schedule":
                        return_message = "returned schedule";
                        viewSchedule(oos,billboard_schedule);
                        break;

                    case "Schedule Billboard":
                        return_message = "Billboard has been scheduled";
                        //schedule billboard
                        scheduleBillboard(ois, connection, billboard_list, billboard_schedule);
                        break;

                    case "Remove Schedule":
                        return_message = "billboard has been removed from schedule";
                        //remove viewing from schedule
                        removeSchedule(ois,connection,billboard_schedule, billboard_list);
                        break;

                    case "List users":
                        return_message = "returned list of users";
                        break;
                    case "Create user":
                        return_message = "user has been created";
                        break;
                    case "Get user permissions":
                        return_message = "returned user permissions";
                        break;
                    case "Set user permissions":
                        return_message = "user permissions have been set";
                        break;
                    case "Set user password":
                        return_message = "user password has been set";
                        break;
                    case "Billboard Viewer":
                        return_message = "Billboard Viewer";
                        runViewer(ois,billboard_schedule);
                        break;

                    default:
                        return_message = "No match";
                }
                //Write return message for client's request to client
                oos.writeObject(return_message);
                oos.flush();

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


    /**
     * Creates tables if they do not exist in DB
     * @param connection Database connection
     */
    public static void Check_tables(Connection connection) {
        //Adds tables to database if they do not exist

        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_BILLBOARD_TABLE);
            st.close();
            System.out.println("added billboard table to db");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_SCHEDULE_TABLE);
            st.close();
            System.out.println("added schedule table to db");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_USER_TABLE);
            st.close();
            System.out.println("added billboard table to db");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sends a list of billboards to the client
     * @param oos Object output stream of the server
     * @param billboard_List the list being sent to the client
     * @throws Exception
     */
    public static void listBillboards(ObjectOutputStream oos, BillboardList billboard_List) throws Exception{
        //Output to client
        oos.writeObject(billboard_List.List_Billboards());
        System.out.println("billboard list: "+ billboard_List.List_Billboards());
    }

    /**
     * Sends billboard info to the client
     * @param oos Object output stream of client
     * @param ois Object Input stream of server
     * @param billboard_List A list of billboards
     * @throws IOException
     */
    public static void getBillboardInfo(ObjectOutputStream oos, ObjectInputStream ois, BillboardList billboard_List)throws Exception{
        //Read Parameters sent by client
        String billboardName = ois.readObject().toString();
        //Output results to the client
        oos.writeObject(billboard_List.Get_billboard_info(billboardName));

        Billboard BillboardInfo = billboard_List.Get_billboard_info(billboardName);
        System.out.println("billboard infos: "+ billboard_List.Get_billboard_info(billboardName));
        System.out.println("billboard name: "+BillboardInfo.Billboard_name);
        System.out.println("billboard bg colour: "+BillboardInfo.Bg_colour);
        System.out.println("billboard image file: "+BillboardInfo.Image_file);
    }

    /**
     * Sends a get info request to the server
     * @param ois Object input stream of server
     * @param connection connection to the db
     * @param billboard_List
     * @throws IOException
     */
    public static void createEditBillboard(ObjectInputStream ois, Connection connection, BillboardList billboard_List) throws Exception {
        //Read parameters sent by the client
        String billboard_name = ois.readObject().toString();
        String text = ois.readObject().toString();
        String bg_colour = ois.readObject().toString();
        String image = ois.readObject().toString();
        String billboard_creator = ois.readObject().toString();

        //For testing purposes
        //print bb list
        System.out.println("billboard list: "+ billboard_List);
        //print what was received from client
        System.out.println("billboard name: "+ billboard_name + "\n" +
                "text: "+text+"\n"+
                "bg_colour: "+bg_colour+"\n"+
                "image: "+image+"\n");

        //Clear the db with the billboard information
        billboard_List.Clear_DBbillboardList(connection);

        //Create the billboard
        billboard_List.Create_edit_Billboard(billboard_name, text, bg_colour, image, billboard_creator);

        //Write the new billboard to the DB
        billboard_List.Write_To_DBbillboard(connection);
    }

    /**
     * Sends a delete request to the DB
     * @param ois Object input stream of the server
     * @param connection connection to the db
     * @param billboard_List the list of billboards
     * @throws IOException
     */
    public static void deleteBillboard(ObjectInputStream ois, Connection connection, BillboardList billboard_List) throws Exception {
        //Read the parameters given by the client
        String billboard_name = ois.readObject().toString();
        //Display the name of the billboard for ease of testing
        System.out.println("billboard name: "+ billboard_name);

        //Clear the db with the billboard information
        billboard_List.Clear_DBbillboardList(connection);

        //Now that the db is empty remove the billboard from the billboard list
        billboard_List.Delete_billboard(billboard_name);

        //Now that the billboard has been removed from the list of billboards
        //Write the updated list to the db
        billboard_List.Write_To_DBbillboard(connection);
    }

    /**
     * Sends schedule to client
     * @param oos Object Output stream of Server
     * @param billboard_schedule schedule being sent to Client
     * @throws IOException
     */
    public static void viewSchedule(ObjectOutputStream oos, ScheduleMultiMap billboard_schedule) throws IOException {
        MultiMap<String, Schedule_Info> schedule = billboard_schedule.View_schedule();
        //send schedule to client
        oos.writeObject(schedule);
    }

   /* *//**
     * schedules Billboard sent by client and stores in DB
     * @param ois Object Input stream of Server
     * @param connection Database connection
     * @param billboard_list
     * @param billboard_schedule
     * @throws Exception
     */
    public static void scheduleBillboard(ObjectInputStream ois, Connection connection, BillboardList billboard_list
            , ScheduleMultiMap billboard_schedule) throws Exception {
        //read parameters sent by client
        String billboard_name = ois.readObject().toString();
        String start_time = ois.readObject().toString();
        String duration = ois.readObject().toString();
        String recurrence = ois.readObject().toString();

        Billboard billboard = billboard_list.Get_billboard_info(billboard_name);
        String billboard_creator = billboard.Billboard_creator;

        //print bb list
        System.out.println("billboard list: "+ billboard_list);

        //print what was received from client
        System.out.println("billboard name: "+ billboard_name + "\n" +
                "start time: "+start_time+"\n" +
                "duration: " + duration +"\n"+
                "recurrence: " +recurrence +"\n");

        //Clear schedule table in DB
        billboard_schedule.Clear_DBschedule(connection);

        //schedule billboard with client input
        billboard_schedule.scheduleBillboard(billboard_name,LocalDateTime.parse(start_time),
                Duration.ofMinutes(Integer.parseInt(duration)),recurrence, billboard_list.List_Billboards(),billboard_creator);

        //write schedule to DB
        billboard_schedule.Write_To_DBschedule(connection);
    }

    /**
     * Removes viewing sent by client from schedule
     * @param ois
     * @param connection
     * @param billboard_schedule
     * @throws Exception
     */
    public static void removeSchedule (ObjectInputStream ois, Connection connection,
                                       ScheduleMultiMap billboard_schedule, BillboardList billboard_list) throws Exception {
        //read billboard name sent by client
        Object billboard_name = ois.readObject();
        System.out.println("billboard name: "+ billboard_name);

        //read start time of viewing sent by client
        String startTime = ois.readObject().toString();

        //read duration sent by client
        String duration = ois.readObject().toString();

        //read recurrence sent by client
        String recurrence = ois.readObject().toString();

        Billboard billboard = billboard_list.Get_billboard_info(billboard_name.toString());
        String billboard_creator = billboard.Billboard_creator;

        //create schedule info object with client's input
        Schedule_Info schedule_info = new Schedule_Info(LocalDateTime.parse(startTime),
                Duration.ofMinutes(Integer.parseInt(duration)),recurrence, billboard_creator);

        //Clear schedule table in DB
        billboard_schedule.Clear_DBschedule(connection);

        //remove billboard from schedule
        billboard_schedule.Schedule_Remove_billboard(billboard_name.toString(),schedule_info);

        //write schedule to DB
        billboard_schedule.Write_To_DBschedule(connection);
    }

    public static void runViewer(ObjectInputStream ois, ScheduleMultiMap billboard_schedule) throws Exception {
        //every 15 seconds
        //create new timer
        Timer timer = new Timer();

        //For every entry of Billboard_schedule
        for (String billboard_name : billboard_schedule.Billboard_schedule.keySet())
        {
            //create array list to store viewings of billboard
            ArrayList<Schedule_Info> viewings = billboard_schedule.getSchedule(billboard_name);

            //for every viewing of billboard
            for ( Schedule_Info viewing : viewings ) {

                //if start time of viewing matches current time
                if (viewing.StartTime_Scheduled.isEqual(LocalDateTime.now()))
                {
                    //create xml file path
                    File finalFile = new File("./"+billboard_name+".xml");
                    System.out.println("xml file: "+ finalFile);

                    //create new timer task to display billboard
                    TimerTask run_viewer = new TimerTask() {
                        @Override
                        public void run(){
                            try {
                                new BillboardViewer(finalFile, true);
                            } catch (SAXException | ParserConfigurationException | IOException | ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    //if billboard is recurs daily
                    if (viewing.Recurrence == "day")
                    {
                        int duration_int = Integer.parseInt(viewing.duration.toString());
                        //schedule viewing at fixed rate
                        timer.scheduleAtFixedRate(run_viewer, Date.from(Instant.now()),(long) duration_int);
                    }

                    //if billboard is recurs hourly
                    if (viewing.Recurrence == "hour")
                    {
                        //schedule viewing at fixed rate
                    }

                    //if billboard is recurs every minute
                    if (viewing.Recurrence == "minute")
                    {
                        //schedule viewing at fixed rate
                    }

                    //if billboard does not recur
                    if (viewing.Recurrence == "none")
                    {
                        int duration_int = Integer.parseInt(viewing.duration.toString());
                        //schedule viewing
                        timer.schedule(run_viewer, Date.from(Instant.now()),(long) duration_int);
                    }

                }
            }

        }

    }

    //Static int for counting which session has expired.
    public static int i = 0;
    //Inner class called when a timer expires.
    class RemoveFromList extends TimerTask{
        public void run(){
            //Remove the session info from the hashmap for the session token that has expired.
            SessionTokenListHashmap.remove(i++);
        }
    }

    int counter;

    /**
     * If the user is valid this creates a session token and sends it back to the control panel.
     * @param ois ObjectInputStream
     * @param oos Object Output stream of Server
     * @param connection Database connection
     * @throws Exception
     */
    private void sessionToken(ObjectInputStream ois, ObjectOutputStream oos,
                                    Connection connection) throws IOException, ClassNotFoundException {
        //Setup for the random token
        final SecureRandom secRand = new SecureRandom();
        final Base64.Encoder base64En = Base64.getUrlEncoder();

        //Check if the user was valid
        String Validity = ois.readObject().toString();

        //If valid give a session token else return a message.
        if (Validity == "Valid"){
            counter++;
            //Create a random Session Token for the user
            byte[] randomBytes = new byte[24];
            secRand.nextBytes(randomBytes);
            String sessionToken = base64En.encodeToString(randomBytes);
            String thisSessionToken = sessionToken;
            //Send the randomised Session Token back to the control panel
            oos.writeObject(thisSessionToken);

            //Create a new timer to be stored in an hashmap with the session token
            Timer timer = new Timer();
            TimerTask taskA = new RemoveFromList();
            //Set the timer to 24 hours, after which it will be removed from the hashmap
            timer.schedule(taskA, (long) 8.64e+7);

            //Store counter ID and session token
            SessionCombinedHashmap.put(counter, timer);
            //Then store the hasmpa of counter and token with a timer
            SessionTokenListHashmap.put(counter, thisSessionToken);

        }else{
            oos.writeObject("User Invalid");
        }
    }

//    /**
//     * Checks the Hashmap of Tokens for the users token
//     * @param oos Object Output stream of Server
//     * @param oois ObjectInputStream
//     * @throws IOException
//     */
//    private void checkToken(ObjectOutputStream oos, ObjectInputStream oois) throws IOException, ClassNotFoundException {
//        //Get the user inputted token
//        String userToken = oois.readObject().toString();
//
//        //Boolean for checking existance of session token
//        Boolean tokenExists;
//        //Check the user inputted token
//        for(SessionTokenListHashmap.Entry<Integer, >){
//
//        }
//    }

    /**
     * Runs server
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        Run_Server();
    }
}