import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Read and Write to Database class
 * Class contains methods for reading and writing billboards and their schedules to the database.
 * @author - Jarod Evans
 * @version - under development
 */

public class ReadWriteDB {
    //Setup a hashmap for tracking billboards.
    HashMap<String, Billboard> billboardHashMap;

    //Setup a schedule multimap
    ScheduleMultiMap scheduleMultiMap = new ScheduleMultiMap();

    //A method for writing the billboardList to the database
    public void Write_To_DBbillboard(Connection connection) throws SQLException {
        //create statement
        Statement st = connection.createStatement();

        //for every billboard name in Billboard_schedule
        for (Billboard billboard : billboardHashMap.values() ) {

            //Pass the values of each billboard to the SQL statement.
            String billboard_name = billboard.Billboard_name;
            String text = billboard.Billboard_text;
            String bg_colour = billboard.Bg_colour;
            String image_file = billboard.Image_file;
            LocalDateTime time_scheduled = billboard.Time_scheduled;
            Duration Duration_mins = billboard.duration;

            st.executeQuery("INSERT INTO Schedule (billboard_name, text, bg_colour, image_file, time_scheduled, Duration_mins) " +
                    "VALUES(\""+billboard_name+"\",\""+text+"\",\""+bg_colour+"\",\""+image_file+"\",\""+time_scheduled+"\",\""+Duration_mins+"\");");
        }

        //close statement
        st.close();
    }

    public void RetrieveDBbillboardList(Connection connection) throws Exception {

        final String SELECT = "SELECT * FROM billboard ORDER BY billboard_name desc";


        //create statement
        Statement st = connection.createStatement();

        ResultSet rs = st.executeQuery(SELECT);

        //for every database entry
        while (rs.next())
        {
            //store database info in local variables
            String billboard_name = rs.getString(0);
            String text = rs.getString(1);
            String bg_colour = rs.getString(2);
            String image_file = rs.getString(3);
            String time_scheduled = rs.getString(4);
            String Duration_mins = rs.getString(5);

            //store time scheduled and duration pair in array schedule_info
            Billboard billboard = new Billboard(billboard_name, text, bg_colour,
                    image_file, LocalDateTime.parse(time_scheduled), Duration.parse(Duration_mins));

            //store billboard name with corresponding times scheduled and durations
            billboardHashMap.put(billboard_name, billboard);
        }

        //close ResultSet
        rs.close();
        //close statement
        st.close();
    }

    public void Clear_DBbillboardList(Connection connection) throws SQLException {
        //create statement to connect to db
        Statement st = connection.createStatement();

        //for all entries in billboardHashMap
        for (String billboard_name : billboardHashMap.keySet())
        {
            //remove each entry from DB using billboard_name
            st.execute("DELETE FROM Schedule WHERE billboard_name=\""+billboard_name+"\";");
        }
    }


}
