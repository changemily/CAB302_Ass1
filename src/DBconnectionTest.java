import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains methods that test the functionality of DBconnection class methods
 *  * @author — Emily Chang
 *  * @version - skeleton
 */
public class DBconnectionTest {

    Connection connection;

    //Test 1: setup connection to database
    @BeforeEach @Test
    public void setupConnection(){
        connection = DBconnection.getInstance();
    }

    //Test 2: Retrieve data from DB
    @Test
    public void Retrieve_info() throws SQLException {

        //setup statement and result set
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Billboards");

        resultSet.next();

        //retrieve billboard data
        assertEquals("Billboard 1",  resultSet.getString("billboard_name"));
        assertEquals("Hello World!",  resultSet.getString("text"));
        assertEquals("red",  resultSet.getString("bg_colour"));
        assertEquals("image.jpg",  resultSet.getString("image_file"));
        assertEquals("2001-01-01",  resultSet.getString("time_scheduled"));
        assertEquals(10,  resultSet.getInt("minutes"));

        //close statement and connection to db
        statement.close();
        DBconnection.Close_connection();
    }

}
