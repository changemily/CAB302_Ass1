import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * Control Panel class
 * Class contains methods for connecting to, receiving and sending info to a server over a port,
 * @author Emily Chang
 * @version - under development
 */
public class controlPanel_Client {

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


            String button_clicked = "Schedule Billboard";
            String request;

            //request given by user saved in local var request
            switch(button_clicked)
            {
                case "Login request":
                    request = "Login request";
                    break;
                case "List billboards":
                    request = "List billboards";
                    break;
                case "Get Billboard info":
                    request = "Get Billboard info";
                    break;
                case "Create edit billboard":
                    request = "Create edit billboard";
                    break;
                case "Delete billboard":
                    request = "Delete billboard";
                    break;
                case "View schedule":
                    request = "View schedule";
                    //Write the Client's request to the server
                    oos.writeObject(request);
                    oos.flush();
                    break;

                case "Schedule Billboard":
                    request = "Schedule Billboard";
                    //Write the Client's request to the server
                    oos.writeObject(request);

                    //Write the details needed to schedule a billboard
                    oos.writeObject("Billboard_1");
                    oos.writeObject("2021-01-01T10:00:00.00");
                    oos.writeObject("10");
                    oos.writeObject("none");
                    oos.flush();
                    break;

                case "Remove billboard":
                    request = "Remove billboard";
                    //Write the Client's request to the server
                    oos.writeObject(request);

                    //Write the details needed to remove a billboard
                    oos.writeObject("billboard_name entry");
                    oos.flush();
                    break;
                case "List users":
                    request = "List users";
                    break;
                case "Create user":
                    request = "Create user";
                    break;
                case "Get user permissions":
                    request = "Get user permissions";
                    break;
                case "Set user permissions":
                    request = "Set user permissions";
                    break;
                case "Set user password":
                    request = "Set user password";
                    break;
                default:
                    request = "No match";
            }

            oos.flush();

            //read response from server
            Object o = ois.readObject();

            //print what was received from server
            System.out.println("received from server: "+o);

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
        //read port & network address from server_props file

        // creates tables if they are not present
        //runs add table

    }

    public static void main(String args[]) throws IOException {
        Run_Client();
    }
}
