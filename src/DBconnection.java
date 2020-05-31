import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Sourced from Week 7 lecture demonstrations
 * DBconnection class
 * Class contains methods for connecting to the database
 * @author Jarod Evans & Emily Chang
 * @version - complete
 */

public class DBconnection {
    /**
     * The singleton instance of the database connection.
     */
    private static Connection instance = null;

    /**
     * Constructor intializes the connection.
     */
    private DBconnection() {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("./db.props");
            props.load(in);
            in.close();

            // specify the data source, username and password
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            String schema = props.getProperty("jdbc.schema");

            // get a connection
            instance = DriverManager.getConnection(url + "/" + schema,
                    username, password);
        } catch (SQLException sqle) { //if connection to DB cannot be established
            //print error message
            System.err.println("Server could not connect to Database");
            //terminate server
            System.exit(1);
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Provides global access to the singleton instance of the UrlSet.
     *
     * @return a handle to the singleton instance of the UrlSet.
     */
    public static Connection getInstance() {
        if (instance == null) {
            //create new instance of DB connection
            new DBconnection();
        }
        return instance;
    }
}
