import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Read and Write to Database class
 * Class contains methods for reading and writing billboards and their schedules to the database.
 * @author - Jarod Evans
 * @version - under development
 */


/**
 * Methods for writing and reading - from the BillboardList.
 */
public class ReadWriteDB {
    //Setup a hashmap for tracking billboards.
    HashMap<String, Billboard> billboardHashMap;

    //Setup a schedule multimap
    ScheduleMultiMap scheduleMultiMap = new ScheduleMultiMap();

    MultiMap<String, Schedule_Info> Billboard_schedule;

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


    /**
     * extracts schedule data from database and stores it in Billboard_schedule
     * @throws SQLException throws exception if billboard does not exist or combination of billboard &
     * schedule information does not exist
     */
    public void RetrieveDBschedule(Connection connection) throws Exception {

        final String SELECT = "SELECT * FROM billboard ORDER BY billboard_name desc";
        final String SELECT1 = "SELECT * FROM schedule ORDER BY Start_TimeScheduled desc";

        //create statement
        Statement st = connection.createStatement();

        //SQL statements
        ResultSet rs = st.executeQuery(SELECT);
        ResultSet rs1 = st.executeQuery(SELECT1);

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

        //for every database entry
        while (rs1.next())
        {
            //store database info in local variables
            String billboard_name = rs1.getString(1);
            String Start_TimeScheduled = rs1.getString(2);
            String duration = rs1.getString(3);
            String recurrence = rs1.getString(4);

            //store time scheduled and duration pair in array schedule_info
            Schedule_Info schedule_info = new Schedule_Info(LocalDateTime.parse(Start_TimeScheduled),
                    Duration.parse(duration), recurrence);

            //store billboard name with corresponding times scheduled and durations
            Billboard_schedule.put(billboard_name, schedule_info);
        }

        //close ResultSet
        rs1.close();
        //close statement
        st.close();
    }

    public void Clear_DBschedule(Connection connection) throws SQLException {
        //create statement
        Statement st = connection.createStatement();

        //for all entries in schedule
        for (String billboard_name : Billboard_schedule.keySet())
        {
            //remove entry from DB
            st.execute("DELETE FROM Schedule WHERE billboard_name=\""+billboard_name+"\";");
        }
    }

    public void Write_To_DB(Connection connection, String DataType) throws SQLException {
        switch (DataType){

            case "Billboard":
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
                break;
            case "Schedule":
                //create statement
                Statement st1 = connection.createStatement();
                //for every billboard name in Billboard_schedule
                for (String billboard_name : Billboard_schedule.keySet() ) {

                    //create array list to store viewings of billboard
                    ArrayList<Schedule_Info> viewings = Billboard_schedule.get(billboard_name);

                    //for every viewing of billboard
                    for ( Schedule_Info viewing : viewings ) {
                        //store Billboard_schedule info in local variables
                        String Start_TimeScheduled = viewing.StartTime_Scheduled.toString();
                        String duration = viewing.duration.toString();
                        String recurrence = viewing.Recurrence;

                        st1.executeQuery("INSERT INTO Schedule (billboard_name,Start_TimeScheduled, Duration,recurrence) " +
                                "VALUES(\""+billboard_name+"\",\""+Start_TimeScheduled+"\",\""+duration+"\",\""+recurrence+"\");");
                    }
                }
                //close statement
                st1.close();
                break;
        }

    }


}
