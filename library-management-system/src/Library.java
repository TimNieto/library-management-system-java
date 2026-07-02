import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;
    private List<Member> members;
    private List<Object[]> borrowedBooks;

    public Library() {
        DatabaseHelper.initializeDatabase();

        books = new ArrayList<>();
        members = new ArrayList<>();
        borrowedBooks = new ArrayList<>();

        loadBooksFromDatabase();
        loadMembersFromDatabase();
        loadBorrowedBooksFromDatabase();
    }

    private void loadBooksFromDatabase() {
        String query = "SELECT Title, Author, ISBN, publicationDate, availableCopies FROM Books";

        try (Connection connection = DatabaseHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String author = resultSet.getString("Author");
                String isbn = resultSet.getString("ISBN");
                String publicationDate = resultSet.getString("publicationDate");
                int availableCopies = resultSet.getInt("availableCopies");

                books.add(new Book(title, author, publicationDate, isbn, availableCopies));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void loadMembersFromDatabase() {
        String query = "SELECT memberID, memberName FROM Members";

        try (Connection connection = DatabaseHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int memberId = resultSet.getInt("memberID");
                String name = resultSet.getString("memberName");

                members.add(new Member(name, memberId));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void loadBorrowedBooksFromDatabase() {
        String query = "SELECT memberID, ISBN, borrowDate FROM BorrowedBooks";

        try (Connection connection = DatabaseHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int memberId = resultSet.getInt("memberID");
                String bookIsbn = resultSet.getString("ISBN");
                Date borrowDate = resultSet.getDate("borrowDate");

                borrowedBooks.add(new Object[] { memberId, bookIsbn, borrowDate });
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Object[]> getBorrowedBooks() {
        return borrowedBooks;
    }

    public Book findBookByISBN(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }

        return null;
    }

    public Member findMemberByID(int memberId) {
        for (Member member : members) {
            if (member.getMemberID() == memberId) {
                return member;
            }
        }

        return null;
    }

    public void addBook(Book book) {
        String query = """
                INSERT INTO Books (Title, Author, ISBN, publicationDate, availableCopies)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setString(4, book.getPublicationDate());
            statement.setInt(5, book.getAvailableCopies());
            statement.executeUpdate();

            books.add(book);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void removeBook(Book book) {
        String query = "DELETE FROM Books WHERE ISBN = ?";

        try (Connection connection = DatabaseHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, book.getIsbn());
            statement.executeUpdate();

            books.remove(book);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void addMember(Member member) {
        String query = """
                INSERT INTO Members (memberID, memberName)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, member.getMemberID());
            statement.setString(2, member.getName());
            statement.executeUpdate();

            members.add(member);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void removeMember(Member member) {
        String query = "DELETE FROM Members WHERE memberID = ?";

        try (Connection connection = DatabaseHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, member.getMemberID());
            statement.executeUpdate();

            members.remove(member);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void borrowBook(Book book, Member member) throws Exception {
        if (book.getAvailableCopies() <= 0) {
            throw new Exception("No available copies for this book.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        updateBookCopiesInDatabase(book);
        addBorrowingRecord(member, book);
    }

    public void returnBook(Book book, Member member) throws Exception {
        book.returnBook();
        updateBookCopiesInDatabase(book);
        removeBorrowingRecord(member, book);
    }

    private void updateBookCopiesInDatabase(Book book) {
        String query = "UPDATE Books SET availableCopies = ? WHERE ISBN = ?";

        try (Connection connection = DatabaseHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, book.getAvailableCopies());
            statement.setString(2, book.getIsbn());
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void addBorrowingRecord(Member member, Book book) {
        String query = """
                INSERT INTO BorrowedBooks (memberID, ISBN, borrowDate)
                VALUES (?, ?, ?)
                """;

        Date borrowDate = new Date(System.currentTimeMillis());

        try (Connection connection = DatabaseHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, member.getMemberID());
            statement.setString(2, book.getIsbn());
            statement.setDate(3, borrowDate);
            statement.executeUpdate();

            borrowedBooks.add(new Object[] { member.getMemberID(), book.getIsbn(), borrowDate });
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void removeBorrowingRecord(Member member, Book book) {
        String query = "DELETE FROM BorrowedBooks WHERE memberID = ? AND ISBN = ?";

        try (Connection connection = DatabaseHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, member.getMemberID());
            statement.setString(2, book.getIsbn());
            statement.executeUpdate();

            borrowedBooks.removeIf(record -> record[0].equals(member.getMemberID())
                    && record[1].equals(book.getIsbn()));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}