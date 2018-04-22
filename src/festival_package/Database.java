package festival_package;


import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;



public class Database {




    //Globals
    public static ArrayList<Festival> Festivals = new ArrayList<>();
    public static ArrayList<Festival> Festivals_Names = new ArrayList<>();

    public static ArrayList<User> Users = new ArrayList<>();
    public static ArrayList<String> User_Names = new ArrayList<>();


    public static ArrayList<User> Displayed_Users = new ArrayList<>();
    public static ArrayList<String> Displayed_User_Names = new ArrayList<>();


    public static ArrayList<String> Locations = new ArrayList<>();

    public static ArrayList<String> test_values = new ArrayList<>();

    public static ArrayList<String> viewed_lis = new ArrayList<>();

    public static String cur_user_name = "";
    public static String cur_user_guid = "";

    static private Connection connection;




    static {
        try {
            connection = DriverManager.getConnection
                        (
                                "jdbc:mysql://festivalproject.mysql.database.azure.com/festivals_project",
                                "festival_admin@festivalproject",
                                "muK43her"
                        );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Queries the DB for a given username and returns the guid for that user if found.
     * Returns an empty String "" if the username is not found
     * TODO: Add password authentication
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public static String authenticate(String username, String password) throws SQLException
    {
        String query = ("SELECT userID " +
                "FROM Users " +
                "WHERE user_name = '" + username + "';");
        System.out.println(query);

        String guid = "";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            guid = resultSet.getString("userID");
            System.out.println(guid);
        }

        return guid;
    }

    /**
     * Queries the database for a given username. If any results are returned in resultSet, returns false.
     * @param username
     * @return
     * @throws SQLException
     */
    public static boolean usernameAvailable(String username) throws SQLException
    {
        String query = ("SELECT user_name " +
                "FROM Users " +
                "WHERE user_name = '" + username + "';");
        System.out.println(query);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            return false;
        }
        return true;
    }

    /**
     * TODO:FIX LOCATION ISSUE. MAKE ZIP CODE WORK WITH USERLOCATION.
     * @param username
     * @param password
     * @param date
     * @param state
     * @param city
     * @param address
     * @param zip
     */
    public static void insertNewUser(String username, String password, LocalDate date, String state, String city, String address, int zip){
        String dob = date.toString();
        String cityState = (city + ", " + state);
        String insertLocationSQL = ("INSERT INTO Location VALUES (" + city + ", " + state + ", " + address + ", " + zip + ", " + cityState);
        //String insertUserSQL = "INSERT INTO User VALUES (UUID(), " + username + ", " + dob + ", "
    }

    public static void insert_to_table(Statement statement, String table, String value) throws SQLException
    {

        String sql = "INSERT INTO " + table + " VALUES ( \'" + value + "\');";
        System.out.println("Query: " + sql);

        statement.executeUpdate(sql);
    }
    public static String remove_defs(String table)
    {
        int length = table.length();
        int i = length - 1;

        int last_paranthesis_index = i;
        int first_paranthesis_index = -1;

        for(; i >= 0; i--)
        {
            if(table.charAt(i) == '(')
            {
                first_paranthesis_index = i;
                break;
            }
        }

        return table.substring(0, first_paranthesis_index);
    }
    public static String fix_title(String word)
    {
        String result = word;


        if(result.endsWith(".fxml"))
        {
            result = result.substring(0, result.indexOf(".fxml"));
        }
        if(result.contains("_"))
        {
            result = result.replace('_', ' ');
        }


        for(int i = 0; i < result.length(); i++)
        {
            if(Character.isLowerCase(result.charAt(i)) && i + 1 < result.length() && Character.isUpperCase(result.charAt(i + 1)))
            {
                System.out.println("character: " + result.charAt(i) + " is lowercase and " + result.charAt(i + 1) + " is uppercase");
                System.out.println("Old result: " + result);
                result = word.substring(0, i + 1) + " " + result.substring(i + 1, result.length());
                System.out.println("New Result: " + result);
            }

        }

        return result;
    }
    public static ResultSet select_from_table(ArrayList<String> columns, String table) throws SQLException
    {

        String new_table = table;

        if(table.endsWith(")"))
        {
            new_table = remove_defs(table);
        }


        String query = "SELECT ";
        int i;

        for(i = 0; i < columns.size(); i++)
        {
            query = query.concat(columns.get(i));

            if(i + 1 != columns.size())
            {
                query += ", ";
            }
        }

        query = query.concat("\nFROM " + new_table + ";");

        System.out.println("Query: " + query);

        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
    public static ResultSet select_from_table(String column, String table) throws SQLException
    {

        String new_table = table;

        if(table.endsWith(")"))
        {
            new_table = remove_defs(table);
        }



        String sql = "SELECT " + column + " FROM " + new_table + ";";

        System.out.println("Query: " + sql);

        Statement statement = connection.createStatement();

        return statement.executeQuery(sql);
    }
    public static ResultSet select_all_from_table(Statement statement, String table) throws SQLException
    {

        String new_table = table;

        if(table.endsWith(")"))
        {
            new_table = remove_defs(table);
        }


        String sql = "SELECT *";

        sql += "\nFROM " + new_table + ";";

        System.out.println("Query: " + sql);
        return statement.executeQuery(sql);
    }

    public static LocalDate date_from_string(String date)
    {
        return LocalDate.parse(date);
    }


    public static User user_from_user_name(String username)
    {
        User result;

        User temp = new User(username);

        result = Users.get(Users.indexOf(temp));

        return result;
    }

    public static void refresh_lists()
    {

    }

    public static void refresh_users() throws SQLException, ParseException
    {
        Users.clear();

        ArrayList<String> columns = new ArrayList<>();

        columns.add("userId");
        columns.add("user_name");
        columns.add("birthdate");
        columns.add("user_location");
        columns.add("is_company");

        ResultSet rows_user_IDs = select_from_table( columns,"users");
        System.out.println("GOT TO RESULTSET");

        while(rows_user_IDs.next())
        {
            LocalDate birth_date = date_from_string(rows_user_IDs.getDate("birthdate").toString());

            User cur_user = new User(rows_user_IDs.getString("userID"),
                    rows_user_IDs.getString("user_name"),
                    birth_date,
                    rows_user_IDs.getString("user_location"),
                    rows_user_IDs.getBoolean("is_company"));

            Users.add(cur_user);
            System.out.println(cur_user.toString());
        }

        rows_user_IDs.close();
        re_add_user_names();


    }

    public static void re_add_user_names()
    {
        User_Names.clear();

        for(int i = 0; i < Users.size(); i++)
        {
            User_Names.add(Users.get(i).user_name);
        }
    }

    public static void refresh_festivals() throws SQLException {
        Festivals.clear();

        ArrayList<String> columns = new ArrayList<>();

        columns.add("festID");
        columns.add("location");
        columns.add("production_comp");
        columns.add("type_fest");
        columns.add("start_date");
        columns.add("end_date");
        columns.add("price");
        String festival_table = "festival";

        columns.add("type_fest");

        //all the subtables of festival
        String art_table = "art";
        String beer_table = "beer";
        String comedy_table = "comedy";
        String music_table = "music";


        columns.add("name");
        String providers_table = "providers";

        String query = "SELECT ";

        for(int i = 0; i < columns.size(); i++)
        {
            query += columns.get(i);

            if((i + 1) < columns.size())
            {
                query += " ,";
            }
        }

        query += " FROM " + festival_table;

        query += "\n JOIN " + art_table + " ON " + festival_table + ".fest_id = " + art_table + ".fest_id";
        query += "\n JOIN " + beer_table + " ON " + festival_table + ".fest_id = " + beer_table + ".fest_id";
        query += "\n JOIN " + comedy_table + " ON " + festival_table + ".fest_id = " + comedy_table + ".fest_id";
        query += "\n JOIN " + music_table + " ON " + festival_table + ".fest_id = " + music_table + ".fest_id";
        query += "\n JOIN " + providers_table + " ON " + festival_table + ".fest_id = " + providers_table + ".fest_id";

        query += ";";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while(resultSet.next())
        {
            LocalDate start_date = date_from_string(resultSet.getDate("start_date").toString());
            LocalDate end_date = date_from_string(resultSet.getDate("end_date").toString());

            Festival temp = new Festival(resultSet.getString("fest_ID"),
                                         resultSet.getString("location"),
                                         resultSet.getString("production_comp"),
                                         resultSet.getString("fest_type"),
                                         start_date, end_date,
                                         resultSet.getFloat("price")
                    );

            if (temp.type.equals("Music"))
            {

            }
            if (temp.type.equals("Comedy"))
            {

            }
            if (temp.type.equals("Art"))
            {

            }
            if(temp.type.equals("Beer"))
            {
                
            }

        }


    }

    public static void re_add_fest_names()
    {

    }




}
