import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Library
{
    private List<Book> books; 
    private List<Member> members; 
    private List<Object[]> borrowedBooks;  

    public Library() 
    {
        books = new ArrayList<>();
        members = new ArrayList<>();
        borrowedBooks = new ArrayList<>();
        loadBooksFromDatabase();
        loadMembersFromDatabase();
        loadBorrowedBooksFromDatabase();  
    }

    private void loadBooksFromDatabase() 
    {
        String query = "SELECT * FROM Books";
        try (ResultSet rs = DatabaseHelper.executeQuery(query)) 
        {
            while (rs.next()) 
            {
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String ISBN = rs.getString("ISBN");
                String publicationDate = rs.getString("publicationDate");
                int availableCopies = rs.getInt("availableCopies");
                books.add(new Book(title, author, publicationDate, ISBN, availableCopies));
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private void loadMembersFromDatabase() 
    {
        String query = "SELECT * FROM Members";
        try (ResultSet rs = DatabaseHelper.executeQuery(query)) 
        {
            while (rs.next()) {
                String name = rs.getString("memberName");
                int ID = rs.getInt("memberID");
                members.add(new Member(name, ID));
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private void loadBorrowedBooksFromDatabase() 
    {
        String query = "SELECT * FROM BorrowedBooks";
        try (ResultSet rs = DatabaseHelper.executeQuery(query)) 
        {
            while (rs.next()) 
            {
                int memberID = rs.getInt("memberID");
                String bookISBN = rs.getString("ISBN");
                Date borrowDate = rs.getDate("borrowDate");
                borrowedBooks.add(new Object[]{memberID, bookISBN, borrowDate});
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public List<Book> getBooks() 
    {
        return books;
    }

    public List<Member> getMembers() 
    {
        return members;
    }

    public List<Object[]> getBorrowedBooks() 
    {
        return borrowedBooks;
    }

    public Book findBookByISBN(String isbn) 
    {
        for (Book book : books) 
        {
            if (book.getIsbn().equals(isbn)) 
            {
                return book;
            }
        }
        return null; 
    }

    public Member findMemberByID(int memberId) 
    {
        for (Member member : members) {
            if (member.getMemberID() == memberId) 
            {
                return member;
            }
        }
        return null; 
    }

    public void addBook(Book book) 
    {
        String query = "INSERT INTO Books (Title, Author, ISBN, publicationDate, availableCopies) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query)) 
        {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getPublicationDate());
            pstmt.setInt(5, book.getAvailableCopies());
            pstmt.executeUpdate();
            books.add(book);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public void removeBook(Book book) 
    {
        String query = "DELETE FROM Books WHERE ISBN = ?";
        try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query)) 
        {
            pstmt.setString(1, book.getIsbn());
            pstmt.executeUpdate();
            books.remove(book);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public void addMember(Member member) 
    {
        String query = "INSERT INTO Members (memberName, memberID) VALUES (?, ?)";
        try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query)) 
        {
            pstmt.setString(1, member.getName());
            pstmt.setInt(2, member.getMemberID());
            pstmt.executeUpdate();
            members.add(member); 
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public void removeMember(Member member) 
    {
        String query = "DELETE FROM Members WHERE memberID = ?";
        try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query)) 
        {
            pstmt.setInt(1, member.getMemberID());
            pstmt.executeUpdate();
            members.remove(member);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public void borrowBook(Book book, Member member) throws Exception 
    {
        if (book.getAvailableCopies() <= 0) 
        {
            throw new Exception("No available copies for this book.");
        }
    
    
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        updateBookCopiesInDatabase(book); 
        
        String query = "INSERT INTO BorrowedBooks (memberID, ISBN, borrowDate) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query)) 
        {
            pstmt.setInt(1, member.getMemberID());
            pstmt.setString(2, book.getIsbn());
            pstmt.setDate(3, new Date(System.currentTimeMillis())); 
            pstmt.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        borrowedBooks.add(new Object[]{member.getMemberID(), book.getIsbn(), new Date(System.currentTimeMillis())});
    }
    
    

    public void returnBook(Book book, Member member) throws Exception 
    {
        book.returnBook();
        updateBookCopiesInDatabase(book);
        removeBorrowingRecord(member, book);
        borrowedBooks.removeIf(record -> record[0].equals(member.getMemberID()) && record[1].equals(book.getIsbn()));
    }
    

    private void updateBookCopiesInDatabase(Book book) 
    {
    String query = "UPDATE Books SET availableCopies = ? WHERE ISBN = ?";
    try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query)) 
    {
        pstmt.setInt(1, book.getAvailableCopies());
        pstmt.setString(2, book.getIsbn());
        pstmt.executeUpdate();
    } 
    catch (SQLException e) 
    {
        e.printStackTrace();
    }
}

    private void removeBorrowingRecord(Member member, Book book)
    {
        String query = "DELETE FROM BorrowedBooks WHERE memberID = ? AND ISBN = ?";
        try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query)) 
        {
            pstmt.setInt(1, member.getMemberID());
            pstmt.setString(2, book.getIsbn());
            pstmt.executeUpdate();
            borrowedBooks.removeIf(record -> record[0].equals(member.getMemberID()) && record[1].equals(book.getIsbn()));
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}
