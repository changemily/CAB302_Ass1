import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Billboard server class
 * Class contains methods for connecting to, receiving and sending info to a client over a port,
 * @author Emily Chang
 * @version 1 - under development
 */

public class billboardServer{

    /**
     * Starts up Billboard server for connection to client
     * Sends and Receives information from client
     */
    public static void Run_Server() {
        //create empty schedule, billboard list and user list
        scheduleMultiMap billboard_schedule = new scheduleMultiMap();
        //populate schedule, billboard list and user with data from database
        //billboard_schedule.RetrieveDBschedule();

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
                        Object billboard_name = ois.readObject();
                        Object start_time = ois.readObject();
                        Object duration = ois.readObject();
                        Object recurrence = ois.readObject();
                        Object billboard_list = ois.readObject();

                        //print what was received from client
                        System.out.println("billboard name: "+ billboard_name + "\n" +
                                "start time: "+start_time+"\n" +
                                "duration: " + duration +"\n"+
                                "recurrence: " +recurrence +"\n" +
                                "billboard list: " +billboard_list);
                        //billboard_schedule.scheduleBillboard();
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

    }


    public void Add_table(){
        //Adds tables to database

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