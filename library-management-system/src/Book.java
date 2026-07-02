public class Book implements LibraryItem //Abstraction
{
    private String title;
    private String author;
    private String isbn;
    private String publicationDate;
    private int availableCopies;

    public Book(String title, String author, String publicationDate, String isbn, int availableCopies) 
    {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.availableCopies = availableCopies;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getAuthor() 
    {
        return author;
    }

    public void setAuthor(String author) 
    {
        this.author = author;
    }

    public String getIsbn() 
    {
        return isbn; 
    }

    public void setIsbn(String isbn) 
    {
        this.isbn = isbn;
    }

    public String getPublicationDate() 
    {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) 
    {
        this.publicationDate = publicationDate;
    }

    public int getAvailableCopies() 
    {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) 
    {
        this.availableCopies = availableCopies;
    }
@Override
    public void borrowBook() throws Exception 
    {
        if (availableCopies > 0) {
            availableCopies--; 
        } else {
            throw new Exception("No copies left to borrow.");
        }
    }
    
@Override
    public void returnBook() {
        availableCopies++;
    }
}
