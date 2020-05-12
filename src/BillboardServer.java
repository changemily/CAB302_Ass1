import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;


/**
 * Billboard server class
 * Class contains methods for connecting to, receiving and sending info to a client over a port,
 * @author Emily Chang
 * @version 1 - under development
 */

public class BillboardServer {

    public static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS Users (username varchar(255),text varchar(1000)," +
                    "bg_colour varchar (255),image_file varchar(255), time_scheduled Datetime, minutes int);";

    public static final String CREATE_BILLBOARD_TABLE =
            "CREATE TABLE IF NOT EXISTS Billboards (billboard_name varchar(255)," +
                    "text varchar(1000),bg_colour varchar (255)," +
                    "image_file varchar(255), time_scheduled Datetime, minutes int);";

    public static final String CREATE_SCHEDULE_TABLE =
            "CREATE TABLE IF NOT EXISTS Schedule (billboard_name varchar(255), Start_TimeScheduled varchar(50), " +
                    "Duration varchar (255), recurrence varchar (50));";
    /**
     * Starts up Billboard server for connection to client
     * Sends and Receives information from client
     */
    public static void Run_Server() throws Exception {
        //create empty schedule, billboard list and user list
        ScheduleMultiMap billboard_schedule = new ScheduleMultiMap();

        BillboardList billboard_list = new BillboardList();

        //TEMP FOR TESTING
        billboard_list.Create_edit_Billboard("Billboard_1", "hello","red", "No image");
        billboard_list.Create_edit_Billboard("Billboard_2", "hello","red", "No image");

        //create DB connection
        Connection connection = DBconnection.getInstance();

        //checks if tables exist in DB, if not adds tables
        Check_tables(connection);

        //populate schedule, billboard list and user list with data from database
        billboard_schedule.RetrieveDBschedule(connection);

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
                        break;
                    case "Get Billboard info":
                        return_message = "returned Billboard info";
                        break;
                    case "Create edit billboard":
                        return_message = "Created/edited billboard";
                        break;
                    case "Delete billboard":
                        return_message = "billboard has been deleted";
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
                        removeSchedule(ois,connection,billboard_schedule);
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

    /**
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

        //print bb list
        System.out.println("billboard list: "+ billboard_list);

        //print what was received from client
        System.out.println("billboard name: "+ billboard_name + "\n" +
                "start time: "+start_time+"\n" +
                "duration: " + duration +"\n"+
                "recurrence: " +recurrence +"\n");

        //schedule billboard with client input
        billboard_schedule.scheduleBillboard(billboard_name,LocalDateTime.parse(start_time),
                Duration.ofMinutes(Integer.parseInt(duration)),recurrence, billboard_list.List_Billboards());

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
                                       ScheduleMultiMap billboard_schedule) throws Exception {
        //read billboard name sent by client
        Object Billboard_name = ois.readObject();
        System.out.println("billboard name: "+ Billboard_name);

        //read start time of viewing sent by client
        String startTime = ois.readObject().toString();

        //read duration sent by client
        String duration2 = ois.readObject().toString();

        //read recurrence sent by client
        String recurrence2 = ois.readObject().toString();

        //create schedule info object with client's input
        Schedule_Info schedule_info = new Schedule_Info(LocalDateTime.parse(startTime),
                Duration.ofMinutes(Integer.parseInt(duration2)),recurrence2);

        //Clear schedule table in DB
        billboard_schedule.Clear_DBschedule(connection);

        //remove billboard from schedule
        billboard_schedule.Schedule_Remove_billboard(Billboard_name.toString(),schedule_info);

        //write schedule to DB
        billboard_schedule.Write_To_DBschedule(connection);
    }

    /**
     * Runs server
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        Run_Server();
    }
}