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

public class billboardServer{

    public static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS Users (username varchar(255),text varchar(1000)," +
                    "bg_colour varchar (255),image_file varchar(255), time_scheduled Datetime, minutes int);";

    public static final String CREATE_BILLBOARD_TABLE =
            "CREATE TABLE IF NOT EXISTS Billboards (billboard_name varchar(255)," +
                    "text varchar(1000),bg_colour varchar (255)," +
                    "image_file varchar(255), time_scheduled Datetime, minutes int);";

    public static final String CREATE_SCHEDULE_TABLE =
            "CREATE TABLE IF NOT EXISTS Schedule (billboard_name varchar(255), Start_TimeScheduled varchar(10), " +
                    "Duration varchar (255), recurrence varchar (4));";
    /**
     * Starts up Billboard server for connection to client
     * Sends and Receives information from client
     */
    public static void Run_Server() throws Exception {
        //create empty schedule, billboard list and user list
        scheduleMultiMap billboard_schedule = new scheduleMultiMap();

        billboardHashMap billboard_list = new billboardHashMap();

        //TEMP FOR TESTING
        billboard_list.Create_edit_Billboard("Billboard_1", "hello","red", "No image");

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
                Object o = ois.readObject();

                //print what was received from client
                System.out.println("received from client: "+o);

                String return_message;

                //save return message, based on what request was received from the client, in local variable return message
                switch(o.toString())
                {
                    case "Login request":
                        return_message = "Login request";
                        break;
                    case "List billboards":
                        return_message = "returned List of billboards";
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
                        billboard_schedule.View_schedule();
                        break;
                    case "Schedule Billboard":
                        return_message = "Billboard has been scheduled";

                        //read parameters sent by client
                        String billboard_name = ois.readObject().toString();
                        String start_time = ois.readObject().toString();
                        String duration = ois.readObject().toString();
                        String recurrence = ois.readObject().toString();

                        //print bb list
                        System.out.println("billboard list: "+billboard_list);

                        //print what was received from client
                        System.out.println("billboard name: "+ billboard_name + "\n" +
                                "start time: "+start_time+"\n" +
                                "duration: " + duration +"\n"+
                                "recurrence: " +recurrence +"\n");

                        //schedule billboard with client input
                        billboard_schedule.scheduleBillboard(billboard_name,LocalDateTime.parse(start_time),
                                Duration.ofMinutes(Integer.parseInt(duration)),recurrence, billboard_list.List_Billboards());

                        //write to DB
                        billboard_schedule.Write_To_DBschedule(connection);

                        break;
                    case "Remove billboard":
                        return_message = "billboard has been removed from schedule";

                        //read billboard name sent by client
                        Object Billboard_name = ois.readObject();

                        System.out.println("billboard name: "+ Billboard_name);
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

        //write to DB

        //close connection
        connection.close();
    }


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

    /*
    public String Login_request(String username, String password){
        // Sends back error or valid session token depending on outcome of login request
        return ;
    }
    */

    public static void main(String args[]) throws Exception {
        Run_Server();
    }
}