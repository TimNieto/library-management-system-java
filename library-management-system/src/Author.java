import java.util.*;

public class Author 
{
    private String name;
    private int authorID;
    private List<Book> books;

    public Author(String name, int authorID) 
    {
        this.name = name;
        this.authorID = authorID;
        this.books = new ArrayList<>();
    }

    public String getName() 
    { 
        return name; 
    }
    public void setName(String name) 
    { 
        this.name = name; 
    }

    public int getAuthorID() 
    { 
        return authorID; 
    }
    public void setAuthorID(int authorID) 
    { 
        this.authorID = authorID; 
    }

    public List<Book> getBooks() 
    { 
        return books; 
    }
    public void setBooks(List<Book> books) 
    { 
        this.books = books; 
    }

    public void addBook(Book book) 
    {
        books.add(book);
    }

    public void removeBook(Book book) 
    {
        books.remove(book);
    }
}
