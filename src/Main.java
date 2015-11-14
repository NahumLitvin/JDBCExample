import java.sql.*;

public class Main {
    final static String DriverClassName = "com.mysql.jdbc.Driver";
    final static String ServerAddress = "jdbc:mysql://localhost/";
    final static String UserName = "root";
    final static String Password = "root";
    public static void main(String[] args) throws Exception{
        Connection conn = null;

        try {
            conn = getConnection();
            SelectWorldCountryAndPrintToScreen(conn);

        } finally {

            if (conn != null) conn.close();
        }
    }

    private static void SelectWorldCountryAndPrintToScreen(Connection conn) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        try {

            statement = conn.createStatement();
            final String SQLQuery = "SELECT * FROM world.city";
            resultSet = statement.executeQuery(SQLQuery);
            while (resultSet.next()) {
                int Id = resultSet.getInt("ID");
                String Name = resultSet.getString("Name");
                String CountryCode = resultSet.getString("CountryCode");
                String District = resultSet.getString("District");
                int Population = resultSet.getInt("Population");
                System.out.println("Id:" + Id + " Name:" + Name + " Code:" + CountryCode + " District:" + District + " Population:" + Population);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }

    private static Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Class.forName(DriverClassName).newInstance();
        return DriverManager.getConnection(
                ServerAddress,
                UserName,
                Password);
    }
}