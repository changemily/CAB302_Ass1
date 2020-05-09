import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * Control Panel class
 * Class contains methods for connecting to, receiving and sending info to a server over a port,
 * @author Emily Chang
 * @version - under development
 */
public class ControlPanelClient {

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

            String buttonClicked = "Remove Schedule";

            //request given by user saved in local var request
            switch(buttonClicked)
            {
                case "Login request":
                    break;

                case "List billboards":
                    break;

                case "Get Billboard info":
                    break;

                case "Create edit billboard":
                    break;

                case "Delete billboard":
                    break;

                case "View schedule":
                    viewSchedule(oos,buttonClicked);
                    break;

                case "Schedule Billboard":
                    //Send details of billboard wanting to be scheduled to server
                    scheduleBillboard(oos, buttonClicked, "Billboard_1",
                            "2021-01-01T10:00:00.00", "10", "none");
                    break;

                case "Remove Schedule":
                    //Send details of billboard wanting to be scheduled to server
                    removeSchedule(oos,buttonClicked,"Billboard_1", "2021-01-01T10:00:00.00",
                           "10", "none");
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
        }

    }

    public static void viewSchedule(ObjectOutputStream oos, String buttonClicked) throws IOException {
        //Write the Client's request to the server
        oos.writeObject(buttonClicked);
    }

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
    public static void main(String args[]){
        Run_Client();
    }
}
