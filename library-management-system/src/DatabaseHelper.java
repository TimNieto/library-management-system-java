import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:library.db";

    private DatabaseHelper() {
        // Utility class
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        return connection;
    }

    public static void initializeDatabase() {
        String createBooksTable = """
                CREATE TABLE IF NOT EXISTS Books (
                    ISBN TEXT PRIMARY KEY NOT NULL,
                    Title TEXT NOT NULL,
                    Author TEXT NOT NULL,
                    publicationDate TEXT NOT NULL,
                    availableCopies INTEGER NOT NULL
                )
                """;

        String createMembersTable = """
                CREATE TABLE IF NOT EXISTS Members (
                    memberID INTEGER PRIMARY KEY NOT NULL,
                    memberName TEXT NOT NULL
                )
                """;

        String createBorrowedBooksTable = """
                CREATE TABLE IF NOT EXISTS BorrowedBooks (
                    borrowID INTEGER PRIMARY KEY AUTOINCREMENT,
                    ISBN TEXT NOT NULL,
                    memberID INTEGER NOT NULL,
                    borrowDate TEXT NOT NULL,
                    FOREIGN KEY (ISBN) REFERENCES Books(ISBN),
                    FOREIGN KEY (memberID) REFERENCES Members(memberID)
                )
                """;

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
            statement.execute(createBooksTable);
            statement.execute(createMembersTable);
            statement.execute(createBorrowedBooksTable);
        } catch (SQLException exception) {
            System.out.println("Database initialization failed: " + exception.getMessage());
        }
    }
}