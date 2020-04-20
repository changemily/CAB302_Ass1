import java.sql.Connection;
import java.sql.SQLException;


/**
 * Billboard server class
 * Class contains methods for connecting to, receiving and sending info to a database
 * @author Jarod Evans & Emily Chang
 * @version 1 - Skeleton
 */

public class billboardServer{

    Connection connection;

    public static void Setup_port(String properties_file){
    //setup port used to connect Server with viewer and control panel
    }

    public void Connect_database(){

        //connection = DBconnection.getInstance();
    }

    public void Start_up(){
        // creates tables if they are not present
        //runs add table

    }

    public void Add_table(){
        //Adds tables to database
    }

    public void Server_Connection(){
        //Connect server to billboard viewer and control panel
    }

    public void Close_connection() throws SQLException {
        //closes connection to DB
        //DBconnection.Close_connection();
    }

    /*
    public String Login_request(String username, String password){
        // Sends back error or valid session token depending on outcome of login request
        return ;
    }
    */
}