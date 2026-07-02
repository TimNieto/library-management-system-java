import java.util.ArrayList;
import java.util.List;

public class Member extends Person {
    private List<Book> borrowedBooks;

    public Member(String name, int memberId) {
        super(name, memberId);
        borrowedBooks = new ArrayList<>();
    }

    public String getMemberName() {
        return getName();
    }

    public void setMemberName(String memberName) {
        setName(memberName);
    }

    public int getMemberID() {
        return getId();
    }

    public void setMemberID(int memberId) {
        setId(memberId);
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