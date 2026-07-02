import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LibraryGUI extends JFrame 
{
    private Library library;
    private JTable booksTable;
    private JTable membersTable;
    private JTable borrowedBooksTable;
    
    private JTextField bookTitleField;
    private JTextField bookAuthorField;
    private JTextField bookISBNField;
    private JTextField bookPublicationDateField;
    private JTextField bookCopiesField;
    
    private JTextField memberNameField;
    private JTextField memberIDField;
    
    private JTextField borrowISBNField;
    private JTextField borrowMemberIDField;

    public LibraryGUI() 
    {
        library = new Library();
        setTitle("Library Management System");
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(null); 

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel bookPanel = createBookPanel();
        tabbedPane.addTab("Books", bookPanel);

        JPanel memberPanel = createMemberPanel();
        tabbedPane.addTab("Members", memberPanel);

        JPanel borrowPanel = createBorrowPanel();
        tabbedPane.addTab("Borrow/Return", borrowPanel);

        JPanel borrowedBooksPanel = createBorrowedBooksPanel();
        tabbedPane.addTab("Borrowed Books", borrowedBooksPanel);

        add(tabbedPane, BorderLayout.CENTER);
        
        updateBookTable((DefaultTableModel) booksTable.getModel());
        updateMemberTable((DefaultTableModel) membersTable.getModel());
        updateBorrowedBooksTable((DefaultTableModel) borrowedBooksTable.getModel());
    }

    private JPanel createBookPanel() 
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
    
        booksTable = new JTable();
        DefaultTableModel bookTableModel = new DefaultTableModel(new Object[]{"Title", "Author", "ISBN", "Publication Date", "Available Copies"}, 0);
        booksTable.setModel(bookTableModel);
    
        JScrollPane bookScrollPane = new JScrollPane(booksTable);
        panel.add(bookScrollPane, BorderLayout.CENTER);
    
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(6, 2));
    
        controlPanel.add(new JLabel("Title:"));
        bookTitleField = new JTextField();
        controlPanel.add(bookTitleField);
    
        controlPanel.add(new JLabel("Author:"));
        bookAuthorField = new JTextField();
        controlPanel.add(bookAuthorField);
    
        controlPanel.add(new JLabel("ISBN:"));
        bookISBNField = new JTextField();
        controlPanel.add(bookISBNField);
    
        controlPanel.add(new JLabel("Publication Date:"));
        bookPublicationDateField = new JTextField();
        controlPanel.add(bookPublicationDateField);
    
        controlPanel.add(new JLabel("Available Copies:"));
        bookCopiesField = new JTextField();
        controlPanel.add(bookCopiesField);
    
        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> addBook());
        controlPanel.add(addBookButton);
    
        JButton removeBookButton = new JButton("Remove Book");
        removeBookButton.addActionListener(e -> removeBook());
        controlPanel.add(removeBookButton);
    
        panel.add(controlPanel, BorderLayout.SOUTH);
    
        return panel;
    }
    

    private JPanel createMemberPanel() 
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        membersTable = new JTable();
        DefaultTableModel memberTableModel = new DefaultTableModel(new Object[]{"Member ID", "Name"}, 0);
        membersTable.setModel(memberTableModel);

        JScrollPane memberScrollPane = new JScrollPane(membersTable);
        panel.add(memberScrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 2));

        controlPanel.add(new JLabel("Member Name:"));
        memberNameField = new JTextField();
        controlPanel.add(memberNameField);

        controlPanel.add(new JLabel("Member ID:"));
        memberIDField = new JTextField();
        controlPanel.add(memberIDField);

        JButton addMemberButton = new JButton("Add Member");
        addMemberButton.addActionListener(e -> addMember());
        controlPanel.add(addMemberButton);

        JButton removeMemberButton = new JButton("Remove Member");
        removeMemberButton.addActionListener(e -> removeMember());
        controlPanel.add(removeMemberButton);

        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBorrowPanel() 
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Book ISBN:"));
        borrowISBNField = new JTextField();
        panel.add(borrowISBNField);

        panel.add(new JLabel("Member ID:"));
        borrowMemberIDField = new JTextField();
        panel.add(borrowMemberIDField);

        JButton borrowButton = new JButton("Borrow Book");
        borrowButton.addActionListener(e -> borrowBook());
        panel.add(borrowButton);

        JButton returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> returnBook());
        panel.add(returnButton);

        return panel;
    }

    private JPanel createBorrowedBooksPanel() 
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
    
        borrowedBooksTable = new JTable();
        DefaultTableModel borrowedBooksTableModel = new DefaultTableModel(new Object[]{"Borrow ID", "Member ID", "Book ISBN", "Borrow Date"}, 0);
        borrowedBooksTable.setModel(borrowedBooksTableModel);
    
        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        return panel;
    }

    

    private void updateBookTable(DefaultTableModel tableModel) 
    {
        tableModel.setDataVector(getBooksData(), new Object[]{"Title", "Author", "ISBN", "Publication Date", "Available Copies"});
    }

    private void updateMemberTable(DefaultTableModel tableModel) 
    {
        tableModel.setDataVector(getMembersData(), new Object[]{"Member ID", "Name"});
    }

    private Object[][] getBooksData() 
    {
        List<Book> books = library.getBooks();
        Object[][] data = new Object[books.size()][5];
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            data[i][0] = book.getTitle();
            data[i][1] = book.getAuthor();
            data[i][2] = book.getIsbn();
            data[i][3] = book.getPublicationDate();
            data[i][4] = book.getAvailableCopies();
        }
        return data;
    }

    private Object[][] getMembersData() 
    {
        List<Member> members = library.getMembers();
        Object[][] data = new Object[members.size()][2];
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            data[i][0] = member.getMemberID();
            data[i][1] = member.getName();
        }
        return data;
    }

    private void addBook() 
    {
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        String isbn = bookISBNField.getText();
        String pubDate = bookPublicationDateField.getText();
        String copiesText = bookCopiesField.getText();
        
        int availableCopies = 0;
        try {
            availableCopies = Integer.parseInt(copiesText);
        } catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Invalid number of copies. Please enter a valid integer.");
            return;
        }
        
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || pubDate.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.");
            return;
        }

        Book existingBook = library.findBookByISBN(isbn);
        if (existingBook != null) 
        {
            JOptionPane.showMessageDialog(this, "A book with this ISBN already exists.");
            return;
        }

        Book book = new Book(title, author, pubDate, isbn, availableCopies);
        library.addBook(book);
        updateBookTable((DefaultTableModel) booksTable.getModel());
        JOptionPane.showMessageDialog(this, "Book added successfully!");
    }
    

    private void removeBook() 
    {
        String isbn = bookISBNField.getText();
        Book book = library.findBookByISBN(isbn);
        if (book != null) {
            library.removeBook(book);
            updateBookTable((DefaultTableModel) booksTable.getModel());
            JOptionPane.showMessageDialog(this, "Book removed successfully!");
        }
    }

    private void addMember() 
    {
        String memberName = memberNameField.getText();
        String memberIDText = memberIDField.getText();
    
        int memberId = 0;
        try 
        {
            memberId = Integer.parseInt(memberIDText);
        }    
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Invalid Member ID. Please enter a valid integer.");
            return;
        }
    
        if (memberName.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Member Name cannot be empty.");
            return;
        }

        Member existingMember = library.findMemberByID(memberId);
        if (existingMember != null) 
        {
            JOptionPane.showMessageDialog(this, "A member with this ID already exists.");
            return;
        }
        Member member = new Member(memberName, memberId);
        library.addMember(member);
        updateMemberTable((DefaultTableModel) membersTable.getModel());
    }

    private void removeMember() 
    {
        int memberId = Integer.parseInt(memberIDField.getText());
        Member member = library.findMemberByID(memberId);
        if (member != null) 
        {
            library.removeMember(member);
            updateMemberTable((DefaultTableModel) membersTable.getModel());
            JOptionPane.showMessageDialog(this, "Member removed successfully!");
        }
    }

    private void borrowBook() 
    {
        String isbn = borrowISBNField.getText(); 
        Book book = library.findBookByISBN(isbn); 
        int memberId = Integer.parseInt(borrowMemberIDField.getText());  
        Member member = library.findMemberByID(memberId);  

        if (book != null && member != null) 
        { 
            if (book.getAvailableCopies() > 0) 
            {
                try 
                {
                    library.borrowBook(book, member); 
                    updateBookTable((DefaultTableModel) booksTable.getModel());
                    updateBorrowedBooksTable((DefaultTableModel) borrowedBooksTable.getModel()); // Update borrowed books table
                    JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
                } 
                catch (Exception e) 
                {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "No available copies to borrow.");
            }
        }    
        else 
        {
            JOptionPane.showMessageDialog(this, "Book or Member not found.");
        }
    }

    

    private void returnBook() 
    {
        String isbn = borrowISBNField.getText();
        Book book = library.findBookByISBN(isbn); 
        int memberId = Integer.parseInt(borrowMemberIDField.getText());
        Member member = library.findMemberByID(memberId);

        if (book != null && member != null) 
        {
            boolean isBorrowed = false;
            for (Object[] record : library.getBorrowedBooks()) 
            {
                if ((int) record[0] == memberId && record[1].equals(isbn)) 
                {
                    isBorrowed = true;
                    break;
                }
            }
            if (isBorrowed) 
            {
                try 
                {
                    library.returnBook(book, member);
                    updateBookTable((DefaultTableModel) booksTable.getModel());
                    updateBorrowedBooksTable((DefaultTableModel) borrowedBooksTable.getModel()); // Update borrowed books table
                    JOptionPane.showMessageDialog(this, "Book returned successfully!");
                } 
                catch (Exception e) 
                {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
            } 
            else 
            {
            JOptionPane.showMessageDialog(this, "This book has not been borrowed by this member.");
            }
        } 
        else 
        {
            JOptionPane.showMessageDialog(this, "Book or Member not found.");
        }
    }



    private void updateBorrowedBooksTable(DefaultTableModel tableModel) 
    {   
        tableModel.setDataVector(getBorrowedBooksData(), new Object[]{"Member ID", "Book ISBN", "Borrow Date"});
    }

    private Object[][] getBorrowedBooksData() 
    {
        List<Object[]> borrowedBooks = library.getBorrowedBooks();
        Object[][] data = new Object[borrowedBooks.size()][3];
        for (int i = 0; i < borrowedBooks.size(); i++) 
        {
            Object[] record = borrowedBooks.get(i);
            data[i][0] = record[0]; 
            data[i][1] = record[1]; 
            data[i][2] = record[2]; 
        }
        return data;
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new LibraryGUI().setVisible(true));
    }
}
