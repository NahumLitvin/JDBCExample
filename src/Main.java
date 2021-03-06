import com.sun.rowset.JdbcRowSetImpl;

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
            AddAnewRowToWordCountryTable(conn);
            SqlInjectionExample(conn);
            SpExample(conn);
            ScrollableAndUpdatable(conn);
            JdbcRowSetDemo();
            Transaction(conn);
        } finally {

            if (conn != null) conn.close();
        }
    }

    private static void Transaction(Connection conn) throws SQLException {
        try {
            conn.setAutoCommit(false);
            //DO SQL STUFF
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
        }

    }


    private static void ScrollableAndUpdatable(Connection conn) throws SQLException {
        //allow moving forward/backward/edit
        Statement statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        String sql = "SELECT * world.city";
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.moveToInsertRow();
        resultSet.updateString("Name", "City");
        resultSet.updateString("CountryCode", "PSE");
        resultSet.updateString("District", "Hebron");
        resultSet.updateInt("Population", 40452532);
        resultSet.insertRow();
    }

    private static void SpExample(Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call SHOW_SUPPLIERS}");
        ResultSet rs = cs.executeQuery();
    }

    private static void SqlInjectionExample(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        String myPassword = "Secret";
        statement.execute("SELECT * FROM users WHERE password='" + myPassword + "'");
        //SELECT * FROM users WHERE password='Secret'
        myPassword = "' OR 'a'='a";
        statement.execute("SELECT * FROM users WHERE password=" + myPassword + "'");
        //SELECT * FROM users WHERE password='' OR 'a'='a'
        myPassword = "' OR 'a'='a";
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM users WHERE password = ?");
        preparedStatement.setString(1, myPassword);
        //Throws Exception


    }

    private static void JdbcRowSetDemo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            JdbcRowSetImpl rowSet = new JdbcRowSetImpl();
            rowSet.setUrl("jdbc:mysql://localhost");
            rowSet.setUsername("root");
            rowSet.setPassword("root");
            rowSet.setCommand("SELECT * FROM emp");
            rowSet.execute();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void AddAnewRowToWordCountryTable(Connection conn) throws SQLException {
        Statement statement = null;
        int linesUpdated;
        try {
            statement = conn.createStatement();
            final String SQLQuery = "insert into world.city"
                    + "(Name,CountryCode,District,Population)"
                    + " values('MyNewCountry','PSE','Rafah',99999);";
            linesUpdated = statement.executeUpdate(SQLQuery);
            if (linesUpdated == 0) {
                System.out.println("error");
            }
        } finally {
            if (statement != null) statement.close();
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