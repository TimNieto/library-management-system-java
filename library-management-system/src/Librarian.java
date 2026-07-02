import java.util.ArrayList;
import java.util.List;

public class Librarian extends Person {
    private List<Book> borrowedBooks;

    public Librarian(String name, int librarianId) {
        super(name, librarianId);
        borrowedBooks = new ArrayList<>();
    }

    public String getLibrarianName() {
        return getName();
    }

    public void setLibrarianName(String librarianName) {
        setName(librarianName);
    }

    public int getLibrarianID() {
        return getId();
    }

    public void setLibrarianID(int librarianId) {
        setId(librarianId);
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    @Override
    public void borrowBook(Book book) throws Exception {
        if (book.getAvailableCopies() <= 0) {
            throw new Exception(book.getTitle() + " is currently unavailable.");
        }

        book.borrowBook();
        borrowedBooks.add(book);
    }

    @Override
    public void returnBook(Book book) throws Exception {
        if (!borrowedBooks.contains(book)) {
            throw new Exception(book.getTitle() + " was not borrowed.");
        }

        book.returnBook();
        borrowedBooks.remove(book);
    }
}