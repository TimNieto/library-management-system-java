import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper 
{
    private static final String DB_URL = "jdbc:sqlite:library.db";

    public static Connection getConnection() throws SQLException 
    {
        return DriverManager.getConnection(DB_URL);
    }

    public static ResultSet executeQuery(String query) throws SQLException 
    {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }
}
