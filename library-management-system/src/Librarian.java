import java.util.*;

public class Librarian extends Person //Inheritance
{
    private String librarianName;
    private int librarianID;
    private ArrayList<Book> borrowedBooksInLibrary;

    public Librarian(String name, int ID) {
        super(name, ID);
        this.librarianName = name;
        this.librarianID = ID;
        this.borrowedBooksInLibrary = new ArrayList<>();
    }

    public String getLibrarianName() 
    { 
        return librarianName; 
    }
    public void setLibrarianName(String librarianName) 
    { 
        this.librarianName = librarianName; 
    }

    public int getLibrarianID() 
    { 
        return librarianID; 
    }
    public void setLibrarianID(int librarianID) 
    { 
        this.librarianID = librarianID; 
    }

    public ArrayList<Book> getBorrowedBooksInLibrary() 
    { 
        return borrowedBooksInLibrary; 
    }
    public void setBorrowedBooksInLibrary(ArrayList<Book> borrowedBooksInLibrary) 
    { 
        this.borrowedBooksInLibrary = borrowedBooksInLibrary; 
    }

    @Override
    public void borrowBook(Book book) throws Exception 
    {
        if (book.getAvailableCopies() > 0) 
        {
            book.borrowBook();
            borrowedBooksInLibrary.add(book);
            System.out.println(getName() + " borrowed: " +  book.getTitle());
        } 
        else 
        {
            throw new Exception(book.getTitle() + " - Book is currently unavailable.");
        }
    }

    @Override
    public void returnBook(Book book) throws Exception 
    {
        if (borrowedBooksInLibrary.contains(book)) 
        {
            book.returnBook();
            borrowedBooksInLibrary.remove(book);
            System.out.println(getName() + " returned: " + book.getTitle());
        } 
        else 
        {
            throw new Exception(book.getTitle() + " - Book was not borrowed.");
        }
    }
}
