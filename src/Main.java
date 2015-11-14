import java.sql.*;

public class Main {
    final static String DriverClassName = "com.mysql.jdbc.Driver";
    final static String ServerAddress = "jdbc:mysql://localhost/";
    final static String UserName = "root";
    final static String Password = "root";
    public static void main(String[] args) throws Exception{
        Connection conn=null;
        try {
        Class.forName(DriverClassName).newInstance();
        conn = DriverManager.getConnection(
                ServerAddress,
                UserName,
                Password);
        }finally {if(conn!=null)conn.close();}
    }
}