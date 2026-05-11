package gui;
import service.LibraryService;
import model.Book;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class BookPanel extends JPanel {
    private LibraryService service;
    private JTable bookTable;
    private JTextField searchField;
    
    public BookPanel(LibraryService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 255));  
        
        loadSampleBooks();
        
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(100, 150, 255));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton searchBtn = new JButton("🔍 Search");
        JButton refreshBtn = new JButton("🔄 Refresh All");
        
        
        styleButton(searchBtn);
        styleButton(refreshBtn);
        
        searchBtn.addActionListener(e -> updateTable(searchField.getText()));
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            updateTable("");
        });
        
        topPanel.add(new JLabel("Search Title: "));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(refreshBtn);
        add(topPanel, BorderLayout.NORTH);
        
       
        bookTable = new JTable();
        styleTable();
        add(new JScrollPane(bookTable), BorderLayout.CENTER);
        
        updateTable("");  
    }
    
    private void loadSampleBooks() {
        String[][] books = {
            {"F001", "Harry Potter 1", "JK Rowling"}, {"F002", "Lord of Rings", "Tolkien"},
            {"T001", "Clean Code", "Robert Martin"}, {"T002", "Java: The Complete Reference", "Herbert Schildt"},
            {"F003", "1984", "George Orwell"}, {"F004", "Dune", "Frank Herbert"},
            {"T003", "Head First Java", "Kathy Sierra"}, {"T004", "Effective Java", "Joshua Bloch"},
            {"F005", "The Hobbit", "Tolkien"}, {"F006", "To Kill a Mockingbird", "Harper Lee"},
            {"T005", "Design Patterns", "Gang of Four"}, {"F007", "Pride and Prejudice", "Jane Austen"},
            {"T006", "Refactoring", "Martin Fowler"}, {"F008", "The Great Gatsby", "F. Scott Fitzgerald"},
            {"T007", "Cracking the Coding Interview", "Gayle Laakmann"}, {"F009", "Brave New World", "Aldous Huxley"}
        };
        
        for (String[] b : books) {
            service.addBook(b[0].startsWith("F") ? 
                new model.FictionBook(b[0], b[1], b[2]) :
                new model.TextBook(b[0], b[1], b[2]));
        }
    }
    
    private void styleTable() {
        bookTable.setRowHeight(40);
        bookTable.setGridColor(new Color(200, 220, 255));
        bookTable.setBackground(new Color(255, 255, 255));
        bookTable.setSelectionBackground(new Color(100, 150, 255));
        bookTable.setRowSelectionAllowed(true);
        
        
        bookTable.getTableHeader().setBackground(new Color(70, 130, 255));
        bookTable.getTableHeader().setForeground(Color.WHITE);
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookTable.getTableHeader().setOpaque(false);
    }
    
    private void styleButton(JButton btn) {
        btn.setBackground(new Color(70, 130, 255));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(50, 110, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(70, 130, 255));
            }
        });
    }
    
    private void updateTable(String search) {
        List<Book> books = search.isEmpty() ? 
            service.getAllBooks() : service.searchBooks(search);
            
        bookTable.setModel(new AbstractTableModel() {
            public int getRowCount() { return books.size(); }
            public int getColumnCount() { return 3; }
            public Object getValueAt(int row, int col) {
                Book b = books.get(row);
                return switch(col) {
                    case 0 -> "📖 " + b.getIsbn();
                    case 1 -> b.getTitle();
                    case 2 -> b.getAuthor();
                    default -> "";
                };
            }
            @Override
            public String getColumnName(int col) {
                return switch(col) {
                    case 0 -> "ISBN";
                    case 1 -> "Title";
                    case 2 -> "Author";
                    default -> "";
                };
            }
        });
    }
}
