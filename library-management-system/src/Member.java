import java.util.*;

public class Member extends Person //Inheritance
{
    private String memberName;
    private int memberID;
    private ArrayList<Book> borrowedBooks;

    public Member(String name, int ID) 
    {
        super(name, ID);
        this.memberName = name;
        this.memberID = ID;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getMemberName() 
    {
        return memberName;
    }

    public void setMemberName(String memberName) 
    {
        this.memberName = memberName;
    }

    public int getMemberID() 
    {
        return memberID;
    }

    public void setMemberID(int memberID) 
    {
        this.memberID = memberID;
    }

    public ArrayList<Book> getBorrowedBooks() 
    {
        return borrowedBooks;
    }

    public void setBorrowedBooks(ArrayList<Book> borrowedBooks) 
    {
        this.borrowedBooks = borrowedBooks;
    }

    @Override
    public void borrowBook(Book book) throws Exception 
    {
        if (book.getAvailableCopies() > 0) 
        {
            book.borrowBook();
            borrowedBooks.add(book);
        } 
        else 
        {
            throw new Exception(book.getTitle() + " is currently unavailable.");
        }
    }

    @Override
    public void returnBook(Book book) throws Exception 
    {
        if (borrowedBooks.contains(book)) 
        {
            book.returnBook();
            borrowedBooks.remove(book);
        } 
        else 
        {
            throw new Exception(book.getTitle() + " was not borrowed.");
        }
    }
}
