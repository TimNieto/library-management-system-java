import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class LibraryGUI extends JFrame {
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

    public LibraryGUI() {
        library = new Library();

        setTitle("Library Management System");
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Books", createBookPanel());
        tabbedPane.addTab("Members", createMemberPanel());
        tabbedPane.addTab("Borrow/Return", createBorrowPanel());
        tabbedPane.addTab("Borrowed Books", createBorrowedBooksPanel());

        add(tabbedPane, BorderLayout.CENTER);

        refreshTables();
    }

    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        booksTable = new JTable();
        DefaultTableModel bookTableModel = new DefaultTableModel(
                new Object[] { "Title", "Author", "ISBN", "Publication Date", "Available Copies" }, 0);
        booksTable.setModel(bookTableModel);

        panel.add(new JScrollPane(booksTable), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(6, 2));

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
        addBookButton.addActionListener(event -> addBook());
        controlPanel.add(addBookButton);

        JButton removeBookButton = new JButton("Remove Book");
        removeBookButton.addActionListener(event -> removeBook());
        controlPanel.add(removeBookButton);

        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMemberPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        membersTable = new JTable();
        DefaultTableModel memberTableModel = new DefaultTableModel(new Object[] { "Member ID", "Name" }, 0);
        membersTable.setModel(memberTableModel);

        panel.add(new JScrollPane(membersTable), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(3, 2));

        controlPanel.add(new JLabel("Member Name:"));
        memberNameField = new JTextField();
        controlPanel.add(memberNameField);

        controlPanel.add(new JLabel("Member ID:"));
        memberIDField = new JTextField();
        controlPanel.add(memberIDField);

        JButton addMemberButton = new JButton("Add Member");
        addMemberButton.addActionListener(event -> addMember());
        controlPanel.add(addMemberButton);

        JButton removeMemberButton = new JButton("Remove Member");
        removeMemberButton.addActionListener(event -> removeMember());
        controlPanel.add(removeMemberButton);

        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBorrowPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Book ISBN:"));
        borrowISBNField = new JTextField();
        panel.add(borrowISBNField);

        panel.add(new JLabel("Member ID:"));
        borrowMemberIDField = new JTextField();
        panel.add(borrowMemberIDField);

        JButton borrowButton = new JButton("Borrow Book");
        borrowButton.addActionListener(event -> borrowBook());
        panel.add(borrowButton);

        JButton returnButton = new JButton("Return Book");
        returnButton.addActionListener(event -> returnBook());
        panel.add(returnButton);

        return panel;
    }

    private JPanel createBorrowedBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        borrowedBooksTable = new JTable();
        DefaultTableModel borrowedBooksTableModel = new DefaultTableModel(
                new Object[] { "Member ID", "Book ISBN", "Borrow Date" }, 0);
        borrowedBooksTable.setModel(borrowedBooksTableModel);

        panel.add(new JScrollPane(borrowedBooksTable), BorderLayout.CENTER);

        return panel;
    }

    private void addBook() {
        String title = bookTitleField.getText().trim();
        String author = bookAuthorField.getText().trim();
        String isbn = bookISBNField.getText().trim();
        String publicationDate = bookPublicationDateField.getText().trim();
        String copiesText = bookCopiesField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publicationDate.isEmpty()
                || copiesText.isEmpty()) {
            showMessage("All book fields must be filled out.");
            return;
        }

        Integer availableCopies = parseInteger(copiesText, "Invalid number of copies. Please enter a valid integer.");
        if (availableCopies == null) {
            return;
        }

        if (availableCopies < 0) {
            showMessage("Available copies cannot be negative.");
            return;
        }

        if (library.findBookByISBN(isbn) != null) {
            showMessage("A book with this ISBN already exists.");
            return;
        }

        Book book = new Book(title, author, publicationDate, isbn, availableCopies);
        library.addBook(book);

        updateBookTable();
        clearBookFields();
        showMessage("Book added successfully.");
    }

    private void removeBook() {
        String isbn = bookISBNField.getText().trim();

        if (isbn.isEmpty()) {
            showMessage("Please enter the ISBN of the book to remove.");
            return;
        }

        Book book = library.findBookByISBN(isbn);

        if (book == null) {
            showMessage("Book not found.");
            return;
        }

        library.removeBook(book);
        updateBookTable();
        clearBookFields();
        showMessage("Book removed successfully.");
    }

    private void addMember() {
        String memberName = memberNameField.getText().trim();
        String memberIDText = memberIDField.getText().trim();

        if (memberName.isEmpty() || memberIDText.isEmpty()) {
            showMessage("Member name and member ID must be filled out.");
            return;
        }

        Integer memberId = parseInteger(memberIDText, "Invalid member ID. Please enter a valid integer.");
        if (memberId == null) {
            return;
        }

        if (library.findMemberByID(memberId) != null) {
            showMessage("A member with this ID already exists.");
            return;
        }

        Member member = new Member(memberName, memberId);
        library.addMember(member);

        updateMemberTable();
        clearMemberFields();
        showMessage("Member added successfully.");
    }

    private void removeMember() {
        String memberIDText = memberIDField.getText().trim();

        if (memberIDText.isEmpty()) {
            showMessage("Please enter the member ID to remove.");
            return;
        }

        Integer memberId = parseInteger(memberIDText, "Invalid member ID. Please enter a valid integer.");
        if (memberId == null) {
            return;
        }

        Member member = library.findMemberByID(memberId);

        if (member == null) {
            showMessage("Member not found.");
            return;
        }

        library.removeMember(member);
        updateMemberTable();
        clearMemberFields();
        showMessage("Member removed successfully.");
    }

    private void borrowBook() {
        String isbn = borrowISBNField.getText().trim();
        String memberIDText = borrowMemberIDField.getText().trim();

        if (isbn.isEmpty() || memberIDText.isEmpty()) {
            showMessage("Book ISBN and member ID must be filled out.");
            return;
        }

        Integer memberId = parseInteger(memberIDText, "Invalid member ID. Please enter a valid integer.");
        if (memberId == null) {
            return;
        }

        Book book = library.findBookByISBN(isbn);
        Member member = library.findMemberByID(memberId);

        if (book == null || member == null) {
            showMessage("Book or member not found.");
            return;
        }

        if (book.getAvailableCopies() <= 0) {
            showMessage("No available copies to borrow.");
            return;
        }

        try {
            library.borrowBook(book, member);
            refreshTables();
            clearBorrowFields();
            showMessage("Book borrowed successfully.");
        } catch (Exception exception) {
            showMessage("Error: " + exception.getMessage());
        }
    }

    private void returnBook() {
        String isbn = borrowISBNField.getText().trim();
        String memberIDText = borrowMemberIDField.getText().trim();

        if (isbn.isEmpty() || memberIDText.isEmpty()) {
            showMessage("Book ISBN and member ID must be filled out.");
            return;
        }

        Integer memberId = parseInteger(memberIDText, "Invalid member ID. Please enter a valid integer.");
        if (memberId == null) {
            return;
        }

        Book book = library.findBookByISBN(isbn);
        Member member = library.findMemberByID(memberId);

        if (book == null || member == null) {
            showMessage("Book or member not found.");
            return;
        }

        if (!isBookBorrowedByMember(memberId, isbn)) {
            showMessage("This book has not been borrowed by this member.");
            return;
        }

        try {
            library.returnBook(book, member);
            refreshTables();
            clearBorrowFields();
            showMessage("Book returned successfully.");
        } catch (Exception exception) {
            showMessage("Error: " + exception.getMessage());
        }
    }

    private boolean isBookBorrowedByMember(int memberId, String isbn) {
        for (Object[] record : library.getBorrowedBooks()) {
            if (record[0].equals(memberId) && record[1].equals(isbn)) {
                return true;
            }
        }

        return false;
    }

    private Integer parseInteger(String value, String errorMessage) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            showMessage(errorMessage);
            return null;
        }
    }

    private void refreshTables() {
        updateBookTable();
        updateMemberTable();
        updateBorrowedBooksTable();
    }

    private void updateBookTable() {
        DefaultTableModel tableModel = (DefaultTableModel) booksTable.getModel();
        tableModel.setDataVector(getBooksData(),
                new Object[] { "Title", "Author", "ISBN", "Publication Date", "Available Copies" });
    }

    private void updateMemberTable() {
        DefaultTableModel tableModel = (DefaultTableModel) membersTable.getModel();
        tableModel.setDataVector(getMembersData(), new Object[] { "Member ID", "Name" });
    }

    private void updateBorrowedBooksTable() {
        DefaultTableModel tableModel = (DefaultTableModel) borrowedBooksTable.getModel();
        tableModel.setDataVector(getBorrowedBooksData(), new Object[] { "Member ID", "Book ISBN", "Borrow Date" });
    }

    private Object[][] getBooksData() {
        List<Book> books = library.getBooks();
        Object[][] data = new Object[books.size()][5];

        for (int index = 0; index < books.size(); index++) {
            Book book = books.get(index);

            data[index][0] = book.getTitle();
            data[index][1] = book.getAuthor();
            data[index][2] = book.getIsbn();
            data[index][3] = book.getPublicationDate();
            data[index][4] = book.getAvailableCopies();
        }

        return data;
    }

    private Object[][] getMembersData() {
        List<Member> members = library.getMembers();
        Object[][] data = new Object[members.size()][2];

        for (int index = 0; index < members.size(); index++) {
            Member member = members.get(index);

            data[index][0] = member.getMemberID();
            data[index][1] = member.getName();
        }

        return data;
    }

    private Object[][] getBorrowedBooksData() {
        List<Object[]> borrowedBooks = library.getBorrowedBooks();
        Object[][] data = new Object[borrowedBooks.size()][3];

        for (int index = 0; index < borrowedBooks.size(); index++) {
            Object[] record = borrowedBooks.get(index);

            data[index][0] = record[0];
            data[index][1] = record[1];
            data[index][2] = record[2];
        }

        return data;
    }

    private void clearBookFields() {
        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookISBNField.setText("");
        bookPublicationDateField.setText("");
        bookCopiesField.setText("");
    }

    private void clearMemberFields() {
        memberNameField.setText("");
        memberIDField.setText("");
    }

    private void clearBorrowFields() {
        borrowISBNField.setText("");
        borrowMemberIDField.setText("");
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryGUI().setVisible(true));
    }
}