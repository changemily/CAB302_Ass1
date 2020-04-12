import java.sql.*;

/**
 * Billboard server class
 * Class contains mehtods for connecting to, recieving and sending info to a database
 * @author Jarod
 * @version Skeleton
 */

public class billboardServer{
    public static void Setup_port(String properties_file){

    }

    public static void Connect_database(String DBproperties_file){
        private DBconnection(){
            Properties props = new Properties();
            FileInputStream in=null;
            try{
                in = new FileInputStream("./db.props");
                props.load(in);
                in.close();

                //data source, username and password
                String url = props.getProperty("jdbc.url");
                String username = props.getProperty("jdbc.username");
                String password = props.getProperty("jdbc.password");
                String schema = props.getProperty("jdbc.schema");

                //getting a connection
                instance = DriveManager.getConnection(url+"/"+schema, username, password);
            } catch(SQLException sqle){
                System.err.println(sqle);
            } catch(FileNotFoundException fnfe){
                System.err.println(fnfe);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public static void Start_up(){

    }

    public static void Add_table(){

    }

    public static void Server_Connection(){

    }

    public static void Close_connection(){
        connection.close();

    }

    public static String Login_request(String username. String password){

    }

}