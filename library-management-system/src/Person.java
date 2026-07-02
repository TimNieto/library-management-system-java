public abstract class Person 
{
    private String name;
    private int ID;

    public Person(String name, int ID) 
    {
        this.name = name;
        this.ID = ID;
    }

    public String getName() 
    {
        return name;
    }

    public int getID() 
    {
        return ID;
    }

    public abstract void borrowBook(Book book) throws Exception;

    public abstract void returnBook(Book book) throws Exception;
}
